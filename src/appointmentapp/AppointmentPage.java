/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;


/**
 *
 * @author Zachary Bennett 
 */
public class AppointmentPage {
	User user;
	Appointment appointment;
	Stage primaryStage;	
	Scene scene;
	GridPane grid;
	Text sceneTitle;
	ArrayList<Appointment> allAppointments;
	ObservableList<Customer> availableCustomers;
	TableView<Customer> customerSelectionTable;
	TextField appointmentTitleInput;
	TextField appointmentDescriptionInput;
	TextField appointmentLocationInput;
	TextField appointmentContactInput;
	DatePicker appointmentStartInput;
	Spinner<LocalTime> startTimeSpinner;
	Spinner<LocalTime> endTimeSpinner;
	DatePicker appointmentEndInput;
	Customer appointmentCustomer;
	Hyperlink customerLink; 
	Button scheduleApptBtn;
	Button updateApptBtn;
	Alert formError; 
	DatabaseQueryBank queryBank;

	public AppointmentPage(Stage primaryStage, Appointment appointment, DatabaseQueryBank queryBank, User user) {
		this.user = user;
		this.primaryStage = primaryStage;	
		this.appointment = appointment;
		this.queryBank = queryBank;
		this.grid = new GridPane();
		this.scene = new Scene(this.grid, 800, 600);
		this.sceneTitle = new Text(appointment == null ? "Add Appointment" : "Appointment: " + appointment.getTitle());
		this.allAppointments = (ArrayList<Appointment>) queryBank.getAppointments(user);
		if(appointment == null) {
			this.configureAddAppointmentBtn();
			ArrayList<Customer> userCustomers = (ArrayList<Customer>) queryBank.getCustomers(user);
			if(userCustomers == null) userCustomers = new ArrayList<Customer>();
			this.availableCustomers = FXCollections.observableArrayList(userCustomers);
			this.configureForm();
		} else {
			this.appointmentCustomer = (Customer) this.queryBank.getCustomer(appointment.getCustomerId());
			this.configureForm();
			this.setInitialFormValues();
			this.configureUpdateAppointmentBtn();
		}
	}

	private void configureHeadline() {
		this.sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		this.grid.add(this.sceneTitle, 0, 0, 2, 1);
	}

	interface IFormError {
		void showFormAlert(String alert);
	}

	private final IFormError formAlertSetter = (message) -> {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.show();
	};

	private boolean isStringFieldInvalid(String field) {
		return Pattern.compile("[0-9\\.\\-\\/()]+", Pattern.CASE_INSENSITIVE)
			      .matcher(field)
		     	      .find();
	}

	private boolean isFieldOnlyWhitespace(String fieldValue) {
		return fieldValue.trim().length() == 0;
	}

	private void configureAddAppointmentBtn() {
		scheduleApptBtn = new Button("Schedule Appointment");
		scheduleApptBtn.setOnAction((ActionEvent e) -> {
			String apptTitle = appointmentTitleInput.getText();
			String apptDesc = appointmentDescriptionInput.getText();
			String apptLocation = appointmentLocationInput.getText();
			String apptContact = appointmentContactInput.getText();
			Date apptStartDate = Date.from(appointmentStartInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			LocalTime apptStartTime = startTimeSpinner.getValue();
			Date apptEndDate = Date.from(appointmentEndInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			LocalTime apptEndTime = endTimeSpinner.getValue();
			Date newStartDate = new Date(apptStartDate.getYear(), apptStartDate.getMonth(), apptStartDate.getDate(), apptStartTime.getHour(), apptStartTime.getMinute()); 
			Date newEndDate = new Date(apptEndDate.getYear(), apptEndDate.getMonth(), apptEndDate.getDate(), apptEndTime.getHour(), apptEndTime.getMinute());

			try {
				if(this.appointmentCustomer == null) {
					throw new InvalidAppointmentDataException("Please select an associated customer for this appointment.");
				}
				if(apptTitle.isEmpty() || this.isStringFieldInvalid(apptTitle) || this.isFieldOnlyWhitespace(apptTitle)) {
					throw new InvalidAppointmentDataException("Please enter a valid appointment title.");
				} 
				if(apptDesc.isEmpty() || this.isFieldOnlyWhitespace(apptDesc)) {
					throw new InvalidAppointmentDataException("Please enter a description.");
				}
				if(apptLocation.isEmpty() || this.isFieldOnlyWhitespace(apptLocation)) {
					throw new InvalidAppointmentDataException("Please enter a location.");
				}
				if(apptContact.isEmpty() || this.isFieldOnlyWhitespace(apptContact)) {
					throw new InvalidAppointmentDataException("Please enter a contact.");
				}
				int startHours = newStartDate.getHours();
				int endHours = newEndDate.getHours();
				if(startHours < 8 || startHours > 17 || endHours < 8 || endHours > 17) {
					throw new InvalidAppointmentDataException("Appointments may not be scheduled outside of business hours (8am to 5pm).");
				}
				final Appointment overlapAppt = new Appointment();
				this.allAppointments.forEach((appointment) -> {
						long currentStart = appointment.getStart().getTime();
						long currentEnd = appointment.getEnd().getTime();
						long mainStart = newStartDate.getTime();
						long mainEnd = newEndDate.getTime();
						if(currentStart <= mainEnd && mainStart <= currentEnd) {
							overlapAppt.setStart(appointment.getStart());
							overlapAppt.setEnd(appointment.getEnd());
							return;
						}
				});
				if(overlapAppt.getStart() != null) {
							throw new InvalidAppointmentDataException("Appointments may not overlap. The overlapping appointment starts at " + overlapAppt.getStart().toString() + " and ends at " + overlapAppt.getEnd().toString() + ".");
						
				}

				//check for valid appointments times, appointment overlap, etc

			} catch(InvalidAppointmentDataException error) {
				formAlertSetter.showFormAlert(error.getMessage());
				return;	
			}

			String userName = this.user.getUserName();	
			Appointment newAppointment = new Appointment(
				this.appointmentCustomer.getCustomerId(), // this needs to come from a table - user needs to select a customer
				userName,
				apptTitle,
				apptDesc,
				apptContact,
				apptLocation,
				"", //placeholder for url
				newStartDate,
				newEndDate
			);
			this.queryBank.insertAppointment(newAppointment);
			
			UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
			homePage.render();
							
		});

		HBox scheduleApptBtnContainer = new HBox(10);
		scheduleApptBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		scheduleApptBtnContainer.getChildren().add(scheduleApptBtn);

		this.grid.add(scheduleApptBtn, 0, 8);

	}

	private void configureUpdateAppointmentBtn() {
		updateApptBtn = new Button("Update Appointment");
		updateApptBtn.setOnAction((ActionEvent e) -> {
			String apptTitle = appointmentTitleInput.getText();
			String apptDesc = appointmentDescriptionInput.getText();
			String apptLocation = appointmentLocationInput.getText();
			String apptContact = appointmentContactInput.getText();
			Date apptStartDate = Date.from(appointmentStartInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			LocalTime apptStartTime = startTimeSpinner.getValue();
			Date apptEndDate = Date.from(appointmentEndInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			LocalTime apptEndTime = endTimeSpinner.getValue();
	
			Date newStartDate = new Date(apptStartDate.getYear(), apptStartDate.getMonth(), apptStartDate.getDate(), apptStartTime.getHour(), apptStartTime.getMinute()); 
			Date newEndDate = new Date(apptEndDate.getYear(), apptEndDate.getMonth(), apptEndDate.getDate(), apptEndTime.getHour(), apptEndTime.getMinute());
			
			try {
				if(apptTitle.isEmpty() || this.isStringFieldInvalid(apptTitle) || this.isFieldOnlyWhitespace(apptTitle)) {
					throw new InvalidAppointmentDataException("Please enter a valid appointment title.");
				} 
				if(apptDesc.isEmpty() || this.isFieldOnlyWhitespace(apptDesc)) {
					throw new InvalidAppointmentDataException("Please enter a description.");
				}
				if(apptLocation.isEmpty() || this.isFieldOnlyWhitespace(apptLocation)) {
					throw new InvalidAppointmentDataException("Please enter a location.");
				}
				if(apptContact.isEmpty() || this.isFieldOnlyWhitespace(apptContact)) {
					throw new InvalidAppointmentDataException("Please enter a contact.");
				}
				int startHours = newStartDate.getHours();
				int endHours = newEndDate.getHours();
				if(startHours < 8 || startHours > 17 || endHours < 8 || endHours > 17) {
					throw new InvalidAppointmentDataException("Appointments may not be scheduled outside of business hours (8am to 5pm).");
				}

				final Appointment overlapAppt = new Appointment();
				this.allAppointments.forEach((appointment) -> {
						long currentStart = appointment.getStart().getTime();
						long currentEnd = appointment.getEnd().getTime();
						long mainStart = newStartDate.getTime();
						long mainEnd = newEndDate.getTime();
						if(appointment.getAppointmentId() != this.appointment.getAppointmentId() && currentStart <= mainEnd && mainStart <= currentEnd) {
							overlapAppt.setStart(appointment.getStart());
							overlapAppt.setEnd(appointment.getEnd());
							return;
						}
				});
				if(overlapAppt.getStart() != null) {
							throw new InvalidAppointmentDataException("Appointments may not overlap. The overlapping appointment starts at " + overlapAppt.getStart().toString() + " and ends at " + overlapAppt.getEnd().toString() + ".");
						
				}

				//check for valid appointments times, appointment overlap, etc

			} catch(InvalidAppointmentDataException error) {
				formAlertSetter.showFormAlert(error.getMessage());
				return;	
			}

			String userName = this.user.getUserName();	
			int appointmentId = this.appointment.getAppointmentId();
			Appointment newAppointment = new Appointment(
				appointmentId, 
				this.appointment.getCustomerId(),
				apptTitle,
				apptDesc,
				apptContact,
				apptLocation,
				"", //placeholder for url
				newStartDate,
				newEndDate,
				userName
			);
			this.queryBank.updateAppointment(newAppointment);
			UserHomePage homePage = new UserHomePage(primaryStage, user, queryBank);
			homePage.render();
		});

		HBox updateBtnContainer = new HBox(10);
		updateBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		updateBtnContainer.getChildren().add(updateApptBtn);

		this.grid.add(updateBtnContainer, 0, 8);

	}

	private void setInitialFormValues() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
		Date startDate = this.appointment.getStart();
		Date endDate = this.appointment.getEnd();
		LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalTime startLocalTime = LocalTime.parse(dateFormatter.format(startDate));
		LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalTime endLocalTime = LocalTime.parse(dateFormatter.format(endDate));

		this.appointmentTitleInput.setText(this.appointment.getTitle());
		this.appointmentDescriptionInput.setText(this.appointment.getDescription());
		this.appointmentLocationInput.setText(this.appointment.getLocation());
		this.appointmentContactInput.setText(this.appointment.getContact());
		this.appointmentStartInput.setValue(startLocalDate);
		this.startTimeSpinner.getValueFactory().setValue(startLocalTime);
		this.appointmentEndInput.setValue(endLocalDate);
		this.endTimeSpinner.getValueFactory().setValue(endLocalTime);
	}

	public void renderCustomerView(Customer customer) {
		CustomerForm customerForm = new CustomerForm(this.primaryStage, customer, this.queryBank, this.user);
		customerForm.render();
	}

	private void configureForm() {
		Label titleLabel = new Label("Type: ");
		grid.add(titleLabel, 0, 1);
		appointmentTitleInput = new TextField();
		grid.add(appointmentTitleInput, 1, 1);

		Label descLabel = new Label("Description: ");
		grid.add(descLabel, 0, 2);
		appointmentDescriptionInput = new TextField();
		grid.add(appointmentDescriptionInput, 1, 2);

		Label locationLabel = new Label("Location: ");
		grid.add(locationLabel, 0, 3);
		appointmentLocationInput = new TextField();
		grid.add(appointmentLocationInput, 1, 3);

		Label contactLabel = new Label("Contact: ");
		grid.add(contactLabel, 0, 4);
		appointmentContactInput = new TextField();
		grid.add(appointmentContactInput, 1, 4);
		
		if(this.appointment != null) {
			Label urlLabel = new Label("Link to " + this.appointmentCustomer.getCustomerName() + "'s customer page: ");
			grid.add(urlLabel, 0, 5);
			customerLink = new Hyperlink();
			customerLink.setText("View Customer");
			customerLink.setOnAction((ActionEvent e) -> {
				this.renderCustomerView(this.appointmentCustomer);
			});
			
			grid.add(customerLink, 1, 5);
		} else {
			this.customerSelectionTable = new TableView<Customer>(this.availableCustomers);
			TableColumn<Customer, String> nameColumn = new TableColumn<>("Available Customers To Link");
			nameColumn.setCellValueFactory((CellDataFeatures<Customer, String> selectedCustomer) -> new ReadOnlyStringWrapper(selectedCustomer.getValue().getCustomerName()));

		this.customerSelectionTable.setOnMouseClicked((MouseEvent event) -> {
			Customer selectedCustomer = this.customerSelectionTable.getSelectionModel().getSelectedItem();
			if(selectedCustomer != null) {
				this.appointmentCustomer = selectedCustomer;	
			}
	        });

		this.customerSelectionTable.getColumns().add(nameColumn);

		this.grid.add(this.customerSelectionTable, 1, 5);

		}

		Label startLabel = new Label("Start: ");
		grid.add(startLabel, 0, 6);
		appointmentStartInput = new DatePicker();
		grid.add(appointmentStartInput, 1, 6);

		/*
			Credit for this time converter goes to James_D from 
			https://stackoverflow.com/questions/32613619/how-to-make-a-timespinner-in-javafx
		*/
		LocalTimeStringConverter timeConverter = new LocalTimeStringConverter() {
			    @Override
			    public String toString(LocalTime time) {
				return DateTimeFormatter.ofPattern("HH:mm").format(time);
			    }

			    @Override
			    public LocalTime fromString(String string) {
				String[] enteredDigits = string.split(":");
				int hours = getIntField(enteredDigits, 0);
				int minutes = getIntField(enteredDigits, 1);
				int totalSeconds = (hours * 60 + minutes) * 60;
				return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60);
			    }

			    private int getIntField(String[] digits, int index) {
				if (digits.length <= index || digits[index].isEmpty()) {
				    return 0 ;
				}
				return Integer.parseInt(digits[index]);
			    }
		};

		this.startTimeSpinner = new Spinner(new SpinnerValueFactory() {

			{
			    setConverter(timeConverter);
			}

			@Override
			public void decrement(int totalSteps) {
			    if (getValue() == null)
				setValue(LocalTime.now());
			    else {
				LocalTime time = (LocalTime) getValue();
				setValue(time.minusMinutes(totalSteps));
			    }
			}

			@Override
			public void increment(int totalSteps) {
			    if (this.getValue() == null)
				setValue(LocalTime.now());
			    else {
				LocalTime time = (LocalTime) getValue();
				setValue(time.plusMinutes(totalSteps));
			    }
			}
	        });
	        startTimeSpinner.setEditable(true);
		grid.add(startTimeSpinner, 2, 6);

		Label endLabel = new Label("End: ");
		grid.add(endLabel, 0, 7);
		appointmentEndInput = new DatePicker();
		grid.add(appointmentEndInput, 1, 7);
		
			

		this.endTimeSpinner = new Spinner(new SpinnerValueFactory() {

			{
			    setConverter(timeConverter);
			}

			@Override
			public void decrement(int totalSteps) {
			    if (getValue() == null)
				setValue(LocalTime.now());
			    else {
				LocalTime time = (LocalTime) getValue();
				setValue(time.minusMinutes(totalSteps));
			    }
			}

			@Override
			public void increment(int totalSteps) {
			    if (this.getValue() == null)
				setValue(LocalTime.now());
			    else {
				LocalTime time = (LocalTime) getValue();
				setValue(time.plusMinutes(totalSteps));
			    }
			}
	        });
	        endTimeSpinner.setEditable(true);
		grid.add(endTimeSpinner, 2, 7);
	}

	public void render() {
		this.configureHeadline();
		this.primaryStage.setScene(this.scene);
		this.primaryStage.show();
	}
	
}
