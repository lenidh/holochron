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

package lenidh.android.holochron.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import lenidh.android.holochron.App;
import lenidh.android.holochron.R;

public class DigitalDisplay extends LinearLayout {

	private final int[] digits_black = {
			R.drawable.digitaldigit0_black,
			R.drawable.digitaldigit1_black,
			R.drawable.digitaldigit2_black,
			R.drawable.digitaldigit3_black,
			R.drawable.digitaldigit4_black,
			R.drawable.digitaldigit5_black,
			R.drawable.digitaldigit6_black,
			R.drawable.digitaldigit7_black,
			R.drawable.digitaldigit8_black,
			R.drawable.digitaldigit9_black,
	};
	private final int[] digits_blue = {
			R.drawable.digitaldigit0_blue,
			R.drawable.digitaldigit1_blue,
			R.drawable.digitaldigit2_blue,
			R.drawable.digitaldigit3_blue,
			R.drawable.digitaldigit4_blue,
			R.drawable.digitaldigit5_blue,
			R.drawable.digitaldigit6_blue,
			R.drawable.digitaldigit7_blue,
			R.drawable.digitaldigit8_blue,
			R.drawable.digitaldigit9_blue,
	};
	private final ImageView[] hours = new ImageView[2];
	private final ImageView[] minutes = new ImageView[2];
	private final ImageView[] seconds = new ImageView[2];
	private final ImageView[] millis = new ImageView[2];
	private int[] digits;

	@SuppressWarnings("UnusedDeclaration")
	public DigitalDisplay(Context context) {
		super(context);

		if (App.getThemePreference().equals(context.getString(R.string.pref_value_theme_dark)) || App.getThemePreference().equals(context.getString(R.string.pref_value_theme_classic))) {
			this.digits = this.digits_blue;
		} else {
			this.digits = this.digits_black;
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);

		this.initComponents();
	}

	@SuppressWarnings("UnusedDeclaration")
	public DigitalDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.digitaldisplay, this, true);

		this.initComponents();
	}

	private void initComponents() {
		ImageView colon1 = (ImageView) this.findViewById(R.id.colon1);
		ImageView colon2 = (ImageView) this.findViewById(R.id.colon2);
		this.hours[0] = (ImageView) this.findViewById(R.id.hour1);
		this.hours[1] = (ImageView) this.findViewById(R.id.hour2);
		this.minutes[0] = (ImageView) this.findViewById(R.id.minute1);
		this.minutes[1] = (ImageView) this.findViewById(R.id.minute2);
		this.seconds[0] = (ImageView) this.findViewById(R.id.second1);
		this.seconds[1] = (ImageView) this.findViewById(R.id.second2);
		this.millis[0] = (ImageView) this.findViewById(R.id.milli1);
		this.millis[1] = (ImageView) this.findViewById(R.id.milli2);

		// Disable, if executed in development tools.
		if (!this.isInEditMode()) {
			int colonResource;
			Context context = this.getContext();
			assert context != null;
			if (App.getThemePreference().equals(context.getString(R.string.pref_value_theme_dark)) || App.getThemePreference().equals(context.getString(R.string.pref_value_theme_classic))) {
				colonResource = R.drawable.digitalcolon_blue;
				this.digits = this.digits_blue;
			} else {
				colonResource = R.drawable.digitalcolon_black;
				this.digits = this.digits_black;
			}

			colon1.setImageResource(colonResource);
			colon2.setImageResource(colonResource);
			this.hours[0].setImageResource(this.digits[0]);
			this.hours[1].setImageResource(this.digits[0]);
			this.minutes[0].setImageResource(this.digits[0]);
			this.minutes[1].setImageResource(this.digits[0]);
			this.seconds[0].setImageResource(this.digits[0]);
			this.seconds[1].setImageResource(this.digits[0]);
			this.millis[0].setImageResource(this.digits[0]);
			this.millis[1].setImageResource(this.digits[0]);
		}
	}

	public void setTime(long hs) {
		this.hours[0].setImageResource(this.digits[(int) (hs / 3600000 % 6)]);
		this.hours[1].setImageResource(this.digits[(int) (hs / 360000 % 10)]);
		this.minutes[0].setImageResource(this.digits[(int) (hs / 60000 % 6)]);
		this.minutes[1].setImageResource(this.digits[(int) (hs / 6000 % 10)]);
		this.seconds[0].setImageResource(this.digits[(int) (hs / 1000 % 6)]);
		this.seconds[1].setImageResource(this.digits[(int) (hs / 100 % 10)]);
		this.millis[0].setImageResource(this.digits[(int) (hs / 10 % 10)]);
		this.millis[1].setImageResource(this.digits[(int) (hs % 10)]);
	}

}
