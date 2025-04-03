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
  });
}