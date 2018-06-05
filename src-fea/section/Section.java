package section;

import java.io.Serializable;

/**
 * Class for cross sections.
 * 
 * @author Murat Artim
 * 
 */
public abstract class Section implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of section. */
	public static final int circle_ = 0, ellipse_ = 1, square_ = 2,
			rectangular_ = 3, eTriangle_ = 4, iTriangle_ = 5, cSegment_ = 6,
			cSector_ = 7, trapezoid_ = 8, polygon_ = 9, hCircle_ = 10,
			hEllipse_ = 11, tee_ = 12, channel_ = 13, i_ = 14, l_ = 15,
			z_ = 16, tWHEllipse_ = 17, tWHCircle_ = 18, tWHRectangle_ = 19,
			tWChannel_ = 20, c_ = 21, hat_ = 22, iTwinChannel_ = 23,
			oTwinChannel_ = 24, eFI_ = 25, uFI_ = 26, tWZ_ = 27,
			hCSector_ = 28, userDefined_ = 29, variable_ = 30, thickness_ = 31,
			varThickness_ = 32;

	/** The name of section. */
	protected String name_;

	/**
	 * Returns the name of section.
	 * 
	 * @return The name of section.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the type of section.
	 * 
	 * @return The type of section.
	 */
	public abstract int getType();

	/**
	 * Returns the cross section area.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Cross section area.
	 */
	public abstract double getArea(int i);

	/**
	 * Returns the shear area in x2 direction.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Shear area in x2 direction.
	 */
	public abstract double getShearAreaX2(int i);

	/**
	 * Returns the shear area in x3 direction.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Shear area in x3 direction.
	 */
	public abstract double getShearAreaX3(int i);

	/**
	 * Returns the polar moment of inertia about x1 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Polar moment of inertia about x1 axis.
	 */
	public double getInertiaX1(int i) {
		return getInertiaX2(0) + getInertiaX3(0);
	}

	/**
	 * Returns the moment of inertia about x2 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Moment of inertia about x2 axis.
	 */
	public abstract double getInertiaX2(int i);

	/**
	 * Returns the moment of inertia about x3 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Moment of inertia about x3 axis.
	 */
	public abstract double getInertiaX3(int i);

	/**
	 * Returns the polar radius of gyration about x1 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Polar radius of gyration about x1 axis.
	 */
	public double getGyrationX1(int i) {
		double r2 = getGyrationX2(i);
		double r3 = getGyrationX3(i);
		return Math.sqrt(r2 * r2 + r3 * r3);
	}

	/**
	 * Returns the radius of gyration about x2 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Radius of gyration about x2 axis.
	 */
	public double getGyrationX2(int i) {
		double i2 = getInertiaX2(i);
		double a = getArea(i);
		return Math.sqrt(i2 / a);
	}

	/**
	 * Returns the radius of gyration about x3 axis.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Radius of gyration about x3 axis.
	 */
	public double getGyrationX3(int i) {
		double i3 = getInertiaX3(i);
		double a = getArea(i);
		return Math.sqrt(i3 / a);
	}

	/**
	 * Returns the torsional constant.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Torsional constant.
	 */
	public abstract double getTorsionalConstant(int i);

	/**
	 * Returns the warping constant.
	 * 
	 * @param i
	 *            Index of end section (for variable type).
	 * @return Warping constant.
	 */
	public abstract double getWarpingConstant(int i);

	/**
	 * Returns the position of the centroid.
	 * 
	 * @param i
	 *            Index for the demanded coordinate of the centroid (x2 -> 0, x3
	 *            -> 1).
	 * @return The position of the centroid.
	 */
	public abstract double getCentroid(int i);

	/**
	 * Returns the position of the shear center.
	 * 
	 * @return The position of the shear center.
	 */
	public abstract double getShearCenter();

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            Index of dimension.
	 * @return The demanded dimension of section.
	 */
	public abstract double getDimension(int i);

	/**
	 * Returns the outline.
	 * 
	 * @return The outline of section.
	 */
	public abstract double[][] getOutline();

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}