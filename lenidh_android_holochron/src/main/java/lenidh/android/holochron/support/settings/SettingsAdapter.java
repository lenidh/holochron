package lenidh.android.holochron.support.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import lenidh.android.holochron.R;

public class SettingsAdapter extends ArrayAdapter<SettingsItem> {

	private LayoutInflater inflater;

	public SettingsAdapter(Context context) {
		super(context, R.layout.support_settings_listitem, SettingsItem.getSettings(context));

		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		// Reuse existing Views.
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = this.inflater.inflate(R.layout.support_settings_listitem, parent, false);
			assert convertView != null;

			holder = new ViewHolder();
			holder.titleView = (TextView) convertView.findViewById(R.id.txt_preference_title);
			holder.summaryView = (TextView) convertView.findViewById(R.id.txt_preference_summary);

			convertView.setTag(holder);
		}

		holder.titleView.setText(getItem(position).getTitle());
		holder.summaryView.setText(getItem(position).getEntry());

		return convertView;
	}

	private class ViewHolder {
		public TextView titleView;
		public TextView summaryView;
	}
}
