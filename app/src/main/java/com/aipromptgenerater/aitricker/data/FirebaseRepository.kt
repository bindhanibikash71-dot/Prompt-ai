package com.aipromptgenerater.aitricker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.vertexai.FirebaseVertexAI
import com.google.firebase.vertexai.type.content
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val vertexAI = FirebaseVertexAI.getInstance()
    private val model = vertexAI.generativeModel("gemini-2.5-flash") // Flash lite equivalent

    val currentUserUid get() = auth.currentUser?.uid

    // Credit Deduction & Generation (Transaction ensures security)
    suspend fun generatePrompt(type: String, name: String, idea: String, techStack: String): Result<String> {
        val uid = currentUserUid ?: return Result.failure(Exception("Not Logged In"))
        val userRef = db.collection("users").document(uid)

        return try {
            // 1. Secure Transaction: Check and Deduct 5 credits
            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val credits = snapshot.getLong("credits") ?: 0
                if (credits < 5) throw Exception("Insufficient credits. Please recharge.")
                transaction.update(userRef, "credits", credits - 5)
            }.await()

            // 2. Fetch Admin System Prompt Rules
            val configSnapshot = db.collection("admin").document("config").get().await()
            val sysPrompt = configSnapshot.getString("system_prompt") 
                ?: "You are an expert developer. Output strictly clean, structured plain text. Absolutely NO markdown like ** or #. No formatting symbols."

            // 3. Generate via Gemini
            val promptText = "$sysPrompt\nType: $type\nName: $name\nIdea: $idea\nTech Stack: $techStack"
            val response = model.generateContent(
                content { text(promptText) }
            )
            
            val cleanResult = response.text?.replace(Regex("[*#`]"), "")?.trim() ?: "Failed to generate"

            // 4. Save History
            val historyId = db.collection("users").document(uid).collection("prompts").document().id
            val history = PromptHistory(historyId, type, idea, cleanResult)
            db.collection("users").document(uid).collection("prompts").document(historyId).set(history).await()

            Result.success(cleanResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWalletCredits(): Int {
        val uid = currentUserUid ?: return 0
        val snap = db.collection("users").document(uid).get().await()
        return snap.getLong("credits")?.toInt() ?: 0
    }
}
