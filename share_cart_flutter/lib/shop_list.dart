class ShopList {
  final String listName;
  List<String> items; //Replace with Item type once it is fully defined

  ShopList(this.listName, this.items);

  addItem(String item){
    items.add(item);
  }

  removeItem(String item){
    items.remove(item);
  }

}

class ShopItem {
  String itemName;

  ShopItem(this.itemName);
}