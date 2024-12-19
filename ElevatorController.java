import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final RequestGenerator requestGenerator;

    public ElevatorController(int numElevators, int buildingHeight, int startingFloor) {
        this.elevators = new ArrayList<>();
        this.requestGenerator = new RequestGenerator(buildingHeight);

        for (int i = 1; i <= numElevators; i++) {
            elevators.add(new Elevator(startingFloor, "Elevator " + i));
        }
    }

    public synchronized void handleRequest(Request request) {
        Elevator bestElevator = null;
        int minCost = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int cost = calculateCost(elevator, request);
            if (cost < minCost) {
                minCost = cost;
                bestElevator = elevator;
            }
        }

        if (bestElevator != null) {
            bestElevator.addRequest(request);
        }
    }

    private int calculateCost(Elevator elevator, Request request) {
        int cost = Integer.MAX_VALUE;

        if (elevator.getDirection() == 0) {
            cost = Math.abs(elevator.getCurrentFloor() - request.getFrom());
        } else if (elevator.getDirection() == 1 && request.getFrom() >= elevator.getCurrentFloor()) {
            cost = request.getFrom() - elevator.getCurrentFloor();
        } else if (elevator.getDirection() == -1 && request.getFrom() <= elevator.getCurrentFloor()) {
            cost = elevator.getCurrentFloor() - request.getFrom();
        }

        return cost;
    }

    public void startSimulation(int durationSeconds) {
        ExecutorService executorService = Executors.newFixedThreadPool(elevators.size());

        for (Elevator elevator : elevators) {
            executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    elevator.move();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) < durationSeconds * 1000L) {
            Request newRequest = requestGenerator.generateRequest();
            handleRequest(newRequest);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                break;
            }
        }

        executorService.shutdownNow();
    }
}