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
public class Appointment {
	private int appointmentId;
	private int customerId;
	private String title;
	private String description;
	private String contact;
	private String location;
	private String createdBy;
	private String lastUpdateBy;
	private String url;
	private Date start;
	private Date end;

	public Appointment() {

	}

	public Appointment(int appointmentId, int customerId, String createdBy, String title, String description, String contact, String location, String url, Date start, Date end) {
		this.appointmentId = appointmentId;
		this.customerId = customerId;
		this.createdBy = createdBy;
		this.title = title;
		this.description = description;
		this.contact = contact;
		this.location = location;
		this.url = url;
		this.start = start;
		this.end = end;
	}

	public Appointment(int appointmentId, int customerId,  String title, String description, String contact, String location, String url, Date start, Date end, String lastUpdateBy) {
		this.appointmentId = appointmentId;
		this.customerId = customerId;
		this.lastUpdateBy = lastUpdateBy;
		this.title = title;
		this.description = description;
		this.contact = contact;
		this.location = location;
		this.url = url;
		this.start = start;
		this.end = end;
	}

	public Appointment(int customerId, String createdBy, String title, String description, String contact, String location, String url, Date start, Date end) {
		this.customerId = customerId;
		this.createdBy = createdBy;
		this.title = title;
		this.description = description;
		this.contact = contact;
		this.location = location;
		this.url = url;
		this.start = start;
		this.end = end;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public String getLastUpdateBy() {
		return this.lastUpdateBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
