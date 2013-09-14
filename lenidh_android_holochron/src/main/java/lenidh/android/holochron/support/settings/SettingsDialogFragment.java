package lenidh.android.holochron.support.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;

import lenidh.android.holochron.R;

public class SettingsDialogFragment extends DialogFragment {

	private SettingsItem settingsItem;

	public SettingsDialogFragment(SettingsItem settingsItem)
	{
		this.settingsItem = settingsItem;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(this.settingsItem.getTitle());
		builder.setSingleChoiceItems(this.settingsItem.getEntries(),
				this.settingsItem.getValueIndex(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(settingsItem.getKey(), settingsItem.getValues()[i]);
				editor.commit();

				dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, null);

		return builder.create();
	}
}
