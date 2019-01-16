package com.skafenko.cisco.threat.grid.api;

import com.skafenko.cisco.threat.grid.api.model.json.FileScanMetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EngineTest {
    public static void main(String[] args) {
        ThreatGridClient client = ThreatGridClient.configure("ddluf46pqudgv6ppmn7f6sa559");
        try {

            File file = new File("C:\\Users\\m.skafenko\\Desktop\\fileStorage\\audio.mp3");
            client.setPrivate(true).setCallbackUrl("https://1ed03711.ngrok.io/TRUM/trum/clients/sample");
            FileScanMetaData response = client.scanFile(new FileInputStream(file), "audio.mp3");
            System.out.println(response);

            FileScanMetaData ciscoResponse = client.getFileReport(response.getData().getId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
