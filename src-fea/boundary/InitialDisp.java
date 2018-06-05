package boundary;

import java.io.Serializable;

import matrix.DVec;

/**
 * Class for initial displacements (for dynamic analysis).
 * 
 * @author Murat
 * 
 */
public class InitialDisp implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of displacement. */
	public final static int global_ = 0, local_ = 1;

	/** The name of initial displacement. */
	private String name_;

	/** The component vector of displacement. */
	private double[] components_;

	/** The coordinate system of displacement. */
	private int coordinateSystem_ = DispLoad.global_;

	/** The boundary case of initial displacement. */
	private BoundaryCase boundaryCase_;

	/** The value for scaling loading values. */
	private double scale_ = 1.0;

	/**
	 * Creates initial nodal displacement.
	 * 
	 * @param name
	 *            The name of initial displacement.
	 * @param boundaryCase
	 *            The boundary case of the initial displacement.
	 * @param components
	 *            The component vector of displacement. It has 6 components,
	 *            first three are translations, last three are rotations.
	 */
	public InitialDisp(String name, BoundaryCase boundaryCase, DVec components) {

		// set name
		name_ = name;

		// set boundary case
		boundaryCase_ = boundaryCase;

		// set components
		if (components.rowCount() == 6)
			components_ = components.get1DArray();
		else
			exceptionHandler("Illegal dimension of displacement vector!");
	}

	/**
	 * Sets boundary case to initial displacement.
	 * 
	 * @param boundaryCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase boundaryCase) {
		boundaryCase_ = boundaryCase;
	}

	/**
	 * Sets coordinate system of initial displacement.
	 * 
	 * @param coordinateSystem
	 *            The nodal coordinate system to be set.
	 */
	public void setCoordinateSystem(int coordinateSystem) {
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal assignment for coordinate system!");
		else
			coordinateSystem_ = coordinateSystem;
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
	 * Returns the name of initial displacement.
	 * 
	 * @return The name of initial displacement.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the boundary case of initial displacement.
	 * 
	 * @return The boundary case of initial displacement.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns the components vector of displacement.
	 * 
	 * @return The components vector of displacement.
	 */
	public DVec getComponents() {
		return new DVec(components_).scale(scale_);
	}

	/**
	 * Returns the coordinate system of displacement.
	 * 
	 * @return The coordinate system of displacement.
	 */
	public int getCoordinateSystem() {
		return coordinateSystem_;
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
