import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/pages/login_page.dart';
import 'package:share_cart_flutter/providers/group_provider.dart';
import 'package:share_cart_flutter/providers/invite_provider.dart';

void main() {
  final ApiService apiService = ApiService(baseUrl: "http://localhost:8080");
  runApp(MyApp(apiService: apiService));
}

/// The base MaterialApp widget and entrypoint for the entire Flutter application.
class MyApp extends StatelessWidget {
  final ApiService apiService;
  const MyApp({super.key, required this.apiService});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        Provider<ApiService>(create: (context) => apiService),
        ChangeNotifierProvider(create: (context) => GroupProvider(apiService)),
        ChangeNotifierProvider(create: (context) => InviteProvider(apiService))
      ],
      child: MaterialApp(
        title: 'ShareCart',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: const Color.fromARGB(255, 82, 127, 34), primary: const Color.fromARGB(255, 5, 81, 7)),
          useMaterial3: true,
        ),
        home: const LoginPage(),
      ),
    );
  }
}
