package section;

/**
 * Class for inward twin channel cross section.
 * 
 * @author Murat
 * 
 */
public class ITwinChannelCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double b_, h_, t_, b1_;

	/**
	 * Creates inward twin channel cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param h
	 *            The outside depth (height) of section.
	 * @param b
	 *            The flange width.
	 * @param b1
	 *            The height of end flange of section.
	 * @param t
	 *            The thickness of section.
	 */
	public ITwinChannelCS(String name, double h, double b, double b1, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		b_ = b;
		h_ = h;
		t_ = t;
		b1_ = b1;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.iTwinChannel_;
	}

	@Override
	public double getArea(int i) {
		return t_ * (2.0 * b_ + 4.0 * b1_);
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
		return t_
				/ 6.0
				* (b_ * b_ * b_ + 3.0 * b_ * h_ * h_ + 2.0
						* b1_
						* (3.0 * h_ * h_ + t_ * t_ + 3.0 * b1_
								* (b1_ - 2.0 * h_)));
	}

	@Override
	public double getInertiaX3(int i) {
		return t_ / 6.0
				* (b_ * b_ * b_ + 2.0 * b1_ * (3.0 * b_ * b_ + t_ * t_));
	}

	@Override
	public double getTorsionalConstant(int i) {
		return t_ * t_ * t_ / 3.0 * (2.0 * b_ + 4.0 * b1_);
	}

	@Override
	public double getWarpingConstant(int i) {
		return b_
				* b_
				* t_
				/ 24.0
				* (8.0 * b1_ * b1_ * b1_ + 6.0 * h_ * h_ * b1_ + h_ * h_ * b_ + 12.0
						* b1_ * b1_ * h_);
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> h, 1 -> b, 2 -> b1, 3 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return h_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return b1_;
		else
			return t_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[4][2];
		outline[0][0] = -b_ / 2.0;
		outline[0][1] = -h_ / 2.0;
		outline[1][0] = b_ / 2.0;
		outline[1][1] = -h_ / 2.0;
		outline[2][0] = b_ / 2.0;
		outline[2][1] = h_ / 2.0;
		outline[3][0] = -b_ / 2.0;
		outline[3][1] = h_ / 2.0;
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
		if (b_ <= 0 || h_ <= 0 || t_ <= 0 || b1_ <= 0)
			exceptionHandler(message);
		if (b1_ >= h_ / 2.0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		return 0.0;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}