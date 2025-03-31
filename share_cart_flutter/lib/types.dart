import 'dart:math';
import 'package:share_cart_flutter/shop_list.dart';

class Location {
  final String address;
  final double latitude;
  final double longitude;

  const Location(this.address, this.latitude, this.longitude);

  double distanceTo(Location other) {
    const double earthRadius = 3963.1;
    
    double dLat = _degreesToRadians(other.latitude - latitude);
    double dLon = _degreesToRadians(other.longitude - longitude);
    
    double a = sin(dLat / 2) * sin(dLat / 2) +
        cos(_degreesToRadians(latitude)) * cos(_degreesToRadians(other.latitude)) *
        sin(dLon / 2) * sin(dLon / 2);
    
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    
    return earthRadius * c;
  }

  double _degreesToRadians(double degrees) {
    return degrees * pi / 180;
  }
}

class ShareCartStore {
  final String id;
  
  final String name;
  final Location location;

  const ShareCartStore(this.name, this.location, this.id);
}

class ShareCartItem {
  final String id;
  final String storeId;
  
  final String name;
  final List<String> keywords;
  final double price;

  const ShareCartItem(this.name, this.keywords, this.price, this.id, this.storeId);
}

class ShareCartList {
  String name;
  final String id;
  final String groupId;

  ShareCartList(this.name, this.id, this.groupId);
}

class ShareCartListItem {
  final String itemId;
  final String listId;
  final String userId;
  bool communal;
  int quantity;

  ShareCartListItem(this.itemId, this.listId, this.userId, this.communal, this.quantity);
}

class ThisUserDetails {
  final String username;
  final String email;
  final int userId;
  final DateTime createdAt;

  const ThisUserDetails(this.username, this.email, this.userId, this.createdAt);
}

class ShallowGroupDetails { //The system will fetch and save these details about all groups the user is a member of
  final String name;
  final int groupId;
  final DateTime createdAt;
  final List<GroupMember> members;

  const ShallowGroupDetails(this.name, this.groupId, this.createdAt, this.members);
}

class DeepGroupDetails { //The system will only fetch the additional details of a group when that group is selected
  final ShallowGroupDetails shallowDetails;
  final List<GroupInvite> invites;
  final List<ShopList> lists;
  final List<ShareCartItem> items;

  const DeepGroupDetails(this.shallowDetails, this.invites, this.lists, this.items);
}

class GroupMember {
  final String username;
  final int userId;
  final GroupRole role;
  final DateTime joinedAt;

  const GroupMember(this.username, this.userId, this.role, this.joinedAt);
}

class GroupInvite {
  final String username;
  final int userId;
  final String invitedAt;

  const GroupInvite(this.username, this.userId, this.invitedAt);
}

enum GroupRole {
  MEMBER,
  SHOPPER,
  ADMIN;
}