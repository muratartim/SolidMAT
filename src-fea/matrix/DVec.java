package matrix;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.Vector.Norm;

/**
 * Class for general dense vector. This class uses an external library called
 * "MTJ".
 * 
 * @author Murat Artim
 */
public class DVec {

	/** Static variable for the coordinate system to be transformed. */
	public static final int toGlobal_ = 0, toLocal_ = 1;

	/** The DenseVector object. */
	public DenseVector vec_;

	/** Size of vector. */
	private int dim_;

	/**
	 * Creates vector from a Vector object.
	 * 
	 * @param vec
	 *            Vector object.
	 */
	public DVec(Vector vec) {
		vec_ = (DenseVector) vec;
		dim_ = vec_.size();
	}

	/**
	 * Creates an arg0 dimensioned vector of zeros.
	 * 
	 * @param arg0
	 *            Number of rows.
	 */
	public DVec(int arg0) {
		try {
			vec_ = new DenseVector(arg0);
			dim_ = vec_.size();
		} catch (Exception excep) {
			exceptionHandler("Vector has illegal dimension!");
		}
	}

	/**
	 * Creates vector from one dimensional double array.
	 * 
	 * @param arg0
	 *            One dimensional array.
	 */
	public DVec(double[] arg0) {
		try {
			vec_ = new DenseVector(arg0);
			dim_ = vec_.size();
		} catch (Exception excep) {
			exceptionHandler("Vector has illegal dimension!");
		}
	}

	/**
	 * Adds vectors (C = A + B).
	 * 
	 * @param arg0
	 *            The vector to be added (B).
	 * @return The resulting vector (C).
	 */
	public DVec add(DVec arg0) {

		// check for dimensions
		if (rowCount() != arg0.rowCount())
			exceptionHandler("Vector dimensions don't agree!");

		// add
		return new DVec(vec_.copy().add(arg0.vec_));
	}

	/**
	 * Adds element to specified position.
	 * 
	 * @param arg0
	 *            The row index.
	 * @param arg1
	 *            The value to be added.
	 */
	public void add(int arg0, double arg1) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");

		// add elements
		vec_.add(arg0, arg1);
	}

	/**
	 * Adds sub-vector to respective place (C = A + b).
	 * 
	 * @param arg0
	 *            The sub-vector (b).
	 * @param arg1
	 *            Initial row index to be added to A.
	 * @return The resulting vector (C).
	 */
	public DVec addSubVector(DVec arg0, int arg1) {

		// check for illegal indices
		if (arg1 < 0 || arg1 > rowCount() - 1)
			exceptionHandler("Illegal index!");

		// compute final row index
		int ii = arg1 + arg0.rowCount() - 1;

		// check for illegal placement of subvector
		if (ii > rowCount() - 1)
			exceptionHandler("Illegal addition of sub-vector!");

		// loop over rows of sub-vector
		DVec C = new DVec(vec_.copy());
		for (int i = 0; i < arg0.rowCount(); i++)

			// add element to respective place
			C.add(arg1 + i, arg0.get(i));

		// return resulting matrix
		return C;
	}

	/**
	 * Performs cross (vector) product of two vectors. Can only be applied to
	 * two vectors having three dimensions. The result is again a vector with
	 * three dimensions.
	 * 
	 * @param arg0
	 *            The second vector to be crossed.
	 * @return The vector product.
	 */
	public DVec cross(DVec arg0) {

		// check if vectors have three dimensions
		if (rowCount() != 3 || arg0.rowCount() != 3)
			exceptionHandler("Illegal dimensions of vectors for cross product!");

		// get components of two vectors
		double ax = vec_.get(0);
		double ay = vec_.get(1);
		double az = vec_.get(2);
		double bx = arg0.vec_.get(0);
		double by = arg0.vec_.get(1);
		double bz = arg0.vec_.get(2);

		// perform operation
		double[] C = new double[3];
		C[0] = ay * bz - az * by;
		C[1] = az * bx - ax * bz;
		C[2] = ax * by - ay * bx;

		// return vector product
		return new DVec(C);
	}

	/**
	 * Performs dot product of two vectors.
	 * 
	 * @param arg0
	 *            The second vector.
	 * @return The dot product.
	 */
	public double dot(DVec arg0) {

		// check if they have same dimension
		if (rowCount() != arg0.rowCount())
			exceptionHandler("Vector dimensions don't agree!");

		// perform operation
		double val = 0.0;
		for (int i = 0; i < rowCount(); i++)
			val += get(i) * arg0.get(i);
		return val;
	}

	/**
	 * Returns the demanded element.
	 * 
	 * @param arg0
	 *            The row index of demanded element.
	 * @return The demanded element.
	 */
	public double get(int arg0) {

		// check index
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");

		// return element
		return vec_.get(arg0);
	}

	/**
	 * Returns one dimensional array storing elements of vector.
	 * 
	 * @return One dimensional array.
	 */
	public double[] get1DArray() {
		return Matrices.getArray(vec_.copy());
	}

	/**
	 * Returns sub-vector in the given intervals.
	 * 
	 * @param arg0
	 *            Initial row index.
	 * @param arg1
	 *            Final row index.
	 * @return The sub-vector.
	 */
	public DVec getSubVector(int arg0, int arg1) {

		// check for illegal initial index
		if (arg0 < 0 || arg0 > rowCount() - 1)
			exceptionHandler("Illegal indices!");

		// check for illegal final index
		if (arg1 < 0 || arg1 > rowCount() - 1)
			exceptionHandler("Illegal indices!");

		// check for illegal mutual indices
		if (arg1 < arg0)
			exceptionHandler("Illegal indices!");

		// get sub-vector
		DVec B = new DVec(arg1 - arg0 + 1);
		for (int i = arg0; i <= arg1; i++)
			B.set(i - arg0, get(i));

		// return sub-vector
		return B;
	}

	/**
	 * Returns L2-Norm of vector.
	 * 
	 * @return The L2-Norm.
	 */
	public double l2Norm() {
		return vec_.norm(Norm.Two);
	}

	/**
	 * Prints vector in decimal format.
	 * 
	 * @param arg0
	 *            The column width.
	 * @param arg1
	 *            The number of digits after the decimal seperator.
	 */
	public void printInDecimal(int arg0, int arg1) {
		PrintWriter pw = new PrintWriter(System.out, true);
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(arg1);
		format.setMinimumFractionDigits(arg1);
		format.setGroupingUsed(false);
		pw.println();
		for (int i = 0; i < rowCount(); i++) {
			String s = format.format(get(i));
			int padding = Math.max(1, arg0 + 2 - s.length());
			for (int k = 0; k < padding; k++)
				pw.print(' ');
			pw.print(s);
			pw.println();
		}
		pw.println();
	}

	/**
	 * Prints vector in scientific format.
	 * 
	 * @param arg0
	 *            The number of digits after the decimal seperator.
	 */
	public void printInScientific(int arg0) {

		for (int i = 0; i < rowCount(); i++) {

			// get element
			double value = get(i);

			// format element
			String s = String.format("%." + arg0 + "E", value);

			// print space
			if (value < 0)
				System.out.print(" ");
			else
				System.out.print("  ");

			// print element
			System.out.print(s);

			// pass to next line
			System.out.println();
		}
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return dim_;
	}

	/**
	 * Returns scaled vector (B = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return The scaled vector (B).
	 */
	public DVec scale(double arg0) {
		return new DVec(vec_.copy().scale(arg0));
	}

	/**
	 * Sets the element to respective place.
	 * 
	 * @param arg0
	 *            The row index to be set.
	 * @param arg1
	 *            The element to be set.
	 */
	public void set(int arg0, double arg1) {

		// check index
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");

		// set element
		vec_.set(arg0, arg1);
	}

	/**
	 * Sets sub-vector to respective place.
	 * 
	 * @param arg0
	 *            The sub-vector.
	 * @param arg1
	 *            Initial row index.
	 * @return The new vector.
	 */
	public DVec setSubVector(DVec arg0, int arg1) {

		// check for illegal index
		if (arg1 < 0 || arg1 > rowCount() - 1)
			exceptionHandler("Illegal index!");

		// compute final row index
		int ii = arg1 + arg0.rowCount() - 1;

		// check for illegal placement of subvector
		if (ii > rowCount() - 1)
			exceptionHandler("Illegal addition of sub-vector!");

		// loop over rows of sub-vector
		DVec C = new DVec(vec_.copy());
		for (int i = 0; i < arg0.rowCount(); i++)

			// add element to respective place
			C.set(arg1 + i, arg0.get(i));

		// return resulting vector
		return C;
	}

	/**
	 * Subtracts vectors.
	 * 
	 * @param arg0
	 *            The second vector.
	 * @return The result vector.
	 */
	public DVec subtract(DVec arg0) {

		// check for dimensions
		if (rowCount() != arg0.rowCount())
			exceptionHandler("Vector dimensions don't agree!");

		// subtract
		return add(arg0.scale(-1.0));
	}

	/**
	 * Transforms the vector to the demanded coordinate system.
	 * 
	 * @param tr
	 *            The transformation matrix.
	 * @param toSystem
	 *            The coordinate system that the vector will be transformed.
	 * @return The transformed vector.
	 */
	public DVec transform(DMat tr, int toSystem) {

		// check coordinate system
		if (toSystem < 0 || toSystem > 1)
			exceptionHandler("Illegal coordinate transformation!");

		// to global coordinate system
		if (toSystem == toGlobal_)
			return tr.transpose().multiply(this);

		// to local coordinate system
		else
			return tr.multiply(this);
	}

	/**
	 * Deletes internal storage of vector and frees memory.
	 * 
	 */
	public void delete() {
		vec_ = null;
	}

	/**
	 * Reallocates internal storage of vector.
	 * 
	 */
	public void reAllocate() {
		vec_ = new DenseVector(dim_);
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
