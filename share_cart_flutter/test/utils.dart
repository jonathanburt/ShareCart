import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mocktail/mocktail.dart';
import 'package:provider/provider.dart';
import 'package:share_cart_flutter/common/api_service.dart';
import 'package:share_cart_flutter/providers/group_details_provider.dart';
import 'package:share_cart_flutter/providers/group_provider.dart';
import 'package:share_cart_flutter/providers/invite_provider.dart';

class MockApiService extends Mock implements ApiService {}

Future<void> pumpSettleTestPage(
  WidgetTester tester,
  Widget page,
  ApiService apiService,
) async {
  await tester.pumpWidget(
    MultiProvider(
      providers: [
        Provider<ApiService>(create: (_) => apiService),
        ChangeNotifierProvider(create: (_) => GroupProvider(apiService)),
        ChangeNotifierProvider(create: (_) => InviteProvider(apiService)),
        ChangeNotifierProvider(
          create: (_) => GroupDetailsProvider(apiService, 1),
        ),
      ],
      child: MaterialApp(
        home: page,
      ),
    ),
  );

  await tester.pumpAndSettle();
}
