import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/login_screen.dart';
import 'package:share_cart_flutter/group_home.dart';

import 'package:http/http.dart' as http;


void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => ListState(),
      child: MaterialApp(
        title: 'ShareCart',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: const Color.fromARGB(255, 82, 127, 34),
            primary: const Color.fromARGB(255, 5, 81, 7)),
          useMaterial3: true,
        ),
        home: const LoginPage(),
      ),
    );
  }
}

//For HTTP Testing Purposes
Future<http.Response> createUser(String name, String email) async {
  return await http.post(
    Uri.parse('http://localhost:8080/cart/add'),
    body: {'name': name, 'email': email}
  );
}