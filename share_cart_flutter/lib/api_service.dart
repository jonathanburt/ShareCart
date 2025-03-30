import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';

abstract class ApiService {
  Future<List<Item>> fetchItems();
  Future<Item?> fetchItem(String itemId);
  Future<List<Store>> fetchStores();
  Future<Store?> fetchStore(String storeId);

  Future<void> authenticateUser(String usernameOrEmail, String password, VoidCallback onSuccess, VoidCallback onFailure);
  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure);
}

class MockApiService implements ApiService {
  final List<Store> stores = [
    Store("Whole Foods Market", Location("95 E Houston St, New York, NY 10002", 40.7223, -73.9928), "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    Store("Whole Foods Market", Location("4 Union Square S, New York, NY 10003", 40.7346, -73.9910), "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    Store("Trader Joe's", Location("142 E 14th St, New York, NY 10003", 40.7330, -73.9876), "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    Store("Trader Joe's", Location("675 6th Ave, New York, NY 10010", 40.7401, -73.9954), "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    Store("Fairway Market", Location("550 2nd Ave, New York, NY 10016", 40.7427, -73.9787), "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    Store("Fairway Market", Location("2131 Broadway, New York, NY 10023", 40.7808, -73.9825), "57c4e69b-830f-4f45-9b73-52af775eb349"),
    Store("H Mart", Location("39 3rd Ave, New York, NY 10003", 40.7306, -73.9894), "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    Store("H Mart", Location("124-126 Ludlow St, New York, NY 10002", 40.7186, -73.9871), "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    Store("Key Food", Location("52 Avenue A, New York, NY 10009", 40.7260, -73.9814), "4b944e4d-4212-4f24-9454-e367610cec62"),
    Store("Key Food", Location("367 Grand St, New York, NY 10002",  40.7161, -73.986), "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
  ];

  final List<Item> items = [
    Item("Organic Bananas", ["fruit", "banana", "organic"], 0.79, "e9014b9c-0056-481b-9ced-2ce0e94838cd", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    Item("Almond Milk", ["milk", "dairy-free", "almond"], 3.49, "911f5002-da0c-4304-84fa-947b0c9ad6b6", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    Item("Ground Beef", ["meat", "beef", "protein"], 5.99, "f1fcfdea-43d5-4164-a681-1960fef11c34", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    Item("Fresh Salmon", ["fish", "seafood", "omega-3"], 10.99, "0131934a-8734-42a2-a7e5-76413fcd5c2b", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    Item("Organic Brown Rice", ["grain", "rice", "organic"], 2.99, "e46ee65f-ef12-4ee7-9960-28336951c736", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    Item("Quinoa", ["grain", "quinoa", "protein"], 4.99, "57ec2320-935b-4b15-abbc-96b83d9993a4", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    Item("Greek Yogurt", ["dairy", "yogurt", "protein"], 1.49, "b1b1343b-9b4c-4cc7-a29b-dbc782a667c8", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    Item("Organic Eggs", ["eggs", "protein", "organic"], 4.79, "e8703f77-22b4-44e3-afae-96b84a360dbe", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    Item("Chicken Breast", ["meat", "chicken", "protein"], 6.49, "603248a0-2c65-4cc0-94e7-b09e55d7d040", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    Item("Organic Spinach", ["vegetable", "greens", "organic"], 3.29, "ab8f4a7c-2bff-47a7-8d8e-5504f6d5d413", "4b944e4d-4212-4f24-9454-e367610cec62"),
    Item("Avocado", ["fruit", "avocado", "healthy fats"], 1.99, "f4387271-b86d-475e-9f73-d463401a7e9f", "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
    Item("Strawberries", ["fruit", "berries", "strawberry"], 3.99, "5b08211f-4fda-470d-a6b7-48e9386da64b", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    Item("Blueberries", ["fruit", "berries", "blueberry"], 4.29, "b8b9a326-6eda-4c80-b9ba-350a6e8438ec", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    Item("Organic Carrots", ["vegetable", "carrots", "organic"], 2.49, "3f648aa6-206a-4e01-8ff5-a974f7fcaea9", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    Item("Broccoli", ["vegetable", "broccoli", "fiber"], 2.99, "f63ef020-ff45-4ef3-9c40-577a93c7ec7b", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    Item("Whole Wheat Bread", ["bread", "whole grain", "fiber"], 3.49, "94f39496-0ee2-4be6-824c-cf12b722a0a1", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    Item("Oatmeal", ["breakfast", "oats", "fiber"], 3.79, "894eccb6-1dd2-49c5-91e8-f031b2373351", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    Item("Peanut Butter", ["spread", "nuts", "protein"], 4.99, "448bfb39-ca64-4f8a-8ac3-79a922fb03ae", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    Item("Honey", ["sweetener", "honey", "natural"], 5.49, "4221896f-ac49-4a43-85e6-1627a365ac37", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    Item("Almond Butter", ["spread", "almond", "healthy fats"], 6.99, "b1fabb42-bba0-438e-91c4-a66cfe63fec0", "4b944e4d-4212-4f24-9454-e367610cec62"),
    Item("Cashews", ["nuts", "cashew", "snack"], 7.49, "e97393d9-7175-418b-bdad-a9c4df0b761c", "2ab6d80b-a0ec-45cc-ab93-d85dfeed505d"),
    Item("Walnuts", ["nuts", "walnut", "brain food"], 6.79, "0eada563-00fa-4389-99e0-787e9ed023cb", "ae97a990-c4cc-4504-bc3f-74c23b547c9c"),
    Item("Chia Seeds", ["superfood", "seeds", "omega-3"], 5.99, "4321c546-ec71-432c-8108-d9de4eaaf995", "73419308-4534-4ba7-9d7c-675b7cf30c90"),
    Item("Flax Seeds", ["superfood", "seeds", "fiber"], 4.29, "b534bdb0-c441-4646-9f8b-8347bf91edad", "df4b7ab0-a2e5-417a-9a6c-b9afadfad708"),
    Item("Dark Chocolate", ["snack", "chocolate", "antioxidants"], 3.99, "72a31639-2893-45d8-9bb3-36c3440c3d08", "5d172211-889f-4473-b34a-eddc9c7fc1d9"),
    Item("Pasta", ["carbs", "pasta", "Italian"], 2.99, "8dfc474a-b2c8-4e9a-b115-4b059059eabf", "ac96c522-af6f-4e98-898b-c9b9e8fa962c"),
    Item("Tomato Sauce", ["sauce", "tomato", "pasta"], 3.49, "88e762e5-95d7-400c-b2ea-67d389147d08", "57c4e69b-830f-4f45-9b73-52af775eb349"),
    Item("Coconut Oil", ["oil", "coconut", "cooking"], 6.49, "6412fd55-ddd9-41e2-9abb-ba7d81a25bcb", "e29eb2df-2b28-4c8c-9c35-373cc513ed58"),
    Item("Olive Oil", ["oil", "olive", "cooking"], 7.99, "220ec65a-cb0d-4558-b3af-0dc2c4d75ec4", "eff03cb2-0790-4f86-a6fb-5f8fe78b83b0"),
    Item("Cinnamon", ["spice", "cinnamon", "baking"], 2.49, "b7b9601e-ab6c-4bcc-93ac-949af9134da4", "4b944e4d-4212-4f24-9454-e367610cec62"),
  ];

  bool itemsCached = false;
  bool storesCached = false;

  @override
  Future<List<Item>> fetchItems() async {
    if (!itemsCached) {
      await Future.delayed(Duration(milliseconds: 500));
      itemsCached = true;
    }
    return items;
  }

  @override
  Future<Item?> fetchItem(String itemId) async {
    await fetchItems();
    return items.firstWhere((item) => item.id == itemId);
  }

  @override
  Future<List<Store>> fetchStores() async {
    if (!storesCached) {
      await Future.delayed(Duration(milliseconds: 500));
      storesCached = true;
    }
    return stores;
  }

  @override
  Future<Store?> fetchStore(String storeId) async {
    await fetchStores();
    return stores.firstWhere((store) => store.id == storeId);
  }

  @override
  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess();
  }
  
  @override
  Future<void> authenticateUser(String usernameOrEmail, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    onSuccess();
  }
}

final ApiService apiService = MockApiService();

