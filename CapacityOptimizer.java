public class CapacityOptimizer {
	private static final int NUM_RUNS = 10;

	private static final double THRESHOLD = 5.0d;

	private static int average = 0;

	public static int getOptimalNumberOfSpots(int hourlyRate) {
	
		int lotSize = 1;
		int sum = 0;
		int numberOfRuns = 0;

		while (true) {

			System.out.println("==== Setting Lot Capacity to: " + lotSize + "====");	

			while (numberOfRuns != NUM_RUNS) {

				ParkingLot parkingLot = new ParkingLot(lotSize);
				Simulator simulatorCar = new Simulator(parkingLot, hourlyRate, 86400);

				simulatorCar.simulate();

				System.out.println("Simulation run " + (numberOfRuns + 1) "; Queue length at the end of the simulation run: " + carThing.getIncomingQueueSize());
				sum += carThing.getIncomingQueueSize();
				numberOfRuns += 1;
			}

			average = sum / numberOfRuns;
			numberOfRuns = 0;

			if (average <= THRESHOLD) {
				System.out.println("The lot is large enough to meet demand.");
				break;

			} else {
				lotSize = lotSize + 1;
				average = 0;
				sum = 0;
			}
		}
		return lotSize;
	}


	public static void main(String args[]) {
	
		StudentInfo.display();

		long mainStart = System.currentTimeMillis();

		if (args.length < 1) {
			System.out.println("Usage: java CapacityOptimizer <hourly rate of arrival>");
			System.out.println("Example: java CapacityOptimizer 11");
			return;
		}

		if (!args[0].matches("\\d+")) {
			System.out.println("The hourly rate of arrival should be a positive integer!");
			return;
		}

		int hourlyRate = Integer.parseInt(args[0]);

		int lotSize = getOptimalNumberOfSpots(hourlyRate);

		System.out.println();
		System.out.println("SIMULATION IS COMPLETE!");
		System.out.println("The smallest number of parking spots required: " + lotSize);

		long mainEnd = System.currentTimeMillis();

		System.out.println("Total execution time: " + ((mainEnd - mainStart) / 1000f) + " seconds");

	}
}