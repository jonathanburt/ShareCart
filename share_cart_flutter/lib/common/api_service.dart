import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:share_cart_flutter/common/exceptions.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:http/http.dart' as http;

/// Defines the interface between the back end and front end.
class ApiService {
  final String baseUrl;
  final http.Client client = http.Client();
  ThisUserDetails? _userDetails;

  /// Returns the currently logged-in user's details.
  ThisUserDetails? get userDetails => _userDetails;

  ApiService({required this.baseUrl});

  final FlutterSecureStorage _storage =
      const FlutterSecureStorage(); // TODO make sure this works with target platforms and everyones machines
  var baseHeaders = {"Content-Type": "application/json"};

  /// Retrieves the JWT token from secure storage.
  Future getJWT() async {
    String? jwt = await _storage.read(key: 'AuthToken');
    if (jwt == null) throw UnimplementedError(); // TODO figure out what to throw here
    return jwt;
  }

  /// Authenticates a user with the provided username and password.
  Future authenticateUser(String username, String password,
      VoidCallback onSuccess, VoidCallback onFailure) async {
    final response = await client.post(
      Uri.parse('$baseUrl/api/auth/signin'),
      headers: baseHeaders,
      body: jsonEncode({"username": username, "password": password}),
    );

    if (response.statusCode == 200) {
      onSuccess.call();
      final responseJson = jsonDecode(response.body) as Map<String, dynamic>;
      await _storage.write(key: 'AuthToken', value: responseJson['token']);
      print(await _storage.read(key: 'AuthToken'));
      _userDetails = ThisUserDetails(
        username,
        responseJson['email'],
        responseJson['userId'],
        HttpDate.parse(responseJson['createdAtFormatted']),
      );
      return;
    } else {
      onFailure.call();
      _userDetails = null;
      return;
    }
  }

  /// Creates a new user with the provided username, email, and password.
  Future createUser(String username, String email, String password,
      VoidCallback onSuccess, VoidCallback onFailure) async {
    final response = await client.post(
      Uri.parse('$baseUrl/api/auth/signup'),
      headers: baseHeaders,
      body: jsonEncode({
        "username": username,
        "email": email,
        "password": password,
      }),
    );
    if (response.statusCode == 200) {
      onSuccess.call();
      return;
    } else {
      onFailure.call();
      return;
    }
  }

  /// Fetches a specific item from a list within a group.
  Future<ShareCartItem?> fetchItem(int groupId, int itemId) {
    // TODO implement fetchItem
    throw UnimplementedError();
  }

  /// Fetches all items within a specific group.
  Future<Map<int, ShareCartItem>> fetchItems(int groupId) async {
    final headers = await authorizedHeaders();

    final itemsResponse = await client.get(
      Uri.parse('$baseUrl/api/group/$groupId/item/getall'),
      headers: headers,
    );

    if (itemsResponse.statusCode == 200) {
      final Iterable jsonResponse = jsonDecode(itemsResponse.body);
      return <int, ShareCartItem>{
        for (final item in jsonResponse)
          item["itemId"]: ShareCartItem.fromJson(item)
      };
    }
    // TODO implement fetchItems
    throw UnimplementedError();
  }

  /// Fetches all shopping lists within a specific group.
  Future<Map<int, ShareCartList>> fetchLists(int groupId) async {
    final headers = await authorizedHeaders();

    final response = await client.get(
      Uri.parse('$baseUrl/api/group/$groupId/list/getall'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final Iterable responseBody = jsonDecode(response.body);
      return <int, ShareCartList>{
        for (final list in responseBody)
          list["listId"]: ShareCartList.fromJson(list)
      };
    }

    // TODO implement fetchLists fail state
    throw UnimplementedError();
  }

  /// Fetches a specific shopping list within a group.
  Future<ShareCartList?> fetchList(int groupId, int listId) async {
    final headers = await authorizedHeaders();

    final response = await client.get(
      Uri.parse('$baseUrl/api/group/$groupId/list/$listId/get'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final jsonResponse = jsonDecode(response.body);
      return ShareCartList.fromJson(jsonResponse);
    }
    // TODO implement fetchList fail state
    throw UnimplementedError();
  }

  /// Fetches all groups that the current user is a member of.
  Future<List<GroupReturn>> fetchGroups() async {
    final headers = await authorizedHeaders();
    final response = await client.get(Uri.parse('$baseUrl/api/group/get/all'), headers: headers);

    if (response.statusCode == 200) {
      final Iterable jsonResponse = jsonDecode(response.body);
      return List<GroupReturn>.from(jsonResponse.map((group) => (
        group: ShareCartGroup.fromJson(group, _roleFromGroupResponse(group)!), //This is ugly and idk if it will work
        members: List<GroupMember>.from((group["members"] as Iterable).map((member) => GroupMember.fromJson(member))),
        invites: List<GroupInvite>.from((group["invites"] as Iterable).map((invite) => GroupInvite.fromJson(invite)))
      )));
    }
    throw UnimplementedError();
  }

  /// Fetches details for a specific group.
  Future<ShareCartGroup?> fetchGroup(int groupId) async {
    // TODO implement fetchGroup
    throw UnimplementedError();
  }

  /// Clears all stored data and notifies the caller when finished.
  Future logOut(VoidCallback onLogOut) async {
    await _storage.deleteAll(); //Clear all stored data on log out
    onLogOut.call();
    return;
  }

  /// Allows the current user to leave a specific group.
  Future leaveGroup(String groupId, VoidCallback onSuccess,
      Function(String) onFailure) async {
    final headers = await authorizedHeaders();

    final response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/leave'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      onSuccess();
    } else {
      var errorMessage = "Failed to leave group";
      try {
        final responseBody = jsonDecode(response.body) as Map<String, dynamic>;
        errorMessage = responseBody['message'] ?? errorMessage;
      } catch (e) {
        // Use default error message if response parsing fails
      }
      onFailure(errorMessage);
    }
  }

  /// Fetches all pending invites for the current user.
  Future<List<MyInvite>> fetchInvites() async {
    final headers = await authorizedHeaders();
    if (_userDetails == null) throw UnimplementedError();
    final int userId = _userDetails!.userId;
    final response = await client.get(
      Uri.parse('$baseUrl/users/$userId/invites/get'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      return List.from((jsonDecode(response.body) as Iterable)
          .map((invite) => MyInvite.fromJson(invite as Map<String, dynamic>)));
    }
    // TODO fail state
    throw UnimplementedError();
  }

  /// Creates a new group with the given name.
  Future<ShareCartGroup?> createGroup(String name) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/group/create'),
      body: jsonEncode({"name": name}),
      headers: headers,
    );
    switch (response.statusCode) {
      case 201:
        return ShareCartGroup.fromJson(
            jsonDecode(response.body) as Map<String, dynamic>, GroupRole.ADMIN);
      case 409:
        throw ApiConflictException("A group with name $name already exists");
      default:
        throw ApiFailureException(
            "Could not create group $name, request failed with code ${response.statusCode}");
    }
  }

  /// Changes the quantity of a specific item in a shopping list.
  Future changeItemQuantity(
      int groupId, int listId, int itemId, int quantity) async {
    final headers = await authorizedHeaders();
    final response = await client.put(
      Uri.parse('$baseUrl/api/group/$groupId/item/$listId/$itemId/quantity'),
      headers: headers,
      body: jsonEncode({"quantity": quantity}),
    );

    if (response.statusCode == 200) {
      return ShareCartListItem.fromJson(jsonDecode(response.body));
    }

    throw UnimplementedError();
  }

  /// Accepts an invitation to join a specific group.
  Future<ShareCartGroup?> acceptInvite(int groupId) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/invite/accept'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> jsonResponse = jsonDecode(response.body);
      return ShareCartGroup.fromJson(
          jsonResponse, _roleFromGroupResponse(jsonResponse) as GroupRole);
    }
    // TODO implement acceptInvite fail stat
    throw UnimplementedError();
  }

  /// Declines an invitation to join a specific group.
  Future declineInvite(int groupId) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/$groupId/invite/decline'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      return;
    }
    // TODO implement fail state
    throw UnimplementedError();
  }

  /// Adds an existing item to a specific shopping list within a group.
  Future<ShareCartList?> addItemToList(
      int groupId, int listId, int itemId, int quantity,
      {bool communal = false}) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/item/$listId/add'),
      headers: headers,
      body: jsonEncode({
        "itemId": itemId,
        "quantity": quantity,
        "communal": communal,
        "bought": false,
      }),
    );

    switch (response.statusCode) {
      case 201:
        return null;
      case 409:
        throw ApiConflictException("Item has already been added to list");
      default:
        throw ApiFailureException("Could not add item to list");
    }
  }

  /// Creates a new item within a specific group.
  Future<ShareCartItem?> createItem(int groupId, String name,
      {String description = "", String category = "", double price = 0.0}) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/item/create'),
      headers: headers,
      body: jsonEncode({
        "name": name,
        "description": description,
        "category": category,
        "price": price,
      }),
    );

    switch (response.statusCode) {
      case 201:
        return ShareCartItem.fromJson(jsonDecode(response.body));
      case 401:
        throw ApiUnauthorizedException(
            "Do not have permissions to create items in this group");
      case 409:
        throw ApiConflictException("Item already exists in this group");
      default:
        throw ApiFailureException("Could not create item");
    }
  }

  /// Creates a new shopping list within a specific group.
  Future<ShareCartList?> createList(int groupId, String name) async {
    final headers = await authorizedHeaders();
    final response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/list/add'),
      headers: headers,
      body: jsonEncode({'name': name}),
    );

    switch (response.statusCode) {
      case 201:
        return ShareCartList.fromJson(jsonDecode(response.body));
      case 409:
        throw ApiConflictException(
            "List already exists with this name in this group");
      default:
        throw ApiUnauthorizedException("Cannot create list");
    }
  }

  /// Constructs the authorized headers including the JWT token.
  Future<Map<String, String>> authorizedHeaders() async {
    final headers = baseHeaders;
    final String jwt = await getJWT();
    headers["Authorization"] = "Bearer $jwt";
    return headers;
  }

  /// Extracts the user's role within a group from the group response.
  GroupRole? _roleFromGroupResponse(Map<String, dynamic> input) {
    final Iterable members = input["members"];
    return (members.firstWhere((member) => member["userId"] == userDetails!.userId))["role"]
        .toString()
        .groupRole;
  }
}