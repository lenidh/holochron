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

package lenidh.android.holochron.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.lenidh.libzeitmesser.stopwatch.Display;
import lenidh.android.holochron.App;
import lenidh.android.holochron.R;
import lenidh.android.holochron.adapters.ElapsedTimeLapAdapter;
import lenidh.android.holochron.adapters.LapAdapter;
import lenidh.android.holochron.adapters.LapPagerAdapter;
import lenidh.android.holochron.adapters.LapTimeLapAdapter;
import lenidh.android.holochron.controls.DigitalDisplay;
import lenidh.android.holochron.services.WatchService;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity
		implements Display, SharedPreferences.OnSharedPreferenceChangeListener, ViewPager.OnPageChangeListener {

	private Button btnState;
	private Button btnExtra;
	private DigitalDisplay display;
	private ViewPager lapPager;
	private LapAdapter elapsedTimeArrayAdapter;
	private LapAdapter lapTimeArrayAdapter;
	private LapListFragment lapTimeListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Theme needs to be selected before super.onCreate.
		if (App.getThemePreference().equals(getString(R.string.pref_value_theme_dark)) || App.getThemePreference().equals(getString(R.string.pref_value_theme_classic))) {
			setTheme(R.style.AppTheme_Dark);
		}

		super.onCreate(savedInstanceState);

		if (App.getThemePreference().equals(getString(R.string.pref_value_theme_classic))) {
			setContentView(R.layout.activity_main_classic);
		} else {
			setContentView(R.layout.activity_main);
		}



		/* state button */

		this.btnState = (Button) this.findViewById(R.id.btnState);
		this.btnState.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onStartStop();
			}
		});



		/* extra button */

		this.btnExtra = (Button) this.findViewById(R.id.btnExtra);
		this.btnExtra.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onResetRecord();
			}
		});

		// Configure dark theme.
		if (App.getThemePreference().equals(getString(R.string.pref_value_theme_dark))) {
			LinearLayout tile = (LinearLayout) this.findViewById(R.id.tile);
			View hView = this.findViewById(R.id.hSeparator);
			View vView = this.findViewById(R.id.vSeparator);
			View landSeparator = this.findViewById(R.id.landSeparator);

			tile.setBackgroundResource(R.drawable.tile_shape_dark);
			hView.setBackgroundResource(R.color.watch_button_separator_color_dark);
			vView.setBackgroundResource(R.color.watch_button_separator_color_dark);
			if (landSeparator != null) landSeparator.setBackgroundResource(android.R.color.white);
			this.btnState.setTextColor(getResources().getColor(android.R.color.white));
			this.btnExtra.setTextColor(getResources().getColor(android.R.color.white));
		}



		/* display */

		this.display = (DigitalDisplay) this.findViewById(R.id.digitalDisplay1);



		/* elapsed time Adapter */

		this.elapsedTimeArrayAdapter = new ElapsedTimeLapAdapter(this, App.getWatch().getLapContainer());



		/* lap time Adapter */

		LapTimeLapAdapter.SortOrder sortOrder = this.getLapTimeMode();
		this.lapTimeArrayAdapter = new LapTimeLapAdapter(this, App.getWatch().getLapContainer(), sortOrder);



		/* lap pages */

		// Find pager view.
		this.lapPager = (ViewPager) this.findViewById(R.id.lapPager);
		this.lapPager.setOnPageChangeListener(this);
		ArrayList<LapListFragment> pages = new ArrayList<LapListFragment>();

		// Check if page was already created and create page if necessary.
		LapListFragment elapsedTimeListFragment =(LapListFragment) getSupportFragmentManager().findFragmentByTag(
				"android:switcher:" + lapPager.getId() + ":0");
		if (elapsedTimeListFragment == null) elapsedTimeListFragment = new LapListFragment();
		elapsedTimeListFragment.setListAdapter(this.elapsedTimeArrayAdapter);
		pages.add(elapsedTimeListFragment);

		// Check if page was already created and create page if necessary.
		this.lapTimeListFragment = (LapListFragment) getSupportFragmentManager().findFragmentByTag(
				"android:switcher:" + lapPager.getId() + ":1");
		if (lapTimeListFragment == null) this.lapTimeListFragment = new LapListFragment();
		this.lapTimeListFragment.setListAdapter(this.lapTimeArrayAdapter);
		pages.add(this.lapTimeListFragment);

		// Create the page adapter.
		LapPagerAdapter lapPagerAdapter = new LapPagerAdapter(getSupportFragmentManager());
		lapPagerAdapter.setPages(pages);

		// Add page adapter to pager.
		this.lapPager.setAdapter(lapPagerAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		updateTime();
		updateLaps();
		updateState();
		App.getWatch().addDisplay(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);

		Intent intent = new Intent(this, WatchService.class);
		stopService(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.unregisterOnSharedPreferenceChangeListener(this);

		// Keep watch alive by running service.
		if(App.getWatch().getElapsedTime() > 0) {
			Intent intent = new Intent(this, WatchService.class);
			startService(intent);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		App.getWatch().removeDisplay(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem sortItem = menu.findItem(R.id.menu_item_order);

		if (this.lapPager.getCurrentItem() == 1 && App.getWatch().getLapContainer().size() >= 2) {
			sortItem.setVisible(true);

			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			boolean lapByNumberPref = preferences.getBoolean(this.getString(R.string.pref_key_lap_by_number), false);
			if (lapByNumberPref) {
				sortItem.setTitle(this.getString(R.string.pref_sort_by_lap_time));
			} else {
				sortItem.setTitle(this.getString(R.string.pref_sort_by_number));
			}
		} else {
			sortItem.setVisible(false);
		}

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
			case R.id.menu_item_order:
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				boolean lapByNumberPref = preferences.getBoolean(this.getString(R.string.pref_key_lap_by_number), false);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(this.getString(R.string.pref_key_lap_by_number), !lapByNumberPref);
				editor.commit();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		App.updateThemePreference();
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				this.onVolumeDown();
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				this.onVolumeUp();
				return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void updateTime() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				display.setTime(App.getWatch().getElapsedTime());
			}
		});
	}

	@Override
	public void updateState() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (App.getWatch().isRunning()) {
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
	public void updateLaps() {
		this.lapTimeArrayAdapter.notifyDataSetChanged();
		this.elapsedTimeArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (this.getString(R.string.pref_key_lap_by_number).equals(key)) {
			this.invalidateOptionsMenu();

			LapTimeLapAdapter.SortOrder sortOrder = this.getLapTimeMode();
			this.lapTimeArrayAdapter = new LapTimeLapAdapter(this, App.getWatch().getLapContainer(), sortOrder);
			this.lapTimeListFragment.setListAdapter(this.lapTimeArrayAdapter);
		}
	}

	private LapTimeLapAdapter.SortOrder getLapTimeMode() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean byNumber = preferences.getBoolean(this.getString(R.string.pref_key_lap_by_number), false);
		return (byNumber) ? LapTimeLapAdapter.SortOrder.SORT_BY_NUMBER : LapTimeLapAdapter.SortOrder.SORT_BY_TIME;
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
	}

	@Override
	public void onPageSelected(int i) {
		this.invalidateOptionsMenu();
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	private void onStartStop() {
		if (App.getWatch().isRunning()) {
			App.getWatch().stop();
		} else {
			App.getWatch().start();
		}
	}

	private void onResetRecord() {
		if (App.getWatch().isRunning()) {
			App.getWatch().record();
			if (App.getWatch().getLapContainer().size() == 2) invalidateOptionsMenu();
		} else {
			App.getWatch().reset();
			this.invalidateOptionsMenu();
		}
	}

	private void onVolumeDown() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String volumeKeyPref = preferences.getString(this.getString(R.string.pref_key_volume_buttons), this.getString(R.string.pref_value_volume_buttons_ignore));

		if (this.getString(R.string.pref_value_volume_buttons_use).equals(volumeKeyPref)) {
			this.onResetRecord();
		} else if (this.getString(R.string.pref_value_volume_buttons_inverse).equals(volumeKeyPref)) {
			this.onStartStop();
		}
	}

	private void onVolumeUp() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String volumeKeyPref = preferences.getString(this.getString(R.string.pref_key_volume_buttons), this.getString(R.string.pref_value_volume_buttons_ignore));

		if (this.getString(R.string.pref_value_volume_buttons_use).equals(volumeKeyPref)) {
			this.onStartStop();
		} else if (this.getString(R.string.pref_value_volume_buttons_inverse).equals(volumeKeyPref)) {
			this.onResetRecord();
		}
	}
}
