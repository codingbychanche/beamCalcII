package org.berthold.beamCalc;

/**
 * Load.
 * 
 * Defines a single or a line load in [N] acting on a {@link Beam}
 *
 * Sign:
 * <p>
 * Vertical loads: Force acting downwards (-) Load acting upwards (+).
 * Horizontal force acts to the left (+), force acts to the right (-)
 * <p>
 * Angle in degrees:
 * <p>
 * Right side angles range from 0 to 180 deg, clockwise. Left side angles range
 * from 0 to -179 deg, counterclokwise.
 * <p>
 * Samples
 * <p>
 * 
 * +90 deg. load acting to left, horizontally<br>
 * +45 deg. load acting diagonally from upper right to bottom left<br>
 * +135 deg. same as +45 but acting from lower right to upper left
 * <p>
 * A negative load at an angle of 180 deg. is the same as a positive load acting
 * at 0 deg.
 * <p>
 *
 * @author Berthold
 * 
 *         ToDo: There is no constructor included yet, which allows to add a
 *         single load. If one want's to do so, the length of the line load and
 *         the ending force have to be passed as 0. The starting force is the
 *         magnitute of the single load: => Add constructor for single load. Add
 *         field singleForce_N.....
 * 
 *         The formula for calculating the center of gravity delivers exact
 *         results only for uniformingly distributed line loads, not for
 *         uniformingly changing line loads!
 */
public class Load implements Comparable<Load> {
	private String nameOfLoad;
	private double forceStart_N;
	private double forceEnd_N;
	private double lengthOfLineLoad_m;
	private double distanceFromLeftEndOfBeam_m;
	private double angleOfLoad_degrees;
	private boolean includeThisLoadIntoCaclulation;
	private boolean thisLoadHasAnError;

	/**
	 * This constructor allows to add uniformly distributed line loads by passing
	 * the load magnitute and the length of the load acting.
	 * 
	 * @param nameOfLoad
	 *            Any name allowed (F1, q1 etc..).
	 * @param force_N
	 *            The force acting (-) downwards. (+) upwards.
	 * @param distanceFromLeftEndOfBeam_m
	 *            Position of force acting.
	 * @param angleOfLoad_degrees
	 *            Angle.
	 * @param lengthOfLineLoad_m
	 *            Length, if line load.
	 * @param includeThisLoadIntoCaclulation
	 *            Use where aplicable....
	 * @param thisLoadHasAnError
	 *            Use where aplicable....
	 */
	public Load(String nameOfLoad, double forceStart_N, double distanceFromLeftEndOfBeam_m, double angleOfLoad_degrees,
			double lengthOfLineLoad_m, boolean includeThisLoadIntoCaclulation, boolean thisLoadHasAnError) {
		this.nameOfLoad = nameOfLoad;
		this.forceStart_N = forceStart_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees = angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
		this.includeThisLoadIntoCaclulation = includeThisLoadIntoCaclulation;
		this.thisLoadHasAnError = thisLoadHasAnError;
	}

	/**
	 * Optional constructor. Pass load without flags (include load, has
	 * error....).
	 * 
	 * @param nameOfLoad
	 * @param force_N
	 * @param distanceFromLeftEndOfBeam_m
	 * @param angleOfLoad_degrees
	 * @param lengthOfLineLoad_m
	 */
	public Load(String nameOfLoad, double force_N, double distanceFromLeftEndOfBeam_m, double angleOfLoad_degrees,
			double lengthOfLineLoad_m) {
		this.nameOfLoad = nameOfLoad;
		this.forceStart_N = force_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees = angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
	}

	/**
	 * This constructor allows to add uniformly changing, line loads.
	 * 
	 * @param nameOfLoad
	 * @param forceStart_N
	 * @param forceEnd_N
	 * @param distanceFromLeftEndOfBeam_m
	 * @param angleOfLoad_degrees
	 * @param lengthOfLineLoad_m
	 * @param includeThisLoadIntoCaclulation
	 * @param thisLoadHasAnError
	 */
	public Load(String nameOfLoad, double forceStart_N, double forceEnd_N, double distanceFromLeftEndOfBeam_m,
			double angleOfLoad_degrees, double lengthOfLineLoad_m, boolean includeThisLoadIntoCaclulation,
			boolean thisLoadHasAnError) {
		this.nameOfLoad = nameOfLoad;
		this.forceStart_N = forceStart_N;
		this.forceEnd_N = forceEnd_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees = angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
		this.includeThisLoadIntoCaclulation = includeThisLoadIntoCaclulation;
		this.thisLoadHasAnError = thisLoadHasAnError;
	}

	/**
	 * Optional constructor. Pass load without flags (include load, has
	 * error....).
	 * 
	 * @param nameOfLoad
	 * @param forceStart_N
	 * @param forceEnd_N
	 * @param distanceFromLeftEndOfBeam_m
	 * @param angleOfLoad_degrees
	 * @param lengthOfLineLoad_m
	 */
	public Load(String nameOfLoad, double forceStart_N, double forceEnd_N, double distanceFromLeftEndOfBeam_m,
			double angleOfLoad_degrees, double lengthOfLineLoad_m) {
		this.nameOfLoad = nameOfLoad;
		this.forceStart_N = forceStart_N;
		this.forceEnd_N = forceEnd_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees = angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
	}

	/**
	 * @return Center of gravity relative to the start of this line load. If the
	 *         lenght of line load and the maginitutes of the forces at the end/
	 *         start of the line load is 0, this load is handled like any single
	 *         load and it's position relative to the left end of the beam is
	 *         returned.
	 */
	public double getCenterOfGravity_m() {
		double cg;

		if (lengthOfLineLoad_m > 0)
			cg = lengthOfLineLoad_m
					- ((lengthOfLineLoad_m * (forceEnd_N + 2 * forceStart_N)) / (3 * (forceStart_N + forceEnd_N)));
		else
			cg = distanceFromLeftEndOfBeam_m;
		return cg;
	}

	/**
	 * @return Either the force of the single load or, if this is a uniform line
	 *         load or a uniformly changing line load, the resulting force
	 *         acting at the center of gravity of this loads plane.
	 */
	public double getForce_N() {
		double resultandForce_N;

		if (lengthOfLineLoad_m > 0) {
			resultandForce_N = (((Math.abs(forceEnd_N) - Math.abs(forceStart_N))
					* (lengthOfLineLoad_m * lengthOfLineLoad_m)) / (2 * lengthOfLineLoad_m))
					+ Math.abs(forceStart_N) * lengthOfLineLoad_m;
			if (forceStart_N < 0 || forceEnd_N<0)
				resultandForce_N = resultandForce_N * (-1);
		} else
			resultandForce_N = forceStart_N;
		return resultandForce_N;
	}

	public String getName() {
		return nameOfLoad;
	}

	public double getForceStart_N() {
		return forceStart_N;
	}

	public double getForceEnd_N() {
		return forceEnd_N;
	}

	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}

	public double getAngleOfLoad_degrees() {
		return angleOfLoad_degrees;
	}

	public double getLengthOfLineLoad_m() {
		return lengthOfLineLoad_m;
	}

	public boolean getIncludeThisLoadIntoCalculation() {
		return includeThisLoadIntoCaclulation;
	}

	public boolean getError() {
		return thisLoadHasAnError;
	}

	/*
	 * Setters
	 */

	public void changeNameTo(String nameOfLoad) {
		this.nameOfLoad = nameOfLoad;
	}

	public void setIncludeIntoCalculation(boolean include) {
		includeThisLoadIntoCaclulation = include;
	}

	public void setError() {
		thisLoadHasAnError = true;
	}

	public void hasNoError() {
		thisLoadHasAnError = false;
	}

	/**
	 * Loads can be sorted by distance from left end of beam in ascending order.
	 */
	@Override
	public int compareTo(Load compareDistanceFromLeftSupport_m) {
		return (int) this.distanceFromLeftEndOfBeam_m
				- (int) compareDistanceFromLeftSupport_m.distanceFromLeftEndOfBeam_m;
	}
}
