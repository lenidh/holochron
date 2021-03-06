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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import lenidh.android.holochron.App;
import lenidh.android.holochron.R;

public class AboutActivity extends SherlockActivity {

	@SuppressWarnings("UnusedDeclaration")
	private static final String TAG = "AboutActivity";

	public void onCreate(Bundle savedInstanceState) {
		// Theme needs to be selected before super.onCreate.
		if (App.getThemePreference().equals(getString(R.string.pref_value_theme_dark))) {
			setTheme(R.style.AppTheme_Dark);
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView version = (TextView) findViewById(R.id.app_version_name);
		TextView thanksTo = (TextView) findViewById(R.id.thanksto);

		PackageManager pm = this.getPackageManager();
		try {
			assert pm != null;
			version.setText(pm.getPackageInfo(this.getPackageName(), 0).versionName);
			version.setVisibility(View.VISIBLE);
		} catch (PackageManager.NameNotFoundException e) {
			version.setVisibility(View.GONE);
		}

		thanksTo.setText(Html.fromHtml(getString(R.string.thanks_to)));
		thanksTo.setMovementMethod(LinkMovementMethod.getInstance());
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
}