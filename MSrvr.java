/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msrvr;

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

public class MSrvr {

    /**
     * @param args the command line arguments
     */
    
    static int clientCount;
    static int bufferCount = 0;
    static int[][] mainBuffer = new int [5][50];
    static int[] clientBufferCount = new int[5];
    
    static int workerOne = 0;
    static int workerTwo = 0;
    int speedOne = 1;
    int speedTwo = 2;
    
    //public static DataInputStream[] dis = new DataInputStream [5] ;
    //public static DataOutputStream[] dos = new DataOutputStream [5] ;
    
    
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            
            ServerSocket ss = new ServerSocket(8080);
            
            for(int z =0 ;z<5;z++)
            {
                clientBufferCount[z] = 0;
            }
            
            clientCount = 0;
            while (clientCount < 5) {
                
                Socket sc = ss.accept();
		
                Thread clientThread = new Thread (new service(sc, clientCount));
                clientThread.start();
                
                
                
                //Thread clientInput = new Thread(new receiveFromClient(sc,clientCount));
                //clientInput.start();
                
                
                
                //Thread clientOutput = new Thread(new clientSendSquare(sc,clientCount));
                //clientOutput.start();
                
                clientCount++;
		
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MSrvr.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}


class service implements Runnable {
    
    int clientNum;
    Socket sc;
    int input, output, count = 0;
    DataInputStream dis ;
    DataOutputStream dos;
    int speedOne = 1;
    int speedTwo = 2;
    
    public service(Socket s,int c) throws IOException{
        sc = s;
        clientNum = c;
        dis = new DataInputStream(sc.getInputStream());
        dos = new DataOutputStream(sc.getOutputStream());
    }
    
    public void run() {
        
        while (true)
        {
            try {
                int i;
                count = 0;
                for(i =0; i<50; i++)
                {
                    input = dis.readInt();
                    System.out.println(input);
                    
                    if(MSrvr.bufferCount<50)
                    {
                        //MSrvr.mainBuffer[clientNum][MSrvr.clientBufferCount[clientNum]] = input;
                        //MSrvr.clientBufferCount[clientNum] ++;
                        MSrvr.mainBuffer[clientNum][count] = input;
                        MSrvr.bufferCount ++;
                        count++;
                    }
                    else {
                        // drop input
                        System.out.println("Drop Input :" + input);
                    }
                    
                }
                dos.writeInt(count);
                for(i =0; i<count;i++)
                {
                    //dos.writeInt(i);
                    //dos.writeInt(i*i);
                    
                    input = MSrvr.mainBuffer[clientNum][i];
                    //MSrvr.workerOne = MSrvr.workerOne * speedOne;
                    //MSrvr.workerTwo = MSrvr.workerTwo * speedTwo;
                    
                    
                    
                    if(MSrvr.workerOne <= MSrvr.workerTwo)
                    {
                        //if(MSrvr.workerOne < 10)
                        //{
                            Socket scWrkr = new Socket("localhost", 8081);

                            DataInputStream disWrkr = new DataInputStream(scWrkr.getInputStream());
                            DataOutputStream dosWrkr = new DataOutputStream(scWrkr.getOutputStream()); 
                            
                            dosWrkr.writeInt(input);
                            MSrvr.bufferCount --;
                            MSrvr.workerOne ++ ;
                            int newInput, newOutput;
                            newInput = disWrkr.readInt();
                            newOutput = disWrkr.readInt();
                            System.out.println("From Worker One : "+newInput +" = "+ newOutput);
                           // MSrvr.workerOne -- ;
                            dos.writeInt(newInput);
                            dos.writeInt(newOutput);
                            
                            Random rand = new Random();
                            int  n = rand.nextInt(2) ;
                            MSrvr.workerOne = MSrvr.workerOne + n;
                        //}     
                    }
                    else if(MSrvr.workerTwo < MSrvr.workerOne)
                    {
                        //if(MSrvr.workerTwo < 10)
                        //{
                            Socket scWrkr = new Socket("localhost", 8082);

                            DataInputStream disWrkr = new DataInputStream(scWrkr.getInputStream());
                            DataOutputStream dosWrkr = new DataOutputStream(scWrkr.getOutputStream()); 
                            
                            dosWrkr.writeInt(input);
                            MSrvr.bufferCount --;
                            MSrvr.workerTwo ++ ;
                            int newInput, newOutput;
                            newInput = disWrkr.readInt();
                            newOutput = disWrkr.readInt();
                            System.out.println("From Worker Two : "+newInput +" = "+ newOutput);
                           // MSrvr.workerTwo -- ;
                            dos.writeInt(newInput);
                            dos.writeInt(newOutput);
                            
                            Random rand = new Random();
                            int  n = rand.nextInt(2) ;
                            MSrvr.workerTwo = MSrvr.workerTwo + n;
                        //}
                    }
                    else
                    {
                        i--;
                    }
                    
                    //dos.writeInt(input);
                    //output = input * input;
                    //dos.writeInt(output);
                    //MSrvr.bufferCount --;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(service.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
            
    }
    
}



////////////////////////////////
/////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

/*
class receiveFromClient implements Runnable {
    
    int clientNum;
    Socket sc;
    int input;
    
    public receiveFromClient(Socket s,int c) throws IOException{
        sc = s;
        clientNum = c;
        MSrvr.dis[clientNum] = new DataInputStream(sc.getInputStream());
        
    }
    
    public void run() {
        
        while (true)
        {
            try {
                input = MSrvr.dis[clientNum].readInt();
                System.out.println("Received Input :" + input);
                if(MSrvr.bufferCount<50)
                {
                    MSrvr.mainBuffer[clientNum][MSrvr.clientBufferCount[clientNum]] = input;
                    MSrvr.clientBufferCount[clientNum] ++;
                    MSrvr.bufferCount ++;
                }
                else {
                    // drop input
                    System.out.println("Drop Input :" + input);
                }


            } catch (IOException ex) {
                Logger.getLogger(receiveFromClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
    
}

class clientSendSquare implements Runnable {
    
    int clientNum;
    Socket sc;
    public clientSendSquare(Socket s,int c) throws IOException{
        sc = s;
        clientNum = c;
        MSrvr.dos[clientNum] = new DataOutputStream(sc.getOutputStream());
    }
    
    
    public void run() {
        while(true)
        {
            if(MSrvr.bufferCount == 0)
            {
                continue;
            }
            else
            {
                
                
                try {
                    int totalClient = MSrvr.clientCount;
                    int randomClient = totalClient % totalClient;
                    int index = MSrvr.clientBufferCount[randomClient];
                    if( index == 0)
                    {
                        continue;
                    }
                    int data = MSrvr.mainBuffer[randomClient][index];
                    System.out.println("h----" + data);
                    MSrvr.dos[randomClient].write(data);
                    data = data * data;
                    System.out.println("aaaaaa" + data);
                    MSrvr.dos[randomClient].write(data);
                    MSrvr.clientBufferCount[randomClient]--;
                    MSrvr.bufferCount -- ;
                    System.out.println("h");
                }
                /*
                int index = MSrvr.bufferCount - 1;
                if(index < 0 )
                {
                continue;
                }
                int whichClient = MSrvr.mainServerBufferRememberClientNum[index];
                int indexNum = MSrvr.eachClientBufferCount[whichClient] - 1;
                if(indexNum < 0 )
                {
                System.out.println("twoooo");
                continue;
                }
                int takeInput = MSrvr.mainBuffer[whichClient][indexNum];
                int getSquare = takeInput * takeInput;
                System.out.println("In :"+takeInput+"  Out : "+ getSquare);
                MSrvr.dos[whichClient].write(takeInput);
                MSrvr.dos[whichClient].write(getSquare);
                MSrvr.bufferCount --;
                 */
                /*
                whichClient = MSrvr.mainServerBufferRememberClientNum[MSrvr.bufferCount];
                System.out.println("aaaa");
                indexNum = MSrvr.eachClientBufferCount[whichClient];
                takeInput = MSrvr.mainBuffer[whichClient][indexNum];
                MSrvr.dos[clientNum].write(takeInput);
                getSquare = takeInput * takeInput;
                MSrvr.dos[clientNum].write(getSquare);

                 */
/*catch (IOException ex) {
                    Logger.getLogger(clientSendSquare.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
 
        }
    }
    
}
/*
.,,,,,,,,,,,,,,,,,,,,.,,,,,,,,,,,,,,
if(MSrvr.workerOne <= MSrvr.workerTwo)
            {
                if(MSrvr.workerOne < 10)
                {
                    // send to worker server one : clientNum and input
                    
                    MSrvr.bufferCount --;
                }
            }
            else
            {
                if(MSrvr.workerTwo < 10)
                {
                    // send to worker server one : clientNum and input
                    
                    MSrvr.bufferCount --;
                }
            }


*/