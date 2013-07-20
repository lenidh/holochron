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

package de.lenidh.android.holochron.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import de.lenidh.android.holochron.App;

public class LapListFragment extends SherlockListFragment {
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView list = this.getListView();
		list.setDivider(null);
		list.setCacheColorHint(Color.TRANSPARENT);
		list.setFadingEdgeLength(App.convertToPx(8));
		list.setVerticalFadingEdgeEnabled(true);
		list.setSelector(android.R.color.transparent);
		list.setPadding(0, 0, 0, App.convertToPx(8));
		list.setClipToPadding(false);
	}
}