import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/add_item_to_list_dialog.dart';
import 'package:share_cart_flutter/app_bar.dart';
import 'package:share_cart_flutter/providers/group_details_provider.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:share_cart_flutter/api_service.dart';

class ShopPage extends StatefulWidget {
  final int listId;
  ShopPage(this.listId);
  @override
  State createState() => _SearchPageState();
}

class _SearchPageState extends State<ShopPage> {

  String _searchQuery = '';
  String sortByValue = "alphabetical";

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
      appBar: MyAppBar(Icon(Icons.local_grocery_store), "Items"),
      body: Consumer<GroupDetailsProvider>(
        builder: (context, groupDetailsProvider, _){
          var items = groupDetailsProvider.items.values.toList();
          List<ShareCartItem> filteredItems = items
            .where((item) =>
                item.name.toLowerCase().contains(_searchQuery) ||
                item.description.toLowerCase().contains(_searchQuery))
            .toList();
          if (sortByValue == 'alphabetical') {
            filteredItems.sort((a, b) => a.name.compareTo(b.name));
          }  else if (sortByValue == 'price') {
            filteredItems.sort((a, b) => a.price.compareTo(b.price));
          }
          return RefreshIndicator(
            onRefresh: () async {
              await groupDetailsProvider.loadItems(forceRefresh: true);
            },
            child: Center(
              child: Column(
                children: [
                  Padding(
                    padding: EdgeInsets.all(16.0),
                    child: Column(
                      children: [
                        TextField(
                          decoration: InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: 'Query'
                          ),
                          onChanged: (value) {
                            setState(() => _searchQuery = value.toLowerCase());
                          },
                        ),
                        Row(
                          children: [
                            Text("Sort by "),
                            DropdownButton<String>(
                              value: sortByValue,
                              onChanged: (String? value) async {
                                if(value != null){
                                  setState(() => sortByValue = value);
                                }
                              },
                              items: [
                                DropdownMenuItem<String>(value: "alphabetical", child: Text("Alphabetical")),
                                DropdownMenuItem<String>(value: "price", child: Text("Price")),
                              ],
                              isDense: true,
                            )
                          ]
                        ),
                      ]
                    )
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: filteredItems.length,
                      itemBuilder:  (context, index) {
                        final item = filteredItems[index];
                        return ShareCartItemDetailsWidget(item: item, onTap: () async {
                          final newListItem = await showDialog<AddItemToList>(context: context, 
                          builder: (_) => AddItemToListDialog(item: item));
                          if(newListItem == null) return;
                          try{
                            await groupDetailsProvider.addItemToList(widget.listId, item.id, newListItem.quantity, communal: newListItem.communal);
                          } catch (e) {
                            //TODO
                          }
                        });
                      })
                  )
                ]
              )
            ),
          );
        }
      ),
    );
  }
}

class ShareCartItemDetailsWidget extends StatelessWidget {
  final ShareCartItem item;
  final VoidCallback onTap;

  ShareCartItemDetailsWidget({required this.item, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Card(
        margin: EdgeInsets.symmetric(vertical: 8.0),
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                item.name,
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold
                ),
              ),
              SizedBox(height: 8),
              Text(
                'Category: ${item.category}',
                style: TextStyle(
                  fontStyle: FontStyle.italic,
                  color: Colors.grey[700],
                ),
              ),
              SizedBox(height: 8),
              Text(item.description),
              SizedBox(height: 12),
              Text(
                '\$${item.price.toStringAsFixed(2)}',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 16,
                  color: Colors.green[700],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
