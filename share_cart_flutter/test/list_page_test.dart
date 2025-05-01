import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/list_page.dart';
import 'package:share_cart_flutter/pages/shop_page.dart';

import 'utils.dart';

class MockApiService extends Mock implements ApiService {}

void main() {
  const int listId = 101;
  const String listName = 'My Test List';

  late MockApiService apiService;

  setUp(() {
    apiService = MockApiService();

    when(() => apiService.fetchItems(any())).thenAnswer((_) async => <int, ShareCartItem>{});
    when(() => apiService.fetchLists(any())).thenAnswer((_) async => <int, ShareCartList>{});
    when(() => apiService.fetchList(any(), any())).thenAnswer((_) async => null);
  });

  group('ListPage', () {
    testWidgets('instantiates correctly', (WidgetTester tester) async {
      await pumpSettleTestPage(
        tester,
        ListPage(listId: listId, listName: listName),
        apiService,
      );

      expect(find.byType(ListPage), findsOneWidget);
    });

    testWidgets('displays list name in AppBar', (WidgetTester tester) async {
      await pumpSettleTestPage(
        tester,
        ListPage(listId: listId, listName: listName),
        apiService,
      );

      expect(find.text(listName), findsOneWidget);
    });

    testWidgets('has an Add items button', (WidgetTester tester) async {
      await pumpSettleTestPage(
        tester,
        ListPage(listId: listId, listName: listName),
        apiService,
      );

      expect(find.text('Add items'), findsOneWidget);
    });

    testWidgets('navigates to ShopPage on Add items tap', (WidgetTester tester) async {
      await pumpSettleTestPage(
        tester,
        ListPage(listId: listId, listName: listName),
        apiService,
      );

      await tester.tap(find.text('Add items'));
      await tester.pumpAndSettle();

      expect(find.byType(ShopPage), findsOneWidget);
    });
  });
}
