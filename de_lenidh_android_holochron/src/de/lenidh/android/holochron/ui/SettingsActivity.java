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
import android.preference.ListPreference;
import android.util.Log;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import de.lenidh.android.holochron.R;

@SuppressWarnings("deprecation")
public class SettingsActivity extends SherlockPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = "SettingsActivity";

	private ListPreference lapAppearence;
	private ListPreference theme;
	private ListPreference volumeButtons;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.lapAppearence = (ListPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_lap_appearance));
		this.theme = (ListPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_theme));
		this.volumeButtons = (ListPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_volume_buttons));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent home = new Intent(this, MainActivity.class);
				home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(home);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.lapAppearence.setSummary(this.lapAppearence.getEntry());
		this.theme.setSummary(this.theme.getEntry());
		this.volumeButtons.setSummary(this.volumeButtons.getEntry());

		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.equals(getString(R.string.pref_key_lap_appearance))) {
			this.lapAppearence.setSummary(this.lapAppearence.getEntry());
		} else if(key.equals(getString(R.string.pref_key_theme))) {
			this.theme.setSummary(this.theme.getEntry());
		} else if(key.equals(getString(R.string.pref_key_volume_buttons))) {
			this.volumeButtons.setSummary(this.volumeButtons.getEntry());
		} else {
			Log.w(TAG, "Unhandled preference change.");
		}
	}
}