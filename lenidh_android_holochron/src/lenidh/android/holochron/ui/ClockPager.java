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
import lenidh.android.holochron.adapters.ClockPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ClockPager extends SherlockFragmentActivity {

	private ViewPager _viewPager;
	private ClockPagerAdapter _tabsAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_viewPager = new ViewPager(this);
		_viewPager.setId(R.id.pager);
		setContentView(_viewPager);

		final ActionBar bar = getSupportActionBar();
		// TODO: Solange der Countdown noch nicht fertig ist, werden keine Tabs
		// benötigt.
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		_tabsAdapter = new ClockPagerAdapter(this, _viewPager);
		_tabsAdapter.addTab(bar.newTab().setText("Stopp"),
				TimekeeperTabFragment.class, null);
		// TODO: Countdown ist noch nicht fertig
		// _tabsAdapter.addTab(bar.newTab().setText("Countdown"),
		// CountdownTabFragment.class, null);

		PreferenceManager.setDefaultValues(this, R.xml.settings, false);

		// if(savedInstanceState != null) {
		// bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		// }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_clock_pager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.menu_item_about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Object currentFragment = _tabsAdapter.instantiateItem(_viewPager,
				_viewPager.getCurrentItem());

		switch (keyCode) {

		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (currentFragment.getClass() == TimekeeperTabFragment.class) {
				boolean result = ((TimekeeperTabFragment) currentFragment)
						.onVolumeDownDown();
				if (result)
					return true;
			}
			break;

		case KeyEvent.KEYCODE_VOLUME_UP:
			if (currentFragment.getClass() == TimekeeperTabFragment.class) {
				boolean result = ((TimekeeperTabFragment) currentFragment)
						.onVolumeUpDown();
				if (result)
					return true;
			}
			break;

		}

		return super.onKeyDown(keyCode, event);
	}

}
