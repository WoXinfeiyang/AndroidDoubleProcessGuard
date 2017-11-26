package com.fxj.androiddoubleprocessguard;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class GuardJobService extends JobService {
    private static final String TAG="fxj1125";

    public GuardJobService() {
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG,"GuardJobService.onStartJob");
        GuardJobService.this.startService(new Intent(GuardJobService.this,LocalService.class));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG,"GuardJobService.onStopJob");
        return false;
    }


}
