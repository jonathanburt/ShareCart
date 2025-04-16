
import 'package:flutter/material.dart';
import 'package:share_cart_flutter/types.dart';

class AddItemToListDialog extends StatefulWidget{
  final ShareCartItem item;
  const AddItemToListDialog({super.key, required this.item});

  @override
  State<StatefulWidget> createState() => _AddItemToListDialogState();
}

class _AddItemToListDialogState extends State<AddItemToListDialog> {
  int quantity = 1;
  bool communal = false;
  
  void _submit() {
    Navigator.of(context).pop(AddItemToList(communal, quantity));
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Row(
        children: [
          Expanded(
            child: Text(
              widget.item.name,
              style: Theme.of(context).textTheme.headlineSmall,
              overflow: TextOverflow.ellipsis,)
            ),
          Text(
            '\$${widget.item.price.toStringAsFixed(2)}',
            style: Theme.of(context).textTheme.bodySmall,
          )
        ],
      ),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Category: ${widget.item.category}',
            style: TextStyle(fontStyle: FontStyle.italic),
          ),
          SizedBox(height: 8),
          Text(
            widget.item.description,
            softWrap: true,
          ),
          SizedBox(height: 16),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconButton(
                    onPressed: () {
                      if (quantity > 1) {
                        setState(() {
                          quantity--;
                        });
                      }
                    },
                    icon: Icon(Icons.remove),
                  ),
                  Text('$quantity'),
                  IconButton(
                    onPressed: () {
                      setState(() {
                        quantity++;
                      });
                    },
                    icon: Icon(Icons.add),
                  ),
                ],
              ),
              Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text('Communal?'),
                  Checkbox(
                    value: communal,
                    onChanged: (bool? value) {
                      setState(() {
                        communal = value!;
                      });
                    },
                  )
                ],
              )
            ],
          )
        ],
      ),
      actionsAlignment: MainAxisAlignment.center,
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: Text('Cancel'),
        ),
        SizedBox(width: 10,),
        ElevatedButton(
          onPressed: _submit,
          child: Text('Add to List'),
        ),
      ],
    );
  }
}