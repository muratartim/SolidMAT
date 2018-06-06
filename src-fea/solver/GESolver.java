/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
