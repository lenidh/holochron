/**
 * Copyright (C) 2012 Moritz Heindl <lenidh[at]gmail[dot]com>
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

package lenidh.android.holochron.ui;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import lenidh.android.holochron.countdown.Countdown;
import lenidh.android.holochron.R;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CountdownTabFragment extends SherlockFragment implements
		CountdownPickerDialog.OnCountdownSelectedListener {

	public static final String TAG = "CountdownTabFragment";

	private Countdown _countdown = null;

	private Button _stateButton;

	private ImageView[] _countdownDisplay = new ImageView[6];

	private Timer _timer = new Timer();
	private TimerTask _countdownTask = null;
	private TimerTask _alarmTask = null;

	private Ringtone _ringtone = null;

	private int[] _digits = { R.drawable.digitaldigit0,
			R.drawable.digitaldigit1, R.drawable.digitaldigit2,
			R.drawable.digitaldigit3, R.drawable.digitaldigit4,
			R.drawable.digitaldigit5, R.drawable.digitaldigit6,
			R.drawable.digitaldigit7, R.drawable.digitaldigit8,
			R.drawable.digitaldigit9, };

	public CountdownTabFragment() {
		setHasOptionsMenu(true);
	}

	private void cancelAlarm() {
		if (_alarmTask != null) {
			_alarmTask.cancel();
			_alarmTask = null;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "Fragment State: onActivityCreated()");

		_stateButton = (Button) getView().findViewById(R.id.startStopButton);
		_countdownDisplay[0] = (ImageView) getView().findViewById(
				R.id.tenHourImg);
		_countdownDisplay[1] = (ImageView) getView().findViewById(R.id.hourImg);
		_countdownDisplay[2] = (ImageView) getView().findViewById(
				R.id.tenMinuteImg);
		_countdownDisplay[3] = (ImageView) getView().findViewById(
				R.id.minuteImg);
		_countdownDisplay[4] = (ImageView) getView().findViewById(
				R.id.tenSecondImg);
		_countdownDisplay[5] = (ImageView) getView().findViewById(
				R.id.secondImg);

		updateTime();
		updateState();

		_stateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "_startStopButton clicked");
//				if (_countdown.isRunning()) {
//					_countdown.stop();
//					cancelAlarm();
//				} else if (_countdown.isFinished()) {
//					_ringtone.stop();
//					_countdown.reset();
//					updateTime();
//				} else {
//					_countdown.start();
//					setAlarm();
//				}
//				updateState();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Fragment State: onCreate()");

		setRingtone();

		if (savedInstanceState != null) {
			_countdown = (Countdown) savedInstanceState
					.getSerializable("countdown");
//			if (_countdown.getRemainingTime() <= 0) {
//				_countdown.stop();
//			}
		} else {
			_countdown = new Countdown(0, 0, 5);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_countdown_tab, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "Fragment State: onCreateView()");
		return inflater.inflate(R.layout.tab_countdown, container, false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(getActivity(), SettingsActivity.class));
			return true;
		case R.id.menu_save_countdown:
			Toast t1 = Toast.makeText(getActivity(), "save item was clicked",
					Toast.LENGTH_SHORT);
			t1.show();
			return true;
		case R.id.menu_edit_countdown:
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			DialogFragment dialog = new CountdownPickerDialog(this);
			dialog.show(transaction, "");

			Toast t2 = Toast.makeText(getActivity(), "edit item was clicked",
					Toast.LENGTH_SHORT);
			t2.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Fragment State: onResume()");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "Fragment State: onSaveInstanceState()");

		outState.putSerializable("countdown", _countdown);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "Fragment State: onStart()");

		updateTime();

//		if (_countdown.isRunning())
//			startTimerTask();
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "Fragment State: onStop()");

		stopTimerTask();
	}

	private void setAlarm() {
		cancelAlarm();

		_alarmTask = new TimerTask() {

			@Override
			public void run() {

				if (_ringtone.isPlaying() == false)
					_ringtone.play();

				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						updateState();
					}
				});
			}
		};

//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.SECOND, (int) (_countdown.getRemainingTime()));
//
//		_timer.schedule(_alarmTask, cal.getTime());
	}

	private void setRingtone() {
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		_ringtone = RingtoneManager.getRingtone(getActivity(), uri);
	}

	private void startTimerTask() {
		if (_countdownTask == null) {
			_countdownTask = new TimerTask() {

				@Override
				public void run() {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							updateTime();
						}
					});
				}
			};
			_timer.scheduleAtFixedRate(_countdownTask, 0, 1000);
		}
	}

	private void stopTimerTask() {
		if (_countdownTask != null) {
			_countdownTask.cancel();
			_countdownTask = null;
		}
	}

	@Override
	public void updateCountdown(Countdown countdown) {
//		stopTimerTask();
//		_countdown.stop();
//		_countdown = countdown;
//		updateTime();
//		updateState();
	}

	public void updateState() {
//		if (_countdown.isRunning()) {
//			_stateButton.setText(R.string.stop);
//			startTimerTask();
//		} else if (_countdown.isFinished()) {
//			_stateButton.setText(R.string.reset);
//			stopTimerTask();
//		} else {
//			_stateButton.setText(R.string.start);
//			stopTimerTask();
//		}
	}

	public void updateTime() {
//		long time = _countdown.getRemainingTime();
//
//		if (time <= 0) {
//			_countdown.stop();
//			updateState();
//			stopTimerTask();
//		}
//
//		_countdownDisplay[0]
//				.setImageResource(_digits[(int) (time / 36000 % 10)]);
//		_countdownDisplay[1]
//				.setImageResource(_digits[(int) (time / 3600 % 10)]);
//		_countdownDisplay[2].setImageResource(_digits[(int) (time / 600 % 6)]);
//		_countdownDisplay[3].setImageResource(_digits[(int) (time / 60 % 10)]);
//		_countdownDisplay[4].setImageResource(_digits[(int) (time / 10 % 6)]);
//		_countdownDisplay[5].setImageResource(_digits[(int) (time / 1 % 10)]);
	}

}
