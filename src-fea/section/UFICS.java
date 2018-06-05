package section;

/**
 * Class for thin walled unequal flanged i cross section.
 * 
 * @author Murat
 * 
 */
public class UFICS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double h_, b1_, b2_, t1_, t2_, tw_;

	/**
	 * Creates thin walled unequal flanged i cross sections.
	 * 
	 * @param name
	 *            The name of section.
	 * @param h
	 *            Height of section.
	 * @param b1
	 *            Width of upper flange.
	 * @param b2
	 *            Width of bottom flange.
	 * @param tw
	 *            Thickness of web.
	 * @param t1
	 *            Thickness of upper flange.
	 * @param t2
	 *            Thickness of bottom flange.
	 */
	public UFICS(String name, double h, double b1, double b2, double tw,
			double t1, double t2) {

		// set name
		name_ = name;

		// set geometrical properties
		h_ = h;
		b1_ = b1;
		b2_ = b2;
		t1_ = t1;
		t2_ = t2;
		tw_ = tw;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.uFI_;
	}

	@Override
	public double getArea(int i) {
		return tw_ * h_ + t1_ * b1_ + t2_ * b2_;
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
		return (b1_
				* t1_
				* (12.0 * Math.pow(getCentroid(1) - h_, 2.0) + t1_ * t1_)
				+ b2_
				* (12.0 * getCentroid(1) * getCentroid(1) * t2_ + t2_ * t2_
						* t2_) + 4.0
				* h_
				* tw_
				* (3.0 * getCentroid(1) * getCentroid(1) - 3.0 * getCentroid(1)
						* h_ + h_ * h_)) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return (b1_ * b1_ * b1_ * t1_ + b2_ * b2_ * b2_ * t2_ + h_ * tw_ * tw_
				* tw_) / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return 1.0 / 3.0 * (t1_ * t1_ * t1_ * b1_ + t2_ * t2_ * t2_ * b2_ + tw_
				* tw_ * tw_ * h_);
	}

	@Override
	public double getWarpingConstant(int i) {
		return h_ * h_ * t1_ * t2_ * b1_ * b1_ * b1_ * b2_ * b2_ * b2_
				/ (12.0 * (t1_ * b1_ * b1_ * b1_ + t2_ * b2_ * b2_ * b2_));
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> h, 1 -> b1, 2 -> b2, 3 -> tw, 4 ->
	 *            t1, 5 -> t2.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return h_;
		else if (i == 1)
			return b1_;
		else if (i == 2)
			return b2_;
		else if (i == 3)
			return tw_;
		else if (i == 4)
			return t1_;
		else
			return t2_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[12][2];
		outline[0][0] = -b2_ / 2.0;
		outline[0][1] = -(h_ + t2_) / 2.0;
		outline[1][0] = b2_ / 2.0;
		outline[1][1] = -(h_ + t2_) / 2.0;
		outline[2][0] = b2_ / 2.0;
		outline[2][1] = -(h_ + t2_) / 2.0 + t2_;
		outline[3][0] = tw_ / 2.0;
		outline[3][1] = -(h_ - t2_) / 2.0;
		outline[4][0] = tw_ / 2.0;
		outline[4][1] = (h_ - t1_) / 2.0;
		outline[5][0] = b1_ / 2.0;
		outline[5][1] = (h_ - t1_) / 2.0;
		outline[6][0] = b1_ / 2.0;
		outline[6][1] = (h_ + t1_) / 2.0;
		outline[7][0] = -b1_ / 2.0;
		outline[7][1] = (h_ + t1_) / 2.0;
		outline[8][0] = -b1_ / 2.0;
		outline[8][1] = (h_ - t1_) / 2.0;
		outline[9][0] = -tw_ / 2.0;
		outline[9][1] = (h_ - t1_) / 2.0;
		outline[10][0] = -tw_ / 2.0;
		outline[10][1] = -h_ / 2.0 + t2_ / 2.0;
		outline[11][0] = -b2_ / 2.0;
		outline[11][1] = -h_ / 2.0 + t2_ / 2.0;
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
		if (h_ <= 0 || b1_ <= 0 || b2_ <= 0 || t1_ <= 0 || t2_ <= 0 || tw_ <= 0)
			exceptionHandler(message);

		// check for other constraints
		if (tw_ >= b1_ || tw_ >= b2_ || t1_ + t2_ >= 2.0 * h_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return 0.0;
		else
			return (b1_ * t1_ * h_ + 0.5 * h_ * h_ * tw_)
					/ (b1_ * t1_ + b2_ * t2_ + h_ * tw_);
	}

	@Override
	public double getShearCenter() {
		return t1_ * b1_ * b1_ * b1_ * h_
				/ (t1_ * b1_ * b1_ * b1_ + t2_ * b2_ * b2_ * b2_);
	}
}