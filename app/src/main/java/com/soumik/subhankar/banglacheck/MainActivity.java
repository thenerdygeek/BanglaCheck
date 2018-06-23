package com.soumik.subhankar.banglacheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;
    private Button run, modify;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();



    }

    private void initialize()
    {
        editText = findViewById(R.id.edit_text);
        run = findViewById(R.id.run);
        run.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.run:
                Charset charset = Charset.forName("UTF-8");
                byte[] bytes = editText.getText().toString().getBytes(charset);
                Intent intent = new Intent(this, CheckerActivity.class);
                intent.putExtra("EditText", bytes);
                startActivity(intent);
                finish();
        }
    }
}
