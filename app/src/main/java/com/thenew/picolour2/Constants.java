package com.thenew.picolour2;

import java.util.UUID;

/**
 * Created by Олександр on 02.12.2017.
 */
public interface Constants  {
    int REQUEST_ENABLE_BT = 1;
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //String address = "B0:89:00:1C:B1:51";
    // MAC-адрес Bluetooth модуля
    String addresGalaxyA5 = "1C:23:2C:53:9C:A8";
    String addresHuaweiP8 = "B0:89:00:1C:B1:51";
    String addresWinPhone = "6C:27:79:9F:1C:90";
    String addresCube = "FC:A8:9A:00:26:90";
    String addresCube2 = "20:16:07:19:34:16";
    String addressrr = "20:16:07:05:94:01";

    String address = addresCube;


    //Shared Prederences
    String APP_PREFERENCES = "appsettings";
    String APP_PREFERENCES_NAME = "bluetoothadress";
    String APP_DEFAULT_NAME_VALUE="default";


    String TAG = "MyLog";
    String TAG_BT = "MyLogBT";

}