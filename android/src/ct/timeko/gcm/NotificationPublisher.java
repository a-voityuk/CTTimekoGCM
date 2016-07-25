package ct.timeko.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import org.appcelerator.titanium.util.TiRHelper;

import android.support.v4.app.NotificationCompat;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.appcelerator.kroll.common.Log;

public class NotificationPublisher extends BroadcastReceiver {

    private static final String TAG = "NotificationPublisher";
    private static final AtomicInteger id = new AtomicInteger(0);

    public static void createNotification(Context context, HashMap<String, Object> data) {
        int ntfId = id.getAndIncrement();

        Log.d(TAG, "Creating notification");
        
        Intent ntfIntent = new Intent(context, NotificationActivity.class);
        ntfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ntfIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ntfIntent.putExtra(TiGCMModule.NTF_KEY_DATA, data);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ntfId, ntfIntent, 0);

        int appIcon = 0;
        boolean empty = true;

        try {
            appIcon = TiRHelper.getApplicationResource("drawable.tray");
        } catch (TiRHelper.ResourceNotFoundException ex) {
            Log.e(TAG, "Resource drawable.tray not found, trying with drawable.appicon");
            try {
                appIcon = TiRHelper.getApplicationResource("drawable.appicon");
            } catch (TiRHelper.ResourceNotFoundException e) {
                Log.e(TAG, "Couldn't find drawable.appicon");
            }
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(appIcon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        
        int badge = 1;

        // Text
        try {
            String payloadString = data.get("payload").toString();
            HashMap<String, Object> payloadData = TiGCMModule.stringToHashMap(payloadString);
            String payloadAndroidString = payloadData.get("android").toString();
            HashMap<String, Object> payloadAndroidData = TiGCMModule.stringToHashMap(payloadAndroidString);

        	String badgeString = payloadAndroidData.get("badge").toString();
        	String alert = payloadAndroidData.get("alert").toString();
        	String title = payloadAndroidData.get("title").toString();
        	String userFullName = payloadData.get("userFullName").toString();
        	String type = payloadData.get("type").toString();
            String text = userFullName + ": " + alert;

            Log.d(TAG, "badgeString = " + badgeString);
            
            if (type.equals("4")) {
                String locale = context.getResources().getConfiguration().locale.getLanguage();
                String localizedText = "";

                try {
                	int resId = TiRHelper.getApplicationResource("string.NEW_FLOW_ANSWER_KEY");
                	localizedText = context.getResources().getString(resId);
                } catch (Exception e) {
                    Log.d(TAG, "Couldn't find string.NEW_FLOW_ANSWER_KEY");
                }

            	text = userFullName + " " + localizedText + " \"" + alert + "\"";
            }

            try {
            	badge = Integer.parseInt(badgeString);
                Log.d(TAG, "badge parsed = " + badge);
            } catch (Exception e) {
                Log.d(TAG, "Couldn't parse badge count");
            }

            Log.d(TAG, "text = " + text);
            notificationBuilder.setContentText(text);
            notificationBuilder.setContentTitle(title);
            empty = false;
        } catch (Exception e) {
            Log.d(TAG, "Couldn't set text");
        }

        if (!empty) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ntfId, notificationBuilder.build());
            
            try {
                Log.d(TAG, "badge = " + badge);
                TiGCMModule.getInstance().updateBadgeCount(badge);
            } catch (Exception e) {
                Log.d(TAG, "Couldn't set badge count");
            }
        } else {
            Log.d(TAG, "Wont create empty notification.");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        HashMap data = (HashMap)intent.getSerializableExtra(TiGCMModule.NTF_KEY_DATA);

        createNotification(context, data);
    }
}
