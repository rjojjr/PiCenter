package com.kirchnersolutions.PiCenter.servers.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Results implements ObjectInterface {

    private List<Map<String, String>> results = null;
    private boolean success = false;
    private String message = "";

    public void setResults(List<Map<String, String>> results) {
        this.results = results;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getResults() {
        if (results != null) {
            return new ArrayList<>(results);
        }else {
            return null;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public byte getHeader() throws Exception{
        return "4".getBytes("UTF-8")[0];
    }
}