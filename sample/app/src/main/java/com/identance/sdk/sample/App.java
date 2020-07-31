package com.identance.sdk.sample;

import android.app.Application;

import androidx.annotation.NonNull;

import com.identance.sdk.TokenConsumer;
import com.identance.sdk.TokenProvider;
import com.identance.sdk.VerificationBuilder;
import com.identance.sdk.VerificationClient;

public class App extends Application {

    public static VerificationClient verificationClient;

    @Override
    public void onCreate() {
        super.onCreate();

        VerificationBuilder builder = new VerificationBuilder(this)
                .setUserId("userId")
                .setEndpoint("your endpoint")
                .setNightMode()
                .addTokenProvider(new TokenProvider() {

                    @NonNull
                    @Override
                    public void requestToken(TokenConsumer tokenConsumer) {
                        tokenConsumer.setToken("");
                    }

                    @Override
                    public void cancelRequest() {

                    }
                });

        verificationClient = builder.build();
    }
}
