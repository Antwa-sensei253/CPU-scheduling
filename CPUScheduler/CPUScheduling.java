import java.util.*;

class Process {
    String name;
    int burstTime;
    int priority;

    Process(String name, int burstTime, int priority) {
        this.name = name;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class CPUScheduling {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Enter the number of processes:");
            int NP = input.nextInt();

            // Create a list to store processes
            List<Process> processes = new ArrayList<>();

            // Input process details
            inputs(processes, NP, input);

            System.out.println("Choose an algorithm");

            // Set time quantum for Round Robin
            int timeQuantum = 2;

            // Create queues for processes
            Queue<Process> processQueueRR = new LinkedList<>(processes);
            Queue<Process> processQueueFCFS = new LinkedList<>(processes);
            Queue<Process> processQueueSJF = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));
            Queue<Process> processQueueNPP = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));
            Queue<Process> processQueuePP = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));

            // Perform scheduling
            System.out.println("Round Robin Scheduling:");
            roundRobinScheduling(processQueueRR, timeQuantum, NP);

            System.out.println("\nFCFS Scheduling:");
            fcfsScheduling(processQueueFCFS, NP);

            System.out.println("\nSJF Scheduling:");
            sjfScheduling(processQueueSJF, NP);

            System.out.println("\nNon-Preemptive Priority Scheduling:");
            nonPreemptivePriorityScheduling(processQueueNPP, NP);

            System.out.println("\nPreemptive Priority Scheduling:");
            preemptivePriorityScheduling(processQueuePP, NP);
        }
    }

    private static void inputs(List<Process> processes, int NP, Scanner input) {
        for (int i = 1; i <= NP; i++) {
            System.out.println("Enter details for Process " + i + ":");
            System.out.print("Burst Time: ");
            int burstTime = input.nextInt();
            System.out.print("Priority: ");
            int priority = input.nextInt();
            processes.add(new Process("P" + i, burstTime, priority));
        }
    }

    private static void roundRobinScheduling(Queue<Process> queue, int timeQuantum, int NP) {
        int currentTime = 0;
        int totalTurnaroundTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            System.out.println("Executing " + currentProcess.name);

            // Calculate turnaround time for the current process
            int turnaroundTime = currentTime + Math.min(timeQuantum, currentProcess.burstTime);

            // Update total turnaround time
            totalTurnaroundTime += turnaroundTime;

            // Move the current time forward
            currentTime = turnaroundTime;

            // Update burst time for the current process
            currentProcess.burstTime -= timeQuantum;

            if (currentProcess.burstTime > 0) {
                queue.add(currentProcess);
            } else {
                System.out.println(currentProcess.name + " completed. Turnaround time: " + turnaroundTime);
            }
        }

        // Calculate and print average turnaround time
        double averageTurnaroundTime = (double) totalTurnaroundTime / NP;
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    private static void fcfsScheduling(Queue<Process> queue, int NP) {
        int currentTime = 0;
        int totalTurnaroundTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            System.out.println("Executing " + currentProcess.name);

            // Calculate turnaround time for the current process
            int turnaroundTime = currentTime + currentProcess.burstTime;

            // Update total turnaround time
            totalTurnaroundTime += turnaroundTime;

            // Move the current time forward
            currentTime = turnaroundTime;

            System.out.println(currentProcess.name + " completed. Turnaround time: " + turnaroundTime);
        }

        // Calculate and print average turnaround time
        double averageTurnaroundTime = (double) totalTurnaroundTime / NP;
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    private static void sjfScheduling(Queue<Process> queue, int NP) {
        int currentTime = 0;
        int totalTurnaroundTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            System.out.println("Executing " + currentProcess.name);

            // Calculate turnaround time for the current process
            int turnaroundTime = currentTime + currentProcess.burstTime;

            // Update total turnaround time
            totalTurnaroundTime += turnaroundTime;

            // Move the current time forward
            currentTime = turnaroundTime;

            System.out.println(currentProcess.name + " completed. Turnaround time: " + turnaroundTime);
        }

        // Calculate and print average turnaround time
        double averageTurnaroundTime = (double) totalTurnaroundTime / NP;
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    private static void nonPreemptivePriorityScheduling(Queue<Process> queue, int NP) {
        int currentTime = 0;
        int totalTurnaroundTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            System.out.println("Executing " + currentProcess.name);

            // Calculate turnaround time for the current process
            int turnaroundTime = currentTime + currentProcess.burstTime;

            // Update total turnaround time
            totalTurnaroundTime += turnaroundTime;

            // Move the current time forward
            currentTime = turnaroundTime;

            System.out.println(currentProcess.name + " completed. Turnaround time: " + turnaroundTime);
        }

        // Calculate and print average turnaround time
        double averageTurnaroundTime = (double) totalTurnaroundTime / NP;
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
    private static void preemptivePriorityScheduling(Queue<Process> queue, int NP) {
        int currentTime = 0;
        int totalTurnaroundTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            if (currentProcess.burstTime > 0) {
                System.out.println("Executing " + currentProcess.name);

                // Calculate turnaround time for the current process
                int turnaroundTime = currentTime + 1;

                // Update total turnaround time
                totalTurnaroundTime += turnaroundTime;

                // Move the current time forward
                currentTime++;

                // Update burst time for the current process
                currentProcess.burstTime--;

                if (currentProcess.burstTime > 0) {
                    queue.add(currentProcess);
                } else {
                    System.out.println(currentProcess.name + " completed. Turnaround time: " + turnaroundTime);
                }
            }
        }

        // Calculate and print average turnaround time
        double averageTurnaroundTime = (double) totalTurnaroundTime / NP;
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
