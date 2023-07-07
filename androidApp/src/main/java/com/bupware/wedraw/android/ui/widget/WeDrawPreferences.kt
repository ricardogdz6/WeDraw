package com.bupware.wedraw.android.ui.widget

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bupware.wedraw.android.data.tables.image.Image




const val IMAGE_KEY = "image_key"

fun MutablePreferences.setImageData(image: Image) {
    this[stringPreferencesKey(IMAGE_KEY)] = image.uri
}