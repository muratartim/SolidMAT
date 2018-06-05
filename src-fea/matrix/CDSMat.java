package matrix;

import no.uib.cipr.matrix.sparse.CompDiagMatrix;

/**
 * Class for compressed-diagonal-storage-square sparse matrix (CDS). Can be used
 * for iterative solvers. This class uses an external library called "MTJ".
 * 
 * @author Murat Artim
 */
public class CDSMat extends SMat {

	/** The CompDiagMatrix object. */
	public CompDiagMatrix mat_;

	/** Half-bandwidth. */
	private int hbw_;

	/**
	 * Creates sparse matrix from another sparse matrix.
	 * 
	 * @param arg0
	 *            Sparse matrix.
	 */
	public CDSMat(CDSMat arg0) {
		mat_ = (CompDiagMatrix) arg0.mat_.copy();
		hbw_ = arg0.hbw_;
	}

	/**
	 * Creates a new arg0 x arg0 sparse matrix without preallocation.
	 * 
	 * @param arg0
	 *            Number of rows.
	 * @param arg1
	 *            Half-bandwidth.
	 */
	public CDSMat(int arg0, int arg1) {
		try {

			// create CRS matrix
			mat_ = new CompDiagMatrix(arg0, arg0);

			// set half-bandwidth
			hbw_ = arg1;
		} catch (Exception excep) {
			exceptionHandler("Matrix has illegal dimensions!");
		}
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
		CDSMat arg = (CDSMat) arg0;

		// check for dimensions
		if (rowCount() != arg.rowCount() || columnCount() != arg.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// set maximum bandwidth sizes
		hbw_ = Math.max(hbw_, arg.hbw_);

		// add
		mat_.add(arg.mat_);
		return this;
	}

	/**
	 * Adds element to the specified position. Entries outside bandwiths will be
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

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// add elements
		if (arg0 < arg1 + hbw_ + 1 && arg1 < arg0 + hbw_ + 1)
			mat_.add(arg0, arg1, arg2);
	}

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public int columnCount() {
		return mat_.numColumns();
	}

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public CDSMat copy() {
		return new CDSMat(this);
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

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// return element
		return mat_.get(arg0, arg1);
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
		mat_.mult(arg0.vec_, vec.vec_);
		return vec;
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return mat_.numRows();
	}

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public CDSMat scale(double arg0) {
		mat_.scale(arg0);
		return this;
	}

	/**
	 * Sets the element to respective place. Entries outside bandwiths will be
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

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// set element
		if (arg0 < arg1 + hbw_ + 1 && arg1 < arg0 + hbw_ + 1)
			mat_.set(arg0, arg1, arg2);
	}
}
