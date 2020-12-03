package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Roundabout implements Intersection {
    private Semaphore sem;
    private CyclicBarrier barrier;
    private int exitTime;

    public void setupIntersection(int capacity, int exitTime) {
        sem = new Semaphore(capacity);
        this.exitTime = exitTime;
        barrier = new CyclicBarrier(Main.carsNo);
    }

    @Override
    public void wait_in_intersection(Car car) {
        System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
        // Wait all cars to arrive at the intersection
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        try {
            sem.acquire();
            System.out.println("Car " + car.getId() + " has entered the roundabout");
            Thread.sleep(exitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float sec = (float)exitTime / 1000.f;
        String seconds = sec == (int) sec ? String.format("%d", (int)sec) : String.format("%s", sec);
        System.out.println("Car " + car.getId() + " has exited the roundabout after " + seconds + " seconds");
        sem.release();
    }
}
