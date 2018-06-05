package section;

/**
 * Class for trapezoid cross section.
 * 
 * @author Murat
 * 
 */
public class TrapezoidCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double m_, n_, b_;

	/**
	 * Creates trapezoid cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param b
	 *            The height of section.
	 * @param m
	 *            The left side length of section.
	 * @param n
	 *            The right side length of section.
	 */
	public TrapezoidCS(String name, double b, double m, double n) {

		// set name
		name_ = name;

		// set geometrical properties
		m_ = m;
		n_ = n;
		b_ = b;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.trapezoid_;
	}

	@Override
	public double getArea(int i) {
		return b_ * 0.5 * (m_ + n_);
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
		return b_
				/ (32.0 * (m_ + n_))
				* (Math.pow(m_, 4.0) + Math.pow(n_, 4.0) + 2.0 * m_ * n_
						* (m_ * m_ + n_ * n_));
	}

	@Override
	public double getInertiaX3(int i) {
		return b_ * b_ * b_ * (m_ * m_ + 4.0 * m_ * n_ + n_ * n_)
				/ (36.0 * (m_ + n_));
	}

	@Override
	public double getTorsionalConstant(int i) {

		// get dimensions
		double m = Math.max(m_, n_);
		double n = Math.min(m_, n_);

		// compute and return torsional constant
		double s = (m - n) / b_;
		double vl = 0.10504 - 0.10 * s + 0.0848 * s * s - 0.06746 * s * s * s
				+ 0.0515 * s * s * s * s;
		double vs = 0.10504 + 0.10 * s + 0.0848 * s * s + 0.06746 * s * s * s
				+ 0.0515 * s * s * s * s;
		return b_ / 12.0 * (m + n) * (m * m + n * n) - vl * m * m * m * m - vs
				* n * n * n * n;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> b, 1 -> m, 2 -> n.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return b_;
		else if (i == 1)
			return m_;
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
		if (m_ <= 0 || n_ <= 0 || b_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_ * (2.0 * m_ + n_) / (3.0 * (m_ + n_));
		else
			return (2.0 * m_ * m_ + 2.0 * m_ * n_ - n_ * n_)
					/ (3.0 * (m_ + n_));
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}