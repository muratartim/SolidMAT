package section;

/**
 * Class for hollow circle cross section.
 * 
 * @author Murat
 * 
 */
public class HCircleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double ro_, ri_;

	/**
	 * Creates hollow circle cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param ro
	 *            The outside radius of section.
	 * @param ri
	 *            The inside radius of section.
	 */
	public HCircleCS(String name, double ro, double ri) {

		// set name
		name_ = name;

		// set geometrical properties
		ro_ = ro;
		ri_ = ri;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.hCircle_;
	}

	@Override
	public double getArea(int i) {
		return Math.PI * (ro_ * ro_ - ri_ * ri_);
	}

	@Override
	public double getShearAreaX2(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getShearAreaX3(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getInertiaX2(int i) {
		return Math.PI / 4.0 * (Math.pow(ro_, 4.0) - Math.pow(ri_, 4.0));
	}

	@Override
	public double getInertiaX3(int i) {
		return Math.PI / 4.0 * (Math.pow(ro_, 4.0) - Math.pow(ri_, 4.0));
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.PI / 2.0 * (Math.pow(ro_, 4.0) - Math.pow(ri_, 4.0));
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> ro, 1 -> ri.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return ro_;
		else
			return ri_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[64][2];
		for (int i = 0; i <= 63; i++) {
			outline[i][0] = ro_ * Math.cos(Math.PI + i * Math.PI / 32.0);
			outline[i][1] = ro_ * Math.sin(Math.PI + i * Math.PI / 32.0);
		}
		return outline;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (ro_ <= 0)
			exceptionHandler(message);
		if (ri_ <= 0)
			exceptionHandler(message);

		// check for other constraints
		if (ri_ >= ro_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		return ro_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
