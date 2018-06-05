package section;

/**
 * Class for thin walled hollow circle cross section.
 * 
 * @author Murat
 * 
 */
public class TWHCircleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double r_, t_;

	/**
	 * Creates thin walled hollow circle cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param r
	 *            The radius of section.
	 * @param t
	 *            The thickness of section.
	 */
	public TWHCircleCS(String name, double r, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		r_ = r;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.tWHCircle_;
	}

	@Override
	public double getArea(int i) {
		return 2.0 * Math.PI * r_ * t_;
	}

	@Override
	public double getShearAreaX2(int i) {
		return Math.PI * r_ * t_;
	}

	@Override
	public double getShearAreaX3(int i) {
		return Math.PI * r_ * t_;
	}

	@Override
	public double getInertiaX2(int i) {
		return Math.PI * r_ * r_ * r_ * t_;
	}

	@Override
	public double getInertiaX3(int i) {
		return Math.PI * r_ * r_ * r_ * t_;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return 2.0 * Math.PI * r_ * r_ * r_ * t_;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> r, 1 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return r_;
		else
			return t_;
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
		if (r_ <= 0 || t_ <= 0)
			exceptionHandler(message);

		// check for other constraints
		if (t_ >= r_)
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