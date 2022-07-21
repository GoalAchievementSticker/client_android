package com.example.java_sticker.group;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.personal.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlinx.coroutines.Job;

/*메시지를 수신하여 알림으로 보여주는 클래스*/
//foreground,background 일 때 알림 제목이 다름 -> 수정하기..
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String title;
//    private String body = "";
    private String from = "";
    private String color = "";
    private int requestId;
    String body;
    // [START receive_message]
    //푸시메시지 수신시 할 작업

    String channelId = getString(R.string.default_notification_channel_id);
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("FMS", "From: " + remoteMessage.getFrom());

        //목표 제목 읽기
        SharedPreferences preferences =getSharedPreferences("noti",MODE_PRIVATE);
        title = preferences.getString("noti_title", " ");


//
//        try {
//            JSONObject jsonObject = new JSONObject(String.valueOf(remoteMessage));
//            JSONArray results = jsonObject.getJSONArray("to");
//            for (int i = 0; i < results.length(); i++) {
//                JSONObject jo = results.getJSONObject(i);
//               body = String.valueOf(jo.getJSONObject("body"));
//
//               // String username = body.optString("username");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.d("FMS", "noti_json_body: " +body); //알람테스트 ok

        //
        if (remoteMessage.getData().size() > 0) {
            Log.d("FMS", "msg data payload: " + remoteMessage.getData());

            from = remoteMessage.getFrom();

            body = remoteMessage.getNotification().getBody();
            //data 맵의 key 로 value 를 가져옴
            requestId = Integer.parseInt(Objects.requireNonNull(remoteMessage.getMessageId()));
            color = remoteMessage.getNotification().getColor();

            Log.d("FMS", "msg received: " + remoteMessage);
            Log.d("FMS", "from:" + from + "title:" + title + "body:" + body + "color:" + color + "data:" + requestId);

           //데이터가 긴 running작업이 필요하다면
            if (true) {
                //롱 러닝 작업동안 워크매니저 사용하기
                scheduleJob();
            } else {
                // 10초 이내의 짧은 작업은 바로 핸들링
                handleNow();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                sendNotification(remoteMessage.getData().toString());
            }


        }


        Log.d("FMS", "msg received: " + remoteMessage);
        Log.d("FMS", "from:" + from + "title:" + title + "body:" + body + "data:" + requestId);


        // 메시지가 notification payload를 포함한다면
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String getMessage = remoteMessage.getNotification().getBody();
            if (TextUtils.isEmpty(getMessage)) {
                Log.e(TAG, "ERR: Message data is empty...");
            } else {
                Map<String, String> mapMessage = new HashMap<>();
                mapMessage.put("key", getMessage);

                //fore_sendNotification(mapMessage);
                // Broadcast Data Sending Test
                Intent intent = new Intent("alert_data");
                intent.putExtra("msg", getMessage);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

            Intent intent = new Intent();
            intent.setAction("com.package.notification");
            sendBroadcast(intent);
        }
        //sendNotification(title, body, requestId,linkUrl);
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }


    //앱 실행 중
    private void fore_sendNotification(Map<String, String> data) {
        int noti_id = 1;
        String getMessage = "";

        Intent intent = new Intent(this, Group_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra("notification_id", 0);
        // Push로 받은 데이터를 그대로 다시 intent에 넣어준다.
        if (data != null && data.size() > 0) {
            for (String key : data.keySet()) {
                getMessage = data.get(key);
                intent.putExtra(key, getMessage);
            }
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0
                , intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_main_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification 채널을 설정합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(noti_id, notificationBuilder.build());

    }

    //back
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        String channelName = getString(R.string.default_notification_channel_name);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_main_round)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    //새로운 token이 생성될때마다 호출되는 callback
    //서버로 변경된 키값 전달
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }
    // [END on_new_token]


    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        Intent intent = new Intent(this, Group_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_main_round)
                .setContentTitle(title)
                .setContentText(token)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public static class MyWorker extends Worker {

        public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            // TODO(developer): add long running task here.
            return Result.success();
        }
    }

}


