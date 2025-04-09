// import 'package:flutter/material.dart';
// import 'package:share_cart_flutter/api_service.dart';
// import 'package:share_cart_flutter/login_page.dart';

// class SettingsPage extends StatefulWidget {
//   const SettingsPage({super.key});

//   @override
//   State<SettingsPage> createState() => _SettingsPageState();
// }

// class _SettingsPageState extends State<SettingsPage> {
//   bool isLoading = false;
//   String errorMessage = "";

//   void _handleLogout() {
//     setState(() {
//       isLoading = true;
//     });

//     apiService.logOut(() {
//       if (!mounted) return;
//       Navigator.of(context).pushAndRemoveUntil(
//         MaterialPageRoute(builder: (context) => const LoginPage()),
//         (Route<dynamic> route) => false
//       );
//     });
//   }

//   void _handleLeaveGroup(String groupId) {
//     setState(() {
//       isLoading = true;
//     });

//     apiService.leaveGroup(groupId, () {
//       // On success
//       if (!mounted) return;
//       setState(() {
//         isLoading = false;
//       });
//       ScaffoldMessenger.of(context).showSnackBar(
//         const SnackBar(content: Text('Successfully left the group'))
//       );
//     }, (error) {
//       // On failure
//       if (!mounted) return;
//       setState(() {
//         isLoading = false;
//         errorMessage = error;
//       });
//       ScaffoldMessenger.of(context).showSnackBar(
//         SnackBar(content: Text('Failed to leave group: $error'))
//       );
//     });
//   }

//   @override
//   Widget build(BuildContext context) {
//     final theme = Theme.of(context);

//     return Scaffold(
//       appBar: AppBar(
//         title: const Text('Settings'),
//         backgroundColor: theme.primaryColor,
//         foregroundColor: theme.colorScheme.onPrimary,
//       ),
//       body: Stack(
//         children: [
//           ListView(
//             children: [
//               const SizedBox(height: 20),
//               // Account Section
//               Container(
//                 padding: const EdgeInsets.all(16),
//                 child: Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   children: [
//                     Text(
//                       'Account',
//                       style: TextStyle(
//                         fontSize: 20,
//                         fontWeight: FontWeight.bold,
//                         color: theme.primaryColor,
//                       ),
//                     ),
//                     const SizedBox(height: 16),
//                     ElevatedButton(
//                       style: ElevatedButton.styleFrom(
//                         shape: RoundedRectangleBorder(
//                           borderRadius: BorderRadius.zero,
//                         ),
//                         backgroundColor: Colors.red,
//                         foregroundColor: Colors.white,
//                         minimumSize: const Size.fromHeight(50),
//                       ),
//                       onPressed: _handleLogout,
//                       child: const Text('Log Out'),
//                     ),
//                   ],
//                 ),
//               ),
//               const Divider(),
//               // Groups Section
//               Container(
//                 padding: const EdgeInsets.all(16),
//                 child: Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   children: [
//                     Text(
//                       'Groups',
//                       style: TextStyle(
//                         fontSize: 20,
//                         fontWeight: FontWeight.bold,
//                         color: theme.primaryColor,
//                       ),
//                     ),
//                     const SizedBox(height: 16),
//                     FutureBuilder(
//                       future: apiService.getUserGroups(),
//                       builder: (context, snapshot) {
//                         if (snapshot.connectionState == ConnectionState.waiting) {
//                           return const Center(child: CircularProgressIndicator());
//                         }
                        
//                         if (snapshot.hasError) {
//                           return Center(
//                             child: Text(
//                               'Error loading groups: ${snapshot.error}',
//                               style: const TextStyle(color: Colors.red),
//                             ),
//                           );
//                         }

//                         final groups = snapshot.data ?? [];
                        
//                         if (groups.isEmpty) {
//                           return const Center(
//                             child: Text('You are not a member of any groups'),
//                           );
//                         }

//                         return ListView.builder(
//                           shrinkWrap: true,
//                           physics: const NeverScrollableScrollPhysics(),
//                           itemCount: groups.length,
//                           itemBuilder: (context, index) {
//                             final group = groups[index];
//                             return Card(
//                               child: ListTile(
//                                 title: Text(group.name),
//                                 trailing: TextButton(
//                                   style: TextButton.styleFrom(
//                                     foregroundColor: Colors.red,
//                                   ),
//                                   onPressed: () => _handleLeaveGroup(group.id),
//                                   child: const Text('Leave Group'),
//                                 ),
//                               ),
//                             );
//                           },
//                         );
//                       },
//                     ),
//                   ],
//                 ),
//               ),
//             ],
//           ),
//           if (isLoading)
//             Container(
//               color: Colors.black54,
//               child: const Center(
//                 child: CircularProgressIndicator(),
//               ),
//             ),
//         ],
//       ),
//     );
//   }
// } 