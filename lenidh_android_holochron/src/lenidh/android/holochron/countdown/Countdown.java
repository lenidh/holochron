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

package lenidh.android.holochron.countdown;

import java.io.Serializable;

public class Countdown implements Serializable {
	
	private static final long serialVersionUID = -5767433868154618316L;
	
	private long _countdownLength = 0;
	
	public Countdown(int h, int m, int s) {
		_countdownLength += h * 3600 * 1000;
		_countdownLength += m * 60 * 1000;
		_countdownLength += s * 1000;
	}
	
	public Countdown(long time) {
		_countdownLength = time;
	}
	
	public int getHours() {
		return (int) (_countdownLength % (3600 * 1000));
	}
	
	public int getMinutes() {
		return (int) (_countdownLength % (60 * 1000));
	}
	
	public int getSeconds() {
		return (int) (_countdownLength % 1000);
	}
	
	public long getLength() {
		return _countdownLength;
	}

}
