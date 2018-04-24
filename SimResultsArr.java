import java.util.ArrayList;

public class SimResultsArr {

    protected double[] sumW, sumW2, sumWs, sumW2s, sumWb, sumW2b;
    protected int[] nW, nWs, nWb;
    
    ArrayList<Double> allSojourntimes = new ArrayList<>();

    //vq length for each vq at a time
    ArrayList<Integer> vqLen1 = new ArrayList<>();
    ArrayList<Double> vqLen1T = new ArrayList<>();

    ArrayList<Integer> vqLen2 = new ArrayList<>();
    ArrayList<Double> vqLen2T = new ArrayList<>();

    ArrayList<Integer> vqLen3 = new ArrayList<>();
    ArrayList<Double> vqLen3T = new ArrayList<>();

    ArrayList<Integer> vqLen4 = new ArrayList<>();
    ArrayList<Double> vqLen4T = new ArrayList<>();

    //q length for each q at each time
    ArrayList<Integer> qLen0 = new ArrayList<>();
    ArrayList<Double> qLen0T = new ArrayList<>();

    ArrayList<Integer> qLen1 = new ArrayList<>();
    ArrayList<Double> qLen1T = new ArrayList<>();

    ArrayList<Integer> qLen2 = new ArrayList<>();
    ArrayList<Double> qLen2T = new ArrayList<>();

    ArrayList<Integer> qLen3 = new ArrayList<>();
    ArrayList<Double> qLen3T = new ArrayList<>();

    ArrayList<Integer> qLen4 = new ArrayList<>();
    ArrayList<Double> qLen4T = new ArrayList<>();

    protected int[] blockCounter;

    //sj time for each run
    ArrayList<Double> meansjR = new ArrayList<>();

    ArrayList<Double> meanWt0 = new ArrayList<>();
    ArrayList<Double> meanWt1 = new ArrayList<>();
    ArrayList<Double> meanWt2 = new ArrayList<>();
    ArrayList<Double> meanWt3 = new ArrayList<>();
    ArrayList<Double> meanWt4 = new ArrayList<>();

    ArrayList<Double> varWt0 = new ArrayList<>();
    ArrayList<Double> varWt1 = new ArrayList<>();
    ArrayList<Double> varWt2 = new ArrayList<>();
    ArrayList<Double> varWt3 = new ArrayList<>();
    ArrayList<Double> varWt4 = new ArrayList<>();

    ArrayList<Integer> blk1 = new ArrayList<>();
    ArrayList<Integer> blk2 = new ArrayList<>();
    ArrayList<Integer> blk3 = new ArrayList<>();
    ArrayList<Integer> blk4 = new ArrayList<>();




    


    //number of sj time observe
    int nS; 
    protected double sumS;
    
    public SimResultsArr(int nrOfStations){
        this.sumW = new double[nrOfStations];
        this.sumWs = new double[nrOfStations];
        this.sumWb = new double[nrOfStations];

        this.sumW2 = new double[nrOfStations];
        this.sumW2s = new double[nrOfStations];
        this.sumW2b = new double[nrOfStations];

        this.nW = new int[nrOfStations];
        this.nWs = new int[nrOfStations];
        this.nWb = new int[nrOfStations];
        this.nS = 0;

        this.blockCounter = new int[nrOfStations - 1];
        
    }

    public void saveMW0(double w){
        meanWt0.add(w);
    }
    public ArrayList<Double> getMW0 (){
        return meanWt0;
    }

    public void saveMW1(double w){
        meanWt1.add(w);
    }
    public ArrayList<Double> getMW1 (){
        return meanWt1;
    }

    public void saveMW2(double w){
        meanWt2.add(w);
    }
    public ArrayList<Double> getMW2 (){
        return meanWt2;
    }

    public void saveMW3(double w){
        meanWt3.add(w);
    }
    public ArrayList<Double> getMW3 (){
        return meanWt3;
    }

    public void saveMW4(double w){
        meanWt4.add(w);
    }
    public ArrayList<Double> getMW4 (){
        return meanWt4;
    }


    public void seTvarWt0(double w){
        varWt0.add(w);
    }
    public ArrayList<Double> getVarWt0 (){
        return varWt0;
    }
    public void seTvarWt1(double w){
        varWt1.add(w);
    }
    public ArrayList<Double> getVarWt1 (){
        return varWt1;
    }
    public void seTvarWt2(double w){
        varWt2.add(w);
    }
    public ArrayList<Double> getVarWt2 (){
        return varWt2;
    }
    public void seTvarWt3(double w){
        varWt3.add(w);
    }
    public ArrayList<Double> getVarWt3 (){
        return varWt3;
    }
    public void seTvarWt4(double w){
        varWt4.add(w);
    }
    public ArrayList<Double> getVarWt4 (){
        return varWt4;
    }
   


    public void setBlk1(int b){
        blk1.add(b);
    }
    public void setBlk2(int b){
        blk2.add(b);
    }
    public void setBlk3(int b){
        blk3.add(b);
    }
    public void setBlk4(int b){
        blk4.add(b);
    }

    public ArrayList<Integer> getBlk1(int b){
        return blk1;
    }
    public ArrayList<Integer> getBlk2(int b){
        return blk2;
    }
    public ArrayList<Integer> getBlk3(int b){
        return blk3;
    }
    public ArrayList<Integer> getBlk4(int b){
        return blk4;
    }


    public void sjtimeperrun(double sj){
        meansjR.add(sj);

    }

    public ArrayList<Double> getsjperrun(){
        return meansjR;
    }

    

    public void registerBlocking(int station){
        blockCounter[station] ++;
    }

    public int[] getBlockCounter(){
        return blockCounter;
    }

    public void registerWaitingTime(int station, double w){
        sumW[station] += w;
        sumW2[station] += w*w;
        nW[station] ++;
    }

    public void registerBWaitingTime(int station, double w){
        sumWb[station] += w;
        sumW2b[station] += w*w;
        nWb[station] ++;
    }

    public void registerSWaitingTime(int station, double w){
        sumWs[station] += w;
        sumW2s[station] += w*w;
        nWs[station] ++;
    }

    public void registerSojournTime(double sjTime){
        sumS += sjTime;
        nS ++;
        allSojourntimes.add(sjTime);
    }

    public double getMeanSojournTime(){
        double sum = 0 ;
        for(int i=0; i< allSojourntimes.size(); i++){
            sum += allSojourntimes.get(i);
        }
        return sum/nS;
    }

    public double[] getMeanWaitingTime(){
        double[] meanW = new double[sumW.length];
        for(int i=0; i<meanW.length; i++){
            meanW[i] = sumW[i]/nW[i];
        }
        return meanW;
    }


    public double[] getMeanWaitingTimeS(){
        double[] meanWs = new double[sumWs.length];
        for(int i=0; i<meanWs.length; i++){
            meanWs[i] = sumWs[i]/nWs[i];
        }
        return meanWs;
    }

    public double[] getMeanWaitingTimeB(){
        double[] meanWb = new double[sumWb.length];
        for(int i=0; i<meanWb.length; i++){
            meanWb[i] = sumWb[i]/nWb[i];
        }
        return meanWb;
    }

    public double[] getVarianceWaitingTimes(){
        double[] varW = new double[sumW.length];
        for(int i=0; i<varW.length; i++){
            varW[i] = sumW2[i] / nW[i] - (sumW[i]/nW[i])*(sumW[i]/nW[i]);
        }
        return varW;
    }

    public double[] getVarianceWaitingTimesS(){
        double[] varWs = new double[sumWs.length];
        for(int i=0; i<varWs.length; i++){
            varWs[i] = sumW2s[i] / nWs[i] - (sumWs[i]/nWs[i])*(sumWs[i]/nWs[i]);
        }
        return varWs;
    }

    public double[] getVarianceWaitingTimesB(){
        double[] varWb = new double[sumWb.length];
        for(int i=0; i<varWb.length; i++){
            varWb[i] = sumW2b[i] / nWb[i] - (sumWb[i]/nWb[i])*(sumWb[i]/nWb[i]);
        }
        return varWb;
    } 
    
    public void addvq1(int vq, double t){
        vqLen1.add(vq);
        vqLen1T.add(t);
    }

    public ArrayList<Integer> getvq1(){
        return vqLen1;
    }
    public ArrayList<Double> getvq1T(){
        return vqLen1T;
    }

    public void addvq2(int vq, double t){
        vqLen2.add(vq);
        vqLen2T.add(t);
    }

    public ArrayList<Integer> getvq2(){
        return vqLen2;
    }
    public ArrayList<Double> getvq2T(){
        return vqLen2T;
    }

    public void addvq3(int vq, double t){
        vqLen3.add(vq);
        vqLen3T.add(t);
    }

    public ArrayList<Integer> getvq3(){
        return vqLen3;
    }
    public ArrayList<Double> getvq3T(){
        return vqLen3T;
    }

    public void addvq4(int vq, double t){
        vqLen4.add(vq);
        vqLen4T.add(t);
    }

    public ArrayList<Integer> getvq4(){
        return vqLen4;
    }
    public ArrayList<Double> getvq4T(){
        return vqLen4T;
    }

    public void addq0(int q, double t){
        qLen0.add(q);
        qLen0T.add(t);
    }

    public ArrayList<Integer> getq0(){
        return qLen0;
    }
    public ArrayList<Double> getq0T(){
        return qLen0T;
    }

    public void addq1(int q, double t){
        qLen1.add(q);
        qLen1T.add(t);
    }

    public ArrayList<Integer> getq1(){
        return qLen1;
    }
    public ArrayList<Double> getq1T(){
        return qLen1T;
    }

    public void addq2(int q, double t){
        qLen2.add(q);
        qLen2T.add(t);
    }

    public ArrayList<Integer> getq2(){
        return qLen2;
    }
    public ArrayList<Double> getq2T(){
        return qLen2T;
    }
    public void addq3(int q, double t){
        qLen3.add(q);
        qLen3T.add(t);
    }
    public ArrayList<Integer> getq3(){
        return qLen3;
    }
    public ArrayList<Double> getq3T(){
        return qLen3T;
    }
    public void addq4(int q, double t){
        qLen4.add(q);
        qLen4T.add(t);
    }
    public ArrayList<Integer> getq4(){
        return qLen4;
    }
    public ArrayList<Double> getq4T(){
        return qLen4T;
    }
}