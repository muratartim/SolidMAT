package analysis;

import java.util.Vector;

import solver.GESolver;
import solver.Solver;

import boundary.BoundaryCase;

import node.Node;
import element.Element;

import matrix.*;

/**
 * Class for modal analysis.
 * 
 * @author Murat
 * 
 */
public class Modal extends Analysis {

	private static final long serialVersionUID = 1L;

	/** The number of equations to be solved and effective bandwidth. */
	private int eqn_ = 0, hbw_ = 0;

	/** The solver of analysis. */
	private GESolver solver_;

	/** Array storing the eigenvalues of analysis. */
	private double[] eigVal_;

	/**
	 * Creates modal analysis.
	 * 
	 * @param name
	 *            The name of analysis.
	 */
	public Modal(String name) {
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
	 * Returns array storing the natural frequencies.
	 * 
	 * @return Array storing the natural frequencies.
	 */
	public double[] getNaturalFrequencies() {
		double[] w = getEigenvalues();
		double[] nf = new double[w.length];
		for (int i = 0; i < nf.length; i++)
			if (w[i] >= 0)
				nf[i] = Math.sqrt(w[i]);
		return nf;
	}

	/**
	 * Returns array storing the cyclic frequencies.
	 * 
	 * @return Array storing the cyclic frequencies.
	 */
	public double[] getCyclicFrequencies() {
		double[] nf = getNaturalFrequencies();
		double[] cf = new double[nf.length];
		for (int i = 0; i < cf.length; i++)
			cf[i] = nf[i] / (2.0 * Math.PI);
		return cf;
	}

	/**
	 * Returns array storing the periods.
	 * 
	 * @return Array storing the periods.
	 */
	public double[] getPeriods() {
		double[] cf = getCyclicFrequencies();
		double[] p = new double[cf.length];
		for (int i = 0; i < p.length; i++)
			if (cf[i] > 0)
				p[i] = 1.0 / cf[i];
		return p;
	}

	/**
	 * Returns analysis properties. The sequence of information is; name
	 * (String), analysis type (int), boundary cases (String[]), solver
	 * (String), eigenvalues (double[]), natural frequencies (double[]), cyclic
	 * frequencies (double[]), periods (double[]).
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

		// get solver -3
		prop.add(getSolver().getName());

		// get eigenvalues -4
		prop.add(getEigenvalues());

		// get natural frequencies -5
		prop.add(getNaturalFrequencies());

		// get cyclic frequencies -6
		prop.add(getCyclicFrequencies());

		// get periods -7
		prop.add(getPeriods());

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
		return Analysis.modal_;
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
			if (solver_.getType() == Solver.solver3_)
				kSystem = new USB1Mat(eqn_, hbw_);
			else if (solver_.getType() == Solver.solver4_)
				kSystem = new USPMat(eqn_);
			assembleStiffness(kSystem);

			// assemble system mass matrix
			status_ = "Assembling system mass matrix...";
			SMat mSystem = null;
			if (solver_.getType() == Solver.solver3_)
				mSystem = new USB1Mat(eqn_, hbw_);
			else if (solver_.getType() == Solver.solver4_)
				mSystem = new USPMat(eqn_);
			assembleMass(mSystem);

			// start of eigenvalue problem solution
			status_ = "Solution of eigensystem...";
			solve(kSystem, mSystem);
		}
	}

	/**
	 * Performs solution of linear system of equations.
	 * 
	 * @param kSystem
	 *            System stiffness matrix.
	 * @param mSystem
	 *            System mass matrix.
	 */
	private void solve(Mat kSystem, Mat mSystem) {

		try {

			// initialize solver
			solver_.initialize(kSystem, mSystem);

			// create solution vector/matrix
			int nroot = solver_.getNumberOfRequiredEigenvalues();
			int nn = kSystem.rowCount();
			DVec eigVal = new DVec(nroot);
			GPSMat eigVec = new GPSMat(nn, nroot);

			// solve
			solver_.solve(kSystem, mSystem, eigVal, eigVec, nroot);

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
	 * Assembles system mass matrix. The system mass matrix is assumed to be
	 * diagonal (lumped).
	 * 
	 * @param mSystem
	 *            System mass matrix.
	 */
	private void assembleMass(Mat mSystem) {

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// get dof numbers of element
			int[] dof = e.getDofNumbers();

			// get mass matrix of element in nodal local coordinate system
			DMat me = e.getMassMatrix(Element.local_);

			// loop over rows of element mass matrix
			for (int j = 0; j < dof.length; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// store into system mass matrix
					mSystem.add(dof[j], dof[j], me.get(j, j));
				}
			}
		}

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// check if the node has mass
			if (node.getMasses() != null) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get mass matrix
				DMat mn = node.getMassMatrix();

				// loop over rows of nodal mass matrix
				for (int j = 0; j < dof.length; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system mass matrix
						mSystem.add(dof[j], dof[j], mn.get(j, j));
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
