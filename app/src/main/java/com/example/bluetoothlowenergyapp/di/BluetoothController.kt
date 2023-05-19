package com.example.bluetoothlowenergyapp.di

import com.example.bluetoothlowenergyapp.data.AndroidBluetoothController
import com.example.bluetoothlowenergyapp.domain.BluetoothController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothControllerModule
{
    @Binds
    abstract fun bindBluetoothController(
        bluetoothController: AndroidBluetoothController
    ): BluetoothController
}