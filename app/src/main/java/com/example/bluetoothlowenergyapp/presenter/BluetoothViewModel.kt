package com.example.bluetoothlowenergyapp.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothlowenergyapp.domain.BluetoothController
import com.example.bluetoothlowenergyapp.domain.BluetoothDevice
import com.example.bluetoothlowenergyapp.domain.BluetoothDeviceDomain
import com.example.bluetoothlowenergyapp.domain.ConnectionResult
import com.example.bluetoothlowenergyapp.domain.ConnectionResult.*
import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion
import kotlinx.coroutines.launch
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
        state.copy(
            scannedDevices,
            pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        ) // clean message liost when disconnected
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null

    init {
        //to listen to each connection
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)
        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(errorMessage = error) }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(bluetoothDevice: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.connectToDevice(bluetoothDevice).listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update { it.copy(isConnected = false, isConnecting = false) }
    }

    fun waitForIncomingConnection() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.startBluetoothServer().listen()
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val sendMessage = bluetoothController.trySendMessage(message = message)
            if (sendMessage != null) {
                _state.update {
                    it.copy(messages = it.messages + sendMessage)
                }
            }
        }
    }

    // twice way etablish server and connect or connect only
    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionEstablished -> {
                    _state.update { it.copy(isConnected = true, isConnecting = false, errorMessage = null) }
                }
                is Error -> {
                    print(result.message)
                    _state.update { it.copy(isConnected = false, isConnecting = false, errorMessage = result.message) }
                }
                is TransferSucceeded -> _state.update { it.copy(messages = it.messages + result.message) }
            }
        }.catch { throwable ->
            bluetoothController.closeConnection()
            _state.update { it.copy(isConnected = false, isConnecting = false, errorMessage = throwable.message) }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        //release all ressoucres when vm leaves the back stack
        bluetoothController.release()
    }
}

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)