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

package lenidh.android.holochron.measurement;

import java.io.Serializable;

import android.os.SystemClock;

/**
 * Time measurement
 * 
 */
public class Timekeeper implements Serializable {
	
	private static final long serialVersionUID = 7253650292465609648L;
	
	private final LapManager _lapManager = new LapManager();
	
	private long _startStamp = 0;
	
	private long _elapsedTime = 0;
	
	/**
	 * Get the number of milliseconds elapsed during measurement.
	 * 
	 * @return elapsed time in milliseconds
	 */
	public long getElapsedTime() {
		long time = _elapsedTime;
		if (_startStamp > 0) {
			time += (SystemClock.elapsedRealtime() - _startStamp);
		}
		return time;
	}
	
	/**
	 * Get the {@link LapManager} of an {@link Timekeeper} object.
	 * 
	 * @return the object's {@link LapManager}
	 */
	public LapManager getLapManager() {
		return _lapManager;
	}
	
	/**
	 * Returns whether the time measurement is currently running
	 * 
	 * @return true if measurement is running, false otherwise.
	 */
	public boolean isRunning() {
		return (_startStamp > 0) ? true : false;
	}
	
	/**
	 * Add a new Lap to the {@link LapManager}.
	 */
	public void recordLap() {
		_lapManager.addLap(getElapsedTime());
	}
	
	/**
	 * Resets the time measurement. Next call of {@link #start()} will start a
	 * new measurement.
	 */
	public void reset() {
		_startStamp = 0;
		_elapsedTime = 0;
		_lapManager.clear();
	}
	
	/**
	 * On first call or if {@link #reset()} was called, a new measurement is
	 * started. Otherwise measurement is resumed.
	 */
	public void start() {
		if (_startStamp <= 0) {
			_startStamp = SystemClock.elapsedRealtime();
		}
	}
	
	/**
	 * Stop (pause) the time measurement.
	 */
	public void stop() {
		if (_startStamp > 0) {
			_elapsedTime +=  (SystemClock.elapsedRealtime() - _startStamp);
			_startStamp = 0;
		}
	}
	
}
