import 'dart:io';
import 'dart:math';

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
  final int id;
  
  final String name;
  final Location location;

  const ShareCartStore(this.name, this.location, this.id);
}

class ShareCartItem {
  final int id;
  
  final String name;
  final String description;
  final double price;
  final String category;
  final DateTime createdAt;

  factory ShareCartItem.fromJson(Map<String, dynamic> input){
    DateTime createdAt = HttpDate.parse(input["createdAtFormatted"]);
    return ShareCartItem(input["name"], input["description"], input["category"], input["price"], input["itemId"], createdAt); //TODO make sure this matches ItemDTO
  }

  const ShareCartItem(this.name, this.description,  this.category, this.price, this.id, this.createdAt);
}

class ShareCartList {
  final int id; 
  final int groupId;
  final DateTime createdAt;

  List<ShareCartListItem> items = [];

  String name;

  factory ShareCartList.fromJson(Map<String, dynamic> input){
    DateTime createdAt = HttpDate.parse(input["createdAt"]);
    List<ShareCartListItem> listItems = List.from((input["items"].map((item) => ShareCartListItem.fromJson(item))));
    return ShareCartList(input["name"], input["listId"], input["groupId"], createdAt, listItems);
  }

  ShareCartList(this.name, this.id, this.groupId, this.createdAt, this.items);
}

class ShareCartListItem {
  final int itemId;
  final int listId;
  final int? userId;
  final DateTime createdAt;

  bool communal;
  int quantity;

  factory ShareCartListItem.fromJson(Map<String, dynamic> input){
    DateTime createdAt = HttpDate.parse(input["createdAt"]);
    return ShareCartListItem(input["itemId"], input["listId"], input["userId"], input["communal"], input["quantity"], createdAt);
  }

  ShareCartListItem(this.itemId, this.listId, this.userId, this.communal, this.quantity, this.createdAt);
}

class ShareCartGroup {
  final int id;
  final String name;
  final DateTime createdAt;
  final GroupRole role;

  factory ShareCartGroup.fromJson(Map<String, dynamic> input, GroupRole role){
    DateTime createdAt = HttpDate.parse(input["createdAtFormatted"]);
    return ShareCartGroup(input["name"], input["groupId"], role, createdAt);
  }


  ShareCartGroup(this.name, this.id, this.role, this.createdAt);
}

class ThisUserDetails {
  final String username;
  final String email;
  final int userId;
  final DateTime createdAt;

  const ThisUserDetails(this.username, this.email, this.userId, this.createdAt);
}

class MyInvite {
  final String groupName;
  final int groupId;
  final DateTime createdAt;

  factory MyInvite.fromJson(Map<String, dynamic> input){
    return MyInvite(input["groupName"], input["groupId"], HttpDate.parse(input["invitedAt"]));
  }

  MyInvite(this.groupName, this.groupId, this.createdAt);
}

typedef GroupReturn = ({ShareCartGroup group, List<GroupMember> members, List<GroupInvite> invites});

class GroupMember {
  final String username;
  final int userId;
  final GroupRole role;
  final DateTime joinedAt;

  factory GroupMember.fromJson(Map<String, dynamic> input){
    DateTime joinedAtFormatted = HttpDate.parse(input["joinedAtFormatted"]);
    GroupMember member = GroupMember(input["username"], input["userId"], input["role"].toString().groupRole, joinedAtFormatted);
    return member;
  }

  const GroupMember(this.username, this.userId, this.role, this.joinedAt);
}

class GroupInvite {
  final String username;
  final int userId;
  final DateTime invitedAt;

  factory GroupInvite.fromJson(Map<String, dynamic> input){
    DateTime joinedAtFormatted = HttpDate.parse(input["invitedAtFormatted"]);
    GroupInvite invite = GroupInvite(input["username"], input["userId"], joinedAtFormatted);
    return invite;
  }

  const GroupInvite(this.username, this.userId, this.invitedAt);
}

enum GroupRole {
  MEMBER,
  SHOPPER,
  ADMIN;
}

extension GroupRoleString on String {
  GroupRole get groupRole {
    switch (this) {
      case 'MEMBER':
        return GroupRole.MEMBER;
      case 'SHOPPER':
        return GroupRole.SHOPPER;
      case 'ADMIN':
        return GroupRole.ADMIN;
      default: throw UnimplementedError(); //TODO figure out what to throw here
    }
  }
}