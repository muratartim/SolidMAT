package matrix;

/**
 * Class for all sparse matrices.
 * 
 * @author Murat Artim
 */
public abstract class SMat extends Mat {

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public abstract SMat copy();

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public abstract SMat scale(double arg0);

	/**
	 * Adds matrices (A = A + B).
	 * 
	 * @param arg0
	 *            The matrix to be added (B).
	 * @return This matrix (A).
	 */
	public abstract SMat add(SMat arg0);

	/**
	 * Performs linear algebraic matrix-vector multiplication (c = A * b).
	 * 
	 * @param arg0
	 *            The vector to be multiplied (b).
	 * @return The product vector (c).
	 */
	public abstract DVec multiply(DVec arg0);
}
