package com.fxj.androiddoubleprocessguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 文件名:Android双进程守护
 * 时间:2017-11-28 10:28
 * 说明：1、Android系统中运行Service有2中方式：
 * (a)通过Context.startService启动Service，访问者与Service之间没有关联,即使访问者退出Service依然运行。
 * (b)通过Context.bindService启动Service，访问者与Service绑定在一起，访问者一旦退出，Service也就终止了。
 * 2、Google提供了一个叫做JobScheduler API的组件来处理这样的场景--需要在稍后的某个时间点或者当满足某个特定的条件时执行一个任务.
 * 3、使用JobService时一定要添加android.permission.BIND_JOB_SERVICE权限.
 * 4、JobService运行在主线程，需要使用子线程、Handler或者异步任务来处理耗时操作以避免阻塞主线程。
 * */

public class MainActivity extends Activity {
    private static final String TAG=MainActivity.class.getSimpleName();
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
