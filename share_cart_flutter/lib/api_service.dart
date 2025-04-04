import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:http/http.dart' as http;


abstract class ApiService {
  Future<List<ShareCartItem>> fetchItems();
  Future<ShareCartItem?> fetchItem(String itemId);
  Future<List<ShareCartStore>> fetchStores();
  Future<ShareCartStore?> fetchStore(String storeId);
  Future<List<ShareCartList>> fetchLists();
  Future<ShareCartList?> fetchList(String listId);
  Future<List<ShareCartListItem>?> fetchListItems(String listId);
  Future<List<ShareCartGroup>> fetchGroups();
  Future<ShareCartGroup?> fetchGroup(String groupId);

  Future<List<ShallowGroupDetails>> fetchAllGroups();
  Future<DeepGroupDetails?> fetchGroupDeep(int groupId);

  Future<ThisUserDetails?> authenticateUser(String usernameOrEmail, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<ThisUserDetails?> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<void> logOut(VoidCallback onLogOut);
  Future<void> leaveGroup(String groupId, VoidCallback onSuccess, Function(String) onFailure);
  Future<List<GroupDetails>> getUserGroups();
}

class MockApiService implements ApiService {
  final List<ShareCartStore> stores = [
    ShareCartStore("Whole Foods Market", Location("95 E Houston St, New York, NY 10002", 40.7223, -73.9928), "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    ShareCartStore("Whole Foods Market", Location("4 Union Square S, New York, NY 10003", 40.7346, -73.9910), "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    ShareCartStore("Trader Joe's", Location("142 E 14th St, New York, NY 10003", 40.7330, -73.9876), "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    ShareCartStore("Trader Joe's", Location("675 6th Ave, New York, NY 10010", 40.7401, -73.9954), "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    ShareCartStore("Fairway Market", Location("550 2nd Ave, New York, NY 10016", 40.7427, -73.9787), "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    ShareCartStore("Fairway Market", Location("2131 Broadway, New York, NY 10023", 40.7808, -73.9825), "57c4e69b-830f-4f45-9b73-52af775eb349"),
    ShareCartStore("H Mart", Location("39 3rd Ave, New York, NY 10003", 40.7306, -73.9894), "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    ShareCartStore("H Mart", Location("124-126 Ludlow St, New York, NY 10002", 40.7186, -73.9871), "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    ShareCartStore("Key Food", Location("52 Avenue A, New York, NY 10009", 40.7260, -73.9814), "4b944e4d-4212-4f24-9454-e367610cec62"),
    ShareCartStore("Key Food", Location("367 Grand St, New York, NY 10002",  40.7161, -73.986), "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
  ];

  final List<ShareCartItem> items = [
    ShareCartItem("Organic Bananas", ["fruit", "banana", "organic"], 0.79, "e9014b9c-0056-481b-9ced-2ce0e94838cd", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    ShareCartItem("Almond Milk", ["milk", "dairy-free", "almond"], 3.49, "911f5002-da0c-4304-84fa-947b0c9ad6b6", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    ShareCartItem("Ground Beef", ["meat", "beef", "protein"], 5.99, "f1fcfdea-43d5-4164-a681-1960fef11c34", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    ShareCartItem("Fresh Salmon", ["fish", "seafood", "omega-3"], 10.99, "0131934a-8734-42a2-a7e5-76413fcd5c2b", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    ShareCartItem("Organic Brown Rice", ["grain", "rice", "organic"], 2.99, "e46ee65f-ef12-4ee7-9960-28336951c736", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    ShareCartItem("Quinoa", ["grain", "quinoa", "protein"], 4.99, "57ec2320-935b-4b15-abbc-96b83d9993a4", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    ShareCartItem("Greek Yogurt", ["dairy", "yogurt", "protein"], 1.49, "b1b1343b-9b4c-4cc7-a29b-dbc782a667c8", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    ShareCartItem("Organic Eggs", ["eggs", "protein", "organic"], 4.79, "e8703f77-22b4-44e3-afae-96b84a360dbe", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    ShareCartItem("Chicken Breast", ["meat", "chicken", "protein"], 6.49, "603248a0-2c65-4cc0-94e7-b09e55d7d040", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    ShareCartItem("Organic Spinach", ["vegetable", "greens", "organic"], 3.29, "ab8f4a7c-2bff-47a7-8d8e-5504f6d5d413", "4b944e4d-4212-4f24-9454-e367610cec62"),
    ShareCartItem("Avocado", ["fruit", "avocado", "healthy fats"], 1.99, "f4387271-b86d-475e-9f73-d463401a7e9f", "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
    ShareCartItem("Strawberries", ["fruit", "berries", "strawberry"], 3.99, "5b08211f-4fda-470d-a6b7-48e9386da64b", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    ShareCartItem("Blueberries", ["fruit", "berries", "blueberry"], 4.29, "b8b9a326-6eda-4c80-b9ba-350a6e8438ec", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    ShareCartItem("Organic Carrots", ["vegetable", "carrots", "organic"], 2.49, "3f648aa6-206a-4e01-8ff5-a974f7fcaea9", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    ShareCartItem("Broccoli", ["vegetable", "broccoli", "fiber"], 2.99, "f63ef020-ff45-4ef3-9c40-577a93c7ec7b", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    ShareCartItem("Whole Wheat Bread", ["bread", "whole grain", "fiber"], 3.49, "94f39496-0ee2-4be6-824c-cf12b722a0a1", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    ShareCartItem("Oatmeal", ["breakfast", "oats", "fiber"], 3.79, "894eccb6-1dd2-49c5-91e8-f031b2373351", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    ShareCartItem("Peanut Butter", ["spread", "nuts", "protein"], 4.99, "448bfb39-ca64-4f8a-8ac3-79a922fb03ae", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    ShareCartItem("Honey", ["sweetener", "honey", "natural"], 5.49, "4221896f-ac49-4a43-85e6-1627a365ac37", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    ShareCartItem("Almond Butter", ["spread", "almond", "healthy fats"], 6.99, "b1fabb42-bba0-438e-91c4-a66cfe63fec0", "4b944e4d-4212-4f24-9454-e367610cec62"),
    ShareCartItem("Cashews", ["nuts", "cashew", "snack"], 7.49, "e97393d9-7175-418b-bdad-a9c4df0b761c", "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
    ShareCartItem("Walnuts", ["nuts", "walnut", "brain food"], 6.79, "0eada563-00fa-4389-99e0-787e9ed023cb", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    ShareCartItem("Chia Seeds", ["superfood", "seeds", "omega-3"], 5.99, "4321c546-ec71-432c-8108-d9de4eaaf995", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    ShareCartItem("Flax Seeds", ["superfood", "seeds", "fiber"], 4.29, "b534bdb0-c441-4646-9f8b-8347bf91edad", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    ShareCartItem("Dark Chocolate", ["snack", "chocolate", "antioxidants"], 3.99, "72a31639-2893-45d8-9bb3-36c3440c3d08", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    ShareCartItem("Pasta", ["carbs", "pasta", "Italian"], 2.99, "8dfc474a-b2c8-4e9a-b115-4b059059eabf", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    ShareCartItem("Tomato Sauce", ["sauce", "tomato", "pasta"], 3.49, "88e762e5-95d7-400c-b2ea-67d389147d08", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    ShareCartItem("Coconut Oil", ["oil", "coconut", "cooking"], 6.49, "6412fd55-ddd9-41e2-9abb-ba7d81a25bcb", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    ShareCartItem("Olive Oil", ["oil", "olive", "cooking"], 7.99, "220ec65a-cb0d-4558-b3af-0dc2c4d75ec4", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    ShareCartItem("Cinnamon", ["spice", "cinnamon", "baking"], 2.49, "b7b9601e-ab6c-4bcc-93ac-949af9134da4", "4b944e4d-4212-4f24-9454-e367610cec62"),
  ];

  final List<ShareCartList> lists = [
    ShareCartList("Weekly Groceries", "5610f089-c34e-4206-ba8e-b6031f28bc9d", "6aeff571-b270-4878-b8f0-1a48f1018bd5"),
    ShareCartList("Halloween Supplies", "3e234a6e-2489-4917-9c9d-f3bc9d1d5a77", "6aeff571-b270-4878-b8f0-1a48f1018bd5"),
    ShareCartList("Game Night Food", "6750c0db-8c25-4d98-9eff-9c6db9a6117b", "6aeff571-b270-4878-b8f0-1a48f1018bd5"),
  ];

  final List<ShareCartListItem> listItems = [
    ShareCartListItem("b1b1343b-9b4c-4cc7-a29b-dbc782a667c8", "5610f089-c34e-4206-ba8e-b6031f28bc9d", "none", false, 1),
    ShareCartListItem("ab8f4a7c-2bff-47a7-8d8e-5504f6d5d413", "5610f089-c34e-4206-ba8e-b6031f28bc9d", "none", true, 5),
    ShareCartListItem("f63ef020-ff45-4ef3-9c40-577a93c7ec7b", "5610f089-c34e-4206-ba8e-b6031f28bc9d", "none", false, 2),
    ShareCartListItem("6412fd55-ddd9-41e2-9abb-ba7d81a25bcb", "3e234a6e-2489-4917-9c9d-f3bc9d1d5a77", "none", true, 1),
    ShareCartListItem("220ec65a-cb0d-4558-b3af-0dc2c4d75ec4", "3e234a6e-2489-4917-9c9d-f3bc9d1d5a77", "none", false, 2),
    ShareCartListItem("b7b9601e-ab6c-4bcc-93ac-949af9134da4", "3e234a6e-2489-4917-9c9d-f3bc9d1d5a77", "none", false, 1),
    ShareCartListItem("b8b9a326-6eda-4c80-b9ba-350a6e8438ec", "6750c0db-8c25-4d98-9eff-9c6db9a6117b", "none", true, 3),
    ShareCartListItem("894eccb6-1dd2-49c5-91e8-f031b2373351", "6750c0db-8c25-4d98-9eff-9c6db9a6117b", "none", false, 2),
    ShareCartListItem("e97393d9-7175-418b-bdad-a9c4df0b761c", "6750c0db-8c25-4d98-9eff-9c6db9a6117b", "none", false, 4),
  ];

  final List<ShareCartGroup> groups = [
    ShareCartGroup("Family Group", "459d5970-16c6-4a6a-9feb-c5619c76cc47"),
  ];

  bool itemsCached = false;
  bool storesCached = false;
  bool listsCached = false;
  bool listItemsCached = false;
  bool groupsCached = false;
  Duration fetchDelay = Duration(milliseconds: 500);

  @override
  Future<List<ShareCartItem>> fetchItems() async {
    if (!itemsCached) {
      await Future.delayed(fetchDelay);
      itemsCached = true;
    }
    return items;
  }

  @override
  Future<ShareCartItem?> fetchItem(String itemId) async {
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
  Future<ShareCartStore?> fetchStore(String storeId) async {
    await fetchStores();
    return stores.firstWhere((store) => store.id == storeId);
  }

  @override
  Future<List<ShareCartList>> fetchLists() async {
    if (!listsCached) {
      await Future.delayed(fetchDelay);
      listsCached = true;
    }
    return lists;
  }

  @override
  Future<ShareCartList?> fetchList(String listId) async {
    await fetchLists();
    return lists.firstWhere((list) => list.id == listId);
  }

  @override
  Future<List<ShareCartListItem>?> fetchListItems(String listId) async {
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
  Future<ShareCartGroup?> fetchGroup(String groupId) async {
    await fetchLists();
    return groups.firstWhere((group) => group.id == groupId);
  }

  @override
  Future<List<ShallowGroupDetails>> fetchAllGroups() {
    // TODO: implement fetchAllGroups
    throw UnimplementedError();
  }

  @override
  Future<DeepGroupDetails?> fetchGroupDeep(int groupId) {
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

  @override
  Future<List<GroupDetails>> getUserGroups() async {
    await Future.delayed(fetchDelay);
    return [
      GroupDetails("1", "Family Group"),
      GroupDetails("2", "Friends Group"),
    ];
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
  
  Future<String?> getJWT(){
    return _storage.read(key: 'AuthToken');
  }

  @override
  Future<ThisUserDetails?> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var response = await client.post(Uri.parse('$baseUrl/api/auth/signin'),
      headers: baseHeaders,
      body: jsonEncode({"username": username, "password": password})
    );

    print(response.statusCode);
    print(response.body);

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
      return null;
    }
  }

  @override
  Future<List<ShallowGroupDetails>> fetchAllGroups() async { //TODO create a test to ensure this works
    //String jwt = (await getJWT())!;
    var headers = baseHeaders;
    //headers["Authorization"] = "Bearer $jwt";
    var response = await client.get(Uri.parse('$baseUrl/api/group/get/all'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      Iterable responseBody = jsonDecode(response.body);
      return List.from(responseBody.map((model) => ShallowGroupDetails.fromJson(model)));
    }

    print("call failed " + response.statusCode.toString());
    // TODO: implement fail-state
    throw UnimplementedError();
  }

  @override
  Future<DeepGroupDetails?> fetchGroupDeep(int groupId) {
    // TODO: implement fetchGroupDeep
    
    throw UnimplementedError();
  }

  @override
  Future<ShareCartItem?> fetchItem(String itemId) {
    // TODO: implement fetchItem
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartItem>> fetchItems() {
    // TODO: implement fetchItems
    throw UnimplementedError();
  }

  @override
  Future<ShareCartStore?> fetchStore(String storeId) {
    // TODO: implement fetchStore
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartStore>> fetchStores() {
    // TODO: implement fetchStores
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartList>> fetchLists() async {
    // TODO: implement fetchLists
    throw UnimplementedError();
  }

  @override
  Future<ShareCartList?> fetchList(String listId) async {
    // TODO: implement fetchList
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartListItem>?> fetchListItems(String listId) async {
    // TODO: implement fetchListItems
    throw UnimplementedError();
  }

  @override
  Future<List<ShareCartGroup>> fetchGroups() async {
    // TODO: implement fetchGroups
    throw UnimplementedError();
  }

  @override
  Future<ShareCartGroup?> fetchGroup(String groupId) async {
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
  Future<List<GroupDetails>> getUserGroups() async {
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

