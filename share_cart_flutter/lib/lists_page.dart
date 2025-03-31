import 'package:flutter/material.dart';

class ListsPage extends StatefulWidget {
  @override
  State createState() => _ListsPageState();
}

class _ListsPageState extends State<ListsPage> {
  @override
  Widget build(BuildContext context) {
    return ListView(
      children: [
        ListTile(
          title: Text("List 1")
        ),
        ListTile(
          title: Text("List 2")
        )
      ],
    );
  }
}
