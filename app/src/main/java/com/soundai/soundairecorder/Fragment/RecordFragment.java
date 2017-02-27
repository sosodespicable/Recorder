package com.soundai.soundairecorder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.soundai.soundairecorder.Action;
import com.soundai.soundairecorder.R;
import com.soundai.soundairecorder.Service.RecorderService;
import com.soundai.soundairecorder.Service.TTSService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by fez on 2017/2/22.
 */

public class RecordFragment extends Fragment {

    private static final String TAG = "SoundAi";
    private static final String ARG_POSITION = "position";

    private int position;
    private int status = 0;
    private boolean isStart = false;
    private boolean isPause = true;
    private long whenPause = 0;

    private FloatingActionButton recordButton;
    private FloatingActionButton pauseButton;
    private Chronometer mChronometer;
    private TTSService tts;

    public static RecordFragment newInstance(int position) {
        RecordFragment f = new RecordFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public RecordFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        tts = new TTSService(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.fragment_record, container, false);
        mChronometer = (Chronometer) recordView.findViewById(R.id.chronometer);
        recordButton = (FloatingActionButton) recordView.findViewById(R.id.btn_record);
        recordButton.setColorNormal(getResources().getColor(R.color.colorPrimary));
        recordButton.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/2/22 开始/暂停录音
                tts.startSpeaking("你好北京明天的天气");
                if (isStart) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });
        pauseButton = (FloatingActionButton) recordView.findViewById(R.id.btn_pause);
        pauseButton.setColorNormal(getResources().getColor(R.color.colorPrimary));
        pauseButton.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/2/23 暂停/继续录音
                pauseRecording();
            }
        });
        return recordView;
    }

    private void startRecording() {
        isStart = true;
        isPause = false;
        recordButton.setImageResource(R.mipmap.ic_media_stop);
        pauseButton.setEnabled(true);
        Toast.makeText(getActivity(), "Recording start", Toast.LENGTH_SHORT).show();
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/SoundAiRecorder");
        if (!folder.exists()) {
            folder.mkdir();
        }
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

            }
        });

        // TODO: 2017/2/23 初始化一个Intent(Service)
        Intent intent = new Intent(getActivity(), RecorderService.class);
        intent.setAction(Action.START_RECORDING);
        getActivity().startService(intent);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopRecording() {
        isStart = false;
        whenPause = 0;
        recordButton.setImageResource(R.mipmap.ic_media_play);
        pauseButton.setEnabled(false);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());

        // TODO: 2017/2/23 停止Service
        Intent intent = new Intent(getActivity(), RecorderService.class);
        intent.setAction(Action.STOP_RECORDING);
        getActivity().stopService(intent);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void pauseRecording() {
        isPause = true;
        isStart = false;
        recordButton.setImageResource(R.mipmap.ic_media_play);
        pauseButton.setVisibility(View.GONE);
        whenPause = mChronometer.getBase() - SystemClock.elapsedRealtime();
        mChronometer.stop();

        Intent intent = new Intent(getActivity(), RecorderService.class);
        intent.setAction(Action.PAUSE_RECORDING);
        getActivity().stopService(intent);
    }

    private void resumeRecording() {
        isPause = false;
        isStart = true;
        recordButton.setImageResource(R.mipmap.ic_media_stop);
        pauseButton.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime() + whenPause);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

            }
        });
        mChronometer.start();

        Intent intent = new Intent(getActivity(), RecorderService.class);
        intent.setAction(Action.RESUME_RECORDING);
        getActivity().startService(intent);
    }
}
