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

import matrix.GPSMat;
import matrix.DVec;
import matrix.Mat;
import matrix.USPMat;

import org.netlib.lapack.DSPGVX;
import org.netlib.util.intW;

/**
 * Class for generalized eigenvalue problem solver. It computes selected
 * eigenvalues and eigenvectors of a real generalized symmetric-definite packed
 * eigenproblem, of the form A * x = (lambda) * B * x. Here A and B are assumed
 * to be symmetric and packed, and B is also positive definite. Eigenvalues and
 * eigenvectors can be selected by a range of indices for the desired
 * eigenvalues. This class uses an external library called "JLAPACK".
 * 
 * @author Murat Artim
 * 
 */
public class Solver4 extends GESolver {

	private static final long serialVersionUID = 1L;

	/** Number of required eigenvalues and eigenvectors. */
	private int nRoot_;

	/** Convergence tolerance on eigenvalues (1.e-06 or smaller). */
	private double rTol_;

	/**
	 * Creates direct eigenproblem solver.
	 * 
	 * @param name
	 *            The name of solver.
	 * @param nRoot
	 *            Number of required eigenvalues and eigenvectors.
	 * @param rTol
	 *            Convergence tolerance on eigenvalues (1.e-06 or smaller).
	 */
	public Solver4(String name, int nRoot, double rTol) {

		// set name
		setName(name);

		// check parameters
		checkParameters(nRoot, rTol);

		// set parameters
		nRoot_ = nRoot;
		rTol_ = rTol;
	}

	@Override
	public int getNumberOfRequiredEigenvalues() {
		return nRoot_;
	}

	/**
	 * Returns the convergence tolerance of solver.
	 * 
	 * @return The convergence tolerance of solver.
	 */
	public double getConvergenceTolerance() {
		return rTol_;
	}

	@Override
	public int getStorageType() {
		return Solver.USPS_;
	}

	@Override
	public int getType() {
		return Solver.solver4_;
	}

	@Override
	public int getSolverType() {
		return GESolver.Direct_;
	}

	@Override
	public void initialize(Mat matA, Mat matB) {

		// get the order of equation system
		int n = matA.rowCount();

		// check number of modes to be found
		if (nRoot_ > n)
			nRoot_ = n;
	}

	@Override
	public void solve(Mat matA, Mat matB, DVec eigval, GPSMat eigvec,
			int nEigval) {

		// cast matrices
		USPMat a = (USPMat) matA;
		USPMat b = (USPMat) matB;

		// initialize number of variable for the # of modes found after solution
		intW m = new intW(0);

		// get the order of equation system
		int n = a.rowCount();

		// initialize exit message variable
		intW info = new intW(0);

		// check number of modes to be found
		if (nRoot_ > n)
			nRoot_ = n;

		// start the solver, and check for problems
		try {
			DSPGVX.DSPGVX(1, "V", "I", "U", n, a.mat_.getData(), b.mat_
					.getData(), 0, 1, 1, nRoot_ - 1, rTol_, m, eigval.vec_
					.getData(), eigvec.getData(), new double[8 * n],
					new int[5 * n], new int[nRoot_], info);

			// successful exit
			if (info.val == 0) {

				// set number of eigenvalues found
				nEigval = m.val;
			}

			// solve failed
			else
				exceptionHandler("Solve failed!");
		}

		// problem occured
		catch (Exception e) {
			exceptionHandler("Solve failed!");
		}
	}

	/**
	 * Checks given parameters.
	 * 
	 * @param nRoot
	 *            Number of required eigenvalues and eigenvectors.
	 * @param rTol
	 *            Convergence tolerance on eigenvalues.
	 */
	private void checkParameters(int nRoot, double rTol) {

		// initialize error message
		String err = "Invalid parameter for solver!";

		// check nRoot
		if (nRoot <= 0 || nRoot > 20)
			exceptionHandler(err);

		// check rTol
		if (rTol <= 0.0)
			exceptionHandler(err);
	}
}
