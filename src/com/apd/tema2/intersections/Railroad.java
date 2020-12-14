package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Railroad implements Intersection {
    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private ArrayBlockingQueue<Integer> queue;

    public Railroad() {
        barrier = new CyclicBarrier(Main.carsNo);
        semaphore = new Semaphore(1);
        queue = new ArrayBlockingQueue<>(Main.carsNo);
    }


    @Override
    public void wait_in_intersection(Car car) {
        // Put the cars in the queues, to wait for the train to pass
        try {
            semaphore.acquire();
            queue.put(car.getId());

            System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
            semaphore.release();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Only 1 Thread/Car should print the message
        if (car.getId() == 0) {
            System.out.println("The train has passed, cars can now proceed");
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (queue.peek() != car.getId()) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has started driving");
        queue.poll();

        synchronized (this) {
            notifyAll();
        }
    }
}
