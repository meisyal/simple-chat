/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kijchat;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author dimas
 */
public class KIJChat2 {
    Socket sock;
    String IP;
    String username;
    int Port;
    BufferedReader reader;
    PrintWriter writer;
    ArrayList<String> userList = new ArrayList();
    Beranda myBeranda;  
    public void create_socket(String IP, int Port, String username){
        this.Port=Port;
        this.IP=IP;
        this.username=username;
            try{
                sock = new Socket(IP, Port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println("CONN:"+username+":");
                writer.flush();
                myBeranda = new Beranda(sock,reader,writer,username);
                myBeranda.setVisible(true);
                ListenThread();
            }catch(Exception ex){
                //new FormLogin().setVisible(true);
            }
    } 
    public class IncomingReader implements Runnable{
        
        public void run(){
            String data[];
            String stream, connect = "CONN", list = "LIST", disconnect="DCON", chat="SEND";
            try{
                while((stream = reader.readLine())!=null){
                    
                    myBeranda.ShowActivity(stream);
                    data = stream.split(":");
                    if(data[0].equals(connect)){
                        myBeranda.RemoveAllActivity();
                        userAdd(data[1]);
                    }else if(data[0].equals(list)){
                        myBeranda.ClearListUser();
                        writeUsers();
                        userList.clear();
                    }else if(data[0].equals(disconnect)){
                        userRemove(data[1]);
                    }else if(data[0].equals(chat)){
                        myBeranda.SendTo(data[1], data[3]);
                    }
                }
            }catch(Exception ex){
                
            }
        }
        
    }
    
    public void ListenThread(){
        Thread IncomingReader = new Thread(new KIJChat2.IncomingReader());
        IncomingReader.start();
    }
    
    public void userAdd(String data){
        userList.add(data);
    }
    
    public void writeUsers(){
        String[] tempList =  new String[(userList.size())];
        userList.toArray(tempList);
        for(String token:tempList){
            myBeranda.AddListUser(token);
        }
    }
    
    public void userRemove(String data){
        myBeranda.ShowActivity(data+" telah disconnected. \n");
    }
}

