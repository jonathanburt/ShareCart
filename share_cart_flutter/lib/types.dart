import 'dart:math';

class Location {
  final String address;
  final double latitude;
  final double longitude;

  const Location(this.address, this.latitude, this.longitude);

  double distanceTo(Location other) {
    const double earthRadius = 3963.1;
    
    double dLat = _degreesToRadians(other.latitude - latitude);
    double dLon = _degreesToRadians(other.longitude - longitude);
    
    double a = sin(dLat / 2) * sin(dLat / 2) +
        cos(_degreesToRadians(latitude)) * cos(_degreesToRadians(other.latitude)) *
        sin(dLon / 2) * sin(dLon / 2);
    
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    
    return earthRadius * c;
  }

  double _degreesToRadians(double degrees) {
    return degrees * pi / 180;
  }
}

class Store {
  final String name;
  final Location location;

  final String id;

  const Store(this.name, this.location, this.id);
}

class Item {
  final String name;
  final List<String> keywords;
  final double price;

  final String id;
  final String storeId;

  const Item(this.name, this.keywords, this.price, this.id, this.storeId);
}
