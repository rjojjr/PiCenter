package com.kirchnersolutions.PiCenter.servers.download;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.services.UserService;
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

    private DebuggingService debuggingService;
    private UserService userService;

    @Autowired
    public FileDownloadController(DebuggingService debuggingService, UserService userService){
        this.userService = userService;
        this.debuggingService = debuggingService;
    }

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
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment; filename=PiCenterBackup.zip");
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
            response.addHeader("Content-Disposition", "attachment; filename=PiCenterBackup_" + date + ".zip");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
                response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            }
        }else{
            File[] files = (new File("PiCenter/Backup/automated")).listFiles();
            if(files.length > 0){
                String filePath = files[0].getPath();
                Path altFile = Paths.get(filePath);
                String fileName = filePath.split("/")[filePath.split("/").length - 1];
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment; filename=PiCenterBackup_" + date + ".zip");
                try {
                    Files.copy(file, response.getOutputStream());
                    response.getOutputStream().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                }
            }
            response.setStatus( HttpServletResponse.SC_NOT_FOUND );
        }
    }
}
