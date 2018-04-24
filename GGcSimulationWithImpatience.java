
// package Week4ANoNetwork;

import java.util.*;

import org.apache.commons.math3.distribution.LogNormalDistribution;

import statistics.Distribution;
import statistics.ExponentialDistribution;
import statistics.GammaDistribution;
import statistics.*;
import org.apache.commons.math3.distribution.LogNormalDistribution;

public class GGcSimulationWithImpatience {

    protected Distribution interarrivalTimeDist, patienceDist;
    protected Distribution[] serviceTimeDist;
    protected int[] nrParking;

    public GGcSimulationWithImpatience(Distribution arrivalDist, Distribution[] serviceDist, Distribution patienceDist,
            int[] nrOfServers) {
        this.interarrivalTimeDist = arrivalDist;
        this.serviceTimeDist = serviceDist;
        this.patienceDist = patienceDist;
        this.nrParking = nrOfServers;
    }

    public int locator(int[] x, int cL, Customer c) {
        //Do stuff to calc next location
        int L = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                if (i > cL) {
                    L = i;
                    i = x.length;
                }
            }
        }
        // this is when there is no destination, leave the system
        if (L == 0) {
            //set location as it's activated
            L = -1;
        }

        return L;
    }

    //thinning of the poisson 
    public double getP(double t) {
        double p = 0.0;
        if (0 <= t && t < 30) {
            p = 0.89;
        } else if (30 <= t && t < 60) {
            p = 1.0;
        } else if (60 <= t && t < 90) {
            p = 0.8;
        } else if (90 <= t && t < 120) {
            p = 0.7;
        } else if (120 <= t && t < 150) {
            p = 0.7;
        } else if (150 <= t && t < 180) {
            p = 0.5;
        } else if (180 <= t && t < 210) {
            p = 0.3;
        } else {
            p = 0.2;
        }

        return p;
    }

    //Run the simulation with nrOfCustomers customers
    public SimResultsArr simulate(double maxTime) {
        //number of car arriving
        int cararr = 0;
        int cararr2 = 0;
        //number of car left 
        int carlef = 0;

        int nrStations = nrParking.length;
        double t = 0;
        Queue[] qs = new Queue[nrStations];
        for (int i = 0; i < nrStations; i++) {
            qs[i] = new Queue(nrParking[i]);
        }
        //this keep number of cars at the station disregarding the size for actual queue
        Queue[] qsC = new Queue[nrStations];
        for (int i = 0; i < nrStations; i++) {
            qsC[i] = new Queue(nrParking[i]);
        }

        //implementation of the virtual queue
        Queue[] vq = new Queue[nrStations - 1];
        for (int i = 0; i < nrStations - 1; i++) {
            vq[i] = new Queue();
        }
        //implementation of the virtual queue disregarding the size
        Queue[] vqC = new Queue[nrStations - 1];
        for (int i = 0; i < nrStations - 1; i++) {
            vqC[i] = new Queue();
        }
        
        int[] blockingCounter = new int[4];

        SimResultsArr results = new SimResultsArr(nrStations);
        FES eventq = new FES();
        Cars carProperty = new Cars();

        // generate first arrival
        int ct = carProperty.getCarType();
        double arr = interarrivalTimeDist.nextRandom();
        double[] trashType = carProperty.getTrashTypes(ct);
        int[] route = carProperty.getWorkStations(trashType);
        
        Customer firstCustomer = new Customer(arr, serviceTimeDist[0].nextRandom(), route, ct);
        // schedule the arrival event
        eventq.addEvent(new Event(Event.ARRIVAL, firstCustomer.getArrivalTime(), firstCustomer));
        // schedule the abandonment event
        //eventq.addEvent(new Event(Event.ABANDONMENT, firstCustomer.getEndPatienceTime(), firstCustomer));

        //Continue the simulation as long as the number of sojourn times n
        //is less than the set maximum.
        while (t < maxTime) {

            Event e = eventq.nextEvent();
            t = e.getTime();
            Customer c = e.getCustomer();
            int stationNr = c.getLocation();
            int nextL = locator(c.getRoute(), c.getLocation(), c);
            c.setnLocation(nextL);
            //current station
            Queue q = null;
            q = qs[stationNr];


            
            

            //    System.out.println("Route: "+Arrays.toString(c.getRoute()));            
            // System.out.println("event: " + e.getType());
            // System.out.println("t: " + t);

            // Important: first check whether the customer is actually still in the system!
            // Otherwise, simply do nothing. Handle the next event.

            // Handle the event
            if (e.getType() == Event.ARRIVAL) {

                //Poisson thinning
                double P = getP(t);
                Random rng = new Random();
                Distribution arrdist = new BernoulliDistribution(P, rng);
                double allowarrival = arrdist.nextRandom();
                // System.out.println("t: " + t);
                // System.out.println("P: " + P);
                // System.out.println("allowarrival: " + allowarrival);
                if (allowarrival == 1.0) {
                   
                    //check for all the stations when car is at the entrance
                    if (stationNr == 0) {
                        
                        if(c.getCT() == 2){
                            q.addCustomer(c);
                            q.addCustomer(c);
                        } else if(c.getCT() == 1){
                            q.addCustomer(c);
                        }

                        
                        qsC[stationNr].addCustomer(c);

                        int sumR = 0;
                        for (int i = 1; i < c.getRoute().length; i++) {
                            sumR += c.getRoute()[i];
                        }
                        int tempL = 0;
                        int stationCounter = 0;

                        if(c.getCT() == 1){
                            while (tempL != -1) {
                                tempL = locator(c.getRoute(), tempL, c);
                                if (tempL != -1) {
                                    if (qs[tempL].getSize() < nrParking[tempL]) {
                                        stationCounter++;
                                    }
    
                                }
                            }
                        } else if(c.getCT() == 2){
                            while (tempL != -1) {
                                tempL = locator(c.getRoute(), tempL, c);
                                if (tempL != -1) {
                                    if ((qs[c.getnLocation()].getNumberOfServers() - qs[c.getnLocation()].getSize() )>= 2) {
                                        stationCounter++;
                                    }
    
                                }
                            }
                        }
                        

                        if (stationCounter == sumR) {
                            
                            cararr++;
                            //finally allow to depart the entrance
                            eventq.addEvent(new Event(Event.DEPARTURE, t + c.getServiceTime(), c));
                            results.registerWaitingTime(stationNr, 0);
                        } else if(c.getnLocation() == -1){
                            //when next is -1, gotta leave the system but think this line is not necessary
                                eventq.addEvent(new Event(Event.DEPARTURE, t + c.getServiceTime(), c));
                                results.registerWaitingTime(stationNr, 0);
                        }
                        
                        
                    }
                }

                // System.out.println("stationNr: "+stationNr+" size: "+qs[stationNr].getSize()+" :parking space "+qs[stationNr].getNumberOfServers());
                // if a station is available, take this car into service immediately.
                // only if the car is in the system already
                if (stationNr != 0) {
                    if (qs[stationNr].getSize() < qs[stationNr].getNumberOfServers()) {

                        if(c.getCT() == 2){
                            q.addCustomer(c);
                            q.addCustomer(c);
                        } else if(c.getCT() == 1){
                            q.addCustomer(c);
                        }

                        
                        qsC[stationNr].addCustomer(c);

                        eventq.addEvent(new Event(Event.DEPARTURE, t + c.getServiceTime(), c));
                        results.registerWaitingTime(stationNr, 0);

                    } else {
                        // blocking inside the system 
                        if(c.getCT() == 2){
                            vq[stationNr - 1].addCustomer(c);
                            vq[stationNr - 1].addCustomer(c);
                            c.setVqTime(t);
                            c.setVqTime(t);
                            
                        } else if(c.getCT() == 1){
                            vq[stationNr - 1].addCustomer(c);
                            c.setVqTime(t);
                        }

                        blockingCounter[stationNr-1]++;
                        vqC[stationNr - 1].addCustomer(c);
                        results.registerBlocking(stationNr - 1);
                        
                    }
                } else {

                    int ct2 = carProperty.getCarType();
                    double[] trashType2 = carProperty.getTrashTypes(ct2);
                    int[] route2 = carProperty.getWorkStations(trashType2);
                    double arr2 = interarrivalTimeDist.nextRandom();
                    Customer nextCustomer = new Customer(t + arr2, serviceTimeDist[0].nextRandom(), route2, ct2);
                    // schedule the arrival event
                    eventq.addEvent(new Event(Event.ARRIVAL, nextCustomer.getArrivalTime(), nextCustomer));

                }

            } else if (e.getType() == Event.DEPARTURE) {

                //First check whether the customer is still in the system
                if (!c.hasLeft()) {
                    // check whether customer is at last station or next loation is exit
                    if (stationNr == (nrStations - 1) || c.getnLocation() == -1) {
                        carlef++;
                        //register his sojourn time. replace this
                        results.registerSojournTime(t - c.getSystemArrival());
                        if(c.getCT() == 1){
                            q.removeCustomer(c);
                        } else if(c.getCT() == 2){
                            q.removeCustomer(c);
                            q.removeCustomer(c);
                        }
                        qsC[stationNr].removeCustomer(c);
                        c.leaveSystem();
                    } else {
                        if (stationNr != 0) {
                            //Check for the virtual queue
                            if (vq[stationNr - 1].getSize() == 0) {
                                
                                if(c.getCT() == 2){
                                    if( (qs[c.getnLocation()].getNumberOfServers() - qs[c.getnLocation()].getSize() )>= 2 ){
                                        q.removeCustomer(c);
                                        q.removeCustomer(c);
                                        if(c.getnLocation() != 4){
                                            c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                            c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                        } else {
                                              LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                              c.moveTo(c.getnLocation(), t, lognorm.sample());
                                              c.moveTo(c.getnLocation(), t, lognorm.sample());
                                        }
                                        c.setpLocation(c.getLocation());
                                        c.setLocation(c.getnLocation());
                                        c.setpLocation(c.getLocation());
                                        c.setLocation(c.getnLocation());
                                        qsC[stationNr].removeCustomer(c);

                                    } else {
                                        vq[stationNr - 1].addCustomer(c);
                                        
                                         c.setVqTime(t);
                                         vq[stationNr - 1].addCustomer(c);
                                         vqC[stationNr - 1].addCustomer(c);
                                          c.setVqTime(t);

                                          results.registerBlocking(stationNr - 1);
                                          blockingCounter[stationNr-1]++;
                                    }

                                } else if(c.getCT() == 1){
                                    if( qs[c.getnLocation()].getSize() < qs[c.getnLocation()].getNumberOfServers()){
                                        
                                             q.removeCustomer(c);
                                             qsC[stationNr].removeCustomer(c);
                                            if(c.getnLocation() != 4){
                                            c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                            } else {
                                            LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                            c.moveTo(c.getnLocation(), t, lognorm.sample());
                                            }
                                            c.setpLocation(c.getLocation());
                                            c.setLocation(c.getnLocation());
                                            eventq.addEvent(new Event(Event.ARRIVAL, t, c));
                                                
                                      } else {
                                        vq[stationNr - 1].addCustomer(c);
                                        vqC[stationNr - 1].addCustomer(c);
                                        c.setVqTime(t);
                                        results.registerBlocking(stationNr - 1);
                                        blockingCounter[stationNr - 1] ++;
                                        
                                    }
                                }

                            
                            } else {
                               
                                

                                Customer vqc = vq[stationNr - 1].getFirstCustomer();
                                

                                if(vqc.getCT() == 2){
                                    
                                    if(vqc.getnLocation() == -1){
                                        
                                        
                                        carlef++;
                                        //register his sojourn time. replace this
                                        results.registerSojournTime(t - vqc.getSystemArrival());
                                        vqc.leaveSystem();
                                        vqc.leaveSystem();
                                  
                                    } else if(  (qs[vqc.getnLocation()].getNumberOfServers() - qs[vqc.getnLocation()].getSize() ) >= 2 ){

                                        qs[vqc.getLocation()].removeCustomer(vqc);
                                        qs[vqc.getLocation()].removeCustomer(vqc);
                                        vq[stationNr-1].removeCustomer(vqc);
                                        vq[stationNr-1].removeCustomer(vqc);
                                        qsC[vqc.getLocation()].removeCustomer(vqc);
                                        // System.out.println("out of boound: " + vqc.getnLocation());
                                        // System.out.println("current location of vq: " + vqc.getLocation());
                                        // System.out.println("vqc n: "+vqc.getnLocation()); 
                                        // System.out.println("vqc c: "+vqc.getLocation());
                                        // System.out.println("vqc p: "+vqc.getpLocation());
                                        if(vqc.getnLocation() != 4){
                                            vqc.moveTo(vqc.getnLocation(), t, serviceTimeDist[vqc.getnLocation()].nextRandom());
                                            vqc.moveTo(vqc.getnLocation(), t, serviceTimeDist[vqc.getnLocation()].nextRandom());
                                            eventq.addEvent(new Event(Event.ARRIVAL, t + (t - vqc.getVqTime()), vqc));
                                        } else {
                                            LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                            vqc.moveTo(vqc.getnLocation(), t, lognorm.sample());
                                            vqc.moveTo(vqc.getnLocation(), t, lognorm.sample());
                                            eventq.addEvent(new Event(Event.ARRIVAL, t + (t - vqc.getVqTime()), vqc));
                                        }
                                    }

                                } else if(vqc.getCT() == 1){

                                    if(vqc.getnLocation() == -1){
                                        
                                        carlef++;
                                        //register his sojourn time. replace this
                                        results.registerSojournTime(t - vqc.getSystemArrival());
                                        vqc.leaveSystem();
                                  
                                    } else if( qs[vqc.getnLocation()].getSize() < qs[vqc.getnLocation()].getNumberOfServers()){
                                        results.registerWaitingTime(vqc.getnLocation(), t-vqc.getVqTime());

                                        qs[vqc.getLocation()].removeCustomer(vqc);
                                        vq[stationNr-1].removeCustomer(vqc);
                                        qsC[vqc.getLocation()].removeCustomer(vqc);
                                        if(vqc.getnLocation() != 4){
                                            vqc.moveTo(vqc.getnLocation(), t, serviceTimeDist[vqc.getnLocation()].nextRandom());
                                            eventq.addEvent(new Event(Event.ARRIVAL, t + (t - vqc.getVqTime()), vqc));
                                        } else {
                                            LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                            vqc.moveTo(vqc.getnLocation(), t, lognorm.sample());
                                            eventq.addEvent(new Event(Event.ARRIVAL, t + (t - vqc.getVqTime()), vqc));
                                        }
                                    }
                                }
                                
                               


                            }
                        } else {
                           // when stationNr == 0
                            results.registerWaitingTime(0, t-c.getSystemArrival());

                            if(c.getCT() == 1){
                                q.removeCustomer(c);
                                qsC[stationNr].removeCustomer(c);
                                if(qs[c.getnLocation()].getSize() < qs[c.getnLocation()].getNumberOfServers()){
                                    if(c.getnLocation() == 4){
                                        LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                        c.moveTo(c.getnLocation(), t, lognorm.sample());
                                        
                                    } else {
                                       c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                    }
        
                                    c.setpLocation(c.getLocation());
                                    c.setLocation(c.getnLocation());
                                    eventq.addEvent(new Event(Event.ARRIVAL, t, c));
                                } else {
                                    vq[c.getnLocation()-1].addCustomer(c);
                                    vqC[c.getnLocation()-1].addCustomer(c);
                                    c.setVqTime(t);
                                    results.registerBlocking(c.getnLocation()-1);
                                    blockingCounter[c.getnLocation()-1]++;
                                }
                            } else if(c.getCT() == 2){
                                q.removeCustomer(c);
                                q.removeCustomer(c);
                                qsC[stationNr].removeCustomer(c);
                                if((qs[c.getnLocation()].getNumberOfServers() - qs[c.getnLocation()].getSize() )>= 2){


                                    if(c.getnLocation() == 4){
                                        LogNormalDistribution lognorm = new LogNormalDistribution(1.67,0.86);
                                        c.moveTo(c.getnLocation(), t, lognorm.sample());
                                        c.moveTo(c.getnLocation(), t, lognorm.sample());
                                        
                                    } else {
                                       c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                       c.moveTo(c.getnLocation(), t, serviceTimeDist[c.getnLocation()].nextRandom());
                                    }
        
                                    c.setpLocation(c.getLocation());
                                    c.setLocation(c.getnLocation());
                                    c.setpLocation(c.getLocation());
                                    c.setLocation(c.getnLocation());
                                    eventq.addEvent(new Event(Event.ARRIVAL, t, c));
                                } else {
                                    vq[c.getnLocation()-1].addCustomer(c);
                                    vq[c.getnLocation()-1].addCustomer(c);
                                    results.registerBlocking(c.getnLocation() - 1);
                                    blockingCounter[c.getnLocation()-1]++;
                                    vqC[c.getnLocation()-1].addCustomer(c);
                                    c.setVqTime(t);
                                    c.setVqTime(t);
                                }

                            }
                            
                            
                         
                        }

                    }
                    
             
                }
            }

                       // //print stuff for verification
            // System.out.println("stationNr: " + c.getLocation());
            // System.out.println(Arrays.toString(c.getRoute()));
            // System.out.println("FES size: " + eventq.getFESsize());
            // System.out.println("next L: " + nextL);
            // System.out.println("current L: " + c.getLocation());
            // printing of real queue
           


            for (int i = 0; i < nrStations; i++) {
                // System.out.println("qs: " + qs[i].getSize());
                switch (i) {
                case 0:
                    results.addq0(qsC[i].getSize(), t);
                    break;
                case 1:
                    results.addq1(qsC[i].getSize(), t);
                    break;
                case 2:
                    results.addq2(qsC[i].getSize(), t);
                    break;
                case 3:
                    results.addq3(qsC[i].getSize(), t);
                    break;
                case 4:
                    results.addq4(qsC[4].getSize(), t);
                    break;
                default:
                    System.out.println("noooooooooo");
                    break;
                }

            }
            //printing of virtual queue
            for (int i = 0; i < nrStations - 1; i++) {
                // System.out.println("vq: " + vq[i].getSize());
                switch (i) {
                case 0:
                    results.addvq1(vqC[i].getSize(), t);
                    break;
                case 1:
                    results.addvq2(vqC[i].getSize(), t);
                    break;
                case 2:
                    results.addvq3(vqC[i].getSize(), t);
                    break;
                case 3:
                    results.addvq4(vqC[i].getSize(), t);
                    break;
                default:
                    System.out.println("noooooooooo");
                    break;
                }

            }

           
        }
        //  System.out.println("Blocking: "+Arrays.toString(blockingCounter));
        //  System.out.println("arrival : " + cararr);
        //  System.out.println("departed: " + carlef);
        return results;
    }

    public static void main(String[] arg) {

    int outerRun = 1;
    int run = 2000;
    int yy = 2400;
    int[] qnorm0A = new int[2600];
    int[] qnorm1A = new int[2600];
    int[] qnorm2A = new int[2600];
    int[] qnorm3A = new int[2600];
    int[] qnorm4A = new int[2600];
    int[] qnormvq1A = new int[2600];
    int[] qnormvq2A = new int[2600];
    int[] qnormvq3A = new int[2600];
    int[] qnormvq4A = new int[2600];

    ArrayList<Double> MWt0 = new ArrayList<>();
    ArrayList<Double> MWt1 = new ArrayList<>();
    ArrayList<Double> MWt2 = new ArrayList<>();
    ArrayList<Double> MWt3 = new ArrayList<>();
    ArrayList<Double> MWt4 = new ArrayList<>();

    ArrayList<Integer> blk1 = new ArrayList<>();
    ArrayList<Integer> blk2 = new ArrayList<>();
    ArrayList<Integer> blk3 = new ArrayList<>();
    ArrayList<Integer> blk4 = new ArrayList<>();
  

    //List sroring statistics
    ArrayList<Double> meanSJtime = new ArrayList<>();
    ArrayList<Double> meanWaitingTime = new ArrayList<>();




//repetition of repetition
for(int v=0; v < outerRun; v++){
        //repetition for the simulation
        for(int m=0 ; m< run; m++){
            
                    
                        int[] nrParking = { 1, 4, 4, 3, 2 };
                        Random rng = new Random();
                
                        Distribution[] serviceDist = new Distribution[5];
                
                        serviceDist[0] = new ExponentialDistribution(2, rng);
                        serviceDist[1] = new GammaDistribution(1.3487, 5.603, rng);
                     serviceDist[2] = new ExponentialDistribution(0.2829, rng);
                     //   serviceDist[2] = new GammaDistribution(0.854, 4.136, rng);
                        serviceDist[3] = new ExponentialDistribution(0.1739, rng);
                        serviceDist[4] = new NormalDistribution(2.133, 2.09, rng);
                
                        // serviceDist[0] = new BernoulliDistribution(0, rng);
                        // serviceDist[1] = new BernoulliDistribution(0, rng);
                        // serviceDist[2] = new BernoulliDistribution(0, rng);
                        // serviceDist[3] = new BernoulliDistribution(0, rng);
                        // serviceDist[4] = new BernoulliDistribution(0, rng);
                
                        Distribution patienceDist = new ExponentialDistribution(0.1, rng);
                
                        GGcSimulationWithImpatience sim = new GGcSimulationWithImpatience(new ExponentialDistribution(0.93, rng),
                                serviceDist, patienceDist, nrParking);
                        long t1 = System.currentTimeMillis();
                        SimResultsArr results = sim.simulate(240);
                        long t2 = System.currentTimeMillis();
            
            
                        
                        ArrayList<Integer> q0norm = new ArrayList<>();
                        ArrayList<Integer> q1norm = new ArrayList<>();
                        ArrayList<Integer> q2norm = new ArrayList<>();
                        ArrayList<Integer> q3norm = new ArrayList<>();
                        ArrayList<Integer> q4norm = new ArrayList<>();
                        ArrayList<Integer> vq1norm = new ArrayList<>();
                        ArrayList<Integer> vq2norm = new ArrayList<>();
                        ArrayList<Integer> vq3norm = new ArrayList<>();
                        ArrayList<Integer> vq4norm = new ArrayList<>();
            
                        // normalizing queues
                        // for (int i=0; i<results.getq0().size(); i++){
                        //     if(i>0){
                        //     for(double j=Math.floor(results.getq0T().get(i-1)* 10 ); j < Math.floor(results.getq0T().get(i)* 10 ); j++){
                        //             q0norm.add( results.getq0().get(i-1));
                        //             q1norm.add( results.getq1().get(i-1));
                        //             q2norm.add( results.getq2().get(i-1));
                        //             q3norm.add( results.getq3().get(i-1));
                        //             q4norm.add( results.getq4().get(i-1));
                        //             vq1norm.add( results.getvq1().get(i-1));
                        //             vq2norm.add( results.getvq2().get(i-1));
                        //             vq3norm.add( results.getvq3().get(i-1));
                        //             vq4norm.add( results.getvq4().get(i-1));
                        //         }
                        //     }
                        // }
            
            
                        
                        // System.out.println("q1: "+Arrays.toString(results.getq1().toArray()));
            
                        //not normalizing 
                        // for(int i=0; i<results.getq0().size(); i++){
                        //     q0norm.add( results.getq0().get(i));
                        //     q1norm.add( results.getq1().get(i));
                        //     q2norm.add( results.getq2().get(i));
                        //     q3norm.add( results.getq3().get(i));
                        //     q4norm.add( results.getq4().get(i));
                        //     vq1norm.add( results.getvq1().get(i));
                        //     vq2norm.add( results.getvq2().get(i));
                        //     vq3norm.add( results.getvq3().get(i));
                        //     vq4norm.add( results.getvq4().get(i));
                        // }
                        
                        
            
                        for(int x=0; x< 2600; x++){
                            qnorm0A[x] += 0;
                            qnorm1A[x] += 0;
                            qnorm2A[x] += 0;
                            qnorm3A[x] += 0;
                            qnorm4A[x] += 0;
                            qnormvq1A[x] += 0;
                            qnormvq2A[x] += 0;
                            qnormvq3A[x] += 0;
                            qnormvq4A[x] += 0;
            
                        }
            
                        //accessing the array outside of the loop
                        // for(int x2=0; x2< q0norm.size(); x2 ++){
                        //     qnorm0A[x2] += q0norm.get(x2);
                        //     qnorm1A[x2] += q1norm.get(x2);
                        //     qnorm2A[x2] += q2norm.get(x2);
                        //     qnorm3A[x2] += q3norm.get(x2);
                        //     qnorm4A[x2] += q4norm.get(x2);
                        //     qnormvq1A[x2] += vq1norm.get(x2);
                        //     qnormvq2A[x2] += vq2norm.get(x2);
                        //     qnormvq3A[x2] +=  vq3norm.get(x2);
                        //     qnormvq4A[x2] +=  vq4norm.get(x2);
            
                        // }


                        // Check for stuff
                        for(int x2=0; x2< results.getq1().size(); x2 ++){
                            qnorm0A[x2] += results.getq0().get(x2);
                            qnorm1A[x2] += results.getq1().get(x2);
                            qnorm2A[x2] += results.getq2().get(x2);
                            qnorm3A[x2] += results.getq3().get(x2);
                            qnorm4A[x2] += results.getq4().get(x2);
                            // qnormvq1A[x2] += vq1norm.get(x2);
                            // qnormvq2A[x2] += vq2norm.get(x2);
                            // qnormvq3A[x2] +=  vq3norm.get(x2);
                            // qnormvq4A[x2] +=  vq4norm.get(x2);
            
                        }
            
            
            // //printing for test
            // System.out.println("size: "+ results.getq0().size());
            // System.out.println("q0n= "+Arrays.toString(q0norm.toArray())+";");
            //  System.out.println("q1= "+Arrays.toString(results.getq1().toArray())+";");
            // System.out.println("q2n= "+Arrays.toString(q2norm.toArray())+";");
            //  System.out.println("q2= "+Arrays.toString(results.getq2().toArray())+";");
            //  System.out.println("q2T= "+Arrays.toString(results.getq2T().toArray())+";");
            // System.out.println("q3= "+Arrays.toString(q3norm.toArray())+";");
            // System.out.println("q4= "+Arrays.toString(q4norm.toArray())+";");
            //  System.out.println("Mean sojourn time: " + results.getMeanSojournTime());
            //  System.out.println("blocking per station: "+ Arrays.toString(results.getBlockCounter()));
             
            

             MWt0.add(results.getVarianceWaitingTimes()[0]);
             MWt1.add(results.getVarianceWaitingTimes()[1]);
             MWt2.add(results.getVarianceWaitingTimes()[2]);
             MWt3.add(results.getVarianceWaitingTimes()[3]);
             MWt4.add(results.getVarianceWaitingTimes()[4]);

          
                 blk1.add( results.getBlockCounter()[0]);
                 blk2.add( results.getBlockCounter()[1]);
                 blk3.add( results.getBlockCounter()[2]);
                 blk4.add( results.getBlockCounter()[3]);
              

             
            

             results.seTvarWt0(results.getVarianceWaitingTimes()[0]);
             results.seTvarWt1(results.getVarianceWaitingTimes()[1]);
             results.seTvarWt2(results.getVarianceWaitingTimes()[2]);
             results.seTvarWt3(results.getVarianceWaitingTimes()[3]);
             results.seTvarWt4(results.getVarianceWaitingTimes()[4]);

            //  results.setBlk1(results.getBlockCounter()[0]);
            //  results.setBlk2(results.getBlockCounter()[1]);
            //  results.setBlk3(results.getBlockCounter()[2]);
            //  results.setBlk4(results.getBlockCounter()[3]);

            meanSJtime.add(results.getMeanSojournTime());

             
            //  System.out.println("vq1= "+Arrays.toString(results.getvq1().toArray())+";");
            //  System.out.println("vq2= "+Arrays.toString(results.getvq2().toArray())+";");
            //  System.out.println("vq3= "+Arrays.toString(results.getvq3().toArray())+";");
            //  System.out.println("vq4= "+Arrays.toString(results.getvq4().toArray())+";");
             
            }


            
}

System.out.println("blk1: "+Arrays.toString(blk1.toArray()));
System.out.println("blk2: "+Arrays.toString(blk2.toArray()));
System.out.println("blk3: "+Arrays.toString(blk3.toArray()));
System.out.println("blk4: "+Arrays.toString(blk4.toArray()));

// System.out.println("mSJ="+Arrays.toString (meanSJtime.toArray())+";");
// System.out.println("wt0="+Arrays.toString (MWt0.toArray() )+";");
// System.out.println("wt1="+Arrays.toString (MWt1.toArray() )+";");
// System.out.println("wt2="+Arrays.toString (MWt2.toArray() )+";");
// System.out.println("wt3="+Arrays.toString (MWt3.toArray() )+";");
// System.out.println("wt4="+Arrays.toString (MWt4.toArray() )+";");





        // int[] qnorm0sum = new int[2600];
        // int[] qnorm1sum = new int[2600];
        // int[] qnorm2sum = new int[2600];
        // int[] qnorm3sum = new int[2600];
        // int[] qnorm4sum = new int[2600];
        // int[] vqnorm1sum = new int[2600];
        // int[] vqnorm2sum = new int[2600];
        // int[] vqnorm3sum = new int[2600];
        // int[] vqnorm4sum = new int[2600];


    // for(int x=0; x< 2600; x++){
    //     for(int m=0; m<run; m++){
    //         qnorm0sum[x] += qnorm0A[m][x];
    //         qnorm1sum[x] += qnorm1A[m][x];
    //         qnorm2sum[x] += qnorm2A[m][x];
    //         qnorm3sum[x] += qnorm3A[m][x]; 
    //         qnorm4sum[x] += qnorm4A[m][x];
    //         vqnorm1sum[x] += qnormvq1A[m][x];
    //         vqnorm2sum[x] += qnormvq2A[m][x];
    //         vqnorm3sum[x] += qnormvq3A[m][x];
    //         vqnorm4sum[x] += qnormvq4A[m][x];
    //     }
    // }

    double[] qnorm0avg = new double[2400];
    double[] qnorm1avg = new double[2400];
    double[] qnorm2avg = new double[2400];
    double[] qnorm3avg = new double[2400];
    double[] qnorm4avg = new double[2400];
    double[] vqnorm1avg = new double[2400];
    double[] vqnorm2avg = new double[2400];
    double[] vqnorm3avg = new double[2400];
    double[] vqnorm4avg = new double[2400];  

    //averaging the result 
    for(int a=0; a<2400; a++){
        qnorm0avg[a] =  qnorm0A[a]/run;
        qnorm1avg[a] = qnorm1A[a]/run;
        qnorm2avg[a] = qnorm2A[a]/run;
        qnorm3avg[a] = qnorm3A[a]/run;;
        qnorm4avg[a] = qnorm4A[a]/run;; 
        vqnorm1avg[a] = qnormvq1A[a]/run;
        vqnorm2avg[a] = qnormvq2A[a]/run;
        vqnorm3avg[a] = qnormvq3A[a]/run;
        vqnorm4avg[a] = qnormvq4A[a]/run;
    }




        // System.out.println("q0= "+Arrays.toString(qnorm0avg ));
        // System.out.println("q1= "+Arrays.toString(qnorm1avg ));
        // System.out.println("q2= "+Arrays.toString(qnorm2avg ));
        // System.out.println("q3= "+Arrays.toString(qnorm3avg ));
        // System.out.println("q4= "+Arrays.toString(qnorm4avg ));
        // System.out.println("vq1= "+Arrays.toString(vqnorm1avg ));
        // System.out.println("vq2= "+Arrays.toString(vqnorm2avg ));
        // System.out.println("vq3= "+Arrays.toString(vqnorm3avg ));
        // System.out.println("vq4= "+Arrays.toString(vqnorm4avg ));

        // System.out.println("Simulation time: " + (t2 - t1) / 1000 + " seconds.");
       
        // System.out.println("Mean waiting time: " + results.getMeanWaitingTime());
        // System.out.println("Variance of waiting time: " + results.getVarianceWaitingTimes());

        // System.out.println("vq1: " + Arrays.toString(results.getvq1().toArray()));
        // System.out.println("vq1T: " + Arrays.toString(results.getvq1T().toArray()));
        // System.out.println("vq2: " + Arrays.toString(results.getvq2().toArray()));
        // System.out.println("vq3: " + Arrays.toString(results.getvq3().toArray()));
        // System.out.println("vq4: " + Arrays.toString(results.getvq4().toArray()));

        // System.out.println("q0: " + Arrays.toString(results.getq0().toArray()

        // System.out.println("q4: " + Arrays.toString(results.getq4().toArray()));

    }
}


    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    


        


    

    