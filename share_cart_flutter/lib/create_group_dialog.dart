import 'package:flutter/material.dart';

class CreateGroupDialog extends StatefulWidget {
  const CreateGroupDialog({super.key});

  @override
  State createState() => _CreateGroupDialogState();
}

class _CreateGroupDialogState extends State<CreateGroupDialog> {
  final TextEditingController _controller = TextEditingController();
  String? _errorText;

  void _submit() {
    final text = _controller.text.trim();
    if (text.isEmpty) {
      setState(() {
        _errorText = 'Group name cannot be empty';
      });
      return;
    }
    Navigator.of(context).pop(text); // Return group name
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Create New Group'),
      content: TextField(
        controller: _controller,
        decoration: InputDecoration(
          labelText: 'Group name',
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
