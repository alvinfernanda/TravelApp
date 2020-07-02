package com.example.android.travelapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.android.travelapp.activity.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    Animation topAnim;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        textView = findViewById(R.id.travalapp);

        textView.setAnimation(topAnim);

        getUsernameLocal();
    }

    private void getUsernameLocal() {
        String usernamelocal = Preferences.getLoggedInUser(getBaseContext());
        if(usernamelocal.isEmpty()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotologin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(gotologin);
                    finish();
                }
            }, 2000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotohome = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(gotohome);
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }
}
