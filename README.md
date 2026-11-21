# Aurum

A mobile clothing store application created in **Kotlin** using **Jetpack Compose** and a **Node.js/Express** backend with a **PostgreSQL** database. Aurum allows browsing products, registration and login, managing the cart and wishlist — all in a modern, responsive interface.

## Overview

Aurum is a full-stack mobile application that brings the shopping experience straight to your phone. It offers:

- **Product catalog**: Browse and view detailed information about each clothing item.
- **User authentication**: Secure registration and login based on JWT.
- **Cart**: Add products with selected size and color, view cart contents, and remove items.
- **Wishlist**: Save favorite products to return to later.

## Features

- **Product browsing**: Fetching and displaying a list of products from the server.
- **Registration and login**: Secure endpoints for creating accounts and obtaining JWT tokens.
- **Cart management**: Adding products, viewing cart contents, and removing items.
- **Wishlist management**: Adding and removing products from a personal wishlist.

## Technology Stack

- **Android App**:
  - Kotlin
  - Jetpack Compose
  - Retrofit + OkHttp + Gson
  - Coroutines (suspend functions)

- **Backend**:
  - Node.js + Express
  - CORS and JSON middleware
  - JWT authorization (jsonwebtoken)
  - PostgreSQL (node-postgres)
  - Bcrypt for password hashing

## Architecture

- **Repository pattern**: `AuthRepository`, `CartRepository`, etc. encapsulate API call logic.
- **API instances**: Retrofit singletons (`AurumApi`, `CartApiInstance`, `WishlistApiInstance`) handle the network layer.
- **Data transfer objects**: `ProductDto`, `CartItemDto`, `WishlistItemDto` map JSON data.

## API Endpoints

| Method | Endpoint                    | Description                              |
| ------ | --------------------------- | ---------------------------------------- |
| GET    | `/api/products`             | Get all products                         |
| POST   | `/register`                 | Create a new account                     |
| POST   | `/login`                    | Log in and receive JWT                   |
| GET    | `/api/cart`                 | Get cart contents (authorization required) |
| POST   | `/api/cart`                 | Add item to cart (authorization required) |
| DELETE | `/api/cart/:cartItemId`     | Remove item from cart (authorization required) |
| GET    | `/api/wishlist`             | Get wishlist (authorization required)    |
| POST   | `/api/wishlist`             | Add item to wishlist (authorization required) |
| DELETE | `/api/wishlist/:itemId`     | Remove item from wishlist (authorization required) |
| POST   | `/api/products`             | Add product (admin only)                 |
| DELETE | `/api/products`             | Delete products (admin only)             |
| GET    | `/api/users`                | List users (admin only)                  |
| POST   | `/api/users`                | Create user (admin only)                 |
| PUT    | `/api/users/:id/password`   | Set user password (admin only)           |
| DELETE | `/api/users`                | Delete users (admin only)                |

## Security

- Secrets are not stored in the code: the JWT secret and database credentials are read from environment variables (`.env`, excluded from Git; template in `.env.example`). The server refuses to start without a JWT secret of at least 32 characters.
- Admin endpoints require a valid JWT **and** the `admin` role, checked in the database on every request, so revoking the role takes effect immediately.
- The admin panel (`/users-page`, `/products-page`) has its own login page (`/admin-login.html`); the token is kept in `sessionStorage`.
- Passwords are hashed with bcrypt (minimum 8 characters). Login returns the same message for an unknown user and a wrong password.
- All SQL queries are parameterised; ID lists are validated as positive integers. An admin cannot delete their own account.

## Running the backend

Requires Node.js 20.6+ and PostgreSQL.

```bash
cd data-backend/aurum-backend
npm install
cp .env.example .env        # fill in JWT_SECRET and PGPASSWORD
psql -d aurum -f sql/001_role_uzytkownika.sql
npm start
```

To create an administrator, register an account in the app and run:

```sql
UPDATE users SET role = 'admin' WHERE email = 'your@email.com';
```

## Contribution

Contributions are welcome! Feel free to open issues or pull requests for fixes, improvements, or new features.

## License

The project is released under the MIT license.

![photo](https://i.imgur.com/zGBjdKM.png)
![photo](https://i.imgur.com/z3pt7cS.png)
![photo](https://i.imgur.com/2mkxvQ8.png)
![photo](https://i.imgur.com/PqzuNum.png)
![photo](https://i.imgur.com/BdqjVE8.png)
![photo](https://i.imgur.com/kpGgPad.png)
![photo](https://i.imgur.com/ctaoYTK.png)
![photo](https://i.imgur.com/3ZQ8NDu.png)



