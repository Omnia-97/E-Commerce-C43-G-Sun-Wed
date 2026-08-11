package com.route.data.dataSource

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val TOKEN = stringPreferencesKey("token")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_PHONE = stringPreferencesKey("user_phone")
}