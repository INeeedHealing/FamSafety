/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;
    private boolean stoped = false;
    private long oldTime = 0;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.stoped = false;
        oldTime = 0;
    }

    public void moveOn() {
        if (!running && stoped) {
            this.oldTime = getElapsedTime() + oldTime;
            this.startTime = System.currentTimeMillis();
            this.running = true;
            this.stoped = false;           
        }
    }

    public boolean isStoped() {
        return this.stoped;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
        this.stoped = true;
    }

    public boolean isRunning() {
        return this.running;
    }

    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime + oldTime);
        } else {
            elapsed = (stopTime - startTime + oldTime);
        }
        return elapsed;
    }

    //elaspsed time in seconds
    public int getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime + oldTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime + oldTime) / 1000);
        }
        return Math.toIntExact(elapsed);
    }

    public int getElapsedTimeMins() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime + oldTime) / 60000);
        } else {
            elapsed = ((stopTime - startTime + oldTime) / 60000);
        }
        return Math.toIntExact(elapsed);
    }
}
