package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

public class Crosswalk implements Intersection {

    @Override
    public void wait_in_intersection(Car car) {
        while(!Main.pedestrians.isFinished()) {
            boolean hadGreen = car.couldPass();
            boolean hasGreen = !Main.pedestrians.isPass();
            car.setLastPass(!Main.pedestrians.isPass());

            if(hadGreen && !hasGreen) {
                System.out.println("Car " + car.getId() + " has now red light");
            } else if (!hadGreen && hasGreen) {
                System.out.println("Car " + car.getId() + " has now green light");
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
