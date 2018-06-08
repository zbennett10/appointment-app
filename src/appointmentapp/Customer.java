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
public class Customer {
	private int customerId;
	private String customerName;
	private String createdBy;
	private int addressId;
	private int active;

	public Customer(int customerId, String customerName, int addressId, int active, String createdBy) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.addressId = addressId;
		this.active = active;
		this.createdBy = createdBy;
	}

	public Customer(String customerName, int addressId, int active, String createdBy) {
		this.customerName = customerName;
		this.addressId = addressId;
		this.active = active;
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getAddressId() {
		return this.addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}
