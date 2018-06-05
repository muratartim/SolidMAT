package solver;

import matrix.Mat;
import matrix.DVec;
import matrix.GPSMat;

/**
 * Class for generalized eigenproblem solvers.
 * 
 * @author Murat Artim
 * 
 */
public abstract class GESolver extends Solver {

	private static final long serialVersionUID = 1L;

	/** Static variable for the solver type. */
	public static final int SubSpace_ = 0, Direct_ = 1;

	@Override
	public int getProblemType() {
		return Solver.eigenSystem_;
	}

	/**
	 * Returns the number of required eigenvalues.
	 * 
	 * @return The number of required eigenvalues.
	 */
	public abstract int getNumberOfRequiredEigenvalues();

	/**
	 * Returns the convergence tolerance of solver.
	 * 
	 * @return The convergence tolerance of solver.
	 */
	public abstract double getConvergenceTolerance();

	/**
	 * Returns the solver type.
	 * 
	 * @return The solver type.
	 */
	public abstract int getSolverType();

	/**
	 * Initializes solver.
	 * 
	 * @param matA
	 *            Coefficient matrix A. Has to be in USP form (assumed positive
	 *            definite).
	 * @param matB
	 *            The second coefficient matrix (B). Has to be
	 *            upper-symmetric-packed matrix (USPMat).
	 */
	public abstract void initialize(Mat matA, Mat matB);

	/**
	 * Solves generalized eigenvalue problem.
	 * 
	 * @param matA
	 *            Coefficient matrix A.
	 * @param matB
	 *            Coefficient matrix B.
	 * @param eigval
	 *            Vector for storing the eigenvalues.
	 * @param eigvec
	 *            Matrix for storing the eigenvectors.
	 * @param nEigval
	 *            Number of computed eigenvalues.
	 */
	public abstract void solve(Mat matA, Mat matB, DVec eigval, GPSMat eigvec,
			int nEigval);
}
