package lenidh.android.holochron.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ClockPagerAdapter extends FragmentPagerAdapter implements
		OnPageChangeListener, TabListener {
	
	private final Context _context;
    private final ActionBar _bar;
    private final ViewPager _pager;
    private final ArrayList<TabInfo> _tabs = new ArrayList<TabInfo>();
    
    static final class TabInfo {
        private final Class<?> _clss;
        private final Bundle _args;

        TabInfo(Class<?> clss, Bundle args) {
            _clss = clss;
            _args = args;
        }
    }

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
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Object tag = tab.getTag();
        for (int i=0; i<_tabs.size(); i++) {
            if (_tabs.get(i) == tag) {
            	if(_pager.getCurrentItem() != i) // Android Bug #29472
            		_pager.setCurrentItem(i);
            }
        }
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		_bar.setSelectedNavigationItem(position);
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = _tabs.get(position);
        return Fragment.instantiate(_context, info._clss.getName(), info._args);
	}

	@Override
	public int getCount() {
		return _tabs.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return _bar.getTabAt(position).getText();
	}
	
	

}
