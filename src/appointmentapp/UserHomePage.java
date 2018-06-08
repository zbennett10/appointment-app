/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
		ArrayList<Customer> userCustomers = (ArrayList<Customer>) queryBank.getCustomers(user);
		if(userCustomers == null) userCustomers = new ArrayList<Customer>();
		this.customers = FXCollections.observableArrayList(userCustomers);
		ArrayList<Appointment> userAppointments = (ArrayList<Appointment>) queryBank.getAppointments(user);
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

	private void configureCustomerTable() {
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Customers"); //set value of column to be name of customer
		nameColumn.setCellValueFactory((CellDataFeatures<Customer, String> selectedCustomer) -> new ReadOnlyStringWrapper(selectedCustomer.getValue().getCustomerName()));

		TableColumn<Customer, String> deleteColumn = new TableColumn<>("");
		deleteColumn.setCellValueFactory((CellDataFeatures<Customer, String> selectedCustomer) -> new ReadOnlyStringWrapper("Delete"));
		
		//on customer double click open up the customer form
		this.customerTable.setOnMouseClicked((MouseEvent event) -> {
			Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		    if (event.getClickCount() > 1 && selectedCustomer != null) {
			this.renderCustomerView(selectedCustomer);
		    } else if (event.getClickCount() == 1) {
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
			}
		    }
		});

		this.customerTable.getColumns().add(nameColumn);
		this.customerTable.getColumns().add(deleteColumn);

		this.grid.add(this.customerTable, 0, 1);
	}

	private void configureAppointmentCalendar() {
		TableColumn<Appointment, String> titleColumn = new TableColumn<>("Appointments"); 
		titleColumn.setCellValueFactory((CellDataFeatures<Appointment, String> selectedAppt) -> new ReadOnlyStringWrapper(selectedAppt.getValue().getTitle()));

		TableColumn<Appointment, String> deleteColumn = new TableColumn<>("");
		deleteColumn.setCellValueFactory((CellDataFeatures<Appointment, String> selectedAppt) -> new ReadOnlyStringWrapper("Delete"));

		//on appointment double click open up the appointment management screen 
		this.appointmentTable.setOnMouseClicked((MouseEvent event) -> {
			Appointment selectedAppt = this.appointmentTable.getSelectionModel().getSelectedItem();
		    if (event.getClickCount() > 1 && selectedAppt != null) {
			this.renderAppointmentView(selectedAppt);
		    } else if (event.getClickCount() == 1 && selectedAppt != null) {
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
					ArrayList<Appointment> updatedApptList = (ArrayList<Appointment>) queryBank.getAppointments(this.user);
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
			}
		    }
		});

		this.appointmentTable.getColumns().add(titleColumn);
		this.appointmentTable.getColumns().add(deleteColumn);

		this.grid.add(this.appointmentTable, 1, 1);

	}

	private void configureHeadline() {
		this.sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		this.grid.add(this.sceneTitle, 0, 0, 2, 1);
	}

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
		

		//create btn that filters appointments only with appointments occuring this week
	}
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

	public void render() {
		this.configureGrid();
		this.configureHeadline();
		this.configureCustomerTable();
		this.configureAddCustomerBtn();
		this.configureAppointmentCalendar();
		this.configureCalendarFilterBtns();
		this.configureAddAppointmentBtn();

		this.primaryStage.setScene(this.scene);
		this.primaryStage.show();
	}
			
}
