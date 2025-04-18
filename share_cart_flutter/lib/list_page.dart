import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/app_bar.dart';
import 'package:share_cart_flutter/list_item.dart';
import 'package:share_cart_flutter/providers/group_details_provider.dart';
import 'package:share_cart_flutter/shop_page.dart';
class ListPage extends StatefulWidget {
  final int listId;
  final String listName;
  ListPage({super.key, required this.listId, required this.listName});

  @override
  State createState() => _ListPageState();
}

class _ListPageState extends State<ListPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    
    return Scaffold(
      appBar: MyAppBar(Icon(Icons.list), widget.listName),
      body: Consumer<GroupDetailsProvider>(
        builder: (context, groupDetailsProvider, _) { 
          return RefreshIndicator(
            onRefresh: () => groupDetailsProvider.refreshList(widget.listId),
            child: Padding(
              padding: EdgeInsets.all(16.0),
              child: Column(
                children: [
                  ElevatedButton.icon(
                    onPressed: ()  {
                      Navigator.push(context, 
                      MaterialPageRoute(builder: (_) => ChangeNotifierProvider.value(
                        value: Provider.of<GroupDetailsProvider>(context, listen: false),
                        child: ShopPage(widget.listId),)));
                    },
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
                    child: ListView.builder(
                        itemCount: groupDetailsProvider.getList(widget.listId)?.items.length ?? 0,
                        itemBuilder: (context, index) {
                          final list = groupDetailsProvider.getList(widget.listId);
                          if(list == null) return const SizedBox.shrink();
                          final listItem = list.items[index];
                          final item = groupDetailsProvider.getItem(listItem.itemId);
                          if(item == null) return const SizedBox.shrink();
                          return ShareCartItemWidget(
                            item: item, 
                            listItem: listItem, 
                            onQuantityChanged: (newQuantity) {
                              groupDetailsProvider.changeItemQuantity(list.id, item.id, newQuantity);
                            }
                          );
                          }
                        ),
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
