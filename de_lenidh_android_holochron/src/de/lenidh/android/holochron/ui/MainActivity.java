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

package de.lenidh.android.holochron.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.lenidh.android.holochron.R;
import de.lenidh.android.holochron.adapters.LapArrayAdapter;
import de.lenidh.android.holochron.adapters.LapPagerAdapter;
import de.lenidh.android.holochron.controls.DigitalDisplay;
import de.lenidh.libzeitmesser.stopwatch.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SherlockFragmentActivity implements Display, SharedPreferences.OnSharedPreferenceChangeListener {
	
	private Watch watch;
	
	// Widgets
	private Button btnState;
	private Button btnExtra;
	private DigitalDisplay display;
	private ViewPager lapPager;
	
	private LapPagerAdapter lapPagerAdapter;

	private List<Lap> elapsedTimeItems;
	private List<Lap> lapTimeItems;
	private LapArrayAdapter elapsedTimeArrayAdapter;
	private LapArrayAdapter lapTimeArrayAdapter;
	private SherlockListFragment elapsedTimeListFragment;
	private SherlockListFragment lapTimeListFragment;
	private LapContainer lapContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get component references.
		this.btnState = (Button)this.findViewById(R.id.btnState);
		this.btnExtra = (Button)this.findViewById(R.id.btnExtra);
		this.display = (DigitalDisplay)this.findViewById(R.id.digitalDisplay1);
		
		this.lapPager = (ViewPager)this.findViewById(R.id.lapPager);
		this.lapPagerAdapter = new LapPagerAdapter(getSupportFragmentManager());
		this.lapPager.setAdapter(this.lapPagerAdapter);
		
		// Watch
		this.watch = new Watch(new SystemTime() {
			
			@Override
			public long getTime() {
				return SystemClock.elapsedRealtime();
			}
		});

		this.lapContainer = this.watch.getLapContainer();
		this.elapsedTimeItems = this.lapContainer.toList();
		this.lapTimeItems = this.lapContainer.toList(LapContainer.Order.lapTime);
		this.elapsedTimeArrayAdapter = new LapArrayAdapter(this, this.elapsedTimeItems, LapArrayAdapter.Mode.elapsedTime);
		this.lapTimeArrayAdapter = new LapArrayAdapter(this, this.lapTimeItems, LapArrayAdapter.Mode.lapTime);
		this.elapsedTimeListFragment = new LapListFragment();
		this.lapTimeListFragment = new LapListFragment();
		this.elapsedTimeListFragment.setListAdapter(this.elapsedTimeArrayAdapter);
		this.lapTimeListFragment.setListAdapter(this.lapTimeArrayAdapter);
		this.updatePages();
		
		// Button listener
		this.btnState.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(watch.isRunning()) {
					watch.stop();
				} else {
					watch.start();
				}
			}
		});
		this.btnExtra.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(watch.isRunning()) {
					watch.record();
				} else {
					watch.reset();
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();

		this.watch.removeDisplay(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.watch.addDisplay(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_settings:
				this.startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case R.id.menu_item_about:
				this.startActivity(new Intent(this, AboutActivity.class));
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void updateLaps() {
		this.lapTimeArrayAdapter.notifyDataSetChanged();
		this.elapsedTimeArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void updateState() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(watch.isRunning()) {
					btnState.setText(R.string.stop);
					btnExtra.setText(R.string.lap);
				} else {
					btnState.setText(R.string.start);
					btnExtra.setText(R.string.reset);
				}
			}
		});
	}

	@Override
	public void updateTime() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				display.setTime(watch.getElapsedTime());
			}
		});
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(this.getString(R.string.pref_key_lap_appearance).equals(key)) {
			this.updatePages();
		} else if(this.getString(R.string.pref_key_volume_buttons).equals(key)) {

		} else if(this.getString(R.string.pref_key_theme).equals(key)) {

		}
	}

	private void updatePages() {
		String prefValue;
		String prefValueAll = this.getString(R.string.pref_value_lap_appearance_all);
		String prefValueAbs = this.getString(R.string.pref_value_lap_appearance_abs);
		String prefValueLap = this.getString(R.string.pref_value_lap_appearance_lap);
		String prefKey = this.getString(R.string.pref_key_lap_appearance);
		ArrayList<SherlockListFragment> pages = new ArrayList<SherlockListFragment>();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefValue = prefs.getString(prefKey, prefValueAll);

		if(prefValue.equals(prefValueAll) || prefValue.equals(prefValueAbs)) {
			pages.add(this.elapsedTimeListFragment);
		}
		if(prefValue.equals(prefValueAll) || prefValue.equals(prefValueLap)) {
			pages.add(this.lapTimeListFragment);
		}

		this.lapPagerAdapter.setPages(pages);
		this.lapPagerAdapter.notifyDataSetChanged();
	}
}
