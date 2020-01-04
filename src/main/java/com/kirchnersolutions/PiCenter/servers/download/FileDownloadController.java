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
                              HttpServletResponse response, @RequestParam String token) throws Exception{
        //Authorized user will download the file
        if(token != null){
            if(!userService.validateToken(token)){
                response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
                return;
            }
        }
        userService.logDownload(token, request.getRequestURI());
        Path file = Paths.get("PiCenter/Backup/Download/PiCenterDownload.zip");
        if (Files.exists(file)) {
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=PiCenterDownload.zip");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment; filename=PiCenterDownload.zip");
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
                               HttpServletResponse response, @RequestParam String date, @RequestParam String token) throws Exception{
        if(token != null){
            if(!userService.validateAdminToken(token)){
                response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
                return;
            }
        }
        userService.logDownload(token, request.getRequestURI());
        Path file = Paths.get("PiCenter/Backup/automated/PiCenterBackup_" + date + ".zip");
        if(date == null){
            downloadFallback(response, date);
            return;

        }
        //Authorized user will download the file
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
            downloadFallback(response, date);
        }
    }

    private void downloadFallback(HttpServletResponse response, @RequestParam String date) {
        File[] files = (new File("PiCenter/Backup/automated")).listFiles();
        if(files.length > 0){
            String filePath = files[0].getPath();
            Path altFile = Paths.get(filePath);
            String fileName = filePath.split("/")[filePath.split("/").length - 1];
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=" + altFile.getFileName());
            try {
                Files.copy(altFile, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
                response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            }
        }else {
            response.setStatus( HttpServletResponse.SC_NOT_FOUND );
        }
    }
}
