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
public class User {
	private int userId;
	private String password;
	private String userName;
	private int active;

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	

	public User(int userId, String userName, int active) {
		this.userId = userId;
		this.userName = userName;
		this.active = active;
	}

	public User(int userId, String password, String userName, int active) {
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.active = active;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setActive(int active) {
		this.active = active;
	}

	public int getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getPassword() {
		return this.password;
	}

	public int getActive() {
		return this.active;
	}
	
		
}
