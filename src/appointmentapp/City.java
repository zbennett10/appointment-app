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
public class City {
	private int cityId;
	private String city;
	private int countryId;
	private String createdBy;

	public City(int cityId, String city, int countryId, String createdBy) {
		this.cityId = cityId;
		this.city = city;
		this.countryId = countryId;
		this.createdBy = createdBy;
	}

	public City(String city, int countryId, String createdBy) {
		this.city = city;
		this.countryId = countryId;
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
}
