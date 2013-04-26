package de.lenidh.android.holochron2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DigitalDisplay extends LinearLayout {

	public DigitalDisplay(Context context) {
		super(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);
	}
	
	public DigitalDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);
	}

}
