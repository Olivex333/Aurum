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



