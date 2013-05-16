package de.lenidh.android.holochron.adapters;

import com.actionbarsherlock.app.SherlockListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LapPagerAdapter extends FragmentStatePagerAdapter {
	
	private SherlockListFragment[] pages = {
			new SherlockListFragment(),
			new SherlockListFragment(),
	};

	public LapPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		return this.pages[index];
	}

	@Override
	public int getCount() {
		return this.pages.length;
	}

}
