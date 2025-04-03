import 'package:flutter/material.dart';

class MyAppBar extends StatelessWidget implements PreferredSizeWidget {
  final Icon icon;
  final String title;

  const MyAppBar(this.icon, this.title, {super.key});

  @override
  Widget build(BuildContext context) {
    return AppBar(
      centerTitle: true,
      title: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconTheme(
            data: const IconThemeData(color: Colors.white),
            child: icon,
          ),
          const SizedBox(width: 8),
          Text(
            title,
            style: const TextStyle(
              fontSize: 22,
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
      backgroundColor: Theme.of(context).colorScheme.primary,
      actions: [
        IconButton(
          icon: const Icon(Icons.settings, color: Colors.white),
          onPressed: () {
            // Add your settings navigation or logic here
          },
        ),
      ],
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
