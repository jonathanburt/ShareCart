import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/home_page.dart';

import 'utils.dart';

void main() {
  List<GroupReturn> mockGroups = List<GroupReturn>.from([
    (
      group: ShareCartGroup('Group 1', 1, GroupRole.ADMIN, DateTime.now()),
      members: List<GroupMember>.from([]),
      invites: List<GroupInvite>.from([]),
    ),
    (
      group: ShareCartGroup('Group 2', 2, GroupRole.ADMIN, DateTime.now()),
      members: List<GroupMember>.from([]),
      invites: List<GroupInvite>.from([]),
    ),
  ]);

  final apiService = MockApiService();

  setUp(() {
    when(() => apiService.fetchGroups()).thenAnswer(
      (_) async => mockGroups,
    );
  });

  group('HomePage', () {
    testWidgets('instantiates correctly', (tester) async {
      await pumpSettleTestPage(tester, HomePage(), apiService);

      expect(find.byType(HomePage), findsOneWidget);
    });

    testWidgets('displays list of groups', (tester) async {
      await pumpSettleTestPage(tester, HomePage(), apiService);

      expect(find.text('Group 1'), findsOneWidget);
      expect(find.text('Group 2'), findsOneWidget);
    });
  });
}
