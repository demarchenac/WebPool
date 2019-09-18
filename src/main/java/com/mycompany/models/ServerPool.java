/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.models;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class ServerPool {
    public static ArrayList<String> serverList = new ArrayList<String>();
    public static ArrayList<FileInfo> files = new ArrayList<FileInfo>();
    
    public static void addFile(String filename, String serverSource){
        FileInfo fi = new FileInfo(filename);
        fi.addFileSource(serverSource);
        files.add(fi);
    }

    public static boolean contains(String filename){
        boolean response = false;
        for(FileInfo file: files){
            if(file.getFilename().equals(filename)){
                response = true;
                break;
            }
        }
        return response;
    }
    
    public static int getIndexOf(String filename){
        int response = -1;
        for(FileInfo file: files){
            if(file.getFilename().equals(filename)){
                response = files.indexOf(file);
                break;
            }
        }
        return response;
    }

    public static ArrayList<String> getFileList(){
    
        ArrayList<String> response = new ArrayList<String>();
        for(FileInfo fi: files){
            response.add(fi.getFilename());
        }
        return response;
    }
    
    public static void removeSoruce(String source){
        for(FileInfo fi: files){
            if(fi.getAvailableSoruces().contains(source)){
               fi.removeFileSource(source);
            }
        }
    }
    
    public static String selectSourceForFile(String filename){
        String response = "";
        
        int index = getIndexOf(filename);
        if(index >= 0){
            FileInfo fi = files.get(index);
            ArrayList<String> sources = fi.getAvailableSoruces(); 
            if(sources.size() > 0){
                if(fi.getLastSource().equals("0.0.0.0")){
                    fi.setLastSourceIndex(0);
                    fi.setLastSource(sources.get(0));
                    response = sources.get(0);
                }else if(fi.fileSourceExists(fi.getLastSource())){
                    if(fi.getLastSourceIndex() >= sources.size() -1){
                        fi.setLastSourceIndex(0);
                        fi.setLastSource(sources.get(0));
                        response = sources.get(0);
                    }else{
                        fi.setLastSourceIndex(fi.getLastSourceIndex() +1);
                        fi.setLastSource(sources.get(fi.getLastSourceIndex() +1));
                        response = sources.get(fi.getLastSourceIndex() +1);
                    }
                }else if(!fi.fileSourceExists(fi.getLastSource())){
                    if(fi.getLastSourceIndex() >= sources.size() -1){
                        fi.setLastSourceIndex(0);
                        fi.setLastSource(sources.get(0));
                        response = sources.get(0);
                    }else{
                        fi.setLastSource(sources.get(fi.getLastSourceIndex()));
                        response = sources.get(fi.getLastSourceIndex());
                    }
                }
            }
        }
        return response;
    }
}
