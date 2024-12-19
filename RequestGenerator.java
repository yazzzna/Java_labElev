import java.util.Random;

public class RequestGenerator {
    private final int maxFloor;
    private final Random random;

    public RequestGenerator(int maxFloor) {
        this.maxFloor = maxFloor;
        this.random = new Random();
    }

    public Request generateRequest() {
        int fromFloor = random.nextInt(maxFloor) + 1;
        int toFloor = fromFloor;
        while (toFloor == fromFloor) {
            toFloor = random.nextInt(maxFloor) + 1;
        }
        
        return new Request(fromFloor, toFloor);
    }
}
