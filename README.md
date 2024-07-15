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
      <img src="https://private-user-images.githubusercontent.com/105142161/348594156-ca6c82f3-b876-47cc-861e-69bc476064c1.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTU2LWNhNmM4MmYzLWI4NzYtNDdjYy04NjFlLTY5YmM0NzYwNjRjMS5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0yYzg4NTUxNWFjMTE5MTA2YzQwOGNlNWU2YzNiYmQxOTU4MmVkNzVjODg3MDc5ZmE3MDZhNjI1ZGI1MzY3OTE5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.vhiR0CjvuARAvQUAkHskAOW2MsDjUMw5xxNuydox1PE" alt="Home" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594148-06444bdf-bc18-4b67-8d21-092ddd6fc624.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTQ4LTA2NDQ0YmRmLWJjMTgtNGI2Ny04ZDIxLTA5MmRkZDZmYzYyNC5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT04NmYzMWU3MzdmZmUwYWM5OTkxYjkwOTA5NDI3ZWU0MDgzOTczNGRiZWMyYzczOWFkMGEyZmNiNmRkYTFkM2M0JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.I6NpswAgKplRSXd6afS1QMVTGujFRLAeFYMYCxTkvQA" alt="Home Screen Light" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594166-14c69891-a4b7-4f7c-b71e-09a4ce4edc53.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTY2LTE0YzY5ODkxLWE0YjctNGY3Yy1iNzFlLTA5YTRjZTRlZGM1My5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02YWRiZTQ2MWVhYTU0OTU2NWUzOGEyNzk0Njg1NmMxYzMwMzk5MTAwZmVjYjM5MDA3ZmQ5YzYxYWQ0Mzc2NTg4JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.JOw1BN3l7NIgWHdMQlTvt7QNC0ZOqXJDUI3Zfwi4W78" alt="Episodes List Light" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594170-602add67-ff6d-41e2-af4d-422a2f491490.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTcwLTYwMmFkZDY3LWZmNmQtNDFlMi1hZjRkLTQyMmEyZjQ5MTQ5MC5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00M2NlYWU4ZjJlYmQyN2I0MTBiOGM1ZGUzYTlhOGU5M2E2OTBlNjA1MGNhNjgwYzM1MDM1NTIyMTUwZWJmZjY0JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.BdkqmKHhew1wC-Dc2IKxZzlALH8ATIPXLJhQ-dJTy0U" alt="Show Details Dark" width="500"/>
    </p>
  </td>
</tr>
    <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594146-ebc72ea8-81f1-4bba-90b7-504e2698cae4.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTQ2LWViYzcyZWE4LTgxZjEtNGJiYS05MGI3LTUwNGUyNjk4Y2FlNC5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02YmJjZjQyNzBiZGUxY2RjNGM4YjdkYzg5NjM3ZGUyZDljZjFiMzUyMWQ0MDcwY2E0MDRkYmI1YTljOTc0Y2FiJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.n5X39GsA0q1xMZJTw12BWyC3CrNjxyZnoYWCzLbr-2o" alt="Show Details Dark" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://private-user-images.githubusercontent.com/105142161/348594131-4fb98de7-ca76-436c-bbf8-5cbfb4c82a26.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjEwMTUxNzMsIm5iZiI6MTcyMTAxNDg3MywicGF0aCI6Ii8xMDUxNDIxNjEvMzQ4NTk0MTMxLTRmYjk4ZGU3LWNhNzYtNDM2Yy1iYmY4LTVjYmZiNGM4MmEyNi5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcxNVQwMzQxMTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0yMjFiNTA1ZTdkMjdmNDgzZDAyY2M0ZWE2NjZiMDBmYjQwNjcyMTFlMWVkY2Y2NGJhNDMwZTRkN2RkYjlkNWNhJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.DcI1d31fBSL18ZRMEKT6JId2mdn0NzAjFh31YR34Sg8" alt="Episodes List Dark" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="![image](https://github.com/user-attachments/assets/112afd5c-4d3b-4277-8a88-4bbf7c019cac)" alt="Episodes List Dark" width="500"/>
    </p>
  </td>
  
</table>

## Getting Started
### Prerequisites
- **Android Studio**: Ensure Android Studio is installed on your development machine.
- **Firebase Project**: Create a Firebase project with Authentication, Firestore, and Cloud Messaging enabled.
- **ZaloPay Account**: Set up a ZaloPay merchant account for payment integration.
- **MoMo Account**: Set up a MoMo merchant account for payment integration.

