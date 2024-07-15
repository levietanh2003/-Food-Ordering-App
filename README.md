# FoodMart App
-------------------------
![Check](https://github.com/c0de-wizard/tv-maniac/actions/workflows/build.yml/badge.svg)  ![android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat) ![ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat) [![TvManiac Debug](https://img.shields.io/badge/Debug--Apk-download-green?style=for-the-badge&logo=android)](https://github.com/c0de-wizard/tv-maniac/releases/latest/download/app-debug.apk)
- Android: Compose
## 🚧 Overview 🚧
FoodMart is an innovative mobile application designed for selling a wide variety of baked goods. Built using Kotlin and Firebase, FoodMart provides a seamless and user-friendly experience for customers. They can browse through our diverse range of products, place orders, and track their purchases in real-time. The integration with Firebase ensures robust data management and real-time updates, making the app efficient and reliable.


## Features
### User Authentication
- **Sign Up/Sign In**: Users can securely sign up or log in using Firebase Authentication, which supports email/password authentication.
- **Password Recovery**: Allows users to reset their password via email if forgotten.

### Product Listing
- **Detailed Listings**: Each product comes with a detailed description, price, and high-quality images.
- **Categories**: Products are organized into categories to make browsing easier.
- **Search Functionality**: Users can search for specific products using keywords.

### Shopping Cart
- **Add to Cart**: Users can add multiple items to their cart.
- **Cart Management**: Easily manage cart items, update quantities, or remove items.
- **Checkout**: Secure and straightforward checkout process.

### Order Tracking
- **Real-Time Updates**: Users receive real-time updates on their order status.
- **Order History**: Users can view past orders and re-order with ease.
- **Order Status**: View orders by their status such as pending, shipped, and delivered.
- **Delivery Tracking**: Real-time tracking of the delivery route.

### Reviews and Comments
- **Product Reviews**: Users can leave reviews and rate products.
- **Comments**: Users can comment on products to share their opinions and experiences.

### Product Suggestions
- **Recommendations**: Personalized product suggestions based on user preferences and past orders.

### Online Payment
- **ZaloPay Integration**: Secure online payments through ZaloPay.
- **MoMo Integration**: Secure online payments through MoMo.

### Admin Panel
- **Inventory Management**: Admins can add, update, or remove products from the inventory.
- **Order Management**: View and manage all orders placed by customers.
- **User Management**: Manage user information and handle customer queries.

### Push Notifications
- **Order Updates**: Customers receive notifications about order status changes.
- **Promotions**: Notify users about special offers and discounts.

## Technologies Used
- **Kotlin**: The primary programming language for building the Android application.
- **Firebase**: 
  - **Firebase Authentication**: For secure user authentication.
  - **Firebase Firestore**: For real-time database and efficient data management.
  - **Firebase Storage**: For storing images of the baked goods.
  - **Firebase Cloud Messaging**: For sending push notifications to users.
- **Payment Gateways**:
  - **ZaloPay SDK**: For integrating ZaloPay online payment.
  - **MoMo SDK**: For integrating MoMo online payment.

### Android Screenshots

<table>
  <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594116-1af17a8c-e28c-4585-a00f-a048f9b5af01.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTE2LTFhZjE3YThjLWUyOGMtNDU4NS1hMDBmLWEwNDhmOWI1YWYwMS5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1hYTUxMDY0NjE3ZDUxOWVjOWFmZGYwM2JkOGNmZWNjNGQyOGFhYTZhNGU5N2YxYzEzZmNiNDcwNTkzNTdkYmQ5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.hA4_y1eUKXlobhG2sTek7D9ksmOd4xuMbp8F_i4HISs" alt="Home Screen Light" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://github.com/c0de-wizard/tv-maniac/blob/main/art/AndroidDetailLight.png?raw=true" alt="Home Screen Light" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://github.com/c0de-wizard/tv-maniac/blob/main/art/EpisodeListLight.png?raw=true" alt="Episodes List Light" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://github.com/c0de-wizard/tv-maniac/blob/main/art/AnroidHomeDark.png?raw=true" alt="Show Details Dark" width="500"/>
    </p>
  </td>
</tr>
    <td>
    <p align="center">
      <img src="https://github.com/c0de-wizard/tv-maniac/blob/main/art/AnroidDetailDark.png?raw=true" alt="Show Details Dark" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://github.com/c0de-wizard/tv-maniac/blob/main/art/EpisodeListDark.png?raw=true" alt="Episodes List Dark" width="500"/>
    </p>
  </td>
</table>

## Getting Started
### Prerequisites
- **Android Studio**: Ensure Android Studio is installed on your development machine.
- **Firebase Project**: Create a Firebase project with Authentication, Firestore, and Cloud Messaging enabled.
- **ZaloPay Account**: Set up a ZaloPay merchant account for payment integration.
- **MoMo Account**: Set up a MoMo merchant account for payment integration.

