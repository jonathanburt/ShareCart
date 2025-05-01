import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/login_page.dart';
import 'package:share_cart_flutter/pages/signup_page.dart';

import 'utils.dart';

class MockApiService extends Mock implements ApiService {}

void main() {
  late MockApiService apiService;

  setUp(() {
    apiService = MockApiService();

    when(() => apiService.fetchItems(any())).thenAnswer((_) async => <int, ShareCartItem>{});
    when(() => apiService.fetchLists(any())).thenAnswer((_) async => <int, ShareCartList>{});
    when(() => apiService.fetchList(any(), any())).thenAnswer((_) async => null);
    when(() => apiService.fetchGroups()).thenAnswer((_) async => <GroupReturn>[]);
    when(() => apiService.fetchInvites()).thenAnswer((_) async => <MyInvite>[]);
  });

  group('SignUpPage', () {
    testWidgets('instantiates correctly', (WidgetTester tester) async {
      await pumpSettleTestPage(tester, const SignUpPage(), apiService);
      expect(find.byType(SignUpPage), findsOneWidget);
    });

    testWidgets('displays header and title', (tester) async {
      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      expect(find.text('ShareCart'), findsOneWidget);
      expect(find.text('Create Account'), findsOneWidget);
    });

    testWidgets('does not show error text initially', (tester) async {
      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      expect(find.textContaining('Passwords do not match'), findsNothing);
      expect(find.textContaining('Failed to create account'), findsNothing);
    });

    testWidgets('shows password mismatch error', (tester) async {
      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      await tester.enterText(find.byType(TextField).at(2), 'pass1');
      await tester.enterText(find.byType(TextField).at(3), 'pass2');

      await tester.tap(find.widgetWithText(ElevatedButton, 'Sign Up'));
      await tester.pumpAndSettle();

      expect(find.text('Passwords do not match'), findsOneWidget);
    });

    testWidgets('shows failure message when createUser fails', (tester) async {
      when(() => apiService.createUser(
            any(),
            any(),
            any(),
            any(),
            any(),
          )).thenAnswer((inv) async {
        final onFailure = inv.positionalArguments[4] as VoidCallback;
        onFailure();
      });

      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      await tester.enterText(find.byType(TextField).at(2), 'password');
      await tester.enterText(find.byType(TextField).at(3), 'password');

      await tester.tap(find.widgetWithText(ElevatedButton, 'Sign Up'));
      await tester.pumpAndSettle();

      expect(find.text('Failed to create account'), findsOneWidget);
    });

    testWidgets('navigates to LoginPage on successful sign up', (tester) async {
      when(() => apiService.createUser(
            any(),
            any(),
            any(),
            any(),
            any(),
          )).thenAnswer((inv) async {
        final onSuccess = inv.positionalArguments[3] as VoidCallback;
        onSuccess();
      });

      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      await tester.enterText(find.byType(TextField).at(2), 'pwd');
      await tester.enterText(find.byType(TextField).at(3), 'pwd');

      await tester.tap(find.widgetWithText(ElevatedButton, 'Sign Up'));
      await tester.pumpAndSettle();

      expect(find.byType(LoginPage), findsOneWidget);
    });

    testWidgets('navigates to LoginPage on Sign In tap', (tester) async {
      await pumpSettleTestPage(tester, const SignUpPage(), apiService);

      await tester.tap(find.widgetWithText(TextButton, 'Sign In'));
      await tester.pumpAndSettle();

      expect(find.byType(LoginPage), findsOneWidget);
    });
  });
}
