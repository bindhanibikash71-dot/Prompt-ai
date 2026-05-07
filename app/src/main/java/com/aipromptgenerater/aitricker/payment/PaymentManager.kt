package com.aipromptgenerater.aitricker.payment

import android.app.Activity
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.CFTheme
import com.cashfree.pg.ui.api.CFDropCheckoutPayment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentManager @Inject constructor(private val db: FirebaseFirestore) {

    // Note: In production, the session_id MUST be generated on your backend (Firebase Functions).
    // This function mimics fetching that session ID from your secure backend.
    suspend fun initiatePayment(activity: Activity, planId: String, userId: String) {
        try {
            // Call your backend/Firebase Function to create Cashfree Order and get Session ID
            // val sessionId = myBackend.createOrder(planId, userId)
            val sessionId = "dummy_session_id_from_backend" 

            val cfSession = CFSession.CFSessionBuilder()
                .setEnvironment(CFSession.Environment.PRODUCTION)
                .setPaymentSessionID(sessionId)
                .setOrderId("order_$planId\_${System.currentTimeMillis()}")
                .build()

            val cfTheme = CFTheme.CFThemeBuilder()
                .setNavigationBarBackgroundColor("#0A192F")
                .setNavigationBarTextColor("#FFFFFF")
                .setButtonBackgroundColor("#3B82F6")
                .setButtonTextColor("#FFFFFF")
                .build()

            val cfDropCheckoutPayment = CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                .session(cfSession)
                .cfDropCheckoutUITheme(cfTheme)
                .build()

            CFPaymentGatewayService.getInstance().doPayment(activity, cfDropCheckoutPayment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
