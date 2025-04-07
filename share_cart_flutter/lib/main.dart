import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/login_page.dart';
import 'package:share_cart_flutter/group_home.dart';
import 'package:share_cart_flutter/types.dart';

void main() { 
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => ListState(),
      child: MaterialApp(
        title: 'ShareCart',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: const Color.fromARGB(255, 82, 127, 34),
            primary: const Color.fromARGB(255, 5, 81, 7)),
          useMaterial3: true,
        ),
        home: const LoginPage(),
      ),
    );
  }
}

class GroupProvider extends ChangeNotifier { //This may be a bit chunky as far as state notifiers go, if need by it could be split into multiple
  List<ShareCartGroup> _groups = [];
  bool _blockGroupRefresh = false;
  DateTime? _lastTimeFetchedGroups;
  Duration cacheDuration = Duration(minutes: 5);

  List<ShareCartGroup> get groups => _groups;
  set blockGroupRefresh(bool blockGroupRefresh) => _blockGroupRefresh = blockGroupRefresh; //We may want to prevent groups from refreshing if it messes up the widget tree

  Map<int /*groupId*/, List<GroupMember>> _memberLists = {};
  Map<int /*groupId*/, List<GroupInvite>> _inviteLists = {};
  Map<int, List<GroupMember>> get memberLists => _memberLists;
  Map<int, List<GroupInvite>> get inviteLists => _inviteLists;

  Map<int, List<GroupInvite>> _groupInvites = {};
  Map<int, List<GroupInvite>> get myInvites => _groupInvites;
  
  Map<int /*groupId*/, Map<int /*itemId*/, ShareCartItem>> _groupItems = {};
  Map<int /*groupId*/, Map<int /*listId*/, ShareCartList>> _groupLists = {};
  Map<int, DateTime?> _lastTimeFetchedItems = {};
  Map<int, DateTime?> _lastTimeFetchedLists = {};
  Map<int, bool> blockItemsRefresh = {};
  Map<int, bool> blockListsRefresh = {};
  Map<int, Map<int, ShareCartItem>> get groupItems => _groupItems;
  Map<int, Map<int, ShareCartList>> get groupLists => _groupLists;

  bool get shouldRefreshGroups {
    if(_lastTimeFetchedGroups == null) true;
    if(_blockGroupRefresh) false;
    return DateTime.now().difference(_lastTimeFetchedGroups!) > cacheDuration;
  }

  bool _getShouldRefreshGroupsItems(int groupId) {
    if(!_lastTimeFetchedItems.containsKey(groupId)) return true;
    if(blockItemsRefresh.containsKey(groupId) && blockItemsRefresh[groupId]!) return false;
    return DateTime.now().difference(_lastTimeFetchedItems[groupId]!) > cacheDuration;
  }

  bool _getShouldRefreshGroupsLists(int groupId) {
    if(!_lastTimeFetchedLists.containsKey(groupId)) return true;
    if(blockListsRefresh.containsKey(groupId) && blockListsRefresh[groupId]!) return false;
    return DateTime.now().difference(_lastTimeFetchedLists[groupId]!) > cacheDuration;
  }

  Future<void> loadGroupsBasic({bool forceRefresh = false}) async {
    if(!forceRefresh && !shouldRefreshGroups) return;
    _groups = [];
    _memberLists = {};
    _inviteLists = {};
    List<GroupReturn> groupReturn = await apiService.fetchGroups();
    for(GroupReturn groupDetails in groupReturn){
      _groups.add(groupDetails.group);
      _memberLists[groupDetails.group.id] = groupDetails.members;
      _inviteLists[groupDetails.group.id] = groupDetails.invites;
      blockItemsRefresh[groupDetails.group.id] = false;
      blockListsRefresh[groupDetails.group.id] = false;
    }
    _lastTimeFetchedGroups = DateTime.now();
    notifyListeners();
  }

  Future<void> loadGroupLists({required int groupId, bool forceRefresh = false}) async {
    if(!_getShouldRefreshGroupsLists(groupId) && !forceRefresh) return;

    Map<int, ShareCartList> lists = await apiService.fetchLists(groupId);
    _groupLists[groupId] = lists;
    _lastTimeFetchedLists[groupId] = DateTime.now();
    notifyListeners();
  }

  Future<void> loadGroupItems({required int groupId, bool forceRefresh = false}) async {
    if(!_getShouldRefreshGroupsItems(groupId) && !forceRefresh) return;

    Map<int, ShareCartItem> items = await apiService.fetchItems(groupId);
    _groupItems[groupId] = items;
    _lastTimeFetchedItems[groupId] = DateTime.now();
    notifyListeners();
  }

  Future<void> refreshList(int groupId, int listId) async {
    ShareCartList? list = await apiService.fetchList(groupId, listId);
    if(list == null) throw UnimplementedError();
    (_groupLists[groupId] as Map<int, ShareCartList>)[listId] = list;
    notifyListeners();
  }

  Future<void> refreshItem(int groupId, int itemId) async {
    ShareCartItem? item = await apiService.fetchItem(groupId, itemId);
    if(item == null) throw UnimplementedError();
    (_groupItems[groupId] as Map<int, ShareCartItem>)[itemId] = item;
    notifyListeners();
  }

  Future<void> getInvites() async {notifyListeners();}

  Future<void> createItem(String name, int groupId) async {notifyListeners();}
  Future<void> createList(String name, int groupId) async {notifyListeners();}
  Future<void> addItemToList(int groupId, int listId, int itemId, int quantity, bool communal) async {notifyListeners();}
  Future<void> createGroup(String name) async {notifyListeners();}
  Future<void> inviteToGroup(int groupId, String username) async {notifyListeners();}
  Future<void> acceptInvite(int groupId) async {notifyListeners();}
  Future<void> declineInvite(int groupId) async {notifyListeners();}

}
