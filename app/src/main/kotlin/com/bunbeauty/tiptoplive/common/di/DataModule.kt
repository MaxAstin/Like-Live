package com.bunbeauty.tiptoplive.common.di

import com.bunbeauty.tiptoplive.common.data.SharedPreferencesStorage
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsKeyValueStorage(sharedPreferencesStorage: SharedPreferencesStorage): KeyValueStorage

}