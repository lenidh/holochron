package lenidh.android.holochron.support.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import lenidh.android.holochron.App;
import lenidh.android.holochron.R;

public class SettingsFragment extends ListFragment
		implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = "SettingsFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SettingsAdapter adapter = new SettingsAdapter(getActivity());
		this.setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_support_settings, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();

		((SettingsAdapter)this.getListAdapter()).notifyDataSetChanged();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		preferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		preferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		SettingsItem item = (SettingsItem)listView.getItemAtPosition(position);
		if(item != null) {
			SettingsDialogFragment dialog = new SettingsDialogFragment(item);
			dialog.show(getFragmentManager(), item.getKey());
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		((SettingsAdapter)this.getListAdapter()).notifyDataSetChanged();

		if (key.equals(getString(R.string.pref_key_theme))) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.restart_message);
			builder.setTitle(R.string.restart);
			builder.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					PackageManager pm = getActivity().getPackageManager();
					assert pm != null;
					Intent i = pm.getLaunchIntentForPackage(getActivity().getPackageName());
					assert i != null;
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					App.updateThemePreference();
					startActivity(i);
				}
			});
			builder.setNegativeButton(R.string.restart_later, null);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}
