import 'package:flutter/material.dart';
import 'package:share_cart_flutter/groups_page.dart';
import 'package:share_cart_flutter/lists_page.dart';
import 'package:share_cart_flutter/shop_page.dart';

class ScaffoldPage extends StatefulWidget {
  @override
  State createState() => _ScaffoldPageState();
}

class _ScaffoldPageState extends State<ScaffoldPage> {
  final PageController _pageController = PageController();
  
  final List<Widget> pages = [
    GroupsPage(),
    ListsPage(),
    ShopPage()
  ];

  final List<String> pageTitles = [
    "Groups",
    "Lists",
    "Shop"
  ];

  final List<Icon> pageIcons = [
    Icon(Icons.group),
    Icon(Icons.list_alt),
    Icon(Icons.search)
  ];
  
  int pageIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(pageTitles[pageIndex]), backgroundColor: Theme.of(context).colorScheme.secondary),
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
        destinations: List.generate(
          pages.length,
          (index) => NavigationDestination(icon: pageIcons[index], label: pageTitles[index])
        ),
      ),
    );
  }
}
