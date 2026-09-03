package com.example.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsHelper(context: Context) {
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logVideoView(videoId: String, creator: String, durationMs: Long) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, videoId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, creator)
            putLong("view_duration_ms", durationMs)
        }
        firebaseAnalytics.logEvent("video_view_duration", bundle)
    }

    fun logClickThrough(videoId: String, actionType: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, videoId)
            putString("action_type", actionType)
        }
        firebaseAnalytics.logEvent("video_click_through", bundle)
    }

    fun logSearchQuery(query: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }
}
