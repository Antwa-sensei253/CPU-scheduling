import java.util.Arrays;
import java.util.Scanner;

public class JobSchedulingAlgorithms {
    public static void main(String[] args) {
        /*
         * This program takes 
         */

        // Input the number of processes
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        // Arrays to store process details
        int[] burstTime = new int[n];
        int[] priority = new int[n];
        int[] arrivalTime = new int[n];

        // Input burst time, priority, and arrival time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for Process " + (i + 1) + ": ");
            burstTime[i] = scanner.nextInt();
            System.out.print("Enter priority for Process " + (i + 1) + ": ");
            priority[i] = scanner.nextInt();
            System.out.print("Enter arrival time for Process " + (i + 1) + ": ");
            arrivalTime[i] = scanner.nextInt();
        }
        // FCFS Scheduling
        double[] fcfsResult = fcfs(burstTime, arrivalTime);

        // SJFNon Scheduling
        double[] sjfNonResult = sjfNon(burstTime, arrivalTime);
        double[] sjfPreResult = sjfPre(burstTime, arrivalTime);

        // Round Robin Scheduling
        System.out.print("Enter time quantum for Round Robin: ");
        int timeQuantum = scanner.nextInt();
        double[] rrResult = roundRobin(burstTime, arrivalTime, timeQuantum);

        // Priority Scheduling
        double[] priorityResult = priorityScheduling(burstTime, arrivalTime, priority);

        // Display results
        System.out.println("\nResults Of The Algorithms");
        System.out.println("###############################################");
        displayResults("FCFS", fcfsResult);
        displayResults("SJFNon", sjfNonResult);
        displayResults("SJFPre", sjfPreResult);
        displayResults("Round Robin", rrResult);
        displayResults("Priority Scheduling", priorityResult);

        scanner.close();
    }

    // FCFS Scheduling Algorithm //Marwan Sabry
    private static double[] fcfs(int[] burstTime, int[] arrivalTime) {
        int n = burstTime.length;
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (int i = 0; i < n; i++) {
            totalWaitingTime += waitingTime[i] = (i == 0) ? 0
                    : Math.max(turnaroundTime[i - 1], arrivalTime[i] - arrivalTime[i - 1]);
            totalTurnaroundTime += turnaroundTime[i] = waitingTime[i] + burstTime[i];
        }

        double[] result = { totalWaitingTime, totalTurnaroundTime, totalWaitingTime / n, totalTurnaroundTime / n };
        return result;
    }
// SJF Non-Preemptive Scheduling Algorithm
private static double[] sjfNon(int[] burstTime, int[] arrivalTime) {
    int n = burstTime.length;
    int[] waitingTime = new int[n];
    int[] turnaroundTime = new int[n];
    int[] burstTimeCopy = Arrays.copyOf(burstTime, n);
    int[] arrivalTimeCopy = Arrays.copyOf(arrivalTime, n);

    // Sort processes based on burst time
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (burstTimeCopy[j] > burstTimeCopy[j + 1]) {
                // Swap burst times
                int tempBurst = burstTimeCopy[j];
                burstTimeCopy[j] = burstTimeCopy[j + 1];
                burstTimeCopy[j + 1] = tempBurst;

                // Swap arrival times
                int tempArrival = arrivalTimeCopy[j];
                arrivalTimeCopy[j] = arrivalTimeCopy[j + 1];
                arrivalTimeCopy[j + 1] = tempArrival;
            }
        }
    }

    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;
    int currentTime = 0;

    for (int i = 0; i < n; i++) {
        // Adjust waiting time based on arrival time
        waitingTime[i] = currentTime - arrivalTimeCopy[i];
        totalWaitingTime += waitingTime[i];
        totalTurnaroundTime += turnaroundTime[i] = waitingTime[i] + burstTimeCopy[i];
        currentTime += burstTimeCopy[i];
    }

    double[] result = { totalWaitingTime, totalTurnaroundTime, totalWaitingTime / n, totalTurnaroundTime / n };
    return result;
}

    private static double[] sjfPre(int[] arrivalTime, int[] burstTime) {
        int n = arrivalTime.length;
        int[] remainingTime = Arrays.copyOf(burstTime, n);
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] completionTime = new int[n];

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        while (true) {
            int minRemainingTime = Integer.MAX_VALUE;
            int shortestJobIndex = -1;
            boolean allJobsCompleted = true;

            for (int i = 0; i < n; i++) {
                if (arrivalTime[i] <= currentTime && remainingTime[i] > 0) {
                    allJobsCompleted = false;
                    if (remainingTime[i] < minRemainingTime) {
                        minRemainingTime = remainingTime[i];
                        shortestJobIndex = i;
                    }
                }
            }

            if (allJobsCompleted) {
                break;
            }

            // Execute the shortest job for one unit of time
            remainingTime[shortestJobIndex]--;
            currentTime++;

            // Update waiting and turnaround times
            for (int i = 0; i < n; i++) {
                if (i != shortestJobIndex && arrivalTime[i] <= currentTime && remainingTime[i] > 0) {
                    waitingTime[i]++;
                }
            }

            if (remainingTime[shortestJobIndex] == 0) {
                completionTime[shortestJobIndex] = currentTime;
                turnaroundTime[shortestJobIndex] = completionTime[shortestJobIndex] - arrivalTime[shortestJobIndex];
                totalWaitingTime += waitingTime[shortestJobIndex];
                totalTurnaroundTime += turnaroundTime[shortestJobIndex];
            }
        }

        double[] result = { totalWaitingTime, totalTurnaroundTime, (double) totalWaitingTime / n, (double) totalTurnaroundTime / n };
        return result;
    }
    // Round Robin Scheduling Algorithm //Mohamed Yasser
    private static double[] roundRobin(int[] burstTime, int[] arrivalTime, int timeQuantum) {
        int n = burstTime.length;
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] remainingTime = Arrays.copyOf(burstTime, n);
        int[] arrivalTimeCopy = Arrays.copyOf(arrivalTime, n);
        int currentTime = 0;
        int[] completionTime = new int[n];

        while (true) {
            boolean done = true;

            for (int i = 0; i < n; i++) {
                if (remainingTime[i] > 0 && arrivalTimeCopy[i] <= currentTime) {
                    done = false;

                    if (remainingTime[i] > timeQuantum) {
                        currentTime += timeQuantum;
                        remainingTime[i] -= timeQuantum;
                    } else {
                        currentTime += remainingTime[i];
                        waitingTime[i] = currentTime - burstTime[i];
                        remainingTime[i] = 0;
                        completionTime[i] = currentTime;
                    }
                }
            }

            if (done) {
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            turnaroundTime[i] = completionTime[i] - arrivalTime[i];
        }

        double totalWaitingTime = Arrays.stream(waitingTime).sum();
        double totalTurnaroundTime = Arrays.stream(turnaroundTime).sum();

        double[] result = { totalWaitingTime, totalTurnaroundTime, totalWaitingTime / n, totalTurnaroundTime / n };
        return result;
    }

    // Priority Scheduling Algorithm //Ahmed Abd
    private static double[] priorityScheduling(int[] burstTime, int[] arrivalTime, int[] priority) {
        int n = burstTime.length;
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] priorityCopy = Arrays.copyOf(priority, n);
        int[] arrivalTimeCopy = Arrays.copyOf(arrivalTime, n);

        Arrays.sort(priorityCopy);

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (int i = 0; i < n; i++) {
            int index = findIndex(priority, priorityCopy[i]);
            totalWaitingTime += waitingTime[index] = (i == 0) ? 0
                    : Math.max(turnaroundTime[i - 1], arrivalTime[index] - arrivalTime[i - 1]);
            totalTurnaroundTime += turnaroundTime[index] = waitingTime[index] + burstTime[index];
        }

        double[] result = { totalWaitingTime, totalTurnaroundTime, totalWaitingTime / n, totalTurnaroundTime / n };
        return result;
    }

    // Helper method to find the index of an element in an array
    private static int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    // Helper method to display results
    private static void displayResults(String algorithm, double[] result) {
        System.out.println(algorithm + " Total Waiting Time: " + result[0]);
        System.out.println(algorithm + " Total Turnaround Time: " + result[1]);
        System.out.println(algorithm + " Average Waiting Time: " + result[2]);
        System.out.println(algorithm + " Average Turnaround Time: " + result[3]);
        System.out.println("------------------------------");
    }
}