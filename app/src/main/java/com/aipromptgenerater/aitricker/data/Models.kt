package com.aipromptgenerater.aitricker.data

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val credits: Int = 15, // 15 Free Credits
    val createdAt: Long = System.currentTimeMillis()
)

data class PromptHistory(
    val id: String = "",
    val type: String = "", // "Website" or "App"
    val idea: String = "",
    val result: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class PricingPlan(
    val id: String = "",
    val title: String = "",
    val price: Int = 0,
    val credits: Int = 0
)
