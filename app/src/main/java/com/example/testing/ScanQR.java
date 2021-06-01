package com.example.testing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanQR extends AppCompatActivity {
    private final String dbName = "webnautes";
    private final String tableName = "person";


    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private static final String TAG_NAME = "name";

    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;

    private Button scanQR2btn;
    private Button scanQR3btn;

    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_qr);



        scanQR2btn = (Button) findViewById(R.id.scanQR2);
        scanQR3btn = (Button) findViewById(R.id.deleteview);


        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QR코드 스캔중");
        qrScan.initiateScan();

        scanQR2btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ScanQR.this, MainActivity.class);
                startActivity(intent);
            }
        });

        scanQR3btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                deletelist();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
                try {


                    sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                    //테이블이 존재하지 않으면 새로 생성합니다.
                    sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                            + " (name VARCHAR(20));");

                    sampleDB.close();

                } catch (SQLiteException se) {
                    Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("", se.getMessage());


                }

                list = (ListView) findViewById(R.id.listView);
                personList = new ArrayList<HashMap<String,String>>();


                showList();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo

                String names = result.getContents();

                try {


                    sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

                    //테이블이 존재하지 않으면 새로 생성합니다.
                    sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                            + " (name VARCHAR(20));");


                    //새로운 데이터를 테이블에 집어넣습니다..
                    //for (int i=0; i<names.length; i++ ) {
                    sampleDB.execSQL("INSERT INTO " + tableName
                            + " (name)  Values ('" + names + "');");
                    // }

                    sampleDB.close();

                } catch (SQLiteException se) {
                    Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("", se.getMessage());


                }

                list = (ListView) findViewById(R.id.listView);
                personList = new ArrayList<HashMap<String,String>>();


                showList();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    protected void showList(){

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String Name = c.getString(c.getColumnIndex("name"));

                        //HashMap에 넣습니다.
                        HashMap<String,String> persons = new HashMap<String,String>();

                        persons.put(TAG_NAME,Name);

                        //ArrayList에 추가합니다..
                        personList.add(persons);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();


            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, personList, R.layout.list_item,
                    new String[]{TAG_NAME},
                    new int[]{ R.id.name}
            );


            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);


        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }

    protected void deletelist(){
        try {


            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            sampleDB.execSQL("DELETE FROM " + tableName  );

            sampleDB.close();

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());


        }

    }



}
