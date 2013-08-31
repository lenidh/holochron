package lenidh.android.holochron.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

import lenidh.android.holochron.App;
import lenidh.android.holochron.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends PreferenceFragment
		implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = "SettingsFragment";
	private ListPreference theme;
	private ListPreference volumeButtons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		PreferenceScreen preferenceScreen = getPreferenceScreen();
		assert preferenceScreen != null;
		this.theme = (ListPreference) preferenceScreen.findPreference(getString(R.string.pref_key_theme));
		this.volumeButtons = (ListPreference) preferenceScreen.findPreference(getString(R.string.pref_key_volume_buttons));
	}

	@Override
	public void onResume() {
		super.onResume();

		this.theme.setSummary(this.theme.getEntry());
		this.volumeButtons.setSummary(this.volumeButtons.getEntry());

		PreferenceScreen preferenceScreen = getPreferenceScreen();
		assert preferenceScreen != null;
		SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
		assert sharedPreferences != null;
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		PreferenceScreen preferenceScreen = getPreferenceScreen();
		assert preferenceScreen != null;
		SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
		assert sharedPreferences != null;
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getString(R.string.pref_key_theme))) {
			this.theme.setSummary(this.theme.getEntry());
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
		} else if (key.equals(getString(R.string.pref_key_volume_buttons))) {
			this.volumeButtons.setSummary(this.volumeButtons.getEntry());
		} else {
			Log.w(TAG, "Unhandled preference change.");
		}
	}
}
