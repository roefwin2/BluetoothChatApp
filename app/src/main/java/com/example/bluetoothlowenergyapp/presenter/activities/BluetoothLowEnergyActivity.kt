package com.example.bluetoothlowenergyapp.presenter.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluetoothlowenergyapp.domain.BluetoothDeviceDomain
import com.example.bluetoothlowenergyapp.presenter.BluetoothUIState
import com.example.bluetoothlowenergyapp.presenter.BluetoothViewModel
import com.example.bluetoothlowenergyapp.presenter.activities.ui.theme.BluetoothLowEnergyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BluetoothLowEnergyActivity : ComponentActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnable get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            /*NO needed */
        }
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true // always enable  for lower

            if (canEnableBluetooth && isBluetoothEnable) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        // no else becasue no need for lower 30 no dangerous = normal protection level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        }

        setContent {
            BluetoothLowEnergyAppTheme {
                val viewModel = hiltViewModel<BluetoothViewModel>()
                val state by viewModel.state.collectAsState()
                // A surface container using the 'background' color from the theme
                LaunchedEffect(state.errorMessage) {
                    state.errorMessage?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    }
                }
                LaunchedEffect(key1 = state.isConnected) {
                    if (state.isConnected) {
                        Toast.makeText(applicationContext, "Your are CONNECTED", Toast.LENGTH_LONG).show()
                    }
                }
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when {
                        state.isConnecting -> Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text(text = "Connecting...")
                        }
                        else -> {
                            DevicesScreen(
                                state,
                                onStartScan = {
                                    viewModel.startScan()
                                },
                                onStopScan = { viewModel.stopScan() },
                                onDeviceClick = viewModel::connectToDevice,
                                onStartServer = viewModel::waitForIncomingConnection
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DevicesScreen(
    bluetoothUIState: BluetoothUIState,
    onStartScan: (() -> Unit),
    onStopScan: (() -> Unit),
    onStartServer: (() -> Unit),
    onDeviceClick: ((BluetoothDeviceDomain) -> Unit),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BluetoothDeviceList(
            scannedDevices = bluetoothUIState.scannedDevices,
            pairedDevices = bluetoothUIState.pairedDevices,
            onClick = {
                onDeviceClick.invoke(it)
            }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            OutlinedButton(onClick = { onStartScan.invoke() }) {
                Text(text = "Start Scan")
            }
            OutlinedButton(onClick = { onStopScan.invoke() }) {
                Text(text = "Stop Scan")
            }
            OutlinedButton(onClick = { onStartServer.invoke() }) {
                Text(text = "Start Server")
            }
        }
    }
}

@Composable
fun BluetoothDeviceList(
    scannedDevices: List<BluetoothDeviceDomain>,
    pairedDevices: List<BluetoothDeviceDomain>,
    onClick: ((BluetoothDeviceDomain) -> Unit),
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {
        item {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "PAIRED DEVICES",
                fontWeight = FontWeight.Bold,
            )
        }
        items(pairedDevices) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onClick.invoke(it) },
                text = it.name ?: "No name",
                fontWeight = FontWeight.Bold,
            )
        }
        item {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "SCANNED DEVICES",
                fontWeight = FontWeight.Bold,
            )
        }
        items(scannedDevices) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onClick.invoke(it) },
                text = it.name ?: it.address,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BluetoothLowEnergyAppTheme {
    }
}