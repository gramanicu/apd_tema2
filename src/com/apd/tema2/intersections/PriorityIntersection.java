package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;


public class PriorityIntersection implements Intersection {
    private ArrayBlockingQueue<Integer> highPriorityQueue;
    private ArrayBlockingQueue<Integer> lowPriorityQueue;

    public void setupIntersection(int lowPriorityCount, int highPriorityCount) {
        highPriorityQueue = new ArrayBlockingQueue<Integer>(Main.carsNo);
        lowPriorityQueue = new ArrayBlockingQueue<Integer>(Main.carsNo);
    }

    @Override
    public void wait_in_intersection(Car car) {
        if (car.getPriority() != 1) {
            highPriorityQueue.add(car.getId());
        } else {
            lowPriorityQueue.add(car.getId());
        }

        if (car.getPriority() == 1) {
            System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
            // Low priority cars have to wait in the intersection
            while (highPriorityQueue.size() != 0 || lowPriorityQueue.peek() != car.getId()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
            lowPriorityQueue.poll();
        } else {
            System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
            highPriorityQueue.poll();
        }
    }
}
