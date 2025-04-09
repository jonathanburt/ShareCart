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
// class ListItemTile extends StatefulWidget {
//   final ShareCartListItem listItem;

//   const ListItemTile({
//     super.key,
//     required this.listItem,
//   });

//   @override
//   State createState() => _ListItemTileState();
// }

// class _ListItemTileState extends State<ListItemTile> {
//   late Future<ShareCartItem?> _itemFuture;

//   @override
//   void initState() {
//     super.initState();
//     _itemFuture = apiService.fetchItem(.listItem.itemId);
//   }

//   void _incrementQuantity() {
//     setState(() {
//       widget.listItem.quantity++;
//       // TODO: call backend if necessary
//     });
//   }

//   void _decrementQuantity() {
//     setState(() {
//       if (widget.listItem.quantity > 0) {
//         widget.listItem.quantity--;
//         // TODO: call backend if necessary
//       }
//     });
//   }

//   @override
//   Widget build(BuildContext context) {
//     return FutureBuilder<ShareCartItem?>(
//       future: _itemFuture,
//       builder: (context, snapshot) {
        
//         if(snapshot.connectionState == ConnectionState.waiting) {
//           return Container(
//             color: Colors.white,
//           );
//         }
//         if (snapshot.hasData) {
//           item = snapshot.data!;
//         }
//         return ListTile(
//           title: Text(item.name),
//           trailing: Row(
//             mainAxisSize: MainAxisSize.min,
//             children: [
//               Text("\$${item.price}"),
//               IconButton(
//                 icon: const Icon(Icons.remove),
//                 tooltip: 'Remove item from list',
//                 onPressed: _decrementQuantity,
//               ),
//               SizedBox(
//                 width: 24, // Adjust this width as needed
//                 child: Text(
//                   "${widget.listItem.quantity}",
//                   textAlign: TextAlign.center,
//                 ),
//               ),
//               IconButton(
//                 icon: const Icon(Icons.add),
//                 tooltip: 'Add item to list',
//                 onPressed: _incrementQuantity,
//               ),
//             ],
//           ),
//         );
//       },
//     );
//   }
// }
