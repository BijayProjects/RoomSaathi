package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiApiClient
import com.example.auth.AuthResult
import com.example.auth.FirebaseAuthManager
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.RoomSaathiRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface AiQueryState {
    object Idle : AiQueryState
    object Loading : AiQueryState
    data class Success(val responseText: String) : AiQueryState
    data class Error(val errorMessage: String) : AiQueryState
}

class RoomSaathiViewModel(application: Application) : AndroidViewModel(application) {

    val authManager = FirebaseAuthManager(application)

    private val db = AppDatabase.getDatabase(application)
    val repository = RoomSaathiRepository(
        propertyDao = db.propertyDao(),
        bookingDao = db.bookingDao(),
        chatDao = db.chatDao(),
        favoriteDao = db.favoriteDao(),
        userDao = db.userDao(),
        appVersionDao = db.appVersionDao()
    )

    private val _appVersionInfo = MutableStateFlow(
        AppVersionInfo(
            appName = "RoomSaathi",
            currentVersion = "1.0.0",
            latestVersion = "1.1.0",
            downloadUrl = "https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk",
            fileSize = "28 MB",
            releaseDate = "August 2026",
            isUpdateAvailable = true,
            isMandatory = false,
            releaseNotes = "New Web Platform sync, real-time booking push updates, faster room search, and PWA web portal access."
        )
    )
    val appVersionInfo: StateFlow<AppVersionInfo> = _appVersionInfo.asStateFlow()

    private val _showUpdateDialog = MutableStateFlow(false)
    val showUpdateDialog: StateFlow<Boolean> = _showUpdateDialog.asStateFlow()

    fun dismissUpdateDialog() {
        _showUpdateDialog.value = false
    }

    fun checkForApkUpdates() {
        viewModelScope.launch {
            val latest = com.example.api.RoomSaathiApiService.getLatestAppVersion()
            _appVersionInfo.value = latest
            if (latest.isUpdateAvailable) {
                _showUpdateDialog.value = true
            }
        }
    }

    fun publishApkRelease(
        version: String,
        downloadUrl: String,
        fileSize: String,
        releaseNotes: String,
        isMandatory: Boolean
    ) {
        viewModelScope.launch {
            val updated = com.example.api.RoomSaathiApiService.publishNewApkRelease(
                version = version,
                downloadUrl = downloadUrl,
                fileSize = fileSize,
                releaseNotes = releaseNotes,
                isMandatory = isMandatory
            )
            _appVersionInfo.value = updated
            repository.updateAppVersion(updated)
        }
    }

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(
        UserProfile(
            userId = "user_default_001",
            name = "Subhash Dev",
            email = "buyer@roomsaathi.com",
            phone = "+977 9801234567",
            role = UserRole.BUYER,
            isKycVerified = true,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
        )
    )
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow(UserRole.BUYER)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _selectedCategory = MutableStateFlow(PropertyCategory.ALL)
    val selectedCategory: StateFlow<PropertyCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _maxPriceFilter = MutableStateFlow(300.0)
    val maxPriceFilter: StateFlow<Double> = _maxPriceFilter.asStateFlow()

    private val _instantBookingOnly = MutableStateFlow(false)
    val instantBookingOnly: StateFlow<Boolean> = _instantBookingOnly.asStateFlow()

    private val _verifiedOnly = MutableStateFlow(false)
    val verifiedOnly: StateFlow<Boolean> = _verifiedOnly.asStateFlow()

    private val _selectedProperty = MutableStateFlow<Property?>(null)
    val selectedProperty: StateFlow<Property?> = _selectedProperty.asStateFlow()

    private val _aiQueryState = MutableStateFlow<AiQueryState>(AiQueryState.Idle)
    val aiQueryState: StateFlow<AiQueryState> = _aiQueryState.asStateFlow()

    private val _currentUserId = MutableStateFlow("buyer_001")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _currentSellerId = MutableStateFlow("seller_001")
    val currentSellerId: StateFlow<String> = _currentSellerId.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Filtered properties for Buyer
    val properties: StateFlow<List<Property>> = combine(
        repository.allApprovedProperties,
        _selectedCategory,
        _searchQuery,
        _maxPriceFilter,
        _instantBookingOnly,
        _verifiedOnly
    ) { flows: Array<Any> ->
        @Suppress("UNCHECKED_CAST")
        val all = flows[0] as List<Property>
        val category = flows[1] as PropertyCategory
        val query = flows[2] as String
        val maxPrice = flows[3] as Double
        val instantOnly = flows[4] as Boolean
        val verified = flows[5] as Boolean

        all.filter { prop ->
            val matchCategory = category == PropertyCategory.ALL || prop.category == category
            val matchQuery = query.isBlank() ||
                    prop.title.contains(query, ignoreCase = true) ||
                    prop.city.contains(query, ignoreCase = true) ||
                    prop.locationAddress.contains(query, ignoreCase = true)
            val matchPrice = prop.pricePerNight <= maxPrice
            val matchInstant = !instantOnly || prop.isInstantBooking
            val matchVerified = !verified || prop.isVerifiedListing
            matchCategory && matchQuery && matchPrice && matchInstant && matchVerified
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favorites: StateFlow<List<SavedFavorite>> = repository.allFavorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myBookings: StateFlow<List<Booking>> = repository.getBookingsByBuyer("buyer_001").stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val sellerProperties: StateFlow<List<Property>> = repository.getPropertiesBySeller("seller_001").stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val sellerBookings: StateFlow<List<Booking>> = repository.getBookingsBySeller("seller_001").stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val adminProperties: StateFlow<List<Property>> = repository.allAdminProperties.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val adminBookings: StateFlow<List<Booking>> = repository.getAllBookingsAdmin().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setUserRole(role: UserRole) {
        _userRole.value = role
    }

    fun setSelectedCategory(category: PropertyCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMaxPriceFilter(price: Double) {
        _maxPriceFilter.value = price
    }

    fun setInstantBookingOnly(enabled: Boolean) {
        _instantBookingOnly.value = enabled
    }

    fun setVerifiedOnly(enabled: Boolean) {
        _verifiedOnly.value = enabled
    }

    fun selectProperty(property: Property?) {
        _selectedProperty.value = property
    }

    fun toggleFavorite(propertyId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId)
        }
    }

    fun createBooking(
        property: Property,
        checkIn: String,
        checkOut: String,
        nights: Int,
        guests: Int,
        total: Double,
        paymentMethod: String,
        buyerName: String = "Subhash Dev",
        buyerPhone: String = "+977 9801234567",
        specialRequests: String = "",
        onSuccess: (Booking) -> Unit
    ) {
        viewModelScope.launch {
            val bookingId = "bk_${System.currentTimeMillis().toString().takeLast(6)}"
            val newBooking = Booking(
                id = bookingId,
                propertyId = property.id,
                propertyTitle = property.title,
                propertyImage = property.imageUrls.firstOrNull() ?: "",
                propertyCity = property.city,
                buyerId = _currentUserId.value,
                buyerName = buyerName.ifBlank { "Subhash Dev" },
                sellerId = property.sellerId,
                checkInDate = checkIn,
                checkOutDate = checkOut,
                totalNights = nights,
                guestCount = guests,
                totalPrice = total,
                paymentMethod = paymentMethod,
                status = "CONFIRMED",
                qrCodePayload = "ROOMSAATHI-BOOKING-$bookingId-VERIFIED"
            )
            repository.createBooking(newBooking)

            // Auto-send private security message to that specific seller's chat
            val convId = "conv_${property.sellerId}_${_currentUserId.value}"
            val notesTxt = if (specialRequests.isNotBlank()) " Notes: $specialRequests." else ""
            val orderMsg = ChatMessage(
                conversationId = convId,
                senderId = _currentUserId.value,
                senderName = buyerName.ifBlank { "Subhash Dev" },
                receiverId = property.sellerId,
                messageText = "Hello ${property.sellerName}, I have placed a booking for '${property.title}' ($checkIn to $checkOut, $guests guests). Phone: $buyerPhone.$notesTxt",
                timestamp = System.currentTimeMillis()
            )
            repository.sendMessage(orderMsg)

            onSuccess(newBooking)
        }
    }

    fun addPropertyBySeller(
        title: String,
        description: String,
        category: PropertyCategory,
        price: Double,
        address: String,
        city: String,
        province: String,
        amenities: List<String>,
        imageUrls: List<String>,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val newId = "prop_${System.currentTimeMillis().toString().takeLast(5)}"
            val property = Property(
                id = newId,
                title = title,
                description = description,
                category = category,
                pricePerNight = price,
                originalPrice = price * 1.2,
                locationAddress = address,
                city = city,
                province = province,
                latitude = 27.7172,
                longitude = 85.3240,
                rating = 5.0,
                reviewCount = 1,
                imageUrls = if (imageUrls.isNotEmpty()) imageUrls else listOf(
                    "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=800&q=80"
                ),
                amenities = amenities.ifEmpty { listOf("1Gbps WiFi", "AC", "Attached Bath") },
                houseRules = listOf("Standard house rules apply"),
                sellerId = _currentSellerId.value,
                sellerName = "Aarav Sharma",
                sellerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                sellerVerified = true,
                isInstantBooking = true,
                isFeatured = false,
                isVerifiedListing = true,
                verificationStatus = "APPROVED"
            )
            repository.addProperty(property)
            onComplete()
        }
    }

    fun updatePropertyBySeller(
        property: Property,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            repository.updateProperty(property)
            onComplete()
        }
    }

    fun signUpWithEmail(
        email: String,
        pass: String,
        name: String,
        role: UserRole,
        onResult: (String?) -> Unit
    ) {
        authManager.signUpWithEmail(email, pass, name, role) { res ->
            when (res) {
                is AuthResult.Success -> {
                    _currentUserProfile.value = res.userProfile
                    _userRole.value = res.userProfile.role
                    _isLoggedIn.value = true
                    if (res.userProfile.role == UserRole.SELLER) {
                        _currentSellerId.value = res.userProfile.userId
                    } else if (res.userProfile.role == UserRole.BUYER) {
                        _currentUserId.value = res.userProfile.userId
                    }
                    onResult(null)
                }
                is AuthResult.Error -> onResult(res.message)
            }
        }
    }

    fun signInWithEmail(
        email: String,
        pass: String,
        role: UserRole,
        onResult: (String?) -> Unit
    ) {
        authManager.signInWithEmail(email, pass, role) { res ->
            when (res) {
                is AuthResult.Success -> {
                    _currentUserProfile.value = res.userProfile
                    _userRole.value = res.userProfile.role
                    _isLoggedIn.value = true
                    if (res.userProfile.role == UserRole.SELLER) {
                        _currentSellerId.value = res.userProfile.userId
                    } else if (res.userProfile.role == UserRole.BUYER) {
                        _currentUserId.value = res.userProfile.userId
                    }
                    onResult(null)
                }
                is AuthResult.Error -> onResult(res.message)
            }
        }
    }

    fun signInWithGoogle(
        role: UserRole,
        onResult: (String?) -> Unit
    ) {
        authManager.signInWithGoogleToken("google_sample_id_token", role) { res ->
            when (res) {
                is AuthResult.Success -> {
                    _currentUserProfile.value = res.userProfile
                    _userRole.value = res.userProfile.role
                    _isLoggedIn.value = true
                    if (res.userProfile.role == UserRole.SELLER) {
                        _currentSellerId.value = res.userProfile.userId
                    } else if (res.userProfile.role == UserRole.BUYER) {
                        _currentUserId.value = res.userProfile.userId
                    }
                    onResult(null)
                }
                is AuthResult.Error -> onResult(res.message)
            }
        }
    }

    fun signOutUser() {
        authManager.signOut()
        _isLoggedIn.value = false
        _currentUserProfile.value = null
    }

    fun deletePropertyBySeller(
        property: Property,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.deleteProperty(property)
            onComplete?.invoke()
        }
    }

    fun sendChatMessage(conversationId: String, messageText: String, receiverId: String = "seller_001") {
        viewModelScope.launch {
            val isSeller = _userRole.value == UserRole.SELLER
            val senderId = if (isSeller) _currentSellerId.value else _currentUserId.value
            val senderName = if (isSeller) "Aarav Sharma (Host)" else "Subhash Dev (Buyer)"

            val msg = ChatMessage(
                conversationId = conversationId,
                senderId = senderId,
                senderName = senderName,
                receiverId = receiverId,
                messageText = messageText,
                timestamp = System.currentTimeMillis()
            )
            repository.sendMessage(msg)
        }
    }

    fun approveListingAdmin(propertyId: String) {
        viewModelScope.launch {
            repository.updatePropertyStatus(propertyId, "APPROVED")
        }
    }

    fun rejectListingAdmin(propertyId: String) {
        viewModelScope.launch {
            repository.updatePropertyStatus(propertyId, "REJECTED")
        }
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
        }
    }

    fun askAiConcierge(query: String, currentProperty: Property? = null) {
        if (query.isBlank()) return
        _aiQueryState.value = AiQueryState.Loading
        viewModelScope.launch {
            val propertyInfo = if (currentProperty != null) {
                "Selected property: ${currentProperty.title} in ${currentProperty.city}, Price: $${currentProperty.pricePerNight}/night. Category: ${currentProperty.category.displayName}."
            } else ""

            val response = GeminiApiClient.queryRoomSaathiAi(query, propertyInfo)
            _aiQueryState.value = AiQueryState.Success(response)
        }
    }

    fun generateDescriptionWithAi(title: String, category: PropertyCategory, city: String, price: Double, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val prompt = "Write an enticing 3-sentence rental description for a property listing titled '$title', category ${category.displayName}, located in $city with price $$price/night."
            val desc = GeminiApiClient.queryRoomSaathiAi(prompt)
            onResult(desc)
        }
    }

    fun sendChatMessage(conversationId: String, text: String) {
        viewModelScope.launch {
            repository.sendMessage(
                ChatMessage(
                    conversationId = conversationId,
                    senderId = _currentUserId.value,
                    senderName = "Subhash Dev",
                    receiverId = "seller_001",
                    messageText = text
                )
            )
        }
    }
}
