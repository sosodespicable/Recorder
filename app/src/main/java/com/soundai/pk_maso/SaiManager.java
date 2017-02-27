package com.soundai.pk_maso;

import android.util.Log;

public class SaiManager {

	private static final String TAG = "SoundAi";
	
	private Sai_MA_JniMethods saiJniMethods;
	private static SaiManager saiManager;

	private SaiManager() {
		saiJniMethods = new Sai_MA_JniMethods();
	}
	
	public static SaiManager getInstance() {
		if (saiManager == null) {
			synchronized (SaiManager.class) {
				if (saiManager == null) {
					saiManager = new SaiManager();
				}
			}
		}
		return saiManager;
	}
	
	public int init(String path) {
		return saiJniMethods.SaiMicaInit(path);
	}
	
	public void record(final String fileName) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				saiJniMethods.SaiMicaStart(fileName);
			}
		}).start();
	}
	
	public void cancel() {
		saiJniMethods.SaiMicaCancel();
	}
	
	public void stop() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					saiJniMethods.SaiMicaCancel();
					Thread.sleep(1000);
					saiJniMethods.SaiMicaRelease();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	public int getVersion() {
		return saiJniMethods.SaiGetVersion();
	}
	
	public int getPermission(String deviceName) {
		return saiJniMethods.getDevicePermission(deviceName);
	}
	
	public int getVolume() {
		return saiJniMethods.getVoiceLevel();
	}
}
