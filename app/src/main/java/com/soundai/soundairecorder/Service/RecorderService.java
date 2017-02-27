package com.soundai.soundairecorder.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.soundai.pk_maso.SaiManager;
import com.soundai.soundairecorder.Action;
import com.soundai.soundairecorder.DBHelper;
import com.soundai.soundairecorder.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by fez on 2017/2/23.
 */

public class RecorderService extends Service {

    private static final String TAG = "SoundAi";

    private SaiManager mSaiMaManager;
    private DBHelper db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSaiMaManager = SaiManager.getInstance();
        db = new DBHelper(RecorderService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: 2017/2/23 startRecording(intent.getString("action"));
        Log.d(TAG, "onStartCommand: " + intent.getAction());
        String action = intent.getAction();
        switch (action) {
            case Action.START_RECORDING:
                startRecording(getFileName());
                break;
            case Action.STOP_RECORDING:
                stopRecording();
                break;
            case Action.RESUME_RECORDING:
                break;
            case Action.PAUSE_RECORDING:
                break;
            default:
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroy");
        stopRecording();
    }

    private void startRecording(String fileName) {
        String SAI_CONFIG = Environment.getDataDirectory().getPath() + "/data/" + RecorderService.this.getPackageName() + "/sai_config";
        db.addItem("soundai.pcm", fileName, 0);
        if (mSaiMaManager.init(SAI_CONFIG) == 1) {
            Log.d(TAG, "startRecording: init success");
            mSaiMaManager.record(Environment.getExternalStorageDirectory().getPath() + "/SoundAiRecorder/" + fileName);
        } else {
            Log.e(TAG, "init failed");
        }
    }

    private void stopRecording() {
        mSaiMaManager.stop();
    }

    private String getFileName() {
        return new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + ".pcm";
    }

}
