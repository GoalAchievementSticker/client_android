package com.example.java_sticker.group;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.personal.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MaFIrebaseMessagingService extends FirebaseMessagingService {

    private String title;
    private String name;
    String channelId = "channelId";
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    /*푸시 알림 정보를 넘겨서 메소드로 넘긴다*/
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //목표 제목 읽기
        SharedPreferences preferences = getSharedPreferences("noti", MODE_PRIVATE);
        title = preferences.getString("noti_title", " ");

        name=preferences.getString("noti_name","");
        Log.d("FMS_title",title);

        sendNotification(title, remoteMessage.getNotification().getBody());
    }


    /* 푸시 알림 정보를 받아서 앱에서 알림을 띄워준다.*/

    private void sendNotification(String title, String body) {
//**add this line**
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, group_goal_click_detail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 알림 왔을때 사운드.

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_main_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /* 안드로이드 오레오 버전 대응 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chName = "ch name";
            NotificationChannel channel = new NotificationChannel(channelId, chName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, notiBuilder.build());


    }

}
