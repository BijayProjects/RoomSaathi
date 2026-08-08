package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.model.UserRole
import com.example.ui.components.RoleHeaderBar
import com.example.ui.screens.*
import com.example.ui.theme.RoomSaathiTheme
import com.example.ui.viewmodel.RoomSaathiViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomSaathiTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: RoomSaathiViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentUserProfile by viewModel.currentUserProfile.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val properties by viewModel.properties.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val myBookings by viewModel.myBookings.collectAsState()
    val sellerProperties by viewModel.sellerProperties.collectAsState()
    val sellerBookings by viewModel.sellerBookings.collectAsState()
    val adminProperties by viewModel.adminProperties.collectAsState()
    val aiQueryState by viewModel.aiQueryState.collectAsState()
    val appVersionInfo by viewModel.appVersionInfo.collectAsState()
    val showUpdateDialog by viewModel.showUpdateDialog.collectAsState()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "splash"

    val showBottomBar = currentRoute in listOf("home", "wishlist", "bookings", "seller_dashboard", "admin_dashboard", "profile")
    val showTopBar = currentRoute != "splash"

    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissUpdateDialog() },
            title = {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RoomSaathi Update Available!", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("A new version (v${appVersionInfo.latestVersion}) of RoomSaathi is available.")
                    Text("• File Size: ${appVersionInfo.fileSize}")
                    Text("• Release Notes: ${appVersionInfo.releaseNotes}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissUpdateDialog()
                        navController.navigate("web_portal")
                    }
                ) {
                    Text("Download APK")
                }
            },
            dismissButton = {
                if (!appVersionInfo.isMandatory) {
                    OutlinedButton(onClick = { viewModel.dismissUpdateDialog() }) {
                        Text("Later")
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                RoleHeaderBar(
                    currentRole = userRole,
                    currentUserProfile = currentUserProfile,
                    isLoggedIn = isLoggedIn,
                    onRoleSelected = { role ->
                        viewModel.setUserRole(role)
                        when (role) {
                            UserRole.BUYER -> navController.navigate("home") { popUpTo(0) }
                            UserRole.SELLER -> navController.navigate("seller_dashboard") { popUpTo(0) }
                            UserRole.ADMIN -> navController.navigate("admin_dashboard") { popUpTo(0) }
                        }
                    },
                    onOpenAiClick = { navController.navigate("ai_assistant") },
                    onOpenAuthClick = { navController.navigate("auth") },
                    onSignOutClick = { viewModel.signOutUser() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_bottom_navigation")
                ) {
                    if (userRole == UserRole.BUYER) {
                        NavigationBarItem(
                            selected = currentRoute == "home",
                            onClick = { navController.navigate("home") },
                            icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                            label = { Text("Explore", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "wishlist",
                            onClick = { navController.navigate("wishlist") },
                            icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                            label = { Text("Saved", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "bookings",
                            onClick = { navController.navigate("bookings") },
                            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Bookings") },
                            label = { Text("Bookings", fontSize = 11.sp) }
                        )
                    } else if (userRole == UserRole.SELLER) {
                        NavigationBarItem(
                            selected = currentRoute == "seller_dashboard",
                            onClick = { navController.navigate("seller_dashboard") },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                            label = { Text("Seller Hub", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "add_property",
                            onClick = { navController.navigate("add_property") },
                            icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Add Property") },
                            label = { Text("List Room", fontSize = 11.sp) }
                        )
                    } else if (userRole == UserRole.ADMIN) {
                        NavigationBarItem(
                            selected = currentRoute == "admin_dashboard",
                            onClick = { navController.navigate("admin_dashboard") },
                            icon = { Icon(Icons.Default.Shield, contentDescription = "Admin") },
                            label = { Text("Admin Hub", fontSize = 11.sp) }
                        )
                    }

                    NavigationBarItem(
                        selected = currentRoute == "chat/conv_seller_001_buyer_001",
                        onClick = { navController.navigate("chat/conv_seller_001_buyer_001") },
                        icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                        label = { Text("Chat", fontSize = 11.sp) }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = { navController.navigate("profile") },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 11.sp) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                BuyerHomeScreen(
                    properties = properties,
                    favorites = favorites,
                    selectedCategory = selectedCategory,
                    searchQuery = searchQuery,
                    onCategorySelected = { viewModel.setSelectedCategory(it) },
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onOpenFilterClick = { navController.navigate("filter") },
                    onPropertyClick = { prop ->
                        viewModel.selectProperty(prop)
                        navController.navigate("detail/${prop.id}")
                    },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onOpenAiClick = { navController.navigate("ai_assistant") }
                )
            }

            composable("filter") {
                SearchFilterScreen(
                    currentMaxPrice = viewModel.maxPriceFilter.collectAsState().value,
                    currentCategory = selectedCategory,
                    isInstantOnly = viewModel.instantBookingOnly.collectAsState().value,
                    isVerifiedOnly = viewModel.verifiedOnly.collectAsState().value,
                    onApplyFilters = { maxP, cat, instant, verified ->
                        viewModel.setMaxPriceFilter(maxP)
                        viewModel.setSelectedCategory(cat)
                        viewModel.setInstantBookingOnly(instant)
                        viewModel.setVerifiedOnly(verified)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = "detail/{propertyId}",
                arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
            ) { backStackEntry ->
                val propId = backStackEntry.arguments?.getString("propertyId") ?: ""
                val property = properties.find { it.id == propId } ?: adminProperties.find { it.id == propId }

                if (property != null) {
                    val isFav = favorites.any { it.propertyId == property.id }
                    PropertyDetailScreen(
                        property = property,
                        isFavorite = isFav,
                        onBackClick = { navController.popBackStack() },
                        onToggleFav = { viewModel.toggleFavorite(property.id) },
                        onBookNowClick = { navController.navigate("checkout/${property.id}") },
                        onChatHostClick = { navController.navigate("chat/conv_seller_001_buyer_001") },
                        onAskAiClick = { prompt ->
                            viewModel.askAiConcierge(prompt, property)
                            navController.navigate("ai_assistant")
                        }
                    )
                }
            }

            composable(
                route = "checkout/{propertyId}",
                arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
            ) { backStackEntry ->
                val propId = backStackEntry.arguments?.getString("propertyId") ?: ""
                val property = properties.find { it.id == propId } ?: adminProperties.find { it.id == propId }

                if (property != null) {
                    BookingCheckoutScreen(
                        property = property,
                        onBackClick = { navController.popBackStack() },
                        onConfirmBooking = { prop, inDate, outDate, nights, guests, total, payMethod, gName, gPhone, requests, callback ->
                            viewModel.createBooking(prop, inDate, outDate, nights, guests, total, payMethod, gName, gPhone, requests, callback)
                        },
                        onViewMyBookingsClick = { navController.navigate("bookings") { popUpTo("home") } }
                    )
                }
            }

            composable("bookings") {
                MyBookingsScreen(
                    bookings = myBookings,
                    onContactHostClick = { navController.navigate("chat/conv_seller_001_buyer_001") }
                )
            }

            composable("wishlist") {
                WishlistScreen(
                    allProperties = properties,
                    favorites = favorites,
                    onPropertyClick = { prop ->
                        viewModel.selectProperty(prop)
                        navController.navigate("detail/${prop.id}")
                    },
                    onToggleFav = { viewModel.toggleFavorite(it) }
                )
            }

            composable("seller_dashboard") {
                SellerDashboardScreen(
                    sellerProperties = sellerProperties,
                    sellerBookings = sellerBookings,
                    onAddPropertyClick = { navController.navigate("add_property") },
                    onApproveBooking = { viewModel.updateBookingStatus(it, "CONFIRMED") },
                    onRejectBooking = { viewModel.updateBookingStatus(it, "DECLINED") },
                    onUpdateProperty = { viewModel.updatePropertyBySeller(it) {} },
                    onDeleteProperty = { viewModel.deletePropertyBySeller(it) },
                    onChatWithBuyer = { buyerId, sellerId ->
                        navController.navigate("chat/conv_${sellerId}_${buyerId}")
                    }
                )
            }

            composable("add_property") {
                AddPropertyScreen(
                    onBackClick = { navController.popBackStack() },
                    onGenerateAiDescription = { title, category, city, price, onResult ->
                        viewModel.generateDescriptionWithAi(title, category, city, price, onResult)
                    },
                    onSubmitProperty = { title, desc, cat, price, addr, city, prov, amen, imgs ->
                        viewModel.addPropertyBySeller(title, desc, cat, price, addr, city, prov, amen, imgs) {
                            navController.navigate("seller_dashboard") { popUpTo(0) }
                        }
                    }
                )
            }

            composable("admin_dashboard") {
                AdminDashboardScreen(
                    adminProperties = adminProperties,
                    appVersionInfo = appVersionInfo,
                    onApproveProperty = { viewModel.approveListingAdmin(it) },
                    onRejectProperty = { viewModel.rejectListingAdmin(it) },
                    onPublishApkRelease = { v, url, size, notes, mandatory ->
                        viewModel.publishApkRelease(v, url, size, notes, mandatory)
                    }
                )
            }

            composable("web_portal") {
                WebPortalScreen(
                    appVersionInfo = appVersionInfo,
                    onDownloadApkClick = {
                        viewModel.checkForApkUpdates()
                    }
                )
            }

            composable("ai_assistant") {
                val currentProperty = viewModel.selectedProperty.collectAsState().value
                AiAssistantScreen(
                    aiState = aiQueryState,
                    onSendQuery = { query -> viewModel.askAiConcierge(query, currentProperty) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = "chat/{conversationId}",
                arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val convId = backStackEntry.arguments?.getString("conversationId") ?: "conv_seller_001_buyer_001"
                val chatMessages by viewModel.repository.getMessagesForConversation(convId).collectAsState(initial = emptyList())

                ChatScreen(
                    conversationId = convId,
                    currentUserId = viewModel.currentUserId.collectAsState().value,
                    messages = chatMessages,
                    onSendMessage = { text -> viewModel.sendChatMessage(convId, text) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("profile") {
                ProfileSettingsScreen(
                    currentRole = userRole,
                    currentUserProfile = currentUserProfile,
                    isLoggedIn = isLoggedIn,
                    onRoleSelected = { viewModel.setUserRole(it) },
                    onOpenAuthClick = { navController.navigate("auth") },
                    onSignOutClick = { viewModel.signOutUser() },
                    onOpenWebPortalClick = { navController.navigate("web_portal") },
                    onCheckForUpdates = { viewModel.checkForApkUpdates() }
                )
            }

            composable("auth") {
                AuthScreen(
                    currentRole = userRole,
                    onAuthSuccess = { profile ->
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() },
                    onSignUpWithEmail = { email, pass, name, role, callback ->
                        viewModel.signUpWithEmail(email, pass, name, role) { err ->
                            if (err == null) {
                                navController.popBackStack()
                            }
                            callback(err)
                        }
                    },
                    onSignInWithEmail = { email, pass, role, callback ->
                        viewModel.signInWithEmail(email, pass, role) { err ->
                            if (err == null) {
                                navController.popBackStack()
                            }
                            callback(err)
                        }
                    },
                    onGoogleSignIn = { role, callback ->
                        viewModel.signInWithGoogle(role) { err ->
                            if (err == null) {
                                navController.popBackStack()
                            }
                            callback(err)
                        }
                    }
                )
            }
        }
    }
}
