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
import java.util.ArrayList;

/**
 * Lap management
 * 
 */
public class LapManager implements Serializable {
	
	private static final long serialVersionUID = -375136825599638230L;
	
	private ArrayList<Lap> _laps = new ArrayList<Lap>();
	
	private Lap _bestLap = null;
	
	private Lap _firstLap = null;
	
	private Lap _lastlap = null;
	
	/**
	 * Creates a new Lap.
	 * 
	 * @param time
	 *            to initialize the lap
	 */
	void addLap(long time) {
		Lap newLap = new Lap(this, time);
		if (_bestLap == null || _bestLap.getLapTime() > newLap.getLapTime()) {
			_bestLap = newLap;
		}
		if (_firstLap == null) {
			_firstLap = newLap;
		}
		_lastlap = newLap;
		_laps.add(newLap);
	}
	
	/**
	 * Deletes all Laps and resets the members.
	 */
	public void clear() {
		_laps.clear();
		_bestLap = null;
		_firstLap = null;
		_lastlap = null;
	}
	
	/**
	 * Returns the lap with shortest lap time
	 * 
	 * @return best lap
	 */
	public Lap getBestLap() {
		return _bestLap;
	}
	
	/**
	 * Returns the first created lap.
	 * 
	 * @return first created lap
	 */
	public Lap getFirstLap() {
		return _firstLap;
	}
	
	/**
	 * Returns the Laps.
	 * 
	 * @return laps as {@link ArrayList}
	 */
	public ArrayList<Lap> getLapList() {
		return _laps;
	}
	
	/**
	 * Returns the last created lap.
	 * 
	 * @return last created lap
	 */
	public Lap getLastLap() {
		return _lastlap;
	}
	
	/**
	 * Returns the number of Laps managed by this {@link LapManager}.
	 * 
	 * @return number of laps
	 */
	public int getNumberOfLaps() {
		return _laps.size();
	}
}
