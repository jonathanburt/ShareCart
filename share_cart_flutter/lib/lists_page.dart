import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/types.dart';

class ListsPage extends StatefulWidget {
  @override
  State createState() => _ListsPageState();
}

class _ListsPageState extends State<ListsPage> {
  List<ShareCartList> lists = [];

  @override
  void initState() {
    super.initState();
    apiService.fetchLists().then((result) => setState(() {
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
            label: Text('Create List'),
            style: ElevatedButton.styleFrom(
              minimumSize: Size.fromHeight(50),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ),
          Expanded(
            child: ListView(
              children: lists.map((list) =>
                ListTile(
                  title: Text(list.name),
                  trailing: IconButton(
                    onPressed: () {}, // TODO: Implement list editing 
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
