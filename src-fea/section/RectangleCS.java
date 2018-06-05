package section;

/**
 * Class for rectangular cross section.
 * 
 * @author Murat
 * 
 */
public class RectangleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double d_, b_;

	/**
	 * Creates rectangle cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param d
	 *            The depth (height) of section.
	 * @param b
	 *            The width of section.
	 */
	public RectangleCS(String name, double d, double b) {

		// set name
		name_ = name;

		// set geometrical properties
		d_ = d;
		b_ = b;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.rectangular_;
	}

	@Override
	public double getArea(int i) {
		return d_ * b_;
	}

	@Override
	public double getShearAreaX2(int i) {
		return getArea(0) * 5.0 / 6.0;
	}

	@Override
	public double getShearAreaX3(int i) {
		return getArea(0) * 5.0 / 6.0;
	}

	@Override
	public double getInertiaX2(int i) {
		return b_ * Math.pow(d_, 3.0) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return d_ * Math.pow(b_, 3.0) / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {

		// get dimensions
		double a = Math.max(d_, b_);
		double b = Math.min(d_, b_);

		// compute and return torsional constant
		double j = a
				* Math.pow(b, 3.0)
				/ 3.0
				* (1.0 - 0.63 * b / a + 0.052 * Math.pow(b, 5.0)
						/ Math.pow(a, 5.0));
		return j;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> d, 1 -> b.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return d_;
		else
			return b_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[4][2];
		outline[0][0] = -b_ / 2.0;
		outline[0][1] = -d_ / 2.0;
		outline[1][0] = b_ / 2.0;
		outline[1][1] = -d_ / 2.0;
		outline[2][0] = b_ / 2.0;
		outline[2][1] = d_ / 2.0;
		outline[3][0] = -b_ / 2.0;
		outline[3][1] = d_ / 2.0;
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
		if (d_ <= 0)
			exceptionHandler(message);
		if (b_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_ * 0.5;
		else
			return d_ * 0.5;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
