import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/types.dart';

class GroupsPage extends StatefulWidget {
  @override
  State createState() => _GroupsPageState();
}

class _GroupsPageState extends State<GroupsPage> {
  List<ShareCartGroup> lists = [];

  @override
  void initState() {
    super.initState();
    apiService.fetchGroups().then((result) => setState(() {
      lists = result;
    }));
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.all(16.0),
      child: Column(
        children: [
          ElevatedButton.icon(
            onPressed: () {},
            icon: Icon(Icons.add),
            label: Text('Create Group'),
            style: ElevatedButton.styleFrom(
              minimumSize: Size.fromHeight(50),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ),
          Expanded(
            child: ListView(
              children: lists.map((group) =>
                ListTile(
                  title: Text(group.name),
                  trailing: IconButton(
                    onPressed: () {}, // TODO: Implement group editing 
                    icon: Icon(Icons.edit)
                  ))
              ).toList(),
            )
          )
        ]
      )
    );
  }
}
