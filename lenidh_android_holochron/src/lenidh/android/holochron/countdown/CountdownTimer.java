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

package lenidh.android.holochron.countdown;

import lenidh.android.holochron.measurement.Timekeeper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;

public class CountdownTimer {

	private final static long ACCURACY = 1000;
	
	private Context _context;
	
	private AlarmManager _alarmManager;
	
	private PendingIntent _alarmIntent;
	
	private Countdown _countdown;
	
	private Timekeeper _timekeeper = new Timekeeper();
	
	public CountdownTimer(Context context, Countdown countdown) {
		_context = context;
		_countdown = countdown;
		_alarmManager = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		
	}
	
	public void start()
	{
		_timekeeper.start();
	}
	
	public void stop()
	{
		_timekeeper.stop();
	}
	
	public void reset()
	{
		_timekeeper.reset();
	}
	
	public boolean isRunning() {
		return (isFinished()) ? false : _timekeeper.isRunning();
	}
	
	public boolean isFinished() {
		return (getRemainingTime() > 0) ? false : true;
	}
	
	public long getRemainingTime() {
		long remainingTime = _countdown.getLength() - _timekeeper.getElapsedTime();
		return (remainingTime > 0) ? remainingTime : 0;
	}
}
