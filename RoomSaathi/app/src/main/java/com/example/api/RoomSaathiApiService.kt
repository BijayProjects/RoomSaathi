package com.example.api

import com.example.data.model.AppVersionInfo
import com.example.data.model.Booking
import com.example.data.model.Property
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

object RoomSaathiApiService {

    private var currentReleaseInfo = AppVersionInfo(
        appName = "RoomSaathi",
        currentVersion = "1.0.0",
        latestVersion = "1.1.0",
        downloadUrl = "https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk",
        fileSize = "28 MB",
        releaseDate = "August 2026",
        isUpdateAvailable = true,
        isMandatory = false,
        releaseNotes = "Added Web Version synchronization, real-time booking push updates, faster room search filters, and PWA web platform access."
    )

    /**
     * Endpoint: GET /api/v1/app/latest-version
     */
    suspend fun getLatestAppVersion(): AppVersionInfo = withContext(Dispatchers.IO) {
        return@withContext currentReleaseInfo
    }

    /**
     * Endpoint: POST /api/v1/app/release
     * Used by Admin panel to update current published APK release details.
     */
    suspend fun publishNewApkRelease(
        version: String,
        downloadUrl: String,
        fileSize: String,
        releaseNotes: String,
        isMandatory: Boolean
    ): AppVersionInfo = withContext(Dispatchers.IO) {
        val updated = currentReleaseInfo.copy(
            latestVersion = version,
            downloadUrl = downloadUrl,
            fileSize = fileSize,
            releaseNotes = releaseNotes,
            isMandatory = isMandatory,
            isUpdateAvailable = true,
            releaseDate = "August 2026"
        )
        currentReleaseInfo = updated
        return@withContext updated
    }

    /**
     * Endpoint: POST /api/v1/auth/login
     */
    suspend fun authenticateUser(email: String, role: UserRole): UserProfile = withContext(Dispatchers.IO) {
        val cleanName = email.substringBefore("@").replace(".", " ").capitalize()
        return@withContext UserProfile(
            userId = "usr_${email.hashCode()}",
            name = cleanName,
            email = email,
            phone = "+977 9801234567",
            role = role,
            isKycVerified = true,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80"
        )
    }

    /**
     * Formats AppVersionInfo into JSON string for Web API consumption
     */
    fun formatVersionJsonResponse(versionInfo: AppVersionInfo): String {
        return JSONObject().apply {
            put("app_name", versionInfo.appName)
            put("current_version", versionInfo.currentVersion)
            put("latest_version", versionInfo.latestVersion)
            put("update_available", versionInfo.isUpdateAvailable)
            put("mandatory", versionInfo.isMandatory)
            put("download_url", versionInfo.downloadUrl)
            put("file_size", versionInfo.fileSize)
            put("release_date", versionInfo.releaseDate)
            put("release_notes", versionInfo.releaseNotes)
        }.toString(2)
    }
}
