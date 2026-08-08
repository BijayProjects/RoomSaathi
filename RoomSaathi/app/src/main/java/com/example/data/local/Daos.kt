package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties WHERE verificationStatus = 'APPROVED' ORDER BY isFeatured DESC, rating DESC")
    fun getAllApprovedProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties ORDER BY createdAt DESC")
    fun getAllPropertiesAdmin(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getPropertiesBySeller(sellerId: String): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: String): Property?

    @Query("SELECT * FROM properties WHERE category = :category AND verificationStatus = 'APPROVED'")
    fun getPropertiesByCategory(category: PropertyCategory): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperties(properties: List<Property>)

    @Update
    suspend fun updateProperty(property: Property)

    @Query("UPDATE properties SET verificationStatus = :status WHERE id = :id")
    suspend fun updateVerificationStatus(id: String, status: String)

    @Delete
    suspend fun deleteProperty(property: Property)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE buyerId = :buyerId ORDER BY bookingTimestamp DESC")
    fun getBookingsByBuyer(buyerId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE sellerId = :sellerId ORDER BY bookingTimestamp DESC")
    fun getBookingsBySeller(sellerId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings ORDER BY bookingTimestamp DESC")
    fun getAllBookingsAdmin(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: String): Booking?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: String, status: String)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<SavedFavorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId)")
    fun isFavorite(propertyId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: SavedFavorite)

    @Query("DELETE FROM favorites WHERE propertyId = :propertyId")
    suspend fun removeFavorite(propertyId: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE userId = :userId")
    fun getUserProfile(userId: String): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)
}

@Dao
interface AppVersionDao {
    @Query("SELECT * FROM app_versions WHERE id = 'latest_release' LIMIT 1")
    fun getLatestVersion(): Flow<AppVersionInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAppVersion(versionInfo: AppVersionInfo)
}
