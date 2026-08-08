package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.example.data.model.SavedFavorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    allProperties: List<Property>,
    favorites: List<SavedFavorite>,
    onPropertyClick: (Property) -> Unit,
    onToggleFav: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favPropertyIds = favorites.map { it.propertyId }.toSet()
    val favProperties = allProperties.filter { it.id in favPropertyIds }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Saved Favorites (${favProperties.size})", fontWeight = FontWeight.Bold) }
        )

        if (favProperties.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Empty Wishlist",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your wishlist is empty", fontWeight = FontWeight.Bold)
                    Text("Tap the heart icon on any property to save it here", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.testTag("wishlist_properties_list")
            ) {
                items(favProperties, key = { it.id }) { property ->
                    PropertyCard(
                        property = property,
                        isFavorite = true,
                        onClick = { onPropertyClick(property) },
                        onToggleFav = { onToggleFav(property.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
