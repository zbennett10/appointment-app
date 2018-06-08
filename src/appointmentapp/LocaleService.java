/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author Zachary Bennett	 
 */

public class LocaleService {
	private String country;
	private String language;
	private Locale appLocale;
	private ResourceBundle loginFormBundle;

	private LocaleService() {
		Properties systemProperties = System.getProperties();
		String userLanguage = systemProperties.getProperty("user.language");
		String userCountry = systemProperties.getProperty("user.country");

		this.appLocale = new Locale(userLanguage, userCountry);
		this.country = userCountry;
		this.language = userLanguage;
		this.loginFormBundle = ResourceBundle.getBundle("appointmentapp.LoginAndRegister", this.appLocale);
	}

	public static LocaleService getInstance() {
		return LocaleServiceContainer.INSTANCE;
	}

	private static class LocaleServiceContainer {
		private static final LocaleService INSTANCE = new LocaleService();
	}

	public Locale getLocale() {
		return this.appLocale;
	}

	public String getUserLanguage() {
		return this.language;
	}

	public String getUserCountry() {
		return this.country;
	}

	public void setLocale(String language, String country) {
		this.appLocale = new Locale(language, country);
		this.language = this.appLocale.getLanguage();
		this.country = this.appLocale.getCountry();
		this.loginFormBundle = ResourceBundle.getBundle("appointmentapp.LoginAndRegister", this.appLocale);
	}

	public String getLocaleValueByKey(String key) {
		return this.loginFormBundle.getString(key);
	}

	public String getIncorrectUsernameOrPasswordMessage() {
		return this.loginFormBundle.getString("login_incorrectUP");
	}

	public String getEmptyUsernameOrPasswordMessage() {
		return this.loginFormBundle.getString("login_emptyUP");
	}

	public String getUsernameLabel() {
		return this.loginFormBundle.getString("login_username");
	}

	public String getPasswordLabel() {
		return this.loginFormBundle.getString("login_password");
	}

	public String getLoginLabel()  {
		return this.loginFormBundle.getString("login_login");
	}

	public String getRegistrationLabel() {
		return this.loginFormBundle.getString("login_register");
	}

	public String getExistingUserMessage() {
		return this.loginFormBundle.getString("register_existingUser");
	}

	public String getRegistrationFieldErrorMessage() {
		return this.loginFormBundle.getString("register_fieldError");
	}

	public String getPasswordConfirmationLabel() {
		return this.loginFormBundle.getString("register_confirm");
	}

	public String getSignupLabel() {
		return this.loginFormBundle.getString("register_signup");
	}

	public String getBackToLoginLabel() {
		return this.loginFormBundle.getString("register_back");
	}

}
