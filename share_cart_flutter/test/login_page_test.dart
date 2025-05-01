import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/home_page.dart';
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

  group('LoginPage', () {
    testWidgets('instantiates correctly', (WidgetTester tester) async {
      await pumpSettleTestPage(tester, const LoginPage(), apiService);
      expect(find.byType(LoginPage), findsOneWidget);
    });

    testWidgets('displays title and sign-in header', (tester) async {
      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      expect(find.text('ShareCart'), findsOneWidget);
      expect(find.text('Sign In'), findsOneWidget);
    });

    testWidgets('does not show error text initially', (tester) async {
      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      expect(find.text('Login attempt failed'), findsNothing);
    });

    testWidgets('navigates to ForgotPasswordPage on tap', (tester) async {
      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      await tester.tap(find.text('Forgot Password'));
      await tester.pumpAndSettle();

      expect(find.byType(ForgotPasswordPage), findsOneWidget);
    });

    testWidgets('shows error when authenticateUser calls failure', (tester) async {
      when(() => apiService.authenticateUser(
            any(),
            any(),
            any(),
            any(),
          )).thenAnswer((inv) async {
        final onFailure = inv.positionalArguments[3] as VoidCallback;
        onFailure();
      });

      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      await tester.enterText(find.byType(TextField).at(0), 'username');
      await tester.enterText(find.byType(TextField).at(1), 'password');
      await tester.tap(find.text('Login'));
      await tester.pumpAndSettle();

      expect(find.text('Login attempt failed'), findsOneWidget);
    });

    testWidgets('navigates to HomePage on successful login', (tester) async {
      when(() => apiService.authenticateUser(
            any(),
            any(),
            any(),
            any(),
          )).thenAnswer((inv) async {
        final onSuccess = inv.positionalArguments[2] as VoidCallback;
        onSuccess();
      });

      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      await tester.enterText(find.byType(TextField).at(0), 'user');
      await tester.enterText(find.byType(TextField).at(1), 'pass');
      await tester.tap(find.text('Login'));
      await tester.pumpAndSettle();

      expect(find.byType(HomePage), findsOneWidget);
    });

    testWidgets('navigates to SignUpPage on Sign Up tap', (tester) async {
      await pumpSettleTestPage(tester, const LoginPage(), apiService);

      await tester.tap(find.text('Sign Up'));
      await tester.pumpAndSettle();

      expect(find.byType(SignUpPage), findsOneWidget);
    });
  });
}
