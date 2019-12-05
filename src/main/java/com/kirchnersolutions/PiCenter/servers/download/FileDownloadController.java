package com.kirchnersolutions.PiCenter.servers.download;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.utilities.DeleteTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/download")
public class FileDownloadController {

    @Autowired
    private DebuggingService debuggingService;

    @RequestMapping("/backup")
    public void downloadCSV( HttpServletRequest request,
                              HttpServletResponse response) throws Exception{
        //Authorized user will download the file
        Path file = Paths.get("PiCenter/Backup/Download/PiCenterBackup.zip");
        if (Files.exists(file)) {
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=PiCenterBackup.zip");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            file.toFile().delete();
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to delete backup download file", e);
        }
    }

    @RequestMapping("/backup/auto")
    public void downloadBackup(HttpServletRequest request,
                               HttpServletResponse response, @RequestParam String date) throws Exception{
        //Authorized user will download the file
        Path file = Paths.get("PiCenter/Backup/automated/PiCenterBackup_" + date + ".zip");
        if (Files.exists(file)) {
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=PiCenterBackup.zip");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            file.toFile().delete();
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to delete backup download file", e);
        }
    }
}
