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

package de.lenidh.android.holochron.controls;

import de.lenidh.android.holochron.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DigitalDisplay extends LinearLayout {
	
	private int digits[] = {
			R.drawable.digitaldigit0,
			R.drawable.digitaldigit1,
			R.drawable.digitaldigit2,
			R.drawable.digitaldigit3,
			R.drawable.digitaldigit4,
			R.drawable.digitaldigit5,
			R.drawable.digitaldigit6,
			R.drawable.digitaldigit7,
			R.drawable.digitaldigit8,
			R.drawable.digitaldigit9,
	};

	private ImageView[] hours = new ImageView[2];
	private ImageView[] minutes = new ImageView[2];
	private ImageView[] seconds = new ImageView[2];
	
	public DigitalDisplay(Context context) {
		super(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);
		
		this.initComponents();
	}
	
	public DigitalDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);
		
		this.initComponents();
	}
	
	private void initComponents() {
		this.hours[0] = (ImageView)this.findViewById(R.id.hour1);
		this.hours[1] = (ImageView)this.findViewById(R.id.hour2);
		this.minutes[0] = (ImageView)this.findViewById(R.id.minute1);
		this.minutes[1] = (ImageView)this.findViewById(R.id.minute2);
		this.seconds[0] = (ImageView)this.findViewById(R.id.second1);
		this.seconds[1] = (ImageView)this.findViewById(R.id.second2);
		
		this.hours[0].setImageResource(this.digits[0]);
		this.hours[1].setImageResource(this.digits[0]);
		this.minutes[0].setImageResource(this.digits[0]);
		this.minutes[1].setImageResource(this.digits[0]);
		this.seconds[0].setImageResource(this.digits[0]);
		this.seconds[1].setImageResource(this.digits[0]);
	}
	
	public void setTime(long ms) {
		this.hours[0].setImageResource(this.digits[(int) (ms / 36000000 % 6)]);
		this.hours[1].setImageResource(this.digits[(int) (ms / 3600000 % 10)]);
		this.minutes[0].setImageResource(this.digits[(int) (ms / 600000 % 6)]);
		this.minutes[1].setImageResource(this.digits[(int) (ms / 60000 % 10)]);
		this.seconds[0].setImageResource(this.digits[(int) (ms / 10000 % 6)]);
		this.seconds[1].setImageResource(this.digits[(int) (ms / 1000 % 10)]);
	}

}