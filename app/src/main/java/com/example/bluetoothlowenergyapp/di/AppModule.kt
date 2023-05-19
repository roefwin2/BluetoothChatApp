package com.example.bluetoothlowenergyapp.di

import android.content.Context
import com.example.bluetoothlowenergyapp.data.AndroidBluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context) = AndroidBluetoothController(context)
}