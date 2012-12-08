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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lenidh.android.holochron.R;
import lenidh.android.holochron.adapters.LapArrayAdapter;
import lenidh.android.holochron.measurement.Lap;
import lenidh.android.holochron.measurement.LapManager;
import lenidh.android.holochron.measurement.Timekeeper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class TimekeeperTabFragment extends SherlockFragment implements
		OnSharedPreferenceChangeListener {

	public static final String TAG = "TimekeeperTabFragment";

	public static final long ACCURACY = 100;

	private Timekeeper _timekeeper;

	private Button _stateButton;

	private Button _extraButton;

	private ImageView[] _timekeeperDisplay = new ImageView[6];

	private Timer _timer = new Timer();

	private TimerTask _task = null;

	private int[] _digits = { R.drawable.digitaldigit0,
			R.drawable.digitaldigit1, R.drawable.digitaldigit2,
			R.drawable.digitaldigit3, R.drawable.digitaldigit4,
			R.drawable.digitaldigit5, R.drawable.digitaldigit6,
			R.drawable.digitaldigit7, R.drawable.digitaldigit8,
			R.drawable.digitaldigit9 };

	private int[] _smallDigits = { R.drawable.digitaldigit0_small,
			R.drawable.digitaldigit1_small, R.drawable.digitaldigit2_small,
			R.drawable.digitaldigit3_small, R.drawable.digitaldigit4_small,
			R.drawable.digitaldigit5_small, R.drawable.digitaldigit6_small,
			R.drawable.digitaldigit7_small, R.drawable.digitaldigit8_small,
			R.drawable.digitaldigit9_small };

	private ArrayList<Lap> _lapListItems;

	private ListView _lapList;

	private LapArrayAdapter _lapListAdapter;

	private LapManager _lapManager = null;

	private SharedPreferences _sharedPreference;
	private String _prefKeyVolumeButtons;
	private String _prefValueVolumeButtonsUse;
	private String _prefValueVolumeButtonsInverse;

	private void changeState() {
		if (_timekeeper.isRunning()) {
			Log.d(TAG, "stop measurement");
			_timekeeper.stop();
		} else {
			Log.d(TAG, "start measurement");
			_timekeeper.start();
		}
		updateState();
	}

	public void initLapList() {

		_lapManager = _timekeeper.getLapManager();
		_lapListItems = _lapManager.getLapList();

		_lapListAdapter = new LapArrayAdapter(getActivity(), _lapListItems);
		_lapList.setAdapter(_lapListAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "Fragment State: onActivityCreated()");

		// Suche die Views Ã¼ber ihre ID und speichere die referenzen
		_stateButton = (Button) getView().findViewById(R.id.startStopButton);
		_extraButton = (Button) getView().findViewById(R.id.resetLapButton);
		_timekeeperDisplay[0] = (ImageView) getView()
				.findViewById(R.id.hourImg);
		_timekeeperDisplay[1] = (ImageView) getView().findViewById(
				R.id.tenMinuteImg);
		_timekeeperDisplay[2] = (ImageView) getView().findViewById(
				R.id.minuteImg);
		_timekeeperDisplay[3] = (ImageView) getView().findViewById(
				R.id.tenSecondImg);
		_timekeeperDisplay[4] = (ImageView) getView().findViewById(
				R.id.secondImg);
		_timekeeperDisplay[5] = (ImageView) getView().findViewById(
				R.id.hundredthImg);
		_lapList = (ListView) getView().findViewById(R.id.lapList);

		if (savedInstanceState != null) {
			_timekeeper = (Timekeeper) savedInstanceState
					.getSerializable("timekeeperSnapshot");
		} else {
			_timekeeper = new Timekeeper();
		}
		initLapList();
		updateState();
		printTime();

		_stateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "_stateButton clicked");
				changeState();
			}
		});

		_extraButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "_extraButton clicked");
				resetOrRecord();
			}
		});
		
		// Referenzen für Einstellungsabfragen.
		_sharedPreference = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		_prefKeyVolumeButtons = getString(R.string.pref_key_volume_buttons);
		_prefValueVolumeButtonsUse = getString(R.string.pref_value_volume_buttons_use);
		_prefValueVolumeButtonsInverse = getString(R.string.pref_value_volume_buttons_inverse);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "Fragment State: onCreateView()");
		return inflater.inflate(R.layout.tab_timekeeper, container, false);
	}

	public void onLapClick() {
		_lapListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "Fragment State: onPause()");

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onResetClick() {
		_lapListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Fragment State: onResume()");

		_lapListAdapter.notifyDataSetChanged();

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "Fragment State: onSaveInstanceState()");

		outState.putSerializable("timekeeperSnapshot", _timekeeper);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(getString(R.string.pref_key_lap_appearance))) {
			_lapListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "Fragment State: onStart()");

		if (_timekeeper.isRunning())
			startTimerTask();
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "Fragment State: onStop()");

		stopTimerTask();
	}

	public boolean onVolumeDownDown() {
		String volumeKeySetting = _sharedPreference.getString(_prefKeyVolumeButtons, "");
		if (volumeKeySetting.equals(_prefValueVolumeButtonsUse)) {
			resetOrRecord();
			return true;
		}
		else if (volumeKeySetting.equals(_prefValueVolumeButtonsInverse)) {
			changeState();
			return true;
		}
		else {
			return false;
		}
	}

	public boolean onVolumeUpDown() {
		String volumeKeySetting = _sharedPreference.getString(_prefKeyVolumeButtons, "");
		if (volumeKeySetting.equals(_prefValueVolumeButtonsUse)) {
			changeState();
			return true;
		}
		else if (volumeKeySetting.equals(_prefValueVolumeButtonsInverse)) {
			resetOrRecord();
			return true;
		}
		else {
			return false;
		}
	}

	public void printTime() {
		long time = _timekeeper.getElapsedTime();

		_timekeeperDisplay[0]
				.setImageResource(_digits[(int) (time / 3600000 % 10)]);
		_timekeeperDisplay[1]
				.setImageResource(_digits[(int) (time / 600000 % 6)]);
		_timekeeperDisplay[2]
				.setImageResource(_digits[(int) (time / 60000 % 10)]);
		_timekeeperDisplay[3]
				.setImageResource(_digits[(int) (time / 10000 % 6)]);
		_timekeeperDisplay[4]
				.setImageResource(_digits[(int) (time / 1000 % 10)]);
		_timekeeperDisplay[5]
				.setImageResource(_smallDigits[(int) (time / 100 % 10)]);
	}

	private void resetOrRecord() {
		if (_timekeeper.isRunning()) {
			Log.d(TAG, "record lap");
			_timekeeper.recordLap();
			onLapClick();
		} else {
			Log.d(TAG, "reset time measurement");
			_timekeeper.reset();
			onResetClick();
			printTime();
		}
	}

	private void startTimerTask() {
		if (_task == null) {
			_task = new TimerTask() {

				@Override
				public void run() {
					updateTime();
				}
			};
			_timer.scheduleAtFixedRate(_task, 0, ACCURACY);
		}
	}

	private void stopTimerTask() {
		if (_task != null) {
			_task.cancel();
			_task = null;
		}
	}

	public void updateState() {
		if (_timekeeper.isRunning()) {
			_stateButton.setText(R.string.stop);
			_extraButton.setText(R.string.lap);
			startTimerTask();
		} else {
			_stateButton.setText(R.string.start);
			_extraButton.setText(R.string.reset);
			stopTimerTask();
		}
	}

	public void updateTime() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				printTime();
			}
		});
	}
}
