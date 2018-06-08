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
public class Country {
	private int countryId;
	private String country;
	private String createdBy;

	public Country(int countryId, String country, String createdBy) {
		this.countryId = countryId;
		this.country = country;
		this.createdBy = createdBy;
	}

	public Country(String country, String createdBy) {
		this.country = country;
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
