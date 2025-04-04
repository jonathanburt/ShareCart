import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/shop_list.dart';

class ListState extends ChangeNotifier {
 var lists = <ShopList>[ShopList("Trader Joes", ["Apples", "Tin Foil"])];

 void addList(ShopList newList) {
   lists.add(newList);
   notifyListeners();
 }
}

class ActiveGroupPage extends StatefulWidget {
  const ActiveGroupPage({super.key});

  @override
  State<ActiveGroupPage> createState() => _ActiveGroupPageState();
}

class _ActiveGroupPageState extends State<ActiveGroupPage> {
  @override
  Widget build(BuildContext context) {
    var appState = context.watch<ListState>();
    final theme = Theme.of(context);
    return Center(
      child: RefreshIndicator(
        onRefresh: () {
          //TODO implement onRefresh
           return Future.delayed(Duration(seconds: 1));
        },
        child: ListView(
          physics: const AlwaysScrollableScrollPhysics(),
          children: <Widget>[
            Center(
              child: Text('Active Lists', style: TextStyle(
                fontSize: 50,
                fontWeight: FontWeight.bold,
                color: theme.primaryColor
              ),),
            ),
            for(var list in appState.lists)
              ListCard(theme: theme, list: list),
          ],
        ),
      ),
    );
  }
}

class ListCard extends StatelessWidget {
  const ListCard({
    super.key,
    required this.theme,
    required this.list
  });

  final ThemeData theme;
  final ShopList list;

  @override
  Widget build(BuildContext context) {
    int items = list.items.length;
    return Card(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(8),
        side: BorderSide(color: theme.colorScheme.inversePrimary, width: 5)
      ),
      elevation: 0,
      clipBehavior: Clip.antiAliasWithSaveLayer,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Container(
            padding: const EdgeInsets.all(15),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Row(
                  children: [
                    Text(list.listName, style: TextStyle(
                      fontSize: 24,
                      color: Colors.grey[800]
                    ),),
                    const Spacer(),
                    PopupMenuButton<String>(
                      itemBuilder: (BuildContext context){
                        return {'Edit List', 'Remove List'}.map((String choice){
                          return PopupMenuItem<String>(
                            value: choice,
                            child: Text(choice),
                            onTap: () {}, //TODO Implement List Screen and Remove List
                          );
                        }).toList();
                      },     
                    ),
                  ],
                ),
                Container(height: 10),
                Text("Example", style: TextStyle(
                  fontSize: 15, color: Colors.grey[700]
                ),),
                //TODO Finish List Cards see this, Stack Post for information on doing tranistion https://stackoverflow.com/questions/54165549/navigate-to-a-new-screen-in-flutter
                Text('Items: $items'),
                Text('Urgent:')
              ],
            ),
          ),
        ],
      ),
    );
  }
}