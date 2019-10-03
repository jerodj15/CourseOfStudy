package com.example.trialattemptone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.example.trialattemptone.Creators.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private NotificationManager notificationManager;
    private PendingIntent alarmIntent;
    public static String NOTIFICATION;
    public static int alertID;
    public static String NOTIFICATION_ID;
    public static int alertHour;
    public static String alertDateString;
    public static Date alertDate;
    public int notificationId;
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Sets the receiver to enabled

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notificationId, notification);







        //Intent intent1 = new Intent(context, AlarmReceiver.class);
        //

        /*
        // Set the alarm to start at approximately 2:00 p.m.

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 5);
            Intent intent1 = new Intent(context, CourseDetailsScreen.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(context,100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        // If the alarm has been set, cancel it.
            if (alarmMgr!= null)
            {
                alarmMgr.cancel(alarmIntent);
             }




*/
    }

    public void testingNotification(Context context, int alertActive)
    {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        NOTIFICATION_ID = "1";

        if (alertActive == 0)
        {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("Content title")
                    .setContentText("Content text")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(activity);

            Notification notification = builder.build();

            Intent notificationIntent = new Intent(context, BootReceiver.class);
            notificationIntent.putExtra(BootReceiver.NOTIFICATION_ID, notificationId);
            notificationIntent.putExtra(BootReceiver.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId ,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 5);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        if (alertActive == 1)
        {
            Intent notificationIntent = new Intent(context, BootReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId ,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        }







    }

    public void scheduleNotification(Context context, Alert alert, String courseTitle)
    {

            // Sets the receiver to enabled
            ComponentName receiver = new ComponentName(context, BootReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            alertID = alert.getAlertID();
            NOTIFICATION_ID = String.valueOf(alertID);
            NOTIFICATION = alert.getTitle();
            alertDateString = alert.getDate();
            alertHour = alert.getHour();
            int alertActive = alert.getActive();
            int alerCourseID = alert.getCourseID();
            try
            {
                alertDate = sdf.parse(alertDateString);
            }catch (ParseException ex)
            {
                System.out.println(ex.getLocalizedMessage());
            }
            if (alertActive == 0)
            {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle(courseTitle)
                        .setContentText(NOTIFICATION)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(activity);

                Notification notification = builder.build();

                Intent notificationIntent = new Intent(context, BootReceiver.class);
                notificationIntent.putExtra(BootReceiver.NOTIFICATION_ID, notificationId);
                notificationIntent.putExtra(BootReceiver.NOTIFICATION, notification);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId ,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, alertHour);
                calendar.setTime(alertDate);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            if (alertActive == 1)
            {
                Intent notificationIntent = new Intent(context, BootReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId ,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

            }
        }



}
