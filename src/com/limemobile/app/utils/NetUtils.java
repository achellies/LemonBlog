
package com.limemobile.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetUtils {

    private static final String TAG = NetUtils.class.getSimpleName();

    public static final int NO_NET = 2147483647;
    public static final int UNKNOWN = 2147483646;
    public static final int WIFI = 2147483645;
    public static final int ROAMING = 2147483644;

    public static int getType(Context context) {

        int result = NO_NET;

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
//                 if (netInfo.isRoaming()) {
//                	 result = ROAMING;
//                 }
                if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager tm = (TelephonyManager) context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    /**
                     * Returns a constant indicating the radio technology (network type)
                     * currently in use on the device for data transmission.
                     * @return the network type
                     *
                     * @see #NETWORK_TYPE_UNKNOWN
                     * @see #NETWORK_TYPE_GPRS
                     * @see #NETWORK_TYPE_EDGE
                     * @see #NETWORK_TYPE_UMTS
                     * @see #NETWORK_TYPE_HSDPA
                     * @see #NETWORK_TYPE_HSUPA
                     * @see #NETWORK_TYPE_HSPA
                     * @see #NETWORK_TYPE_CDMA
                     * @see #NETWORK_TYPE_EVDO_0
                     * @see #NETWORK_TYPE_EVDO_A
                     * @see #NETWORK_TYPE_EVDO_B
                     * @see #NETWORK_TYPE_1xRTT
                     * @see #NETWORK_TYPE_IDEN
                     * @see #NETWORK_TYPE_LTE
                     * @see #NETWORK_TYPE_EHRPD
                     * @see #NETWORK_TYPE_HSPAP
                     */
                    result = tm.getNetworkType();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    result = WIFI;
                } else {
                    result = UNKNOWN;
                }
            } else {
                Log.d(TAG, "netInfo.getType() == ConnectivityManager.NO_NET -->> ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getType() result toTypeName() -->> " + toTypeName(result));
        return result;
    }

    public static String toTypeName(int code) {
        switch (code) {
            case WIFI:
                return "WIFI";
            case NO_NET:
                return "NO_NET";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
                // case TelephonyManager.NETWORK_TYPE_HSDPA:
                // return "HSDPA";
                // case TelephonyManager.NETWORK_TYPE_HSUPA:
                // return "HSUPA";
                // case TelephonyManager.NETWORK_TYPE_HSPA:
                // return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            default:
                return "UNKNOWN";
        }
    }

    public static String getProxyHost(Context context) {
        if (getType(context) != TelephonyManager.NETWORK_TYPE_EDGE) {
            return null;
        }
        String defaultHost = android.net.Proxy.getDefaultHost();
        return defaultHost;
    }

}
