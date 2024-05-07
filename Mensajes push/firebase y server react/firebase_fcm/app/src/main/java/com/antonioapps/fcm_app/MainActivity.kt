package com.antonioapps.fcm_app


import android.content.ContentValues.TAG
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.antonioapps.fcm_app.ui.theme.FCM_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FCM_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FCMApp()
                }
            }
        }
    }
}
@Composable
fun FCMApp() {
    val token = remember { mutableStateOf<String?>(null) }

    RequestNotificationPermission {
        getToken { newToken ->
            token.value = newToken
        }
    }

    Column {
        token.value?.let {
            Text(text = "Token: $it")
        }
    }
}

@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp, 40.dp)
            .padding(vertical = 8.dp) // Agrega un espacio vertical alrededor del botón
    ) {
        Button(
            onClick = { requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "Conceder permiso")
        }
    }
}


fun getToken(onTokenReceived: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            onTokenReceived(token.orEmpty())
        }
    })
}