package de.lenidh.android.holochron.adapters;

import java.util.List;

import android.util.Log;
import de.lenidh.android.holochron.R;
import de.lenidh.libzeitmesser.stopwatch.Lap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LapArrayAdapter extends ArrayAdapter<Lap> {

	private static final String TAG = "LapArrayAdapter";
	
	private class ViewHolder {
		public TextView numberView;
		public TextView timeView;
		public TextView diffView;
	}

	public enum Mode {
		lapTime,
		elapsedTime,
	}

	private LayoutInflater inflater;
	private List<Lap> values;
	private Mode mode;

	public LapArrayAdapter(Context context, List<Lap> values) {
		this(context, values, Mode.elapsedTime);
	}

	public LapArrayAdapter(Context context, List<Lap> values, Mode mode) {
		super(context, R.layout.lap_listitem, values);
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.values = values;
		this.mode = mode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		// Reuse existing Views.
		if(convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = this.inflater.inflate(R.layout.lap_listitem, parent, false);
			
			holder = new ViewHolder();
			holder.numberView = (TextView)convertView.findViewById(R.id.txt_number);
			holder.timeView = (TextView)convertView.findViewById(R.id.txt_time);
			holder.diffView = (TextView)convertView.findViewById(R.id.txt_diff);
			
			convertView.setTag(holder);
		}

		holder.numberView.setText(Integer.toString(position));

		switch (this.mode) {
			case elapsedTime:
				holder.timeView.setText(this.formatTime(this.values.get(position).getElapsedTime(), false));
				holder.diffView.setText(this.formatTime(this.values.get(position).getElapsedTimeDiff(), true));
				break;
			case lapTime:
				holder.timeView.setText(this.formatTime(this.values.get(position).getLapTime(), false));
				holder.diffView.setText(this.formatTime(this.values.get(position).getLapTimeDiff(), true));
				break;
		}
		
		return convertView;
	}

	/**
	 * This method creates a formatted time string from a millisecond value.
	 * Format: [[[[[h]h:]m]m:]s]s.ms (e.g. 00:00:00.000)
	 * @param time in milliseconds
	 * @param trim If true, leading zeros and separators are trimmed. (e.g. 00:00:00.000 becomes 0.000)
	 * @return formatted time string
	 */
	private String formatTime(long time, boolean trim) {
		StringBuilder timeFormat = new StringBuilder();
		short digits[] = {
				(short) (time / 36000000 % 6),    // hours: tens
				(short) (time / 3600000 % 10),    //        ones
				-1,                               // separator: ':'
				(short) (time / 600000 % 6),      // minutes: tens
				(short) (time / 60000 % 10),      //          ones
				-1,                               // separator: ':'
				(short) (time / 10000 % 6),       // seconds: tens
				(short) (time / 1000 % 10),       //          ones
				-2,                               // separator: '.'
				(short) (time / 100 % 10),        // milliseconds: hundreds
				(short) (time / 10 % 10),         //               tens
				(short) (time % 10),              //               ones
		};
		int index = 0;

		// Skip leading zeros and separators.
		while (trim && index < 7 && digits[index] <= 0) index++;

		while (index < 12) {
			if(digits[index] >= 0) timeFormat.append(digits[index]);
			else if(digits[index] == -1) timeFormat.append(':');
			else if(digits[index] == -2) timeFormat.append('.');
			else Log.e(TAG, String.format("Invalid value while formatting: time: %d, index: %d, value: %d", time, index, digits[index]));
			index++;
		}

		return timeFormat.toString();
	}

}
