import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/group_page.dart';
import 'package:share_cart_flutter/providers/group_provider.dart';

class HomePage extends StatefulWidget {
  @override
  State createState() => _GroupsHomePageState();
}

class _GroupsHomePageState extends State<HomePage> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<GroupProvider>(context, listen: false).loadGroups();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("My Groups")),
      body: Consumer<GroupProvider>(
        builder: (context, groupProvider, _) {
          final groups = groupProvider.groups;
          return RefreshIndicator(
            onRefresh: () => groupProvider.loadGroups(force: true),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  ElevatedButton.icon(
                    onPressed: () {
                      // TODO: Implement group creation logic via GroupProvider
                    },
                    icon: const Icon(Icons.add),
                    label: const Text('Create Group'),
                    style: ElevatedButton.styleFrom(
                      minimumSize: const Size.fromHeight(50),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Expanded(
                    child: groups.isEmpty
                        ? ListView( // Required so RefreshIndicator works with empty list
                            children: [
                              SizedBox(height: 400),
                              Center(child: Text("No groups found.")),
                            ],
                          )
                        : ListView(
                            children: groups.map((group) {
                              final members = groupProvider.getMembers(group.id);
                              return Card(
                                margin: const EdgeInsets.symmetric(vertical: 8),
                                child: ListTile(
                                  title: Text(group.name),
                                  subtitle: Text(members.map((m) => m.username).join(', ')),
                                  onTap: () {
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (_) => GroupPage(groupId: group.id),
                                      ),
                                    );
                                  },
                                  trailing: IconButton(
                                    onPressed: () {
                                      // TODO: Implement group editing
                                    },
                                    icon: const Icon(Icons.edit),
                                  ),
                                ),
                              );
                            }).toList(),
                          ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
