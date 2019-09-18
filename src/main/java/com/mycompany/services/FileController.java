/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import java.net.UnknownHostException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dependencies
 */
import com.google.gson.Gson;
import com.mycompany.models.FileInfo;
import com.mycompany.models.FileRegistrationQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.mycompany.models.Response;
import com.mycompany.models.ServerPool;
import com.mycompany.models.ServerRegistrationQuery;
import com.mycompany.utils.Constants;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

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
        return gson.toJson(new Response(true, "", gson.toJson(ServerPool.getFileList())));
    }
    
    @GET
    @Path("/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getFile(@PathParam("filename") String filename) {
        String server = selectServerToDownloadFile(filename);
        if(!server.equals("")){
            try {
                InputStream is = loadFileFromServer(server, filename);
                return javax.ws.rs.core.Response
                    .ok(is, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"" ) 
                    .build();
            } catch (Exception ex) {
                return javax.ws.rs.core.Response
                    .status(javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE)
                    .build();
            }
        }else{
            //Respond with 404
            return javax.ws.rs.core.Response
                .status(javax.ws.rs.core.Response.Status.NOT_FOUND)
                .build();
        }
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addServerToServerList(String body){
        FileRegistrationQuery frq = gson.fromJson(body, FileRegistrationQuery.class);
        int bookmark = 0;
        if(ServerPool.contains(frq.getFilename())){
            bookmark = ServerPool.getIndexOf(frq.getFilename());
            ServerPool.files.get(bookmark).addFileSource(frq.getServerIp());
        }else{
            FileInfo fi = new FileInfo(frq.getFilename());
            ServerPool.addFile(frq.getFilename(), frq.getServerIp());
        }
        System.out.println(
            "[LBS] File (" +
            frq.getFilename() +
            ") notification from server ip: " 
            +frq.getServerIp()
        );
        
        return gson.toJson(
            new Response(
                true, 
                "", 
                "File (" +frq.getFilename() +
                ") added from server@" 
                +frq.getServerIp()
            )
        );
    }
    
    private String selectServerToDownloadFile(String filename){
        return ServerPool.selectSourceForFile(filename);
    }
    
    private InputStream loadFileFromServer(String server, String filename) throws Exception {

        String url = "http://" +server +":8080/FileServer/api/files" +"/" +filename;

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", Constants.USER_AGENT);
        
        System.out.println("[LBS] Requesting " +filename +" to server@" +server);
        
        HttpResponse response = client.execute(request);

        return response.getEntity().getContent();
    }
}
