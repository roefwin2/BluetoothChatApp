package com.example.bluetoothlowenergyapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothlowenergyapp.domain.BluetoothController
import com.example.bluetoothlowenergyapp.domain.BluetoothDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    // use abstraction to not write real fake implementation
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        bluetoothController.scanDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(scannedDevices, pairedDevices)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }
}

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
)