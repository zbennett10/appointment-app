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
public class Address {
	private int addressId;
	private String address;
	private String address2;
	private int cityId;
	private String postalCode;
	private String phone;
	private String createdBy;

	public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, String createdBy) {
		this.addressId = addressId;
		this.address = address;
		this.address2 = address2;
		this.cityId = cityId;
		this.postalCode = postalCode;
		this.phone = phone;
		this.createdBy = createdBy;
	}

	public Address(String address, String address2, int cityId, String postalCode, String phone, String createdBy) {
		this.address = address;
		this.address2 = address2;
		this.cityId = cityId;
		this.postalCode = postalCode;
		this.phone = phone;
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
