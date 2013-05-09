package de.lenidh.android.holochron2.ui;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

import de.lenidh.android.holochron2.R;
import de.lenidh.android.holochron2.R.id;
import de.lenidh.android.holochron2.R.layout;
import de.lenidh.android.holochron2.adapters.LapArrayAdapter;
import de.lenidh.android.holochron2.adapters.LapPagerAdapter;
import de.lenidh.android.holochron2.controls.DigitalDisplay;
import de.lenidh.libzeitmesser.stopwatch.Display;
import de.lenidh.libzeitmesser.stopwatch.Lap;
import de.lenidh.libzeitmesser.stopwatch.LapContainer;
import de.lenidh.libzeitmesser.stopwatch.SystemTime;
import de.lenidh.libzeitmesser.stopwatch.Watch;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends SherlockFragmentActivity implements Display {
	
	private Watch watch;
	
	// Widgets
	private Button btnState;
	private Button btnExtra;
	private DigitalDisplay display;
	private ViewPager lapPager;
	
	private LapPagerAdapter lapPagerAdapter;
	
	private List<Lap> lapTimeItems;
	private List<Lap> elapsedTimeItems;
	private LapArrayAdapter lapTimeArrayAdapter;
	private LapArrayAdapter elapsedTimeArrayAdapter;
	private LapContainer lapContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get component references.
		this.btnState = (Button)this.findViewById(R.id.btnState);
		this.btnExtra = (Button)this.findViewById(R.id.btnExtra);
		this.display = (DigitalDisplay)this.findViewById(R.id.digitalDisplay1);
		
		this.lapPager = (ViewPager)this.findViewById(R.id.lapPager);
		this.lapPagerAdapter = new LapPagerAdapter(getSupportFragmentManager());
		this.lapPager.setAdapter(this.lapPagerAdapter);
		
		// Watch
		this.watch = new Watch(new SystemTime() {
			
			@Override
			public long getTime() {
				return SystemClock.elapsedRealtime();
			}
		});
		this.watch.addDisplay(this);

		this.lapContainer = this.watch.getLapContainer();
		this.lapTimeItems = this.lapContainer.toList();
		this.elapsedTimeItems = this.lapContainer.toList();
		this.lapTimeArrayAdapter = new LapArrayAdapter(this, this.lapTimeItems);
		this.elapsedTimeArrayAdapter = new LapArrayAdapter(this, this.elapsedTimeItems);
		((SherlockListFragment)this.lapPagerAdapter.getItem(0)).setListAdapter(this.lapTimeArrayAdapter);
		((SherlockListFragment)this.lapPagerAdapter.getItem(1)).setListAdapter(this.elapsedTimeArrayAdapter);
		
		// Button listener
		this.btnState.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(watch.isRunning()) {
					watch.stop();
				} else {
					watch.start();
				}
			}
		});
		this.btnExtra.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(watch.isRunning()) {
					watch.record();
				} else {
					watch.reset();
				}
			}
		});
	}

	@Override
	public void updateLaps() {
		this.lapTimeArrayAdapter.notifyDataSetChanged();
		this.elapsedTimeArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void updateState() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(watch.isRunning()) {
					btnState.setText(R.string.stop);
					btnExtra.setText(R.string.lap);
				} else {
					btnState.setText(R.string.start);
					btnExtra.setText(R.string.reset);
				}
			}
		});
	}

	@Override
	public void updateTime() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				display.setTime(watch.getElapsedTime());
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
