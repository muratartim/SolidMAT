package section;

/**
 * Class for equilateral triangle cross section.
 * 
 * @author Murat
 * 
 */
public class ETriangleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_;

	/**
	 * Creates equilateral triangle cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            The side length of section.
	 */
	public ETriangleCS(String name, double a) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.eTriangle_;
	}

	@Override
	public double getArea(int i) {
		return 0.4330 * a_ * a_;
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
		return 0.01804 * Math.pow(a_, 4.0);
	}

	@Override
	public double getInertiaX3(int i) {
		return 0.01804 * Math.pow(a_, 4.0);
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.pow(a_, 4.0) * Math.sqrt(3.0) / 80.0;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            Not used here.
	 */
	public double getDimension(int i) {
		return a_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[3][2];
		outline[0][0] = -a_ / 2.0;
		outline[0][1] = -a_ * Math.sqrt(3.0) / 6.0;
		outline[1][0] = a_ / 2.0;
		outline[1][1] = -a_ * Math.sqrt(3.0) / 6.0;
		outline[2][0] = 0.0;
		outline[2][1] = a_ * Math.sqrt(3.0) / 3.0;
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
		if (a_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return 0.5 * a_;
		else
			return 0.5774 * a_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}