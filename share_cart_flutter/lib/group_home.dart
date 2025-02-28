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

class ActiveListsPage extends StatefulWidget {
  const ActiveListsPage({super.key, required this.title});

  final String title;

  @override
  State<ActiveListsPage> createState() => _ActiveListsPageState();
}

class _ActiveListsPageState extends State<ActiveListsPage> {
  @override
  Widget build(BuildContext context) {
    var appState = context.watch<ListState>();
    final theme = Theme.of(context);
    
    return Scaffold(
      //AppBar Widget contains the Profile Button
      appBar: AppBar(
        actions: [
          //Profile Button
          RawMaterialButton(
            onPressed: (){},
            elevation: 2.0,
            fillColor: Colors.white,
            constraints: BoxConstraints(minWidth: 0.0),
            padding: EdgeInsets.all(10),
            shape: CircleBorder(),
            child: Icon(
              Icons.person,
              size: 20.0,
            ),
          ),
        ],
        backgroundColor: theme.primaryColor,
        title: Center(child: Text(widget.title, style: TextStyle(
          fontWeight: FontWeight.bold
        ),)),
      ),
      bottomNavigationBar: NavigationBar(
        destinations: [
          NavigationDestination(
            icon: Icon(Icons.home), 
            label: 'Home'),
          NavigationDestination(
            icon: Icon(Icons.search),
            label: 'Search'),
          ],
        ),
      //Drawer is the Hamburger Menu
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: const BoxDecoration(
                gradient: LinearGradient(colors: [Colors.amber, Colors.red]),
              ),
              child: Center(
                child: Text('My Groups',
                style: theme.textTheme.displayMedium!.copyWith(color: theme.colorScheme.onPrimary)),
              ),
            ),
            ListTile(
              title: const Text('Roomies'),
              onTap: () {
                print('Selected 1');
              },
            ),
            ListTile(
              title: const Text('Family'),
              onTap: () {
                print('Selected 2');
              },
            )
          ],
        ),
      ),
      body: Center(
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
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: (){
          appState.addList(ShopList("New List",[""]));
          //createUser("Name", "email@email.com"); HTTP TEST
        },
        child: Icon(Icons.add),
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