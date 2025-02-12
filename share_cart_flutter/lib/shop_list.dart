class ShopList {
  final String listName;
  List<String> items; //Replace with Item type once it is defined

  ShopList(this.listName, this.items);

  addItem(String item){
    this.items.add(item);
  }

  removeItem(String item){
    this.items.remove(item);
  }

}

class ShopItem {
  String itemName;

  ShopItem(this.itemName);
}