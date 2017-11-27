package com.fxj.androiddoubleprocessguard;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service {
    public static final String TAG="fxj1125";

    private RemoteBinder mRemoteBinder;
    private RemoteServiceConnection connection;

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"RemoteService.onCreate");
        if(this.mRemoteBinder==null){
            this.mRemoteBinder=new RemoteBinder();
        }

        if(this.connection==null){
            this.connection=new RemoteServiceConnection();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"RemoteService.onBind");
        RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
        RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),connection, Context.BIND_IMPORTANT);
        return this.mRemoteBinder;
    }



    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.i(TAG,"RemoteService.onStartCommand");
        RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
        RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),connection, Context.BIND_IMPORTANT);
        return START_STICKY;/*当Service被异常杀死时，系统会再去尝试再次启动这个Service，并且之前的Intent也会重新被传给onStartCommand方法*/
    }

    class RemoteBinder extends IServiceInterface.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return RemoteService.class.getSimpleName();
        }
    }

    class RemoteServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"远程服务连接本地服务成功!RemoteService connected LocalService succeed!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG,"远程服务连接本地服务失败!RemoteService Disconnected LocalService");
            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),connection,Context.BIND_IMPORTANT);
        }
    }
}
