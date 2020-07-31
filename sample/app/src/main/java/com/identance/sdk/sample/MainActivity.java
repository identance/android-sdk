package com.identance.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.identance.sdk.VerificationClient;
import com.identance.sdk.VerificationMode;
import com.identance.sdk.VerificationStatus;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btnStart);
        button.setOnClickListener(v -> {
            VerificationClient.getInstance().startForResult(this, REQUEST_CODE, VerificationMode.ALL_STAGES);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case VerificationStatus.CANCELED:
                    Log.d(TAG, "User canceled verification");
                    break;
                case VerificationStatus.REJECTED:
                    Log.d(TAG, "User failed verification");
                    break;
                case VerificationStatus.SUBMITTED:

                    HashMap<String, String> stages = null;
                    stages = (HashMap<String, String>) data.getSerializableExtra(VerificationClient.EXTRAS_RESULT_VERIFICATION);

                    StringBuilder builder = new StringBuilder("Stages:");
                    for (Map.Entry<String, String> entry : stages.entrySet()) {
                        builder.append("\n" + entry.getKey() + " = " + entry.getValue());
                    }

                    Log.d(TAG, "User submitted verification\n" + builder.toString());
                    break;
            }
        }
    }
}


