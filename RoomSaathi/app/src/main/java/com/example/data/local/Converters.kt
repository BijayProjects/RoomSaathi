package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.PropertyCategory
import com.example.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromListString(value: List<String>): String {
        return value.joinToString("|||")
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split("|||")
    }

    @TypeConverter
    fun fromPropertyCategory(category: PropertyCategory): String {
        return category.name
    }

    @TypeConverter
    fun toPropertyCategory(value: String): PropertyCategory {
        return try {
            PropertyCategory.valueOf(value)
        } catch (e: Exception) {
            PropertyCategory.ROOM
        }
    }

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(value: String): UserRole {
        return try {
            UserRole.valueOf(value)
        } catch (e: Exception) {
            UserRole.BUYER
        }
    }
}
