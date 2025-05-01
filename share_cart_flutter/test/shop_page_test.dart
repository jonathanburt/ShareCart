import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/shop_page.dart';

import 'utils.dart';

void main() {
  const int groupId = 1;
  const int listId = 101;
  final DateTime createdAt = DateTime(2025, 5, 1);

  final Map<int, ShareCartItem> mockItems = {
    1: ShareCartItem('Apple', 'Red and juicy', 'Fruit', 1.50, 1, createdAt),
    2: ShareCartItem('Banana', 'Yellow and sweet', 'Fruit', 0.75, 2, createdAt),
  };

  final Map<int, ShareCartList> mockLists = {
    listId: ShareCartList('Grocery', listId, groupId, createdAt, []),
  };

  late MockApiService apiService;

  setUp(() {
    apiService = MockApiService();

    when(() => apiService.fetchItems(groupId)).thenAnswer((_) async => mockItems);
    when(() => apiService.fetchLists(groupId)).thenAnswer((_) async => mockLists);

    when(() => apiService.fetchList(groupId, listId)).thenAnswer((_) async => mockLists[listId]);
  });

  group('ShopPage', () {
    testWidgets('instantiates correctly', (tester) async {
      await pumpSettleTestPage(tester, ShopPage(listId), apiService);
      expect(find.byType(ShopPage), findsOneWidget);
    });

    testWidgets('displays list of items', (tester) async {
      await pumpSettleTestPage(tester, ShopPage(listId), apiService);

      expect(find.text('Apple'), findsOneWidget);
      expect(find.text('Banana'), findsOneWidget);

      expect(find.text('Category: Fruit'), findsNWidgets(2));
      expect(find.text('\$1.50'), findsOneWidget);
      expect(find.text('\$0.75'), findsOneWidget);
    });

    testWidgets('filters items based on search query', (tester) async {
      await pumpSettleTestPage(tester, ShopPage(listId), apiService);

      await tester.enterText(find.byType(TextField), 'banana');
      await tester.pumpAndSettle();

      expect(find.text('Banana'), findsOneWidget);
      expect(find.text('Apple'), findsNothing);
    });
  });
}
