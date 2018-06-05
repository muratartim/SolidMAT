package section;

/**
 * Class for thickness section.
 * 
 * @author Murat
 * 
 */
public class Thickness extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double t_;

	/**
	 * Creates thickness section for two dimensional elements.
	 * 
	 * @param name
	 *            The name of section.
	 * @param t
	 *            The thickness of section.
	 */
	public Thickness(String name, double t) {

		// set values
		name_ = name;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.thickness_;
	}

	@Override
	public double getArea(int i) {
		return 0;
	}

	@Override
	public double getShearAreaX2(int i) {
		return 0;
	}

	@Override
	public double getShearAreaX3(int i) {
		return 0;
	}

	@Override
	public double getInertiaX2(int i) {
		return 0;
	}

	@Override
	public double getInertiaX3(int i) {
		return 0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return 0;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0;
	}

	/**
	 * Returns the demanded dimension of the section.
	 * 
	 * @param i
	 *            Not used here.
	 */
	public double getDimension(int i) {
		return t_;
	}

	@Override
	public double[][] getOutline() {
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
		if (t_ <= 0)
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
