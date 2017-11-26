package com.fxj.androiddoubleprocessguard;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocalService extends Service {
    private static final String TAG="fxj1125";
//    private static final String TAG=LocalService.class.getSimpleName();

    private LocalBinder mLocalBinder;

    LocalServiceConnection connection;

    public LocalService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"LocalService#onCreate");
        if(this.mLocalBinder==null){
            this.mLocalBinder=new LocalBinder();
        }
        if(this.connection==null){
            this.connection=new LocalServiceConnection();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"LocalService#onBind");
        return mLocalBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"LocalService#onStartCommand");
        startService(new Intent(LocalService.this,RemoteService.class));
        this.bindService(new Intent(LocalService.this,RemoteService.class),connection, Context.BIND_IMPORTANT);
        return START_REDELIVER_INTENT;/*当Service被异常杀死时，系统会再去尝试再次启动这个Service，并且之前的Intent也会重新被传给onStartCommand方法*/
    }

    class LocalBinder extends IServiceInterface.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return LocalService.class.getSimpleName();
        }
    }

    class LocalServiceConnection implements ServiceConnection{
        /*访问者与Service连接成功时的回调方法*/
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"本地服务连接远程服务成功!LocalService connected RemoteService succeed!");
        }
        /*访问者与Service连接失败时的回调方法*/
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG,"本地服务连接远程服务失败!LocalService Disconnected RemoteService!");
            LocalService.this.startService(new Intent(LocalService.this,RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this,RemoteService.class),connection,Context.BIND_IMPORTANT);
        }
    }
}
