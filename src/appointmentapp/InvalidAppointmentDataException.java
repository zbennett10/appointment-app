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
public class InvalidAppointmentDataException extends Exception {

    public InvalidAppointmentDataException(String message) {
        super(message);
    }

    public InvalidAppointmentDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
	
}
