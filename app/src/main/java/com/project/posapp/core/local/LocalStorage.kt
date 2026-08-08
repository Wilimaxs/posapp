package com.project.posapp.core.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalStorage @Inject constructor(
    @param:ApplicationContext
    @PublishedApi internal val context: Context
) {
    val Context.dataStore by preferencesDataStore(name = "local_storage")

    suspend inline fun <reified T> save(
        key: String,
        value: T
    ) {
        context.dataStore.edit {
            when (value) {
                is String -> it[stringPreferencesKey(key)] = value
                is Int -> it[intPreferencesKey(key)] = value
                is Long -> it[longPreferencesKey(key)] = value
                is Float -> it[floatPreferencesKey(key)] = value
                is Double -> it[doublePreferencesKey(key)] = value
                is Boolean -> it[booleanPreferencesKey(key)] = value
                else -> error("Unsupported type")
            }
        }
    }

    inline fun <reified T> get(
        key: String,
        default: T
    ): Flow<T> {
        return context.dataStore.data.map {
            when (T::class) {
                String::class -> it[stringPreferencesKey(key)]
                Int::class -> it[intPreferencesKey(key)]
                Long::class -> it[longPreferencesKey(key)]
                Float::class -> it[floatPreferencesKey(key)]
                Double::class -> it[doublePreferencesKey(key)]
                Boolean::class -> it[booleanPreferencesKey(key)]
                else -> error("Unsupported type")
            } as? T ?: default
        }
    }

    suspend fun remove(key: String) {
        context.dataStore.edit {
            it.remove(stringPreferencesKey(key))
            it.remove(intPreferencesKey(key))
            it.remove(longPreferencesKey(key))
            it.remove(floatPreferencesKey(key))
            it.remove(doublePreferencesKey(key))
            it.remove(booleanPreferencesKey(key))
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
