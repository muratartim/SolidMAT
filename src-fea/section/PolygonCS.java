package section;

/**
 * Class for polygon cross section.
 * 
 * @author Murat
 * 
 */
public class PolygonCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, alpha_;
	private int n_;

	/**
	 * Creates polygon cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            The side length of section.
	 * @param alpha
	 *            The side angle of section (in degrees).
	 * @param n
	 *            Number of sides.
	 */
	public PolygonCS(String name, double a, double alpha, Double n) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		alpha_ = alpha;
		float s = n.floatValue();
		n_ = Math.round(s);

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.polygon_;
	}

	@Override
	public double getArea(int i) {

		// convert angle to radians
		double alpha = Math.toRadians(alpha_);

		// compute
		return a_ * a_ * n_ / (4.0 * Math.tan(alpha));
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

		// convert angle to radians
		double alpha = Math.toRadians(alpha_);

		// compute ro1
		double ro1 = a_ / (2.0 * Math.sin(alpha));

		return getArea(i) / 24.0 * (6.0 * ro1 * ro1 - a_ * a_);
	}

	@Override
	public double getInertiaX3(int i) {

		// convert angle to radians
		double alpha = Math.toRadians(alpha_);

		// compute ro1
		double ro1 = a_ / (2.0 * Math.sin(alpha));

		return getArea(i) / 24.0 * (6.0 * ro1 * ro1 - a_ * a_);
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.pow(getArea(i), 4.0) / (40.0 * getInertiaX1(i));
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> m, 1 -> alpha, 2 -> n.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return alpha_;
		else
			return n_;
	}

	@Override
	public double[][] getOutline() {
		// TODO Computation codes...
		return null;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (a_ <= 0 || n_ <= 2)
			exceptionHandler(message);

		// convert angle to radians
		double alpha = Math.toRadians(alpha_);

		// check
		if (alpha <= 0.0 || alpha >= Math.PI / 2)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {

		// convert angle to radians
		double alpha = Math.toRadians(alpha_);

		// compute ro1, ro2
		double ro1 = a_ / (2.0 * Math.sin(alpha));
		double ro2 = a_ / (2.0 * Math.tan(alpha));

		// n is odd
		if (Math.IEEEremainder(n_, 2.0) != 0.0)
			return ro1 * Math.cos(alpha * (n_ + 1) * 0.5 - Math.PI * 0.5);

		// n/2 is odd
		else if (Math.IEEEremainder(n_ / 2, 2.0) != 0.0) {
			if (i == 0)
				return ro1;
			else
				return ro2;
		}

		// n/2 is even
		else {
			if (i == 0)
				return ro2;
			else
				return ro1;
		}
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}