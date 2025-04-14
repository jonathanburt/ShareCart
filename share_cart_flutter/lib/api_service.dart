import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:http/http.dart' as http;

abstract class ApiService {
  Future<Map<int, ShareCartItem>> fetchItems(int groupId);
  Future<ShareCartItem?> fetchItem(int groupId, int itemId) ;
  Future<List<ShareCartStore>> fetchStores();
  Future<ShareCartStore?> fetchStore(int storeId);
  Future<Map<int, ShareCartList>> fetchLists(int groupId);
  Future<ShareCartList?> fetchList(int groupId, int listId);
  Future<List<GroupReturn>> fetchGroups();
  Future<ShareCartGroup?> fetchGroup(int groupId);
  Future<List<MyInvite>> fetchInvites();
  Future<ShareCartListItem> changeItemQuantity(int groupId, int listId, int itemId, int quantity);
  Future<ShareCartGroup?> acceptInvite(int groupId);
  Future<void> declineInvite(int groupId);

  Future<ShareCartGroup?> createGroup(String name);
  Future<ShareCartList?> createList(int groupId, String name);
  Future<ShareCartItem?> createItem(int groupId, String name, {String description = "", String category = "", double price = 0.0});
  Future<ShareCartList?> addItemToList(int groupId, int listId, int itemId, int quantity, {bool communal = false});

  Future<void> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<void> logOut(VoidCallback onLogOut);
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String) onFailure);
}

class MockApiService implements ApiService {
  final List<ShareCartGroup> groups = [
    ShareCartGroup("Family", 103, GroupRole.ADMIN, DateTime.now()),
    ShareCartGroup("Roommates", 921, GroupRole.SHOPPER, DateTime.now())
  ];
  final Map<int, List<GroupMember>> groupMemberships = {
    103: [
      GroupMember("Jimmy", 9, GroupRole.ADMIN, DateTime.now()),
      GroupMember("Mom", 21, GroupRole.SHOPPER, DateTime.now()),
      GroupMember("Dan", 90, GroupRole.MEMBER, DateTime.now())
    ],
    921: [
      GroupMember("Jimmy", 9, GroupRole.SHOPPER, DateTime.now()),
      GroupMember("Dale", 2735, GroupRole.ADMIN, DateTime.now())
    ]
  };
  final Map<int, List<GroupInvite>> groupInvites = {
    103: [
      GroupInvite("Karen", 57, DateTime.now())
    ],
    921: []
  };
  final Map<int, Map<int, ShareCartItem>> items = {
    103: {
      1: ShareCartItem("Bananas", "Fresh ripe bananas", "produce", 1.29, 1, DateTime.now()),
      2: ShareCartItem("Whole Milk", "1 gallon whole milk", "dairy", 3.49, 2, DateTime.now()),
      3: ShareCartItem("Ground Beef", "Lean ground beef 1 lb", "meat", 5.99, 3, DateTime.now()),
      4: ShareCartItem("Pasta", "Spaghetti noodles 16 oz", "pasta", 1.79, 4, DateTime.now()),
      5: ShareCartItem("Tomato Sauce", "Classic marinara sauce", "canned", 2.19, 5, DateTime.now()),
      6: ShareCartItem("Apples", "Red delicious apples (3 lb)", "produce", 4.29, 6, DateTime.now()),
      7: ShareCartItem("Bread", "Whole wheat sandwich bread", "bakery", 2.99, 7, DateTime.now()),
      8: ShareCartItem("Eggs", "Dozen large eggs", "dairy", 2.79, 8, DateTime.now()),
      9: ShareCartItem("Orange Juice", "Fresh squeezed OJ 64 oz", "beverages", 4.59, 9, DateTime.now()),
      10: ShareCartItem("Cheddar Cheese", "Shredded cheddar cheese", "dairy", 3.89, 10, DateTime.now()),
    },
    921: {
      11: ShareCartItem("Chicken Breast", "Boneless skinless chicken breasts", "meat", 6.49, 11, DateTime.now()),
      12: ShareCartItem("Rice", "Long grain white rice 2 lb", "grains", 2.39, 12, DateTime.now()),
      13: ShareCartItem("Cereal", "Honey oat breakfast cereal", "cereal", 3.79, 13, DateTime.now()),
      14: ShareCartItem("Carrots", "Baby carrots 1 lb", "produce", 1.59, 14, DateTime.now()),
      15: ShareCartItem("Yogurt", "Strawberry Greek yogurt", "dairy", 1.19, 15, DateTime.now()),
      16: ShareCartItem("Peanut Butter", "Creamy peanut butter 16 oz", "spreads", 2.89, 16, DateTime.now()),
      17: ShareCartItem("Tortilla Chips", "Restaurant style chips", "snacks", 3.49, 17, DateTime.now()),
      18: ShareCartItem("Salsa", "Mild tomato salsa", "condiments", 2.99, 18, DateTime.now()),
      19: ShareCartItem("Lettuce", "Romaine hearts (3 pack)", "produce", 3.19, 19, DateTime.now()),
      20: ShareCartItem("Frozen Pizza", "Pepperoni frozen pizza", "frozen", 5.79, 20, DateTime.now()),
    }
    
  };
  final Map<int, Map<int, ShareCartList>> lists = {
    103: {
      1: ShareCartList("Weekly Groceries", 1, 103, DateTime.now(), [
        ShareCartListItem(5, 1, 21, false, false, 2, DateTime.now()),
        ShareCartListItem(1, 1, 90, true, false, 10, DateTime.now()),
        ShareCartListItem(3, 1, 9, false, false, 2, DateTime.now())
      ]),
    },
    921: {
      2: ShareCartList("Sunday Party", 2, 921, DateTime.now(), [
        ShareCartListItem(17, 2, 9, true, false, 3, DateTime.now()),
        ShareCartListItem(18, 2, 2735, true, false, 3, DateTime.now())
      ]),
      3: ShareCartList("Weekley Groceries", 3, 921, DateTime.now(), [
        ShareCartListItem(20, 3, 9, false, false, 2, DateTime.now()),
        ShareCartListItem(16, 3, 2735, false, false, 1, DateTime.now())
      ])
    }
  };

  final List<MyInvite> myInvites = [
    MyInvite("THE HOMIES", 2, DateTime.now())
  ];

  Duration loadTime = Duration(seconds: 1);

  @override
  Future<void> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess.call();
  }

  @override
  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess.call();
  }

  @override
  Future<ShareCartGroup?> fetchGroup(int groupId) {
    // TODO: implement fetchGroup
    throw UnimplementedError();
  }

  @override
  Future<List<GroupReturn>> fetchGroups() async {
    await Future.delayed(loadTime);
    List<GroupReturn> ret = [];
    for(ShareCartGroup group in groups){
      ret.add((group: group, members: groupMemberships[group.id]!, invites: groupInvites[group.id]!));
    }
    return ret;
  }

  @override
  Future<ShareCartItem?> fetchItem(int groupId, int itemId) async {
    await Future.delayed(loadTime);
    return items[groupId]![itemId];
  }

  @override
  Future<Map<int, ShareCartItem>> fetchItems(int groupId) async {
    await Future.delayed(loadTime);
    return items[groupId]!;
  }

  @override
  Future<ShareCartList?> fetchList(int groupId, int listId) async {
    await Future.delayed(loadTime);
    return lists[groupId]![listId];
  }

  @override
  Future<Map<int, ShareCartList>> fetchLists(int groupId) async {
    await Future.delayed(loadTime);
    return lists[groupId]!;
  }

  @override
  Future<ShareCartStore?> fetchStore(int storeId) {
    // TODO: implement fetchStore
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartStore>> fetchStores() {
    // TODO: implement fetchStores
    throw UnimplementedError();
  }

  @override
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String p1) onFailure) async {
    onSuccess.call();
  }

  @override
  Future<void> logOut(VoidCallback onLogOut) async {
    onLogOut.call();
  }
  
  @override
  Future<List<MyInvite>> fetchInvites() async {
    await Future.delayed(loadTime);
    return myInvites;
  }
  
  @override
  Future<ShareCartGroup?> createGroup(String name) {
    // TODO: implement createGroup
    throw UnimplementedError();
  }

  @override
  Future<ShareCartGroup?> acceptInvite(int groupId) {
    // TODO: implement acceptInvite
    throw UnimplementedError();
  }
  
  @override
  Future<void> declineInvite(int groupId) {
    // TODO: implement declineInvite
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartListItem> changeItemQuantity(int groupId, int listId, int itemId, int quantity) async {
    await Future.delayed(loadTime);
    var groupLists = lists[groupId] ?? {};
    ShareCartList list = groupLists[listId] as ShareCartList;
    int itemIndex = list.items.indexWhere((listItem) => listItem.itemId == itemId);
    ShareCartListItem item = list.items[itemIndex];
    return ShareCartListItem(item.itemId, item.listId, item.userId, item.communal, item.bought, quantity, item.createdAt);
  }
  
  @override
  Future<ShareCartList?> addItemToList(int groupId, int listId, int itemId, int quantity, {bool communal = false}) {
    // TODO: implement addItemToList
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartItem?> createItem(int groupId, String name, {String description = "", String category = "", double price = 0.0}) {
    // TODO: implement createItem
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartList?> createList(int groupId, String name) {
    // TODO: implement createList
    throw UnimplementedError();
  }

}

//final ApiService apiService = MockApiService();
final ApiService apiService = RealApiService(baseUrl: "http://localhost:8080");

class RealApiService implements ApiService {
  final String baseUrl;
  final http.Client client;
  ThisUserDetails? _userDetails;

  ThisUserDetails? get userDetails => _userDetails;

  RealApiService({required this.baseUrl, http.Client? client})
    : client = client ?? http.Client(); //Client dependency injection for testing purposes, defaults to regular http client

  final FlutterSecureStorage _storage = const FlutterSecureStorage(); //TODO make sure this works with target platforms and everyones machines
  var baseHeaders = { 
    "Content-Type":
      "application/json"
  };
  
  Future<String> getJWT() async {
    String? jwt = await _storage.read(key: 'AuthToken');
    if(jwt == null) throw UnimplementedError(); //TODO figure out what to throw here
    return jwt;
  }

  @override
  Future<void> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var response = await client.post(Uri.parse('$baseUrl/api/auth/signin'),
      headers: baseHeaders,
      body: jsonEncode({"username": username, "password": password})
    );

    if(response.statusCode == 200){
      onSuccess.call();
      var responseJson = jsonDecode(response.body) as Map<String, dynamic>;
      await _storage.write(key: 'AuthToken', value: responseJson['token']);
      print(await _storage.read(key: 'AuthToken'));
      _userDetails = ThisUserDetails(username, responseJson['email'], responseJson['userId'], HttpDate.parse(responseJson['createdAtFormatted']));
      return;
    } else {
      onFailure.call();
      _userDetails = null;
      return;
    }
  }

  @override
  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var response = await client.post(
      Uri.parse('$baseUrl/api/auth/signup'),
      headers: baseHeaders,
      body: jsonEncode({
        "username": username,
        "email": email,
        "password": password
      })
    );
    if (response.statusCode == 200) {
      onSuccess.call();
      return;
    } else {
      onFailure.call();
      return;
    }
  }

  @override
  Future<ShareCartItem?> fetchItem(int groupId, int itemId) {
    // TODO: implement fetchItem
    throw UnimplementedError();
  }

  @override
  Future<Map<int, ShareCartItem>> fetchItems(int groupId) async {
    var headers = await authorizedHeaders();

    var itemsResponse = await client.get(Uri.parse('$baseUrl/api/group/$groupId/item/getall'), headers: headers);

    if(itemsResponse.statusCode == 200){
      Iterable jsonResponse = jsonDecode(itemsResponse.body);
      return <int, ShareCartItem>{for (var item in jsonResponse) item["listId"]: ShareCartItem.fromJson(item)};
    }
    // TODO: implement fetchItems
    throw UnimplementedError();
  }

  @override
  Future<ShareCartStore?> fetchStore(int storeId) {
    // TODO: implement fetchStore
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartStore>> fetchStores() {
    // TODO: implement fetchStores
    throw UnimplementedError();
  }

  @override
  Future<Map<int, ShareCartList>> fetchLists(int groupId) async {
    var headers = await authorizedHeaders();

    var response = await client.get(Uri.parse('$baseUrl/api/group/$groupId/list/getall'), headers: headers);

    if(response.statusCode == 200){
      Iterable responseBody = jsonDecode(response.body);
      return <int, ShareCartList>{for (var list in responseBody) list["listId"]: ShareCartList.fromJson(list)};
    }

    // TODO: implement fetchLists fail state
    throw UnimplementedError();
  }

  @override
  Future<ShareCartList?> fetchList(int groupId, int listId) async {
    var headers = await authorizedHeaders();

    var response = await client.get(Uri.parse('$baseUrl/api/group/$groupId/list/$listId/get'), headers: headers);

    if(response.statusCode == 200){
      var jsonResponse = jsonDecode(response.body);
      print(jsonResponse);
      return ShareCartList.fromJson(jsonResponse);
    }
    // TODO: implement fetchList fail state
    throw UnimplementedError();
  }

  @override
  Future<List<GroupReturn>> fetchGroups() async {
    var headers = await authorizedHeaders();
    var response = await client.get(Uri.parse('$baseUrl/api/group/get/all'), headers: headers);

    if(response.statusCode == 200){
      Iterable jsonResponse = jsonDecode(response.body);
      return List<GroupReturn>.from(jsonResponse.map((group) => (group: ShareCartGroup.fromJson(group, _roleFromGroupResponse(group)!), //This is ugly and idk if it will work
                                                    members: List<GroupMember>.from((group["members"] as Iterable).map((member) => GroupMember.fromJson(member))),
                                                    invites: List<GroupInvite>.from((group["invites"] as Iterable).map((invite) => GroupInvite.fromJson(invite))))));
    }
    throw UnimplementedError();
  }

  @override
  Future<ShareCartGroup?> fetchGroup(int groupId) async {
    // TODO: implement fetchGroup
    throw UnimplementedError();
  }

  @override
  Future<void> logOut(VoidCallback onLogOut) async{
    await _storage.deleteAll(); //Clear all stored data on log out
    onLogOut.call();
    return;
  }

  @override
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String) onFailure) async {
   var headers = await authorizedHeaders();

    var response = await client.post(
      Uri.parse('$baseUrl/api/group/$groupId/leave'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      onSuccess();
    } else {
      var errorMessage = "Failed to leave group";
      try {
        var responseBody = jsonDecode(response.body) as Map<String, dynamic>;
        errorMessage = responseBody['message'] ?? errorMessage;
      } catch (e) {
        // Use default error message if response parsing fails
      }
      onFailure(errorMessage);
    }
  }
  
  @override
  Future<List<MyInvite>> fetchInvites() async {
    var headers = await authorizedHeaders();
    if(_userDetails == null) throw UnimplementedError();
    int userId = _userDetails!.userId;
    var response = await client.get(Uri.parse('$baseUrl/users/$userId/invites/get'), headers: headers);

    if(response.statusCode == 200){
      return List.from((jsonDecode(response.body) as Iterable).map((invite) => MyInvite.fromJson(invite as Map<String, dynamic>)));
    }
    //TODO fail state
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartGroup?> createGroup(String name) async {
    var headers = await authorizedHeaders();
    var response = await client.post(Uri.parse('$baseUrl/api/group/create'), body: jsonEncode({"name" : name}), headers: headers);

    if(response.statusCode == 201){
      return ShareCartGroup.fromJson(jsonDecode(response.body) as Map<String, dynamic>, GroupRole.ADMIN);
    }
    //TODO fail state (409 conflict or other)
    throw UnimplementedError();
  }

  @override
  Future<ShareCartListItem> changeItemQuantity(int groupId, int listId, int itemId, int quantity) async {
    var headers = await authorizedHeaders();
    var response = await client.put(Uri.parse('$baseUrl/api/group/$groupId/item/$listId/$itemId/quantity'), 
      headers: headers,
      body: jsonEncode({"quantity" : quantity}));
    
    if(response.statusCode == 200) {
      return ShareCartListItem.fromJson(jsonDecode(response.body));
    }

    throw UnimplementedError();
  }

  @override
  Future<ShareCartGroup?> acceptInvite(int groupId) async {
    var headers = await authorizedHeaders();
    var response = await client.post(Uri.parse('$baseUrl/api/group/$groupId/invite/accept'), headers: headers);

    if(response.statusCode == 200){
      Map<String, dynamic> jsonResponse = jsonDecode(response.body);
      return ShareCartGroup.fromJson(jsonResponse, _roleFromGroupResponse(jsonResponse) as GroupRole);
    }
    // TODO: implement acceptInvite fail stat
    throw UnimplementedError();
  }
  
  @override
  Future<void> declineInvite(int groupId) async {
    var headers = await authorizedHeaders();
    var response = await client.post(Uri.parse('$baseUrl/api/$groupId/invite/decline'), headers: headers);

    if(response.statusCode == 200){
      return;
    }
    //TODO implement fail state
    throw UnimplementedError();
  }

  @override
  Future<ShareCartList?> addItemToList(int groupId, int listId, int itemId, int quantity, {bool communal = false}) {
    // TODO: implement addItemToList
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartItem?> createItem(int groupId, String name, {String description = "", String category = "", double price = 0.0}) {
    // TODO: implement createItem
    throw UnimplementedError();
  }
  
  @override
  Future<ShareCartList?> createList(int groupId, String name) {
    // TODO: implement createList
    throw UnimplementedError();
  }

    Future<Map<String, String>> authorizedHeaders() async {
    var headers = baseHeaders;
    String jwt = await getJWT();
    headers["Authorization"] = "Bearer $jwt";
    return headers;
  }

  GroupRole? _roleFromGroupResponse(Map<String, dynamic> input){
    Iterable members = input["members"];
    return (members.firstWhere((member) => member["userId"] == userDetails!.userId))["role"].toString().groupRole;
  }
}

