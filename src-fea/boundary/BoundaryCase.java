package boundary;

import java.io.Serializable;

/**
 * Class for storing data coming from Boundary Case Library menu.
 * 
 * @author Murat
 * 
 */
public class BoundaryCase implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The name of the boundary case object. */
	private String name_;

	/**
	 * Stores the given parameters into attributes.
	 * 
	 * @param name
	 *            The name of boundary case.
	 */
	public BoundaryCase(String name) {
		name_ = name;
	}

	/**
	 * Sets the name of boundary case.
	 * 
	 * @param name
	 *            The name of boundary case.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Returns the name of boundary case.
	 * 
	 * @return name_ The name of boundary case.
	 */
	public String getName() {
		return name_;
	}
}
