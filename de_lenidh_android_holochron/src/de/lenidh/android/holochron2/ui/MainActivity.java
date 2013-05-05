package de.lenidh.android.holochron2.ui;

import com.actionbarsherlock.app.SherlockActivity;

import de.lenidh.android.holochron2.R;
import de.lenidh.android.holochron2.R.id;
import de.lenidh.android.holochron2.R.layout;
import de.lenidh.android.holochron2.controls.DigitalDisplay;
import de.lenidh.libzeitmesser.stopwatch.Display;
import de.lenidh.libzeitmesser.stopwatch.SystemTime;
import de.lenidh.libzeitmesser.stopwatch.Watch;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements Display {
	
	private Watch watch;
	
	private Button btnState;
	private Button btnExtra;
	private DigitalDisplay display;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get component references.
		this.btnState = (Button)this.findViewById(R.id.btnState);
		this.btnExtra = (Button)this.findViewById(R.id.btnExtra);
		this.display = (DigitalDisplay)this.findViewById(R.id.digitalDisplay1);
		
		// Watch
		this.watch = new Watch(new SystemTime() {
			
			@Override
			public long getTime() {
				return SystemClock.elapsedRealtime();
			}
		});
		this.watch.addDisplay(this);
		
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
					// TODO: Laps
				} else {
					watch.reset();
				}
			}
		});
	}

	@Override
	public void updateLaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateState() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(watch.isRunning()) {
					btnState.setText(R.string.stop);
				} else {
					btnState.setText(R.string.start);
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
