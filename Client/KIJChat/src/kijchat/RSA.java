/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kijchat;
import java.util.Random;
import java.math.*;
/**
 *
 * @author dimas
 */
public class RSA {
    int i, tempI;
    String Plain;
    String Cipher;
    String Key="";
    char tempC;
    public String RSA(){
        /*  Return nya adalah e:d:N dengan ketentuan e:N adalah Public Key sedangkan d:N adalah Private Key*/
        /*  1. Pilih 2 Bilangan prima p dan q */ 
        BigInteger P, Q, N, EuN, e, d, encrypt, temp1, dualimaenam, Chiper, Plain_Text;        
        P= new BigInteger(String.valueOf(getprime()));
        Q= new BigInteger(String.valueOf(getprime()));
        /*  2. Mendapatkan integer n dengan perkalian p*q */
        N=P.multiply(Q);
        /*  3. Hitung Euler Totien n */
        EuN =P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));
        /*  4. Cari Euler Totien (n)*/
        e = get_E(EuN);
        
        /*  5. Cari nilai d */
        d = get_D(e,EuN); 
        //  6. Enkripsi algoritm */
        Key=Key.concat(e.toString());
        Key=Key.concat(":");
        Key=Key.concat(d.toString());
        Key=Key.concat(":");
        Key=Key.concat(N.toString());
        return Key;
    }
    
    public String Get_e_RSA(String Key){
        String data[];
        data=Key.split(":");
        return data[0];
    }
    
    public String Get_d_RSA(String Key){
        String data[];
        data=Key.split(":");
        return data[1];        
    }
    
    public String Get_N_RSA(String Key){
        String data[];
        data=Key.split(":");
        return data[2];
    }
    
    public String Enkripsi_RSA(String Plain, String e, String N){
        BigInteger encrypt, satuduatujuh, e1, N1, Chiper, temp1;
        encrypt = new BigInteger("0");
        satuduatujuh = new BigInteger("128");
        e1= new BigInteger(e);
        N1= new BigInteger(N);
        for(i=0; i<Plain.length(); i++){
            tempC=Plain.charAt(0);
            tempI=(int)tempC;
            temp1= new BigInteger(String.valueOf(tempI));
            encrypt=encrypt.add(temp1.multiply(satuduatujuh.pow(i)));
        }
        Chiper = encrypt.modPow(e1, N1);
        return Chiper.toString();
    }
    
    public String Deskripsi_RSA(String Cipher, String d, String N){
        BigInteger Cipher1, d1, N1, satuduatujuh, Plain;
        String Plain_text;
        satuduatujuh = new BigInteger("128");
        Cipher1 = new BigInteger(Cipher);
        d1 = new BigInteger(d);
        N1 = new BigInteger(N);
        Plain = Cipher1.modPow(d1, N1);
        
        return 
    }
    
    public BigInteger getprime(){
        BigInteger num;
        num=new BigInteger("0");
        boolean check=true;
        while(true){
            num=GenerateNumber(100, BigInteger.ZERO);
            check=CheckPrime(num);
            if(check==true)break;
        }
        return num;
    }
    /*  Membuat Function untuk mengenerate number secara otomatis */
    public BigInteger GenerateNumber(int bit_length, BigInteger value){
        BigInteger randomvalue;
        randomvalue=BigInteger.ZERO;
        Random randomGenerator=new Random();
        if(value.compareTo(BigInteger.ZERO)==0)
        {
            randomvalue=new BigInteger(bit_length,randomGenerator);
        }
        else{
            randomvalue=new BigInteger(bit_length,randomGenerator);
            if(randomvalue.compareTo(value)==-1){
                randomvalue=randomvalue.mod(value);
            }
        }
        return randomvalue;
    }
    
    /* Membuat Euclidean Function untuk melakukan pengecheckan apakah int a dan b adalah relatively prime */
    public boolean EuclideanFunction(BigInteger a, BigInteger b){
        BigInteger R, A, B;
        A=a;
        B=b;
        R=BigInteger.ONE;
        while(true){
            if (B.compareTo(BigInteger.ZERO)==0) {
                break;
            }
            else{
                R= A.mod(B);
                A=B;
                B=R;
            }            
        }
        if(A.compareTo(BigInteger.ONE)==0) return true;
        else return false;
    }
    
    public boolean CheckPrime(BigInteger num){
        BigInteger DUA;
        DUA = new BigInteger("2");
        if(num.mod(DUA).compareTo(BigInteger.ZERO)==0)return false;
        else{
            boolean i;
            i=num.isProbablePrime(1);
            if(i==true)return i;
            else return false;
        }
    }
    
    public BigInteger get_D(BigInteger e, BigInteger EuN){
        BigInteger d;
        d=e.modInverse(EuN);
        return d;
    }
    
    public BigInteger get_E(BigInteger EuN){
        BigInteger num;
        num=new BigInteger("0");
        boolean gcd=false;
        num=GenerateNumber(200, EuN);
        while(!gcd){
            if(EuN.compareTo(num)==1)gcd=EuclideanFunction(EuN,num);
            else gcd=EuclideanFunction(num,EuN);
            if(gcd==true)break;
            else num=num.add(BigInteger.ONE);
        }
        return num;
    }


}
