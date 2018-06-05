package section;

/**
 * Class for user defined cross section.
 * 
 * @author Murat
 * 
 */
public class UserCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The properties of section. */
	private double a_, a2_, a3_, i2_, i3_, j_, r_;

	/**
	 * Creates user defined cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            Cross section area.
	 */
	public UserCS(String name, double a) {

		// set name
		name_ = name;

		// set property
		a_ = a;

		// check value
		checkProperties(a_);
	}

	/**
	 * Sets shear area in x2 direction to section.
	 * 
	 * @param value
	 *            Shear area in x2 direction.
	 */
	public void setShearX2(double value) {

		// set property
		a2_ = value;

		// check value
		checkProperties(a2_);
	}

	/**
	 * Sets shear area in x3 direction to section.
	 * 
	 * @param value
	 *            Shear area in x3 direction.
	 */
	public void setShearX3(double value) {

		// set property
		a3_ = value;

		// check value
		checkProperties(a3_);
	}

	/**
	 * Sets moment of inertia about x2 axis to section.
	 * 
	 * @param value
	 *            Moment of inertia about x2 axis.
	 */
	public void setInertiaX2(double value) {

		// set property
		i2_ = value;

		// check value
		checkProperties(i2_);
	}

	/**
	 * Sets moment of inertia about x3 axis to section.
	 * 
	 * @param value
	 *            Moment of inertia about x3 axis.
	 */
	public void setInertiaX3(double value) {

		// set property
		i3_ = value;

		// check value
		checkProperties(i3_);
	}

	/**
	 * Sets torsional constant to section.
	 * 
	 * @param value
	 *            Torsional constant.
	 */
	public void setTorsionalConstant(double value) {

		// set property
		j_ = value;

		// check value
		checkProperties(j_);
	}

	/**
	 * Sets warping constant to section.
	 * 
	 * @param value
	 *            Warping constant.
	 */
	public void setWarpingConstant(double value) {

		// set property
		r_ = value;

		// check value
		checkProperties(r_);
	}

	@Override
	public int getType() {
		return Section.userDefined_;
	}

	@Override
	public double getArea(int i) {
		return a_;
	}

	@Override
	public double getShearAreaX2(int i) {
		return a2_;
	}

	@Override
	public double getShearAreaX3(int i) {
		return a3_;
	}

	@Override
	public double getInertiaX2(int i) {
		return i2_;
	}

	@Override
	public double getInertiaX3(int i) {
		return i3_;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return j_;
	}

	@Override
	public double getWarpingConstant(int i) {
		return r_;
	}

	@Override
	public double getDimension(int i) {
		return 0;
	}

	@Override
	public double[][] getOutline() {
		double radius = Math.sqrt(a_ / Math.PI);
		double[][] outline = new double[64][2];
		for (int i = 0; i <= 63; i++) {
			outline[i][0] = radius * Math.cos(Math.PI + i * Math.PI / 32.0);
			outline[i][1] = radius * Math.sin(Math.PI + i * Math.PI / 32.0);
		}
		return outline;
	}

	/**
	 * Checks for illegal assignments to properties of section, if not throws
	 * exception.
	 */
	private void checkProperties(double value) {

		// message to be displayed
		String message = "Illegal value for section!";

		// check if positive
		if (value <= 0)
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