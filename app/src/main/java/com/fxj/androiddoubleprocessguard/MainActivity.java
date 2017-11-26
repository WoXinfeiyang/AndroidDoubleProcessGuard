package com.fxj.androiddoubleprocessguard;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
//                startGuardJobService();
                Toast.makeText(getApplicationContext(),"StartServiceBtn 被点击了!",Toast.LENGTH_SHORT).show();
            }
        });

//        startGuardJobService();
    }

    private void startGuardJobService(){
        Log.i(TAG,"MainActivity.startGuardJobService");
        JobScheduler jobScheduler= (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int scheduleResult;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder mJobInfoBuilder=new JobInfo.Builder(0x01,new ComponentName(MainActivity.this,LocalService.class));
            mJobInfoBuilder.setPeriodic(1000);/*设置任务执行的周期*/
            mJobInfoBuilder.setOverrideDeadline(0);/*设置任务最晚延迟执行时间*/
            mJobInfoBuilder.setPersisted(true);/*设置手机重启了任务是否还执行*/
            //mJobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);/*设置任务在指定网络条件下执行*/

            if((scheduleResult=jobScheduler.schedule(mJobInfoBuilder.build()))<=0){
                Log.i(TAG,"JobScheduler调度GuardJobService启动失败!scheduleResult="+scheduleResult);
                Toast.makeText(MainActivity.this,"GuardJobService启动失败!",Toast.LENGTH_SHORT).show();
            }else{
                Log.i(TAG,"JobScheduler调度GuardJobService启动成功!scheduleResult="+scheduleResult);
                Toast.makeText(MainActivity.this,"GuardJobService启动成功!scheduleResult="+scheduleResult,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
