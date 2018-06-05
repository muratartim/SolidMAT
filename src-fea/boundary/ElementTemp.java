package boundary;

import java.io.Serializable;

/**
 * Class for element temperature loads.
 * 
 * @author Murat
 * 
 */
public class ElementTemp implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The name of element temperature load. */
	private String name_;

	/** The boundary case of element temperature load. */
	private BoundaryCase boundaryCase_;

	/** The loading value. */
	private double value_;

	/** The value for scaling loading values. */
	private double scale_ = 1.0;

	/**
	 * Creates element temperature load.
	 * 
	 * @param name
	 *            The name of load.
	 * @param bCase
	 *            The boundary case of load.
	 * @param value
	 *            The value of load.
	 */
	public ElementTemp(String name, BoundaryCase bCase, double value) {
		name_ = name;
		boundaryCase_ = bCase;
		value_ = value;
	}

	/**
	 * Sets boundary case to element temperature load.
	 * 
	 * @param bCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase bCase) {
		boundaryCase_ = bCase;
	}

	/**
	 * Sets scaling factor for loading values.
	 * 
	 * @param scale
	 *            The scaling factor for loading values.
	 */
	public void setLoadingScale(double scale) {
		scale_ = scale;
	}

	/**
	 * Returns the name of element temperature load.
	 * 
	 * @return The name of element temperature load.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the boundary case of element temperature load.
	 * 
	 * @return The boundary case of element temperature load.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns the loading value.
	 * 
	 * @return The loading value of temperature load.
	 */
	public double getValue() {
		return value_ * scale_;
	}
}
