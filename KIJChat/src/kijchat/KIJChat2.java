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
    RSA myRSA=new RSA();
    RC4 myRC4= new RC4();
    Socket sock;
    int Port;
    BufferedReader reader;
    PrintWriter writer;
    ArrayList<String> userList = new ArrayList();
    ArrayList<String> PublicKeyList=new ArrayList();
    String myPrivateKey, myPublicKey="", RSAKey, IP,username;
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
                RSAKey=myRSA.RSA();
                myPrivateKey=myRSA.PrivateKey(RSAKey);
                myPublicKey=myRSA.PublicKey(RSAKey);
                writer.println("CONN:"+username+":"+myPublicKey+":");
                writer.flush();
                myBeranda = new Beranda(sock,reader,writer,username);
                myBeranda.setVisible(true);
                myBeranda.ShowActivity("You Already Connected \n");
                ListenThread();
            }catch(Exception ex){
                new FormLogin().setVisible(true);
            }
    } 
    public class IncomingReader implements Runnable{
        
        public void run(){
            String data[];
            String stream, connect = "LIST", list = "DONE", disconnect="DCON", chat="SEND";
            try{
                
                while((stream = reader.readLine())!=null){                    
                    //myBeranda.ShowActivity(stream+"\n");
                    data = stream.split(":");
                    if(data[0].equals(connect)){
                        myBeranda.RemoveAllActivity();
                        userAdd(data[1]);
                        PublicKeyAdd(data[2],data[3]);
                    }else if(data[0].equals(list)){
                        myBeranda.ClearListUser();
                        writeUsers();
                        userList.clear();
                    }else if(data[0].equals(disconnect)){
                        userRemove(data[1]);
                    }else if(data[0].equals(chat)){
                        String Key=myRSA.Deskripsi_RSA(data[4], myRSA.Get_ed_RSA(myPrivateKey), myRSA.Get_N_RSA(myPrivateKey));
                        //myBeranda.ShowActivity("Got Message From "+data[1]+"\n");
                        String Plain=myRC4.Deskripsi(data[3], Key);
                        String Hash=myRC4.getMD5(Plain);
                        if(Hash.compareTo(data[5])==0){
                            myBeranda.ShowActivity("Got Message From "+data[1]+" \nMessage : "+data[3]+"\nPlain :"+Plain+"\n");
                            myBeranda.SendTo(data[1], Plain);
                        }
                        else{
                            myBeranda.ShowActivity("Pesan Tidak Valid\n");
                        }
                    }
                }
            }catch(Exception ex){
                myBeranda.ShowActivity("Gagal Menerima Pesan \n");
            }
        }
        
    }
    
    public void ListenThread(){
        Thread IncomingReader = new Thread(new KIJChat2.IncomingReader());
        IncomingReader.start();
    }
    
    public void userAdd(String data){
        userList.add(data);
        myBeranda.adduser(data);
    }
    
    public void PublicKeyAdd(String e, String n){
        PublicKeyList.add(e+":"+n);
        myBeranda.addpublickey(e+":"+n);
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

