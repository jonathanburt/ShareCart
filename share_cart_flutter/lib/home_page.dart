import 'package:flutter/material.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/app_bar.dart';
import 'package:share_cart_flutter/group_page.dart';
import 'package:share_cart_flutter/types.dart';

class HomePage extends StatefulWidget {
  @override
  State createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
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
    return Scaffold(
      appBar: MyAppBar(Icon(Icons.home), "Home"),
      body: Padding(
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
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => GroupPage(group: group))
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
