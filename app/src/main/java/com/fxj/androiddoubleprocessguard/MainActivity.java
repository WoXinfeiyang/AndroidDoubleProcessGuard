package com.fxj.androiddoubleprocessguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG="fxj1125";
    private Button mStartServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.mStartServiceBtn= (Button) findViewById(R.id.btn_start_service);
        this.mStartServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LocalService.class);
                MainActivity.this.startService(intent);

                Toast.makeText(getApplicationContext(),"StartServiceBtn 被点击了!",Toast.LENGTH_SHORT).show();
            }
        });
        startService(new Intent(this,GuardJobService.class));
    }

}
