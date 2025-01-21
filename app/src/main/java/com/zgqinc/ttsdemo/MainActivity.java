package com.zgqinc.ttsdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private EditText editText;
    private Button speakButton;
    private Button settingsButton;
    private Button restartButton; // 新增按钮变量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        speakButton = findViewById(R.id.speakButton);
        settingsButton = findViewById(R.id.settingsButton);
        restartButton = findViewById(R.id.restartButton); // 初始化按钮

        // 初始化 TTS
        textToSpeech = new TextToSpeech(this, this);

        // 朗读按钮点击事件
        speakButton.setOnClickListener(v -> speakText());

        // 打开 TTS 设置按钮点击事件
        settingsButton.setOnClickListener(v -> openTTSSettings());

        // 重启按钮点击事件
        restartButton.setOnClickListener(v -> restartApplication());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.i("TTS", "TextToSpeech initialized successfully.");
        } else {
            Log.e("TTS", "Initialization failed.");
        }
    }

    private void speakText() {
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void openTTSSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("TTS", "Failed to open TTS settings: " + e.getMessage());
        }
    }

    private void restartApplication() {
        // 重启应用
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // 结束当前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}

