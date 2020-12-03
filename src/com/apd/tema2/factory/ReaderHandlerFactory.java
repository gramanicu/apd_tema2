package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        return switch (handlerType) {
            case "simple_semaphore", "railroad" -> (handlerType1, br) -> Main.intersection = IntersectionFactory.getIntersection(handlerType1);
            case "simple_n_roundabout" -> (handlerType2, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType2);

                int roundaboutCapacity = Integer.parseInt(line[0]);
                int exitTime = Integer.parseInt(line[1]);
                ((Roundabout) Main.intersection).setupIntersection(roundaboutCapacity, exitTime);
            };
            case "simple_strict_1_car_roundabout" -> (handlerType3, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType3);

                int directionsCount = Integer.parseInt(line[0]);
                int exitTime = Integer.parseInt(line[1]);
                ((StrictRoundabout) Main.intersection).setupIntersection(directionsCount, exitTime, 1, 3);
            };
            case "simple_strict_x_car_roundabout" -> (handlerType4, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType4);

                int directionsCount = Integer.parseInt(line[0]);
                int exitTime = Integer.parseInt(line[1]);
                int carsPerDirection = Integer.parseInt(line[2]);
                ((StrictRoundabout) Main.intersection).setupIntersection(directionsCount, exitTime, carsPerDirection, 4);
            };
            case "simple_max_x_car_roundabout" -> (handlerType5, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType5);

                int directionsCount = Integer.parseInt(line[0]);
                int exitTime = Integer.parseInt(line[1]);
                int carsPerDirection = Integer.parseInt(line[2]);
                ((StrictRoundabout) Main.intersection).setupIntersection(directionsCount, exitTime, carsPerDirection, 5);
            };
            case "priority_intersection" -> (handlerType6, br) -> {

                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType6);

                int highPriorityCars = Integer.parseInt(line[0]);
                int lowPriorityCars = Integer.parseInt(line[1]);
                ((PriorityIntersection) Main.intersection).setupIntersection(lowPriorityCars, highPriorityCars);
            };
            case "crosswalk" -> (handlerType7, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType7);

                int execTime = Integer.parseInt(line[0]);
                int maxNumberPedestrians = Integer.parseInt(line[1]);
                Main.pedestrians = new Pedestrians(execTime, maxNumberPedestrians);
            };
            case "simple_maintenance" -> (handlerType8, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType8);

                int maxCars = Integer.parseInt(line[0]);
                ((Maintenance) Main.intersection).setupIntersection(maxCars, 1, 2, 8);
            };
            case "complex_maintenance" -> (handlerType9, br) -> {
                String[] line = br.readLine().split(" ");
                Main.intersection = IntersectionFactory.getIntersection(handlerType9);

                int freeLanes = Integer.parseInt(line[0]);
                int initialLanes = Integer.parseInt(line[1]);
                int maxCars = Integer.parseInt(line[2]);
                ((Maintenance) Main.intersection).setupIntersection(maxCars, freeLanes, initialLanes, 9);
            };
            default -> null;
        };
    }

}
