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

/**
 * Lap class
 * 
 */
public class Lap implements Serializable {
	
	private static final long serialVersionUID = 2535033769842130881L;
	
	private final LapManager _manger;
	
	private long _lapTime = 0;
	
	private long _absTime = 0;
	
	/**
	 * Constructor
	 * 
	 * @param manager
	 *            {@link LapManager} by witch the lap is created
	 * @param time
	 *            in milliseconds to initialize the lap
	 */
	public Lap(LapManager manager, long time) { // TODO: solve truncation
		_manger = manager;
		_absTime = time;
		if (_manger.getNumberOfLaps() > 0) {
			_lapTime = _absTime - _manger.getLastLap()._absTime;
		} else {
			_lapTime = _absTime;
		}
	}
	
	/**
	 * Returns the difference between absolute time of this lap and the first
	 * lap.
	 * 
	 * @return absolute time difference
	 */
	public long getAbsDiff() {
		return _absTime - _manger.getFirstLap()._absTime;
	}
	
	/**
	 * Returns the time since measurement start.
	 * 
	 * @return absolute lap time
	 */
	public long getAbsTime() {
		return _absTime;
	}
	
	/**
	 * Returns the difference between lap time of this lap and the best lap.
	 * 
	 * @return lap time difference
	 */
	public long getLapDiff() {
		return _lapTime - _manger.getBestLap()._lapTime;
	}
	
	/**
	 * Returns the lap time
	 * 
	 * @return lap time in milliseconds
	 */
	public long getLapTime() {
		return _lapTime;
	}
	
}
