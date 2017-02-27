package com.soundai.soundairecorder.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soundai.soundairecorder.DBHelper;
import com.soundai.soundairecorder.OnDatabaseChangedListener;
import com.soundai.soundairecorder.R;
import com.soundai.soundairecorder.RecordItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by fez on 2017/2/25.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.RecordingViewHolder> implements OnDatabaseChangedListener{

    private static final String TAG = "SoundAi";

    private Context context;
    private RecordItem item;
    private DBHelper db;
    private LinearLayoutManager linearLayoutManager;

    public FileAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super();
        this.context = context;
        db = new DBHelper(this.context);
        db.setOnDatabaseChangedListener(FileAdapter.this);
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: FileAdapter");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recorditem, parent, false);
        this.context = parent.getContext();
        return new RecordingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FileAdapter");
        item = getItem(position);
        long itemDuration = item.getLength();
        long min = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long sec = TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(min);
        holder.fileName.setText(item.getName());
        holder.fileLength.setText(String.format("%02d:%02d", min, sec));
        holder.fileTime.setText(DateUtils.formatDateTime(this.context,
                item.getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/2/25 添加一个播放界面
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ArrayList<String> list = new ArrayList<String>();
                list.add("Share");
                list.add("Rename");
                list.add("Delete");

                AlertDialog.Builder builder = new AlertDialog.Builder(FileAdapter.this.context);
                builder.setTitle("Options");
                builder.setItems(list.toArray(new CharSequence[list.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // TODO: 2017/2/25 分享
                        } if (i == 1) {
                            // TODO: 2017/2/25 重命名
                        } if (i == 2) {
                            // TODO: 2017/2/25 删除
                        }
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return db.getCount();
    }

    @Override
    public void onDataAdded() {
        notifyItemChanged(getItemCount() - 1);
        linearLayoutManager.scrollToPosition(getItemCount() - 1);
    }

    @Override
    public void onDataRenamed() {
        // TODO: 2017/2/25  数据库中信息重命名时回调
    }

    public static class RecordingViewHolder extends RecyclerView.ViewHolder {

        protected TextView fileName;
        protected TextView fileLength;
        protected TextView fileTime;
        protected CardView cardView;

        public RecordingViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "RecordingViewHolder: FileAdapter");
            fileName = (TextView) itemView.findViewById(R.id.txv_filename);
            fileLength = (TextView) itemView.findViewById(R.id.txv_filelength);
            fileTime = (TextView) itemView.findViewById(R.id.txv_fileTime);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public RecordItem getItem(int position) {
        return db.getItem(position);
    }
}
