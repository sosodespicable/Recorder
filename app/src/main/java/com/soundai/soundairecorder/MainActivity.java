package com.soundai.soundairecorder;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.soundai.pk_maso.SaiManager;
import com.soundai.soundairecorder.Fragment.FileFragment;
import com.soundai.soundairecorder.Fragment.LicensesFragment;
import com.soundai.soundairecorder.Fragment.RecordFragment;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "SoundAi";

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private UsbManager usbManager;
    private SaiManager mSaiMaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSaiMaManager = SaiManager.getInstance();
        initView();
        initDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_licenses:
                openLicenses();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void initDevice() {
        usbManager = (UsbManager) MainActivity.this.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDeviceHashMap = usbManager.getDeviceList();
        if (usbDeviceHashMap.size() != 0) {
            mSaiMaManager.getPermission("");
        }
    }

    private void openLicenses() {
        // TODO: 2017/2/22 打开Licenses窗口
        LicensesFragment licensesFragment = new LicensesFragment();
        licensesFragment.show(getSupportFragmentManager().beginTransaction(), "dialog_licenese");
    }

    public class MainAdapter extends FragmentPagerAdapter {

        private String[] titles = {"Record"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // TODO: 2017/2/22 显示录音Fragment
                    return RecordFragment.newInstance(position);
                case 1:
                    // TODO: 2017/2/22 显示保存录音文件的Fragment
                    return FileFragment.getInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
