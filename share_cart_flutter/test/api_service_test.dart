import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/annotations.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:mockito/mockito.dart';
import 'package:http/http.dart' as http;
import 'package:share_cart_flutter/types.dart'; 

import 'api_service_test.mocks.dart';

@GenerateMocks([http.Client])
void main(){
  
  group('ApiService', () {
    final client = MockClient();
    final realApiService = RealApiService(baseUrl: 'https://jsonplaceholder.typicode.com', client: client);
    const MethodChannel channel = MethodChannel('plugins.it_nomads.com/flutter_secure_storage');
    TestWidgetsFlutterBinding.ensureInitialized();

    setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      if (methodCall.method == 'write') {
        return null; // Simulate a successful write operation
      }
      if (methodCall.method == 'read') {
        return 'mock_value'; // Return a mock value when reading
      }
      if (methodCall.method == 'delete') {
        return null; // Simulate delete success
      }
      return null;
      });
    });

    tearDown(() {
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
    });



    test('returns correctly formed list of ShallowGroupDetails if http call completes succesfully', () async {
      final headers = {
        'Content-Type': 'application/json', // Ensure this matches baseHeaders
      };
      when(client.get(Uri.parse('https://jsonplaceholder.typicode.com/api/group/get/all'), headers: headers,))
        .thenAnswer((_) async => http.Response('[{"name": "NewTestGroup1","groupId": 1,"createdAtFormatted": "Mon, 31 Mar 2025 19:26:01 GMT","members": [{"username": "Jonah","userId": 1,"role": "ADMIN","joinedAtFormatted": "Mon, 31 Mar 2025 19:26:01 GMT"}],"invites": []},{"name": "AnotherNewTestGroup1","groupId": 2,"createdAtFormatted": "Tue, 01 Apr 2025 19:54:12 GMT","members": [{"username": "Jonah","userId": 1,"role": "ADMIN","joinedAtFormatted": "Tue, 01 Apr 2025 19:54:12 GMT"}],"invites": []}]',
          200));
      List<ShallowGroupDetails> list = await realApiService.fetchAllGroups();
      expect(list.length, 2);
      expect(list[0].members.length, 1);
      print(list[0].createdAt.toString());
    });

    test('login creates ThisUserDetails object with correct information and saves correct JWT', () async {
      final headers = {
        'Content-Type': 'application/json', // Ensure this matches baseHeaders
      };
      when(client.post(Uri.parse('https://jsonplaceholder.typicode.com/api/auth/signin'), headers: headers, body: jsonEncode({"username": "Jonah", "password": "password"})))
        .thenAnswer((_) async => http.Response('{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJ1c2VybmFtZSI6IkpvbmFoIiwiaWF0IjoxNzQzNzA1NzcxLCJleHAiOjE3NDM3NDE3NzEsImlzcyI6IlNoYXJlQ2FydEFwcGxpY2F0aW9uU2VydmVyIn0.M5ebtyG98xir_WPDtoBYC0lEFp8MGf3tVM6CzfYr9n4","userId":1,"email":"jbl113@case.edu","createdAtFormatted":"Mon, 31 Mar 2025 19:26:01 GMT"}', 
        200));
      ThisUserDetails userDetails = (await realApiService.authenticateUser('Jonah', 'password', () => {}, ()=>{}))!;
      expect(userDetails.username, 'Jonah');
      expect(userDetails.userId, 1);
      expect(userDetails.email, 'jbl113@case.edu');
      String jwt = (await realApiService.getJWT())!;
      expect(jwt, "mock_value"); //We are mocking flutter_secure_store in this instance
    });
  });
}