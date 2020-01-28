
package ysa_hasan;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.AutoencoderNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class YSA {
    
    private static final File veriseti = new File("./assets/Data.txt");
    MomentumBackpropagation bp;
    int maksEpoch;
    int araKatman;
    double ogrenmeKatsayi;
    
    double []eldeEdilenHatalar;
    
    private DataSet egitimVeriSeti;
    private DataSet testVeriSeti;
    
    private double[] maksimumlar;
    private double[] minimumlar;
    
    public YSA(int araKatman,int maksEpoch,double momentum,double ogrenmeKatsayi) throws FileNotFoundException{
        this.araKatman=araKatman;
        this.maksEpoch=maksEpoch;
        this.ogrenmeKatsayi=ogrenmeKatsayi;
    
        eldeEdilenHatalar=new double [(int)maksEpoch];
        
        maksimumlar = new double[15];
        minimumlar =  new double[15];
        for (int i = 0; i < 15; i++) {
            maksimumlar[i] = Double.MIN_VALUE;
            minimumlar[i] = Double.MAX_VALUE;
        }
        EgitimVeriSetiMaks();      
        TestVeriSetiMaks();
       
        
        egitimVeriSeti = EgitimVeriSeti();      
        testVeriSeti = TestVeriSeti();
        
        bp = new MomentumBackpropagation();
        bp.setMomentum(momentum);
        bp.setLearningRate(ogrenmeKatsayi);
        bp.setMaxIterations(maksEpoch); 

    }
    
    public void egit(){
        MultiLayerPerceptron sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,15,araKatman,2);
        sinirselAg.setLearningRule(bp);
        /*for (int i = 0; i < maksEpoch; i++) {
            sinirselAg.getLearningRule().doOneLearningIteration(egitimVeriSeti);
            
            if(i==0){            
            eldeEdilenHatalar[i]=1;
            }
            
            else
                eldeEdilenHatalar[i]=sinirselAg.getLearningRule().getPreviousEpochError();                              
          }*/
          //Momentum back propagation kullaniyoruz bu kisimdan sonra.Bundan dolayı ustteki yorum satiri icerisindeki kod blogunu kullanmadik.
       sinirselAg.learn(egitimVeriSeti);
        sinirselAg.save("ogrenenAg.nnet");
        System.out.println("Eğitim Tamamlandı.");
        
    }
    
    private void EgitimVeriSetiMaks() throws FileNotFoundException {
        double x = 27.91551882;
        Scanner oku = new Scanner(veriseti);
        while(oku.hasNextDouble()){
            for (int i = 0; i < 15; i++) {
                double d = oku.nextDouble();
                if(d > maksimumlar[i])  maksimumlar[i] = d;
                if(d < minimumlar[i])  minimumlar[i] = d;
            }
            oku.nextDouble();oku.nextDouble();
        }
        oku.close();
    }
    
    private void TestVeriSetiMaks() throws FileNotFoundException {
     Scanner oku = new Scanner(veriseti);
        while(oku.hasNextDouble()){
            for (int i = 0; i < 15; i++) {
                double d = oku.nextDouble();
                if(d > maksimumlar[i])  maksimumlar[i] = d;
                if(d < minimumlar[i])  minimumlar[i] = d;
            }
            oku.nextDouble(); oku.nextDouble();
        }
        oku.close();
    
    }
    
    
    public DataSet EgitimVeriSeti() throws FileNotFoundException{

        Scanner oku = new Scanner(veriseti);
        DataSet egitim = new DataSet(15, 2);
        int sayacOku=0;
        while(oku.hasNextDouble()){
            
           // I çıktısı datada az olduğundan dolayı ilk 61 satırı okuttum.

                    if(sayacOku==61)
                        break;
                    double [] inputs = new double [15];
                    for (int j = 0; j < 15; j++) {
                        double d = oku.nextDouble();
                        
                        if(j!=4)
                            inputs[j] = min_max(maksimumlar[j], minimumlar[j], d);
                        else
                            inputs[j]=d;
                    }
                    
                    
                    DataSetRow satir = new DataSetRow(inputs,new double []{oku.nextDouble(),oku.nextDouble()});
                    egitim.add(satir);  
                    

                    sayacOku++;
        }
    
        oku.close();
        return egitim;
    }
    public DataSet TestVeriSeti() throws FileNotFoundException { 
  
           Scanner oku = new Scanner(veriseti);
           DataSet test = new DataSet(15, 2);
           int sayacOku2=0;
           while(oku.hasNextDouble()){
                   //Egitimden kalan yuzde 30 veri olan 60. satirdan sonrasi test icin kullaniliyor. 
                if(sayacOku2>=61){
                    double [] inputs = new double [15];
                    for (int j = 0; j < 15; j++) {
                        double d = oku.nextDouble();
                       //5. degerin hepsi 1 oldugundan sigmoud fonskiyonun bolum kisimi 0 oldugundan sonsuza gidiyor.Deger zaten 0-1 arasi oldugundan bu kisim sigmoud foksiyonu
                       //ile islem yapmadan esitleniyor
                        if(j!=4)
                            inputs[j] = min_max(maksimumlar[j], minimumlar[j], d);
                        else
                            inputs[j]=d;
                    }
                    DataSetRow satir = new DataSetRow(inputs,new double []{oku.nextDouble(),oku.nextDouble()});
                    test.add(satir);}
                    
                sayacOku2++;
           }
           oku.close();
           return test;
    
    }
    
    private double min_max (double max, double min, double x){
        return  (x-min)/(max-min);//Sigmoid Foksiyonu
    }
    public double [] hatalar(){                
        return eldeEdilenHatalar;  
    } 
    
    
    public double hata (){return bp.getTotalNetworkError();}
    public String sonuc(double[] outputs){
        
        
        if(outputs[0] == outputs[1])
        {
            return "A";
        }
        else if(outputs[0]<outputs[1])
            return "S";
        else if (outputs[0]>outputs[1])
            return "I";
        return "";
    }
    
    public String tekTest(double [] inputs){
        for (int i = 0; i < 15; i++) {
             //5. degerin hepsi 1 oldugundan sigmoud fonskiyonun bolum kisimi 0 oldugundan sonsuza gidiyor.Deger zaten 0-1 arasi oldugundan bu kisim sigmoud foksiyonu
                       //ile islem yapmadan esitleniyor
            if(i!=4)
                inputs[i] = min_max(maksimumlar[i], minimumlar[i], inputs[i]);
            
                
        }
        NeuralNetwork sinirselAg = new NeuralNetwork().createFromFile("ogrenenAg.nnet");
        sinirselAg.setInput(inputs);
        sinirselAg.calculate();
        return sonuc(sinirselAg.getOutput());
    }
    
    double mse(double[] beklenen , double[] cikti){
        double satirHata = 0;
        
        satirHata += Math.pow((beklenen[0] - cikti[0]) , 2);
        
        return satirHata;
    }
    
    public double test(){
        NeuralNetwork sinirselAg = new NeuralNetwork().createFromFile("ogrenenAg.nnet");
        double toplamHata = 0;
        for (DataSetRow r : testVeriSeti) {
            sinirselAg.setInput(r.getInput());
            sinirselAg.calculate();
            toplamHata += mse(r.getDesiredOutput(), sinirselAg.getOutput());
        }
        return toplamHata / testVeriSeti.size();
    }
    //Rapordada belirttigim gibi random olarak satir satir okutmak icin egit fonksiyonunda kullanmam gereken foksiyonlar.
    /*public int [] randomEgit(){
        
        
        int [] randomEgitSayi=new int [81];
        Random rnd= new Random();
        int sayi1;
        randomEgitSayi[0]=0;
        for (int i = 0; i < 81; i++) {
            sayi1= rnd.nextInt(116);
            for (int j = 0; j <81; j++) {                
                if(randomEgitSayi[j]==sayi1){
                    sayi1= rnd.nextInt(116);
                    j=0;
                }               
            }
            randomEgitSayi[i]=sayi1;           
        }
    
        Arrays.sort(randomEgitSayi);
    
        return randomEgitSayi;
    
    }
    public int [] randomTest(){
        
        ArrayList<String> dizi = new ArrayList<String>();
        for (int i = 0; i < 116; i++) {
            dizi.add(String.valueOf(i));
        }
        
        int [] sayi3= randomEgit();
        for (int i = 0; i < 81; i++) {
            
             dizi.remove(String.valueOf(sayi3[i]));
        }
        
        int [] randomTestSayi=new int [35];   
        
        for (int i = 0; i < 35; i++) {                       
            randomTestSayi[i]=Integer.valueOf(dizi.get(i));           
        }
           
        Arrays.sort(randomTestSayi);
    
        return randomTestSayi;
    }
    */
    
    
    
}
