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
package analysis;

import java.util.Vector;

import boundary.BoundaryCase;

import matrix.*;
import solver.*;

import node.Node;
import element.Element;

/**
 * Class for linear static analysis.
 * 
 * @author Murat
 * 
 */
public class LinearStatic extends Analysis {

	private static final long serialVersionUID = 1L;

	/** The number of equations to be solved and effective bandwidth. */
	private int eqn_ = 0, hbw_ = 0;

	/** The solver of analysis. */
	private LESolver solver_;

	/** # of iterations of iterative solver. */
	private Integer iterations_;

	/** Residual of iterative solver. */
	private Double residual_;

	/**
	 * Creates linear static analysis.
	 * 
	 * @param name
	 *            The name of analysis.
	 */
	public LinearStatic(String name) {
		name_ = name;
	}

	/**
	 * Sets solver to analysis.
	 * 
	 * @param solver
	 *            The solver to be set.
	 */
	public void setSolver(Solver solver) {
		solver_ = (LESolver) solver;
	}

	/**
	 * Returns analysis properties. The sequence of information is; name
	 * (String), analysis type (int), boundary cases (String[]), boundary scales
	 * (double[]), solver (String).
	 * 
	 * @return Vector storing the analysis information.
	 */
	protected Vector<Object> getAnalysisInfo() {

		// initialize vector
		Vector<Object> prop = new Vector<Object>();

		// get analysis name -0
		prop.add(getName());

		// get analysis type -1
		prop.add(getType());

		// get boundary cases -2
		Vector<BoundaryCase> bCases = getBoundaries();
		String[] names = new String[bCases.size()];
		for (int i = 0; i < names.length; i++)
			names[i] = bCases.get(i).getName();
		prop.add(names);

		// get boundary scales -3
		prop.add(getBoundaryScales());

		// get solver -4
		prop.add(getSolver().getName());

		// return vector
		return prop;
	}

	/**
	 * Returns the solver of analysis.
	 * 
	 * @return The solver of analysis.
	 */
	public Solver getSolver() {
		return solver_;
	}

	@Override
	public int getType() {
		return Analysis.linearStatic_;
	}

	/**
	 * Returns number of equations to be solved.
	 * 
	 * @return The number of equations to be solved.
	 */
	public int getNumberOfEquations() {
		return eqn_;
	}

	/**
	 * Returns effective bandwidth of the system stiffness matrix.
	 * 
	 * @return Effective bandwidth of the system stiffness matrix.
	 */
	public int getEffectiveBandWidth() {
		return hbw_;
	}

	/**
	 * Returns number of iterations, if an iterative solver is used.
	 * 
	 * @return Number of iterations.
	 */
	public Integer getNumberOfIterations() {
		return iterations_;
	}

	/**
	 * Returns residual, if an iterative solver is used.
	 * 
	 * @return Residual.
	 */
	public Double getResidual() {
		return residual_;
	}

	@Override
	public void analyze() {

		// initialize variables
		eqn_ = 0;
		hbw_ = 0;
		killed_ = false;
		completed_ = false;
		status_ = null;
		iterations_ = null;
		residual_ = null;

		// check model
		status_ = "Checking model...";
		String message = structure_.checkModel(0);

		// problem occured with the model
		if (message != null) {
			status_ = message;
			killed_ = true;
		}

		// no problem with the model
		else {

			// initialize structure
			status_ = "Initializing model...";
			structure_.initialize();

			// enumerate degrees of freedom of structure
			status_ = "Enumerating dofs...";
			eqn_ = structure_.enumerateDofs(bCases_, bScales_);

			// compute effective bandwidth
			status_ = "Computing effective bandwidth...";
			hbw_ = computeHalfBandWidth();

			// assemble system stiffness matrix
			status_ = "Assembling system stiffness matrix...";
			SMat kSystem = null;
			if (solver_.getType() == Solver.solver0_)
				kSystem = new CDSMat(eqn_, hbw_);
			else if (solver_.getType() == Solver.solver1_)
				kSystem = new USB1Mat(eqn_, hbw_);
			else if (solver_.getType() == Solver.solver2_)
				kSystem = new USB2Mat(eqn_, hbw_);
			assembleStiffness(kSystem);

			// assemble system load vector
			status_ = "Assembling system load vector...";
			DVec rSystem = new DVec(eqn_);
			assembleLoad(rSystem, kSystem);

			// start of linear equation solution
			status_ = "Solving linear system of equations...";
			DVec uSystem = new DVec(eqn_);
			solve(kSystem, rSystem, uSystem);
		}
	}

	/**
	 * Assembles system stiffness matrix. Both homogeneous and inhomogeneous
	 * Dirichlet boundaries are imposed during the assembly of system stiffness
	 * matrix.
	 * 
	 * @param kSystem
	 *            System stiffness matrix.
	 */
	private void assembleStiffness(Mat kSystem) {

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// get dof numbers of element
			int[] dof = e.getDofNumbers();

			// get stiffness matrix of element in nodal local coordinate system
			DMat ke = e.getStiffnessMatrix(Element.local_);

			// loop over rows of element stiffness matrix
			for (int j = 0; j < dof.length; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// loop over columns of element stiffness matrix
					for (int k = 0; k < dof.length; k++) {

						// check if dof is free
						if (dof[k] != -1) {

							// store into system stiffness matrix
							kSystem.add(dof[j], dof[k], ke.get(j, k));
						}
					}
				}
			}
		}

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// check if the node has stiffness
			if (node.getSprings() != null) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get stiffness matrix
				DMat kn = node.getStiffnessMatrix();

				// loop over rows of nodal stiffness matrix
				for (int j = 0; j < dof.length; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// loop over columns of nodal stiffness matrix
						for (int k = 0; k < dof.length; k++) {

							// check if dof is free
							if (dof[k] != -1) {

								// store into system stiffness matrix
								kSystem.add(dof[j], dof[k], kn.get(j, k));
							}
						}
					}
				}
			}

			// check if the node has displacement loads
			if (node.getDispLoads().size() != 0) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get displacement vector of node
				DVec vec = node.getDispLoadVector();

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// get the displacement value
						double value = vec.get(j);

						// check if the value is non-zero
						if (value != 0) {

							// modify the related stiffness term
							value = kSystem.get(dof[j], dof[j])
									* Math.pow(10.0, 8.0);
							kSystem.set(dof[j], dof[j], value);
						}
					}
				}
			}
		}
	}

	/**
	 * Assembles system load vector. Both homogeneous and inhomogeneous
	 * Dirichlet boundaries are imposed during the assembly of system load
	 * vector.
	 * 
	 * @param rSystem
	 *            System load vector.
	 * @param kSystem
	 *            The system stiffness matrix.
	 */
	private void assembleLoad(DVec rSystem, Mat kSystem) {

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// check if element has mechanical load
			if (e.getMechLoads().size() != 0) {

				// get dof array of element
				int[] dof = e.getDofNumbers();

				// get load vector of element in nodal local coordinates
				DVec re = e.getMechLoadVector(Node.local_);

				// loop over rows of element load vector
				for (int j = 0; j < dof.length; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system load vector
						rSystem.add(dof[j], re.get(j));
					}
				}
			}

			// check if element has thermal load
			if (e.getTempLoads().size() != 0) {

				// get dof array of element
				int[] dof = e.getDofNumbers();

				// get thermal load vector of element in nodal local coordinates
				DVec te = e.getTempLoadVector(Node.local_);

				// loop over rows of element load vector
				for (int j = 0; j < dof.length; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system load vector
						rSystem.add(dof[j], te.get(j));
					}
				}
			}
		}

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// check if the node has mechanical load
			if (node.getMechLoads().size() != 0) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get mechanical load vector of node
				DVec rn = node.getMechLoadVector();

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system load vector
						rSystem.add(dof[j], rn.get(j));
					}
				}
			}

			// check if the node has displacement load
			if (node.getDispLoads().size() != 0) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get displacement load vector of node
				DVec vec = node.getDispLoadVector();

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// get the displacement value
						double value = vec.get(j);

						// check if the value is non-zero
						if (value != 0.0) {

							// modify load coefficient
							value *= kSystem.get(dof[j], dof[j]);
							rSystem.set(dof[j], value);
						}
					}
				}
			}
		}
	}

	/**
	 * Performs solution of linear system of equations.
	 * 
	 * @param kSystem
	 *            System stiffness matrix.
	 * @param rSystem
	 *            System load vector.
	 * @param uSystem
	 *            Solution vector.
	 */
	private void solve(Mat kSystem, DVec rSystem, DVec uSystem) {

		try {

			// initialize solver
			solver_.initialize(kSystem, uSystem);

			// solve
			solver_.solve(kSystem, rSystem, uSystem);

			// set # of iterations and residual
			setNumberOfIterations();
			setResidual();

			// clear solver
			solver_.clear();

			// set unknowns to structure
			status_ = "Writing output data...";

			// exception occurred during writing output data
			if (setUnknowns(uSystem) == false) {
				status_ = "Exception occurred during writing output data!";
				killed_ = true;
			}

			// no problem with writing
			else {

				// set analysis info to structure
				status_ = "Setting analysis info to model...";
				structure_.setAnalysisInfo(getAnalysisInfo());

				// analysis completed
				status_ = "Analysis complete.";
				completed_ = true;
			}
		}

		// solve failed
		catch (Exception excep) {

			// write message
			status_ = "Solve failed!";
			killed_ = true;
		}
	}

	/**
	 * Sets nodal unknowns to structure.
	 * 
	 * @param uSystem
	 *            System displacement vector.
	 */
	private boolean setUnknowns(DVec uSystem) {

		// create unknowns matrix
		double[][] unknowns = new double[eqn_][1];

		// set unknowns to matrix
		for (int i = 0; i < eqn_; i++)
			unknowns[i][0] = uSystem.get(i);

		// set matrix to structure
		return structure_.setUnknowns(path_, unknowns, 1);
	}

	/**
	 * Computes and returns half-bandwidth of the system stiffness matrix.
	 * 
	 * @return The half-bandwidth of the system stiffness matrix.
	 */
	private int computeHalfBandWidth() {

		// initialize half-bandwidth
		int max = -1;

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get dof numbers of element
			int[] dof = structure_.getElement(i).getDofNumbers();

			// loop over rows of element stiffness matrix
			for (int j = 0; j < dof.length - 1; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// loop over columns of element stiffness matrix
					for (int k = j + 1; k < dof.length; k++) {

						// check if dof is free
						if (dof[k] != -1) {

							// compute the difference between global dofs
							if (max < Math.abs(dof[j] - dof[k]))
								max = Math.abs(dof[j] - dof[k]);
						}
					}
				}
			}
		}

		// return half-bandwidth
		return max;
	}

	/**
	 * Sets number of iterations, if an iterative solver is used.
	 * 
	 */
	private void setNumberOfIterations() {

		// iterative solver
		if (solver_.getSolutionType() == LESolver.iterative_) {

			// cast solver
			Solver0 solver = (Solver0) solver_;

			// return number of iterations
			iterations_ = solver.getIterations();
		}
	}

	/**
	 * Sets residual, if an iterative solver is used.
	 * 
	 */
	private void setResidual() {

		// iterative solver
		if (solver_.getSolutionType() == LESolver.iterative_) {

			// cast solver
			Solver0 solver = (Solver0) solver_;

			// return residual
			residual_ = solver.getResidual();
		}
	}
}
