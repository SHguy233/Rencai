package com.example.godkiller.rencai.db;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.page.MessagePage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/5/6.
 */
public class PollingService extends Service{

    public static final String ACTION = "com.example.godkiller.rencai.db.PollingService";
    int count = 0;
    String username;
    private static  String url_query = "http://10.0.3.2:63342/htdocs/db/query_positions.php";
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();

    private Notification mNotification;
    private NotificationManager mManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initNotifiManager();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        username = intent.getStringExtra("username");
        if (username != null) {
            new PollingThread(username).start();
        }
    }

    //初始化通知栏配置
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.message;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "New Message";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    private void queryPosition() {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("username", username));
        JSONObject jsonObject = jsonParser.makeHttpRequest(url_query, "POST", pairs);
        Log.d("query", jsonObject.toString());
        try {
            int success = jsonObject.getInt(TAG_SUCCESS);
            if (success == 1) {
                showNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //弹出Notification
    private void showNotification() {
        mNotification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title
        Intent i = new Intent(this, MessagePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,0
                );
        mNotification.setLatestEventInfo(this,
                getResources().getString(R.string.app_name), "You have new message!", pendingIntent);
        mManager.notify(0, mNotification);
    }

    public class PollingThread extends Thread {
        private String username;

        public PollingThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
                System.out.println("Polling...");
                System.out.println(username);
                queryPosition();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }
}
