/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skafenko.cisco.threat.grid.api.exception;

import java.net.MalformedURLException;

public class InvalidURLException extends MalformedURLException {

    public InvalidURLException() {
    }

    public InvalidURLException(String msg) {
        super(msg);
    }
}
