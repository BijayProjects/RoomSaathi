package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RoomSaathiRepository(
    private val propertyDao: PropertyDao,
    private val bookingDao: BookingDao,
    private val chatDao: ChatDao,
    private val favoriteDao: FavoriteDao,
    private val userDao: UserDao,
    private val appVersionDao: AppVersionDao? = null
) {

    val allApprovedProperties: Flow<List<Property>> = propertyDao.getAllApprovedProperties()
    val allAdminProperties: Flow<List<Property>> = propertyDao.getAllPropertiesAdmin()
    val allFavorites: Flow<List<SavedFavorite>> = favoriteDao.getAllFavorites()
    val latestAppVersion: Flow<AppVersionInfo?>? = appVersionDao?.getLatestVersion()

    suspend fun updateAppVersion(versionInfo: AppVersionInfo) {
        appVersionDao?.updateAppVersion(versionInfo)
    }

    fun getPropertiesBySeller(sellerId: String): Flow<List<Property>> =
        propertyDao.getPropertiesBySeller(sellerId)

    fun getPropertiesByCategory(category: PropertyCategory): Flow<List<Property>> =
        propertyDao.getPropertiesByCategory(category)

    fun getBookingsByBuyer(buyerId: String): Flow<List<Booking>> =
        bookingDao.getBookingsByBuyer(buyerId)

    fun getBookingsBySeller(sellerId: String): Flow<List<Booking>> =
        bookingDao.getBookingsBySeller(sellerId)

    fun getAllBookingsAdmin(): Flow<List<Booking>> =
        bookingDao.getAllBookingsAdmin()

    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>> =
        chatDao.getMessagesForConversation(conversationId)

    fun isFavorite(propertyId: String): Flow<Boolean> =
        favoriteDao.isFavorite(propertyId)

    suspend fun getPropertyById(id: String): Property? =
        propertyDao.getPropertyById(id)

    suspend fun addProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    suspend fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }

    suspend fun deleteProperty(property: Property) {
        propertyDao.deleteProperty(property)
    }

    suspend fun updatePropertyStatus(id: String, status: String) {
        propertyDao.updateVerificationStatus(id, status)
    }

    suspend fun createBooking(booking: Booking) {
        bookingDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: String, status: String) {
        bookingDao.updateBookingStatus(bookingId, status)
    }

    suspend fun toggleFavorite(propertyId: String) {
        val currentFavs = favoriteDao.getAllFavorites().first()
        val exists = currentFavs.any { it.propertyId == propertyId }
        if (exists) {
            favoriteDao.removeFavorite(propertyId)
        } else {
            favoriteDao.addFavorite(SavedFavorite(propertyId))
        }
    }

    suspend fun sendMessage(message: ChatMessage) {
        chatDao.insertMessage(message)
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingProps = propertyDao.getAllPropertiesAdmin().first()
        if (existingProps.isNotEmpty()) return

        val sampleProperties = listOf(
            Property(
                id = "prop_101",
                title = "Skyline Luxury Studio & Private Room",
                description = "Modern furnished private studio room with high-speed 1Gbps WiFi, dedicated workspace, attached bath, and panoramic city views. Located near central metro and dining hubs.",
                category = PropertyCategory.ROOM,
                pricePerNight = 45.0,
                originalPrice = 60.0,
                locationAddress = "Block B, Tech Park Road, Sector 62",
                city = "Kathmandu",
                province = "Bagmati",
                latitude = 27.7172,
                longitude = 85.3240,
                rating = 4.9,
                reviewCount = 128,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = listOf("1Gbps WiFi", "AC", "Attached Bath", "Work Desk", "Power Backup", "Kitchen Access"),
                houseRules = listOf("No smoking indoors", "Quiet hours after 10 PM", "Valid ID required at check-in"),
                sellerId = "seller_001",
                sellerName = "Aarav Sharma",
                sellerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = true,
                isFeatured = true,
                isVerifiedListing = true,
                verificationStatus = "APPROVED",
                bedrooms = 1,
                bathrooms = 1,
                maxGuests = 2
            ),
            Property(
                id = "prop_102",
                title = "Grand Vista 2BHK Serviced Apartment",
                description = "Fully furnished 2-bedroom luxury apartment with modern kitchen, balcony overlooking lake, smart home lock, and 24/7 security. Perfect for business travelers and families.",
                category = PropertyCategory.APARTMENT,
                pricePerNight = 85.0,
                originalPrice = 110.0,
                locationAddress = "Lakeside Marg, Ward 6",
                city = "Pokhara",
                province = "Gandaki",
                latitude = 28.2096,
                longitude = 83.9856,
                rating = 4.8,
                reviewCount = 94,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1560185007-c5ca9d2c014d?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = listOf("Lake View", "Full Kitchen", "Washing Machine", "Elevator", "Free Parking", "Gym Access"),
                houseRules = listOf("No parties without host notice", "Pets allowed with prior approval"),
                sellerId = "seller_002",
                sellerName = "Priya Adhikari",
                sellerAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = true,
                isFeatured = true,
                isVerifiedListing = true,
                verificationStatus = "APPROVED",
                bedrooms = 2,
                bathrooms = 2,
                maxGuests = 4
            ),
            Property(
                id = "prop_103",
                title = "Himalayan Breeze Heritage Villa",
                description = "Private luxury villa with lush gardens, heated outdoor plunge pool, traditional wood craftsmanship, and private butler service. Breathtaking views of the snow-capped mountain range.",
                category = PropertyCategory.VILLA,
                pricePerNight = 180.0,
                originalPrice = 220.0,
                locationAddress = "Heights Road, Nagarkot",
                city = "Bhaktapur",
                province = "Bagmati",
                latitude = 27.7153,
                longitude = 85.5222,
                rating = 4.95,
                reviewCount = 67,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = listOf("Private Pool", "Mountain View", "Fireplace", "Breakfast Included", "Free WiFi", "Chef on Call"),
                houseRules = listOf("Respect local environment", "No smoking indoors"),
                sellerId = "seller_001",
                sellerName = "Aarav Sharma",
                sellerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = false,
                isFeatured = true,
                isVerifiedListing = true,
                verificationStatus = "APPROVED",
                bedrooms = 3,
                bathrooms = 3,
                maxGuests = 6
            ),
            Property(
                id = "prop_104",
                title = "NomadHub Co-Living & Workspace Room",
                description = "Modern single bedroom in a vibrant digital nomad hostel & coworking hub. Includes ergonomic chair, standing desk, community coffee lounge, and weekly networking events.",
                category = PropertyCategory.HOSTEL,
                pricePerNight = 25.0,
                originalPrice = 35.0,
                locationAddress = "Jhamsikhel, Lalitpur",
                city = "Kathmandu",
                province = "Bagmati",
                latitude = 27.6782,
                longitude = 85.3168,
                rating = 4.75,
                reviewCount = 210,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = listOf("Ergonomic Desk", "Co-working Space", "Coffee Bar", "Community Lounge", "High-Speed WiFi"),
                houseRules = listOf("Maintain quiet in workspace zones", "Self-service kitchen rules"),
                sellerId = "seller_003",
                sellerName = "Rohan Karki",
                sellerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = true,
                isFeatured = false,
                isVerifiedListing = true,
                verificationStatus = "APPROVED",
                bedrooms = 1,
                bathrooms = 1,
                maxGuests = 1
            ),
            Property(
                id = "prop_105",
                title = "Central Plaza Executive Office Space",
                description = "Ready-to-move 500 sq ft office floor in prime commercial building. Features conference room access, glass partitions, central AC, fiber internet, and 24/7 security guard.",
                category = PropertyCategory.OFFICE,
                pricePerNight = 120.0,
                originalPrice = 150.0,
                locationAddress = "New Road Commercial Tower, Fl 4",
                city = "Kathmandu",
                province = "Bagmati",
                latitude = 27.7028,
                longitude = 85.3123,
                rating = 4.85,
                reviewCount = 42,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1497215728101-856f4ea42174?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = listOf("Conference Room", "Fiber Optic Internet", "Central AC", "24/7 Security", "Basement Parking"),
                houseRules = listOf("Commercial use only", "Visitor register mandatory"),
                sellerId = "seller_002",
                sellerName = "Priya Adhikari",
                sellerAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = false,
                isFeatured = false,
                isVerifiedListing = true,
                verificationStatus = "APPROVED",
                bedrooms = 0,
                bathrooms = 2,
                maxGuests = 12
            )
        )

        propertyDao.insertProperties(sampleProperties)

        // Seed initial booking sample
        val sampleBooking = Booking(
            id = "bk_90123",
            propertyId = "prop_101",
            propertyTitle = "Skyline Luxury Studio & Private Room",
            propertyImage = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=800&q=80",
            propertyCity = "Kathmandu",
            buyerId = "buyer_001",
            buyerName = "Subhash Dev",
            sellerId = "seller_001",
            checkInDate = "Aug 15, 2026",
            checkOutDate = "Aug 18, 2026",
            totalNights = 3,
            guestCount = 2,
            totalPrice = 135.0,
            paymentMethod = "Khalti Digital Wallet",
            status = "CONFIRMED",
            qrCodePayload = "ROOMSAATHI-BOOKING-bk_90123-VERIFIED"
        )
        bookingDao.insertBooking(sampleBooking)

        // Seed initial chat sample
        chatDao.insertMessage(
            ChatMessage(
                conversationId = "conv_seller_001_buyer_001",
                senderId = "seller_001",
                senderName = "Aarav Sharma",
                receiverId = "buyer_001",
                messageText = "Namaste Subhash! Thanks for booking Skyline Studio. What time do you expect to arrive on Aug 15?"
            )
        )
        chatDao.insertMessage(
            ChatMessage(
                conversationId = "conv_seller_001_buyer_001",
                senderId = "buyer_001",
                senderName = "Subhash Dev",
                receiverId = "seller_001",
                messageText = "Hello Aarav! I'll be arriving around 2:30 PM. Is early check-in or luggage drop available?"
            )
        )

        // Seed default buyer profile
        userDao.insertOrUpdateProfile(
            UserProfile(
                userId = "buyer_001",
                name = "Subhash Dev",
                email = "subhash.dev@example.com",
                phone = "+977 9841234567",
                role = UserRole.BUYER,
                isKycVerified = true,
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80"
            )
        )
    }
}
