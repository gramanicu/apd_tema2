package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Maintenance implements Intersection {
    private int maxThroughput;

    private CyclicBarrier barrier;

    private List<ArrayBlockingQueue<CarInfo>> waitingLanes;
    private List<ArrayBlockingQueue<Integer>> freeLanes;

    // A simple class that stores essential car info
    private class CarInfo {
        int id;
        int lane;

        CarInfo() {
            id = 0;
            lane = 0;
        }

        CarInfo(Car car) {
            id = car.getId();
            lane = car.getStartDirection();
        }
    }

    public void setupIntersection(int maxThroughput, int _freeLanes, int initialLanes) {
        this.maxThroughput = maxThroughput;

        barrier = new CyclicBarrier(Main.carsNo);
        waitingLanes = new ArrayList<>(initialLanes);
        freeLanes = new ArrayList<>(_freeLanes);

        for (int i = 0; i < initialLanes; i++) {
            waitingLanes.add(new ArrayBlockingQueue<CarInfo>(Main.carsNo));
        }

        for (int i = 0; i < _freeLanes; i++) {
            freeLanes.add(new ArrayBlockingQueue<Integer>(initialLanes));
        }

        // Assign waiting lanes to free lanes
        for(int i = 0; i < initialLanes; i++) {
            freeLanes.get(i % _freeLanes).add(i);
        }

    }

    @Override
    public void wait_in_intersection(Car car) {
        // Wait all cars to arrive at the "intersection" and assign them to a waiting lane
        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has reached the bottleneck");
        waitingLanes.get(car.getStartDirection()).add(new CarInfo(car));

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (true) {
            synchronized (this) {
                if(car.getStartDirection() == freeLanes.get(0).peek()) {
                    waitingLanes.get(car.getStartDirection()).poll();
                    freeLanes.get(0).poll();
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has passed the bottleneck");

                    // If there are more cars waiting on this lane, put it at the back of the queue
                    if(waitingLanes.get(car.getStartDirection()).size() != 0) {
                        try {
                            freeLanes.get(0).put(car.getStartDirection());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }


    }
}
