package data;

import java.io.Serializable;
import java.util.Vector;

import section.Section;
import node.LocalAxis;
import node.NodalMass;
import node.NodalSpring;
import material.Material;
import boundary.*;
import analysis.Analysis;
import element.ElementLibrary;
import solver.*;
import element.ElementMass;
import element.ElementSpring;
import math.Function;

/**
 * Class for input data object.
 * 
 * @author Murat
 * 
 */
public class InputData implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Vector for storing group model input objects. */
	private Vector<Group> group_ = new Vector<Group>();

	/** Vector for storing function input objects. */
	private Vector<Function> function_ = new Vector<Function>();

	/** Vector for storing element library input objects. */
	private Vector<ElementLibrary> elementLib_ = new Vector<ElementLibrary>();

	/** Vector for storing solver library input objects. */
	private Vector<Solver> solver_ = new Vector<Solver>();

	/** Vector for storing material input objects. */
	private Vector<Material> material_ = new Vector<Material>();

	/** Vector for storing nodal spring input objects. */
	private Vector<NodalSpring> nodalSpring_ = new Vector<NodalSpring>();

	/** Vector for storing element spring input objects. */
	private Vector<ElementSpring> elementSpring_ = new Vector<ElementSpring>();

	/** Vector for storing nodal mass input objects. */
	private Vector<NodalMass> nodalMass_ = new Vector<NodalMass>();

	/** Vector for storing element mass input objects. */
	private Vector<ElementMass> elementMass_ = new Vector<ElementMass>();

	/** Vector for storing constraint input objects. */
	private Vector<Constraint> constraint_ = new Vector<Constraint>();

	/** Vector for storing nodal mechanical load input objects. */
	private Vector<NodalMechLoad> nodalMechLoad_ = new Vector<NodalMechLoad>();

	/** Vector for storing element mechanical load input objects. */
	private Vector<ElementMechLoad> elementMechLoad_ = new Vector<ElementMechLoad>();

	/** Vector for storing prescribed displacement input objects. */
	private Vector<DispLoad> dispLoad_ = new Vector<DispLoad>();

	/** Vector for storing initial displacement input objects. */
	private Vector<InitialDisp> initialDisp_ = new Vector<InitialDisp>();

	/** Vector for storing initial velocity input objects. */
	private Vector<InitialVelo> initialVelo_ = new Vector<InitialVelo>();

	/** Vector for storing local axis input objects. */
	private Vector<LocalAxis> localAxis_ = new Vector<LocalAxis>();

	/** Vector for storing section input objects. */
	private Vector<Section> section_ = new Vector<Section>();

	/** Vector for storing boundary case input objects. */
	private Vector<BoundaryCase> boundaryCase_ = new Vector<BoundaryCase>();

	/** Vector for storing analysis case input objects. */
	private Vector<Analysis> analysis_ = new Vector<Analysis>();

	/** Vector for storing element temperature load input objects. */
	private Vector<ElementTemp> elementTemp_ = new Vector<ElementTemp>();

	/**
	 * Constructs input data object, sets default input data.
	 */
	public InputData() {

		// set default input data
		setDefault();
	}

	/**
	 * Sets element library input object vector.
	 * 
	 * @param elementLib
	 *            Element library input object vector.
	 */
	public void setElementLibrary(Vector<ElementLibrary> elementLib) {
		elementLib_ = elementLib;
	}

	/**
	 * Sets solver input object vector.
	 * 
	 * @param solver
	 *            Solver input object vector.
	 */
	public void setSolver(Vector<Solver> solver) {
		solver_ = solver;
	}

	/**
	 * Sets group model input object vector.
	 * 
	 * @param group
	 *            Group model input object vector.
	 */
	public void setGroup(Vector<Group> group) {
		group_ = group;
	}

	/**
	 * Sets material input object vector.
	 * 
	 * @param material
	 *            Material input object vector.
	 */
	public void setMaterial(Vector<Material> material) {
		material_ = material;
	}

	/**
	 * Sets constraint input object vector.
	 * 
	 * @param constraint
	 *            Constraint input object vector.
	 */
	public void setConstraint(Vector<Constraint> constraint) {
		constraint_ = constraint;
	}

	/**
	 * Sets nodal mechanical load input object vector.
	 * 
	 * @param nodalMechLoad
	 *            Nodal mechanical load input object vector.
	 */
	public void setNodalMechLoad(Vector<NodalMechLoad> nodalMechLoad) {
		nodalMechLoad_ = nodalMechLoad;
	}

	/**
	 * Sets element mass input object vector.
	 * 
	 * @param elementMass
	 *            Element mass input object vector.
	 */
	public void setElementMass(Vector<ElementMass> elementMass) {
		elementMass_ = elementMass;
	}

	/**
	 * Sets nodal nodal mass input object vector.
	 * 
	 * @param nodalMass
	 *            Nodal mass input object vector.
	 */
	public void setNodalMass(Vector<NodalMass> nodalMass) {
		nodalMass_ = nodalMass;
	}

	/**
	 * Sets nodal nodal spring input object vector.
	 * 
	 * @param nodalSpring
	 *            Nodal spring input object vector.
	 */
	public void setNodalSpring(Vector<NodalSpring> nodalSpring) {
		nodalSpring_ = nodalSpring;
	}

	/**
	 * Sets nodal element spring input object vector.
	 * 
	 * @param elementSpring
	 *            Element spring input object vector.
	 */
	public void setElementSpring(Vector<ElementSpring> elementSpring) {
		elementSpring_ = elementSpring;
	}

	/**
	 * Sets element mechanical load input object vector.
	 * 
	 * @param elementMechLoad
	 *            Element mechanical load input object vector.
	 */
	public void setElementMechLoad(Vector<ElementMechLoad> elementMechLoad) {
		elementMechLoad_ = elementMechLoad;
	}

	/**
	 * Sets element temperature load input object vector.
	 * 
	 * @param elementTemp
	 *            Element temperature load input object vector.
	 */
	public void setElementTemp(Vector<ElementTemp> elementTemp) {
		elementTemp_ = elementTemp;
	}

	/**
	 * Sets local axis input object vector.
	 * 
	 * @param localAxis
	 *            Local axis input object vector.
	 */
	public void setLocalAxis(Vector<LocalAxis> localAxis) {
		localAxis_ = localAxis;
	}

	/**
	 * Sets section input object vector.
	 * 
	 * @param section
	 *            Section input object vector.
	 */
	public void setSection(Vector<Section> section) {
		section_ = section;
	}

	/**
	 * Sets displacement load input object vector.
	 * 
	 * @param dispLoad
	 *            Diaplacement load input object vector.
	 */
	public void setDispLoad(Vector<DispLoad> dispLoad) {
		dispLoad_ = dispLoad;
	}

	/**
	 * Sets initial displacement input object vector.
	 * 
	 * @param initialDisp
	 *            Initial displacement input object vector.
	 */
	public void setInitialDisp(Vector<InitialDisp> initialDisp) {
		initialDisp_ = initialDisp;
	}

	/**
	 * Sets initial velocity input object vector.
	 * 
	 * @param initialVelo
	 *            Initial velocity input object vector.
	 */
	public void setInitialVelo(Vector<InitialVelo> initialVelo) {
		initialVelo_ = initialVelo;
	}

	/**
	 * Sets boundary case input object vector.
	 * 
	 * @param boundaryCase
	 *            Boundary case input object vector.
	 */
	public void setBoundaryCase(Vector<BoundaryCase> boundaryCase) {
		boundaryCase_ = boundaryCase;
	}

	/**
	 * Sets analysis case input object vector.
	 * 
	 * @param analysisCase
	 *            Analysis case input object vector.
	 */
	public void setAnalysis(Vector<Analysis> analysisCase) {
		analysis_ = analysisCase;
	}

	/**
	 * Sets function input object vector.
	 * 
	 * @param function
	 *            Function input object vector.
	 */
	public void setFunction(Vector<Function> function) {
		function_ = function;
	}

	/**
	 * Returns element library input object vector.
	 * 
	 * @return element library input object vector.
	 */
	public Vector<ElementLibrary> getElementLibrary() {
		return elementLib_;
	}

	/**
	 * Returns solver input object vector.
	 * 
	 * @return solver input object vector.
	 */
	public Vector<Solver> getSolver() {
		return solver_;
	}

	/**
	 * Returns group model input object vector.
	 * 
	 * @return group model input object vector.
	 */
	public Vector<Group> getGroup() {
		return group_;
	}

	/**
	 * Returns material input object vector.
	 * 
	 * @return material input object vector.
	 */
	public Vector<Material> getMaterial() {
		return material_;
	}

	/**
	 * Returns constraint input object vector.
	 * 
	 * @return constraint input object vector.
	 */
	public Vector<Constraint> getConstraint() {
		return constraint_;
	}

	/**
	 * Returns nodal mechanical load input object vector.
	 * 
	 * @return nodal mechanical load input object vector.
	 */
	public Vector<NodalMechLoad> getNodalMechLoad() {
		return nodalMechLoad_;
	}

	/**
	 * Returns nodal mass input object vector.
	 * 
	 * @return nodal mass input object vector.
	 */
	public Vector<NodalMass> getNodalMass() {
		return nodalMass_;
	}

	/**
	 * Returns element mass input object vector.
	 * 
	 * @return element mass input object vector.
	 */
	public Vector<ElementMass> getElementMass() {
		return elementMass_;
	}

	/**
	 * Returns nodal spring input object vector.
	 * 
	 * @return nodal spring input object vector.
	 */
	public Vector<NodalSpring> getNodalSpring() {
		return nodalSpring_;
	}

	/**
	 * Returns element spring input object vector.
	 * 
	 * @return element spring input object vector.
	 */
	public Vector<ElementSpring> getElementSpring() {
		return elementSpring_;
	}

	/**
	 * Returns element mechanical load input object vector.
	 * 
	 * @return element mechanical load input object vector.
	 */
	public Vector<ElementMechLoad> getElementMechLoad() {
		return elementMechLoad_;
	}

	/**
	 * Returns element temperature load input object vector.
	 * 
	 * @return element temperature load input object vector.
	 */
	public Vector<ElementTemp> getElementTemp() {
		return elementTemp_;
	}

	/**
	 * Returns local axis input object vector.
	 * 
	 * @return local axis input object vector.
	 */
	public Vector<LocalAxis> getLocalAxis() {
		return localAxis_;
	}

	/**
	 * Returns section input object vector.
	 * 
	 * @return section input object vector.
	 */
	public Vector<Section> getSection() {
		return section_;
	}

	/**
	 * Returns displacement load input object vector.
	 * 
	 * @return displacement load input object vector.
	 */
	public Vector<DispLoad> getDispLoad() {
		return dispLoad_;
	}

	/**
	 * Returns initial displacement input object vector.
	 * 
	 * @return initial displacement input object vector.
	 */
	public Vector<InitialDisp> getInitialDisp() {
		return initialDisp_;
	}

	/**
	 * Returns initial velocity input object vector.
	 * 
	 * @return initial velocity input object vector.
	 */
	public Vector<InitialVelo> getInitialVelo() {
		return initialVelo_;
	}

	/**
	 * Returns boundary case input object vector.
	 * 
	 * @return boundary case input object vector.
	 */
	public Vector<BoundaryCase> getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns analysis case input object vector.
	 * 
	 * @return analysis case input object vector.
	 */
	public Vector<Analysis> getAnalysis() {
		return analysis_;
	}

	/**
	 * Returns function input object vector.
	 * 
	 * @return function input object vector.
	 */
	public Vector<Function> getFunction() {
		return function_;
	}

	/**
	 * Returns True if no items other than the default values are valid.
	 * 
	 * @return True if no items other than the default values are valid.
	 */
	public boolean isEmpty() {

		// initialize items
		int items = 0;

		// add items
		items += group_.size();
		items += function_.size();
		items += elementLib_.size();
		items += material_.size();
		items += nodalSpring_.size();
		items += elementSpring_.size();
		items += nodalMass_.size();
		items += elementMass_.size();
		items += constraint_.size();
		items += nodalMechLoad_.size();
		items += elementMechLoad_.size();
		items += dispLoad_.size();
		items += initialDisp_.size();
		items += initialVelo_.size();
		items += localAxis_.size();
		items += section_.size();
		items += boundaryCase_.size();
		items += analysis_.size();
		items += elementTemp_.size();
		items += solver_.size();

		// empty
		if (items == 4)
			return true;
		else
			return false;
	}

	/**
	 * Sets default input data for initialization.
	 */
	private void setDefault() {

		// set default input data for boundary cases
		BoundaryCase default1 = new BoundaryCase("Case1");
		getBoundaryCase().add(default1);

		// set default input data for function
		Function default2 = new Function("Func1", Function.linear_);
		double[] param = { 0, 1.0, 0, 0 };
		default2.setParameters(param);
		getFunction().add(default2);

		// set default input data for solver
		Solver0 default3 = new Solver0("Solver1", Solver0.CG_, Solver0.DP_);
		Solver3 default4 = new Solver3("Solver2", 10, Math.pow(10, -6), 16);
		getSolver().add(default3);
		getSolver().add(default4);
	}
}
