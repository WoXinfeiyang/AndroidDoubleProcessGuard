package com.fxj.androiddoubleprocessguard;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class GuardJobService extends JobService {
    private static final String TAG="fxj1125";

    public GuardJobService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"GuardJobService.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"GuardJobService.onStartCommand");

        schedulerJob(getJobInfo());

        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG,"GuardJobService.onStartJob");

        boolean isLocalServiceWork=isServiceWork(this,LocalService.class.getName());

        if(!isLocalServiceWork){
            this.startService(new Intent(this,LocalService.class));
            Toast.makeText(this, "进程启动", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG,"GuardJobService.onStopJob");
        schedulerJob(getJobInfo());
        return false;
    }
    //将任务作业发送到作业调度中去
    private void schedulerJob(JobInfo jobInfo){
        JobScheduler jobScheduler= (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    private JobInfo getJobInfo(){
        JobInfo.Builder builder=new JobInfo.Builder(0x01,new ComponentName(this,LocalService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);/*在满足指定网络条件的情况下执行任务*/
        builder.setPersisted(true);/*设置重启设备后任务是否执行*/
        builder.setRequiresCharging(false);/*设置设备是否充电的时候执行任务*/
        builder.setRequiresDeviceIdle(false);/*设置设备是否空闲的时候执行任务*/
        //间隔100毫秒
        builder.setPeriodic(100);/*设置任务调度执行周期*/
        return builder.build();
    }

    // 判断服务是否正在运行
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
