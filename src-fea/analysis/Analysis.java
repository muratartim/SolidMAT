package analysis;

import java.io.Serializable;
import java.util.Vector;

import boundary.BoundaryCase;

/**
 * Class for analysis.
 * 
 * @author Murat
 * 
 */
public abstract class Analysis implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of analysis. */
	public static final int linearStatic_ = 0, modal_ = 1,
			linearTransient_ = 2, linearBuckling_ = 3;

	/** Boolean variables denoting if analysis killed or completed. */
	protected boolean killed_ = false, completed_ = false;

	/** Current status message of analysis. */
	protected String status_;

	/** The name of analysis. */
	protected String name_;

	/** The boundary cases of analysis. */
	protected Vector<BoundaryCase> bCases_ = new Vector<BoundaryCase>();

	/** The scaling factors of boundary cases of analysis. */
	protected double[] bScales_ = new double[0];

	/** The structure to be analyzed. */
	protected Structure structure_;

	/** The path of analysis to write output data. */
	protected String path_;

	/**
	 * Sets structure to analysis.
	 * 
	 * @param structure
	 *            The structure to be analyzed.
	 */
	public void setStructure(Structure structure) {
		structure_ = structure;
	}

	/**
	 * Sets path to analysis to write output data.
	 * 
	 * @param path
	 *            The path to write output data.
	 */
	public void setPath(String path) {
		path_ = path;
	}

	/**
	 * Sets boundary cases for the analysis.
	 * 
	 * @param bCases
	 *            Vector storing the boundary cases.
	 * @param scales
	 *            The scaling factors of boundary cases.
	 */
	public void setBoundaries(Vector<BoundaryCase> bCases, double[] scales) {
		bCases_ = bCases;
		bScales_ = scales;
	}

	/** Starts analysis for the demanded analysis option. */
	public abstract void analyze();

	/**
	 * Returns the name of analysis.
	 * 
	 * @return The name of analysis.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the structure of the analysis.
	 * 
	 * @return The structure subjected for the analysis.
	 */
	public Structure getStructure() {
		return structure_;
	}

	/**
	 * Returns path of analysis for writing output data.
	 * 
	 * @return Path of analysis for writing output data.
	 */
	public String getPath() {
		return path_;
	}

	/**
	 * Returns the boundary cases assigned to the analysis.
	 * 
	 * @return The boundary case array of analysis.
	 */
	public Vector<BoundaryCase> getBoundaries() {
		return bCases_;
	}

	/**
	 * Returns the scaling factor array of boundaries assigned for the analysis.
	 * 
	 * @return The scaling factor array of boundaries.
	 */
	public double[] getBoundaryScales() {
		return bScales_;
	}

	/**
	 * Returns the type of analysis.
	 * 
	 * @return The type of analysis.
	 */
	public abstract int getType();

	/**
	 * Returns message indicating the current status of analysis.
	 * 
	 * @return Message indicating the current status of analysis.
	 */
	public String getStatus() {
		return status_;
	}

	/**
	 * Returns analysis properties depending on the analysis type.
	 * 
	 * @return Vector storing the analysis properties.
	 */
	protected abstract Vector<Object> getAnalysisInfo();

	/**
	 * Returns True if analysis completed, False vice versa.
	 * 
	 * @return True if analysis completed, False vice versa.
	 */
	public boolean isCompleted() {
		return completed_;
	}

	/**
	 * Returns True if analysis killed, False vice versa.
	 * 
	 * @return True if analysis killed, False vice versa.
	 */
	public boolean isKilled() {
		return killed_;
	}

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
