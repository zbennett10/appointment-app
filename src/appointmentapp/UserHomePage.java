/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

/**
 *
 * @author Zachary Bennett 
 */
public class UserHomePage {
	User user;
	TextArea reportDisplay;
	ObservableList<Customer> customers;
	ObservableList<Appointment> appointments;
	ObservableList<Appointment> appointmentsWithinCurrentMonth;
	ObservableList<Appointment> appointmentsWithinCurrentWeek;
	Stage primaryStage;	
	Scene scene;
	GridPane grid;
	TableView<Customer> customerTable;
	TableView<Appointment> appointmentTable;
	Text sceneTitle;
	Button addCustomerBtn;
	DatabaseQueryBank queryBank;


	public UserHomePage(Stage primaryStage, User user, DatabaseQueryBank queryBank) {
		this.primaryStage = primaryStage;	
		this.user = user;
		this.queryBank = queryBank;
		ArrayList<Customer> userCustomers = (ArrayList<Customer>) queryBank.getCustomersByUsername(user);
		if(userCustomers == null) userCustomers = new ArrayList<Customer>();
		this.customers = FXCollections.observableArrayList(userCustomers);
		ArrayList<Appointment> userAppointments = (ArrayList<Appointment>) queryBank.getAppointmentsByUser(user);
		if(userAppointments == null) userAppointments = new ArrayList<Appointment>();
		this.appointments = FXCollections.observableArrayList(userAppointments);
		this.grid = new GridPane();
		this.customerTable = new TableView<Customer>(this.customers);
		this.appointmentTable = new TableView<Appointment>(this.appointments);
		this.scene = new Scene(this.grid, 1000, 800);
		this.sceneTitle = new Text("Appointment App - Home");
	}	

	private void configureGrid() {
		this.grid.setAlignment(Pos.CENTER);
		this.grid.setHgap(10);
		this.grid.setVgap(10);
		this.grid.setPadding(new Insets(25, 25, 25, 25));
	}
	
	/*
	* This function configures the table for handling customers and bootstraps event listeners appropriately.
	*/
	private void configureCustomerTable() {
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Customers"); //set value of column to be name of customer
		nameColumn.setCellValueFactory((CellDataFeatures<Customer, String> selectedCustomer) -> new ReadOnlyStringWrapper(selectedCustomer.getValue().getCustomerName()));

		TableColumn<Customer, String> deleteColumn = new TableColumn<>("");
		deleteColumn.setCellValueFactory((CellDataFeatures<Customer, String> selectedCustomer) -> new ReadOnlyStringWrapper("Delete"));
		
		//on customer double click open up the customer form
		this.customerTable.setOnMouseClicked((MouseEvent event) -> {
			Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		    if (event.getClickCount() > 1 && selectedCustomer != null) {
			TablePosition pos = customerTable.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			Object item = customerTable.getItems().get(row);
			TableColumn col = pos.getTableColumn();

			String data = (String) col.getCellObservableValue(item).getValue();
			if(data.equals("Delete")) {
				//show popup deletion confirmation
			    	Popup popup = new Popup(); 
				Text confirmation = new Text("Delete customer? ");
				Button yesBtn = new Button("Yes");
				yesBtn.setOnAction((ActionEvent e) -> {
					this.queryBank.deleteCustomer(selectedCustomer.getCustomerId());
					popup.hide();
					this.customers.removeAll();
					ArrayList<Customer> updatedCustomerList = (ArrayList<Customer>) queryBank.getCustomers(user);
					this.customers = FXCollections.observableArrayList(updatedCustomerList);
					this.customerTable.setItems(this.customers);
					
				});

				Button cancelBtn = new Button("No");
				cancelBtn.setOnAction((ActionEvent e) -> {
					popup.hide();
				});
				HBox popupContentCntr = new HBox();
				popupContentCntr.setAlignment(Pos.BOTTOM_CENTER);
				popupContentCntr.getChildren().add(confirmation);
				popupContentCntr.getChildren().add(yesBtn);
				popupContentCntr.getChildren().add(cancelBtn);
				popup.centerOnScreen();
			    	popup.getContent().addAll(popupContentCntr);
			    	popup.show(primaryStage);
			} else {
				this.renderCustomerView(selectedCustomer);
			}
		    }
		});

		this.customerTable.getColumns().add(nameColumn);
		this.customerTable.getColumns().add(deleteColumn);

		this.grid.add(this.customerTable, 0, 1);
	}
	
	/*
	* This function configures the appointment table and bootstraps event listeners appropriately.
	*/
	private void configureAppointmentCalendar() {
		TableColumn<Appointment, String> titleColumn = new TableColumn<>("Appointments"); 
		titleColumn.setCellValueFactory((CellDataFeatures<Appointment, String> selectedAppt) -> new ReadOnlyStringWrapper(selectedAppt.getValue().getTitle()));

		TableColumn<Appointment, String> deleteColumn = new TableColumn<>("");
		deleteColumn.setCellValueFactory((CellDataFeatures<Appointment, String> selectedAppt) -> new ReadOnlyStringWrapper("Delete"));

		//on appointment double click open up the appointment management screen 
		this.appointmentTable.setOnMouseClicked((MouseEvent event) -> {
			Appointment selectedAppt = this.appointmentTable.getSelectionModel().getSelectedItem();
		    if (event.getClickCount() > 1 && selectedAppt != null) {
			TablePosition pos = this.appointmentTable.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			Object item = this.appointmentTable.getItems().get(row);
			TableColumn col = pos.getTableColumn();

			String columnData = (String) col.getCellObservableValue(item).getValue();
			if(columnData.equals("Delete")) {
				//show popup deletion confirmation
			    	Popup popup = new Popup(); 
				Text confirmation = new Text("Delete appointment? ");
				Button yesBtn = new Button("Yes");
				yesBtn.setOnAction((ActionEvent e) -> {
					this.queryBank.deleteAppointment(selectedAppt.getAppointmentId());
					//need to also delete reminderrs associated with this appointment (also increment types?)
					popup.hide();
					this.appointments.removeAll();
					ArrayList<Appointment> updatedApptList = (ArrayList<Appointment>) queryBank.getAppointmentsByUser(this.user);
					this.appointments = FXCollections.observableArrayList(updatedApptList);
					this.appointmentTable.setItems(this.appointments);
					
				});

				Button cancelBtn = new Button("No");
				cancelBtn.setOnAction((ActionEvent e) -> {
					popup.hide();
				});
				HBox popupContentCntr = new HBox();
				popupContentCntr.setAlignment(Pos.BOTTOM_CENTER);
				popupContentCntr.getChildren().add(confirmation);
				popupContentCntr.getChildren().add(yesBtn);
				popupContentCntr.getChildren().add(cancelBtn);
				popup.centerOnScreen();
			    	popup.getContent().addAll(popupContentCntr);
			    	popup.show(primaryStage);
			} else {
				this.renderAppointmentView(selectedAppt);
			}
		    }
		});

		this.appointmentTable.getColumns().add(titleColumn);
		this.appointmentTable.getColumns().add(deleteColumn);

		this.grid.add(this.appointmentTable, 1, 1);

	}
	
	private void configureReportDisplay() {
		TextArea reportOutput = new TextArea();	
		this.reportDisplay = reportOutput;
		reportOutput.setPrefHeight(600);
		reportOutput.setPrefWidth(450);
		reportOutput.setEditable(false);
		this.grid.add(reportOutput, 2, 1);
	}
	
	/*
	* This helper function takes an ArrayList of appointments and divvies them up by their creator.
	*/
	private HashMap<String, ArrayList<Appointment>> getAppointmentsByUsername(ArrayList<Appointment> appointments) {
		HashMap<String, ArrayList<Appointment>> scheduleMap = new HashMap<>();
		appointments.forEach(appointment -> {
			String userName = appointment.getCreatedBy();
			if(scheduleMap.get(userName) == null) {
				ArrayList<Appointment> userAppointmentList = new ArrayList<Appointment>();
				userAppointmentList.add(appointment);
				scheduleMap.put(userName, userAppointmentList);
			} else {
				scheduleMap.get(userName).add(appointment);
			}
		});
		return scheduleMap;
	}
	
	/*
	* This helper function is for extracting a specific key from a hash table that correspondes to the
	* following format: MM_YY
	*/
	private String getMonthYearKey(Date appointmentStart) {
		int month = appointmentStart.getMonth();
		int year  = appointmentStart.getYear();
		return Integer.toString(month) + "_" + Integer.toString(year);
	}
	
	/*
	* This function takes in a key of format: MM_YY and stringifies it.
	*/
	private String getMonthYearDesignation(String key) {
		String[] tokens = key.split("\\_");
		String month = new DateFormatSymbols().getMonths()[Integer.parseInt(tokens[0])];
		return month + " - " + tokens[1].substring(1);
	}
	
	/*	
	* This helper function takes an ArrayList of appointments and divvies them up by their month.
	*/
	private HashMap<String, ArrayList<Appointment>> getAppointmentsByMonth(ArrayList<Appointment> appointments) {
		HashMap<String, ArrayList<Appointment>> appointmentsByMonth = new HashMap<>();
		appointments.forEach(appointment -> {
			String key = this.getMonthYearKey(appointment.getStart());

			if(appointmentsByMonth.get(key) == null) {
				ArrayList<Appointment> newApptList = new ArrayList<Appointment>();
				newApptList.add(appointment);
				appointmentsByMonth.put(key, newApptList);
			} else {
				appointmentsByMonth.get(key).add(appointment);
			}
		});
		return appointmentsByMonth;
			
	}
	
	/*
	* This function generates a report that shows a consultant's schedule.
	* This is basically just appointments that are divvied up according to their corresponding user.
	*/
	private String generateConsultantReport () {
		ArrayList<Appointment> appointments = (ArrayList<Appointment>) this.queryBank.getAllAppointments();
		HashMap<String, ArrayList<Appointment>> scheduleMap = this.getAppointmentsByUsername(appointments);
		StringBuilder report = new StringBuilder();
		report.append("Consultant Schedule\n");

		for(Map.Entry<String, ArrayList<Appointment>> pair : scheduleMap.entrySet()) {
			String userName = pair.getKey();
			ArrayList<Appointment> apptList = pair.getValue();
			report.append("\n" + userName + ": \n");
			apptList.forEach(appt -> {
				System.out.println(appt.getTitle());
				report.append("\t" + appt.getTitle() + ": ");
				report.append(appt.getStart().toString() + " -> ");
				report.append(appt.getEnd().toString());
				report.append("\n");
			});
			report.append("\n");
		}
		return report.toString();
	}

	/*
	* This function generates a report that shows a appointment types by month.
	*/
	private String generateAppointmentTypesReport() {
		ArrayList<Appointment> appointments = (ArrayList<Appointment>) this.queryBank.getAllAppointments();
		HashMap<String, ArrayList<Appointment>> appointmentsByMonth = this.getAppointmentsByMonth(appointments);
		StringBuilder report = new StringBuilder();
		report.append("Unique Appointment Types By Month\n");

		for(Map.Entry<String, ArrayList<Appointment>> pair : appointmentsByMonth.entrySet()) {
			String key = pair.getKey();
			String monthYearDesignation = this.getMonthYearDesignation(key);
			ArrayList<Appointment> apptList = pair.getValue();
			ArrayList<String> apptTypes = new ArrayList();
			for(Appointment appt : apptList) {
				apptTypes.add(appt.getTitle());
			}
			Set<String> uniqueApptTypes = new HashSet<String>(apptTypes);
			int apptTypeCount = uniqueApptTypes.size();
			report.append("\n" + monthYearDesignation + ": \n");
			report.append("\t Total Appointment Types: " + Integer.toString(apptTypeCount) + "\n");
			uniqueApptTypes.forEach(type -> {
				report.append("\t" + type);
				report.append("\n");
			});
			report.append("\n");
		}
		return report.toString();
	}

	/*
	* This function generates a report that shows the total appointments, consultants and customers in the database.
	*/
	private String generateCompanyTotalsReport() {
		ArrayList<Appointment> appointments = (ArrayList<Appointment>) this.queryBank.getAllAppointments();
		ArrayList<User> consultants = (ArrayList<User>) this.queryBank.getAllUsers(null);
		ArrayList<Customer> customers = (ArrayList<Customer>) this.queryBank.getCustomers(null);

		StringBuilder report = new StringBuilder();
		report.append("Company Totals: \n");
		report.append("\n Total Consultants: " + Integer.toString(consultants.size()) + "\n");
		report.append("\n Total Customers: " + Integer.toString(customers.size()) + "\n");
		report.append("\n Total Appointments: " + Integer.toString(appointments.size()) + "\n");
		return report.toString();
	}
	
	private void configureReportGenerationBtns() {
		VBox reportGenerationBtnContainer = new VBox();
		Button consultantScheduleBtn = new Button("Generate Consultant Schedule Report");
		consultantScheduleBtn.setOnAction((ActionEvent e) -> {
			this.reportDisplay.setText(this.generateConsultantReport());
		});
		Button appointmentTypesBtn = new Button("Generate Appointment Types Report");
		appointmentTypesBtn.setOnAction((ActionEvent e) -> {
			this.reportDisplay.setText(this.generateAppointmentTypesReport());
		});
		Button generateCompanyTotalsBtn = new Button("Generate Company Totals Report");
		generateCompanyTotalsBtn.setOnAction((ActionEvent e) -> {
			this.reportDisplay.setText(this.generateCompanyTotalsReport());	
		});
		reportGenerationBtnContainer.setAlignment(Pos.BOTTOM_RIGHT);
		reportGenerationBtnContainer.getChildren().add(consultantScheduleBtn);
		reportGenerationBtnContainer.getChildren().add(appointmentTypesBtn);
		reportGenerationBtnContainer.getChildren().add(generateCompanyTotalsBtn);
		this.grid.add(reportGenerationBtnContainer, 2, 2);
	}
		

	private void configureHeadline() {
		this.sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		this.grid.add(this.sceneTitle, 0, 0, 2, 1);
	}
	
	/*
	* This function configures the filter buttons for the calendar - bootstrapping their event listeners.
	*/
	private void configureCalendarFilterBtns() {
		Button filterByMonthBtn = new Button("By Month");
		filterByMonthBtn.setOnAction((ActionEvent e) -> {
			int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
			Calendar iterationCalendar = Calendar.getInstance();
			ArrayList<Appointment> apptsWithinCurrentMonth = new ArrayList<Appointment>();
			this.appointments.forEach((appointment) -> {
				iterationCalendar.setTime(appointment.getStart());
				if(iterationCalendar.get(Calendar.MONTH) == currentMonth) {
					apptsWithinCurrentMonth.add(appointment);	
				}
			});
			this.appointmentsWithinCurrentMonth = FXCollections.observableArrayList(apptsWithinCurrentMonth);
			this.appointmentTable.setItems(this.appointmentsWithinCurrentMonth);
		});

		Button filterByWeekBtn = new Button("By Week");
		filterByWeekBtn.setOnAction((ActionEvent e) -> {
			Calendar currentTimeCalendar = Calendar.getInstance();
			int dayOfWeek = currentTimeCalendar.get(Calendar.DAY_OF_WEEK);
			int currentMonth = currentTimeCalendar.get(Calendar.MONTH);
			Calendar iterationCalendar = Calendar.getInstance();
			ArrayList<Appointment> apptsWithinCurrentWeek = new ArrayList<Appointment>();
			this.appointments.forEach((appointment) -> {
				iterationCalendar.setTime(appointment.getStart());
				if(iterationCalendar.get(Calendar.DAY_OF_WEEK) >= dayOfWeek && iterationCalendar.get(Calendar.MONTH) == currentMonth) {
					apptsWithinCurrentWeek.add(appointment);	
				}
			});
			this.appointmentsWithinCurrentWeek= FXCollections.observableArrayList(apptsWithinCurrentWeek);
			this.appointmentTable.setItems(this.appointmentsWithinCurrentWeek);
		});

		Button allApptsBtn = new Button("All");
		allApptsBtn.setOnAction((ActionEvent e) -> {
			this.appointmentTable.setItems(this.appointments);
		});

		HBox filterBtnContainer = new HBox(10);
		filterBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		filterBtnContainer.getChildren().add(allApptsBtn);
		filterBtnContainer.getChildren().add(filterByWeekBtn);
		filterBtnContainer.getChildren().add(filterByMonthBtn);
		this.grid.add(filterBtnContainer, 1, 0);
		
	}

	/*
	* This function configures the button for adding appointments and bootstraps it's event listener.
	*/
	private void configureAddAppointmentBtn() {
		Button addApptBtn = new Button("Add Appointment");
		addApptBtn.setOnAction((ActionEvent e) -> {
			this.renderAppointmentView(null);
		});
		HBox addApptContainer = new HBox(10);
		addApptContainer.setAlignment(Pos.BOTTOM_CENTER);
		addApptContainer.getChildren().add(addApptBtn);

		this.grid.add(addApptContainer, 1, 2);
	}

	/*
	* This function configures the button for adding customers and bootstraps it's event listener.
	*/
	private void configureAddCustomerBtn() {
		addCustomerBtn = new Button("Add Customer");
		addCustomerBtn.setOnAction((ActionEvent e) -> {
			renderCustomerView(null);
		});
		HBox addBtnContainer = new HBox(10);
		addBtnContainer.setAlignment(Pos.BOTTOM_CENTER);
		addBtnContainer.getChildren().add(addCustomerBtn);

		this.grid.add(addBtnContainer, 0, 2);
	}

	public void renderCustomerView(Customer customer) {
		CustomerForm customerForm = new CustomerForm(this.primaryStage, customer, this.queryBank, this.user);
		customerForm.render();
	}

	public void renderAppointmentView(Appointment appointment) {
		AppointmentPage appointmentPage = new AppointmentPage(this.primaryStage, appointment, this.queryBank, this.user);
		appointmentPage.render();
	}

	private void alertUserOfUpcomingAppointments() {
		//get current time
		long currentTimestamp = Calendar.getInstance().getTime().getTime();

		//iterate over appointments -> if appointment start is 15 minutes or less past current time show alert for that appointment
		this.appointments.forEach((appt) -> {
			long apptTimestamp = appt.getStart().getTime();
			if (Math.abs(apptTimestamp - currentTimestamp) <= TimeUnit.MINUTES.toMillis(15)) {
				this.showAppointmentReminderAlert(appt);
			}
		});

	}

	private void showAppointmentReminderAlert(Appointment appointment)  {
		StringBuilder reminder = new StringBuilder();
		reminder.append("Upcoming Appointment: \n\n")
			.append("Title/Type: " + appointment.getTitle() + "\n")
			.append("Description: " + appointment.getDescription() + "\n")
			.append("Start: " + appointment.getStart().toString() + "\n")
			.append("End: " + appointment.getEnd().toString() + "\n");

		Alert alert = new Alert(AlertType.INFORMATION, reminder.toString());
		alert.show();
	}

	public void render(boolean userJustLoggedIn) {
		this.configureGrid();
		this.configureHeadline();
		this.configureCustomerTable();
		this.configureAddCustomerBtn();
		this.configureAppointmentCalendar();
		this.configureCalendarFilterBtns();
		this.configureAddAppointmentBtn();
		this.configureReportDisplay();
		this.configureReportGenerationBtns();

		this.primaryStage.setScene(this.scene);
		this.primaryStage.show();
		if(userJustLoggedIn) {
			this.alertUserOfUpcomingAppointments();
		}
	}
			
}
