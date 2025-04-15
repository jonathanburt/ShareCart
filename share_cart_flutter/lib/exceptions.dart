class ApiConflictException implements Exception{
  final String message;
  ApiConflictException(this.message);
}

class ApiUnauthorizedException implements Exception{
  final String message;
  ApiUnauthorizedException(this.message);
}