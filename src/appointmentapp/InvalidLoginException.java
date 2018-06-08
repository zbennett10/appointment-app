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
public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
