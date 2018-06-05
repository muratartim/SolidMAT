package boundary;

import java.io.Serializable;

import matrix.DMat;
import matrix.DVec;

/**
 * Class for element mechanical loads.
 * 
 * @author Murat
 * 
 */
public class ElementMechLoad implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the component of mechanical load. */
	public static final int fx_ = 0, fy_ = 1, fz_ = 2, mx_ = 3, my_ = 4,
			mz_ = 5;

	/** Static variable for the type of load. */
	public static final int line_ = 0, area_ = 1, volume_ = 2;

	/** Static variable for the geometry of 2D loading function. */
	public final static int quadrangular_ = 0, triangular_ = 1;

	/** Static variable for the geometry of 3D loading function. */
	public final static int hexahedral_ = 0, tetrahedral_ = 1;

	/** Static variable for the coordinate system of load. */
	public static final int global_ = 0, local_ = 1;

	/** Basic properties of element mechanical load. */
	private int component_, type_;

	/** Coordinate system of load. */
	private int coordinateSystem_ = ElementMechLoad.global_;

	/** Values of mechanical load. */
	private double[] loadingValues_;

	/** The value for scaling loading values. */
	private double scale_ = 1.0;

	/** The name of element mechanical load. */
	private String name_;

	/** The boundary case of element mechanical load. */
	private BoundaryCase boundaryCase_;

	/** Boolean variable indicating if load is self weight or not. */
	private boolean isSelfWeight_ = false;

	/**
	 * Creates element mechanical load.
	 * 
	 * @param name
	 *            The name of element mechanical load.
	 * @param bCase
	 *            The boundary case of element mechanical load.
	 * @param type
	 *            The type of mechanical load.
	 * @param component
	 *            The component of mechanical load.
	 * @param values
	 *            The values vector of mechanical load. For line load, it has
	 *            two components denoting the loading values at I-th and J-th
	 *            ends. For area load, it has three components denoting the
	 *            loading values at three successive corners starting from lower
	 *            left corner and in counter-clockwise order. For volume load,
	 *            it has one component denoting the uniform distribution of load
	 *            over the element.
	 */
	public ElementMechLoad(String name, BoundaryCase bCase, int type,
			int component, DVec values) {

		// set name
		name_ = name;

		// set boundary case
		boundaryCase_ = bCase;

		// set type
		if (type < 0 || type > 2)
			exceptionHandler("Illegal type for element mechanical load!");
		else
			type_ = type;

		// set component
		if (component < 0 || component > 5)
			exceptionHandler("Illegal component for element mechanical load!");
		else
			component_ = component;

		// set values
		if (checkValues(values))
			loadingValues_ = values.get1DArray();
	}

	/**
	 * Sets load as self weight depending on the given parameter.
	 * 
	 * @param isSelfWeight
	 *            True if self weight, False vice versa.
	 */
	public void setAsSelfWeight(boolean isSelfWeight) {
		isSelfWeight_ = isSelfWeight;
	}

	/**
	 * Sets boundary case to element mechanical load.
	 * 
	 * @param bCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase bCase) {
		boundaryCase_ = bCase;
	}

	/**
	 * Sets loading values to element mechanical load.
	 * 
	 * @param values
	 *            Loading values to be set.
	 */
	public void setLoadingValues(DVec values) {
		if (checkValues(values))
			loadingValues_ = values.get1DArray();
	}

	/**
	 * Sets scalinf factor for loading values.
	 * 
	 * @param scale
	 *            The scaling factor for loading values.
	 */
	public void setLoadingScale(double scale) {
		scale_ = scale;
	}

	/**
	 * Sets the coordinate system of element mechanical load.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system to be set.
	 */
	public void setCoordinateSystem(int coordinateSystem) {
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for element mechanical load!");
		else
			coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Returns the name of element mechanical load.
	 * 
	 * @return The name of element mechanical load.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the boundary case of element mechanical load.
	 * 
	 * @return The boundary case of element mechanical load.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns the type of element mechanical load.
	 * 
	 * @return The type of element mechanical load.
	 */
	public int getType() {
		return type_;
	}

	/**
	 * Returns coordinate system of element mechanical load.
	 * 
	 * @return The coordinate system of element mechanical load.
	 */
	public int getCoordinateSystem() {
		return coordinateSystem_;
	}

	/**
	 * Returns the component of element mechanical load.
	 * 
	 * @return The component of element mechanical load.
	 */
	public int getComponent() {
		return component_;
	}

	/**
	 * Returns the degree of loading function.
	 * 
	 * @return The degree of loading function.
	 */
	public int getDegree() {

		// for line load
		if (type_ == ElementMechLoad.line_) {

			// get values of load
			double a = loadingValues_[0];
			double b = loadingValues_[1];

			// uniform
			if (a == b)
				return 0;

			// linear
			else
				return 1;
		}

		// for area load
		else if (type_ == ElementMechLoad.area_) {

			// get values of load
			double a = loadingValues_[0];
			double b = loadingValues_[1];
			double c = loadingValues_[2];

			// uniform
			if (a == b && a == c)
				return 0;

			// bilinear
			else
				return 1;
		}

		// for volume load
		else if (type_ == ElementMechLoad.volume_) {
			return 0;
		}
		return 0;
	}

	/**
	 * Returns the function value of element mechanical load.
	 * 
	 * @param geometry
	 *            The geometry of 2D/3D element that this load will be applied.
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The value of element mechanical load.
	 */
	public double getFunction(int geometry, double eps1, double eps2,
			double eps3) {

		// for line load
		if (type_ == ElementMechLoad.line_) {

			// compute loading function values
			DVec loadingValues = new DVec(loadingValues_).scale(scale_);
			DMat FuncValues = new DMat(2, 2);
			FuncValues.set(0, 0, -1.0);
			FuncValues.set(0, 1, 1.0);
			FuncValues.set(1, 0, 1.0);
			FuncValues.set(1, 1, 1.0);
			loadingValues = FuncValues.invert().multiply(loadingValues);

			// return function value at demanded coordinate
			return loadingValues.get(0) * eps1 + loadingValues.get(1);
		}

		// for area load
		else if (type_ == ElementMechLoad.area_) {

			// for quadrangular geometry
			if (geometry == ElementMechLoad.quadrangular_) {

				// compute loading function values
				DVec loadingValues = new DVec(loadingValues_).scale(scale_);
				DMat FuncValues = new DMat(3, 3);
				FuncValues.set(0, 0, -1.0);
				FuncValues.set(0, 1, -1.0);
				FuncValues.set(0, 2, 1.0);
				FuncValues.set(1, 0, 1.0);
				FuncValues.set(1, 1, -1.0);
				FuncValues.set(1, 2, 1.0);
				FuncValues.set(2, 0, 1.0);
				FuncValues.set(2, 1, 1.0);
				FuncValues.set(2, 2, 1.0);
				loadingValues = FuncValues.invert().multiply(loadingValues);

				// return function value at demanded coordinate
				return loadingValues.get(0) * eps1 + loadingValues.get(1)
						* eps2 + loadingValues.get(2);
			}

			// triangular geometry
			else {

				// compute loading function values
				DVec loadingValues = new DVec(loadingValues_).scale(scale_);
				DMat FuncValues = new DMat(3, 3);
				FuncValues.set(0, 0, 0.0);
				FuncValues.set(0, 1, 0.0);
				FuncValues.set(0, 2, 1.0);
				FuncValues.set(1, 0, 1.0);
				FuncValues.set(1, 1, 0.0);
				FuncValues.set(1, 2, 1.0);
				FuncValues.set(2, 0, 0.0);
				FuncValues.set(2, 1, 1.0);
				FuncValues.set(2, 2, 1.0);
				loadingValues = FuncValues.invert().multiply(loadingValues);

				// return function value at demanded coordinate
				return loadingValues.get(0) * eps1 + loadingValues.get(1)
						* eps2 + loadingValues.get(2);
			}
		}

		// for volume load
		else if (type_ == ElementMechLoad.volume_) {

			// compute loading function values
			DVec loadingValues = new DVec(loadingValues_).scale(scale_);

			// return value of uniformly distributed load
			return loadingValues.get(0);
		}
		return 0.0;
	}

	/**
	 * Returns the loading values of element mechanical load.
	 * 
	 * @return The loading values of element mechanical load.
	 */
	public DVec getLoadingValues() {
		return new DVec(loadingValues_);
	}

	/**
	 * Returns True if load is self weight.
	 * 
	 * @return True if load is self weight.
	 */
	public boolean isSelfWeight() {
		return isSelfWeight_;
	}

	/**
	 * Checks number of parameters given.
	 * 
	 * @param values
	 *            The parameter vector of element mechanical load.
	 * @return True if number of parameters is legal.
	 */
	private boolean checkValues(DVec values) {

		// set error message
		String message = "Illegal number of parameters for element mechanical load!";

		// for line loads
		if (type_ == ElementMechLoad.line_) {
			if (values.rowCount() != 2)
				exceptionHandler(message);
		}

		// for area loads
		else if (type_ == ElementMechLoad.area_) {
			if (values.rowCount() != 3)
				exceptionHandler(message);
		}

		// for volume loads
		else if (type_ == ElementMechLoad.volume_) {
			if (values.rowCount() != 1)
				exceptionHandler(message);
		}

		// legal values
		return true;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
