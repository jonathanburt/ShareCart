/// Exception used in the case of an API conflict.
class ApiConflictException implements Exception {
  final String message;
  ApiConflictException(this.message);
}

/// Exception used in the case of an unauthorized attempt to see or modify content.
class ApiUnauthorizedException implements Exception {
  final String message;
  ApiUnauthorizedException(this.message);
}

/// Exception used in the case of any unclassified exceptions with the ApiService, including network failures.
class ApiFailureException implements Exception {
  final String message;
  ApiFailureException(this.message);
}
