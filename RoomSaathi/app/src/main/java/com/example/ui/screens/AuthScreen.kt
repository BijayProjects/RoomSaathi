package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.components.AnimatedAppLogo

enum class AuthTab {
    SIGN_IN,
    SIGN_UP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    currentRole: UserRole,
    onAuthSuccess: (UserProfile) -> Unit,
    onBackClick: () -> Unit,
    onSignUpWithEmail: (String, String, String, UserRole, (String?) -> Unit) -> Unit,
    onSignInWithEmail: (String, String, UserRole, (String?) -> Unit) -> Unit,
    onGoogleSignIn: (UserRole, (String?) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(AuthTab.SIGN_IN) }

    var email by remember { mutableStateOf("buyer@roomsaathi.com") }
    var password by remember { mutableStateOf("password123") }
    var fullName by remember { mutableStateOf("Subhash Dev") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Helper function to auto-detect role from email input
    fun autoDetectRoleFromEmail(inputEmail: String): UserRole {
        val lower = inputEmail.lowercase().trim()
        return when {
            lower.contains("seller") || lower.contains("host") || lower.contains("vendor") || lower.contains("store") || lower.contains("agency") -> UserRole.SELLER
            lower.contains("admin") || lower.contains("super") -> UserRole.ADMIN
            else -> UserRole.BUYER
        }
    }

    var selectedRole by remember(email) { mutableStateOf(autoDetectRoleFromEmail(email)) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Authentication & Identity Portal", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner & App Identity
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedAppLogo(
                        size = 72.dp,
                        showLoadingProgress = true,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        accentColor = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Access Control Portal",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "Sign in to securely access Buyer, Seller, or Admin Dashboards",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Tab Selector (Sign In vs Sign Up)
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Tab(
                        selected = selectedTab == AuthTab.SIGN_IN,
                        onClick = {
                            selectedTab = AuthTab.SIGN_IN
                            errorMessage = null
                        },
                        text = { Text("Sign In", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedTab == AuthTab.SIGN_UP,
                        onClick = {
                            selectedTab = AuthTab.SIGN_UP
                            errorMessage = null
                        },
                        text = { Text("Create Account", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            // Error alert banner
            if (errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Text(errorMessage!!, fontSize = 12.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Full Name (Only for Sign Up)
            if (selectedTab == AuthTab.SIGN_UP) {
                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_input")
                    )
                }
            }

            // Email Field with Auto-Detection
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { newEmail ->
                        email = newEmail
                        selectedRole = autoDetectRoleFromEmail(newEmail)
                    },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input")
                )
            }

            // Auto-Detection Status Indicator & Role Selection
            item {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                when (selectedRole) {
                                    UserRole.BUYER -> Icons.Default.Person
                                    UserRole.SELLER -> Icons.Default.Store
                                    UserRole.ADMIN -> Icons.Default.Shield
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    "Auto-Detected Access Role: ${selectedRole.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    if (selectedRole == UserRole.SELLER) "Authorized as Seller / Host for listing properties & stores"
                                    else if (selectedRole == UserRole.ADMIN) "Authorized as Admin with full moderation privileges"
                                    else "Authorized as Buyer / Renter for booking rooms",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Text("Override or Verify Account Role:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserRole.entries.forEach { role ->
                            val isSelected = selectedRole == role
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedRole = role },
                                label = { Text(role.name, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                leadingIcon = {
                                    Icon(
                                        when(role) {
                                            UserRole.BUYER -> Icons.Default.Person
                                            UserRole.SELLER -> Icons.Default.Store
                                            UserRole.ADMIN -> Icons.Default.Shield
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Password Field
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password"
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input")
                )
            }

            // Submit Button (Sign In / Sign Up)
            item {
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter email and password."
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null

                        if (selectedTab == AuthTab.SIGN_IN) {
                            onSignInWithEmail(email, password, selectedRole) { err ->
                                isLoading = false
                                if (err != null) {
                                    errorMessage = err
                                } else {
                                    Toast.makeText(context, "Logged in as ${selectedRole.name}!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            onSignUpWithEmail(email, password, fullName, selectedRole) { err ->
                                isLoading = false
                                if (err != null) {
                                    errorMessage = err
                                } else {
                                    Toast.makeText(context, "Account Created as ${selectedRole.name}!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_submit_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
                    } else {
                        Text(
                            text = if (selectedTab == AuthTab.SIGN_IN) "Sign In as ${selectedRole.name}" else "Register as ${selectedRole.name}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Divider OR
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text("  OR  ", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }

            // Google Sign-In Button
            item {
                OutlinedButton(
                    onClick = {
                        isLoading = true
                        onGoogleSignIn(selectedRole) { err ->
                            isLoading = false
                            if (err != null) {
                                errorMessage = err
                            } else {
                                Toast.makeText(context, "Google Auth Successful as ${selectedRole.name}!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("google_auth_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Google", tint = MaterialTheme.colorScheme.primary)
                        Text("Sign In with Google Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            // Quick Demo Accounts Shortcuts
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Quick Demo Accounts Switcher:", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    email = "buyer.subhash@roomsaathi.com"
                                    password = "password123"
                                    selectedRole = UserRole.BUYER
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Buyer Demo", fontSize = 10.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    email = "seller.aarav@roomsaathi.com"
                                    password = "password123"
                                    selectedRole = UserRole.SELLER
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Seller Demo", fontSize = 10.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    email = "admin.super@roomsaathi.com"
                                    password = "password123"
                                    selectedRole = UserRole.ADMIN
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Admin Demo", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
