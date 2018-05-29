package com.example.nick.carbontracker.UI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;

import java.util.Calendar;

/**
 * This class displays notification at 9 pm. It covers 3 cases.
 * First is no journeys added today.
 * Second is journeys are added, but bills haven't been added at the last 1.5 months.
 * Last, when both have been added.
 * Created by ahad on 2017-04-04.
 */

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!isCorrectTime()) {
            Log.d("Notification","Rejected Alarm Manager Call at Time : " + Calendar.getInstance().getTimeInMillis());
            return;
        }
        int id  = 001;
        String promptArray[] = new String[]
                {context.getString(R.string.noJourneyTodayPrompt), // No Journeys
                 context.getString(R.string.noBillPrompt),  // No Bills
                 context.getString(R.string.defaultNotificationPrompt1) + " " +
                 Singleton.getInstance().numJourneysEnteredToday()
                 + " " + context.getString(R.string.defaultNotificationPrompt2)
                };
        // Create a Notification Builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.carbonn)
                        .setContentTitle(context.getString(R.string.notificationTitle))
                        .setContentText("Initialize me");

        int activityToDisplay = determineCase();
        Intent resultIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        switch(activityToDisplay){
            case 1:
            case 3:
                resultIntent = new Intent(context, TransportationModeActivity.class);
                stackBuilder.addParentStack(TransportationModeActivity.class);
                break;
            case 2:
                resultIntent = new Intent(context, AddBill.class);
                stackBuilder.addParentStack(AddBill.class);
                break;
            default:
                Log.d("Notification","Invalid Case Number");
                return;
        }
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(promptArray[activityToDisplay-1]));
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(id, mBuilder.build());
        Log.i("Notification","Displaying Notification");
    }

    private boolean isCorrectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return (hour == 21) && (minute == 0);
    }

    private int determineCase() {
        Boolean journeyNotEntered = !(Singleton.getInstance().numJourneysEnteredToday() > 0);
        Boolean billNotEntered = !Singleton.getInstance().utilityBillEnteredRecently();

        if (journeyNotEntered) return 1;
        else if (billNotEntered)  return 2;
        else return 3;
    }
}
