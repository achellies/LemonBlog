
package com.limemobile.app.blog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.bean.Account;
import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.TencentWeibo;
import com.limemobile.app.utils.NetUtils;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LunchActivity extends ThemeActivity {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String OPEN_ID = "openid";
    public static final String OPEN_KEY = "openkey";
    public static final String NICK = "nick";
    public static final String NAME = "name";
    public static final String MSG = "msg";
    public static final String CLIENTIP = "clientip";

    private static final int SPLASH_TIME_INTERVAL = 1000;

    private boolean hasAuthorized = false;
    
    private AsyncTask<Void, Void, String> clientIPTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lunch);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String defValue = "";
        String accessToken = sp.getString(ACCESS_TOKEN, defValue);
        String expiresIn = sp.getString(EXPIRES_IN, defValue);
        String openid = sp.getString(OPEN_ID, defValue);
        String openkey = sp.getString(OPEN_KEY, defValue);
        String msg = sp.getString(MSG, defValue);
        String name = sp.getString(NAME, defValue);
        String nick = sp.getString(NICK, defValue);
        
        String clientIP = sp.getString(CLIENTIP, defValue);
        if (TextUtils.isEmpty(clientIP))
            clientIP = Constant.CLIENTIP;
        TencentWeibo.getInstance().setClientIP(clientIP);

        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(expiresIn)
                && !TextUtils.isEmpty(openid) && !TextUtils.isEmpty(openkey)
                && !TextUtils.isEmpty(msg) && !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(nick)) {
            hasAuthorized = true;

            TencentWeibo.getInstance().setMsg(msg);
            TencentWeibo.getInstance().setAccessToken(accessToken);
            TencentWeibo.getInstance().setExpiresIn(expiresIn);
            TencentWeibo.getInstance().setOpenid(openid);
            TencentWeibo.getInstance().setOpenkey(openkey);
        } else {
            ArrayList<Account> array = new ArrayList<Account>();
            Account.getAccountList(this, array);
            if (!array.isEmpty()) {
                for (Account object : array) {
                    if (object.active) {
                        AccountActivity.changeAccount(this, object, true);
                        hasAuthorized = true;
                        break;
                    }
                }
            }
        }
        if (!hasAuthorized) {
            autoLogin();
        } else {
            if (NetUtils.getType(this) == NetUtils.NO_NET) {
                new Handler(getMainLooper()).postDelayed(new Runnable() {
    
                    @Override
                    public void run() {
                        finish();
    
                        startActivity(new Intent(LunchActivity.this, MainActivity.class));
                    }
    
                }, SPLASH_TIME_INTERVAL);
            } else {
                clientIPTask = new getClientIP().execute();
            }
        }
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            final AsyncTask<Void, Void, String> task = clientIPTask;
            if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
                task.cancel(true);
                clientIPTask = null;
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void autoLogin() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void initViews() {
    }

    @Override
    public void themeChanged() {
    }
    
    private class getClientIP extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals(Constant.CLIENTIP))
                TencentWeibo.getInstance().setClientIP(result);
            finish();

            startActivity(new Intent(LunchActivity.this, MainActivity.class));
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            String clientIP = Constant.CLIENTIP;
            String string = GetNetIp("http://pv.sohu.com/cityjson?ie=utf-8");
            if (!TextUtils.isEmpty(string)) {
                String result = string.substring(string.indexOf("{"), string.lastIndexOf("}") + 1);
                try {
                    JSONObjectProxy object = new JSONObjectProxy(new JSONObject(result));
                    clientIP = object.getString("cip");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LunchActivity.this);
                Editor editor = sp.edit();
                editor.putString(CLIENTIP, clientIP);
                editor.commit();
            }
            return clientIP;
        }
        
        public String GetNetIp(String ipaddr) {
            URL infoUrl = null;
            InputStream inStream = null;
            try {
                infoUrl = new URL(ipaddr);
                URLConnection connection = infoUrl.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inStream, "utf-8"));
                    StringBuilder strber = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                        strber.append(line + "\n");
                    inStream.close();
                    return strber.toString();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "";
        }
    }
}
