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

import lenidh.android.holochron.R;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.preference.ListPreference;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

/**
 * Einstellungsaktivität
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends SherlockPreferenceActivity implements
        OnSharedPreferenceChangeListener {
	
	public static final String TAG = "SettingsActivity";
	
	private ListPreference _lapAppearance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		_lapAppearance = (ListPreference) getPreferenceScreen()
		        .findPreference(getString(R.string.pref_key_lap_appearance));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this); // TODO
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "Activity State: onPause()");
		
		getPreferenceScreen().getSharedPreferences()
		        .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Activity State: onResume()");
		
		_lapAppearance.setSummary(_lapAppearance.getEntry());
		
		getPreferenceScreen().getSharedPreferences()
		        .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {
		if (key.equals(getString(R.string.pref_key_lap_appearance))) {
			_lapAppearance.setSummary(_lapAppearance.getEntry());
		}
	}
	
}
