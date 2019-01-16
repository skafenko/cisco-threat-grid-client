package com.skafenko.cisco.threat.grid.api.model;


public enum VirtualMachine {
    WINDOWS_7("win7-x64"),
    WINDOWS_7_PROFILE("win7-x64-2"),
    WINDOWS_7_JAPANESE("win7-x64-jp"),
    WINDOWS_7_KOREAN("win7-x64-kr"),
    NONE(""),
    WINDOWS_10("win10");


    private String value;

    VirtualMachine(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
