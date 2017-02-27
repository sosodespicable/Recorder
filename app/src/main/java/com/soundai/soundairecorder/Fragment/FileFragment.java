package com.soundai.soundairecorder.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soundai.soundairecorder.Adapter.FileAdapter;
import com.soundai.soundairecorder.R;

/**
 * Created by fez on 2017/2/24.
 */

public class FileFragment extends Fragment {

    private static final String TAG = "SoundAi";
    private static final String ARG_POSITION = "position";

    private FileAdapter fileAdapter;
    private int position;

    public static FileFragment getInstance(int position) {
        FileFragment f = new FileFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: FileFragment onCreat");
        position = getArguments().getInt(ARG_POSITION);
        observer.startWatching();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: FileFragment onCreateView");
        View v = inflater.inflate(R.layout.fragment_file, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // TODO: 2017/2/24 为Recyclerview设置Adapter
        fileAdapter = new FileAdapter(getActivity(), linearLayoutManager);
        recyclerView.setAdapter(fileAdapter);
        return v;
    }

    FileObserver observer = new FileObserver(Environment.getExternalStorageDirectory().getPath() + "/SoundAiRecorder") {
        @Override
        public void onEvent(int i, String s) {
            if (i == FileObserver.DELETE) {
                // TODO: 2017/2/25 通知List移除已删除的项
            }
        }
    };
}
