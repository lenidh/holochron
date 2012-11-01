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

package lenidh.android.holochron.adapters;

import java.util.ArrayList;

import lenidh.android.holochron.R;
import lenidh.android.holochron.measurement.Lap;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Befüllt die Rundenliste mit Einträgen.
 */
public class LapArrayAdapter extends ArrayAdapter<Lap> {

	/**
	 * Referenzen auf die Elemente der Listeneinträge.
	 */
	private static class ViewHolder {

		public TextView lapNumber;

		public TextView lapTime;

		public TextView lapDifference;

		public TextView absoluteTime;

		public TextView absoluteDifference;
	}

	private LayoutInflater _inflater;

	private SharedPreferences _sharedPreference;

	private String _prefKeyLapAppearance;

	private String _prefValueLapAppearanceLap;

	private String _prefValueLapAppearanceAbs;

	private String _strTimeFormat;

	private String _strBestLap;

	private String _strBestTime;

	private ArrayList<Lap> _values;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 *            Der aufrufende {@link Context}
	 * @param values
	 *            Die {@link ArrayList}, die der Adapter verwalten soll
	 */
	public LapArrayAdapter(Context context, ArrayList<Lap> values) {
		super(context, R.layout.lap_entry, values);
		_inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_values = values;

		// Referenzen für Einstellungsabfragen.
		_sharedPreference = PreferenceManager
				.getDefaultSharedPreferences(context);
		_prefKeyLapAppearance = context
				.getString(R.string.pref_key_lap_appearance);
		_prefValueLapAppearanceLap = context
				.getString(R.string.pref_value_lap_appearance_lap);
		_prefValueLapAppearanceAbs = context
				.getString(R.string.pref_value_lap_appearance_abs);

		_strTimeFormat = context.getString(R.string.timer_format);
		_strBestLap = context.getString(R.string.best_lap);
		_strBestTime = context.getString(R.string.best_time);
	}

	/**
	 * Liefert für die übergebene Zeit einen formatierten String zurück.
	 * 
	 * @param time
	 *            Die Zeit in Millisekunden.
	 * @return formatierter Zeitstring
	 */
	private String getTimeString(long time) {
		time += 50; // truncation tolerance
		byte hs = (byte) (time / 100 % 10);
		byte s = (byte) (time / 1000 % 60);
		byte m = (byte) (time / 60000 % 60);
		byte h = (byte) (time / 3600000 % 100);
		String timeString = String.format(_strTimeFormat, h, m, s, hs);
		return timeString;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.lap_entry, parent, false);

			holder = new ViewHolder();
			holder.lapNumber = (TextView) convertView
					.findViewById(R.id.lapNumber);
			holder.lapTime = (TextView) convertView.findViewById(R.id.lapTime);
			holder.lapDifference = (TextView) convertView
					.findViewById(R.id.lapDifference);
			holder.absoluteTime = (TextView) convertView
					.findViewById(R.id.absoluteTime);
			holder.absoluteDifference = (TextView) convertView
					.findViewById(R.id.absoluteDifference);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (_sharedPreference.getString(_prefKeyLapAppearance, "").equals(
				_prefValueLapAppearanceLap)) {
			holder.absoluteTime.setVisibility(View.GONE);
			holder.absoluteDifference.setVisibility(View.GONE);
			holder.lapTime.setVisibility(View.VISIBLE);
			holder.lapDifference.setVisibility(View.VISIBLE);
		} else if (_sharedPreference.getString(_prefKeyLapAppearance, "")
				.equals(_prefValueLapAppearanceAbs)) {
			holder.lapTime.setVisibility(View.GONE);
			holder.lapDifference.setVisibility(View.GONE);
			holder.absoluteTime.setVisibility(View.VISIBLE);
			holder.absoluteDifference.setVisibility(View.VISIBLE);
		} else {
			holder.lapTime.setVisibility(View.VISIBLE);
			holder.lapDifference.setVisibility(View.VISIBLE);
			holder.absoluteTime.setVisibility(View.VISIBLE);
			holder.absoluteDifference.setVisibility(View.VISIBLE);
		}

		holder.lapNumber.setText(String.format(" %d", position + 1));
		holder.lapTime
				.setText(getTimeString(_values.get(position).getLapTime()));
		holder.absoluteTime.setText(getTimeString(_values.get(position)
				.getAbsTime()));

		long diff = _values.get(position).getLapDiff();
		if (diff <= 0) {
			holder.lapDifference.setText(_strBestLap);
		} else {
			holder.lapDifference.setText("+" + getTimeString(diff));
		}

		diff = _values.get(position).getAbsDiff();
		if (diff <= 0) {
			holder.absoluteDifference.setText(_strBestTime);
		} else {
			holder.absoluteDifference.setText("+" + getTimeString(diff));
		}

		return convertView;
	}

}
