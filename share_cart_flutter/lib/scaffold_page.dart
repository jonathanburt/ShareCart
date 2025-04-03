import 'package:flutter/material.dart';
import 'package:share_cart_flutter/active_lists_page.dart';
import 'package:share_cart_flutter/group_home.dart';
import 'package:share_cart_flutter/groups_page.dart';
import 'package:share_cart_flutter/search_page.dart';
import 'package:share_cart_flutter/settings_page.dart';

class ScaffoldPage extends StatefulWidget {
  @override
  State createState() => _ScaffoldPageState();
}

class _ScaffoldPageState extends State<ScaffoldPage> {
  final PageController _pageController = PageController();
  
  final List<Widget> pages = [
    ActiveListsPage(),
    GroupsPage(),
    SearchPage(),
    SettingsPage(),
  ];

  final List<String> pageTitles = [
    "Home",
    "Group",
    "Search",
    "Settings"
  ];
  
  int pageIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(pageTitles[pageIndex]), 
        backgroundColor: Theme.of(context).colorScheme.secondary,
        actions: [
          if (pageIndex == 0 || pageIndex == 1) // Show only on Home (list) and Group pages
            IconButton(
              icon: const Icon(Icons.settings),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const SettingsPage()),
                );
              },
            ),
        ],
      ),
      body: PageView(
        controller: _pageController,
        onPageChanged: (int newIndex) {
          setState(() {
            pageIndex = newIndex;
          });
        },
        children: pages,
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: pageIndex,
        onDestinationSelected: (int newPageIndex) {
          _pageController.animateToPage(
            newPageIndex,
            duration: Duration(milliseconds: 300),
            curve: Curves.easeInOut,
          );
        },
        destinations: [
          NavigationDestination(icon: Icon(Icons.home), label: 'Home'),
          NavigationDestination(icon: Icon(Icons.group), label: 'Group'),
          NavigationDestination(icon: Icon(Icons.search), label: 'Search'),
          NavigationDestination(icon: Icon(Icons.settings), label: 'Settings'),
        ],
      ),
    );
  }
}
