package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleStrict1CarRoundabout implements Intersection {
    private List<Lock> occupiedDirections;
    private CyclicBarrier barrier;
    private  Semaphore sem;
    private int exitTime;

    public void setupIntersection(int directions, int exitTime) {
        sem = new Semaphore(directions);
        this.exitTime = exitTime;
        barrier = new CyclicBarrier(Main.carsNo);
        occupiedDirections = Arrays.asList(new Lock[directions]);

        for (int i = 0; i < directions; i++) {
            occupiedDirections.set(i, new ReentrantLock(true));
        }
    }

    @Override
    public void wait_in_intersection(Car car) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Car " + car.getId() + " has reached the roundabout");

        // Occupy a lane
        occupiedDirections.get(car.getStartDirection()).lock();

        // Wait for all lanes to be occupied and enter the roundabout
        try {
            sem.acquire();
            System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
            Thread.sleep(exitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Free a lane
        float sec = (float)exitTime / 1000.f;
        String seconds = sec == (int) sec ? String.format("%d", (int)sec) : String.format("%s", sec);
        System.out.println("Car " + car.getId() + " has exited the roundabout after " + seconds + " seconds");
        sem.release();
        occupiedDirections.get(car.getStartDirection()).unlock();
    }
}
