import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/app_bar.dart';
import 'package:share_cart_flutter/create_group_dialog.dart';
import 'package:share_cart_flutter/create_list_dialog.dart';
import 'package:share_cart_flutter/exceptions.dart';
import 'package:share_cart_flutter/list_page.dart';
import 'package:share_cart_flutter/providers/group_details_provider.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:share_cart_flutter/create_item_dialog.dart';


class GroupPage extends StatefulWidget {
  final ShareCartGroup group;

  const GroupPage({super.key, required this.group});

  @override
  State createState() => _GroupPageState();
}

class _GroupPageState extends State<GroupPage> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final provider = Provider.of<GroupDetailsProvider>(context, listen: false);
      provider.loadLists();
      provider.loadItems();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyAppBar(Icon(Icons.people), widget.group.name),
      body: Consumer<GroupDetailsProvider>(
        builder: (context, groupDetailsProvider, _) {
          final lists = groupDetailsProvider.lists;
          return RefreshIndicator(
            onRefresh: () async => await groupDetailsProvider.loadLists(forceRefresh: true),
            child: Padding(
              padding: EdgeInsets.all(16.0),
              child: Column(
                children: [
                  ElevatedButton.icon(
                    onPressed: () async {
                      final newListName = await showDialog<String>(
                        context: context,
                        builder: (_) => const CreateListDialog(),
                      );
                      if(newListName == null) return;
                      try{
                        await groupDetailsProvider.createList(newListName);
                      } on ApiConflictException catch (e) {
                        if(!context.mounted) return;
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text(e.message))
                        );
                      } on ApiUnauthorizedException catch (e) {
                        if(!context.mounted) return;
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text(e.message))
                        );
                      }
                      
                    },
                    icon: Icon(Icons.add),
                    label: Text('Create List'),
                    style: ElevatedButton.styleFrom(
                      minimumSize: Size.fromHeight(50),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                  ),
                  SizedBox(width: 10),
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: () async {
                        final newItemData = await showDialog<Map<String, dynamic>>(
                          context: context,
                          builder: (context) => const CreateItemDialog(),
                        );
                        if (newItemData != null) {
                          // call provider
                        }
                      },
                      icon: Icon(Icons.add_shopping_cart),
                      label: Text('New Item'),
                    ),
                  ),
                  Expanded(
                    child: ListView(
                      children: lists.entries.map((list) =>
                        ListTile(
                          title: Text(list.value.name),
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(builder: (_) => ChangeNotifierProvider.value(
                                value: Provider.of<GroupDetailsProvider>(context, listen: false),
                                child: ListPage(listId: list.value.id, listName: list.value.name,),))
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
            ),
          );
        }
      )
    );
  }
}
