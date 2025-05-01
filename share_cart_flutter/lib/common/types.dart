import 'dart:io';

/// Front end representation of an item (not yet associated with a list, only with a group).
class ShareCartItem {
  final int id;

  final String name;
  final String description;
  final double price;
  final String category;
  final DateTime createdAt;

  factory ShareCartItem.fromJson(Map<String, dynamic> input) {
    DateTime createdAt = HttpDate.parse(input["createdAtFormatted"]);
    return ShareCartItem(input["name"], input["description"], input["category"], input["price"], input["itemId"], createdAt); //TODO make sure this matches ItemDTO
  }

  const ShareCartItem(this.name, this.description, this.category, this.price, this.id, this.createdAt);
}

/// Front end representation of a list associated with a given group.
class ShareCartList {
  final int id;
  final int groupId;
  final DateTime createdAt;

  List<ShareCartListItem> items = [];

  String name;

  factory ShareCartList.fromJson(Map<String, dynamic> input) {
    DateTime createdAt = HttpDate.parse(input["createdAt"]);
    List<ShareCartListItem> listItems = List.from((input["items"].map((item) => ShareCartListItem.fromJson(item))));
    return ShareCartList(input["name"], input["listId"], input["groupId"], createdAt, listItems);
  }

  ShareCartList(this.name, this.id, this.groupId, this.createdAt, this.items);
}

/// Front end representation of a list-item relation.
class ShareCartListItem {
  final int itemId;
  final int listId;
  final int? userId;
  final DateTime createdAt;

  final bool communal;
  final bool bought;
  int quantity;

  factory ShareCartListItem.fromJson(Map<String, dynamic> input) {
    DateTime createdAt = HttpDate.parse(input["createdAt"]);
    return ShareCartListItem(input["itemId"], input["listId"], input["userId"], input["communal"], input["bought"], input["quantity"], createdAt);
  }

  ShareCartListItem(this.itemId, this.listId, this.userId, this.communal, this.bought, this.quantity, this.createdAt);
}

/// Front end representation of a list-item relation during the creation process.
class AddItemToList {
  final int quantity;
  final bool communal;

  AddItemToList(this.communal, this.quantity);
}

/// Front end representation of a group.
class ShareCartGroup {
  final int id;
  final String name;
  final DateTime createdAt;
  final GroupRole role;

  factory ShareCartGroup.fromJson(Map<String, dynamic> input, GroupRole role) {
    DateTime createdAt = HttpDate.parse(input["createdAtFormatted"]);
    return ShareCartGroup(input["name"], input["groupId"], role, createdAt);
  }

  ShareCartGroup(this.name, this.id, this.role, this.createdAt);
}

/// Front end representation of a user.
class ThisUserDetails {
  final String username;
  final String email;
  final int userId;
  final DateTime createdAt;

  const ThisUserDetails(this.username, this.email, this.userId, this.createdAt);
}

/// Front end representation of an invite to a group.
class MyInvite {
  final String groupName;
  final int groupId;
  final DateTime createdAt;

  factory MyInvite.fromJson(Map<String, dynamic> input) {
    return MyInvite(input["groupName"], input["groupId"], HttpDate.parse(input["invitedAt"]));
  }

  MyInvite(this.groupName, this.groupId, this.createdAt);
}

typedef GroupReturn = ({ShareCartGroup group, List<GroupMember> members, List<GroupInvite> invites});

/// Front end representation of a group-user relation.
class GroupMember {
  final String username;
  final int userId;
  final GroupRole role;
  final DateTime joinedAt;

  factory GroupMember.fromJson(Map<String, dynamic> input) {
    DateTime joinedAtFormatted = HttpDate.parse(input["joinedAtFormatted"]);
    GroupMember member = GroupMember(input["username"], input["userId"], input["role"].toString().groupRole, joinedAtFormatted);
    return member;
  }

  const GroupMember(this.username, this.userId, this.role, this.joinedAt);
}

/// Front end representation of a group invite.
class GroupInvite {
  final String username;
  final int userId;
  final DateTime invitedAt;

  factory GroupInvite.fromJson(Map<String, dynamic> input) {
    DateTime joinedAtFormatted = HttpDate.parse(input["invitedAtFormatted"]);
    GroupInvite invite = GroupInvite(input["username"], input["userId"], joinedAtFormatted);
    return invite;
  }

  const GroupInvite(this.username, this.userId, this.invitedAt);
}

/// Enum defining the role a member has in a group.
enum GroupRole {
  MEMBER,
  SHOPPER,
  ADMIN;
}

/// Definition of mapping between strings and the group role enum.
extension GroupRoleString on String {
  GroupRole get groupRole {
    switch (this) {
      case 'MEMBER':
        return GroupRole.MEMBER;
      case 'SHOPPER':
        return GroupRole.SHOPPER;
      case 'ADMIN':
        return GroupRole.ADMIN;
      default:
        throw UnimplementedError(); //TODO figure out what to throw here
    }
  }
}
