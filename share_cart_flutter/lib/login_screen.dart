import "package:flutter/material.dart";
import 'package:share_cart_flutter/group_home.dart';

class LoginPage extends StatefulWidget{
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      body: Padding(
        //TODO try and add shopping cart image
        padding: const EdgeInsets.all(10),
        child: ListView(
          children: <Widget>[
            Container(
              alignment: Alignment.center,
              padding: EdgeInsets.all(10),
              child: Text(
                'ShareCart',
                style: TextStyle(
                  color: theme.primaryColor,
                  fontWeight: FontWeight.w500,
                  fontSize: 30
                ),),
                ),
                Container(
                  alignment: Alignment.center,
                  padding: const EdgeInsets.all(10),
                  child: const Text(
                    'Sign In',
                    style: TextStyle(fontSize: 20),
                  )
                ),
                Container(
                  padding: const EdgeInsets.all(10),
                  child: TextField(
                    controller: usernameController,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Username'
                    ),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.fromLTRB(10, 10, 10, 0),
                  child: TextField(
                    obscureText: true,
                    controller: passwordController,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Password',
                  ),
                ),
              ),
              TextButton(
                onPressed: () {
                  //TODO: Implement Forgot Password Screen
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(builder: (context) => const Placeholder()));
                },
                child: const Text("Forgot Password"),
              ),
              Container(
                height: 50,
                padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.zero
                    ),
                    backgroundColor: theme.primaryColor,
                    foregroundColor: theme.colorScheme.onPrimary
                  ),
                  onPressed: () {
                    //TODO Implement Login System
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (context) => const ActiveListsPage(title: 'ShareCart Demo'))
                    );
                    print(usernameController.text);
                    print(passwordController.text);
                  },
                  child: const Text("Login")
                ),
              ),
              Row(mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const Text("Dont have an account?"),
                  TextButton(
                    onPressed: () {
                      //TODO: Add sign up screen
                      throw UnimplementedError();
                    },
                    child: const Text(
                      "Sign Up",
                      style: TextStyle(fontSize: 15),))
                ],
                
              )
          ],
        ),),
    );

  }
}

// class ForgotPasswordScreen extends StatefulWidget{

// }
