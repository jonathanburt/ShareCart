import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/pages/home_page.dart';

class MockApiService extends Mock implements ApiService {}

void main() {
  final apiService = MockApiService();

  group('HomePage', () {
    test('can be instantiated', () {
      expect(HomePage(), isNotNull);
    });
  });
}
