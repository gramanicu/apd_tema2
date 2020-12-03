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
    private int destinationLane;
    private int currentPassed;
    private int availableLanes;

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
        currentPassed = 0;
        destinationLane = 0;
        availableLanes = _freeLanes;

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
                int carLane = car.getStartDirection();
                // Check if this car is on a active lane
                if(carLane == freeLanes.get(destinationLane).peek()) {
                    // Check if this car is the car that should pass now
                    if(car.getId() == waitingLanes.get(carLane).peek().id) {
                        // Make the car pass
                        System.out.println("Car " + car.getId() + " from side number " + carLane + " has passed the bottleneck");
                        currentPassed++;

                        // Remove car from the queue
                        waitingLanes.get(carLane).poll();

                        if(waitingLanes.get(carLane).size() == 0) {
                            // Remove lane if it is empty
                            freeLanes.get(destinationLane).poll();

                            // Make next lane active if enough cars passed
                            if(currentPassed == maxThroughput) {
                                currentPassed = 0;
                                destinationLane++;
                                if(destinationLane == availableLanes) {
                                    destinationLane = 0;
                                }
                            }
                        } else if(currentPassed == maxThroughput) {
                            currentPassed = 0;

                            // Put lane at the back of the queue if enough cars have passed
                            try {
                                freeLanes.get(destinationLane).put(freeLanes.get(destinationLane).poll());

                                // Make next lane active
                                destinationLane++;
                                if(destinationLane == availableLanes) {
                                    destinationLane = 0;
                                }
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
}
