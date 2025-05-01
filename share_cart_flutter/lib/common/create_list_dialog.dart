import 'package:flutter/material.dart';

class CreateListDialog extends StatefulWidget {
  const CreateListDialog({super.key});

  @override
  State createState() => _CreateListDialogState();
}

class _CreateListDialogState extends State<CreateListDialog> {
  final TextEditingController _controller = TextEditingController();
  String? _errorText;

  void _submit() {
    final text = _controller.text.trim();
    if (text.isEmpty) {
      setState(() {
        _errorText = 'List name cannot be empty';
      });
      return;
    }
    Navigator.of(context).pop(text); // Return list name
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Create New List'),
      content: TextField(
        controller: _controller,
        decoration: InputDecoration(
          labelText: 'List name',
          errorText: _errorText,
        ),
        autofocus: true,
        onSubmitted: (_) => _submit(),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(), // Cancel
          child: Text('Cancel'),
        ),
        ElevatedButton(
          onPressed: _submit,
          child: Text('Create'),
        ),
      ],
    );
  }
}
