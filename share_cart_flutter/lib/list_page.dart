import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/app_bar.dart';
import 'package:share_cart_flutter/list_item.dart';
import 'package:share_cart_flutter/types.dart';

class ListPage extends StatefulWidget {
  final ShareCartList list;

  ListPage({required this.list});

  @override
  State createState() => _ListPageState();
}

class _ListPageState extends State<ListPage> {

  List<ShareCartListItem> listItems = [];

  @override
  void initState() {
    super.initState();
    apiService.fetchListItems(widget.list.id).then((result) => setState(() {
      listItems = result ?? [];
    }));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyAppBar(Icon(Icons.list), widget.list.name),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            ElevatedButton.icon(
              onPressed: () {},
              icon: Icon(Icons.add),
              label: Text('Add items'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size.fromHeight(50),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
            ),
            Expanded(
              child: ListView(
                children: listItems.map((listItem) => ListItemTile(listItem: listItem)).toList(),
              )
            )
          ]
        )
      )
    );
  }
}
