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

import no.uib.cipr.matrix.sparse.BiCG;
import no.uib.cipr.matrix.sparse.BiCGstab;
import no.uib.cipr.matrix.sparse.CG;
import no.uib.cipr.matrix.sparse.CGS;
import no.uib.cipr.matrix.sparse.CompRowMatrix;
import no.uib.cipr.matrix.sparse.DiagonalPreconditioner;
import no.uib.cipr.matrix.sparse.GMRES;
import no.uib.cipr.matrix.sparse.ICC;
import no.uib.cipr.matrix.sparse.ILU;
import no.uib.cipr.matrix.sparse.IR;
import no.uib.cipr.matrix.sparse.IterativeSolver;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.sparse.Preconditioner;
import no.uib.cipr.matrix.sparse.QMR;
import matrix.CDSMat;
import matrix.DVec;
import matrix.Mat;

/**
 * Class for iterative linear equation solvers. It can be used for solving
 * linear system of equations (A x = b). This class uses an external library
 * called "MTJ". The properties of solvers are as follows; CG : Conjugate
 * Gradients solver. CG solves the symmetric positive definite linear system
 * Ax=b using the Conjugate Gradient method. CGS : Conjugate Gradients squared
 * solver. CGS solves the unsymmetric linear system Ax = b using the Conjugate
 * Gradient Squared method. BiCG : BiCG solver. BiCG solves the unsymmetric
 * linear system Ax = b using the Preconditioned BiConjugate Gradient method.
 * BiCGstab : BiCG stablized solver. BiCGstab solves the unsymmetric linear
 * system Ax = b using the Preconditioned BiConjugate Gradient Stabilized
 * method. QMR : Quasi-Minimal Residual method. QMR solves the unsymmetric
 * linear system Ax = b using the Quasi-Minimal Residual method. QMR uses two
 * preconditioners, and by default these are the same preconditioner. GMRES :
 * GMRES solver. GMRES solves the unsymmetric linear system Ax = b using the
 * Generalized Minimum Residual method. The GMRES iteration is restarted after a
 * given number of iterations. By default it is restarted after 30 iterations.
 * IR : Iterative Refinement. IR solves the unsymmetric linear system Ax = b
 * using Iterative Refinement (preconditioned Richardson iteration). The
 * properties of preconditioners are as follows; ICC : Incomplete Cholesky
 * preconditioner without fill-in using a compressed row matrix as internal
 * storage. ILU : ILU(0) preconditioner using a compressed row matrix as
 * internal storage. DP : Diagonal preconditioner. Uses the inverse of the
 * diagonal as preconditioner.
 * 
 * @author Murat Artim
 * 
 */
public class Solver0 extends LESolver {

	private static final long serialVersionUID = 1L;

	/** Static variable for the iterative solver type. */
	public static final int CG_ = 0, CGS_ = 1, BiCG_ = 2, BiCGstab_ = 3,
			QMR_ = 4, GMRES_ = 5, IR_ = 6;

	/** Static variable for the preconditioner type. */
	public static final int ICC_ = 0, ILU_ = 1, DP_ = 2;

	/** Iterative solver type. */
	private int solverType_;

	/** Preconditioner type. */
	private int precondType_;

	/** Iterative solver. */
	private IterativeSolver solver_;

	/**
	 * Creates linear system iterative solver.
	 * 
	 * @param name
	 *            The name of solver.
	 * @param solverType
	 *            Solver type.
	 * @param precondType
	 *            Preconditioner type.
	 */
	public Solver0(String name, int solverType, int precondType) {

		// set name
		setName(name);

		// check parameters
		checkParameters(solverType, precondType);

		// set parameters
		solverType_ = solverType;
		precondType_ = precondType;
	}

	/**
	 * Returns the preconditioner type of this solver.
	 * 
	 * @return The preconditioner type of this solver.
	 */
	public int getPreconditionerType() {
		return precondType_;
	}

	/**
	 * Returns number of iterations performed.
	 * 
	 * @return Number of iterations performed.
	 */
	public int getIterations() {
		return solver_.getIterationMonitor().iterations();
	}

	/**
	 * Returns Returns final residual.
	 * 
	 * @return Final residual.
	 */
	public double getResidual() {
		return solver_.getIterationMonitor().residual();
	}

	@Override
	public int getSolverType() {
		return solverType_;
	}

	@Override
	public int getSolutionType() {
		return LESolver.iterative_;
	}

	@Override
	public int getStorageType() {
		if (precondType_ == Solver0.DP_)
			return Solver.CDS_;
		else
			return Solver.CRS_;
	}

	@Override
	public int getType() {
		return Solver.solver0_;
	}

	@Override
	public void initialize(Mat matA, DVec vecX) {

		// cast matrix
		CDSMat a = (CDSMat) matA;

		// allocate storage for the selected solver type
		createSolver(vecX);

		// create and set up the selected preconditioner
		createPreconditioner(a);
	}

	@Override
	public void solve(Mat matA, DVec vecB, DVec vecX) {

		// cast matrix
		CDSMat a = (CDSMat) matA;

		// start the solver, and check for problems
		try {
			solver_.solve(a.mat_, vecB.vec_, vecX.vec_);
		}

		// problem occured
		catch (IterativeSolverNotConvergedException e) {
			exceptionHandler("Iterative solver failed to converge");
		}
	}

	@Override
	public void clear() {
		solver_ = null;
	}

	/**
	 * Creates iterative solver for the given solver type
	 * 
	 * @param vecX
	 *            Solution vector (x). Has to be dense vector (DVec).
	 */
	private void createSolver(DVec vecX) {

		// Conjugate Gradients solver (CG)
		if (solverType_ == Solver0.CG_) {
			solver_ = new CG(vecX.vec_);
		}

		// Conjugate Gradients Squared solver (CGS)
		else if (solverType_ == Solver0.CGS_) {
			solver_ = new CGS(vecX.vec_);
		}

		// BiConjugate Gradients solver (BiCG)
		else if (solverType_ == Solver0.BiCG_) {
			solver_ = new BiCG(vecX.vec_);
		}

		// BiConjugate Gradients Stabilized solver (BiCGstab)
		else if (solverType_ == Solver0.BiCGstab_) {
			solver_ = new BiCGstab(vecX.vec_);
		}

		// Quasi-Minimal Residual solver (QMR)
		else if (solverType_ == Solver0.QMR_) {
			solver_ = new QMR(vecX.vec_);
		}

		// Generalized Minimum Residual solver (GMRES)
		else if (solverType_ == Solver0.GMRES_) {
			solver_ = new GMRES(vecX.vec_);
		}

		// Iterative Refinement solver (IR)
		else if (solverType_ == Solver0.IR_) {
			solver_ = new IR(vecX.vec_);
		}
	}

	/**
	 * Creates and sets up the selected preconditioner. Then, attaches it to the
	 * iterative solver.
	 * 
	 * @param matA
	 *            Coefficient matrix (A). Has to be compressed-diagonal-storage
	 *            sparse matrix (CDSMat).
	 */
	private void createPreconditioner(CDSMat matA) {

		// initialize preconditioner
		Preconditioner M = null;

		// Cholesky preconditioner without fill-in (ICC)
		if (precondType_ == Solver0.ICC_) {
			CompRowMatrix a = new CompRowMatrix(matA.mat_);
			M = new ICC(a);
			M.setMatrix(matA.mat_);
		}

		// Incomplete LU preconditioner without fill-in (ILU)
		else if (precondType_ == Solver0.ILU_) {
			CompRowMatrix a = new CompRowMatrix(matA.mat_);
			M = new ILU(a);
			M.setMatrix(matA.mat_);
		}

		// Diagonal preconditioner (DP)
		else if (precondType_ == Solver0.DP_) {
			M = new DiagonalPreconditioner(matA.rowCount());
			M.setMatrix(matA.mat_);
		}

		// attach preconditioner to solver
		solver_.setPreconditioner(M);
	}

	/**
	 * Checks given parameters for the solver and preconditioner types.
	 * 
	 * @param solverType
	 *            The type of iterative solver.
	 * @param precondType
	 *            The type of preconditioner.
	 */
	private void checkParameters(int solverType, int precondType) {

		// check solver type
		if (solverType < 0 || solverType > 6)
			exceptionHandler("Invalid solver type for iterative solver!");

		// check preconditioner type
		if (precondType < 0 || precondType > 2)
			exceptionHandler("Invalid preconditioner type for iterative solver!");
	}
}
