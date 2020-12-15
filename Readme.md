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

I used a barrier [...]. 

© 2020 Grama Nicolae
