
import java.util.*;
import statistics.Distribution;
import statistics.BernoulliDistribution;
import statistics.NormalDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;

public class Cars {
    protected double arrivalTime;
    protected double[] trash;
    protected int[] trashtypes;
    protected int location = 0;

    //for the location tester.
    public void modLocation(int L) {
        this.location = L;
    }

    public int getLocation() {
        return location;
    }

    //test case
    public int locator(int[] x, int pL) {
        //Do stuff to calc next location
        int L = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                if (i > pL) {
                    L = i;
                    i = x.length;
                }
            }
        }

        // this is when there is no destination, leave the system
        if (L == 0) {
            L = -1;
        }
        modLocation(L);
        return L;

    }

   

    public void initTrash() {
        trash = new double[20];
        trashtypes = new int[5];
        

        //Initialize arrays
        for (int i = 0; i < trash.length; i++) {
            trash[i] = 0.0;
        }
        for (int i = 0; i < trashtypes.length; i++) {
            trashtypes[i] = 0;
        }

      

        /*
        System.out.println(Arrays.toString(trash));
        
        System.out.println(indexer);
        */
        //System.out.println(Arrays.toString(trashtypes));
    }

    public int[] getWorkStations(double[] trash){
        //All the car must go through the entrance
        trashtypes[0] = 1;
        double sF =0;
        double sD = 0;
        double sG=0;
        double sR=0;

        for(int i =0 ; i<4;i++){
            sF += trash[i];
        }
        // System.out.println(sF);
        if(sF>0){
            trashtypes[1]=1;
        }
        for(int i =4 ; i<8;i++){
            sD += trash[i];
            
        }
        if(sD>0){
            trashtypes[2]=1;
        }
        for(int i =8 ; i<13;i++){
            sG += trash[i];
        }
        if(sG>0){
            trashtypes[3]=1;
        }
        for(int i =13 ; i<20;i++){
            sR += trash[i];
        }
        if(sR>0){
            trashtypes[4]=1;
        }

        
        return trashtypes;
    }


    public double[] getTrashTypes(int vt) {
        initTrash();
        Random rng = new Random();
        //Trash type 
        if(vt > 0){
            for (int i = 0; i < trash.length; i++) {
                Distribution ttype; //= new BernoulliDistribution(0.15, rng);
                switch(i){
                    case 0: ttype = new BernoulliDistribution(0.6069, rng); trash[i] = ttype.nextRandom(); break;
                    case 1: ttype = new BernoulliDistribution(0.3768, rng); trash[i] = ttype.nextRandom(); break;
                    case 2: ttype = new BernoulliDistribution(0.2482, rng); trash[i] = ttype.nextRandom(); break;
                    case 3: ttype = new BernoulliDistribution(0.2383, rng); trash[i] = ttype.nextRandom(); break;
                    case 4: ttype = new BernoulliDistribution(0.1229, rng); trash[i] = ttype.nextRandom(); break;
                    case 5: ttype = new BernoulliDistribution(0.1032, rng); trash[i] = ttype.nextRandom(); break;
                    case 6: ttype = new BernoulliDistribution(0.0369, rng); trash[i] = ttype.nextRandom(); break;
                    case 7: ttype = new BernoulliDistribution(0.1818, rng); trash[i] = ttype.nextRandom(); break;
                    case 8: ttype = new BernoulliDistribution(0.1892, rng); trash[i] = ttype.nextRandom(); break;
                    case 9: ttype = new BernoulliDistribution(0.0516, rng); trash[i] = ttype.nextRandom(); break;
                    case 10:ttype = new BernoulliDistribution(0.0098, rng); trash[i] = ttype.nextRandom(); break; 
                    case 11:ttype = new BernoulliDistribution(0.1572, rng); trash[i] = ttype.nextRandom(); break;
                    case 12:ttype = new BernoulliDistribution(0.0098, rng); trash[i] = ttype.nextRandom(); break;
                    case 13:ttype = new BernoulliDistribution(0.0025, rng); trash[i] = ttype.nextRandom(); break;
                    case 14:ttype = new BernoulliDistribution(0.0344, rng); trash[i] = ttype.nextRandom(); break;
                    case 15:ttype = new BernoulliDistribution(0.0762, rng); trash[i] = ttype.nextRandom(); break;
                    case 16:ttype = new BernoulliDistribution(0.0172, rng); trash[i] = ttype.nextRandom(); break;
                    case 17:ttype = new BernoulliDistribution(0.0541, rng); trash[i] = ttype.nextRandom(); break;
                    case 18:ttype = new BernoulliDistribution(0.0246, rng); trash[i] = ttype.nextRandom(); break;
                    case 19:ttype = new BernoulliDistribution(0.0319, rng); trash[i] = ttype.nextRandom(); break;
                    default: System.out.println("something is wrong"); break;   
                } 
            }
        }
        else {
            for (int i = 0; i < trash.length; i++) {
                Distribution ttype; //= new BernoulliDistribution(0.15, rng);
                switch(i){
                case 0: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 1: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 2: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 3: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 4: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 5: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 6: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 7: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 8: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 9: ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 10:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break; 
                case 11:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 12:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 13:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 14:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 15:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 16:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 17:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 18:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                case 19:ttype = new BernoulliDistribution(0, rng); trash[i] = ttype.nextRandom(); break;
                default: System.out.println("something is wrong"); break;
                }
            }
        } 

        return trash;
    }

    public int getCarType() {
        Random rng = new Random();
        int carType;
        Distribution bikeprob = new BernoulliDistribution(0.11, rng);

        if(bikeprob.nextRandom() == 1){
            carType=0;
           
            //check large ot cmall 
            
           
        } else {
          Distribution  isSmall = new BernoulliDistribution(1, rng);
            
            if(isSmall.nextRandom() == 1){
                carType = 1;
            }else {
                carType = 2;
            }
        }

        return carType;
    }
    

    public double getArrivalTime() {
        return arrivalTime;
    }

    public int[] getWorkStations() {
        
        
        return trashtypes;

    }

    // For testing 
    public static void main(String[] args) {
        Cars carr = new Cars();
        
       

        /*for (int i = 0; i < 5; i++) {
            System.out.println(carr.locator(test, carr.getLocation()));
        }*/
        LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
        double logN = lognorm.sample();
        System.out.println("logN: "+logN); 

        for(int i=0; i<100; i++){
            System.out.println(carr.getCarType());
        }

        

        System.out.println(Arrays.toString(carr.getTrashTypes(carr.getCarType())) );
       
    }

}
