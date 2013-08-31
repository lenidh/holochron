package lenidh.android.holochron.support.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import lenidh.android.holochron.R;

class SettingsItem {

	private Context context;

	private String title;

	private String key;

	private String defaultValue;

	private String[] values;

	private String[] entries;

	public static SettingsItem[] getSettings(Context context) {
		SettingsItem[] settings = {
				new SettingsItem(),
				new SettingsItem(),
		};

		settings[0].context = context;
		settings[0].title = context.getString(R.string.pref_theme);
		settings[0].key = context.getString(R.string.pref_key_theme);
		settings[0].defaultValue = context.getString(R.string.pref_value_theme_light);
		settings[0].values = new String[] {
				context.getString(R.string.pref_value_theme_light),
				context.getString(R.string.pref_value_theme_dark),
				context.getString(R.string.pref_value_theme_classic),
		};
		settings[0].entries = new String[] {
				context.getString(R.string.pref_theme_light),
				context.getString(R.string.pref_theme_dark),
				context.getString(R.string.pref_theme_classic),
		};

		settings[1].context = context;
		settings[1].title = context.getString(R.string.pref_volume_buttons);
		settings[1].key = context.getString(R.string.pref_key_volume_buttons);
		settings[1].defaultValue = context.getString(R.string.pref_value_volume_buttons_ignore);
		settings[1].values = new String[] {
				context.getString(R.string.pref_value_volume_buttons_use),
				context.getString(R.string.pref_value_volume_buttons_inverse),
				context.getString(R.string.pref_value_volume_buttons_ignore),
		};
		settings[1].entries = new String[] {
				context.getString(R.string.pref_volume_buttons_use),
				context.getString(R.string.pref_volume_buttons_inverse),
				context.getString(R.string.pref_volume_buttons_ignore),
		};

		return settings;
	}

	private SettingsItem() {
	}

	public String getKey() {
		return this.key;
	}

	public String getTitle() {
		return this.title;
	}

	public String[] getEntries() {
		return this.entries;
	}

	public String[] getValues() {
		return this.values;
	}

	public String getValue() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(this.key, this.defaultValue);
	}

	public String getEntry() {
		return getEntryForValue(getValue());
	}

	public int getValueIndex() {
		for(int i = 0; i < this.values.length; i++) {
			if(this.values[i].equals(getValue())) return i;
		}

		return -1;
	}

	private String getEntryForValue(String value) {
		for(int i = 0; i < this.values.length; i++) {
			if(i >= this.entries.length) break; // prevent overflow
			if(this.values[i].equals(value)) return this.entries[i];
		}

		return null;
	}
}
