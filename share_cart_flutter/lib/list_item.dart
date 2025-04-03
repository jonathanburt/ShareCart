import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';
import 'package:share_cart_flutter/api_service.dart';

class ListItemTile extends StatefulWidget {
  final ShareCartListItem listItem;

  const ListItemTile({
    super.key,
    required this.listItem,
  });

  @override
  State createState() => _ListItemTileState();
}

class _ListItemTileState extends State<ListItemTile> {
  late Future<ShareCartItem?> _itemFuture;

  @override
  void initState() {
    super.initState();
    _itemFuture = apiService.fetchItem(widget.listItem.itemId);
  }

  void _incrementQuantity() {
    setState(() {
      widget.listItem.quantity++;
      // TODO: call backend if necessary
    });
  }

  void _decrementQuantity() {
    setState(() {
      if (widget.listItem.quantity > 0) {
        widget.listItem.quantity--;
        // TODO: call backend if necessary
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<ShareCartItem?>(
      future: _itemFuture,
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return const Text("Loading...");
        }

        final item = snapshot.data!;
        return ListTile(
          title: Text(item.name),
          trailing: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text("\$${item.price}"),
              IconButton(
                icon: const Icon(Icons.remove),
                tooltip: 'Remove item from list',
                onPressed: _decrementQuantity,
              ),
              SizedBox(
                width: 24, // Adjust this width as needed
                child: Text(
                  "${widget.listItem.quantity}",
                  textAlign: TextAlign.center,
                ),
              ),
              IconButton(
                icon: const Icon(Icons.add),
                tooltip: 'Add item to list',
                onPressed: _incrementQuantity,
              ),
            ],
          ),
        );
      },
    );
  }
}
