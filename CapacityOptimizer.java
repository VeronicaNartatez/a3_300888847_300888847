public class CapacityOptimizer {
	private static final int NUM_RUNS = 10;

	private static final double THRESHOLD = 5.0d;

	public static int getOptimalNumberOfSpots(int hourlyRate) {
	
		int lotSize = 1;
		int sum = 0;

		while (true) {
			while (NUM_RUNS != 0) {

				ParkingLot parkingLot = new ParkingLot(lotSize);
				Simulator simulatorCar = new Simulator(parkingLot, hourlyRate, 86400);

				simulatorCar.simulate();

				LinkedQueue incomingQ = new LinkedQueue();
				sum += incomingQ.size();
			}

			Simulator average = new Simulator(parkingLot, hourlyRate, 86400);
			average = sum / NUM_RUNS;

			if (average <= THRESHOLD) {
				System.out.println("The lot is large enough to meet demand.");
				break;

			} else {
				lotSize += 1;
			}
		}
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