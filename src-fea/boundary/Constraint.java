package boundary;

import java.io.Serializable;

/**
 * Class for constraint.
 * 
 * @author Murat
 * 
 */
public class Constraint implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The constraint array. */
	private boolean[] free_;

	/** The name of constraint. */
	private String name_;

	/** The boundary case of constraint. */
	private BoundaryCase boundaryCase_;

	/**
	 * Creates constraints object.
	 * 
	 * @param name
	 *            The name of constraint.
	 * @param boundaryCase
	 *            The boundary case of constraint.
	 * @param constraints
	 *            The constraints array of constraint.
	 */
	public Constraint(String name, BoundaryCase boundaryCase,
			boolean[] constraints) {

		// set name
		name_ = name;

		// set boundary case
		boundaryCase_ = boundaryCase;

		// check and set constraints
		if (constraints.length != 6)
			exceptionHandler("Illegal dimension of constraints array!");
		free_ = constraints;
	}

	/**
	 * Sets name to constraint.
	 * 
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets constraints to constraint.
	 * 
	 * @param free
	 *            The constraints array to be set.
	 */
	public void setConstraints(boolean[] free) {
		free_ = free;
	}

	/**
	 * Sets boundary case to constraint.
	 * 
	 * @param boundaryCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase boundaryCase) {
		boundaryCase_ = boundaryCase;
	}

	/**
	 * Returns the name of constraint.
	 * 
	 * @return The name of constraint.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the constraints array of constraint.
	 * 
	 * @return The constraints array of constraint.
	 */
	public boolean[] getConstraints() {
		return free_;
	}

	/**
	 * Returns the boundary case of constraint.
	 * 
	 * @return The boundary case of constraint.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
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
