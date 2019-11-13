package com.kirchnersolutions.PiCenter.Configuration;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SysVars {

    public static final String VERSION = "1.0.00a";
    public static final String BACKUP_VERSION = "1.0.00b";
    public static final String BUILD = "100110";
    public static final String OS = "WIN";
    //public static final String OS = "LIN";

    private String characterEncoding = "UTF-8", newLineChar = "\r\n", fileSeperator = File.separatorChar + "", passwordLife = "none", os = "win";

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setNewLineChar(String newLineChar) {
        this.newLineChar = newLineChar;
    }

    public String getNewLineChar() {
        return newLineChar;
    }

    public void setFileSeperator(String fileSeperator) {
        this.fileSeperator = fileSeperator;
    }

    public String getFileSeperator() {
        return fileSeperator;
    }

    public void setPasswordLife(String passwordLife){
        this.passwordLife = passwordLife;
    }

    public String getPasswordLife() {
        return passwordLife;
    }

    public void setVars(String[] vars){
        characterEncoding = vars[0];

        if(vars[1].contains("wi") || vars[1].contains("W")){
            os = "WIN";
            newLineChar = "\r\n";
            fileSeperator = File.separatorChar + "";
        }else{
            os = vars[1];
            newLineChar = "\n";
        }
        passwordLife = vars[2];
    }

    public String[] getVars(){
        String[] vars = new String[3];
        vars[0] = characterEncoding;
        //vars[1] = os;
        if(os.equals("WIN")){
            newLineChar = "\r\n";
        }else {
            newLineChar = "\n";
        }
        vars[2] = passwordLife;
        return vars;
    }
    public String[] getVarsNames(){
        String[] vars = new String[3];
        vars[0] = "Character Encoding:" + characterEncoding;
        vars[1] = "OS:" + os;
        if(os.equals("WIN")){
            newLineChar = "\r\n";
        }else {
            newLineChar = "\n";
        }
        vars[2] = "Password Life(Days):" + passwordLife;
        return vars;
    }
}