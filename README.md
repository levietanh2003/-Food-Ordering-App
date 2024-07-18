# FoodMart App
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

## Android Screenshots

<table>
  <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/5cce1d8a-024e-4e4c-8b15-2893c8a66931" alt="Home" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/ee4d18e9-aa6b-4f44-adb4-fc9a5ff2b158" alt="Cart" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/f7b1c92f-eaf7-4738-93dc-e1992fae6d3f" alt="Detail" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/1b7ad926-ee94-4137-84dd-3769b1cd228a" alt="Comment" width="500"/>
    </p>
  </td>
</tr>
    <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/8835f43b-94f8-40a1-8997-a20ee1804e7d" alt="Payout" width="500"/>
    </p>
  </td>
  <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/123eed4b-4d46-4c11-bef8-d0ae0b94f1a1" alt="Payout Momo Method" width="500"/>
    </p>
  </td>
    <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/b42a1d8e-7a3d-427f-9c20-fa38bff54975" alt="Payout ZaloPay Method" width="500"/>
    </p>
  </td>
      <td>
    <p align="center">
      <img src="https://github.com/user-attachments/assets/686818f4-82d6-4887-b8cf-6808a6632fa2" alt="Order History" width="500"/>
    </p>
  </td>
</table>

## Payment Online 
- [ ] TODO: Payment ZalopPay
      
![thanhtoan_zalopay](https://github.com/user-attachments/assets/b7010001-5d20-477e-a04d-fcf7ec0ce38e)

- [ ] TODO: Payment MoMo

![thanhToanMoMo](https://github.com/user-attachments/assets/1a02dbd9-0193-44d8-b3c8-00063dd82768)

## Getting Started
### Prerequisites
- **Android Studio**: Ensure Android Studio is installed on your development machine.
- **Firebase Project**: Create a Firebase project with Authentication, Firestore, and Cloud Messaging enabled.
- **ZaloPay Account**: Set up a ZaloPay merchant account for payment integration.
- **MoMo Account**: Set up a MoMo merchant account for payment integration.
- **LinkYoutobeReview**: https://www.youtube.com/watch?v=pOY3mplv040&t=12s
