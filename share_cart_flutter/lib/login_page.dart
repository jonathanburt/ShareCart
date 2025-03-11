import "package:flutter/material.dart";
import 'package:share_cart_flutter/api_calls.dart';
import 'package:share_cart_flutter/scaffold_page.dart';

class LoginPage extends StatefulWidget{
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  bool loginFailed = false;

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
                  alignment: Alignment.center,
                  padding: const EdgeInsets.all(1),
                  child: Visibility(
                      visible: loginFailed,
                      child: Text("Login attempt failed",
                        style: TextStyle(color: Colors.redAccent),)
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
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const ForgotPasswordPage()));
                  //TODO: Implement Forgot Password Screen
                  
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
                  onPressed: () async {
                    //TODO Implement Login System
                  ApiService apiService = ApiService();
                  apiService.authenticateUser(usernameController.text, passwordController.text, () { //onSuccess
                    if(!context.mounted) return;
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (context) => ScaffoldPage())
                    );
                  },
                  () { //onFailure
                    if(!context.mounted) return;
                    setState(() {
                      loginFailed = true;
                    });
                  }
                  );
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
        ),
      ),
    );

  }
}

class ForgotPasswordPage extends StatefulWidget{
  const ForgotPasswordPage({super.key});

  @override
  State<ForgotPasswordPage> createState() => _ForgotPasswordPage();
}

class _ForgotPasswordPage extends State<ForgotPasswordPage> {
  TextEditingController emailController = TextEditingController();

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
                'Forgot Password?',
                style: TextStyle(
                  color: theme.primaryColor,
                  fontWeight: FontWeight.bold,
                  fontSize: 30
                ),
              ),
            ),
            Container(
                padding: const EdgeInsets.all(10),
                child: TextField(
                  controller: emailController,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'Email'
                  ),
                ),
              ),
            Container(
              height: 50,
              padding: const EdgeInsets.all(10),
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.zero
                  ),
                  backgroundColor: theme.primaryColor,
                  foregroundColor: theme.colorScheme.onPrimary,
                ),
                onPressed: () {
                  //TODO Implement forgot password system, show something to indicate that button was pressed
                },
                child: const Text('Send Email')), //TODO: Possibly change to 'Resend Email' after pressed.
              ),
              TextButton(
              onPressed: () {
                //TODO Verify this is the correct action
                Navigator.pop(context);
              },
              child: const Text('Return to Sign In'))
          ],
        ),
        ),
    );
  }
}
