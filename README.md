# 🌱 Vighnaharta Nursery - Spring Boot Backend Project

This is a backend project built using **Spring Boot + MySQL**, representing a **Plant Nursery Management System**.  
It is designed with **production-level features** like authentication, authorization, secure payments, and plant management.

## ✅ Completed Features

### 1. Authentication & Security
- **Role-based Authentication** (Admin / User)
- **JWT Token-based Security**
- Passwords encrypted using **BCrypt**

### 2. Plant Management
- CRUD operations for plants (Create, Read, Update, Delete)
- Image upload functionality for plant images
- Uses **DTO pattern** for request/response mapping
- Supports advanced fields: quantity, discountPercent, rating, reviews, originLocation, plantingSeason

### 3. Booking & Order Management
- Users can create bookings for plants
- Admin can view all bookings
- Booking status updates: **PENDING → CONFIRMED → CANCELLED → REJECTED**
- Email notifications for booking confirmation & status changes

### 4. Payment Integration
- **Razorpay integration** for online payments
- COD (Cash on Delivery) support
- Payment verification and linking with booking
- Post-payment email & invoice generation

### 5. Cart System
- Add, update, remove plants in cart
- Calculate total price automatically
- Cart linked to user account

### 6. Wishlist
- Users can add/remove plants from wishlist
- Quick access for favorite plants

### 7. Search & Filter
- Search plants by name
- Filter by category and price range
- Combined search & filter for better results

### 8. Discount Coupons
- Apply discount codes to cart
- Validate usage, max usage, and active period

### 9. Rating & Review System
- Users can add ratings and reviews for plants
- Ratings stored and displayed on plant details

### 10. Notifications & Emails
- Booking confirmation emails
- Payment success emails
- Booking status update emails

### 11. PDF Invoice Generation
- Automatically generates invoice after successful payment


---

## 📌 Purpose

This project is built **strictly for learning, interview preparation, and personal growth.**  
Please do not copy or misuse it.

---

## 🛡️ Disclaimer

> This code is developed by **Pavan Dhangude** for learning purposes.  
> It is not intended for commercial use. Any kind of plagiarism or reuse without proper permission is discouraged.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot (2.x)  
- **Database**: MySQL  
- **Security**: JWT Authentication & Spring Security  
- **Payments**: Razorpay / PayPal / UPI Integration  
- **Storage**: Local Directory for Images (`/upload-image/`)  
- **Build Tool**: Maven  


---

## 🔮 Future Scope / Upcoming Features

- Loyalty points / reward system for users  
- Advanced discount campaigns (seasonal offers, flash sales)  
- Multi-payment gateway integration (PayPal, UPI, etc.)  
- Analytics & reporting dashboard for admins  
- Cart & wishlist optimization (real-time stock updates)  
- Review moderation & reporting  
---

## 📂 Project Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/pavanDhangude/vighnaharta-nursery.git
---
   

## 🙏 Acknowledgement

Thanks for visiting this repository.  
Feel free to explore, learn, and connect!


