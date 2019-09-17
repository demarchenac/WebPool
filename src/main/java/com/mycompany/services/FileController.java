/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dependencies
 */
import com.google.gson.Gson;
import com.mycompany.models.Response;
import com.mycompany.models.ServerAddress;
import com.mycompany.models.ServerPool;
import com.mycompany.models.SinglePropQuery;
import com.mycompany.utils.Constants;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author user
 */
@Path("files")
public class FileController 
{
    private final Gson gson;
    
    public FileController() {
        this.gson = new Gson();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAllFiles() throws UnknownHostException{
        ArrayList<String> response = new ArrayList<String>();
        ArrayList<String> tempFileList;
        for (String server : ServerPool.serverList) {
            try {
                tempFileList = getFileListFromServer(server);
                for(String file : tempFileList){
                    if(!response.contains(file)){
                        response.add(file);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return gson.toJson(new Response(true, "", gson.toJson(response)));
    }
    
    private ArrayList getFileListFromServer(String server) throws Exception {

        String url = "http://" +server +":8080/FileServer/api/files";

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        // add header
        request.setHeader("User-Agent", Constants.USER_AGENT);

        System.out.println("[FS] Sending register request");
        HttpResponse response = client.execute(request);
        System.out.println("[FS] Response Code : " + 
                            response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
                result.append(line);
        }
        
        System.out.println("[LBS] "+result.toString());
        
        Response r = gson.fromJson(result.toString(), Response.class);
        ArrayList<String> fileList = gson.fromJson(r.getData(), ArrayList.class);
        return fileList;
    }
}
