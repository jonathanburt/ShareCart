# Testing

## Front End Tests
Frontend tests are located in `share_cart_flutter/test`. Tests are designed to cover the most important aspects of the major pages in the application.

## Back End Tests
Backend tests are located in SpringBoot/cart/src/test
There are two portions to the tests:
1. The JUnit tests in test/java/org/swe/cart
2. The Postman Request Collection defined as a JSON in test/postman

- The JUnit tests can be run using Gradle (See README.md)
- The Postman tests are run manually using either the Postman app, or Postman VSCode extension (See README.md)

### Test Coverage

The backend tests are primary designed to ensure that when the client calls the REST API, the server properly authenticates the user, performs the correct actions on the database if the user is authroized, and returns correct and well formatted data. These tests are either performed automatically by the JUnit tests, or manually by performing testing using Postman, and verification of DB changes using MySql Workspace.