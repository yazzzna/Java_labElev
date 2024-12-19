import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class Elevator {
    private int currentFloor;
    private int direction;
    private final String name;
    private final PriorityQueue<Request> requestsUp;
    private final PriorityQueue<Request> requestsDown;

    public Elevator(int startingFloor, String elevatorName) {
        this.currentFloor = startingFloor;
        this.direction = 0;
        this.name = elevatorName;
        this.requestsUp = new PriorityQueue<>((r1, r2) -> Integer.compare(r1.getFrom(), r2.getFrom()));
        this.requestsDown = new PriorityQueue<>((r1, r2) -> Integer.compare(r2.getFrom(), r1.getFrom()));
    }

    public synchronized void addRequest(Request request) {
        System.out.println("New Request (from=" + request.getFrom() + ", to=" + request.getTo() + ") assigned to " + name);

        if (request.getFrom() >= currentFloor || (direction >= 0 && request.getTo() >= currentFloor)) {
            requestsUp.offer(request);
        } else {
            requestsDown.offer(request);
        }
        updateDirection();
    }

    public void move() {
        if (requestsUp.isEmpty() && requestsDown.isEmpty()) {
            direction = 0;
            return;
        }

        if (direction == 1) {
            handleRequests(requestsUp);
        } else if (direction == -1) {
            handleRequests(requestsDown);
        }

        updateDirection();
    }

    private void handleRequests(PriorityQueue<Request> requests) {
        while (!requests.isEmpty()) {
            Request request = requests.peek();

            if (isPopUpRequest(request)) {
                requests.poll();
                handleRequest(request);
            } else {
                break;
            }
        }
    }

    private void handleRequest(Request request) {
        if (currentFloor != request.getFrom()) {
            moveToFloor(request.getFrom());
        }

        moveToFloor(request.getTo());
    }

    private boolean isPopUpRequest(Request request) {
        if (direction == 1) {
            return request.getFrom() >= currentFloor;
        } else if (direction == -1) {
            return request.getFrom() <= currentFloor;
        }
        return false;
    }

    private void moveToFloor(int targetFloor) {
        while (currentFloor != targetFloor) {
            currentFloor += (targetFloor > currentFloor ? 1 : -1);
            System.out.println(name + " moving to floor: " + currentFloor);
            processIntermediateRequests();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(name + " stopped at floor: " + currentFloor);
    }

    private void processIntermediateRequests() {
        if (direction == 1) {
            while (!requestsUp.isEmpty() && requestsUp.peek().getFrom() == currentFloor) {
                Request request = requestsUp.poll();
                System.out.println(name + " picked up passenger going to floor: " + request.getTo());
                moveToFloor(request.getTo());
            }
        } else if (direction == -1) {
            while (!requestsDown.isEmpty() && requestsDown.peek().getFrom() == currentFloor) {
                Request request = requestsDown.poll();
                System.out.println(name + " picked up passenger going to floor: " + request.getTo());
                moveToFloor(request.getTo());
            }
        }
    }

    private void updateDirection() {
        if (!requestsUp.isEmpty() && (requestsDown.isEmpty() || requestsUp.peek().getFrom() >= currentFloor)) {
            direction = 1;
        } else if (!requestsDown.isEmpty()) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDirection() {
        return direction;
    }

    public boolean hasRequests() {
        return !requestsUp.isEmpty() || !requestsDown.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }
}


