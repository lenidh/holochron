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

package lenidh.android.holochron.adapters;

import android.content.Context;
import de.lenidh.libzeitmesser.stopwatch.Lap;
import de.lenidh.libzeitmesser.stopwatch.LapContainer;

import java.util.List;

public class LapTimeLapAdapter extends LapAdapter {

	private static List<Lap> getLapList(LapContainer container, SortOrder order) {
		switch (order) {
			case SORT_BY_NUMBER:
				return container.toList(LapContainer.SortOrder.SORT_BY_ELAPSED_TIME);
			case SORT_BY_TIME:
				return container.toList(LapContainer.SortOrder.SORT_BY_LAP_TIME);
			default:
				throw new Error("Unhandled SortOrder.");
		}
	}

	public LapTimeLapAdapter(Context context, LapContainer container, SortOrder order) {
		super(context, container, getLapList(container, order));
	}

	@Override
	protected long getTime(Lap lap) {
		return lap.getLapTime(10);
	}

	@Override
	protected long getTimeDiff(Lap lap) {
		return lap.getLapTimeDelta(10);
	}

	public enum SortOrder {
		SORT_BY_NUMBER,
		SORT_BY_TIME,
	}
}
