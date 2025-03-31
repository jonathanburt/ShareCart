import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:share_cart_flutter/api_service.dart';
import 'package:share_cart_flutter/scaffold_page.dart';

void main() {

  if (apiService is MockApiService) {
    (apiService as MockApiService).fetchDelay = Duration.zero;
  }

  group('ScaffoldPage', () {
    Future<void> pumpScaffoldPage(WidgetTester tester) async {
      await tester.pumpWidget(
        MaterialApp(
          home: ScaffoldPage(),
        ),
      );
    }

    testWidgets('should show nav bar options', (WidgetTester tester) async {
      await pumpScaffoldPage(tester);

      expect(find.text('Groups'), findsAny);
      expect(find.text('Lists'), findsAny);
      expect(find.text('Shop'), findsAny);
    });

    group('ShopPage', () {
      Future<void> pumpShopPage(WidgetTester tester) async {
        await pumpScaffoldPage(tester);
        await tester.tap(find.text('Shop'));
        await tester.pumpAndSettle();
      }

      testWidgets('should render Shop page text', (WidgetTester tester) async {
        await pumpShopPage(tester);

        expect(find.text('Shop'), findsAtLeast(2));
        expect(find.text('Query'), findsOne);
        expect(find.text('Sort by '), findsOne);
        expect(find.text('alphabetical'), findsOne);
      });

      testWidgets('should default to sort by alphabetical', (WidgetTester tester) async {
        await pumpShopPage(tester);

        expect(find.text('Almond Butter'), findsOne);
        expect(find.text('Almond Milk'), findsOne);
        expect(find.text('Avocado'), findsOne);
      });

      testWidgets('should sort by price', (WidgetTester tester) async {
        await pumpShopPage(tester);
        await tester.tap(find.text('alphabetical'));
        await tester.pumpAndSettle();
        await tester.tap(find.text('price'));
        await tester.pumpAndSettle();

        expect(find.text('Organic Bananas'), findsOne);
        expect(find.text('Greek Yogurt'), findsOne);
        expect(find.text('Avocado'), findsOne);
      });

      testWidgets('should sort by distance', (WidgetTester tester) async {
        await pumpShopPage(tester);
        await tester.tap(find.text('alphabetical'));
        await tester.pumpAndSettle();
        await tester.tap(find.text('distance'));
        await tester.pumpAndSettle();

        expect(find.text('Almond Milk'), findsOne);
        expect(find.text('Organic Bananas'), findsOne);
        expect(find.text('Strawberries'), findsOne);
      });

      testWidgets('should show current location when sorting by distance', (WidgetTester tester) async {
        await pumpShopPage(tester);
        await tester.tap(find.text('alphabetical'));
        await tester.pumpAndSettle();
        await tester.tap(find.text('distance'));
        await tester.pumpAndSettle();

        expect(find.text('Current Location: '), findsOne);
      });
    });
  });
}
