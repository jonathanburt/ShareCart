import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:http/http.dart' as http;

abstract class ApiService {
  Future<List<ShareCartItem>> fetchItems(int groupId);
  Future<ShareCartItem?> fetchItem(int itemId);
  Future<List<ShareCartStore>> fetchStores();
  Future<ShareCartStore?> fetchStore(int storeId);
  Future<List<ShareCartList>> fetchLists(int groupId);
  Future<ShareCartList?> fetchList(int listId);
  Future<List<ShareCartListItem>?> fetchListItems(int listId);
  Future<List<GroupReturn>> fetchGroups();
  Future<ShareCartGroup?> fetchGroup(int groupId);

  Future<ThisUserDetails?> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<ThisUserDetails?> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<void> logOut(VoidCallback onLogOut);
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String) onFailure);
}

class MockApiService implements ApiService {
  final List<ShareCartStore> stores = [
    ShareCartStore("Whole Foods Market", Location("95 E Houston St, New York, NY 10002", 40.7223, -73.9928), 4505),
    ShareCartStore("Whole Foods Market", Location("4 Union Square S, New York, NY 10003", 40.7346, -73.9910), 1328),
    ShareCartStore("Trader Joe's", Location("142 E 14th St, New York, NY 10003", 40.7330, -73.9876), 1339),
    ShareCartStore("Trader Joe's", Location("675 6th Ave, New York, NY 10010", 40.7401, -73.9954), 2240),
    ShareCartStore("Fairway Market", Location("550 2nd Ave, New York, NY 10016", 40.7427, -73.9787), 9870),
    ShareCartStore("Fairway Market", Location("2131 Broadway, New York, NY 10023", 40.7808, -73.9825), 12),
    ShareCartStore("H Mart", Location("39 3rd Ave, New York, NY 10003", 40.7306, -73.9894), 1290),
    ShareCartStore("H Mart", Location("124-126 Ludlow St, New York, NY 10002", 40.7186, -73.9871), 64345),
    ShareCartStore("Key Food", Location("52 Avenue A, New York, NY 10009", 40.7260, -73.9814), 8901),
    ShareCartStore("Key Food", Location("367 Grand St, New York, NY 10002",  40.7161, -73.986), 26798),
  ];

  final List<ShareCartItem> items = [
    ShareCartItem("Organic Bananas", ["fruit", "banana", "organic"], 0.79, 98642, 4505),
    ShareCartItem("Almond Milk", ["milk", "dairy-free", "almond"], 3.49, 2344, 4505),
    ShareCartItem("Ground Beef", ["meat", "beef", "protein"], 5.99, 87666, 1328),
    ShareCartItem("Fresh Salmon", ["fish", "seafood", "omega-3"], 10.99, 34532, 1339),
    ShareCartItem("Organic Brown Rice", ["grain", "rice", "organic"], 2.99, 900023, 2240),
    ShareCartItem("Quinoa", ["grain", "quinoa", "protein"], 4.99, 343445, 9870),
    ShareCartItem("Greek Yogurt", ["dairy", "yogurt", "protein"], 1.49, 993345, 12),
    ShareCartItem("Organic Eggs", ["eggs", "protein", "organic"], 4.79, 67333, 1290),
    ShareCartItem("Chicken Breast", ["meat", "chicken", "protein"], 6.49, 788224, 64345),
    ShareCartItem("Organic Spinach", ["vegetable", "greens", "organic"], 3.29, 12313454, 8901),
    ShareCartItem("Avocado", ["fruit", "avocado", "healthy fats"], 1.99, 874567, 26798),
    ShareCartItem("Strawberries", ["fruit", "berries", "strawberry"], 3.99, 9019023, 4505),
    ShareCartItem("Blueberries", ["fruit", "berries", "blueberry"], 4.29, 1234, 1328),
    ShareCartItem("Organic Carrots", ["vegetable", "carrots", "organic"], 2.49, 4321, 1339),
    ShareCartItem("Broccoli", ["vegetable", "broccoli", "fiber"], 2.99, 54637532, 2240),
    ShareCartItem("Whole Wheat Bread", ["bread", "whole grain", "fiber"], 3.49, 54639787892, 9870),
    ShareCartItem("Oatmeal", ["breakfast", "oats", "fiber"], 3.79, 549117892, 12),
    ShareCartItem("Peanut Butter", ["spread", "nuts", "protein"], 4.99, 54, 1290),
    ShareCartItem("Honey", ["sweetener", "honey", "natural"], 5.49, 90000975, 64345),
    ShareCartItem("Almond Butter", ["spread", "almond", "healthy fats"], 6.99, 80085, 8901),
    ShareCartItem("Cashews", ["nuts", "cashew", "snack"], 7.49, 4311, 26798),
    ShareCartItem("Walnuts", ["nuts", "walnut", "brain food"], 6.79, 299751, 4505),
    ShareCartItem("Chia Seeds", ["superfood", "seeds", "omega-3"], 5.99, 454312, 1328),
    ShareCartItem("Flax Seeds", ["superfood", "seeds", "fiber"], 4.29, 4567899, 1339),
    ShareCartItem("Dark Chocolate", ["snack", "chocolate", "antioxidants"], 3.99, 790123, 2240),
    ShareCartItem("Pasta", ["carbs", "pasta", "Italian"], 2.99, 90897123, 9870),
    ShareCartItem("Tomato Sauce", ["sauce", "tomato", "pasta"], 3.49, 97123, 12),
    ShareCartItem("Coconut Oil", ["oil", "coconut", "cooking"], 6.49, 34012983, 1290),
    ShareCartItem("Olive Oil", ["oil", "olive", "cooking"], 7.99, 123987, 64345),
    ShareCartItem("Cinnamon", ["spice", "cinnamon", "baking"], 2.49, 677912, 8901),
  ];

  final List<ShareCartList> lists = [
    ShareCartList("Weekly Groceries", 8979123, 6578981, DateTime.now()),
    ShareCartList("Halloween Supplies", 9090765, 6578981, DateTime.now()),
    ShareCartList("Game Night Food",76123567, 6578981, DateTime.now()),
  ];

  final List<ShareCartListItem> listItems = [
    ShareCartListItem(993345, 8979123, 0, false, 1, DateTime.now()),
    ShareCartListItem(12313454, 8979123, 0, true, 5, DateTime.now()),
    ShareCartListItem(54637532, 8979123, 0, false, 2, DateTime.now()),
    ShareCartListItem(34012983, 9090765, 0, true, 1, DateTime.now()),
    ShareCartListItem(123987, 9090765, 0, false, 2, DateTime.now()),
    ShareCartListItem(677912, 9090765, 0, false, 1, DateTime.now()),
    ShareCartListItem(1234,76123567, 0, true, 3, DateTime.now()),
    ShareCartListItem(549117892,76123567, 0, false, 2, DateTime.now()),
    ShareCartListItem(4311,76123567, 0, false, 4, DateTime.now()),
  ];

  final List<ShareCartGroup> groups = [
    ShareCartGroup("Family Group", 76120909067),
  ];

  bool itemsCached = false;
  bool storesCached = false;
  bool listsCached = false;
  bool listItemsCached = false;
  bool groupsCached = false;
  Duration fetchDelay = Duration(milliseconds: 500);

  @override
  Future<List<ShareCartItem>> fetchItems(int groupId) async {
    if (!itemsCached) {
      await Future.delayed(fetchDelay);
      itemsCached = true;
    }
    return items;
  }

  @override
  Future<ShareCartItem?> fetchItem(int itemId) async {
    await fetchItems();
    return items.firstWhere((item) => item.id == itemId);
  }

  @override
  Future<List<ShareCartStore>> fetchStores() async {
    if (!storesCached) {
      await Future.delayed(fetchDelay);
      storesCached = true;
    }
    return stores;
  }

  @override
  Future<ShareCartStore?> fetchStore(int storeId) async {
    await fetchStores();
    return stores.firstWhere((store) => store.id == storeId);
  }

  @override
  Future<List<ShareCartList>> fetchLists(int groupId) async {
    if (!listsCached) {
      await Future.delayed(fetchDelay);
      listsCached = true;
    }
    return lists;
  }

  @override
  Future<ShareCartList?> fetchList(int listId) async {
    await fetchLists(1);
    return lists.firstWhere((list) => list.id == listId);
  }

  @override
  Future<List<ShareCartListItem>?> fetchListItems(int listId) async {
    if (!listItemsCached) {
      await Future.delayed(fetchDelay);
      listItemsCached = true;
    }
    return listItems.where((listItem) => listItem.listId == listId).toList();
  }

  @override
  Future<List<ShareCartGroup>> fetchGroups() async {
    if (!groupsCached) {
      await Future.delayed(fetchDelay);
      groupsCached = true;
    }
    return groups;
  }

  @override
  Future<ShareCartGroup?> fetchGroup(int groupId) async {
    await fetchLists(1);
    return groups.firstWhere((group) => group.id == groupId);
  }

  @override
  Future<List<ShallowGroupDetails>> fetchAllGroups() {
    // TODO: implement fetchAllGroups
    throw UnimplementedError();
  }

  @override
  Future<DeepGroupDetails?> fetchGroupDeep(ShallowGroupDetails group) {
    // TODO: implement fetchGroupDeep
    throw UnimplementedError();
  }

  @override
  Future<ThisUserDetails?> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess();
    return null;
  }
  
  @override
  Future<ThisUserDetails?> authenticateUser(String usernameOrEmail, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess();
    return null;
  }

  @override
  Future<void> logOut(VoidCallback onLogOut) async {
    onLogOut();
  }

  @override
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String) onFailure) async {
    await Future.delayed(fetchDelay);
    onSuccess();
  }
}

final ApiService apiService = MockApiService();

class RealApiService implements ApiService {
  final String baseUrl;
  final http.Client client;

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
  Future<ThisUserDetails?> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var response = await client.post(Uri.parse('$baseUrl/api/auth/signin'),
      headers: baseHeaders,
      body: jsonEncode({"username": username, "password": password})
    );

    if(response.statusCode == 200){
      onSuccess.call();
      var responseJson = jsonDecode(response.body) as Map<String, dynamic>;
      await _storage.write(key: 'AuthToken', value: responseJson['token']);
      print(await _storage.read(key: 'AuthToken'));
      return ThisUserDetails(username, responseJson['email'], responseJson['userId'], HttpDate.parse(responseJson['createdAtFormatted']));
    } else {
      onFailure.call();
      return null;
    }
  }

  @override
  Future<ThisUserDetails?> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
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
      var responseBody = jsonDecode(response.body) as Map<String, dynamic>;
      return ThisUserDetails(username, email, responseBody['userId'], HttpDate.parse(responseBody['createdAt']));
    } else {
      onFailure.call();
      return null; //TODO replace this with a throw, then the calling method can catch
    }
  }

  @override
  Future<List<ShallowGroupDetails>> fetchAllGroups() async { //TODO create a test to ensure this works
    String jwt = await getJWT();
    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";
    var response = await client.get(Uri.parse('$baseUrl/api/group/get/all'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      Iterable responseBody = jsonDecode(response.body);
      return List.from(responseBody.map((model) => ShallowGroupDetails.fromJson(model)));
    }

    print("call failed ${response.statusCode}");
    // TODO: implement fail-state
    throw UnimplementedError();
  }

  @override
  Future<DeepGroupDetails?> fetchGroupDeep(int groupId) async {
    String jwt = await getJWT();
    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";
    var response = await client.get(Uri.parse('$baseUrl/api/group/get/$groupId'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      List<ShareCartList> lists = await fetchLists(groupId);
      List<ShareCartItem> items = await fetchItems(groupId);
      ShallowGroupDetails shallowDetails = ShallowGroupDetails.fromJson(jsonDecode(response.body));
      return DeepGroupDetails(shallowDetails, lists, items);
    }
    
    // TODO: implement fetchGroupDeep
    
    throw UnimplementedError();
  }

  @override
  Future<ShareCartItem?> fetchItem(int itemId) {
    // TODO: implement fetchItem
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartItem>> fetchItems(int groupId) async {
    String jwt = (await getJWT())!;
    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";

    var itemsResponse = await client.get(Uri.parse('$baseUrl/api/group/$groupId/item/getall'), headers: headers);

    if(itemsResponse.statusCode == 200){
      Iterable jsonResponse = jsonDecode(itemsResponse.body);
      return List.from(jsonResponse.map((item) => ShareCartItem.fromJson(item)));
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
  Future<List<ShareCartList>> fetchLists(int groupId) async {
    String jwt = (await getJWT())!;
    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";

    var response = await client.get(Uri.parse('$baseUrl/api/group/$groupId/list/getall'), headers: headers);

    if(response.statusCode == 200){
      Iterable responseBody = jsonDecode(response.body);
      return List.from(responseBody.map((list) => ShareCartList.fromJson(list)));
    }

    // TODO: implement fetchLists fail state
    throw UnimplementedError();
  }

  @override
  Future<ShareCartList?> fetchList(int listId) async {
    // TODO: implement fetchList
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartListItem>?> fetchListItems(int listId) async {
    // TODO: implement fetchListItems
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartGroup>> fetchGroups() async {
    // TODO: implement fetchGroups
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
    String? jwt = await getJWT();
    if (jwt == null) {
      onFailure("Not authenticated");
      return;
    }

    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";

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
  Future<List<GroupDetails>> getUserGroups() async { //TODO depracated
    String? jwt = await getJWT();
    if (jwt == null) {
      return [];
    }

    var headers = baseHeaders;
    headers["Authorization"] = "Bearer $jwt";

    var response = await client.get(
      Uri.parse('$baseUrl/api/group/user/groups'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      var responseBody = jsonDecode(response.body) as List;
      return responseBody.map((group) => 
        GroupDetails(
          group['id'].toString(),
          group['name'],
        )
      ).toList();
    }
    return [];
  }
}

class GroupDetails {
  final String id;
  final String name;

  GroupDetails(this.id, this.name);
}

