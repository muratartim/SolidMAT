package node;

import java.io.Serializable;

import matrix.DMat;

/**
 * Class for nodal mass.
 * 
 * @author Murat
 * 
 */
public class NodalMass implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of mass. */
	public final static int global_ = 0, local_ = 1;

	/** The mass matrix of nodal mass. */
	private double[][] mass_;

	/** The coordinate system of nodal mass. */
	private int coordinateSystem_;

	/** The name of nodal mass. */
	private String name_;

	/**
	 * Creates nodal mass.
	 * 
	 * @param name
	 *            Name of nodal mass.
	 * @param coordinateSystem
	 *            The coordinate system of the nodal mass.
	 * @param mass
	 *            The mass matrix of the nodal mass. The nodal mass matrix
	 *            should be positive definite and symmetric.
	 */
	public NodalMass(String name, int coordinateSystem, DMat mass) {

		// set name
		name_ = name;

		// set coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal assignment for coordinate system!");
		else
			coordinateSystem_ = coordinateSystem;

		// check mass matrix
		if (mass.rowCount() != 6 || mass.columnCount() != 6)
			exceptionHandler("Illegal mass matrix for nodal mass!");
		if (mass.determinant() < 0)
			exceptionHandler("Illegal mass matrix for nodal mass!");
		if (!mass.isSymmetric())
			exceptionHandler("Illegal mass matrix for nodal mass!");

		// set mass
		mass_ = mass.get2DArray();
	}

	/**
	 * Returns name of the nodal mass.
	 * 
	 * @return Name of nodal mass.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the mass matrix of nodal mass.
	 * 
	 * @return The mass matrix of nodal mass.
	 */
	public DMat getMass() {
		return new DMat(mass_);
	}

	/**
	 * Returns the coordinate system of nodal mass.
	 * 
	 * @return The coordinate system of nodal mass.
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
