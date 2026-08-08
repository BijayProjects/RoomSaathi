package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Booking
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import kotlin.math.roundToInt

enum class SellerTab {
    MY_PROPERTIES,
    ORDERED_BOOKINGS,
    ANALYTICS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    sellerProperties: List<Property>,
    sellerBookings: List<Booking>,
    onAddPropertyClick: () -> Unit,
    onApproveBooking: (String) -> Unit,
    onRejectBooking: (String) -> Unit,
    onUpdateProperty: (Property) -> Unit,
    onDeleteProperty: (Property) -> Unit,
    onChatWithBuyer: (String, String) -> Unit, // buyerId, sellerId
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(SellerTab.MY_PROPERTIES) }
    var propertyToEdit by remember { mutableStateOf<Property?>(null) }
    var propertyToDelete by remember { mutableStateOf<Property?>(null) }
    var showAlertMessage by remember { mutableStateOf<String?>(null) }

    val totalRevenue = sellerBookings.sumOf { it.totalPrice }

    // Alert Message Banner
    if (showAlertMessage != null) {
        AlertDialog(
            onDismissRequest = { showAlertMessage = null },
            confirmButton = {
                Button(onClick = { showAlertMessage = null }) {
                    Text("OK")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Seller Notification", fontWeight = FontWeight.Bold)
                }
            },
            text = { Text(showAlertMessage!!) }
        )
    }

    // Delete Confirmation Dialog
    if (propertyToDelete != null) {
        AlertDialog(
            onDismissRequest = { propertyToDelete = null },
            confirmButton = {
                Button(
                    onClick = {
                        val prop = propertyToDelete!!
                        propertyToDelete = null
                        onDeleteProperty(prop)
                        showAlertMessage = "Listing '${prop.title}' was successfully deleted."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { propertyToDelete = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Property Listing?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently remove '${propertyToDelete?.title}' from your active marketplace listings?") }
        )
    }

    // Edit Property Dialog Modal
    if (propertyToEdit != null) {
        EditPropertyDialog(
            property = propertyToEdit!!,
            onDismiss = { propertyToEdit = null },
            onSave = { updatedProp ->
                propertyToEdit = null
                onUpdateProperty(updatedProp)
                showAlertMessage = "Property '${updatedProp.title}' details updated successfully!"
                Toast.makeText(context, "Property Updated Successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Seller Dashboard", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text("Manage listings, edit rooms & guest orders", fontSize = 12.sp, color = Color.Gray)
                }

                Button(
                    onClick = onAddPropertyClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_property_fab")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Add Property", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Dashboard Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard("Total Revenue", "$${totalRevenue.toInt()}", Icons.Default.Payments, MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                MetricCard("Active Listings", "${sellerProperties.size}", Icons.Default.HomeWork, MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                MetricCard("Orders / Bookings", "${sellerBookings.size}", Icons.Default.ReceiptLong, Color(0xFF00C853), modifier = Modifier.weight(1f))
            }
        }

        // Navigation Tabs Menu (My Listings / Ordered & Edit / Performance)
        item {
            PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
                Tab(
                    selected = selectedTab == SellerTab.MY_PROPERTIES,
                    onClick = { selectedTab = SellerTab.MY_PROPERTIES },
                    text = { Text("My Listings (${sellerProperties.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == SellerTab.ORDERED_BOOKINGS,
                    onClick = { selectedTab = SellerTab.ORDERED_BOOKINGS },
                    text = { Text("Orders / Bookings (${sellerBookings.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }

        // TAB 1: MY PROPERTIES LISTINGS WITH SWIPE-TO-DELETE & EDIT
        if (selectedTab == SellerTab.MY_PROPERTIES) {
            item {
                Text(
                    text = "Tip: Swipe left on a property card to delete, or tap Edit button to update.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            if (sellerProperties.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.HomeWork, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No properties listed yet.", fontWeight = FontWeight.Bold)
                            Text("Click 'Add Property' above to create your first listing.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            } else {
                items(sellerProperties, key = { it.id }) { prop ->
                    SwipeToDeletePropertyCard(
                        property = prop,
                        onEditClick = { propertyToEdit = prop },
                        onDeleteClick = { propertyToDelete = prop }
                    )
                }
            }
        }

        // TAB 2: ORDERED BOOKINGS FORM REQUESTS
        if (selectedTab == SellerTab.ORDERED_BOOKINGS) {
            item {
                Text("Received Buyer Booking Requests", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                if (sellerBookings.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No guest orders or bookings received yet.", fontWeight = FontWeight.Bold)
                            Text("When buyers submit booking forms for your properties, they will appear here.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            items(sellerBookings, key = { it.id }) { booking ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = booking.propertyImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Column {
                                    Text(booking.propertyTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Guest: ${booking.buyerName} • ${booking.guestCount} guests", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                    Text("Check-In: ${booking.checkInDate} — ${booking.checkOutDate}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Text("$${booking.totalPrice.toInt()}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = when(booking.status) {
                                    "CONFIRMED" -> Color(0xFFE8F5E9)
                                    "DECLINED" -> Color(0xFFFFEBEE)
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Status: ${booking.status}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when(booking.status) {
                                        "CONFIRMED" -> Color(0xFF2E7D32)
                                        "DECLINED" -> Color(0xFFC62828)
                                        else -> MaterialTheme.colorScheme.primary
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Private Message Buyer
                                OutlinedButton(
                                    onClick = { onChatWithBuyer(booking.buyerId, booking.sellerId) },
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Chat Buyer", fontSize = 11.sp)
                                }

                                if (booking.status != "DECLINED") {
                                    OutlinedButton(
                                        onClick = {
                                            onRejectBooking(booking.id)
                                            showAlertMessage = "Booking request declined."
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Decline", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                                    }
                                }

                                if (booking.status != "CONFIRMED") {
                                    Button(
                                        onClick = {
                                            onApproveBooking(booking.id)
                                            showAlertMessage = "Booking confirmed for ${booking.buyerName}!"
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Accept Guest", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Interactive Swipe To Delete Card Component (Swiping > 50% triggers delete or reveals Delete button)
@Composable
private fun SwipeToDeletePropertyCard(
    property: Property,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFD32F2F)) // Red Delete background revealed on swipe
    ) {
        // Red Delete background content
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(28.dp))
                Text("Delete", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        // Foreground Property Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        // Only allow left drag (swiping left up to -300px)
                        offsetX = (offsetX + delta).coerceIn(-300f, 0f)
                    },
                    onDragStopped = {
                        // If swiped more than 50% left threshold (-150f), trigger delete confirmation
                        if (offsetX < -150f) {
                            onDeleteClick()
                        }
                        offsetX = 0f
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = property.imageUrls.firstOrNull() ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column {
                        Text(property.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${property.category.displayName} • ${property.city}", fontSize = 11.sp, color = Color.Gray)
                        Text("$${property.pricePerNight.toInt()} / night", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Edit Button
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Property", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }

                    // Delete Button
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Property", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

// Edit Property Dialog Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPropertyDialog(
    property: Property,
    onDismiss: () -> Unit,
    onSave: (Property) -> Unit
) {
    var title by remember { mutableStateOf(property.title) }
    var description by remember { mutableStateOf(property.description) }
    var priceText by remember { mutableStateOf(property.pricePerNight.toInt().toString()) }
    var city by remember { mutableStateOf(property.city) }
    var address by remember { mutableStateOf(property.locationAddress) }
    var imageUrl by remember { mutableStateOf(property.imageUrls.firstOrNull() ?: "") }
    var category by remember { mutableStateOf(property.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toDoubleOrNull() ?: property.pricePerNight
                    val updated = property.copy(
                        title = title,
                        description = description,
                        pricePerNight = price,
                        city = city,
                        locationAddress = address,
                        imageUrls = if (imageUrl.isNotBlank()) listOf(imageUrl) else property.imageUrls,
                        category = category
                    )
                    onSave(updated)
                }
            ) {
                Text("Save Updates")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Property Listing", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Property Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price per Night ($)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Street Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Cover Image URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
            Text(title, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
