package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class Maintenance implements Intersection {
    private int maxThroughput;
    private int destinationLane;
    private int currentPassed;
    private int availableLanes;
    private int task;

    private CyclicBarrier barrier;
    private Semaphore semaphore;

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

    public void setupIntersection(int maxThroughput, int _freeLanes, int initialLanes, int task) {
        this.maxThroughput = maxThroughput;
        currentPassed = 0;
        destinationLane = 0;
        availableLanes = _freeLanes;
        this.task = task;

        barrier = new CyclicBarrier(Main.carsNo);
        waitingLanes = new ArrayList<>(initialLanes);
        freeLanes = new ArrayList<>(_freeLanes);
        semaphore = new Semaphore(1);

        for (int i = 0; i < initialLanes; i++) {
            waitingLanes.add(new ArrayBlockingQueue<>(Main.carsNo));
        }

        for (int i = 0; i < _freeLanes; i++) {
            freeLanes.add(new ArrayBlockingQueue<>(initialLanes));
        }

        // Assign waiting lanes to free lanes
        for(int i = 0; i < _freeLanes; i++) {
            int start = (int) (i * ((float)initialLanes / _freeLanes));
            int end = (int) ((i+1) * ((float)initialLanes / _freeLanes));
            for(int j = start; j < end; j++) {
                freeLanes.get(i).add(j);
            }
        }
    }

    /**
     * Set next active lane
     */
    private void setNextActive() {
        destinationLane++;
        if (destinationLane == availableLanes) {
            destinationLane = 0;
        }

        if(freeLanes.get(destinationLane).size() == 0) {
            for (int i = 0; i < availableLanes; i++) {
                if(freeLanes.get(i).size() != 0) {
                    destinationLane = i;
                    break;
                }
            }
        }
    }

    @Override
    public void wait_in_intersection(Car car) {
        // Wait all cars to arrive at the "intersection" and assign them to a waiting lane
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingLanes.get(car.getStartDirection()).add(new CarInfo(car));
        if(task == 8) {
            System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has reached the bottleneck");
        } else {

            System.out.println("Car " + car.getId() + " has come from lane number " + car.getStartDirection());
        }
        semaphore.release();

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (true) {
            synchronized (this) {
                int carLane = car.getStartDirection();

                // Check that the destination is available
                if(freeLanes.get(destinationLane).size() == 0) {
                    setNextActive();
                }

                // Check if this car is on a active lane
                if(carLane == freeLanes.get(destinationLane).peek()) {
                    // Check if this car is the car that should pass now
                    if(car.getId() == waitingLanes.get(carLane).peek().id) {
                        // Make the car pass
                        if(task == 8) {
                            System.out.println("Car " + car.getId() + " from side number " + carLane + " has passed the bottleneck");
                        } else {
                            System.out.println("Car " + car.getId() + " from the lane " + carLane + " has entered lane number " + destinationLane);
                        }
                        currentPassed++;

                        // Remove car from the queue
                        waitingLanes.get(carLane).poll();

                        if(waitingLanes.get(carLane).size() == 0) {
                            // Remove lane if it is empty
                            freeLanes.get(destinationLane).poll();

                            if(task == 9) {
                                System.out.println("The initial lane " + carLane + " has been emptied and removed from the new lane queue");
                            }

                            // Make next lane active if enough cars passed
                            if(currentPassed == maxThroughput) {
                                currentPassed = 0;
                                setNextActive();
                            }
                        } else if(currentPassed == maxThroughput) {
                            currentPassed = 0;
                            if (task == 9) {
                                System.out.println("The initial lane " + carLane + " has no permits and is moved to the back of the new lane queue");
                            }

                            // Put lane at the back of the queue if enough cars have passed
                            try {
                                freeLanes.get(destinationLane).put(freeLanes.get(destinationLane).poll());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            setNextActive();
                        }

                        break;
                    }
                }
            }
        }


    }
}
