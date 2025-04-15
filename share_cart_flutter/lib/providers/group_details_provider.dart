import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/types.dart';

class GroupDetailsProvider extends ChangeNotifier {
  final int groupId;
  Map<int, ShareCartItem> _items = {};
  Map<int, ShareCartList> _lists = {};
  DateTime? _lastFetchedItems;
  DateTime? _lastFetchedLists;
  Duration cacheDuration = const Duration(minutes: 5);

  GroupDetailsProvider(this.groupId);

  Map<int, ShareCartItem> get items => _items;
  Map<int, ShareCartList> get lists => _lists;

  bool get _shouldRefreshItems =>
      _lastFetchedItems == null || DateTime.now().difference(_lastFetchedItems!) > cacheDuration;

  bool get _shouldRefreshLists =>
      _lastFetchedLists == null || DateTime.now().difference(_lastFetchedLists!) > cacheDuration;

  ShareCartList? getList(int listId){
    return _lists[listId];
  }

  ShareCartItem? getItem(int itemId){
    return _items[itemId];
  }

  Future<void> loadItems({bool forceRefresh = false}) async {
    if (!forceRefresh && !_shouldRefreshItems) return;
    _items = await apiService.fetchItems(groupId);
    _lastFetchedItems = DateTime.now();
    notifyListeners();
  }

  Future<void> loadLists({bool forceRefresh = false}) async {
    if (!forceRefresh && !_shouldRefreshLists) return;
    _lists = await apiService.fetchLists(groupId);
    _lastFetchedLists = DateTime.now();
    notifyListeners();
  }

  Future<void> refreshItem(int itemId) async {
    final item = await apiService.fetchItem(groupId, itemId);
    if (item != null) {
      _items[itemId] = item;
      notifyListeners();
    }
  }

  Future<void> refreshList(int listId) async {
    final list = await apiService.fetchList(groupId, listId);
    if (list != null) {
      _lists[listId] = list;
      notifyListeners();
    }
  }

  //This is not an ideal way of doing this, we should proably cache these changes and submit them later, but that is an issue for later
  Future<void> changeItemQuantity(int listId, int itemId, int quantity) async {
    ShareCartListItem newItem = await apiService.changeItemQuantity(groupId, listId, itemId, quantity);
    ShareCartList list = lists[listId] as ShareCartList;
    list.items[list.items.indexWhere((listItem) => listItem.itemId == itemId)] = newItem;
    notifyListeners();
  }

  Future<void> createList(String name) async {
    await apiService.createList(groupId, name);
    await loadLists(forceRefresh: true);
  }

  // Future<void> createItem(String name) async {
  //   await apiService.createItem(name, groupId);
  //   await loadItems(forceRefresh: true);
  // }

  // Future<void> addItemToList(int listId, int itemId, int quantity, bool communal) async {
  //   await apiService.addItemToList(groupId, listId, itemId, quantity, communal);
  //   await refreshList(listId);
  // }
}