
package ysa_hasan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class YSA_HASAN {

    
    public static void main(String[] args) throws FileNotFoundException {
        
        Scanner in = new Scanner(System.in);
        int araKatmanNoron;
        double ok;
        int epoch,sec;
        double momentum =0.8;
        araKatmanNoron=5;
        ok=0.5;
        epoch=500;
        
        int secim;
       
        YSA ysa = ysa= new YSA(araKatmanNoron,epoch,momentum,ok);
        
        do{
        
            System.out.println("1. Ağı Eğit");
            System.out.println("2. Ağı Test Et ");
            System.out.println("3. Tek Veri ile Test Et ");
            System.out.println("4. Çıkış ");
            System.out.println("=>");
            
            sec = in.nextInt();
            if (sec==1) {
                ysa.egit();
                
                
                
                System.out.println("Ulaşılan Son Hata : "+ysa.hata());
            }
            else if(sec == 2){
                System.out.println("test hata ortalaması"+ysa.test());
            }
            else if(sec == 3){
                double [] inputs = new double [15];
                
                System.out.println("Hastanın ic sicakligi(C) : ");
	                        double girdi = in.nextDouble();
	                        if(true) {
	                        	if(girdi > 37) {inputs[0]=1; inputs[1]=0;}
	                        	else if(girdi<=37 && girdi >=36) {inputs[0]=0; inputs[1]=1; }
	                        	else {inputs[0]=0; inputs[1]=0; }
	                        }
	                        System.out.println("Hastanın yuzey sicakligi(C) : ");
	                        girdi = in.nextDouble();
	                        if(true) {
	                        	if(girdi > 36.5) {inputs[2]=1; inputs[3]=0;}
	                        	else if(girdi<=36.5 && girdi >=35) {inputs[2]=0; inputs[3]=1; }
	                        	else {inputs[2]=0; inputs[3]=0; }
	                        }
	                        System.out.println("oksijen satürasyon(%) : ");
	                        girdi = in.nextDouble();
	                        if(true) {
	                        	if(girdi >= 98) {inputs[4]=1; inputs[5]=1;}
	                        	else if(girdi<=98 && girdi >=90) {inputs[4]=1; inputs[5]=0; }
	                        	else if(girdi<=90 && girdi >=80) {inputs[4]=0; inputs[5]=1; }
	                        	else {inputs[4]=0; inputs[5]=0; }
	                        }
	                        System.out.println("Kan basincinin son olcumu : ");
	                        girdi = in.nextDouble();
	                        if(true) {
	                        	if(girdi > 130/90) {inputs[6]=1; inputs[7]=0;}
	                        	else if(girdi<=130/90 && girdi >=90/70) {inputs[6]=0; inputs[7]=1; }
	                        	else {inputs[6]=0; inputs[7]=0; }
	                        }
	                        System.out.println("Hastanin yuzey sicakliginin dengesi(degerler:unstable, modstable, stable ) : ");
	                        String sgirdi = in.next();
	                        
	                        switch(sgirdi){
                            case "unstable":
                                inputs[8] = 0;
                                inputs[9] = 0;
                                break;
                                case "modstable":
                                inputs[8] = 1;
                                inputs[9] = 0;
                                break;
                                case "stable":
                                inputs[8] = 0;
                                inputs[9] = 1;
                                break;
                                 default : 
                                    inputs[12] =0;
                                    inputs[13] =0;
                                    break;
	                        }
	                        
	                        System.out.println("Hastanin ic sicakliginin dengesi(degerler:unstable, modstable, stable ) : ");
	                        sgirdi = in.next();
	                        
	                        switch(sgirdi){
                            case "unstable":
                                inputs[10] = 0;
                                inputs[11] = 0;
                                break;
                                case "modstable":
                                inputs[10] = 1;
                                inputs[11] = 0;
                                break;
                                case "stable":
                                inputs[10] = 0;
                                inputs[11] = 1;
                                break;
                                 default : 
                                    inputs[12] =0;
                                    inputs[13] =0;
                                    break;
	                        }
	                        
	                        System.out.println("Hastanin kan basincinin dengesi(degerler:unstable, modstable, stable ) : ");
	                        sgirdi = in.next();
	                        
	                        switch(sgirdi){
                            case "unstable":
                                inputs[12] = 0;
                                inputs[13] = 0;
                                break;
                                case "modstable":
                                inputs[12] = 1;
                                inputs[13] = 0;
                                break;
                                case "stable":
                                inputs[12] = 0;
                                inputs[13] = 1;
                                break;
                                default : 
                                    inputs[12] =0;
                                    inputs[13] =0;
                                    break;
                                    
	                        }
	                        
	                        
	                        System.out.println("Hastanin taburcu olurken konforu(0-20) :");
	                        inputs[14]=in.nextDouble();
                
                
                String a = ysa.tekTest(inputs);
                System.out.println(a);
                
            }

        
        }while(sec!=4);
        

        
    }
    
}
