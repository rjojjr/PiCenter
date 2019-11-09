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
public class DevVars {

    private boolean devExceptions = true, stompDebug = true, debug = true, ipDebug = true, nonFatal = true, socketDebug = true, trace = true;
    private File devExceptionLogsDir, uncaughtExceptionLogsDir;

    /**
     * Creates DevelopmentException Log directory.
     */
    public DevVars(){
        File dir = new File("Database");
        uncaughtExceptionLogsDir = new File(dir, "/Dev/Trace/UncaughtExceptions");
        devExceptionLogsDir = new File(dir, "/Dev/Trace/DevelopmentExceptions");
        if(!devExceptionLogsDir.exists()){
            devExceptionLogsDir.mkdirs();
        }
        if(!uncaughtExceptionLogsDir.exists()){
            uncaughtExceptionLogsDir.mkdirs();
        }
    }

    /**
     * Sets the development variables from String[].
     * @param vars
     */
    public void setVars(String[] vars){
        if(vars[0].contains("t")){
            setDevExceptions(true);
            System.out.println("Developer exception logging is turned on");
        }else{
            setDevExceptions(false);
            System.out.println("Developer exception logging is turned off");
        }
        if(vars[1].contains("t")){
            setStompDebug(true);
            System.out.println("Stomp debugging is turned on");
        }else{
            setStompDebug(false);
            System.out.println("Stomp debugging is turned off");
        }
        if(vars[2].contains("t")){
            setIpDebug(true);
            System.out.println("IP debugging is turned on");
        }else{
            setIpDebug(false);
            System.out.println("IP debugging is turned off");
        }
        if(vars[3].contains("t")){
            setNonFatal(true);
            System.out.println("non-fatal debugging is turned on");
        }else{
            setNonFatal(false);
            System.out.println("non-fatal debugging is turned off");
        }
        if(vars[3].contains("t")){
            setNonFatal(true);
            System.out.println("non-fatal debugging is turned on");
        }else{
            setNonFatal(false);
            System.out.println("non-fatal debugging is turned off");
        }
        if(vars[4].contains("t")){
            setSocketDebug(true);
            System.out.println("Socket debugging is turned on");
        }else{
            setSocketDebug(false);
            System.out.println("Socket debugging is turned off");
        }
        if(vars[5].contains("t")){
            setTrace(true);
            System.out.println("Tracing is turned on");
        }else{
            setTrace(false);
            System.out.println("Tracing is turned off");
        }
    }

    public String[] getVars(){
        String[] vars = new String[6];
        if(isDevExceptions()){
            vars[0] = "true";
        }else{
            vars[0] = "false";
        }
        if(isStompDebug()){
            vars[1] = "true";
        }else{
            vars[1] = "false";
        }
        if(isIpDebug()){
            vars[2] = "true";
        }else{
            vars[2] = "false";
        }
        if(isNonFatal()){
            vars[3] = "true";
        }else{
            vars[3] = "false";
        }
        if(isSocketDebug()){
            vars[4] = "true";
        }else{
            vars[4] = "false";
        }
        if(isTrace()){
            vars[5] = "true";
        }else{
            vars[5] = "false";
        }
        return vars;
    }

    public String[] getVarsNames(){
        String[] vars = new String[6];
        if(isDevExceptions()){
            vars[0] = "Exception Trace:true";
        }else{
            vars[0] = "Exception Trace:false";
        }
        if(isStompDebug()){
            vars[1] = "Stomp Debugging:true";
        }else{
            vars[1] = "Stomp Debugging:false";
        }
        if(isIpDebug()){
            vars[2] = "IP Debugging:true";
        }else{
            vars[2] = "IP Debugging:false";
        }
        if(isNonFatal()){
            vars[3] = "Non Fatal Exception Debugging:true";
        }else{
            vars[3] = "Non Fatal Exception Debugging:false";
        }
        if(isSocketDebug()){
            vars[4] = "Socket Debugging:true";
        }else{
            vars[4] = "Socket Debugging:false";
        }
        if(isTrace()){
            vars[5] = "Tracing:true";
        }else{
            vars[5] = "Tracing:false";
        }
        return vars;
    }

    public void setSocketDebug(boolean socketDebug){
        this.socketDebug = socketDebug;
    }

    public boolean isSocketDebug() {
        return socketDebug;
    }

    public void setNonFatal(boolean nonFatal) {
        this.nonFatal = nonFatal;
    }

    public boolean isNonFatal() {
        return nonFatal;
    }

    public boolean isStompDebug() {
        return stompDebug;
    }

    public void setStompDebug(boolean stompDebug) {
        this.stompDebug = stompDebug;
    }

    public void setDevExceptions(boolean devExceptions) {
        this.devExceptions = devExceptions;
    }

    public boolean isDevExceptions() {
        return devExceptions;
    }

    public boolean isIpDebug() {
        return ipDebug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setIpDebug(boolean ipDebug){
        this.ipDebug = ipDebug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setTrace(boolean trace){
        this.trace = trace;
    }

    public boolean isTrace() {
        return trace;
    }

    public File getDevExceptionLogsDir() {
        return devExceptionLogsDir;
    }

    public File getUncaughtExceptionLogsDir() {
        return uncaughtExceptionLogsDir;
    }
}