/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mclient;

/**
 *
 * @author Md mahmudul Hasan
 */

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Socket sc = new Socket("localhost", 8080);
            
            DataInputStream dis;
            DataOutputStream dos;          
            Scanner scanner = new Scanner(System.in);
            
            dis = new DataInputStream(sc.getInputStream());
            dos = new DataOutputStream(sc.getOutputStream()); 
            
            while(true)
            {
                int i, count, input, output;
                for(i =0;i<50;i++)
                {
                    dos.writeInt(i);
                }
                count = dis.readInt();

                for(i=0; i<count;i++)
                {
                    input = dis.readInt();
                    output = dis.readInt();
                    System.out.println(input+" = "+output);
                }

                System.out.println("Run Again, Press Y/N");
                String str = scanner.nextLine();
                if (str.equalsIgnoreCase("N")) 
                {
                    break;
                }
            }
                
            
            //Thread inputSend = new Thread(new sender(sc));
            //inputSend.start();
            
            //Thread squareReceive = new Thread(new receiver(sc));
            //squareReceive.start();
            
        } catch (IOException ex) {
            Logger.getLogger(MClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

/*

class sender implements Runnable {
    
    DataOutputStream dos;          
    Scanner scanner = new Scanner(System.in);
    Socket sc;
    
    public sender(Socket s) throws IOException{
        sc = s;
        dos = new DataOutputStream(sc.getOutputStream()); 
    }
    
    public void run() {
        int i;
        while(true)
        {
            for(i = 0; i<50; i++)
            {
                try {
                    dos.writeInt(i);
                    System.out.println("Input : "+ i);
                } catch (IOException ex) {
                    Logger.getLogger(sender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("Run Again, Press Y/N");
            String str = scanner.nextLine();
            if (str.equalsIgnoreCase("N")) 
            {
                break;
            }
        }
            
        
    }
    
}

class receiver implements Runnable {
    
    DataInputStream dis;
    Socket sc;
    
    public receiver(Socket s) throws IOException{
        sc = s;
        dis = new DataInputStream(sc.getInputStream()); 
    }
    
    public void run() {
        int j,k;
        while (true)
        {
            try {
                j = dis.readInt();
                k = dis.readInt();
                System.out.println(j+" = "+k);
            } catch (IOException ex) {
                Logger.getLogger(receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}


*/