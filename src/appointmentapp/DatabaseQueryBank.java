/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.time.format.FormatStyle;
import com.mysql.jdbc.Statement;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zachary Bennett
 * This class provides an API for interacting with the database.
 */
public class DatabaseQueryBank {
	private final String DATABASE_URL = "jdbc:mysql://52.206.157.109/U04tYO?";
	
	/*
	* Define constants which point to functions containing statements to run against the database.
	* Reflection is used to map these constants to their corresponding functions.
	*/
	private final String GET_ALL_USERS = "runGetAllUsers";
	private final String GET_USER = "runGetUser";
	private final String INSERT_USER = "runInsertUser";
	private final String DELETE_USER = "runDeleteUser";
	private final String UPDATE_USER = "runUpdateUser";

	private final String GET_ADDRESS = "runGetAddress";
	private final String INSERT_ADDRESS = "runInsertAddress";
	private final String DELETE_ADDRESS = "runDeleteAddress";
	private final String UPDATE_ADDRESS = "runUpdateAddress";
	
	private final String GET_ALL_APPOINTMENTS = "runGetAllAppointments";
	private final String GET_APPOINTMENTS_BY_USER = "runGetAppointmentsByUser";
	private final String GET_APPOINTMENT = "runGetAppointment";
	private final String INSERT_APPOINTMENT = "runInsertAppointment";
	private final String DELETE_APPOINTMENT = "runDeleteAppointment";
	private final String UPDATE_APPOINTMENT = "runUpdateAppointment";

	private final String GET_CITY = "runGetCity";
	private final String INSERT_CITY = "runInsertCity";
	private final String DELETE_CITY = "runDeleteCity";
	private final String UPDATE_CITY = "runUpdateCity";

	private final String GET_COUNTRY = "runGetCountry";
	private final String INSERT_COUNTRY = "runInsertCountry";
	private final String DELETE_COUNTRY = "runDeleteCountry";
	private final String UPDATE_COUNTRY = "runUpdateCountry";
	
	private final String GET_CUSTOMERS_BY_USERNAME = "runGetCustomersByUsername";	
	private final String GET_CUSTOMERS = "runGetCustomers";
	private final String GET_CUSTOMER = "runGetCustomer";
	private final String INSERT_CUSTOMER = "runInsertCustomer";
	private final String DELETE_CUSTOMER = "runDeleteCustomer";
	private final String UPDATE_CUSTOMER = "runUpdateCustomer";
	
	/*
	* Public API
	*/
	public Object getAddress(Object queryObj) {
		return executeQuery(getQueryMethod(GET_ADDRESS), queryObj);
	}

	public int insertAddress(Object queryObj ) {
		return (int) executeQuery(getQueryMethod(INSERT_ADDRESS), queryObj);
	}

	public void deleteAddress(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_ADDRESS), queryObj);
	}

	public void updateAddress(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_ADDRESS), queryObj);
	}

	public Object getAppointmentsByUser(Object queryObj) {
		return executeQuery(getQueryMethod(GET_APPOINTMENTS_BY_USER), queryObj);
	}

	public Object getAllAppointments() {
		return executeQuery(getQueryMethod(GET_ALL_APPOINTMENTS), null);
	}

	public Object getAppointment(Object queryObj) {
		return executeQuery(getQueryMethod(GET_APPOINTMENT), queryObj);
	}

	public void insertAppointment(Object queryObj ) {
		executeQuery(getQueryMethod(INSERT_APPOINTMENT), queryObj);
	}

	public void deleteAppointment(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_APPOINTMENT), queryObj);
	}

	public void updateAppointment(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_APPOINTMENT), queryObj);
	}

	public Object getCity(Object queryObj) {
		return executeQuery(getQueryMethod(GET_CITY), queryObj);
	}

	public int insertCity(Object queryObj ) {
		return (int) executeQuery(getQueryMethod(INSERT_CITY), queryObj);
	}

	public void deleteCity(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_CITY), queryObj);
	}

	public void updateCity(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_CITY), queryObj);
	}

	public Object getCountry(Object queryObj) {
		return executeQuery(getQueryMethod(GET_COUNTRY), queryObj);
	}

	public int insertCountry(Object queryObj ) {
		return (int) executeQuery(getQueryMethod(INSERT_COUNTRY), queryObj);
	}

	public void deleteCountry(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_COUNTRY), queryObj);
	}

	public void updateCountry(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_COUNTRY), queryObj);
	}

	public Object getCustomersByUsername(Object queryObj) {
		return executeQuery(getQueryMethod(GET_CUSTOMERS_BY_USERNAME), queryObj);
	}

	public Object getCustomers(Object queryObj) {
		return executeQuery(getQueryMethod(GET_CUSTOMERS), queryObj);
	}

	public Object getCustomer(Object queryObj) {
		return executeQuery(getQueryMethod(GET_CUSTOMER), queryObj);
	}

	public void insertCustomer(Object queryObj ) {
		executeQuery(getQueryMethod(INSERT_CUSTOMER), queryObj);
	}

	public void deleteCustomer(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_CUSTOMER), queryObj);
	}

	public void updateCustomer(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_CUSTOMER), queryObj);
	}

	public Object getAllUsers(Object queryObj) {
		return executeQuery(getQueryMethod(GET_ALL_USERS), null);	
	}

	public Object getUser(Object queryObj) {
		return executeQuery(getQueryMethod(GET_USER), queryObj);
	}

	public Object getUserByUsernameAndPassword(Object queryObj) {
		return executeQuery(getQueryMethod("runGetUserByUsernameAndPassword"), queryObj);
	}

	public Object getUserByUsername(Object queryObj) {
		return executeQuery(getQueryMethod("runGetUserByUsername"), queryObj);
	}

	public int insertUser(Object queryObj ) {
		return (int) executeQuery(getQueryMethod(INSERT_USER), queryObj);
	}

	public void deleteUser(Object queryObj) {
		executeQuery(getQueryMethod(DELETE_USER), queryObj);
	}

	public void updateUser(Object queryObj) {
		executeQuery(getQueryMethod(UPDATE_USER), queryObj);
	}

	/*
	* Private API
	* These functions run queries against the database and are mapped to the public API via reflection.
	*/

	private void runDeleteAddress(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM address WHERE addressId = ?";
		int addressId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, addressId);
			ResultSet cursor = statement.executeQuery();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	private Object runGetAddress(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM address WHERE addressId = ?";
		Address address = null;	
		int addressId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, addressId);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				address = new Address(
					cursor.getInt("addressId"), 
					cursor.getString("address"),
					cursor.getString("address2"),
					cursor.getInt("cityId"),
					cursor.getString("postalCode"),
					cursor.getString("phone"),
					cursor.getString("createdBy")
				);
			}
			return address;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return address;
		}
	}

	private void runUpdateAddress(Connection conn, Object queryObj) {
		Address address = (Address) queryObj;
		String queryString = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?,  createdBy = ?,"
			+ " lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, address.getAddress());
			statement.setString(2, address.getAddress2());
			statement.setInt(3, address.getCityId());
			statement.setString(4, address.getPostalCode());
			statement.setString(5, address.getPhone());
			statement.setString(6, address.getCreatedBy());
			statement.setTimestamp(7, new Timestamp(timestamp));
			statement.setString(8, address.getCreatedBy());
			statement.setInt(9, address.getAddressId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}



	private int runInsertAddress(Connection conn,  Object queryObj){
		Address address = (Address) queryObj;
		String queryString = "INSERT INTO address (address, address2, cityId, postalCode, phone, createdBy, createDate, lastUpdate, lastUpdateBy)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int addressId = 0;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, address.getAddress());
			statement.setString(2, address.getAddress2());
			statement.setInt(3, address.getCityId());
			statement.setString(4, address.getPostalCode());
			statement.setString(5, address.getPhone());
			statement.setString(6, address.getCreatedBy());
			statement.setDate(7, new java.sql.Date(timestamp));
			statement.setTimestamp(8, new Timestamp(timestamp));
			statement.setString(9, address.getCreatedBy());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next()) {
				addressId = rs.getInt(1);
			}
			return addressId;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			return addressId;
		}
	}


	private void runDeleteAppointment(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM appointment WHERE appointmentId = ?";
		int appointmentId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, appointmentId);
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	private Object runGetAppointment(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM appointment WHERE appointmentId = ?";
		Appointment appointment = null;	
		int appointmentId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, appointmentId);
			ResultSet cursor = statement.executeQuery();

			while(cursor.next()) {
				appointment = new Appointment(
					cursor.getInt("appointmentId"), 
					cursor.getInt("customerId"),
					cursor.getString("createdBy"),
					cursor.getString("title"),
					cursor.getString("description"),
					cursor.getString("contact"),
					cursor.getString("location"),
					cursor.getString("url"),
					new Date(cursor.getTimestamp("start").getTime()),
					new Date(cursor.getTimestamp("end").getTime())
				);
			}
			return appointment;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return appointment;
		}
	}

	private void runUpdateAppointment(Connection conn, Object queryObj) {
		Appointment appointment = (Appointment) queryObj;
		String queryString = "UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end =?,"
			+ " lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?";

		 ZonedDateTime startUTC = appointment.getStart().toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime endUTC = appointment.getEnd().toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));            
		
		Timestamp startTimestamp = Timestamp.valueOf(startUTC.toLocalDateTime()); //this value can be inserted into database
		Timestamp endTimestamp = Timestamp.valueOf(endUTC.toLocalDateTime()); //this value can be inserted into database 

		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, appointment.getTitle());
			statement.setString(2, appointment.getDescription());
			statement.setString(3, appointment.getLocation());
			statement.setString(4, appointment.getContact());
			statement.setString(5, appointment.getUrl());
			statement.setTimestamp(6, startTimestamp);
			statement.setTimestamp(7, endTimestamp);
			statement.setTimestamp(8, new Timestamp(timestamp));
			statement.setString(9, appointment.getLastUpdateBy());
			statement.setInt(10, appointment.getAppointmentId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void runInsertAppointment(Connection conn,  Object queryObj){
		Appointment appointment = (Appointment) queryObj;
		String queryString = "INSERT INTO appointment (customerId, title, description, location, contact, url, start, end, createdBy, createDate, lastUpdate, lastUpdateBy)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		 ZonedDateTime startUTC = appointment.getStart().toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime endUTC = appointment.getEnd().toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));            
		
		Timestamp startTimestamp = Timestamp.valueOf(startUTC.toLocalDateTime()); 
		Timestamp endTimestamp = Timestamp.valueOf(endUTC.toLocalDateTime());  

		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setInt(1, appointment.getCustomerId());
			statement.setString(2, appointment.getTitle());
			statement.setString(3, appointment.getDescription());
			statement.setString(4, appointment.getLocation());
			statement.setString(5, appointment.getContact());
			statement.setString(6, appointment.getUrl());
			statement.setTimestamp(7, startTimestamp);
			statement.setTimestamp(8, endTimestamp);
			statement.setString(9, appointment.getCreatedBy());
			statement.setTimestamp(10, new Timestamp(timestamp));
			statement.setTimestamp(11, new Timestamp(timestamp));
			statement.setString(12, appointment.getCreatedBy());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}


	private void runDeleteCity(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM city WHERE cityId = ?";
		int cityId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, cityId);
			ResultSet cursor = statement.executeQuery();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	private Object runGetCity(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM city WHERE cityId = ?";
		City city = null;	
		int cityId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, cityId);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				city = new City(
					cursor.getInt("cityId"), 
					cursor.getString("city"),
					cursor.getInt("countryId"),
					cursor.getString("createdBy"));
			}
			return city;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return city;
		}
	}

	private void runUpdateCity(Connection conn, Object queryObj) {
		City city = (City) queryObj;
		String queryString = "UPDATE city SET city = ?, countryId = ?, createdBy = ?,"
			+ " createDate = ?, lastUpdate = ?, lastUpdateBy = ? WHERE cityId = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, city.getCity());
			statement.setInt(2, city.getCountryId());
			statement.setString(3, city.getCreatedBy());
			statement.setDate(4, new java.sql.Date(timestamp));
			statement.setTimestamp(5, new Timestamp(timestamp));
			statement.setString(6, city.getCreatedBy());
			statement.setInt(7, city.getCityId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private int runInsertCity(Connection conn,  Object queryObj){
		City city = (City) queryObj;
		String queryString = "INSERT INTO city (city, countryId, createdBy, createDate, lastUpdate, lastUpdateBy)" + " values (?, ?, ?, ?, ?, ?)";
		int cityId = 0;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, city.getCity());
			statement.setInt(2, city.getCountryId());
			statement.setString(3, city.getCreatedBy());
			statement.setDate(4, new java.sql.Date(timestamp));
			statement.setTimestamp(5, new Timestamp(timestamp));
			statement.setString(6, city.getCreatedBy());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next()) {
				cityId = rs.getInt(1);
			}
			return cityId;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return cityId;
		}
	}


	private void runDeleteCountry(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM country WHERE countryId = ?";
		int countryId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, countryId);
			ResultSet cursor = statement.executeQuery();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	private Object runGetCountry(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM country WHERE countryId = ?";
		Country country = null;	
		int countryId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, countryId);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				country = new Country(
					cursor.getInt("countryId"), 
					cursor.getString("country"),
					cursor.getString("createdBy"));
			}
			return country;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return country;
		}
	}

	private void runUpdateCountry(Connection conn, Object queryObj) {
		Country country = (Country) queryObj;
		String queryString = "UPDATE country SET country = ?,"
			+ " createDate = ?, lastUpdate = ?, lastUpdateBy = ? WHERE countryId = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, country.getCountry());
			statement.setDate(2, new java.sql.Date(timestamp));
			statement.setTimestamp(3, new Timestamp(timestamp));
			statement.setString(4, country.getCreatedBy());
			statement.setInt(5, country.getCountryId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private int runInsertCountry(Connection conn,  Object queryObj){
		Country country = (Country) queryObj;
		String queryString = "INSERT INTO country (country, createdBy, createDate, lastUpdate, lastUpdateBy)" + " values (?, ?, ?, ?, ?)";
		int countryId = 0;	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, country.getCountry());
			statement.setString(2, country.getCreatedBy());
			statement.setDate(3, new java.sql.Date(timestamp));
			statement.setTimestamp(4, new Timestamp(timestamp));
			statement.setString(5, country.getCreatedBy());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next()) {
				countryId = rs.getInt(1);
			}
			return countryId;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return countryId;
		}
	}


	private void runDeleteCustomer(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM customer WHERE customerId = ?";
		int customerId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, customerId);
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private Object runGetAllAppointments(Connection conn, Object queryObj) {
		String queryString = "SELECT * FROM appointment";
		ArrayList<Appointment> appointments = new ArrayList<>();	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

				Timestamp start = cursor.getTimestamp("start");
				ZonedDateTime baseStart = start.toLocalDateTime().atZone(ZoneId.of("UTC"));
				ZonedDateTime localStart = baseStart.withZoneSameInstant(ZoneId.systemDefault());

				Timestamp end = cursor.getTimestamp("end");
				ZonedDateTime baseEnd = end.toLocalDateTime().atZone(ZoneId.of("UTC"));
				ZonedDateTime localEnd = baseEnd.withZoneSameInstant(ZoneId.systemDefault());

				Appointment appointment = new Appointment(
					cursor.getInt("appointmentId"), 
					cursor.getInt("customerId"),
					cursor.getString("createdBy"),
					cursor.getString("title"),
					cursor.getString("description"),
					cursor.getString("contact"),
					cursor.getString("location"),
					cursor.getString("url"),
					new Date(cursor.getTimestamp("start").getTime()),
					new Date(cursor.getTimestamp("end").getTime()));

				appointments.add(appointment);
			}
			return appointments;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return appointments;
		}

	} 

	private Object runGetAppointmentsByUser(Connection conn, Object queryObj) {
		String queryString = "SELECT * FROM appointment WHERE createdBy = ?";
		User user = (User) queryObj;
		ArrayList<Appointment> appointments = new ArrayList<>();	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setString(1, user.getUserName());
			ResultSet cursor = statement.executeQuery();

			while(cursor.next()) {
				Appointment appointment = new Appointment(
					cursor.getInt("appointmentId"), 
					cursor.getInt("customerId"),
					cursor.getString("createdBy"),
					cursor.getString("title"),
					cursor.getString("description"),
					cursor.getString("contact"),
					cursor.getString("location"),
					cursor.getString("url"),
					new Date(cursor.getTimestamp("start").getTime()),
					new Date(cursor.getTimestamp("end").getTime()));

				appointments.add(appointment);
			}
			return appointments;
		} catch (Exception e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return appointments;
		}

	}

	private Object runGetCustomersByUsername(Connection conn, Object queryObj) {
		String queryString = "SELECT * FROM customer WHERE createdBy = ?";
		User user = (User) queryObj;
		ArrayList<Customer> customers = new ArrayList<>();	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setString(1, user.getUserName());
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				Customer customer = new Customer(
					cursor.getInt("customerId"), 
					cursor.getString("customerName"), 
					cursor.getInt("addressId"), 
					cursor.getInt("active"),
					cursor.getString("createdBy"));
				customers.add(customer);
			}
			return customers;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return customers;
		}


	}

	private Object runGetCustomers(Connection conn, Object queryObj) {
		String queryString = "SELECT * FROM customer";
		ArrayList<Customer> customers = new ArrayList<>();	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				Customer customer = new Customer(
					cursor.getInt("customerId"), 
					cursor.getString("customerName"), 
					cursor.getInt("addressId"), 
					cursor.getInt("active"),
					cursor.getString("createdBy"));
				customers.add(customer);
			}
			return customers;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return customers;
		}

	}
	
	private Object runGetCustomer(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM customer WHERE customerId = ?";
		Customer customer = null;	
		int customerId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, customerId);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				customer = new Customer(
					cursor.getInt("customerId"), 
					cursor.getString("customerName"), 
					cursor.getInt("addressId"), 
					cursor.getInt("active"),
					cursor.getString("createdBy"));
			}
			return customer;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return customer;
		}
	}

	private void runUpdateCustomer(Connection conn, Object queryObj) {
		Customer customer = (Customer) queryObj;
		String queryString = "UPDATE customer SET customerName = ?, addressId = ?, active = ? WHERE customerId = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setString(1, customer.getCustomerName());
			statement.setInt(2, customer.getAddressId());
			statement.setInt(3, customer.getActive());
			statement.setInt(4, customer.getCustomerId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void runInsertCustomer(Connection conn,  Object queryObj){
		Customer customer = (Customer) queryObj;
		String queryString = "INSERT INTO customer (customerName, addressId, active, createdBy, createDate, lastUpdate, lastUpdateBy)" + " values (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, customer.getCustomerName());
			statement.setInt(2, customer.getAddressId());
			statement.setInt(3, customer.getActive());
			statement.setString(4, customer.getCreatedBy());
			statement.setDate(5, new java.sql.Date(timestamp));
			statement.setTimestamp(6, new Timestamp(timestamp));
			statement.setString(7, customer.getCreatedBy());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void runDeleteUser(Connection conn, Object queryObj) {
		String queryString = "DELETE FROM user WHERE userId = ?";
		int userId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, userId);
			ResultSet cursor = statement.executeQuery();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private Object runGetAllUsers(Connection conn, Object queryObj) {
		String queryString = "SELECT * FROM user";
		ArrayList<User> users = new ArrayList<User>();
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				User user = new User(
					cursor.getInt("userId"), 
					cursor.getString("userName"), 
					cursor.getInt("active"));
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return users;
		}
	}

	private Object runGetUserByUsername(Connection conn, Object queryObj) {
		String userName = (String) queryObj;
		String queryString = "SELECT * FROM user WHERE  userName = ?";
		User user = null;	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setString(1, userName);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				user = new User(
					cursor.getInt("userId"), 
					cursor.getString("userName"), 
					cursor.getInt("active"));
			}
			return user;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return user;
		}

	}

	private Object runGetUserByUsernameAndPassword(Connection conn, Object queryObj) {
		ArrayList<String> userNameAndPassword = (ArrayList<String>) queryObj;
		String queryString = "SELECT * FROM user WHERE  userName = ? AND password = ?";
		User user = null;	
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setString(1, userNameAndPassword.get(0));
			statement.setString(2, userNameAndPassword.get(1));
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				user = new User(
					cursor.getInt("userId"), 
					cursor.getString("userName"), 
					cursor.getInt("active"));
			}
			return user;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return user;
		}

	}
	
	private Object runGetUser(Connection conn, Object queryObj){
		String queryString = "SELECT * FROM user WHERE userId = ?";
		User user = null;	
		int userId = (int) queryObj;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			statement.setInt(1, userId);
			ResultSet cursor = statement.executeQuery();
			while(cursor.next()) {
				user = new User(
					cursor.getInt("userId"), 
					cursor.getString("userName"), 
					cursor.getInt("active"));
			}
			return user;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return user;
		}
	}

	private void runUpdateUser(Connection conn, Object queryObj) {
		User user = (User) queryObj;
		String queryString = "UPDATE user SET userName = ?, password = ?, active = ?, createBy = ?,"
			+ " createDate = ?, lastUpdatedBy = ? WHERE userId = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(queryString);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getPassword());
			statement.setInt(3, user.getActive());
			statement.setString(4, user.getUserName());
			statement.setDate(5, new java.sql.Date(timestamp));
			statement.setTimestamp(6, new Timestamp(timestamp));
			statement.setString(7, user.getUserName());
			statement.setInt(8, user.getUserId());
			statement.execute();
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private int runInsertUser(Connection conn,  Object queryObj){
		User user = (User) queryObj;
		String queryString = "INSERT INTO user (userName, password, active, createBy, createDate, lastUpdate, lastUpdatedBy)" + " values (?, ?, ?, ?, ?, ?, ?)";
		int userId = 0;
		try {
			PreparedStatement statement = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			Long timestamp = System.currentTimeMillis();
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getPassword());
			statement.setInt(3, 1);
			statement.setString(4, user.getUserName());
			statement.setDate(5, new java.sql.Date(timestamp));
			statement.setTimestamp(6, new Timestamp(timestamp));
			statement.setString(7, user.getUserName());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next()) {
				userId = rs.getInt(1);
			}
			return userId;
		} catch (SQLException e) {
			  Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			  return userId;
		}
	}
	
	/*
	* This function invokes a given query with the specified query object. In order to keep this function generic,
	* the 'Object' type is used here and is cast into it's specific type by the query function that uses it.
	* This function is meant to be used by the public query API.
	*/
	private Object executeQuery(Method query, Object queryObj) {
	   try (Connection connection = DriverManager.getConnection(DATABASE_URL+"user=U04tYO" + "&password=53688344230")) {
		return query.invoke(this, connection, queryObj);
	   } catch (Exception e) {
		Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
		return null;
	   }
	}
	
	/*
	* This helper method uses reflection to map function name constants to their corresponding private API method.
	*/
	private Method getQueryMethod(String methodName) {
		try {	
			return DatabaseQueryBank.class.getDeclaredMethod(methodName, Connection.class, Object.class);
		} catch (NoSuchMethodException e) {
			Logger.getLogger(DatabaseQueryBank.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}
}
