package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// Simple intersection, in which each car waits specific amount of time
public class SimpleSemaphore implements Intersection {
    private final CyclicBarrier barrier;

    public SimpleSemaphore() {
        barrier = new CyclicBarrier(Main.carsNo);
    }

    @Override
    public void wait_in_intersection(Car car) {
        System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");

        // Wait all cars to arrive at the intersection
        try {
            barrier.await();
            Thread.sleep(car.getWaitingTime());
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Car " + car.getId() + " has waited enough, now driving...");
    }
}
