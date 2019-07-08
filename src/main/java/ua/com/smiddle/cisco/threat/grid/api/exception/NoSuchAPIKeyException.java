/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.smiddle.cisco.threat.grid.api.exception;

public class NoSuchAPIKeyException extends RuntimeException {

    public NoSuchAPIKeyException() {
    }

    public NoSuchAPIKeyException(String message) {
        super(message);
    }

    public NoSuchAPIKeyException(Throwable cause) {
        super(cause);
    }

    public NoSuchAPIKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
