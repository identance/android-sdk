package com.identance.sdk.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.identance.sdk.VerificationClient
import com.identance.sdk.VerificationMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.btnStart).setOnClickListener {
            VerificationClient.getInstance().start(this, VerificationMode.ALL_STAGES)
        }
    }
}