package de.lenidh.android.holochron2.adapters;

import java.util.ArrayList;
import java.util.List;

import de.lenidh.android.holochron2.R;
import de.lenidh.libzeitmesser.stopwatch.Lap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LapArrayAdapter extends ArrayAdapter<Lap> {
	
	private class ViewHolder {
		public TextView elapsedTimeView;
		public TextView elapsedTimeDiffView;
		public TextView lapTimeView;
		public TextView lapTimeDiffView;
	}

	private LayoutInflater inflater;
	private List<Lap> values;
	
	public LapArrayAdapter(Context context, List<Lap> values) {
		super(context, R.layout.lap_listitem, values);
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.values = values;
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
			holder.elapsedTimeView = (TextView)convertView.findViewById(R.id.lap_text);
			
			convertView.setTag(holder);
		}
		
		holder.elapsedTimeView.setText(String.valueOf(this.values.get(position).getElapsedTime()));
		
		return convertView;
	}

}
