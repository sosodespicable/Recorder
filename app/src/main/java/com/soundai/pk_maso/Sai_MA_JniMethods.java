package com.soundai.pk_maso;


public class Sai_MA_JniMethods {
   
	
    public native int SaiMicaInit(String filePath);
  
    public native void SaiMicaStart(String fileName);

    public native void SaiMicaCancel();

    public native void SaiMicaRelease();

    public native int SaiGetVersion();

    public native int getDevicePermission(String deviceName);

    public native int getVoiceLevel();
}
