/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.models;

import java.util.ArrayList;

/**
 *
 * @author M4rchen
 */
public class FileInfo {
    private String filename;
    private String lastSource;
    private int lastSourceIndex;
    private ArrayList<String> availableSoruces;

    public FileInfo(String filename) {
        this.filename = filename;
        this.availableSoruces = new ArrayList<String>();
        this.lastSource = "0.0.0.0";
        this.lastSourceIndex = -1;
    }

    public void addFileSource(String source){
        if(!availableSoruces.contains(source)){
            availableSoruces.add(source);
        }
    }
    
    public void removeFileSource(String source){
        if(availableSoruces.contains(source)){
            availableSoruces.remove(source);
        }
    }
    
    public boolean fileSourceExists(String source){
        return availableSoruces.contains(source);
    }
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ArrayList<String> getAvailableSoruces() {
        return availableSoruces;
    }

    public void setAvailableSoruces(ArrayList<String> availableSoruces) {
        this.availableSoruces = availableSoruces;
    }

    public String getLastSource() {
        return lastSource;
    }

    public void setLastSource(String lastSource) {
        this.lastSource = lastSource;
        
    }

    public int getLastSourceIndex() {
        return lastSourceIndex;
    }

    public void setLastSourceIndex(int lastSourceIndex) {
        this.lastSourceIndex = lastSourceIndex;
    }
}
