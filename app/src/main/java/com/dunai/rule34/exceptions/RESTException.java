package com.dunai.rule34.exceptions;

/**
 * Created by anderson on 14.07.15.
 */
public class RESTException extends Exception {
    public RESTException(String message) {
        super("REST ERROR: " + message);
    }
}
