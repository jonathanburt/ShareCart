import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';

class ShareCartItemWidget extends StatelessWidget {
  final ShareCartItem item;
  final ShareCartListItem listItem;
  final Function(int newQuantity) onQuantityChanged;

  ShareCartItemWidget({
    required this.item,
    required this.listItem,
    required this.onQuantityChanged,
  });

  @override
  Widget build(BuildContext context) {
    final quantity = listItem.quantity;
    return Card(
      margin: EdgeInsets.symmetric(vertical: 8.0),
      child: ListTile(
        contentPadding: EdgeInsets.all(12.0),
        title: Text(item.name, style: TextStyle(fontWeight: FontWeight.bold)),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(item.description),
            SizedBox(height: 8.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Price: \$${item.price.toStringAsFixed(2)}'),
                Text('Quantity: $quantity'),
              ],
            ),
          ],
        ),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(
              icon: Icon(Icons.remove),
              onPressed: () {
                if (quantity > 1) {
                  onQuantityChanged(quantity-1);
                }
              }
            ),
            IconButton(
              icon: Icon(Icons.add),
              onPressed: () {
                onQuantityChanged(quantity+1);
              },
            ),
          ],
        ),
      ),
    );
  }

}