package com.skafenko.cisco.threat.grid.api;

import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanMetaData;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class EngineTest {
    public static void main(String[] args) {
        ThreatGridClient client = ThreatGridClient.configure("ddluf46pqudgv6ppmn7f6sa559");
        try {

            File file = new File("C:\\Users\\m.skafenko\\Desktop\\fileStorage\\audio.mp3");
            client.setPrivate(true)
                    .setCallbackUrl("https://1ed03711.ngrok.io/TRUM/trum/clients/sample")
                    .setVM(VirtualMachine.WINDOWS_10);
            FileScanReport response = client.scanFile(file, "audio.mp3", "tag");
            FileScanReport response2 = client.scanFile(file, "audio.mp3", "tag");
//            FileScanMetaData translate = client.scanUrl("https://translate.google.ru/", "translate", Playbook.RUN_DIALOG_BOX_IE);
//            System.out.println(translate);
            client.getFilesReport(Arrays.asList(response.getId(), response2.getId()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
