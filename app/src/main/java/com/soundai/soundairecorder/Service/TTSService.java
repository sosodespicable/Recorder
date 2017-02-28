package com.soundai.soundairecorder.Service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.soundai.soundairecorder.Fragment.RecordFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by fez on 2017/2/27.
 */

public class TTSService{

    private static final String TAG = "SoundAi";
    private SpeechSynthesizer tts;
    private Context context;
    private Random random;
    private MediaPlayer mediaPlayer;

    public TTSService(Context context) {
        this.context = context;
        random = new Random();
        mediaPlayer = new MediaPlayer();
        tts = SpeechSynthesizer.createSynthesizer(this.context, new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Log.e(TAG, "TTS init failed");
                }
            }
        });
    }

    public int startSpeaking(final String text) {
        setParam();
        int code = tts.startSpeaking(text, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                Log.e(TAG, "start tts");
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
//                Log.e(TAG, "buffer progress = " + i + "," + i1 + "," + i2 + "," + s);
            }

            @Override
            public void onSpeakPaused() {
                Log.e(TAG, "pause tts");
            }

            @Override
            public void onSpeakResumed() {
                Log.e(TAG, "resume tts");
            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {
//                Log.e(TAG, "speak progress = " + i + "," + i1 + "," + i2);
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (speechError == null) {
                    Log.e(TAG, "tts completed");
                    startSpeaking(RecordFragment.ttsTalkingDate[random.nextInt(9)]);
                } else {
                    Log.e(TAG, speechError.getErrorDescription());
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
        return code;
    }

    public void stopSpeaking() {
        tts.stopSpeaking();
    }

    public void startPlayMusic(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlayMusic() {
        mediaPlayer.stop();
    }

    private void setParam() {
        tts.setParameter(SpeechConstant.PARAMS, null);
        tts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        tts.setParameter(SpeechConstant.SPEED, "50");
        tts.setParameter(SpeechConstant.PITCH, "50");
        tts.setParameter(SpeechConstant.VOLUME, "50");
        tts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
//        tts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/SoundAiRecorder/" + getTTSName());
    }

}
