/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Week4ANoNetwork;

import java.util.ArrayList;

/**
 *
 * @author MBoon
 */
public class SimResults {

    protected double sumQL, sumW, sumS; //QL is queuelength, W is wai.....
    protected double sumW2; //sum of the squared queue lengths
    protected double sumQL2; //sum of the squared queue lengths
    protected double oldTime;
    protected int nW; //number of waiting times observed
    protected int nS; //number of sojourn times obs.
    protected double amountOfTimeQueueEmpty;
    ArrayList<Double> allSojourntimes = new ArrayList<>();
    protected int nrOfAbandonments;

    public SimResults() {
        this.sumQL = 0;
        this.sumQL2 = 0;
        this.oldTime = 0;
        this.sumW = 0;
        this.sumW2 = 0;
        this.nW = 0;
        this.amountOfTimeQueueEmpty = 0;
        this.nrOfAbandonments = 0;
    }

    public void registerAbandonment() {
        this.nrOfAbandonments++;
    }

    /**
     * This function should be invoked every time the queue length changes,
     * during the simulation.
     *
     * @param time the time at which the queue length changes.
     * @param currentQueueLength the current queue length <b>right before</b>
     * the change.
     */
    public void registerQueueLength(double time, int currentQueueLength) {
        sumQL += currentQueueLength * (time - oldTime);
        sumQL2 += currentQueueLength * currentQueueLength * (time - oldTime);
        if (currentQueueLength == 0) {
            amountOfTimeQueueEmpty += (time - oldTime);
        }
        oldTime = time;

    }

    public void registerSojournTime(double sojourntime) {
        sumS += sojourntime;
        nS++;
        allSojourntimes.add(sojourntime);
    }

    /**
     * This function should be invoked every time a customer is taken into
     * service (and his waiting time ends), during the simulation.
     *
     * @param waitingTime the waiting time of the customer being taken into
     * service.
     */
    public void registerWaitingTime(double waitingTime) {
        sumW += waitingTime;
        sumW2 += waitingTime * waitingTime;
        nW++;
    }

    public double getMeanQueueLength() {
        return sumQL / oldTime;
    }

    public double getMeanWaitingTime() {
        return sumW / nW;
    }

    public double getMeanSojournTime() {
        return sumS / nW;
    }

    public double getSecondMomentQueueLength() {
        return sumQL2 / oldTime;
    }

    public double getSecondMomentWaitingTime() {
        return sumW2 / oldTime;
    }

    public double getProbQueueEmpty() {
        return amountOfTimeQueueEmpty / oldTime;
    }

    public double getVarianceWaitingTimes() {
        return sumW2 / nW - (sumW / nW) * (sumW / nW);
    }

    public int getTotalNumberOfAbandonments() {
        return nrOfAbandonments;
    }

    public double getFractionAbandonments() {
        return 1.0 * nrOfAbandonments / (nS + nrOfAbandonments);
    }
}
