/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kijchat;

/**
 *
 * @author dimas
 */
public class RC4 {
    String Cipher, Plain, Key;
    int i, j, tempI, n, t;
    char tempC;
    
    public String Enkripsi(String Plain, String Key){
        this.Plain=Plain;
        this.Key=Key;
        Cipher=RC4(Plain, Key);
        return Cipher;
    }
    
    public String Deskripsi(String Cipher, String Key){
        this.Cipher=Cipher;
        this.Key=Key;
        String Plain_text, tempS, Cipher_text;
        int New_Cipher[]=new int[256];
        int counter=0, temp;       
        for(i=0; i<Cipher.length(); i=i+2){
            tempS="";
            tempS=tempS.concat(Character.toString(Cipher.charAt(i)));
            tempS=tempS.concat(Character.toString(Cipher.charAt(i+1)));
            New_Cipher[counter]=convert_octal_to_decimal(tempS);
            counter++;
        }
        Cipher_text="";
        for(i=0; i<counter; i++){
            Cipher_text=Cipher_text.concat(Character.toString((char)New_Cipher[i]));
        }
        Plain=RC4(Cipher_text,Key);
        counter=0;
        for(i=0; i<Plain.length(); i=i+2){
            tempS="";
            tempS=tempS.concat(Character.toString(Plain.charAt(i)));
            tempS=tempS.concat(Character.toString(Plain.charAt(i+1)));
            New_Cipher[counter]=convert_octal_to_decimal(tempS);
            counter++;
        }
        Plain_text="";
        for(i=0; i<counter; i++){
            Plain_text=Plain_text.concat(Character.toString((char)New_Cipher[i]));
        }
        return Plain_text;
    }
    
    private String RC4(String Plain, String Key){
        /*  Menyiapkan State Vector dan Temporary Vector
            Tahap Inisialisasi
        */
        //------------------------
        String RC4Text="";
        int []SVector = new int[257];
        int []TVector = new int[257];
        int []Cipher_temp = new int[257];
        int []k = new int[257];
        
        //------------------------
        j=0;
        for(i=0; i<256; i++){
            SVector[i]=i;
            if(j==Key.length()){
                j=0;
            }
            tempC=(Key.charAt(j));
            TVector[i]=(int)tempC;
            j++;
        }
        /*  Permutasi awal untuk S */
        for(i=0;i<256;i++){
            j=(j+SVector[i]+TVector[i])%256;
            /*  Melakukan Pertukaran Nilai*/
                tempI=SVector[i];
                SVector[i]=SVector[j];
                SVector[j]=tempI;
        }
        /*  Key Stream Generation */
        i=j=0;
        for(n=0; n<Plain.length(); n++){
            i=(i+1)%256;
            j=(j+SVector[i])%256;
            /*  Melakukan Pertukaran Nilai*/
                tempI=SVector[i];
                SVector[i]=SVector[j];
                SVector[j]=tempI;
            t=(SVector[i]+SVector[j])%256;
            k[n]=SVector[t];                
        }
        
        /* Enkripsi Dengan XOR data*/
        
        for(n=0; n<Plain.length();n++){
            tempC=Plain.charAt(n);
            tempI=(int)tempC;
            Cipher_temp[n]=k[n]^tempI;
        }
        
        String temp;
        for(i=0; i<Plain.length();i++){
            temp=convert_to_octal(Cipher_temp[i]);
            RC4Text=RC4Text.concat(temp);
        }
        return RC4Text;
    }
    
    private String convert_to_octal(int num){
        int temp1, temp2;
        String tempC1, tempC2, nilai="";
        temp1=num/16;
        temp2=num%16;
        if(temp1>9){
            tempC1=Character.toString((char)(65+temp1-10));
        }
        else{
            tempC1=Integer.toString(temp1);
        }
        if(temp2>9){
            tempC2=Character.toString((char)(65+temp2-10));
        }
        else{
            tempC2=Integer.toString(temp2);
        }
        nilai=tempC1.concat(tempC2);
        return nilai;
    }
    
    private int convert_octal_to_decimal(String num){
        int tempI1, tempI2, nilai;
        char tempC1, tempC2;
        tempC1=num.charAt(0);
        tempC2=num.charAt(1);
        if(tempC1>='A'){
            tempI1=((int)tempC1)-55;
        }
        else{
            tempI1=((int)tempC1)-48;
        }
        if(tempC2>='A'){
            tempI2=((int)tempC2)-55;            
        }
        else{
            tempI2=((int)tempC2)-48;
        }
        nilai=(tempI1*16)+(tempI2);
        return nilai;
    }
}
