package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button createQRBtn;
    private Button scanQRBtn;
    public EditText editText;
    public EditText editText2;
    public String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createQRBtn = (Button) findViewById(R.id.createQR);
        scanQRBtn = (Button) findViewById(R.id.scanQR);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);


        createQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                text = "name :" + " " + editText.getText().toString() + " Phone :" + " " + editText2.getText().toString();
                Intent intent = new Intent(MainActivity.this, CreateQR.class);

                intent.putExtra("text",text);

                startActivity(intent);
            }
        });

        scanQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivity(intent);
            }
        });

    }
}
