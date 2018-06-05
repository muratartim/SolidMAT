package solver;

import matrix.Mat;
import matrix.DVec;

/**
 * Class for linear equation solvers.
 * 
 * @author Murat Artim
 * 
 */
public abstract class LESolver extends Solver {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of solution. */
	public static final int iterative_ = 0, direct_ = 1;

	/** Static variable for the direct solver type. */
	public static int COLSOL_ = 0, GaussSymm_ = 1;

	@Override
	public int getProblemType() {
		return Solver.linearSystem_;
	}

	/**
	 * Returns the solution type of solver.
	 * 
	 * @return The solution type of solver.
	 */
	public abstract int getSolutionType();

	/**
	 * Returns the solver type.
	 * 
	 * @return Solver type.
	 */
	public abstract int getSolverType();

	/**
	 * Initializes solver.
	 * 
	 * @param matA
	 *            Coefficient matrix (A).
	 * @param vecX
	 *            Solution vector (x).
	 */
	public abstract void initialize(Mat matA, DVec vecX);

	/**
	 * Solves system of linear equations (A x = b).
	 * 
	 * @param matA
	 *            Coefficient matrix (A).
	 * @param vecB
	 *            Right hand side vector (b).
	 * @param vecX
	 *            Solution vector (x).
	 */
	public abstract void solve(Mat matA, DVec vecB, DVec vecX);

	/**
	 * Clears attributes of solver (other than parameters).
	 * 
	 */
	public abstract void clear();
}
