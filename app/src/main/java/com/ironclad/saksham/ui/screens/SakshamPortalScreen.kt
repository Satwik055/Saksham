package com.ironclad.saksham.ui.screens

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SakshamPortalScreen(modifier: Modifier) {

    val url = "https://saksham.sitslive.com/"

    AndroidView(
        modifier = modifier,
        factory = {
        WebView(it).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }, update = {
        it.loadUrl(url)
    })
}


//            val cookieManager = CookieManager.getInstance()
//            cookieManager.setCookie(url, "ASP.NET_SessionId=uoj2ofepm0d0xus2sfijpt0v")
//            cookieManager.setAcceptCookie(true)
//            cookieManager.setAcceptThirdPartyCookies(this, true)
//
//            val cookie = cookieManager.getCookie(url)
//            println("Cookies: $cookie")
