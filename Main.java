public class Main {
    public static void main(String[] args) {
        int buildingHeight = 10;
        int numElevators = 2;
        int startingFloor = 1;
        int simulationDuration = 30; 

        System.out.println("Simulation started with " + numElevators + " elevators in a building with " + buildingHeight + " floors.");
        System.out.println("==================================================================");

        ElevatorController controller = new ElevatorController(numElevators, buildingHeight, startingFloor);
        controller.startSimulation(simulationDuration);
    }
}
 