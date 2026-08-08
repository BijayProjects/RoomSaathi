package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.UserProfile
import com.example.data.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    currentRole: UserRole,
    currentUserProfile: UserProfile?,
    isLoggedIn: Boolean,
    onRoleSelected: (UserRole) -> Unit,
    onOpenAuthClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onOpenWebPortalClick: () -> Unit = {},
    onCheckForUpdates: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var editName by remember(currentUserProfile) { mutableStateOf(currentUserProfile?.name ?: "Subhash Dev") }
    var editEmail by remember(currentUserProfile) { mutableStateOf(currentUserProfile?.email ?: "buyer@roomsaathi.com") }
    var editPhone by remember(currentUserProfile) { mutableStateOf(currentUserProfile?.phone ?: "+977 9801234567") }
    var editLocation by remember { mutableStateOf("Kathmandu, Nepal") }

    var isEditingProfile by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var isKycSubmitted by remember { mutableStateOf(true) }
    var citizenshipNumber by remember { mutableStateOf("12-01-78-04921") }
    var citizenshipFrontUploaded by remember { mutableStateOf(true) }
    var citizenshipBackUploaded by remember { mutableStateOf(true) }
    
    // Seller Specific Store Documentation Fields
    var storeName by remember { mutableStateOf("RoomSaathi Premier Store & Rentals") }
    var storePanVat by remember { mutableStateOf("609812345") }
    var storeDocUploaded by remember { mutableStateOf(true) }
    var storeRegistrationType by remember { mutableStateOf("Sole Proprietorship / Personal Store") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Account Profile & Settings", fontWeight = FontWeight.Bold) }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = currentUserProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80",
                            contentDescription = "Profile Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(currentUserProfile?.name ?: "Guest User", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            }
                            Text(currentUserProfile?.email ?: "Not logged in", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "Role: ${currentRole.name}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        if (isKycSubmitted) "KYC Verified" else "KYC Pending",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (isLoggedIn) {
                            IconButton(onClick = { isEditingProfile = !isEditingProfile }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            // Editable Account Information Details
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Personal Account Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            TextButton(onClick = { isEditingProfile = !isEditingProfile }) {
                                Text(if (isEditingProfile) "Cancel" else "Edit Details", fontSize = 12.sp)
                            }
                        }

                        if (isEditingProfile) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editEmail,
                                onValueChange = { editEmail = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                label = { Text("Phone Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editLocation,
                                onValueChange = { editLocation = it },
                                label = { Text("Location City") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    isEditingProfile = false
                                    Toast.makeText(context, "Account details saved successfully!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save Profile Changes", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Full Name", fontSize = 11.sp, color = Color.Gray)
                                    Text(editName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                            HorizontalDivider()
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Email Address", fontSize = 11.sp, color = Color.Gray)
                                    Text(editEmail, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                            HorizontalDivider()
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Phone Number", fontSize = 11.sp, color = Color.Gray)
                                    Text(editPhone, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                            HorizontalDivider()
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("City / Location", fontSize = 11.sp, color = Color.Gray)
                                    Text(editLocation, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            // KYC & Identity Verification Management
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text("KYC & Identity Verification", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Surface(
                                color = if (isKycSubmitted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    if (isKycSubmitted) "Verified Account" else "Verification Pending",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isKycSubmitted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            "Complete identity & documentation verification to list properties, make secure transactions, and earn verified badges.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        HorizontalDivider()

                        // 1. Email Verification Section
                        Text("1. Email Identity Verification", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editEmail,
                                onValueChange = { editEmail = it },
                                label = { Text("Verified Email Address") },
                                leadingIcon = { Icon(Icons.Default.MarkEmailRead, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Button(
                                onClick = { Toast.makeText(context, "Email verification link sent to $editEmail", Toast.LENGTH_SHORT).show() },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp)
                            ) {
                                Text("Verify", fontSize = 11.sp)
                            }
                        }

                        HorizontalDivider()

                        // 2. Personal Citizenship Card Photo Upload (Required for All Users)
                        Text("2. Personal Citizenship Card (Front & Back)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        
                        OutlinedTextField(
                            value = citizenshipNumber,
                            onValueChange = { citizenshipNumber = it },
                            label = { Text("Citizenship / Govt ID Number") },
                            leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Citizenship Front
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        if (citizenshipFrontUploaded) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = if (citizenshipFrontUploaded) MaterialTheme.colorScheme.primary else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text("Citizenship Front Photo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        if (citizenshipFrontUploaded) "Front_ID_Card.jpg Uploaded" else "Tap to upload front",
                                        fontSize = 10.sp,
                                        color = if (citizenshipFrontUploaded) MaterialTheme.colorScheme.primary else Color.Gray
                                    )
                                    OutlinedButton(
                                        onClick = {
                                            citizenshipFrontUploaded = true
                                            Toast.makeText(context, "Citizenship Front photo selected!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(if (citizenshipFrontUploaded) "Replace Front" else "Upload Front", fontSize = 10.sp)
                                    }
                                }
                            }

                            // Citizenship Back
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        if (citizenshipBackUploaded) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = if (citizenshipBackUploaded) MaterialTheme.colorScheme.primary else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text("Citizenship Back Photo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        if (citizenshipBackUploaded) "Back_ID_Card.jpg Uploaded" else "Tap to upload back",
                                        fontSize = 10.sp,
                                        color = if (citizenshipBackUploaded) MaterialTheme.colorScheme.primary else Color.Gray
                                    )
                                    OutlinedButton(
                                        onClick = {
                                            citizenshipBackUploaded = true
                                            Toast.makeText(context, "Citizenship Back photo selected!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(if (citizenshipBackUploaded) "Replace Back" else "Upload Back", fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        // 3. Store & Business Documentation (Exclusively for SELLER / Host / Business Accounts)
                        HorizontalDivider()
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                            Text("3. Store & Sale Business Documentation", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.tertiary)
                        }

                        if (currentRole == UserRole.SELLER || currentRole == UserRole.ADMIN) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                                    Text(
                                        "Required for Seller / Store accounts: Submit official business registration and PAN/VAT docs to list commercial rooms & sales properties.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = storeName,
                                onValueChange = { storeName = it },
                                label = { Text("Store / Agency / Business Name") },
                                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = storePanVat,
                                onValueChange = { storePanVat = it },
                                label = { Text("PAN / VAT Registration Number") },
                                leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = storeRegistrationType,
                                onValueChange = { storeRegistrationType = it },
                                label = { Text("Store / Property Business Type") },
                                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Icon(
                                            if (storeDocUploaded) Icons.Default.Description else Icons.Default.FileUpload,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.tertiary
                                        )
                                        Column {
                                            Text("Store Business License / PAN Doc", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Text(
                                                if (storeDocUploaded) "Store_Registration_Certificate.pdf (Attached)" else "Upload PAN or Registration Document",
                                                fontSize = 10.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            storeDocUploaded = true
                                            Toast.makeText(context, "Store registration document attached!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(if (storeDocUploaded) "Re-upload" else "Attach", fontSize = 10.sp)
                                    }
                                }
                            }
                        } else {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    Text(
                                        "Store & Sale documentation is only required for Sellers. Switch to 'Seller Mode' below to upload store licenses.",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                isKycSubmitted = true
                                Toast.makeText(context, "KYC & Store Documentation submitted for verification!", Toast.LENGTH_LONG).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submit KYC & Store Verification Docs", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // User Role Switcher
            item {
                Text("Account Mode / Perspective", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))

                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        listOf(
                            UserRole.BUYER to "Buyer / Renter Mode (Search & Book Rooms)",
                            UserRole.SELLER to "Seller / Host Mode (List & Manage Properties)",
                            UserRole.ADMIN to "Super Admin Portal (Governance & Approvals)"
                        ).forEach { (role, desc) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentRole == role,
                                    onClick = { onRoleSelected(role) }
                                )
                                Text(desc, fontSize = 12.sp, fontWeight = if (currentRole == role) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }

            // Security Settings
            item {
                Text("Enterprise Security & Password", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))

                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Password & Credentials", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Update Firebase security credentials", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            TextButton(onClick = { showPasswordDialog = true }) {
                                Text("Change Password", fontSize = 12.sp)
                            }
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Biometric Authentication", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Fingerprint / Face ID login", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Switch(checked = biometricEnabled, onCheckedChange = { biometricEnabled = it })
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text("Push Notifications", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Booking confirmations & chat alerts", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                        }
                    }
                }
            }

            // Exit Account Option inside Account Settings
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Account Session Management", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.error)
                        Text("Sign out or exit the current session securely.", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isLoggedIn) {
                            Button(
                                onClick = onSignOutClick,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Text("Exit / Sign Out Account", fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Button(
                                onClick = onOpenAuthClick,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Text("Sign In / Register Account", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Password Change Dialog Modal
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentPassword.isNotBlank() && newPassword.isNotBlank()) {
                            showPasswordDialog = false
                            currentPassword = ""
                            newPassword = ""
                            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Please fill both password fields", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

