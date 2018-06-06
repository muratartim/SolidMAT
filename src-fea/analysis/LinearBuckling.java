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

import solver.GESolver;
import solver.Solver;

import node.Node;
import element.Element;

import boundary.BoundaryCase;
import matrix.*;

/**
 * Class for linear buckling analysis.
 * 
 * @author Murat
 * 
 */
public class LinearBuckling extends Analysis {

	private static final long serialVersionUID = 1L;

	/** The number of equations to be solved and effective bandwidth. */
	private int eqn_ = 0, hbw_ = 0;

	/** The solver of analysis. */
	private GESolver solver_;

	/** Array storing the eigenvalues of analysis. */
	private double[] eigVal_;

	/**
	 * Creates linear buckling analysis.
	 * 
	 * @param name
	 *            The name of analysis.
	 */
	public LinearBuckling(String name) {
		name_ = name;
	}

	/**
	 * Sets solver to analysis.
	 * 
	 * @param solver
	 *            The solver to be set.
	 */
	public void setSolver(Solver solver) {
		solver_ = (GESolver) solver;
	}

	/**
	 * Returns the number of eqautions to be solved.
	 * 
	 * @return The number of equations.
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
	 * Returns the number of computed eigenvalues.
	 * 
	 * @return The number of computed eigenvalues.
	 */
	public int getNumberOfEigenvalues() {
		return eigVal_.length;
	}

	/**
	 * Returns computed eigenvalues.
	 * 
	 * @return Eigenvalues.
	 */
	public double[] getEigenvalues() {
		return eigVal_;
	}

	/**
	 * Returns analysis properties. The sequence of information is; name
	 * (String), analysis type (int), boundary cases (String[]), boundary scales
	 * (double[]), solver (String), eigenvalues (double[]).
	 * 
	 * @return Vector storing the analysis properties.
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

		// get eigenvalues -5
		prop.add(getEigenvalues());

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
		return Analysis.linearBuckling_;
	}

	@Override
	public void analyze() {

		// initialize variables
		eqn_ = 0;
		hbw_ = 0;
		killed_ = false;
		completed_ = false;
		status_ = null;
		eigVal_ = null;

		// no previous analysis has been performed
		if (structure_.getAnalysisInfo().size() == 0) {
			status_ = "A static analysis should be performed before the analysis!";
			killed_ = true;
		}

		// an analysis has been performed
		else {

			// get the type of analysis
			int type = (Integer) structure_.getAnalysisInfo().get(1);

			// not linear static analysis
			if (type != Analysis.linearStatic_) {
				status_ = "A static analysis should be performed before the analysis!";
				killed_ = true;
			}

			// linear static analysis has been performed
			else {

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

					// set step number to structure
					status_ = "Setting step number...";
					structure_.setStep(path_, 0);

					// enumerate degrees of freedom of structure
					status_ = "Enumerating dofs...";
					eqn_ = structure_.enumerateDofs(bCases_, bScales_);

					// compute effective bandwidth
					status_ = "Computing effective bandwidth...";
					hbw_ = computeHalfBandWidth();

					// assemble system stiffness matrix
					status_ = "Assembling system stiffness matrix...";
					SMat kSystem = null;
					if (solver_.getType() == Solver.solver3_)
						kSystem = new USB1Mat(eqn_, hbw_);
					else if (solver_.getType() == Solver.solver4_)
						kSystem = new USPMat(eqn_);
					assembleStiffness(kSystem);

					// assemble system stability matrix
					status_ = "Assembling system stability matrix...";
					SMat gSystem = null;
					if (solver_.getType() == Solver.solver3_)
						gSystem = new USB1Mat(eqn_, hbw_);
					else if (solver_.getType() == Solver.solver4_)
						gSystem = new USPMat(eqn_);
					assembleStability(gSystem);

					// initialize structure
					status_ = "Initializing model...";
					structure_.initialize();

					// start of eigenvalue problem solution
					status_ = "Solution of eigensystem...";
					solve(kSystem, gSystem);
				}
			}
		}
	}

	/**
	 * Performs solution of linear system of equations.
	 * 
	 * @param kSystem
	 *            System stiffness matrix.
	 * @param gSystem
	 *            System stability matrix.
	 */
	private void solve(Mat kSystem, Mat gSystem) {

		try {

			// initialize solver
			solver_.initialize(kSystem, gSystem);

			// create solution vector/matrix
			int nroot = solver_.getNumberOfRequiredEigenvalues();
			int nn = kSystem.rowCount();
			DVec eigVal = new DVec(nroot);
			GPSMat eigVec = new GPSMat(nn, nroot);

			// solve
			solver_.solve(kSystem, gSystem, eigVal, eigVec, nroot);

			// set unknowns to structure
			status_ = "Writing output data...";

			// exception occurred during writing output data
			if (structure_.setUnknowns(path_, eigVec.getData(), nroot) == false) {
				status_ = "Exception occurred during writing output data!";
				killed_ = true;
			}

			// no problem with writing
			else {

				// set eigenvalues
				eigVal_ = eigVal.get1DArray();

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
	 * Assembles system stability matrix.
	 * 
	 * @param gSystem
	 *            System stability matrix.
	 */
	private void assembleStability(Mat gSystem) {

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// get dof numbers of element
			int[] dof = e.getDofNumbers();

			// get stability matrix of element in nodal local coordinate system
			DMat ge = e.getStabilityMatrix(Node.local_);

			// loop over rows of element stability matrix
			for (int j = 0; j < dof.length; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// loop over columns of element stability matrix
					for (int k = 0; k < dof.length; k++) {

						// check if dof is free
						if (dof[k] != -1) {

							// store into system stability matrix
							gSystem.add(dof[j], dof[k], ge.get(j, k));
						}
					}
				}
			}
		}
	}

	/**
	 * Assembles system stiffness matrix. Homogeneous Dirichlet boundaries are
	 * imposed during the assembly of system stiffness matrix.
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
			DMat ke = e.getStiffnessMatrix(Node.local_);

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
		}
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
}
