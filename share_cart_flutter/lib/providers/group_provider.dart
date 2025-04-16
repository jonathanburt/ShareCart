import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/types.dart';

class GroupProvider extends ChangeNotifier {
  List<ShareCartGroup> _groups = [];
  Map<int, List<GroupMember>> _memberLists = {};
  Map<int, List<GroupInvite>> _inviteLists = {};

  DateTime? _lastFetched;
  Duration cacheDuration = const Duration(minutes: 5);

  List<ShareCartGroup> get groups => _groups;
  List<GroupMember> getMembers(int groupId) => _memberLists[groupId] ?? [];
  List<GroupInvite> getInvites(int groupId) => _inviteLists[groupId] ?? [];

  bool get _shouldRefresh =>
      _lastFetched == null || DateTime.now().difference(_lastFetched!) > cacheDuration;

  Future<void> loadGroups({bool force = false}) async {
    if (!force && !_shouldRefresh) return;

    final result = await apiService.fetchGroups();
    _groups = result.map((g) => g.group).toList();
    _memberLists = {
      for (var g in result) g.group.id: g.members,
    };
    _inviteLists = {
      for (var g in result) g.group.id: g.invites,
    };
    _lastFetched = DateTime.now();
    notifyListeners();
  }

  Future<void> createGroup(String name) async {
    try{
      await apiService.createGroup(name);
      await loadGroups(force: true);
    } catch (e){
      rethrow;
    } 
  }
}