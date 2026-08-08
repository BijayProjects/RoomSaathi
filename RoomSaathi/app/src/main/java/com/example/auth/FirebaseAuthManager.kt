package com.example.auth

import android.content.Context
import android.util.Log
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AuthResult {
    data class Success(val userProfile: UserProfile) : AuthResult
    data class Error(val message: String) : AuthResult
}

class FirebaseAuthManager(private val context: Context) {

    private var firebaseAuth: FirebaseAuth? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                firebaseAuth = FirebaseAuth.getInstance()
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Firebase Auth init error: ${e.message}")
        }
    }

    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth?.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth?.currentUser
    }

    fun signUpWithEmail(
        email: String,
        password: String,
        name: String,
        role: UserRole,
        onResult: (AuthResult) -> Unit
    ) {
        val auth = firebaseAuth
        if (auth != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fbUser = auth.currentUser
                        val userId = fbUser?.uid ?: "user_${System.currentTimeMillis()}"
                        val profile = UserProfile(
                            userId = userId,
                            name = name.ifBlank { email.substringBefore("@") },
                            email = email,
                            phone = "+977 9800000000",
                            role = role,
                            isKycVerified = true,
                            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
                        )
                        _currentUser.value = fbUser
                        onResult(AuthResult.Success(profile))
                    } else {
                        val err = task.exception?.localizedMessage ?: "Sign up failed"
                        onResult(AuthResult.Error(err))
                    }
                }
        } else {
            // Fallback / Mock Auth for offline or local preview
            val mockId = "user_fb_${email.hashCode()}"
            val profile = UserProfile(
                userId = mockId,
                name = name.ifBlank { email.substringBefore("@") },
                email = email,
                phone = "+977 9800000000",
                role = role,
                isKycVerified = true,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
            )
            onResult(AuthResult.Success(profile))
        }
    }

    fun signInWithEmail(
        email: String,
        password: String,
        targetRole: UserRole,
        onResult: (AuthResult) -> Unit
    ) {
        val auth = firebaseAuth
        if (auth != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fbUser = auth.currentUser
                        val userId = fbUser?.uid ?: "user_${System.currentTimeMillis()}"
                        val profile = UserProfile(
                            userId = userId,
                            name = fbUser?.displayName ?: email.substringBefore("@"),
                            email = email,
                            phone = "+977 9800000000",
                            role = targetRole,
                            isKycVerified = true,
                            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
                        )
                        _currentUser.value = fbUser
                        onResult(AuthResult.Success(profile))
                    } else {
                        val err = task.exception?.localizedMessage ?: "Sign in failed"
                        onResult(AuthResult.Error(err))
                    }
                }
        } else {
            // Local / Mock Sign In
            val mockId = "user_fb_${email.hashCode()}"
            val profile = UserProfile(
                userId = mockId,
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                phone = "+977 9800000000",
                role = targetRole,
                isKycVerified = true,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
            )
            onResult(AuthResult.Success(profile))
        }
    }

    fun signInWithGoogleToken(
        idToken: String,
        role: UserRole,
        onResult: (AuthResult) -> Unit
    ) {
        val auth = firebaseAuth
        if (auth != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fbUser = auth.currentUser
                        val userId = fbUser?.uid ?: "user_g_${System.currentTimeMillis()}"
                        val profile = UserProfile(
                            userId = userId,
                            name = fbUser?.displayName ?: "Google User",
                            email = fbUser?.email ?: "googleuser@roomsaathi.com",
                            phone = "+977 9800000000",
                            role = role,
                            isKycVerified = true,
                            avatarUrl = fbUser?.photoUrl?.toString()
                                ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
                        )
                        _currentUser.value = fbUser
                        onResult(AuthResult.Success(profile))
                    } else {
                        val err = task.exception?.localizedMessage ?: "Google Auth failed"
                        onResult(AuthResult.Error(err))
                    }
                }
        } else {
            // Mock Google Sign-In
            val profile = UserProfile(
                userId = "google_user_001",
                name = "Google Verified User",
                email = "user.google@roomsaathi.com",
                phone = "+977 9801122334",
                role = role,
                isKycVerified = true,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
            )
            onResult(AuthResult.Success(profile))
        }
    }

    fun signOut() {
        firebaseAuth?.signOut()
        _currentUser.value = null
    }
}
