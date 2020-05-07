package com.identance.sdk.sample

import android.app.Application
import com.identance.sdk.VerificationBuilder


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = VerificationBuilder(this)
            .setUserId("userId")
            .setEndpoint("your endpoint")
            .setNightMode()
            .addTokenProvider { "Java web token, should be provided by merchant" }

        builder.build()
    }

}