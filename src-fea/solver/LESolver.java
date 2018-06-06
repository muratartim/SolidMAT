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
