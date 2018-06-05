package matrix;

/**
 * Class for upper-symmetric-banded matrix stored in 1D array (USB1).
 * 
 * @author Murat Artim
 */
public class USB1Mat extends SMat {

	/** The dimension and the half-bandwidth. */
	private int n_, hbw_;

	/** Internal storage array. */
	private double[] mat_;

	/** Array storing the adresses of diagonal elements. */
	private int[] maxa_;

	/**
	 * Creates arg0 by arg0 upper-symmetric-banded-square matrix.
	 * 
	 * @param arg0
	 *            Dimension.
	 * @param arg1
	 *            Half-bandwidth.
	 */
	public USB1Mat(int arg0, int arg1) {

		// compute dimension of storage array
		int dim = arg0;
		for (int i = 1; i < arg1 + 1; i++)
			dim += arg0 - i;
		mat_ = new double[dim];

		// compute adress array
		maxa_ = new int[arg0 + 1];
		for (int i = 1; i < arg0 + 1; i++) {
			if (i <= arg1)
				maxa_[i] = maxa_[i - 1] + i;
			else
				maxa_[i] = maxa_[i - 1] + arg1 + 1;
		}

		// set dimension
		n_ = arg0;

		// set halfbandwidth
		hbw_ = arg1;
	}

	/**
	 * Creates sparse matrix from another sparse matrix.
	 * 
	 * @param arg0
	 *            Sparse matrix.
	 */
	public USB1Mat(USB1Mat arg0) {

		// copy storage array
		mat_ = new double[arg0.getData().length];
		for (int i = 0; i < mat_.length; i++)
			mat_[i] = arg0.getData()[i];

		// copy adresses array
		maxa_ = new int[arg0.getAdresses().length];
		for (int i = 0; i < maxa_.length; i++)
			maxa_[i] = arg0.getAdresses()[i];

		// copy dimension
		n_ = arg0.rowCount();

		// copy halfbandwidth
		hbw_ = arg0.getHalfBandwidth();
	}

	/**
	 * Adds matrices (A = A + B).
	 * 
	 * @param arg0
	 *            The matrix to be added (B).
	 * @return This matrix (A).
	 */
	public SMat add(SMat arg0) {

		// cast given matrix
		USB1Mat arg = (USB1Mat) arg0;

		// check for dimensions
		if (rowCount() != arg.rowCount() || columnCount() != arg.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// check half bandwidths
		if (getHalfBandwidth() < arg.getHalfBandwidth())
			exceptionHandler("Matrix bandwidths don't agree!");

		// add
		for (int i = 0; i < rowCount(); i++)
			for (int j = 0; j < columnCount(); j++)
				add(i, j, arg.get(i, j));
		return this;
	}

	/**
	 * Adds element to the specified position. Entries outside bandwidth will be
	 * ignored.
	 * 
	 * @param arg0
	 *            The row index.
	 * @param arg1
	 *            The column index.
	 * @param arg2
	 *            The value to be added.
	 */
	public void add(int arg0, int arg1, double arg2) {
		if (Math.max(0, arg1 - hbw_) <= arg0 && arg0 <= arg1)
			mat_[maxa_[arg1] + arg1 - arg0] += arg2;
	}

	/**
	 * Sets the element to respective place. Entries outside bandwidth will be
	 * ignored.
	 * 
	 * @param arg0
	 *            The row index to be set.
	 * @param arg1
	 *            The column index to be set.
	 * @param arg2
	 *            The element to be set.
	 */
	public void set(int arg0, int arg1, double arg2) {
		if (Math.max(0, arg1 - hbw_) <= arg0 && arg0 <= arg1)
			mat_[maxa_[arg1] + arg1 - arg0] = arg2;
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return n_;
	}

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public int columnCount() {
		return n_;
	}

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public USB1Mat copy() {
		return new USB1Mat(this);
	}

	/**
	 * Performs linear algebraic matrix-vector multiplication (c = A * b).
	 * 
	 * @param arg0
	 *            The vector to be multiplied (b).
	 * @return The product vector (c).
	 */
	public DVec multiply(DVec arg0) {
		DVec vec = new DVec(arg0.rowCount());
		for (int i = 0; i < rowCount(); i++)
			for (int j = 0; j < columnCount(); j++)
				vec.set(i, get(i, j) * arg0.get(i));
		return vec;
	}

	/**
	 * Returns the demanded element.
	 * 
	 * @param arg0
	 *            The row index of demanded element.
	 * @param arg1
	 *            The column index of demanded element.
	 * @return The demanded element.
	 */
	public double get(int arg0, int arg1) {
		if (arg0 < arg1 + hbw_ + 1 && arg1 < arg0 + hbw_ + 1) {
			if (arg0 <= arg1)
				return mat_[maxa_[arg1] + arg1 - arg0];
			else
				return mat_[maxa_[arg0] + arg0 - arg1];
		}
		return 0.0;
	}

	/**
	 * Returns the internal storage array.
	 * 
	 * @return The internal storage array.
	 */
	public double[] getData() {
		return mat_;
	}

	/**
	 * Returns array containing the adresses of diagonal elements.
	 * 
	 * @return Array containing the adresses of diagonal elements.
	 */
	public int[] getAdresses() {
		return maxa_;
	}

	/**
	 * Returns the number of super diagonals.
	 * 
	 * @return The number of super diagonals.
	 */
	public int getHalfBandwidth() {
		return hbw_;
	}

	/**
	 * Returns number of non-zero diagonal elements.
	 * 
	 * @return Number of non-zero diagonal elements.
	 */
	public int getNumberOfNZDElements() {
		int nz = 0;
		for (int i = 0; i < n_; i++)
			if (get(i, i) != 0.0)
				nz++;
		return nz;
	}

	/**
	 * Deletes internal storage arrays and frees memory.
	 * 
	 */
	public void delete() {
		mat_ = null;
		maxa_ = null;
	}

	/**
	 * Reallocates internal storage arrays.
	 * 
	 */
	public void reAllocate() {

		// reallocate mat_
		int dim = n_;
		for (int i = 1; i < hbw_ + 1; i++)
			dim += n_ - i;
		mat_ = new double[dim];

		// reallocate and compute maxa_
		maxa_ = new int[n_ + 1];
		for (int i = 1; i < n_ + 1; i++) {
			if (i <= hbw_)
				maxa_[i] = maxa_[i - 1] + i;
			else
				maxa_[i] = maxa_[i - 1] + hbw_ + 1;
		}
	}

	/**
	 * Sets the element to internal storage array.
	 * 
	 * @param arg0
	 *            Index of element.
	 * @param arg1
	 *            Element to be set.
	 */
	public void set(int arg0, double arg1) {
		mat_[arg0] = arg1;
	}

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public USB1Mat scale(double arg0) {
		for (int i = 0; i < mat_.length; i++)
			mat_[i] *= arg0;
		return this;
	}
}
