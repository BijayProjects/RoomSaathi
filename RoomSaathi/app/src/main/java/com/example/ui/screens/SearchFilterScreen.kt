package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PropertyCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterScreen(
    currentMaxPrice: Double,
    currentCategory: PropertyCategory,
    isInstantOnly: Boolean,
    isVerifiedOnly: Boolean,
    onApplyFilters: (Double, PropertyCategory, Boolean, Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var maxPrice by remember { mutableFloatStateOf(currentMaxPrice.toFloat()) }
    var selectedCat by remember { mutableStateOf(currentCategory) }
    var instantOnly by remember { mutableStateOf(isInstantOnly) }
    var verifiedOnly by remember { mutableStateOf(isVerifiedOnly) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter Properties", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        maxPrice = 300f
                        selectedCat = PropertyCategory.ALL
                        instantOnly = false
                        verifiedOnly = false
                    }) {
                        Text("Reset All")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            onApplyFilters(maxPrice.toDouble(), selectedCat, instantOnly, verifiedOnly)
                            onBackClick()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("apply_search_filters_button")
                    ) {
                        Text("Apply Search Filters", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Price Range Slider
            item {
                Text("Max Price / Night", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$0", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Text(
                        "$${maxPrice.toInt()}/night",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Slider(
                    value = maxPrice,
                    onValueChange = { maxPrice = it },
                    valueRange = 10f..500f,
                    steps = 49,
                    modifier = Modifier.testTag("price_filter_slider")
                )
            }

            // Categories
            item {
                Text("Property Category", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PropertyCategory.entries.chunked(3).forEach { rowCats ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowCats.forEach { cat ->
                                FilterChip(
                                    selected = selectedCat == cat,
                                    onClick = { selectedCat = cat },
                                    label = { Text(cat.displayName, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // Quick Options
            item {
                Text("Preferences & Verification", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Instant Booking Only", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Properties that don't require host approval", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Switch(
                                checked = instantOnly,
                                onCheckedChange = { instantOnly = it },
                                modifier = Modifier.testTag("instant_booking_switch")
                            )
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Verified Listings Only", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Show properties with verified blue badge", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Switch(
                                checked = verifiedOnly,
                                onCheckedChange = { verifiedOnly = it },
                                modifier = Modifier.testTag("verified_only_switch")
                            )
                        }
                    }
                }
            }
        }
    }
}
