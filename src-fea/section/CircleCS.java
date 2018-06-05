package section;

/**
 * Class for circular cross section.
 * 
 * @author Murat
 * 
 */
public class CircleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double r_;

	/**
	 * Creates circular cross section.
	 * 
	 * @param r
	 *            The radius of section.
	 */
	public CircleCS(String name, double r) {

		// set name
		name_ = name;

		// set geometrical properties
		r_ = r;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.circle_;
	}

	@Override
	public double getArea(int i) {
		return Math.PI * Math.pow(r_, 2.0);
	}

	@Override
	public double getShearAreaX2(int i) {
		return 0.9 * getArea(0);
	}

	@Override
	public double getShearAreaX3(int i) {
		return 0.9 * getArea(0);
	}

	@Override
	public double getInertiaX2(int i) {
		return Math.PI * Math.pow(r_, 4.0) / 4.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return Math.PI * Math.pow(r_, 4.0) / 4.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.PI * Math.pow(r_, 4.0) / 2.0;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section (radius).
	 * 
	 * @param i
	 *            Not used here.
	 */
	public double getDimension(int i) {
		return r_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[64][2];
		for (int i = 0; i <= 63; i++) {
			outline[i][0] = r_ * Math.cos(Math.PI + i * Math.PI / 32.0);
			outline[i][1] = r_ * Math.sin(Math.PI + i * Math.PI / 32.0);
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
		if (r_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		return r_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}