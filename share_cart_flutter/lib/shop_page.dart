import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:share_cart_flutter/api_service.dart';

class ShopPage extends StatefulWidget {
  @override
  State createState() => _SearchPageState();
}

class _SearchPageState extends State<ShopPage> {

  List<Item> items = [];
  String sortByValue = "alphabetical";
  Location userLocation = Location("123 Broadway, New York, NY 10001, USA", 40.7099, -74.0113);

  @override
  void initState() {
    super.initState();
    apiService.fetchItems().then((result) => setState(() {
      items = result;
      sortItems();
    }));
  }

  @override
  Widget build(BuildContext context) {
    return Center(
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
                  onChanged: (value) async {
                    await filterItems(value);
                    await sortItems();
                    setState(() {
                      items = items;
                    });
                  },
                ),
                Row(
                  children: [
                    Text("Sort by "),
                    DropdownButton<String>(
                      value: sortByValue,
                      onChanged: (String? value) async {
                        sortByValue = value!;
                        await sortItems();
                        setState(() {
                          items = items;
                        });
                      },
                      items: [
                        DropdownMenuItem<String>(value: "alphabetical", child: Text("alphabetical")),
                        DropdownMenuItem<String>(value: "price", child: Text("price")),
                        DropdownMenuItem<String>(value: "distance", child: Text("distance")),
                      ],
                      isDense: true,
                    )
                  ]
                ),
                Visibility(
                  visible: sortByValue == "distance",
                  child: Row(
                    children: [
                      Text("Current Location: "),
                      Text(userLocation.address, style: TextStyle(fontSize: 10)) // TODO: Implement location selector to change user location
                    ]
                  )
                )
              ]
            )
          ),
          Expanded(
            child: ListView(
              children: items.map((item) => 
                ListTile(
                  title: Text(item.name),
                  subtitle: FutureBuilder<Store?>(
                    future: apiService.fetchStore(item.storeId),
                    builder: (context, snapshot) {
                      Store? store = snapshot.data;
                      return store == null ? Text("Loading...") : Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(store.name, style: TextStyle(fontSize: 12)),
                          Text(store.location.address, style: TextStyle(fontSize: 10)),
                          Text("${(10 * store.location.distanceTo(userLocation)).round() / 10} mi", style: TextStyle(fontSize: 10)),
                        ],
                      );
                    }
                  ),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text("\$${item.price}"),
                      IconButton(
                        icon: const Icon(Icons.add),
                        tooltip: 'Add item to list',
                        onPressed: () {
                          print("User wants to add ${item.name} to some list."); // TODO: implement adding item to list from search page
                        },
                      ),
                    ]
                  )
                )
              ).toList()
            )
          )
        ]
      )
    );
  }

  Future<void> filterItems(String value) async {
    items = (await apiService.fetchItems()).where((item) {
      String valueLower = value.toLowerCase();
      return item.name.toLowerCase().contains(valueLower) || item.keywords.any((keyword) => keyword.toLowerCase().contains(valueLower));
    }).toList();
  }

  Future<void> sortItems() async {
    
    if (sortByValue == "alphabetical") {
      items.sort((a, b) => a.name.compareTo(b.name));
    }
    else if (sortByValue == "price") {
      items.sort((a, b) => (100 * (a.price - b.price)).round());
    }
    else if (sortByValue == "distance") {

      var mappedItems = await Future.wait(items.map((item) async {
        double distance = (await apiService.fetchStore(item.storeId))?.location.distanceTo(userLocation) ?? 10000;
        return (distance, item);
      }));

      mappedItems.sort((a, b) => (100 * (a.$1 - b.$1)).round());

      items = mappedItems.map((mappedItem) => mappedItem.$2).toList();
    }
  }
}
