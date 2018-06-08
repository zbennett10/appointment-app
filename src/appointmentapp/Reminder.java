/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.util.Date;

/**
 *
 * @author Zachary Bennett
 */
public class Reminder {
	private int reminderId;
	private Date reminderDate;	
	private int snoozeIncrement;
	private int snoozeIncrementTypeId;
	private int appointmentId;

	public Reminder(int reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId ) {
		this.reminderId = reminderId;
		this.reminderDate = reminderDate;
		this.snoozeIncrement = snoozeIncrement;
		this.snoozeIncrementTypeId = snoozeIncrementTypeId;
		this.appointmentId = appointmentId;
		
	}

	public Date getReminderDate() {
		return this.reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	public int getSnoozeIncrement() {
		return this.snoozeIncrement;
	}

	public void setSnoozeIncrement(int snoozeIncrement) {
		this.snoozeIncrement = snoozeIncrement;
	}

	public int getReminderId() {
		return this.reminderId;
	}

	public int getSnoozeIncrementTypeId() {
		return this.snoozeIncrementTypeId;
	}

	public int getAppointmentId() {
		return this.appointmentId;
	}
}
