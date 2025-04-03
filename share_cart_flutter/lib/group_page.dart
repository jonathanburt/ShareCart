import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/list_page.dart';
import 'package:share_cart_flutter/types.dart';

class GroupPage extends StatefulWidget {
  final ShareCartGroup group;

  GroupPage({required this.group});

  @override
  State createState() => _GroupPageState();
}

class _GroupPageState extends State<GroupPage> {
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
    return Scaffold(
      appBar: AppBar(
        title: Text("${widget.group.name} Lists", style: TextStyle(color: Colors.white)),
        backgroundColor: Theme.of(context).colorScheme.secondary,
      ),
      body: Padding(
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
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => ListPage(list: list))
                      );
                    },
                    trailing: IconButton(
                      onPressed: () {
                        // TODO: Implement group editing
                      },
                      icon: Icon(Icons.edit),
                    ),
                  )
                ).toList(),
              )
            )
          ]
        )
      )
    );
  }
}
