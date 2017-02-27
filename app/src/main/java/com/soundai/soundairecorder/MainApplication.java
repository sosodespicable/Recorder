package com.soundai.soundairecorder;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fez on 2017/2/22.
 */

public class MainApplication extends Application {
    static {
        System.loadLibrary("sai_micbasex");
        System.loadLibrary("sai_micarray");
        System.loadLibrary("sai_denoise");
        System.loadLibrary("SAI_Recorder");
        System.loadLibrary("msc");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String rootPath = Environment.getDataDirectory().getPath() + "/data/" + MainApplication.this.getPackageName() + "/sai_config";
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdir();
        }
        if (!new File(rootPath + "/sai_config.txt").exists()) {
            copyAsset(MainApplication.this, "sai_config.txt", rootPath + "/sai_config.txt");
            copyAsset(MainApplication.this, "wopt_5mic.bin", rootPath + "/wopt_5mic.bin");
        }
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=580ed459");
    }

    private void copyAsset(Context context, String oldPath, String newPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newPath);
            is = context.getAssets().open(oldPath);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
