/*
 * Copyright (C) 2013 Moritz Heindl <lenidh[at]gmail[dot]com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package lenidh.android.holochron.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import lenidh.android.holochron.App;
import lenidh.android.holochron.R;
import lenidh.android.holochron.ui.MainActivity;
import de.lenidh.libzeitmesser.stopwatch.Watch;

public final class WatchService extends Service implements Watch.TimeChangedListener,
		Watch.StateChangedListener {

	private static final String TAG = "WatchService";

	private static final String ACTION_START = "lenidh.android.holochron.intent.action.START";

	private static final String ACTION_STOP = "lenidh.android.holochron.intent.action.STOP";

	private static final String ACTION_RECORD= "lenidh.android.holochron.intent.action.RECORD";

	private static final String ACTION_RESET = "lenidh.android.holochron.intent.action.RESET";

	private final int NOTIFICATION_ID = 1;

	private IntentFilter actionFilter;

	private PendingIntent pendingMainIntent;

	private NotificationManager notificationManager;

	private NotificationCompat.Builder runningNotificationBuilder;

	private NotificationCompat.Builder notRunningNotificationBuilder;

	private BroadcastReceiver actionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(ACTION_START.equals(intent.getAction())) {
				App.getWatch().start();
			} else if(ACTION_STOP.equals(intent.getAction())) {
				App.getWatch().stop();
			} else if(ACTION_RECORD.equals(intent.getAction())) {
				App.getWatch().record();
			} else if(ACTION_RESET.equals(intent.getAction())) {
				App.getWatch().reset();
				try {
					pendingMainIntent.send();
				} catch (PendingIntent.CanceledException e) {
					Log.w(TAG, "Sending mainIntent failed.");
				}
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		this.notificationManager
				= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent mainIntent = new Intent(this, MainActivity.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.pendingMainIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

		Intent startIntent = new Intent(ACTION_START);
		PendingIntent pendingStartIntent = PendingIntent.getBroadcast(this, 0, startIntent, 0);

		Intent stopIntent = new Intent(ACTION_STOP);
		PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

		Intent recordIntent = new Intent(ACTION_RECORD);
		PendingIntent pendingRecordIntent = PendingIntent.getBroadcast(this, 0, recordIntent, 0);

		Intent resetIntent = new Intent(ACTION_RESET);
		PendingIntent pendingResetIntent = PendingIntent.getBroadcast(this, 0, resetIntent, 0);

		this.actionFilter = new IntentFilter();
		this.actionFilter.addAction(ACTION_START);
		this.actionFilter.addAction(ACTION_STOP);
		this.actionFilter.addAction(ACTION_RECORD);
		this.actionFilter.addAction(ACTION_RESET);

		this.runningNotificationBuilder = new NotificationCompat.Builder(this)
				.setWhen(0)
				.setSmallIcon(R.drawable.ic_stat_notify_icon)
				.setContentIntent(pendingMainIntent)
				.addAction(R.drawable.ic_stat_notify_stop, this.getString(R.string.stop), pendingStopIntent)
				.addAction(R.drawable.ic_stat_notify_lap, this.getString(R.string.lap), pendingRecordIntent);

		this.notRunningNotificationBuilder = new NotificationCompat.Builder(this)
				.setWhen(0)
				.setSmallIcon(R.drawable.ic_stat_notify_icon)
				.setContentIntent(pendingMainIntent)
				.addAction(R.drawable.ic_stat_notify_start, this.getString(R.string.start), pendingStartIntent)
				.addAction(R.drawable.ic_stat_notify_reset, this.getString(R.string.reset), pendingResetIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		NotificationCompat.Builder builder = getNotificationBuilder();
		builder.setContentTitle(formatTime(App.getWatch().getElapsedTime()));
		startForeground(NOTIFICATION_ID, builder.build());

		registerReceiver(actionReceiver, this.actionFilter);

		App.getWatch().addTimeChangedListener(this, 1000);
		App.getWatch().addStateChangedListener(this);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		App.getWatch().removeStateChangedListener(this);
		App.getWatch().removeTimeChangedListener(this);
		unregisterReceiver(actionReceiver);
		notificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onTimeChanged() {
		updateNotification();
	}

	@Override
	public void onStateChanged() {
		updateNotification();
	}

	private void updateNotification() {
		NotificationCompat.Builder builder = getNotificationBuilder();
		builder.setContentTitle(formatTime(App.getWatch().getElapsedTime()));
		this.notificationManager.notify(NOTIFICATION_ID, builder.build());
	}

	private NotificationCompat.Builder getNotificationBuilder() {
		if(App.getWatch().isRunning()) {
			return this.runningNotificationBuilder;
		} else {
			return this.notRunningNotificationBuilder;
		}
	}

	private String formatTime(long time) {
		StringBuilder timeFormat = new StringBuilder();

		short digits[] = {
				(short) (time / 36000000 % 6),    // hours: tens
				(short) (time / 3600000 % 10),    //        ones
				-1,                              // separator: ':'
				(short) (time / 600000 % 6),      // minutes: tens
				(short) (time / 60000 % 10),      //          ones
				-1,                              // separator: ':'
				(short) (time / 10000 % 6),       // seconds: tens
				(short) (time / 1000 % 10),       //          ones
		};

		int index = 0;

		while (index < 8) {
			if (digits[index] >= 0) {
				timeFormat.append(digits[index]);
			} else if (digits[index] == -1) {
				timeFormat.append(':');
			} else if (digits[index] == -2) {
				timeFormat.append('.');
			} else {
				Log.e(TAG, String.format("Invalid value while formatting: time: %d, index: %d, value: %d",
						time, index, digits[index]));
			}
			index++;
		}

		return timeFormat.toString();
	}
}
