# APD, 2nd Homework - "Traffic simulator"

The second homework for the Parallel & Distributed Algorithms Course ([The problem statement](tema%202%20-%20enunt.pdf))

## Problem solution

Each type of intersection is defined in the "intersections" package (some of the exercises are solved using the same intersection class, like ex 3, 4 & 5 for example). The project's "framework" is managing the input parsing and most of the logic (except the specific intersection's logic).

In the `IntersectionHandlerFactory`, there were a few `sleep` calls, which I moved inside the intersection's implementations.

### General Implementation

Each intersection implements the `wait_in_intersection` method (which contains the intersection logic), and some of them also implement an "intersection setup" method (which is used in the `ReaderHandlerFactory` to pass the intersection specific data: number of lanes, task number, etc..).

### Ex. 1 - `SimpleSemaphore`

To solve this problem I used a barrier, to make sure all the cars reach the semaphore, then I added the delay (sleep).

### Ex. 2 - `Roundabout`

I used a barrier to make sure all the cars reach the intersection, then a semaphore (who has a number of tickets equal to the intersection capacity) and a sleep (the time to exit the intersection).

### Ex. 2, 3, 4 - `StrictRoundabout`

The `StrictRoundabout` class implements mainly task 4, as it is the most general of them all. However, there are some slight differences between the tasks, mostly related to the output.

The intersection logic (in the brackets I wrote the number of the task that step is specific to - if it is the case):

- delay moved from `IntersectionHandlerFactory` (5)
- wait for all cars to arrive at the intersection - with a barrier (4)
- enter the lane - `occupiedDirection` array of barriers
- wait for all lanes to be full - barrier (3 & 4)
- traverse the roundabout - sleep
- wait all cars to exit the roundabout - barrier (4)

### Ex. 6 - `PriorityIntersection`

The cars enter two queues, based on their priority. The `highPriorityQueue` represents the high priority cars that are in the intersection, while the `lowPriorityQueue` represents the low priority cars that are waiting to enter.

High priority cars enter the intersection right away, stay in it for a specific amount of time (2000 ms, with a sleep), then they exit the intersection (are removed from the queue).

The low priority cars wait until there are now high priority cars in the intersection and they are the car that should enter (at the top of the queue). After that, they enter and exit the intersection instantly.

### Ex. 7 - `Crosswalk`

Note - This intersection was implemented using busy-waiting (allowed according to [this](https://curs.upb.ro/mod/forum/discuss.php?d=31790) forum thread).

Until the test is finished (`Pedestrians.finished`), we wait for a change of state of the "semaphore" (the pedestrians could pass and now they can't or the other way around). When this happens, we print a corresponding message (if they could pass and now they can't, it means the car has a green light).

### Ex. 8, 9 - `Maintenance`

This intersection was the hardest to implement (and in the end, I couldn't, as it "complex_maintenance" task doesn't work). I decided to combine the two task, as the later is a generalization of the former.

At the start (during the parsing of the input and the setup of the intersection), "source lanes" are assigned to the "destination lanes". The `freeLanes` (destination) variable stores an array with the id of its assigned `waitingLanes` (source).

At he beginning, cars enter their waiting lanes. Then, they wait for all the other cars (barrier). Each car will wait for it's turn to enter the intersection and go from the `waitingLanes` to the `freeLanes`. If a waiting car is empty, it is removed. After a car exits the intersection, `notifyAll` is called so that another car will pass the intersection.

### Ex. 10 - `Railroad`

One at a time, the cars will enter a queue to wait the "train" to pass (barrier). After the train has passed (all cars reached the intersection), we print the message from one of the threads (the car with the id 0). Another barrier is used to "prepare" all the cars to exit the intersection, and then, they will leave the "railroad crossing" in the same order they entered.

© 2020 Grama Nicolae
