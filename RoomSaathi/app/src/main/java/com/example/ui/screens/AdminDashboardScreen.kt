package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Property

import androidx.compose.runtime.*
import com.example.data.model.AppVersionInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminProperties: List<Property>,
    appVersionInfo: AppVersionInfo = AppVersionInfo(),
    onApproveProperty: (String) -> Unit,
    onRejectProperty: (String) -> Unit,
    onPublishApkRelease: (String, String, String, String, Boolean) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var versionText by remember(appVersionInfo) { mutableStateOf(appVersionInfo.latestVersion) }
    var apkUrlText by remember(appVersionInfo) { mutableStateOf(appVersionInfo.downloadUrl) }
    var fileSizeText by remember(appVersionInfo) { mutableStateOf(appVersionInfo.fileSize) }
    var releaseNotesText by remember(appVersionInfo) { mutableStateOf(appVersionInfo.releaseNotes) }
    var isMandatory by remember(appVersionInfo) { mutableStateOf(appVersionInfo.isMandatory) }
    var showSuccessBanner by remember { mutableStateOf(false) }

    val pendingProperties = adminProperties.filter { it.verificationStatus == "PENDING" || it.verificationStatus == "APPROVED" }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Super Admin Portal", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Text("Platform governance, APK release updates & listing moderation", fontSize = 12.sp, color = Color.Gray)
            }
        }

        // Platform Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard("Platform GMV", "$28,450", Icons.Default.MonetizationOn, MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                AdminStatCard("Total Users", "12,890", Icons.Default.Group, MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                AdminStatCard("Live Listings", "${adminProperties.size}", Icons.Default.Apartment, Color(0xFF00C853), modifier = Modifier.weight(1f))
            }
        }

        // APK Version & Release Management Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text("RoomSaathi APK Release Management", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Current v${appVersionInfo.latestVersion}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = versionText,
                        onValueChange = { versionText = it },
                        label = { Text("Published Version (e.g., 1.1.0)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = apkUrlText,
                        onValueChange = { apkUrlText = it },
                        label = { Text("APK Download Storage URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = fileSizeText,
                            onValueChange = { fileSizeText = it },
                            label = { Text("File Size") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f).padding(top = 8.dp)
                        ) {
                            Checkbox(checked = isMandatory, onCheckedChange = { isMandatory = it })
                            Text("Mandatory Update", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = releaseNotesText,
                        onValueChange = { releaseNotesText = it },
                        label = { Text("Release Notes / Changelog") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onPublishApkRelease(versionText, apkUrlText, fileSizeText, releaseNotesText, isMandatory)
                            showSuccessBanner = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish New APK Release to Web & App", fontWeight = FontWeight.Bold)
                    }

                    if (showSuccessBanner) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✅ APK Release v$versionText published successfully! Web platform and Android users are notified.",
                            color = Color(0xFF00C853),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // AI Security & Fraud Detection Log Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Security, contentDescription = "Security", tint = MaterialTheme.colorScheme.primary)
                        Text("AI Fraud & Threat Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Zero duplicate listings detected in past 24 hrs", fontSize = 12.sp)
                    Text("• KYC Government ID Verification: 99.4% Pass Rate", fontSize = 12.sp)
                    Text("• Automated Image Quality Scan: All clear", fontSize = 12.sp)
                }
            }
        }

        // Property Moderation Queue
        item {
            Text("Property Listing Moderation Queue", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        items(adminProperties) { prop ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(prop.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Seller: ${prop.sellerName} • ${prop.city}", fontSize = 12.sp, color = Color.Gray)
                            Text("$${prop.pricePerNight.toInt()} / night", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        }

                        Surface(
                            color = if (prop.verificationStatus == "APPROVED") Color(0xFF00C853).copy(alpha = 0.2f) else MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = prop.verificationStatus,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (prop.verificationStatus == "APPROVED") Color(0xFF00C853) else MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(Alignment.End)) {
                        OutlinedButton(
                            onClick = { onRejectProperty(prop.id) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Reject", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                        }
                        Button(
                            onClick = { onApproveProperty(prop.id) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_approve_button_${prop.id}")
                        ) {
                            Text("Approve Listing", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
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
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
            Text(title, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
