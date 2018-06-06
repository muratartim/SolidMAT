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

import solver.LESolver;
import solver.Solver;
import solver.Solver0;

import boundary.BoundaryCase;

import node.Node;
import element.Element;

import math.Function;
import matrix.*;

/**
 * Class for linear tarnsient analysis.
 * 
 * @author Murat
 * 
 */
public class LinearTransient extends Analysis {

	private static final long serialVersionUID = 1L;

	/** Static variable for the direct integration method. */
	public static final int newmark_ = 0, wilson_ = 1;

	/**
	 * The number of equations to be solved, effective bandwidth and current
	 * time step.
	 */
	private int eqn_ = 0, hbw_ = 0, currentStep_ = 0;

	/** Mass and stiffness proportional coefficients (Bathe-p797). */
	private double[] proporCoeff_ = { 0.0, 0.0 };

	/** Newmark parameters. The first is alpha, the second is ro (Bathe-p781). */
	private double[] newmarkPar_ = { 0.25, 0.5 };

	/** Wilson parameter. Theta (Bathe-p778). */
	private double wilsonPar_ = 1.4;

	/** Time step size for integration scheme. */
	private double dt_ = 0.1;

	/** Number of time steps for integration scheme. */
	private int n_ = 10;

	/** The integration method of analysis. */
	private int integrationMethod_ = LinearTransient.newmark_;

	/** The time function for scaling the system load vector. */
	private Function loadTimeFunc_ = new Function("default", Function.linear_);

	/** The solver of analysis. */
	private LESolver solver_;

	/** # of iterations of iterative solver. */
	private Integer iterations_;

	/** Residual of iterative solver. */
	private Double residual_;

	/**
	 * Creates linear dynamic analysis.
	 * 
	 * @param name
	 *            The name of analysis.
	 */
	public LinearTransient(String name) {
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
	 * Sets time parameters for integration scheme.
	 * 
	 * @param n
	 *            Number of time steps.
	 * @param dt
	 *            Time step size.
	 */
	public void setTimeParameters(int n, double dt) {

		// check parameters
		if (n <= 0 || dt <= 0.0)
			exceptionHandler("Illegal time parameters for linear dynamic analysis!");

		// set parameters
		n_ = n;
		dt_ = dt;
	}

	/**
	 * Sets Newmark parameters (Bathe-p781).
	 * 
	 * @param alpha
	 *            The first Newmark parameter.
	 * @param ro
	 *            The second Newmark parameter.
	 */
	public void setNewmarkParameters(double alpha, double ro) {

		// check parameters
		if (ro < 0.5 || alpha < 0.25 * Math.pow((0.5 + ro), 2.0))
			exceptionHandler("Illegal Newmark parameters!");

		// set parameters
		newmarkPar_[0] = alpha;
		newmarkPar_[1] = ro;
	}

	/**
	 * Sets Wilson parameter (Bathe-p778).
	 * 
	 * @param theta
	 *            The Wilson parameter.
	 */
	public void setWilsonParameter(double theta) {

		// check parameter
		if (theta < 1.37)
			exceptionHandler("Illegal Wison parameter!");

		// set parameter
		wilsonPar_ = theta;
	}

	/**
	 * Sets mass and stiffness proportional coefficients. They are used to
	 * construct system damping matrix (Bathe-p797).
	 * 
	 * @param alpha
	 *            Mass proportional coefficient.
	 * @param beta
	 *            Stiffness proportional coefficient.
	 */
	public void setProportionalCoefficients(double alpha, double beta) {

		// check if negative
		if (alpha < 0 || beta < 0)
			exceptionHandler("Illegal parameters for linear dynamic analysis!");

		// set parameters
		proporCoeff_[0] = alpha;
		proporCoeff_[1] = beta;
	}

	/**
	 * Sets integration method to analysis.
	 * 
	 * @param method
	 *            The integration method to be set.
	 */
	public void setIntegrationMethod(int method) {

		// check method
		if (method < 0 || method > 2)
			exceptionHandler("Illegal integration method for linear dynamic analysis!");

		// set integration method
		integrationMethod_ = method;
	}

	/**
	 * Sets time function for scaling the system load vector during the
	 * analysis.
	 * 
	 * @param func
	 *            Time function to be set.
	 */
	public void setLoadTimeFunction(Function func) {

		// check if user defined
		if (func.getType() == Function.userDefined_) {
			try {

				// loop over time steps
				for (int i = 0; i < n_; i++)
					func.getValue(i * dt_ + dt_);
			}

			// problem occurred with the function
			catch (Exception e) {
				exceptionHandler("Incompatible time function for analysis!");
			}
		}

		// check if log or log10
		else if (func.getType() == Function.log_
				|| func.getType() == Function.log10_)
			exceptionHandler("Incompatible time function for analysis!");

		// set function
		loadTimeFunc_ = func;
	}

	/**
	 * Returns analysis properties. The sequence of information is; name
	 * (String), analysis type (int), boundary cases (String[]), boundary scales
	 * (double[]), solver (String), number of time steps (int), time step size
	 * (double), integration method (int), integration parameters (double[]),
	 * damping constants (double[]), load time function (Function).
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

		// get number of time steps -5
		prop.add(getNumberOfTimeSteps());

		// get time step size -6
		prop.add(getTimeStepSize());

		// get integration method -7
		prop.add(getIntegrationMethod());

		// get integration parameters -8
		double[] param = getNewmarkParameters();
		if (integrationMethod_ == LinearTransient.wilson_) {
			param = new double[1];
			param[0] = getWilsonParameter();
		}
		prop.add(param);

		// get damping constants -9
		prop.add(getProportionalCoefficients());

		// get load time function -10
		prop.add(getLoadTimeFunction());

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

	/**
	 * Returns the integration method of analysis.
	 * 
	 * @return The integration method of analysis.
	 */
	public int getIntegrationMethod() {
		return integrationMethod_;
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
	 * Returns current time step of analysis.
	 * 
	 * @return Current time step of analysis.
	 */
	public int getCurrentTimeStep() {
		return currentStep_;
	}

	/**
	 * Returns mass and stiffness proportional coefficients.
	 * 
	 * @return Array storing mass and stiffness proportional coefficients,
	 *         respectively.
	 */
	public double[] getProportionalCoefficients() {
		return proporCoeff_;
	}

	/**
	 * Returns Newmark parameters.
	 * 
	 * @return Array storing Newmark parameters. The first value is alpha, the
	 *         second is ro.
	 */
	public double[] getNewmarkParameters() {
		return newmarkPar_;
	}

	/**
	 * Returns Wilson parameter.
	 * 
	 * @return The Wilson parameter (Theta).
	 */
	public double getWilsonParameter() {
		return wilsonPar_;
	}

	/**
	 * Returns number of time steps used for time integration.
	 * 
	 * @return Number of time steps.
	 */
	public int getNumberOfTimeSteps() {
		return n_;
	}

	/**
	 * Returns time step size used for time integration.
	 * 
	 * @return Time step size.
	 */
	public double getTimeStepSize() {
		return dt_;
	}

	/**
	 * Returns the load time function.
	 * 
	 * @return Load time function.
	 */
	public Function getLoadTimeFunction() {
		return loadTimeFunc_;
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
	public int getType() {
		return Analysis.linearTransient_;
	}

	@Override
	public void analyze() {

		// initialize variables
		eqn_ = 0;
		hbw_ = 0;
		currentStep_ = -1;
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

			// assemble system mass matrix
			status_ = "Assembling system mass matrix...";
			SMat mSystem = null;
			if (solver_.getType() == Solver.solver0_)
				mSystem = new CDSMat(eqn_, 0);
			else if (solver_.getType() == Solver.solver1_)
				mSystem = new USB1Mat(eqn_, 0);
			else if (solver_.getType() == Solver.solver2_)
				mSystem = new USB2Mat(eqn_, 0);
			assembleMass(mSystem);

			// assemble system damping matrix
			SMat cSystem = null;
			if (proporCoeff_[0] != 0.0 || proporCoeff_[1] != 0.0) {
				status_ = "Assembling system damping matrix...";
				cSystem = mSystem.copy().scale(proporCoeff_[0]).add(
						kSystem.copy().scale(proporCoeff_[1]));
			}

			// assemble system load vector
			status_ = "Assembling system load vector...";
			DVec rSystem = new DVec(eqn_);
			assembleLoad(rSystem, kSystem);

			// assemble system initial displacement-velocity vectors
			status_ = "Assembling system initial vectors...";
			DVec uSystem = new DVec(eqn_);
			DVec uuSystem = new DVec(eqn_);
			assembleInitialVec(uSystem, uuSystem);

			// start of time integration
			status_ = "Time integration of system equations...";
			double[][] sol = new double[eqn_][n_];
			solve(kSystem, mSystem, cSystem, rSystem, uSystem, uuSystem, sol);
		}
	}

	/**
	 * Solves semi-discrete equation of motion by direct integration.
	 * 
	 * @param k
	 *            System stiffness matrix.
	 * @param m
	 *            System mass matrix.
	 * @param c
	 *            System damping matrix.
	 * @param r
	 *            System load vector.
	 * @param u
	 *            System initial displacement vector.
	 * @param uu
	 *            System initial velocity vector.
	 * @param sol
	 *            Solution matrix.
	 */
	private void solve(SMat k, SMat m, SMat c, DVec r, DVec u, DVec uu,
			double[][] sol) {

		try {

			// newmark method selected
			if (integrationMethod_ == LinearTransient.newmark_)
				newmark(k, m, c, r, u, uu, sol);

			// wilson method selected
			else if (integrationMethod_ == LinearTransient.wilson_)
				wilson(k, m, c, r, u, uu, sol);

			// clear solver
			solver_.clear();

			// set unknowns to structure
			status_ = "Writing output data...";

			// exception occurred during writing output data
			if (structure_.setUnknowns(path_, sol, n_) == false) {
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
	 * Solves semi-discrete equation of motion by Newmark direct integration
	 * method.
	 * 
	 * @param k
	 *            System stiffness matrix.
	 * @param m
	 *            System mass matrix.
	 * @param c
	 *            System damping matrix.
	 * @param r
	 *            System load vector.
	 * @param u
	 *            System initial displacement vector.
	 * @param uu
	 *            System initial velocity vector.
	 * @param sol
	 *            Solution matrix.
	 */
	private void newmark(SMat k, SMat m, SMat c, DVec r, DVec u, DVec uu,
			double[][] sol) {

		// initialize acceleration vector
		DVec uuu = r.scale(loadTimeFunc_.getValue(0.0));
		if (c == null)
			uuu = uuu.add(k.multiply(u).scale(-1.0));
		else
			uuu = uuu.add(c.multiply(uu).add(k.multiply(u)).scale(-1.0));

		// compute initial acceleration vector
		for (int i = 0; i < uuu.rowCount(); i++)
			uuu.set(i, uuu.get(i) / m.get(i, i));

		// compute integration constants
		double a0 = 1.0 / (newmarkPar_[0] * dt_ * dt_);
		double a1 = newmarkPar_[1] / (newmarkPar_[0] * dt_);
		double a2 = 1.0 / (newmarkPar_[0] * dt_);
		double a3 = 1.0 / (2.0 * newmarkPar_[0]) - 1.0;
		double a4 = newmarkPar_[1] / newmarkPar_[0] - 1.0;
		double a5 = dt_ / 2.0 * (newmarkPar_[1] / newmarkPar_[0] - 2.0);
		double a6 = dt_ * (1.0 - newmarkPar_[1]);
		double a7 = newmarkPar_[1] * dt_;

		// form effective stiffness matrix
		if (c == null)
			k.add(m.copy().scale(a0));
		else
			k.add(m.copy().scale(a0)).add(c.copy().scale(a1));

		// initialize solver
		solver_.initialize(k, u);

		// loop over time steps
		for (int i = 0; i < n_; i++) {

			// set current time step
			currentStep_ = i;

			// calculate effective load vector at time t + dt
			DVec temp1 = u.scale(a0).add(uu.scale(a2)).add(uuu.scale(a3));
			DVec rt = r.scale(loadTimeFunc_.getValue(i * dt_ + dt_));
			if (c == null)
				rt = rt.add(m.multiply(temp1));
			else {
				DVec temp2 = u.scale(a1).add(uu.scale(a4)).add(uuu.scale(a5));
				rt = rt.add(m.multiply(temp1)).add(c.multiply(temp2));
			}

			// solve for displacements at time t + dt
			DVec ut = new DVec(u.rowCount());
			solver_.solve(k, rt, ut);

			// set # of iterations and residual
			setNumberOfIterations();
			setResidual();

			// calculate accelerations and velocities at time t + dt
			DVec temp3 = ut.subtract(u).scale(a0);
			DVec uuut = temp3.subtract(uu.scale(a2)).subtract(uuu.scale(a3));
			uu = uu.add(uuu.scale(a6)).add(uuut.scale(a7));

			// replace displacement and acceleration vectors
			u = ut;
			uuu = uuut;

			// set displacement to solution matrix
			for (int j = 0; j < eqn_; j++)
				sol[j][i] = u.get(j);
		}
	}

	/**
	 * Solves semi-discrete equation of motion by Wilson direct integration
	 * method.
	 * 
	 * @param k
	 *            System stiffness matrix.
	 * @param m
	 *            System mass matrix.
	 * @param c
	 *            System damping matrix.
	 * @param r
	 *            System load vector.
	 * @param u
	 *            System initial displacement vector.
	 * @param uu
	 *            System initial velocity vector.
	 * @param sol
	 *            Solution matrix.
	 */
	private void wilson(SMat k, SMat m, SMat c, DVec r, DVec u, DVec uu,
			double[][] sol) {

		// initialize acceleration vector
		DVec uuu = r.scale(loadTimeFunc_.getValue(0.0));
		if (c == null)
			uuu = uuu.add(k.multiply(u).scale(-1.0));
		else
			uuu = uuu.add(c.multiply(uu).add(k.multiply(u)).scale(-1.0));

		// compute initial acceleration vector
		for (int i = 0; i < uuu.rowCount(); i++)
			uuu.set(i, uuu.get(i) / m.get(i, i));

		// compute integration constants
		double a0 = 6.0 / Math.pow(wilsonPar_ * dt_, 2.0);
		double a1 = 3.0 / (wilsonPar_ * dt_);
		double a2 = 2.0 * a1;
		double a3 = (wilsonPar_ * dt_) / 2.0;
		double a4 = a0 / wilsonPar_;
		double a5 = -a2 / wilsonPar_;
		double a6 = 1.0 - 3.0 / wilsonPar_;
		double a7 = dt_ / 2.0;
		double a8 = dt_ * dt_ / 6.0;

		// form effective stiffness matrix
		if (c == null)
			k.add(m.copy().scale(a0));
		else
			k.add(m.copy().scale(a0)).add(c.copy().scale(a1));

		// initialize solver
		solver_.initialize(k, u);

		// loop over time steps
		for (int i = 0; i < n_; i++) {

			// set current time step
			currentStep_ = i;

			// calculate effective load vector at time t + theta * dt
			DVec temp1 = u.scale(a0).add(uu.scale(a2)).add(uuu.scale(2.0));
			DVec temp2 = u.scale(a1).add(uu.scale(2.0)).add(uuu.scale(a3));
			DVec rt = r.scale(loadTimeFunc_.getValue(i * dt_));
			rt = rt.subtract(rt.scale(wilsonPar_));
			rt = rt.add(r.scale(loadTimeFunc_.getValue(i * dt_ + dt_)).scale(
					wilsonPar_));
			if (c == null)
				rt = rt.add(m.multiply(temp1));
			else
				rt = rt.add(m.multiply(temp1)).add(c.multiply(temp2));

			// solve for displacements at time t + theta * dt
			DVec ut = new DVec(u.rowCount());
			solver_.solve(k, rt, ut);

			// set # of iterations and residual
			setNumberOfIterations();
			setResidual();

			// calculate accelerations, velocities and displacements at time t +
			// theta * dt
			temp1 = ut.subtract(u).scale(a4);
			DVec uuut = temp1.add(uu.scale(a5)).add(uuu.scale(a6));
			DVec uut = uu.add(uuut.add(uuu).scale(a7));
			temp2 = uuut.add(uuu.scale(2.0)).scale(a8);
			u = u.add(uu.scale(dt_)).add(temp2);

			// replace velocities and accelerations
			uu = uut;
			uuu = uuut;

			// set displacement to solution matrix
			for (int j = 0; j < eqn_; j++)
				sol[j][i] = u.get(j);
		}
	}

	/**
	 * Assembles system initial displacement and velocity vectors.
	 * 
	 * @param uSystem
	 *            System initial displacement vector.
	 * @param uuSystem
	 *            System initial velocity vector.
	 */
	private void assembleInitialVec(DVec uSystem, DVec uuSystem) {

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// check if the node has initial displacement
			if (node.getInitialDisp().size() != 0) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get initial displacement vector of node
				DVec rn = node.getInitialDispVector();

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system displacement vector
						uSystem.add(dof[j], rn.get(j));
					}
				}
			}

			// check if the node has initial velocity
			if (node.getInitialVelo().size() != 0) {

				// get node's dof numbers array
				int[] dof = node.getDofNumbers();

				// get initial velocity vector of node
				DVec rn = node.getInitialVeloVector();

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// store into system velocity vector
						uuSystem.add(dof[j], rn.get(j));
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
