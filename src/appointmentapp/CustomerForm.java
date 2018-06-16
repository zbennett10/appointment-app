/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Zachary Bennett 
 * This class handles the Customer Form View
 */
public class CustomerForm {
	User user;
	Customer customer;
	Address customerAddress;
	City customerCity;
	Country customerCountry; 
	Stage primaryStage;	
	Scene scene;
	GridPane grid;
	Text sceneTitle;
	TextField customerNameInput;
	TextField customerAddressOneInput;
	TextField customerAddressTwoInput;
	TextField customerCityInput;
	TextField customerPostalCodeInput;
	TextField customerPhoneInput;
	TextField customerCountryInput;
	Button addCustomerBtn;
	Button updateCustomerBtn;
	Alert formError; 
	DatabaseQueryBank queryBank;

	public CustomerForm(Stage primaryStage, Customer customer, DatabaseQueryBank queryBank, User user) {
		this.user = user;
		this.primaryStage = primaryStage;	
		this.customer = customer;
		this.queryBank = queryBank;
		this.grid = new GridPane();
		this.scene = new Scene(this.grid, 800, 600);
		this.sceneTitle = new Text(customer == null ? "Add Customer" : "Customer: " + customer.getCustomerName());
		this.configureForm();

		/*
		* If there is no customer to update just configure a customer insertion form
		* otherwise configure update-specific logic 
		*/
		if(customer == null) {
			this.configureAddCustomerBtn();
		} else {
			this.customerAddress = (Address) this.queryBank.getAddress(customer.getAddressId());
			this.customerCity = (City) this.queryBank.getCity(this.customerAddress.getCityId());
			this.customerCountry = (Country) this.queryBank.getCountry(this.customerCity.getCountryId());
			this.setInitialFormValues();
			this.configureUpdateCustomerBtn();
		}
	}
	
	/*
	* This interface provides a foundational type in order to show alerts via lambda function.
	*/
	interface IFormError {
		void showFormAlert(String alert);
	}

	private final IFormError formAlertSetter = (message) -> {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.show();
	};
	
	private boolean isPostalCodeInvalid(String postalCode) {
		return !Pattern.compile("^[0-9]{5}(-[0-9]{4})?$", Pattern.CASE_INSENSITIVE)
			      .matcher(postalCode)
			      .find();
	}

	private boolean isPhoneInvalid(String phone) {
		return !Pattern.compile("1?\\W*([2-9][0-8][0-9])\\W*([2-9][0-9]{2})\\W*([0-9]{4})(\\se?x?t?(\\d*))?")
			      .matcher(phone)
			      .find();
	}

	private void setInitialFormValues() {
		this.customerNameInput.setText(this.customer.getCustomerName());
		this.customerAddressOneInput.setText(this.customerAddress.getAddress());
		this.customerAddressTwoInput.setText(this.customerAddress.getAddress2());
		this.customerCityInput.setText(this.customerCity.getCity());
		this.customerPostalCodeInput.setText(this.customerAddress.getPostalCode());
		this.customerPhoneInput.setText(this.customerAddress.getPhone());
		this.customerCountryInput.setText(this.customerCountry.getCountry());
	}
	
	/*
	* This function bootstraps the form and event listeners.
	*/
	private void configureForm() {
		Label nameLabel = new Label("Name: ");
		grid.add(nameLabel, 0, 1);
		customerNameInput = new TextField();
		grid.add(customerNameInput, 1, 1);

		Label addressOneLabel = new Label("Address One: ");
		grid.add(addressOneLabel, 0, 2);
		customerAddressOneInput = new TextField();
		grid.add(customerAddressOneInput, 1, 2);

		Label addressTwoLabel = new Label("Address Two: ");
		grid.add(addressTwoLabel, 0, 3);
		customerAddressTwoInput = new TextField();
		grid.add(customerAddressTwoInput, 1, 3);

		Label cityLabel = new Label("City: ");
		grid.add(cityLabel, 0, 4);
		customerCityInput = new TextField();
		grid.add(customerCityInput, 1, 4);

		Label postalLabel = new Label("Postal Code: ");
		grid.add(postalLabel, 0, 5);
		customerPostalCodeInput = new TextField();
		grid.add(customerPostalCodeInput, 1, 5);

		Label phoneLabel = new Label("Phone: ");
		grid.add(phoneLabel, 0, 6);
		customerPhoneInput = new TextField();
		grid.add(customerPhoneInput, 1, 6);

		Label countryLabel = new Label("Country: ");
		grid.add(countryLabel, 0, 7);
		customerCountryInput = new TextField();
		grid.add(customerCountryInput, 1, 7);
	}

	private void configureHeadline() {
		this.sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		this.grid.add(this.sceneTitle, 0, 0, 2, 1);
	}
	
	/*
	* This function bootstraps the button for updating customers.
	*/
	private void configureUpdateCustomerBtn() {
		updateCustomerBtn = new Button("Update Customer");
		updateCustomerBtn.setOnAction((ActionEvent e) -> {
			String newCustomerName = customerNameInput.getText();
			String addressOne = customerAddressOneInput.getText();
			String addressTwo = customerAddressTwoInput.getText();
			String newCityName = customerCityInput.getText();
			String postalCode = customerPostalCodeInput.getText();
			String phone = customerPhoneInput.getText();
			String newCountryName = customerCountryInput.getText();
			try {

				if(newCustomerName.isEmpty() || Util.isStringFieldInvalid(newCustomerName) || Util.isFieldOnlyWhitespace(newCustomerName)) {
					throw new InvalidCustomerDataException("Please enter a valid customer name.");
				} 
				if(addressOne.isEmpty() || addressTwo.isEmpty() ||Util.isFieldOnlyWhitespace(addressTwo) || Util.isFieldOnlyWhitespace(addressOne)) {
					throw new InvalidCustomerDataException("Please enter a valid address for both address fields.");
				}
				if(newCityName.isEmpty() || Util.isStringFieldInvalid(newCityName) || Util.isFieldOnlyWhitespace(newCityName)) {
					throw new InvalidCustomerDataException("Please enter a valid city.");
				}
				if(postalCode.isEmpty() || this.isPostalCodeInvalid(postalCode) || Util.isFieldOnlyWhitespace(postalCode)) {
					throw new InvalidCustomerDataException("Please enter a valid postal code.");
				}
				if(phone.isEmpty() || this.isPhoneInvalid(phone) || Util.isFieldOnlyWhitespace(phone)) {
					throw new InvalidCustomerDataException("Please enter a valid phone number.");
				}
				if(newCountryName.isEmpty() || Util.isStringFieldInvalid(newCountryName) || Util.isFieldOnlyWhitespace(newCountryName)) {
					throw new InvalidCustomerDataException("Please enter a valid country.");
				}

			} catch(InvalidCustomerDataException error) {
				formAlertSetter.showFormAlert(error.getMessage());
				return;	
			}
			String userName = this.user.getUserName();	
			///insert country and get id
			int countryId = this.customerCountry.getCountryId();
			Country newCountry = new Country(countryId, newCountryName, userName);
			this.queryBank.updateCountry(newCountry);
			//insert city and get id
			int cityId = this.customerCity.getCityId();
			City newCity = new City(cityId, newCityName, countryId , userName);
			this.queryBank.updateCity(newCity);
			//insert address and get id
			int addressId = this.customerAddress.getAddressId();
			Address newAddress = new Address(addressId, addressOne, addressTwo, cityId, postalCode, phone, userName);
			this.queryBank.updateAddress(newAddress);
			//insert customer
			this.customer.setCustomerName(newCustomerName);
			this.queryBank.updateCustomer(this.customer);
			UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
			homePage.render(false);
							
		});
		HBox updateBtnContainer = new HBox(10);
		updateBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		updateBtnContainer.getChildren().add(updateCustomerBtn);

		this.grid.add(updateBtnContainer, 0, 8);
	}
	
	/*
	* This function configures the button for inserting customers into the database.
	*/
	private void configureAddCustomerBtn() {
		addCustomerBtn = new Button("Add Customer");
		addCustomerBtn.setOnAction((ActionEvent e) -> {
			String customerName = customerNameInput.getText();
			String addressOne = customerAddressOneInput.getText();
			String addressTwo = customerAddressTwoInput.getText();
			String customerCity = customerCityInput.getText();
			String postalCode = customerPostalCodeInput.getText();
			String phone = customerPhoneInput.getText();
			String customerCountry = customerCountryInput.getText();
			
			try {
				if(customerName.isEmpty() || Util.isStringFieldInvalid(customerName) || Util.isFieldOnlyWhitespace(customerName)) {
					throw new InvalidCustomerDataException("Please enter a valid customer name.");
				} 
				if(addressOne.isEmpty() || addressTwo.isEmpty() ||Util.isFieldOnlyWhitespace(addressTwo) || Util.isFieldOnlyWhitespace(addressOne)) {
					throw new InvalidCustomerDataException("Please enter a valid address for both address fields.");
				}
				if(customerCity.isEmpty() || Util.isStringFieldInvalid(customerCity) || Util.isFieldOnlyWhitespace(customerCity)) {
					throw new InvalidCustomerDataException("Please enter a valid city.");
				}
				if(postalCode.isEmpty() || this.isPostalCodeInvalid(postalCode) || Util.isFieldOnlyWhitespace(postalCode)) {
					throw new InvalidCustomerDataException("Please enter a valid postal code.");
				}
				if(phone.isEmpty() || this.isPhoneInvalid(phone) || Util.isFieldOnlyWhitespace(phone)) {
					throw new InvalidCustomerDataException("Please enter a valid phone number.");
				}
				if(customerCountry.isEmpty() || Util.isStringFieldInvalid(customerCountry) || Util.isFieldOnlyWhitespace(customerCountry)) {
					throw new InvalidCustomerDataException("Please enter a valid country.");
				}

			} catch(InvalidCustomerDataException error) {
				formAlertSetter.showFormAlert(error.getMessage());
				return;
			}

			String userName = this.user.getUserName();	
			///insert country and get id
			Country newCountry = new Country(customerCountry, userName);
			int countryId = this.queryBank.insertCountry(newCountry);
			//insert city and get id
			City newCity = new City(customerCity, countryId, userName);
			int cityId = this.queryBank.insertCity(newCity);
			//insert address and get id
			Address newAddress = new Address(addressOne, addressTwo, cityId, postalCode, phone, userName);
			int addressId = this.queryBank.insertAddress(newAddress);
			//insert customer
			Customer newCustomer = new Customer(customerName, addressId, 1, userName);
			this.queryBank.insertCustomer(newCustomer);
			UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
			homePage.render(false);
							
		});
		HBox addBtnContainer = new HBox(10);
		addBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		addBtnContainer.getChildren().add(addCustomerBtn);

		this.grid.add(addBtnContainer, 0, 8);
	}

	private void configureBackBtn() {
		Button backBtn = new Button("Back");
		backBtn.setOnAction((ActionEvent e) -> {
			UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
			homePage.render(false);
		});

		HBox backBtnContainer = new HBox(10);
		backBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		backBtnContainer.getChildren().add(backBtn);

		this.grid.add(backBtnContainer, 1, 8);
	}

	public void render() {
		this.configureHeadline();
		this.configureBackBtn();
		this.primaryStage.setScene(this.scene);
		this.primaryStage.show();
	}
		
}
