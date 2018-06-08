/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

/**
 *
 * @author Zachary Bennett
 */
public class SnoozeIncrementType {
	private int incrementTypeId;
	private String incrementTypeDescription;	

	public SnoozeIncrementType(int incrementTypeId, String incrementTypeDescription) {
		this.incrementTypeId = incrementTypeId;
		this.incrementTypeDescription = incrementTypeDescription;
	}

	public int getIncrementTypeId() {
		return incrementTypeId;
	}

	public String getIncrementTypeDescription() {
		return incrementTypeDescription;
	}

	public void setIncrementTypeDescription(String incrementTypeDescription) {
		this.incrementTypeDescription = incrementTypeDescription;
	}
}
