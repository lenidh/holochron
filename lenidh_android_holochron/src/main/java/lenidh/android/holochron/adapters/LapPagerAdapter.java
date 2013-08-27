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

package lenidh.android.holochron.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import lenidh.android.holochron.App;
import lenidh.android.holochron.R;
import lenidh.android.holochron.ui.LapListFragment;

import java.util.ArrayList;

public class LapPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<LapListFragment> pages = null;

	public LapPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setPages(ArrayList<LapListFragment> pages) {
		this.pages = pages;
	}

	@Override
	public ListFragment getItem(int index) {
		return this.pages.get(index);
	}

	@Override
	public int getCount() {
		if (this.pages == null) return 0;
		return this.pages.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return App.getStringFromResource(R.string.elapsed_time);
			case 1:
				return App.getStringFromResource(R.string.lap_time);
		}
		return super.getPageTitle(position);
	}
}
