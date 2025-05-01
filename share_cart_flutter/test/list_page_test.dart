import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';
import 'package:share_cart_flutter/common/types.dart';
import 'package:share_cart_flutter/pages/group_page.dart';

import 'utils.dart';

void main() {
  const int groupId = 1;
  final DateTime createdAt = DateTime(2025, 5, 1);
  final ShareCartGroup mockGroup = ShareCartGroup(
    'Group 1',
    groupId,
    GroupRole.ADMIN,
    createdAt,
  );

  final Map<int, ShareCartItem> mockItems = {
    1: ShareCartItem('Item 1', 'Desc 1', 'Category', 10.0, 1, createdAt),
    2: ShareCartItem('Item 2', 'Desc 2', 'Category', 20.0, 2, createdAt),
  };

  final List<ShareCartListItem> mockListItems = [
    ShareCartListItem(1, 101, null, false, false, 2, createdAt),
  ];

  final Map<int, ShareCartList> mockLists = {
    101: ShareCartList('List 1', 101, groupId, createdAt, mockListItems),
  };

  late MockApiService apiService;

  setUp(() {
    apiService = MockApiService();

    when(() => apiService.fetchGroup(groupId)).thenAnswer((_) async => mockGroup);
    when(() => apiService.fetchItems(groupId)).thenAnswer((_) async => mockItems);
    when(() => apiService.fetchLists(groupId)).thenAnswer((_) async => mockLists);
  });

  group('GroupPage', () {
    testWidgets('instantiates correctly', (WidgetTester tester) async {
      await pumpSettleTestPage(tester, GroupPage(group: mockGroup), apiService);

      expect(find.byType(GroupPage), findsOneWidget);
    });

    testWidgets('displays group name in AppBar', (WidgetTester tester) async {
      await pumpSettleTestPage(tester, GroupPage(group: mockGroup), apiService);

      expect(find.text(mockGroup.name), findsOneWidget);
    });

    testWidgets('renders list titles', (WidgetTester tester) async {
      await pumpSettleTestPage(tester, GroupPage(group: mockGroup), apiService);

      expect(find.text('List 1'), findsOneWidget);
    });
  });
}
