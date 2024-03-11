/**
 * @author Mehrdad Sabetzadeh, University of Ottawa
 *
 */
public class Simulator {

	/**
	 * Length of car plate numbers
	 */
	public static final int PLATE_NUM_LENGTH = 3;

	/**
	 * Number of seconds in one hour
	 */
	public static final int NUM_SECONDS_IN_1H = 3600;

	/**
	 * Maximum duration a car can be parked in the lot
	 */
	public static final int MAX_PARKING_DURATION = 8 * NUM_SECONDS_IN_1H;

	/**
	 * Total duration of the simulation in (simulated) seconds
	 */
	public static final int SIMULATION_DURATION = 24 * NUM_SECONDS_IN_1H;

	/**
	 * The probability distribution for a car leaving the lot based on the duration
	 * that the car has been parked in the lot
	 */
	public static final TriangularDistribution departurePDF = new TriangularDistribution(0, MAX_PARKING_DURATION / 2,
			MAX_PARKING_DURATION);

	/**
	 * The probability that a car would arrive at any given (simulated) second
	 */
	private Rational probabilityOfArrivalPerSec;

	/**
	 * The simulation clock. Initially the clock should be set to zero; the clock
	 * should then be incremented by one unit after each (simulated) second
	 */
	private int clock;

	/**
	 * Total number of steps (simulated seconds) that the simulation should run for.
	 * This value is fixed at the start of the simulation. The simulation loop
	 * should be executed for as long as clock < steps. When clock == steps, the
	 * simulation is finished.
	 */
	private int steps;

	/**
	 * Instance of the parking lot being simulated.
	 */
	private ParkingLot lot;

	/**
	 * Queue for the cars wanting to enter the parking lot
	 */
	private Queue<Spot> incomingQueue;

	/**
	 * Queue for the cars wanting to leave the parking lot
	 */
	private Queue<Spot> outgoingQueue;

	/**
	 * @param lot   is the parking lot to be simulated
	 * @param steps is the total number of steps for simulation
	 */
	public Simulator(ParkingLot lot, int perHourArrivalRate, int steps) {
	
		this.steps = steps;
		this.lot = lot;

		this.probabilityOfArrivalPerSec = new Rational(perHourArrivalRate, 3600);

		incomingQ = new LinkedQueue();
		outgoingQ = new LinkedQueue();
	}


	/**
	 * Simulate the parking lot for the number of steps specified by the steps
	 * instance variable
	 * NOTE: Make sure your implementation of simulate() uses peek() from the Queue interface.
	 */
	public void simulate() {
	
		this.clock = 0;

		while (clock < steps) {

			if (RandomGenerator.eventOccurred(probabilityOfArrivalPerSec)) {
				Spot parkedCar = new Spot(RandomGenerator.generateRandomCar(), clock);
				incomingQ.enqueue(parkedCar);
			}

			for (int i = 0; i < lot.getNumRows(); i++) {
				for (int j = 0; j < lot.getNumSpotsPerRow(); j++) {

					Spot parked = lot.getSpotAt(i,j);

					if (parked != null) {

						int duration = clock - parked.getTimestamp();

						if (duration == MAX_PARKING_DURATION) {
							lot.remove(i,j);
							outgoingQ.enqueue(parked);

						} else if (duration < MAX_PARKING_DURATION) {
							if (RandomGenerator.eventOccurred(departurePDF.pdf(j))) {
								lot.remove(i,j);
								outgoingQ.enqueue(parked);
							}
						}
					}
				}
			}

			if (incomingQ.peek() != null) {

				Car newCar = incomingQ.dequeue().getCar();
				lot.attemptParking(newCar,clock);
				System.out.println(newCar + "ENTERED at timestep" + clock + " ;occupancy is at " + lot.getTotalOccupancy());
			}

			if (outgoingQ.peek() != null) {
				Car newCar = outgoingQ.dequeue.getCar();
				System.out.println(newCar + " EXITED at timestep " + clock + "; occupancy is at " + lot.getTotalOccupancy());
			}
			clock++;
		}
	}

	public int getIncomingQueueSize() {
		return incomingQ.size();
	}
}