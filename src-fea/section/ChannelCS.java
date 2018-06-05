package section;

/**
 * Class for channel cross section.
 * 
 * @author Murat
 * 
 */
public class ChannelCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double b_, d_, t_, tw_, r_;

	/**
	 * Creates channel cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param b
	 *            The outside depth (height) of section.
	 * @param d
	 *            The flange width.
	 * @param tw
	 *            The flange thickness.
	 * @param t
	 *            The web thickness.
	 * @param r
	 *            The radius of flange corner.
	 */
	public ChannelCS(String name, double b, double d, double tw, double t,
			double r) {

		// set name
		name_ = name;

		// set geometrical properties
		b_ = b;
		d_ = d;
		t_ = t;
		tw_ = tw;
		r_ = r;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.channel_;
	}

	@Override
	public double getArea(int i) {
		return t_ * b_ + 2.0 * tw_ * d_;
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
		return (d_ + t_) * b_ * b_ * b_ / 12.0 - d_
				* Math.pow(b_ - 2.0 * tw_, 3.0) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return b_
				/ 3.0
				* Math.pow(d_ + t_, 3.0)
				- d_
				* d_
				* d_
				/ 3.0
				* (b_ - 2.0 * tw_)
				- getArea(i)
				* Math.pow(d_ + t_
						- (b_ * t_ * t_ + 2.0 * tw_ * d_ * (2.0 * t_ + d_))
						/ (2.0 * (t_ * b_ + 2.0 * tw_ * d_)), 2.0);
	}

	@Override
	public double getTorsionalConstant(int i) {
		return t_
				* t_
				* t_
				* (0.333 * b_ - 0.42 * t_ + 0.56 * Math.pow(t_, 5.0)
						/ Math.pow(b_, 4.0))
				+ d_
				* Math.pow(tw_, 3.0)
				* (2.0 / 3.0 - 0.21
						* tw_
						/ d_
						* (1.0 - Math.pow(tw_, 4.0)
								/ (192.0 * Math.pow(d_, 4.0))))
				+ 2.0
				* tw_
				/ t_
				* (0.07 + 0.076 * r_ / t_)
				* Math.pow(2.0 * (tw_ + t_ + 3.0 * r_ - Math.sqrt(2.0
						* (2.0 * r_ + t_) * (2.0 * r_ + tw_))), 4.0);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> b, 1 -> d, 2 -> tw, 3 -> t, 4 -> r.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return b_;
		else if (i == 1)
			return d_;
		else if (i == 2)
			return tw_;
		else if (i == 3)
			return t_;
		else
			return r_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[8][2];
		outline[0][0] = -t_ / 2.0;
		outline[0][1] = -b_ / 2.0;
		outline[1][0] = (d_ + t_) - t_ / 2.0;
		outline[1][1] = -b_ / 2.0;
		outline[2][0] = (d_ + t_) - t_ / 2.0;
		outline[2][1] = -b_ / 2.0 + tw_;
		outline[3][0] = t_ / 2.0;
		outline[3][1] = -b_ / 2.0 + tw_;
		outline[4][0] = t_ / 2.0;
		outline[4][1] = b_ / 2.0 - tw_;
		outline[5][0] = (d_ + t_) - t_ / 2.0;
		outline[5][1] = b_ / 2.0 - tw_;
		outline[6][0] = (d_ + t_) - t_ / 2.0;
		outline[6][1] = b_ / 2.0;
		outline[7][0] = -t_ / 2.0;
		outline[7][1] = b_ / 2.0;
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
		if (b_ <= 0 || d_ <= 0 || t_ <= 0 || tw_ <= 0 || r_ < 0)
			exceptionHandler(message);

		// check for other constraints
		if (tw_ >= b_ / 2.0)
			exceptionHandler(message);
		if (t_ >= 2.0 * (tw_ + r_))
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return (b_ * t_ * t_ + 2.0 * tw_ * d_ * (2.0 * t_ + d_))
					/ (2.0 * (t_ * b_ + 2.0 * tw_ * d_));
		else
			return b_ / 2.0;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}