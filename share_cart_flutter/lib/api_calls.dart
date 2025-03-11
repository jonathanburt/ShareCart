import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiService {
  static const String baseUrl = 'http://localhost:8080'; //The default base address of the Spring Boot server

  Future<void> authenticateUser(String usernameOrEmail, String password, VoidCallback onSuccess, VoidCallback onFailure) async {
    var headers = {
      "Content-Type":
        "application/json",
    };
    var response = await http.post(Uri.parse('$baseUrl/api/auth/signin'),
      headers: headers,
      body: jsonEncode({"usernameOrEmail": usernameOrEmail, "password": password})
    );

    print(response.statusCode);
    print(response.body);

    if(response.statusCode == 200){
      onSuccess.call();
      //TODO Save session cookie?
    } else {
      onFailure.call();
    }
  }
}