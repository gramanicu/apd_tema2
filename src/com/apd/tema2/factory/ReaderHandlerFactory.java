package com.apd.tema2.factory;

import com.apd.tema2.Main;
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
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> (handlerType1, br) -> Main.intersection = IntersectionFactory.getIntersection(handlerType1);
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
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection(handlerType);

                    int maxCars = Integer.parseInt(line[0]);
                    ((Maintenance) Main.intersection).setupIntersection(maxCars, 1, 2);
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            default -> null;
        };
    }

}
