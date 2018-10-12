/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wserver;

import java.net.ServerSocket;

/**
 *
 * @author Md Mahmudul Hasan
 */

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WServer {

    /**
     * @param args the command line arguments
     */
    
    static int clientCount;
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        ServerSocket ss = new ServerSocket(8081);
        clientCount = 0;
            while (clientCount < 10) {
                
                Socket sc = ss.accept();
		
                Thread clientThread = new Thread (new service(sc));
                clientThread.start();
                
                clientCount++;
		
            }
    }
    
}

class service implements Runnable {
    
    Socket sc;
    int input, output, count = 0;
    DataInputStream dis ;
    DataOutputStream dos;
    
    public service(Socket s) throws IOException{
        sc = s;
        dis = new DataInputStream(sc.getInputStream());
        dos = new DataOutputStream(sc.getOutputStream());
    }
    
    public void run() {
        try {
            input = dis.readInt();
            System.out.println("Input :"+input);
            output = input * input;
            System.out.println(output);
            
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(service.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            dos.writeInt(input);
            dos.writeInt(output);
            WServer.clientCount --;
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}