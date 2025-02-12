import 'package:flutter/material.dart';
import 'package:provider/provider.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => MyAppState(),
      child: MaterialApp(
        title: 'ShareCart',
        theme: ThemeData(
          // This is the theme of your application.
          //
          // TRY THIS: Try running your application with "flutter run". You'll see
          // the application has a purple toolbar. Then, without quitting the app,
          // try changing the seedColor in the colorScheme below to Colors.green
          // and then invoke "hot reload" (save your changes or press the "hot
          // reload" button in a Flutter-supported IDE, or press "r" if you used
          // the command line to start the app).
          //
          // Notice that the counter didn't reset back to zero; the application
          // state is not lost during the reload. To reset the state, use hot
          // restart instead.
          //
          // This works for code too, not just values: Most code changes can be
          // tested with just a hot reload.
          colorScheme: ColorScheme.fromSeed(seedColor: const Color.fromARGB(255, 82, 127, 34),
            primary: const Color.fromARGB(255, 5, 81, 7)),
          useMaterial3: true,
        ),
        home: const MyHomePage(title: 'ShareCart Demo'),
      ),
    );
  }
}

class MyAppState extends ChangeNotifier {
 int tjItems = 5;
 int tjUrgent = 1;
}


class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
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
              NavigationDestination(
                icon: Icon(Icons.note_add), 
                label: 'New List')
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
        child: ListView(
          children: <Widget>[
            Center(
              child: Text('Active Lists', style: TextStyle(
                fontSize: 50,
                fontWeight: FontWeight.bold,
                color: theme.primaryColor
              ),),
            ),
            ListCard(
              theme: theme,
              listName: 'Trader Joes'),
            // const Text(
            //   'You have pushed the button this many times:',
            // ),
            // Text(
            //   '$_counter',
            //   style: Theme.of(context).textTheme.headlineMedium,
            // ),
          ],
        ),
      ),
      // floatingActionButton: FloatingActionButton(
      //   onPressed: _incrementCounter,
      //   tooltip: 'Increment',
      //   child: const Icon(Icons.add),
      // ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class ListCard extends StatelessWidget {
  const ListCard({
    super.key,
    required this.theme,
    required this.listName
  });

  final ThemeData theme;
  final String listName;

  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();
    int items = appState.tjItems;
    int tjUrgent = appState.tjUrgent;
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
                    Text(listName, style: TextStyle(
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
                Text('Items: $items'),
                Text('Urgent: $tjUrgent')
              ],
            ),
          ),
        ],
      ),
    );
  }
}
