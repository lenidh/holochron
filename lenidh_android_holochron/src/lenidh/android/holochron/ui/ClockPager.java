package lenidh.android.holochron.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import lenidh.android.holochron.adapters.ClockPagerAdapter;
import lenidh.android.holochron.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;

public class ClockPager extends SherlockFragmentActivity {
	
	private ViewPager _viewPager;
	private ClockPagerAdapter _tabsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        
        _viewPager = new ViewPager(this);
        _viewPager.setId(R.id.pager);
        setContentView(_viewPager);
        //_viewPager = (ViewPager) findViewById(R.id.pager);
        
        final ActionBar bar = getSupportActionBar();
        // TODO: Solange der Countdown noch nicht fertig ist, werden keine Tabs
        //       benötigt.
        //bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        _tabsAdapter = new ClockPagerAdapter(this, _viewPager);
        _tabsAdapter.addTab(bar.newTab().setText("Stopp"), TimekeeperTabFragment.class, null);
        // TODO: Countdown ist noch nicht fertig
        //_tabsAdapter.addTab(bar.newTab().setText("Countdown"), CountdownTabFragment.class, null);
        
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        
        if(savedInstanceState != null) {
        	bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_clock_pager, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
    }

}
