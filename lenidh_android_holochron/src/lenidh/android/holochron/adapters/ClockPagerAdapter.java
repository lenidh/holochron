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

package lenidh.android.holochron.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ClockPagerAdapter extends FragmentPagerAdapter implements
		OnPageChangeListener, TabListener {

	private static class TabInfo {
		private final Class<?> _class;
		private final Bundle _args;

		TabInfo(Class<?> clss, Bundle args) {
			_class = clss;
			_args = args;
		}
	}

	private static final String TAG = "ClockPagerAdapter";
	private final Context _context;
	private final ActionBar _bar;
	private final ViewPager _pager;

	private final ArrayList<TabInfo> _tabs = new ArrayList<TabInfo>();

	public ClockPagerAdapter(SherlockFragmentActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());

		_context = activity;
		_bar = activity.getSupportActionBar();
		_pager = pager;
		_pager.setAdapter(this);
		_pager.setOnPageChangeListener(this);
	}

	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
		TabInfo info = new TabInfo(clss, args);
		tab.setTag(info);
		tab.setTabListener(this);
		_tabs.add(info);
		_bar.addTab(tab);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return _tabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = _tabs.get(position);
		String className = info._class.getName();
		Log.d(TAG, "return instance of " + className);
		return Fragment.instantiate(_context, className, info._args);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return _bar.getTabAt(position).getText();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageSelected(int position) {
		_bar.setSelectedNavigationItem(position);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Object tag = tab.getTag();
		for (int i = 0; i < _tabs.size(); i++) {
			if (_tabs.get(i) == tag) {
				if (_pager.getCurrentItem() != i) // Android Bug #29472
					_pager.setCurrentItem(i);
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

}
