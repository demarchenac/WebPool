/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author user
 */
public class ServletContextManager implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            System.out.println("Started Load Balancer Service -> [LBS]");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[LBS] Service stopped!");
    }
}
