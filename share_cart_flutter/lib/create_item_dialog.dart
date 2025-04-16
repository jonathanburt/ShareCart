import 'package:flutter/material.dart';

class CreateItemDialog extends StatefulWidget {
  const CreateItemDialog({super.key});

  @override
  State createState() => _CreateItemDialogState();
}

class _CreateItemDialogState extends State<CreateItemDialog> {
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _categoryController = TextEditingController();
  final TextEditingController _priceController = TextEditingController();

  String? _error;

  void _submit() {
    final name = _nameController.text.trim();
    final description = _descriptionController.text.trim();
    final category = _categoryController.text.trim();
    final priceText = _priceController.text.trim();

    if (name.isEmpty || priceText.isEmpty) {
      setState(() {
        _error = 'Name and price are required';
      });
      return;
    }

    final price = double.tryParse(priceText);
    if (price == null) {
      setState(() {
        _error = 'Price must be a valid number';
      });
      return;
    }

    Navigator.of(context).pop({
      'name': name,
      'description': description,
      'category': category,
      'price': price,
    });
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Add New Item'),
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (_error != null)
              Padding(
                padding: const EdgeInsets.only(bottom: 8),
                child: Text(_error!, style: TextStyle(color: Colors.red)),
              ),
            TextField(
              controller: _nameController,
              decoration: InputDecoration(labelText: 'Name'),
              autofocus: true,
            ),
            TextField(
              controller: _descriptionController,
              decoration: InputDecoration(labelText: 'Description'),
            ),
            TextField(
              controller: _categoryController,
              decoration: InputDecoration(labelText: 'Category'),
            ),
            TextField(
              controller: _priceController,
              decoration: InputDecoration(labelText: 'Price'),
              keyboardType: TextInputType.numberWithOptions(decimal: true),
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(), // Cancel
          child: Text('Cancel'),
        ),
        ElevatedButton(
          onPressed: _submit,
          child: Text('Add'),
        ),
      ],
    );
  }
}
