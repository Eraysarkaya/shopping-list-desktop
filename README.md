# Shopping List Desktop

A Java Swing learning project for managing personal shopping lists and products with a local SQLite database.

## Features

- Registration and login
- Multiple shopping lists per user
- Product name, brand, URL, quantity, unit, and price
- Completed-item tracking and list totals
- Local SQLite database initialized on first run
- PBKDF2 password hashing with automatic upgrade of legacy test accounts

## Requirements

- JDK 17 or newer
- Maven 3.9 or newer

## Run

```bash
mvn clean package
java -jar target/shopping-list-desktop-1.0.0.jar
```

The application creates `data/database.db` locally. Runtime databases are excluded from Git.

## Test

```bash
mvn test
```

The initial automated test verifies that passwords are salted, hashed, and compared safely.

## Security note

The original coursework version stored test passwords as plaintext. New registrations use PBKDF2-HMAC-SHA256, and an old local test account is upgraded after its next successful login.

## License

MIT
