import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiService {
  static const String baseUrl = "http://localhost:8080"; //The default base address of the Spring Boot server

  Future<Map<String,dynamic>?> authenticateUser(String usernameOrEmail, String password) async {
    var headers = {
      "Content-Type":
        "application/json"
    };
    var request = http.Request('POST', Uri.parse('$baseUrl/api/auth/signin'));
    request.body = jsonEncode({"usernameOrEmail": usernameOrEmail, "password": password});
    request.headers.addAll(headers);
    http.StreamedResponse response = await request.send();

    if(response.statusCode == 200){
      return jsonDecode(await response.stream.bytesToString());
      //TODO Save session cookie?
    } else {
      return null;
    }
  }
}