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

package lenidh.android.holochron.ui;

import lenidh.android.holochron.countdown.Countdown;
import lenidh.android.holochron.R;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Erstellt einen Countdownauswahldialog. Dabei wird, abhängig von der
 * SDK-Version des Systems, ein Layout zusammengestellt.
 * 
 * Ab Honeycomb wird für das Layout, des Dialogs, das {@link NumberPicker}
 * Widget verwendet.
 * 
 * Bei früheren Versionen wird statt des NumberPickers ein Sublayout, bestehend
 * aus einem {@link EditText} für den ausgewählten Wert und jeweils einem
 * {@link Button} für das erhöhen und verringern des Wertes, verwendet.
 */
public class CountdownPickerDialog extends DialogFragment {

	private OnCountdownSelectedListener _listener;

	/**
	 * Constructor
	 * 
	 * @param listener
	 */
	public CountdownPickerDialog(OnCountdownSelectedListener listener) {
		_listener = listener;
	}

	/**
	 * Verwaltet einen klassischen {@link NumberPicker}, wie er mit Honeycomb
	 * eingeführt wurde. Da diese Klasse auf Funktionen der APIv11 zugreift,
	 * wird es zum Wurf einer Ausnahme führen, sollte versucht werden ein Objekt
	 * auf älteren Versionen zu erstellen.
	 */
	@TargetApi(11)
	private class NormalPicker implements Picker {

		private NumberPicker _picker;
		private LinearLayout _wrapper;

		public NormalPicker() {
			NumberPicker.Formatter formatter = new NumberPicker.Formatter() {

				@Override
				public String format(int value) {
					return String.format("%02d", value);
				}
			};

			LinearLayout.LayoutParams pickerParams = new LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			_picker = new NumberPicker(getActivity());
			_picker.setMinValue(MIN_VALUE);
			_picker.setMaxValue(MAX_VALUE);
			_picker.setFormatter(formatter);
			_picker.setLayoutParams(pickerParams);

			LinearLayout.LayoutParams wrapperParams = new LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

			_wrapper = new LinearLayout(getActivity());
			_wrapper.setLayoutParams(wrapperParams);
			_wrapper.setGravity(Gravity.CENTER);
			_wrapper.addView(_picker);
		}

		@Override
		public int getValue() {
			return _picker.getValue();
		}

		@Override
		public View getView() {
			return _wrapper;
		}

		@Override
		public void setValue(int value) {
			_picker.setValue(value);
		}

	}

	/**
	 * Stellt implementierungsunabhängige Methoden für den Zugriff auf
	 * NumberPicker bereit.
	 */
	private interface Picker {
		public int getValue();

		public View getView();

		public void setValue(int value);
	}

	/**
	 * Ein NumberPicker, der SDK-Versionen vor Honeycomb unterstützt. Verwaltet
	 * die Widgets des Pickers und reagiert auf Benutzerinteraktionen.
	 */
	private class SupportPicker implements Picker {

		private int _value = 0;
		private Button _btnUp;
		private Button _btnDown;
		private EditText _inputBox;
		private LinearLayout _container;
		private LinearLayout _wrapper;

		public SupportPicker() {
			_inputBox = new EditText(getActivity());
			_inputBox.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			_inputBox.setGravity(Gravity.CENTER);
			_inputBox.setEms(2);
			_inputBox
					.setOnFocusChangeListener(new View.OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus == false) {
								try {
									int i = Integer.parseInt(_inputBox
											.getText().toString());
									if (i >= MIN_VALUE && i <= MAX_VALUE) {
										_value = i;
									}
								} catch (NumberFormatException e) {
								}
								updateInputBox();
							}
						}
					});

			_btnUp = new Button(getActivity());
			_btnUp.setText("+");
			_btnUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (_value < MAX_VALUE) {
						++_value;
					} else {
						_value = MIN_VALUE;
					}
					updateInputBox();
				}
			});

			_btnDown = new Button(getActivity());
			_btnDown.setText("-");
			_btnDown.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (_value > MIN_VALUE) {
						--_value;
					} else {
						_value = MAX_VALUE;
					}
					updateInputBox();
				}
			});

			updateInputBox();

			LinearLayout.LayoutParams containerParams = new LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			_container = new LinearLayout(getActivity());
			_container.setOrientation(LinearLayout.VERTICAL);
			_container.setLayoutParams(containerParams);
			_container.setGravity(Gravity.CENTER);
			_container.addView(_btnUp);
			_container.addView(_inputBox);
			_container.addView(_btnDown);

			LinearLayout.LayoutParams wrapperParams = new LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
			wrapperParams.setMargins(0, 16, 0, 16);

			_wrapper = new LinearLayout(getActivity());
			_wrapper.setLayoutParams(wrapperParams);
			_wrapper.setGravity(Gravity.CENTER);
			_wrapper.addView(_container);

		}

		@Override
		public int getValue() {
			return _value;
		}

		@Override
		public View getView() {
			return _wrapper;
		}

		@Override
		public void setValue(int value) {
			_value = value;
			updateInputBox();
		}

		private void updateInputBox() {
			_inputBox.setText(String.format("%02d", _value));
		}
	}

	private final int MAX_VALUE = 99;
	private final int MIN_VALUE = 0;

	private Picker _hourPicker;

	private Picker _minutePicker;

	private Picker _secondPicker;

	/**
	 * Setzt die Picker zu einem Gesamtlayout zusammen. Die Art der Picker wird
	 * abhängig von der SDK-Version gewählt.
	 * 
	 * @return das Layout
	 */
	private View createContent() {

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			createNormalPickers();
		} else {
			createSupportPickers();
		}

		_minutePicker.setValue(1);

		TextView sep1 = new TextView(getActivity());
		sep1.setText(R.string.seperator60);
		sep1.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Medium);

		TextView sep2 = new TextView(getActivity());
		sep2.setText(R.string.seperator60);
		sep2.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Medium);

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setGravity(Gravity.CENTER);
		layout.addView(_hourPicker.getView());
		layout.addView(sep1);
		layout.addView(_minutePicker.getView());
		layout.addView(sep2);
		layout.addView(_secondPicker.getView());

		return layout;
	}

	/**
	 * Instanziiert normale (Honeycomb und höher) Picker.
	 */
	private void createNormalPickers() {
		_hourPicker = new NormalPicker();
		_minutePicker = new NormalPicker();
		_secondPicker = new NormalPicker();
	}

	/**
	 * Instanziiert die zusammengestzten Picker, welche ältere SDK-Versionen
	 * unterstützen.
	 */
	private void createSupportPickers() {
		_hourPicker = new SupportPicker();
		_minutePicker = new SupportPicker();
		_secondPicker = new SupportPicker();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(createContent());
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Countdown countdown;
				if (_hourPicker.getValue() > 0 || _minutePicker.getValue() > 0
						|| _secondPicker.getValue() > 0) {
					countdown = new Countdown(_hourPicker.getValue(),
							_minutePicker.getValue(), _secondPicker.getValue());
				} else {
					countdown = new Countdown(60000);
				}
				_listener.updateCountdown(countdown);
			}
		});
		builder.setNegativeButton("Abbruch",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog = builder.create();

		return alertDialog;
	}

	public interface OnCountdownSelectedListener {
		public void updateCountdown(Countdown countdown);
	}

}
