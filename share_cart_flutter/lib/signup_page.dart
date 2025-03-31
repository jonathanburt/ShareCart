import "package:flutter/material.dart";
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/login_page.dart';

class SignUpPage extends StatefulWidget {
  const SignUpPage({super.key});

  @override
  State<SignUpPage> createState() => _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController confirmPasswordController = TextEditingController();
  bool signUpFailed = false;
  String errorMessage = "";

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      body: Center(
        child: ListView(
          shrinkWrap: true,
          children: <Widget>[
            Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.all(10),
              child: Text(
                'ShareCart',
                style: TextStyle(
                  color: theme.primaryColor,
                  fontWeight: FontWeight.w500,
                  fontSize: 30
                ),
              ),
            ),
            Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.all(10),
              child: const Text(
                'Create Account',
                style: TextStyle(fontSize: 20),
              )
            ),
            Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.all(1),
              child: Visibility(
                visible: signUpFailed,
                child: Text(
                  errorMessage,
                  style: const TextStyle(color: Colors.redAccent),
                )
              ),
            ),
            Container(
              padding: const EdgeInsets.all(10),
              child: TextField(
                controller: usernameController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Username',
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.all(10),
              child: TextField(
                controller: emailController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Email',
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.all(10),
              child: TextField(
                obscureText: true,
                controller: passwordController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Password',
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.all(10),
              child: TextField(
                obscureText: true,
                controller: confirmPasswordController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Confirm Password',
                ),
              ),
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
                  if (passwordController.text != confirmPasswordController.text) {
                    setState(() {
                      signUpFailed = true;
                      errorMessage = "Passwords do not match";
                    });
                    return;
                  }

                  apiService.createUser(
                    usernameController.text,
                    emailController.text,
                    passwordController.text,
                    () { // onSuccess
                      if(!context.mounted) return;
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => const LoginPage())
                      );
                    },
                    () { // onFailure
                      if(!context.mounted) return;
                      setState(() {
                        signUpFailed = true;
                        errorMessage = "Failed to create account";
                      });
                    }
                  );
                },
                child: const Text("Sign Up")
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                const Text("Already have an account?"),
                TextButton(
                  onPressed: () {
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (context) => const LoginPage())
                    );
                  },
                  child: const Text(
                    "Sign In",
                    style: TextStyle(fontSize: 15),
                  )
                )
              ],
            )
          ],
        ),
      ),
    );
  }
} 