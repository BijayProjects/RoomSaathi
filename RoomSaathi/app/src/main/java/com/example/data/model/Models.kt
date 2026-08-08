package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    BUYER,
    SELLER,
    ADMIN
}

enum class PropertyCategory(val displayName: String) {
    ALL("All Properties"),
    ROOM("Room"),
    APARTMENT("Apartment"),
    HOTEL("Hotel"),
    VILLA("Villa"),
    HOUSE("House"),
    HOSTEL("Hostel"),
    OFFICE("Office"),
    COWORKING("Coworking Space"),
    LAND("Land"),
    COMMERCIAL("Commercial Building")
}

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: PropertyCategory,
    val pricePerNight: Double,
    val originalPrice: Double? = null,
    val locationAddress: String,
    val city: String,
    val province: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewCount: Int,
    val imageUrls: List<String>,
    val amenities: List<String>,
    val houseRules: List<String>,
    val sellerId: String,
    val sellerName: String,
    val sellerAvatar: String,
    val sellerVerified: Boolean = true,
    val isInstantBooking: Boolean = true,
    val isFeatured: Boolean = false,
    val isVerifiedListing: Boolean = true,
    val verificationStatus: String = "APPROVED", // APPROVED, PENDING, REJECTED
    val bedrooms: Int = 1,
    val bathrooms: Int = 1,
    val maxGuests: Int = 2,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val propertyImage: String,
    val propertyCity: String,
    val buyerId: String,
    val buyerName: String,
    val sellerId: String,
    val checkInDate: String,
    val checkOutDate: String,
    val totalNights: Int,
    val guestCount: Int,
    val totalPrice: Double,
    val paymentMethod: String,
    val status: String, // CONFIRMED, PENDING, CANCELLED, COMPLETED
    val qrCodePayload: String,
    val bookingTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val receiverId: String,
    val messageText: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "favorites")
data class SavedFavorite(
    @PrimaryKey val propertyId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val isKycVerified: Boolean = true,
    val avatarUrl: String,
    val joinedDate: String = "Aug 2026"
)

data class ReviewItem(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val rating: Double,
    val date: String,
    val comment: String
)

data class CityDestination(
    val name: String,
    val propertyCount: Int,
    val imageUrl: String
)

@Entity(tableName = "app_versions")
data class AppVersionInfo(
    @PrimaryKey val id: String = "latest_release",
    val appName: String = "RoomSaathi",
    val currentVersion: String = "1.0.0",
    val latestVersion: String = "1.1.0",
    val downloadUrl: String = "https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk",
    val fileSize: String = "28 MB",
    val releaseDate: String = "August 2026",
    val isUpdateAvailable: Boolean = true,
    val isMandatory: Boolean = false,
    val releaseNotes: String = "New Web Platform sync, real-time booking updates, performance optimizations, and bug fixes."
)

data class WebPlatformConfig(
    val siteName: String = "RoomSaathi",
    val webUrl: String = "https://roomsaathi.app",
    val apiBaseUrl: String = "https://api.roomsaathi.app/v1",
    val pwaEnabled: Boolean = true,
    val supportEmail: String = "support@roomsaathi.app"
)
