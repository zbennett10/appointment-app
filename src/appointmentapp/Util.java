/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentapp;

import java.util.regex.Pattern;

/**
 *
 * @author Zachary Bennett
 * This class provides application utility functions.
 */
public class Util {
	/*
	* This function makes sure that a given field does not container numbers or special characters.
	*/
	public static boolean isStringFieldInvalid(String field) {
		return Pattern.compile("[0-9\\.\\-\\/()]+", Pattern.CASE_INSENSITIVE)
			      .matcher(field)
		     	      .find();
	}
	
	/*
	* This function makes sure that a string value is not whitespace alone.
	*/
	public static boolean isFieldOnlyWhitespace(String fieldValue) {
		return fieldValue.trim().length() == 0;
	}
	
}
