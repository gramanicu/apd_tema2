package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.Map;

import static java.util.Map.entry;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static final Map<String, Intersection> cache = Map.ofEntries(
            entry("simple_semaphore", new SimpleSemaphore()),
            entry("simple_n_roundabout", new Roundabout()),
            entry("simple_strict_1_car_roundabout", new StrictRoundabout()),
            entry("simple_strict_x_car_roundabout", new StrictRoundabout()),
            entry("simple_max_x_car_roundabout", new StrictRoundabout()),
            entry("priority_intersection", new PriorityIntersection()),
            entry("crosswalk", new Crosswalk()),
            entry("simple_maintenance", new Maintenance()),
            entry("complex_maintenance", new Maintenance())
    );

    public static Intersection getIntersection(String handlerType) {
        Intersection i = cache.get(handlerType);
        return cache.get(handlerType);
    }

}
