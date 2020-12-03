package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {
    public static IntersectionHandler getHandler(String handlerType) {
        return switch (handlerType) {
            case "simple_semaphore", "simple_n_roundabout", "simple_strict_1_car_roundabout", "simple_strict_x_car_roundabout", "priority_intersection", "simple_max_x_car_roundabout", "crosswalk", "simple_maintenance", "complex_maintenance", "railroad" -> car -> Main.intersection.wait_in_intersection(car);
            default -> null;
        };
    }
}
