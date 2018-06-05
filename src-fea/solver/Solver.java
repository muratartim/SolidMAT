package solver;

import java.io.Serializable;

/**
 * Class for all solvers.
 * 
 * @author Murat Artim
 * 
 */
public abstract class Solver implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the solver type. */
	public static final int solver0_ = 0, solver1_ = 1, solver2_ = 2,
			solver3_ = 3, solver4_ = 4;

	/** Static variable for the problem type of solver. */
	public static final int linearSystem_ = 0, eigenSystem_ = 1;

	/** Static variable for the storage type of solver. */
	public static final int CDS_ = 0, CRS_ = 1, USPS_ = 2, USB1S_ = 3,
			USB2S_ = 4;

	/** The name of solver. */
	private String name_;

	/**
	 * Sets name to solver.
	 * 
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Returns the name of solver.
	 * 
	 * @return The name of solver.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the type of solver.
	 * 
	 * @return The type of solver.
	 */
	public abstract int getType();

	/**
	 * Returns the problem type of solver.
	 * 
	 * @return The problem type of solver.
	 */
	public abstract int getProblemType();

	/**
	 * Returns the storage type of solver.
	 * 
	 * @return The storage type of solver.
	 */
	public abstract int getStorageType();

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
