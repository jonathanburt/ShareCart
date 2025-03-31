import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

// THIS FILE SHOULD BE DEPRECATED, AND IT'S CONTENTS SHOULD BE MADE TO COMPLY WITH THE INTERFACE ESTABLISHED IN api_service.dart
// As a temporary file, I am replacing references to this file but not deleting it since it has work that
// is yet to be fully transfered.

class ApiService {
  static const String baseUrl = 'http://localhost:8080'; //The default base address of the Spring Boot server
  static final FlutterSecureStorage _storage = const FlutterSecureStorage(); //TODO make sure this works with target platforms and everyones machines

  Future<void> authenticateUser(String username, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var headers = {
      "Content-Type":
        "application/json",
    };
    var response = await http.post(Uri.parse('$baseUrl/api/auth/signin'),
      headers: headers,
      body: jsonEncode({"username": username, "password": password})
    );

    print(response.statusCode);
    print(response.body);

    if(response.statusCode == 200){
      onSuccess.call();
      var responseJson = jsonDecode(response.body) as Map<String, dynamic>;
      await _storage.write(key: 'AuthToken', value: responseJson['token']);
      print(await _storage.read(key: 'AuthToken'));
    } else {
      onFailure.call();
    }
  }

  Future<void> createUser(String username, String email, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var headers = {
      "Content-Type": "application/json",
    };
    var response = await http.post(
      Uri.parse('$baseUrl/api/auth/signup'),
      headers: headers,
      body: jsonEncode({
        "username": username,
        "email": email,
        "password": password
      })
    );

    print(response.statusCode);
    print(response.body);

    if (response.statusCode == 200) {
      onSuccess.call();
    } else {
      onFailure.call();
    }
  }

  Future<void> logOut(VoidCallback onLogOut) async{
    await _storage.deleteAll(); //Clear all stored data on log out
    onLogOut.call();
    return;
  }
}