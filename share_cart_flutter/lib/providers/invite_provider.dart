import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/types.dart';

class InviteProvider extends ChangeNotifier {
  List<MyInvite> _invites = [];
  DateTime? _lastFetched;
  Duration cacheDuration = const Duration(minutes: 5);

  List<MyInvite> get invites => _invites;

  bool get _shouldRefresh =>
      _lastFetched == null || DateTime.now().difference(_lastFetched!) > cacheDuration;

  Future<void> loadInvites({bool force = false}) async {
    if (!force && !_shouldRefresh) return;
    _invites = await apiService.fetchInvites();
    _lastFetched = DateTime.now();
    notifyListeners();
  }

  // Future<void> acceptInvite(int groupId) async {
  //   await apiService.acceptInvite(groupId);
  //   await loadInvites(force: true);
  // }

  // Future<void> declineInvite(int groupId) async {
  //   await apiService.declineInvite(groupId);
  //   await loadInvites(force: true);
  // }
}