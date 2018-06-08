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
public class InvalidCustomerDataException extends Exception {

    public InvalidCustomerDataException(String message) {
        super(message);
    }

    public InvalidCustomerDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
	
}
