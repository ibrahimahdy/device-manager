# Device Manager Application

The Device Manager Application is a Spring Boot project that allows you to manage devices and users with assigned devices.

## Technologies Used

- Spring Boot
- H2 Database
- Gradle

## Getting Started

To run the application, you can use the following command:

```bash
./gradlew bootRun
```
The application will start on http://localhost:8080.

## Endpoints
### Create a Device
**POST /devices**

Create a new device with the following attributes:

- `serialNumber` (String, required) - Unique for each device
- `model` (String, required)
- `phoneNumber` (String)

Example:
```json
{
  "serialNumber": "SN789",
  "model": "Model X",
  "phoneNumber": "1234567890"
}
```

### Create a User
**POST /users**
Create a new user with the following attributes:
- firstName (String, required)
- lastName (String, required)
- address (String, required)
- birthday (String in format yyyy-MM-dd, required)

Example:
```json
{
  "firstName": "Adam",
  "lastName": "West",
  "address": "123 Main St",
  "birthday": "1995-01-01"
}
```

### Assign a Device to a User
**POST /users/{userId}/assign-device/{deviceId}**
Assign an existing device to an existing user.

### List Users with Devices
**GET /users/with-devices**
List all users along with the devices assigned to them.

## Unit Tests
The application includes unit tests for the services and controllers. To run the tests, you can use the following command:
```bash
./gradlew test
```

## Database

The application uses an H2 in-memory database for simplicity. You can access the H2 console at http://localhost:8080/h2-console with the following settings:
```
JDBC URL: jdbc:h2:mem:device-manager
Username: sa
Password:
```