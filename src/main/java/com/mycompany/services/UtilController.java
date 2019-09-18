/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.google.gson.Gson;
import com.mycompany.models.FileInfo;
import com.mycompany.models.Response;
import com.mycompany.models.ServerAddress;
import com.mycompany.models.ServerPool;
import com.mycompany.models.ServerRegistrationQuery;
import com.mycompany.models.SinglePropQuery;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Path("utils")
public class UtilController {
    private final Gson gson;
    
    public UtilController() {
        this.gson = new Gson();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIp() throws UnknownHostException{
        ServerAddress serverAddress = new ServerAddress(InetAddress.getLocalHost().getHostAddress());
        return gson.toJson(new Response(true, "", this.gson.toJson(serverAddress)));
    }
    
    @GET
    @Path("/serverList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getServerList() throws UnknownHostException{
        return gson.toJson(new Response(true, "", this.gson.toJson(ServerPool.serverList)));
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addServerToServerList(String body){
        ServerRegistrationQuery sr = gson.fromJson(body, ServerRegistrationQuery.class);
        ServerPool.serverList.add(sr.getServerIp());
        int bookmark = 0;
        for(String filename: sr.getFileList()){
            if(ServerPool.contains(filename)){
                bookmark = ServerPool.getIndexOf(filename);
                ServerPool.files.get(bookmark).addFileSource(sr.getServerIp());
            }else{
                FileInfo fi = new FileInfo(filename);
                ServerPool.addFile(filename, sr.getServerIp());
            }
        }
        System.out.println("[LBS] Added server ip: " +sr.getServerIp());
        return gson.toJson(new Response(true, "", "Added server: " +sr.getServerIp()));
    }
    
    @DELETE
    @Path("/{ip}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeServerFromServerList(@PathParam("ip") String ip){
        ServerPool.serverList.remove(ip);
        ServerPool.removeSoruce(ip);
        System.out.println("[LBS] Server@" +ip +" removed");
        return gson.toJson(new Response(true, "", gson.toJson(new SinglePropQuery("Server removed"))));
    }
}
