import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';

class GroupsPage extends StatefulWidget {
  @override
  State createState() => _GroupsPageState();
}

class _GroupsPageState extends State<GroupsPage> {
  @override
  Widget build(BuildContext context) {
    return ListView(
      children: [
        ListTile(
          title: Text("Group 1")
        ),
        ListTile(
          title: Text("Group 2")
        )
      ],
    );
  }
}
