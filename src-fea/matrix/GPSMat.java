package matrix;

/**
 * Class for general purpose storage matrix.
 * 
 * @author Murat Artim
 */
public class GPSMat extends Mat {

	/** Internal storage array. */
	private double[][] mat_;

	/** Row and column counts of matrix. */
	private int row_, col_;

	public GPSMat(int i, int j) {
		mat_ = new double[i][j];
		row_ = i;
		col_ = j;
	}

	@Override
	public void add(int arg0, int arg1, double arg2) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// add
		mat_[arg0][arg1] += arg2;
	}

	@Override
	public int columnCount() {
		return col_;
	}

	@Override
	public double get(int arg0, int arg1) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// return element
		return mat_[arg0][arg1];
	}

	@Override
	public int rowCount() {
		return row_;
	}

	@Override
	public void set(int arg0, int arg1, double arg2) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// add
		mat_[arg0][arg1] = arg2;
	}

	/**
	 * Returns internal storage array.
	 * 
	 * @return Internal storage array.
	 */
	public double[][] getData() {
		return mat_;
	}

	/**
	 * Deletes internal storage and frees memory.
	 * 
	 */
	public void delete() {
		mat_ = null;
	}

	/**
	 * Reallocates internal storage arrays.
	 * 
	 */
	public void reAllocate() {
		mat_ = new double[row_][col_];
	}
}
