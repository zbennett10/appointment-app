/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

/**
 *
 * @author Zachary Bennett
 * This class bootstraps the JavaFX Desktop Application
 */
public class AppointmentApp extends Application {
	private Logger loginLogger;
	private DatabaseQueryBank queryBank = null;
	private LocaleService locationService;
	private Locale appLocale;
	private String currentAlertKey = null;

	private Label usernameLabel;
	private Label passwordLabel;
	private Label passwordConfirmationLabel;
	private Label alertLabel;
	private Button loginBtn;
	private Button registerBtn;
	private Button signupBtn;
	private Button backToLoginBtn;
	
	/*
	* This interface is used to expose a lambda function that sets alerts.
	*/	
	interface IAlert {
		void setAlertText(String alert);
	}

	private final IAlert alertSetter = (alert) -> alertLabel.setText(alert);
    
   @Override
    public void start(Stage primaryStage) {
	// In order to test the changing of languages of the login page, uncomment the line below 
	// Locale.setDefault(new Locale("fr", "FR")); 
	
	locationService = LocaleService.getInstance();
	appLocale = locationService.getLocale();
	initiateLogger();
	this.queryBank = new DatabaseQueryBank(); 
	renderLoginForm(primaryStage);
    }
	
    /*
    * This function writes a login timestamp out to the login.txt file every time a user logs in.
    */
    private void initiateLogger() {
	this.loginLogger = Logger.getLogger("LoginLog");  
	FileHandler fh;  
	try {  
		fh = new FileHandler("./login.txt", true);  
		this.loginLogger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();  
		fh.setFormatter(formatter);  

	} catch (Exception e) {System.out.println(e.getMessage());}
    }
	
    /*
    * Bootstraps the view/event listeners for the Login Page
    */
    private void renderLoginForm(Stage primaryStage) {
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Appointment App");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	grid.add(scenetitle, 0, 0, 2, 1);

	usernameLabel = new Label(locationService.getLoginLabel() + ":");
	grid.add(usernameLabel, 0, 1);

	TextField userNameTextField = new TextField();
	grid.add(userNameTextField, 1, 1);

	passwordLabel = new Label(locationService.getPasswordLabel() + ":");
	grid.add(passwordLabel, 0, 2);

	PasswordField passwordInput = new PasswordField();
	grid.add(passwordInput, 1, 2);

	alertLabel = new Label("");
	alertLabel.setTextFill(Color.web("#ff0000"));
	HBox alertContainer = new HBox(10);
	alertContainer.setAlignment(Pos.CENTER_LEFT);
	alertContainer.getChildren().add(alertLabel);
	grid.add(alertContainer, 0, 5);
	grid.setColumnSpan(alertContainer, GridPane.REMAINING);

	loginBtn = new Button(locationService.getLoginLabel());
	loginBtn.setOnAction((ActionEvent e) -> {
		String userName = userNameTextField.getText();
		String password = passwordInput.getText();
		try {
			if(userName.isEmpty() != true && password.isEmpty() != true) {
				ArrayList<String> queryObj = new ArrayList<>();
				queryObj.add(userName);
				queryObj.add(password);
				User user = (User) queryBank.getUserByUsernameAndPassword(queryObj);
				System.out.println(user);
				if(user == null) {
					throw new InvalidLoginException(locationService.getIncorrectUsernameOrPasswordMessage());
				} else {
					//handle successful login
					this.loginLogger.log(Level.INFO, "{0} logged in.", userName);
					UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
					homePage.render(true);
				}
			} else {
				throw new InvalidLoginException(locationService.getEmptyUsernameOrPasswordMessage());
			}
		} catch(InvalidLoginException error) {
			String errorMessage = error.getMessage();
			alertSetter.setAlertText(errorMessage);
			if(errorMessage.equals(locationService.getIncorrectUsernameOrPasswordMessage())) {
				currentAlertKey = "login_incorrectUP";	
			} else {
				currentAlertKey = "login_emptyUP";
			}
		}
	});

	registerBtn = new Button(locationService.getRegistrationLabel());

	registerBtn.setOnAction((ActionEvent e) -> {
		    renderRegistrationForm(primaryStage);
	    }
	);
	HBox btnContainer = new HBox(10);
	btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
	btnContainer.getChildren().add(loginBtn);
	btnContainer.getChildren().add(registerBtn);
	grid.add(btnContainer, 1, 4);

	Scene scene = new Scene(grid, 400, 275);
	primaryStage.setScene(scene);
	primaryStage.show();

    }
	
    /*
    * This function sets the language of the Login Page based upon the Application Locale
    */
    private void setLoginFormLabels() {
	if(alertLabel.getText().isEmpty() != true && currentAlertKey != null) {
		alertLabel.setText(locationService.getLocaleValueByKey(currentAlertKey));
	}
	usernameLabel.setText(locationService.getUsernameLabel());
	passwordLabel.setText(locationService.getPasswordLabel());
	loginBtn.setText(locationService.getLoginLabel());
	registerBtn.setText(locationService.getRegistrationLabel());
	
    }
	
    /*
    * This function sets the language of the Registration page based upon the Application Locale
    */
    private void setRegistrationFormLabels() {
	if(alertLabel.getText().isEmpty() != true && currentAlertKey != null) {
		alertLabel.setText(locationService.getLocaleValueByKey(currentAlertKey));
	}
	usernameLabel.setText(locationService.getUsernameLabel());
	passwordLabel.setText(locationService.getPasswordLabel());
	passwordConfirmationLabel.setText(locationService.getPasswordConfirmationLabel());
	signupBtn.setText(locationService.getSignupLabel());
	backToLoginBtn.setText(locationService.getBackToLoginLabel());
    }
	
    /*
    * This function bootstraps the Registration Form view/event listeners
    */
    private void renderRegistrationForm(Stage primaryStage) {
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Appointment App");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	grid.add(scenetitle, 0, 0, 2, 1);

	usernameLabel = new Label(locationService.getUsernameLabel() + ":");
	grid.add(usernameLabel, 0, 1);

	TextField userNameTextField = new TextField();
	grid.add(userNameTextField, 1, 1);

	passwordLabel = new Label(locationService.getPasswordLabel() + ":");
	grid.add(passwordLabel, 0, 2);

	PasswordField passwordInput = new PasswordField();
	grid.add(passwordInput, 1, 2);

	passwordConfirmationLabel = new Label(locationService.getPasswordConfirmationLabel() + ":");
	grid.add(passwordConfirmationLabel, 0, 3);

	PasswordField confirmationPasswordInput = new PasswordField();
	grid.add(confirmationPasswordInput, 1, 3);

    	ToggleGroup  langGroup = new ToggleGroup();
    	RadioButton unitedStatesBtn = new RadioButton("US");
    	unitedStatesBtn.setToggleGroup(langGroup);
	unitedStatesBtn.setUserData("en-US");

    	RadioButton franceBtn = new RadioButton("FR");
    	franceBtn.setToggleGroup(langGroup);
	franceBtn.setUserData("fr-FR");

	if (appLocale.getCountry().equals("FR")) {
		franceBtn.setSelected(true);
	} else {
		unitedStatesBtn.setSelected(true);
	}

	HBox langGroupContainer = new HBox(10);
	langGroupContainer.setAlignment(Pos.TOP_RIGHT);
	langGroupContainer.getChildren().add(unitedStatesBtn);
	langGroupContainer.getChildren().add(franceBtn);
	grid.add(langGroupContainer, 1, 6);

	langGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> _ov, Toggle _oldToggle, Toggle _newToggle) -> {
		if (langGroup.getSelectedToggle() != null) {
			String newLocationTag = langGroup.getSelectedToggle().getUserData().toString();
			locationService.setLocale(newLocationTag.substring(0, 2), newLocationTag.substring(3, 5));
			appLocale = locationService.getLocale();
			setRegistrationFormLabels();
		} 
	});

	alertLabel = new Label("");
	alertLabel.setTextFill(Color.web("#ff0000"));
	HBox alertContainer = new HBox(10);
	alertContainer.setAlignment(Pos.CENTER_LEFT);
	alertContainer.getChildren().add(alertLabel);
	grid.add(alertContainer, 0, 5);
	grid.setColumnSpan(alertContainer, GridPane.REMAINING);

	signupBtn = new Button(locationService.getSignupLabel());
	backToLoginBtn = new Button(locationService.getBackToLoginLabel());

	signupBtn.setOnAction((ActionEvent e) -> {
		String userName = userNameTextField.getText();
		String password = passwordInput.getText();
		String confirmationPassword = confirmationPasswordInput.getText();
		try {
			if(userName.isEmpty() != true && password.isEmpty() != true && password.equals(confirmationPassword)) {
				//check to see if username is already in database
				User existingUser = (User) queryBank.getUserByUsername(userName);
				if(existingUser == null) {
					User newUser = new User(userName, password);
					int userId = this.queryBank.insertUser(newUser);
					newUser.setUserId(userId);
					this.loginLogger.info(userName + " logged in.");
					UserHomePage homePage = new UserHomePage(primaryStage, newUser, queryBank);
					homePage.render(true);
				} else {
					throw new InvalidLoginException(locationService.getExistingUserMessage());
				}
			} else {
				throw new InvalidLoginException(locationService.getRegistrationFieldErrorMessage());
			}

		} catch(InvalidLoginException error) {
			String errorMessage = error.getMessage();
			alertSetter.setAlertText(errorMessage);
			if(errorMessage.equals(locationService.getExistingUserMessage())) {
				currentAlertKey = "register_existingUser";
			} else {
				currentAlertKey = "register_fieldError";
			}
		}
	    }
	);

	backToLoginBtn.setOnAction((ActionEvent e) -> {
		renderLoginForm(primaryStage);	
	    }
	);

	HBox btnContainer = new HBox(10);
	btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
	btnContainer.getChildren().add(signupBtn);
	btnContainer.getChildren().add(backToLoginBtn);
	grid.add(btnContainer, 1, 4);

	Scene scene = new Scene(grid, 400, 275);
	primaryStage.setScene(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
