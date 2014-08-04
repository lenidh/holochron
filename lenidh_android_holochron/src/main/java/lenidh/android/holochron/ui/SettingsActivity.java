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

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import lenidh.android.holochron.App;
import lenidh.android.holochron.R;

public class SettingsActivity extends ActionBarActivity {

	private static final String TAG = "SettingsActivity";

	public void onCreate(Bundle savedInstanceState) {
		// Theme needs to be selected before super.onCreate.
		if (App.getThemePreference().equals(getString(R.string.pref_value_theme_dark)) ||
				App.getThemePreference().equals(getString(R.string.pref_value_theme_classic))) {
			setTheme(R.style.AppTheme_Dark);
		}

		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			setFragment();
		} else {
			setSupportFragment();
		}
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setFragment() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(android.R.id.content, new SettingsFragment());
		transaction.commit();
	}

	private void setSupportFragment() {
		setContentView(R.layout.activity_support_settings);

		android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment, new lenidh.android.holochron.support.settings
				.SettingsFragment());
		transaction.commit();
	}
}