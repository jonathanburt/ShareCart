class ApiConflictException implements Exception{
  final String message;
  ApiConflictException(this.message);
}

class ApiUnauthorizedException implements Exception{
  final String message;
  ApiUnauthorizedException(this.message);
}

class ApiFailureException implements Exception{
  final String message;
  ApiFailureException(this.message);
}