package com.soumik.subhankar.banglacheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;

public class CheckerActivity extends AppCompatActivity{

    private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);

        initialize();


        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("EditText");
        Charset charset = Charset.forName("UTF-8");
        String text1, text2 = Normalizer.normalize(new String(bytes, charset), Normalizer.Form.NFD);

        try {
            text1 = new String(bytes, "UTF-8");
            tv.setText(text1);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
    }

    private void initialize()
    {
        tv = findViewById(R.id.text_view);
    }
}
