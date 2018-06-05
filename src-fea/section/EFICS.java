package section;

/**
 * Class for thin walled equal flanged i cross section.
 * 
 * @author Murat
 * 
 */
public class EFICS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double b_, t_, h_, tw_;

	/**
	 * Creates thin walled equal flanged i cross sections.
	 * 
	 * @param name
	 *            The name of section.
	 * @param h
	 *            Height of section.
	 * @param b
	 *            Width of flange.
	 * @param tw
	 *            Thickness of web.
	 * @param t
	 *            Thickness of flange.
	 */
	public EFICS(String name, double h, double b, double tw, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		h_ = h;
		b_ = b;
		tw_ = tw;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.eFI_;
	}

	@Override
	public double getArea(int i) {
		return 2.0 * b_ * t_ + tw_ * h_;
	}

	@Override
	public double getShearAreaX2(int i) {
		return 5.0 / 3.0 * t_ * b_;
	}

	@Override
	public double getShearAreaX3(int i) {
		return tw_ * h_;
	}

	@Override
	public double getInertiaX2(int i) {
		return (2.0 * b_ * t_ * (3.0 * h_ * h_ + t_ * t_) + h_ * h_ * h_ * tw_) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return (2.0 * b_ * b_ * b_ * t_ + h_ * tw_ * tw_ * tw_) / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return 1.0 / 3.0 * (2.0 * t_ * t_ * t_ * b_ + tw_ * tw_ * tw_ * h_);
	}

	@Override
	public double getWarpingConstant(int i) {
		return h_ * h_ * t_ * b_ * b_ * b_ / 24.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> h, 1 -> b, 2 -> tw, 3 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return h_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return tw_;
		else
			return t_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[12][2];
		outline[0][0] = -b_ / 2.0;
		outline[0][1] = -(h_ + t_) / 2.0;
		outline[1][0] = b_ / 2.0;
		outline[1][1] = -(h_ + t_) / 2.0;
		outline[2][0] = b_ / 2.0;
		outline[2][1] = -(h_ + t_) / 2.0 + t_;
		outline[3][0] = tw_ / 2.0;
		outline[3][1] = -(h_ - t_) / 2.0;
		outline[4][0] = tw_ / 2.0;
		outline[4][1] = (h_ - t_) / 2.0;
		outline[5][0] = b_ / 2.0;
		outline[5][1] = (h_ - t_) / 2.0;
		outline[6][0] = b_ / 2.0;
		outline[6][1] = (h_ + t_) / 2.0;
		outline[7][0] = -b_ / 2.0;
		outline[7][1] = (h_ + t_) / 2.0;
		outline[8][0] = -b_ / 2.0;
		outline[8][1] = (h_ - t_) / 2.0;
		outline[9][0] = -tw_ / 2.0;
		outline[9][1] = (h_ - t_) / 2.0;
		outline[10][0] = -tw_ / 2.0;
		outline[10][1] = -h_ / 2.0 + t_ / 2.0;
		outline[11][0] = -b_ / 2.0;
		outline[11][1] = -h_ / 2.0 + t_ / 2.0;
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
		if (b_ <= 0 || t_ <= 0 || h_ <= 0 || tw_ <= 0)
			exceptionHandler(message);

		// check for other constraints
		if (tw_ >= b_ || t_ >= h_)
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