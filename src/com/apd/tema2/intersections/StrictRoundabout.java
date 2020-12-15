package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class StrictRoundabout implements Intersection {
    private List<Semaphore> occupiedDirections;
    private CyclicBarrier barrier;
    private CyclicBarrier stepBarrier;
    private CyclicBarrier selectBarrier;
    private Semaphore sem;

    private int exitTime;
    private int taskNumber;

    /**
     * Set up the data for this type of intersection
     * @param directions The number of directions cars come from
     * @param exitTime The time required for a car to exit the roundabout
     * @param carsPerDirection The number of cars allowed in the roundabout from each direction
     * @param taskNumber The task/intersection to setup
     */
    public void setupIntersection(int directions, int exitTime, int carsPerDirection, int taskNumber) {
        this.exitTime = exitTime;
        this.taskNumber = taskNumber;

        int maxCars = directions * carsPerDirection;
        sem = new Semaphore(maxCars);
        barrier = new CyclicBarrier(Main.carsNo);
        stepBarrier = new CyclicBarrier(maxCars);
        selectBarrier = new CyclicBarrier(maxCars);
        occupiedDirections = new ArrayList<>();

        // Create the direction (and cars per direction) barriers
        for (int i = 0; i < directions; i++) {
            occupiedDirections.add(new Semaphore(carsPerDirection));
        }

    }

    @Override
    public void wait_in_intersection(Car car) {
        // Task specific output
        if(taskNumber == 5) {
            // Moved the sleep/delay here
            try {
                sleep(car.getWaitingTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Car " + car.getId() + " has reached the roundabout from lane " + car.getStartDirection());
        } else if (taskNumber == 4) {
            System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
        } else {
            System.out.println("Car " + car.getId() + " has reached the roundabout");
        }

        if(taskNumber == 4) {
            // Wait all cars to arrive at the roundabout
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        // Wait for all lanes to be occupied and enter the roundabout
        try {
            occupiedDirections.get(car.getStartDirection()).acquire();
            sem.acquire();

            // Wait all cars to be selected to enter roundabout - Task 3 & 4
            if (taskNumber != 5) {
                if(taskNumber == 4) {
                    System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());
                }
                try {
                    selectBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

            // Car will traverse the roundabout
            Thread.sleep(exitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Free a lane
        float sec = (float)exitTime / 1000.f;
        String seconds = sec == (int) sec ? String.format("%d", (int)sec) : String.format("%s", sec);
        System.out.println("Car " + car.getId() + " has exited the roundabout after " + seconds + " seconds");

        // Wait all cars to exit roundabout - Task 4
        if (taskNumber == 4) {
            try {
                stepBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        sem.release();
        occupiedDirections.get(car.getStartDirection()).release();
    }
}
