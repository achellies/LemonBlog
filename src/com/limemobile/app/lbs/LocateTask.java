package com.limemobile.app.lbs;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

public class LocateTask {
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private DefaultHttpClient httpClient;
    private HttpContext httpContext;
    
    private static final int NETWORK_PROVIDER_MIN_TIME = 5 * 60 * 1000; // 5 minutes
    
    /** Maximum age of a GPS location to be considered current. */
    public static final long MAX_LOCATION_AGE_MS = 60 * 1000;  // 1 minute

    /** Maximum age of a network location to be considered current. */
    public static final long MAX_NETWORK_AGE_MS = 1000 * 60 * 10;  // 10 minutes
    
    public enum LocateType {
        APN_LOCATE,
        WIFI_LOCATE,
        GPS_LOCATE,
        UNKNOWN_LOCATE
    }
    
    private LocateType type = LocateType.UNKNOWN_LOCATE;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private AsyncTask<Object, Void, Void> fetcherTask;
    
    private OnLocationListener onLocationListener;
    
    private Handler handler;
    
    public LocateTask(Context context) {
        this.context = context;
        
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (onLocationListener != null)
                            onLocationListener.onError();
                        break;
                    case 1:
                        Bundle data = msg.getData();
                        if (onLocationListener != null && data != null) {
                            double latitude = data.getDouble("latitude");
                            double longitude = data.getDouble("longitude");
                            String location = data.getString("location");
                            onLocationListener.onSuccess(latitude, longitude, location);
                        }
                        break;
                }
                super.handleMessage(msg);
            }
            
        };
        init();
    }

    public void setLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }
    
    private void init() {
        boolean usingGPS = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                type = LocateType.APN_LOCATE;
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                type = LocateType.WIFI_LOCATE;
            else
                usingGPS = true;
        }
        
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (usingGPS && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            type = LocateType.GPS_LOCATE;
        
        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
                new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager tscm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        httpClient = new DefaultHttpClient(tscm, httpParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });
    }
    
    public void enableLocationSettings(Context context) {
        if (locationManager != null) {
            final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            final boolean networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gpsEnabled && !networkEnable) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(settingsIntent);
            }
        }
    }
    
    public void uninit() {
        if (type == LocateType.GPS_LOCATE && locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
        }
        
        final AsyncTask<Object, Void, Void> task = fetcherTask;
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
            fetcherTask = null;
        }
    }
    
    public void getLocation() {
        if (LocateType.UNKNOWN_LOCATE == type) {
            handler.obtainMessage(0).sendToTarget();
            return;
        }
        
        int typeNumber = -1;
        JSONObject object = new JSONObject();
        Location location = null;
        try {
            object.put("version", "1.1.0");
            object.put("host", "maps.google.com");
            object.put("address_language", "zh_CN");
            object.put("request_address", true);
            if (type == LocateType.GPS_LOCATE && locationManager != null) {
                typeNumber = 2;
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (!isLocationRecent(location)) {
                    // Try network location
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (!isLocationRecent(location)) {
                    // Try network location
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (!isLocationRecent(location) && locationListener == null) {
                        locationListener = new LocationListener() {
        
                            @Override
                            public void onLocationChanged(Location location) {
                            }
        
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
        
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
        
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                            
                        };
                        
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            long minTime = 5000;
                            float minDistance = 10.0f;
                            
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_FINE);
                            criteria.setAltitudeRequired(false);
                            criteria.setBearingRequired(false);
                            criteria.setCostAllowed(true);
                            criteria.setPowerRequirement(Criteria.POWER_LOW);
                            
                            String bestProvider = locationManager.getBestProvider(criteria, false);
                            locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, locationListener);
        
                            // Listen for network location
                            try {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, NETWORK_PROVIDER_MIN_TIME, 0, locationListener);
                            } catch (RuntimeException e) {
                                // Network location is optional, so just log the exception
                            }
                        }
                    }
                } else {
                    JSONObject data = new JSONObject();
                    data.put("latitude", location.getLatitude());
                    data.put("longitude", location.getLongitude());
                    object.put("location", data);
                }
            } else if (type == LocateType.WIFI_LOCATE) {
                typeNumber = 1;
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                
                if(wifiManager.getConnectionInfo().getBSSID() == null) {
                    throw new RuntimeException("bssid is null");
                }
                
                JSONArray array = new JSONArray();
                JSONObject data = new JSONObject();
                data.put("mac_address", wifiManager.getConnectionInfo().getBSSID());  
                data.put("signal_strength", 8);  
                data.put("age", 0);  
                array.put(data);
                object.put("wifi_towers", array);
            } else if (type == LocateType.APN_LOCATE) {
                typeNumber = 0;
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                
                int type = tm.getNetworkType();
                //在中国，移动的2G是EGDE，联通的2G为GPRS，电信的2G为CDMA，电信的3G为EVDO 

                //中国电信为CTC
                //NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
                //NETWORK_TYPE_CDMA电信2G是CDMA
                if (type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type ==TelephonyManager.NETWORK_TYPE_1xRTT)
                {
                    CdmaCellLocation cellLocation = (CdmaCellLocation) tm.getCellLocation();
                    int cellIDs = cellLocation.getBaseStationId();
                    int networkID = cellLocation.getNetworkId();
                    String mobileNetworkCode = Integer.toString(cellLocation.getSystemId());
                    String mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
                    
                    object.put("home_mobile_country_code", mobileCountryCode);
                    object.put("home_mobile_network_code", mobileNetworkCode);
                    object.put("radio_type", "cdma");
                    object.put("request_address", true);
                    if ("460".equals(tm.getNetworkOperator().substring(0, 3)))
                        object.put("address_language", "zh_CN");
                    else
                        object.put("address_language", "en_US");
                    JSONObject data, current_data;
                    JSONArray array = new JSONArray();
                    current_data = new JSONObject();
                    current_data.put("cell_id", cellIDs);
                    current_data.put("location_area_code", networkID);
                    current_data.put("mobile_country_code", mobileCountryCode);
                    current_data.put("mobile_network_code", mobileNetworkCode);
                    current_data.put("age", 0);
                    array.put(current_data);
                    object.put("cell_towers", array);
                }
                //移动2G卡 + CMCC + 2 
                //type = NETWORK_TYPE_EDGE
                else if(type == TelephonyManager.NETWORK_TYPE_EDGE)
                {
                    GsmCellLocation cellLocation = (GsmCellLocation)tm.getCellLocation();  
                    int cellIDs = cellLocation.getCid();  
                    int lac = cellLocation.getLac(); 

                    String mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);   
                    String mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
                    
                    object.put("home_mobile_country_code", mobileCountryCode);
                    object.put("home_mobile_network_code", mobileNetworkCode);
                    object.put("radio_type", "gsm");
                    object.put("request_address", true);
                    if ("460".equals(tm.getNetworkOperator().substring(0, 3)))
                        object.put("address_language", "zh_CN");
                    else
                        object.put("address_language", "en_US");
                    JSONObject data, current_data;
                    JSONArray array = new JSONArray();
                    current_data = new JSONObject();
                    current_data.put("cell_id", cellIDs);
                    current_data.put("location_area_code", lac);
                    current_data.put("mobile_country_code", mobileCountryCode);
                    current_data.put("mobile_network_code", mobileNetworkCode);
                    current_data.put("age", 0);
                    array.put(current_data);
                    object.put("cell_towers", array);
                }
                //联通的2G经过测试 China Unicom   1 NETWORK_TYPE_GPRS
                else if(type == TelephonyManager.NETWORK_TYPE_GPRS)
                {
                    GsmCellLocation cellLocation = (GsmCellLocation)tm.getCellLocation();  
                    int cellIDs = cellLocation.getCid();  
                    int lac = cellLocation.getLac(); 

                    //经过测试，获取联通数据以下两行必须去掉，否则会出现错误，错误类型为JSON Parsing Error
                    //info.mobileNetworkCode = tm.getNetworkOperator().substring(0, 3);   
                    //info.mobileCountryCode = tm.getNetworkOperator().substring(3);
                    String mobileNetworkCode = tm.getNetworkOperator().substring(0, 3);   
                    String mobileCountryCode = tm.getNetworkOperator().substring(3);
                    object.put("radio_type", "gsm");
                    object.put("request_address", true);
                    if ("460".equals(tm.getNetworkOperator().substring(0, 3)))
                        object.put("address_language", "zh_CN");
                    else
                        object.put("address_language", "en_US");
                    JSONObject data, current_data;
                    JSONArray array = new JSONArray();
                    current_data = new JSONObject();
                    current_data.put("cell_id", cellIDs);
                    current_data.put("location_area_code", lac);
                    current_data.put("age", 0);
                    array.put(current_data);
                    object.put("cell_towers", array);
                }
                else
                {
                    GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
                    int cid = gcl.getCid();
                    int lac = gcl.getLac();
                    int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,3));
                    int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,5));
                    
                    JSONArray array = new JSONArray();
                    JSONObject data = new JSONObject();
                    data.put("cell_id", cid);
                    data.put("location_area_code", lac);
                    data.put("mobile_country_code", mcc);
                    data.put("mobile_network_code", mnc);
                    array.put(data);
                    object.put("cell_towers", array);
                }
            }
            final AsyncTask<Object, Void, Void> task = fetcherTask;
            if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
                task.cancel(true);
                fetcherTask = null;
            }
            
            fetcherTask = new LocationTask().execute(httpClient, typeNumber, object, location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the last known location.
     */
    public Location getLastKnownLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (!isLocationRecent(location)) {
            // Try network location
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (isLocationRecent(location)) {
            } else {
            }
        }
        return location;
    }

    /**
     * Returns true if the location is recent.
     * 
     * @param location the location
     */
    private boolean isLocationRecent(Location location) {
        if (location == null) {
            return false;
        }
        return location.getTime() > System.currentTimeMillis() - MAX_LOCATION_AGE_MS;
    }
    
    private class LocationTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            DefaultHttpClient httpClient = (DefaultHttpClient) params[0];
            LocateType type = LocateType.UNKNOWN_LOCATE;
            int typeNumber = (Integer) params[1];
            JSONObject object = (JSONObject) params[2];
            Location location = (Location) params[3];
            
            switch (typeNumber) {
                case 0:
                    type = LocateType.APN_LOCATE;
                    break;
                case 1:
                    type = LocateType.WIFI_LOCATE;
                    break;
                case 2:
                    type = LocateType.GPS_LOCATE;
                    break;
            }
            
            HttpResponse response = null;
            if (object != null && type != LocateType.GPS_LOCATE) {
                HttpPost post = new HttpPost("http://www.google.com/loc/json");
                try {
                    if (type == LocateType.APN_LOCATE) {
                        String proxyHost = Proxy.getDefaultHost();
                        if(proxyHost != null) {
                            HttpHost proxy = new HttpHost(proxyHost, 80);
                            httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
                        }
                    }
                
                    StringEntity se = new StringEntity(object.toString());
                    post.setEntity(se);
                    response = httpClient.execute(post);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    post.abort();
                }
            }
            double longitude = 0.0;
            double latitude = 0.0;
            
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                BufferedReader br;
                try {
                    br = new BufferedReader(new InputStreamReader(entity.getContent()));
                    StringBuffer sb = new StringBuffer();
                    String result = br.readLine();
                    while (result != null) {
                        sb.append(result);
                        result = br.readLine();
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    JSONObject lca = json.getJSONObject("location");

                    String access_token = json.getString("access_token");
                    String accuracy;
                    String region;
                    String streetNumber;
                    String countryCode;
                    String city;
                    String country;
                    String street;
                    if (lca != null) {
                        if (lca.has("accuracy"))
                            accuracy = lca.getString("accuracy");
                        if (lca.has("longitude"))
                            longitude = lca.getDouble("longitude");
                        if (lca.has("latitude"))
                            latitude = lca.getDouble("latitude");
                        if (lca.has("address")) {
                            JSONObject address = lca.getJSONObject("address");
                            if (address != null) {
                                if (address.has("region"))
                                    region = address.getString("region");
                                if (address.has("street_number"))
                                    streetNumber = address.getString("street_number");
                                if (address.has("country_code"))
                                    countryCode = address.getString("country_code");
                                if (address.has("street"))
                                    street = address.getString("street");
                                if (address.has("city"))
                                    city = address.getString("city");
                                if (address.has("country"))
                                    country = address.getString("country");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    location = null;
                } finally {
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (type == LocateType.GPS_LOCATE && location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            } else
                handler.obtainMessage(0).sendToTarget();
            
            String resultString = "";

            String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", latitude, longitude);
            HttpGet get = new HttpGet(urlString);
            try {
                response = httpClient.execute(get);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                get.abort();
            }
            
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                try {
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
    
                    StringBuffer strBuff = new StringBuffer();
                    String result = null;
                    while ((result = buffReader.readLine()) != null) {
                        strBuff.append(result);
                    }
                    resultString = strBuff.toString();
                    if (resultString != null && resultString.length() > 0) {
                        JSONObject jsonobject = new JSONObject(resultString);
                        JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                        resultString = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            resultString = jsonArray.getJSONObject(i).getString("address");
                        }
                    }
                    Message msg = handler.obtainMessage(1);
                    Bundle data = new Bundle();
                    data.putDouble("latitude", latitude);
                    data.putDouble("longitude", longitude);
                    data.putString("location", resultString);
                    msg.setData(data);
                    msg.sendToTarget();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else
                handler.obtainMessage(0).sendToTarget();
            return null;
        }
        
    }
    
    private static class InflatingEntity extends HttpEntityWrapper {
        private GZIPInputStream mGZIP;

        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            mGZIP = new GZIPInputStream(wrappedEntity.getContent());
            return mGZIP;
        }

        @Override
        public long getContentLength() {
            return -1;
        }

        @Override
        public void consumeContent() throws IOException {
            super.consumeContent();
            if (mGZIP != null)
                mGZIP.close();
            wrappedEntity.consumeContent();
        }
    }

    public interface OnLocationListener {
        public void onError();
        
        public void onSuccess(double latitude, double longitude, String location);
    }
}
