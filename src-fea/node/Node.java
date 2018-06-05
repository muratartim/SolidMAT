package node;

import java.io.Serializable;
import java.util.Vector;

import matrix.DMat;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.Constraint;
import boundary.DispLoad;
import boundary.NodalMechLoad;
import boundary.InitialDisp;
import boundary.InitialVelo;

/**
 * Class for Node.
 * 
 * @author Murat
 * 
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of node. */
	public static final int global_ = 0, local_ = 1;

	/** The position vector of node. */
	private double[] position_ = new double[3];

	/** The local axis system of node. */
	private LocalAxis localAxis_;

	/** The constraint of node. */
	private Constraint constraint_;

	/** The transformation matrix of node. */
	private double[][] trans_;

	/** Vector for storing mechanical loads of node. */
	private Vector<NodalMechLoad> mechLoads_;

	/** Vector for storing displacement loads of node. */
	private Vector<DispLoad> dispLoads_;

	/** Vector for storing initial displacement of node. */
	private Vector<InitialDisp> initialDisp_;

	/** Vector for storing initial velocity of node. */
	private Vector<InitialVelo> initialVelo_;

	/** Vector for storing springs of node. */
	private Vector<NodalSpring> springs_;

	/** Vector for storing masses of node. */
	private Vector<NodalMass> masses_;

	/** The local unknown vector of node. */
	private double[] unknown_;

	/** The local reaction force vector of node. */
	private double[] reactionForce_;

	/** Array for storing avaible dofs of node. */
	private int[] availableDofs_;

	/** The degree of freedom numbers array of node */
	private int[] dofNumbers_;

	/** Vector for storing the boundary cases to compute nodal quantities. */
	private Vector<BoundaryCase> bCases_;

	/** The scaling factors of boundary cases. */
	private double[] bScales_;

	/**
	 * Creates Node.
	 * 
	 * @param x
	 *            The x coordinate of node.
	 * @param y
	 *            The y coordinate of node.
	 * @param z
	 *            The z coordinate node.
	 */
	public Node(double x, double y, double z) {
		position_[0] = x;
		position_[1] = y;
		position_[2] = z;
	}

	/**
	 * Sets position vector to node.
	 * 
	 * @param pos
	 *            The position vector to be set.
	 */
	public void setPosition(DVec pos) {

		// check dimensions
		if (pos.rowCount() != 3)
			exceptionHandler("Illegal dimension for nodal position vector!");

		// set position vector
		position_ = pos.get1DArray();
	}

	/**
	 * Sets available dofs to the node.
	 * 
	 * @param dofs
	 *            Array storing the dofs of the node. Dofs given as -1 are
	 *            considered as unavailable.
	 */
	public void setAvailableDofs(int[] dofs) {

		// check dimension of array
		if (dofs.length != 6)
			exceptionHandler("Illegal dimension for available dofs!");

		// set dofs
		availableDofs_ = dofs;
	}

	/**
	 * Sets constraint to node.
	 * 
	 * @param constraint
	 *            Constraint to be applied.
	 */
	public void setConstraint(Constraint constraint) {
		constraint_ = constraint;
	}

	/**
	 * Sets local axes to node.
	 * 
	 * @param localAxis
	 *            The local axis of node.
	 */
	public void setLocalAxis(LocalAxis localAxis) {

		// null assignment
		if (localAxis == null){
			localAxis_ = localAxis;
			trans_ = null;
		}

		// other assignments
		else {

			// check type of local axis
			if (localAxis.getType() != LocalAxis.point_)
				exceptionHandler("Illegal local axis type for node!");

			// set local axis
			localAxis_ = localAxis;

			// get rotation angles of local axis
			double[] angles = localAxis.getValues();

			// construct transformation matrix
			DMat tr = new DMat(angles[0], angles[1], angles[2], DMat.xyz_);
			DMat trans = new DMat(6);
			trans = trans.setSubMatrix(tr, 0, 0);
			trans = trans.setSubMatrix(tr, 3, 3);
			trans_ = trans.get2DArray();
		}
	}

	/**
	 * Sets boundary cases and their scaling factors to element for computing
	 * nodal quantities.
	 * 
	 * @param bCases
	 *            The vector storing the demanded boundary cases of analysis.
	 * @param bScales
	 *            The scaling factors of boundary cases.
	 */
	public void setBoundaryCases(Vector<BoundaryCase> bCases, double[] bScales) {
		bCases_ = bCases;
		bScales_ = bScales;
	}

	/**
	 * Sets transformation matrix to node. This method can be used as an
	 * alternative to setLocalAxis() method.
	 * 
	 * @param tr3D
	 *            The three dimensional transformation matrix to be set.
	 */
	public void setTransformation(DMat tr3D) {

		// check dimensions
		if (tr3D.rowCount() != 3 && tr3D.columnCount() != 3)
			exceptionHandler("Illegal dimensions of nodal transformation matrix!");

		// set to nodal transformation matrix
		DMat trans = new DMat(6);
		trans = trans.setSubMatrix(tr3D, 0, 0);
		trans = trans.setSubMatrix(tr3D, 3, 3);
		trans_ = trans.get2DArray();
	}

	/**
	 * Sets nodal unknowns (displacements and rotations).
	 * 
	 * @param unknown
	 *            The unknown vector of node. The first three components are
	 *            displacements and the last three components are rotations.
	 */
	public void setUnknown(double[] unknown) {
		unknown_ = unknown;
	}

	/**
	 * Sets nodal reaction forces.
	 * 
	 * @param reaction
	 *            The reaction force vector of node.
	 */
	public void setReactionForce(double[] reaction) {
		reactionForce_ = reaction;
	}

	/**
	 * Sets mechanical loads to node.
	 * 
	 * @param mechLoads
	 *            Vector storing the nodal mechanical loads.
	 */
	public void setMechLoads(Vector<NodalMechLoad> mechLoads) {
		mechLoads_ = mechLoads;
	}

	/**
	 * Sets displacement loads to node.
	 * 
	 * @param dispLoads
	 *            Vector storing the nodal displacement loads.
	 */
	public void setDispLoads(Vector<DispLoad> dispLoads) {
		dispLoads_ = dispLoads;
	}

	/**
	 * Sets initial displacement to node.
	 * 
	 * @param initialDisp
	 *            Vector storing the nodal initial displacements.
	 */
	public void setInitialDisp(Vector<InitialDisp> initialDisp) {
		initialDisp_ = initialDisp;
	}

	/**
	 * Sets initial velocity to node.
	 * 
	 * @param initialVelo
	 *            Vector storing the nodal initial velocities.
	 */
	public void setInitialVelo(Vector<InitialVelo> initialVelo) {
		initialVelo_ = initialVelo;
	}

	/**
	 * Sets springs to node.
	 * 
	 * @param springs
	 *            Vector storing the nodal springs.
	 */
	public void setSprings(Vector<NodalSpring> springs) {
		springs_ = springs;
	}

	/**
	 * Sets masses to node.
	 * 
	 * @param masses
	 *            Vector storing the nodal masses.
	 */
	public void setMasses(Vector<NodalMass> masses) {
		masses_ = masses;
	}

	/**
	 * Adds reaction forces to node.
	 * 
	 * @param force
	 *            The reaction force vector of node. The first three components
	 *            are forces and the last three components are moments.
	 */
	public void addReactionForce(DVec force) {

		// initialize reaction force array
		DVec reac = null;

		// no reaction force available
		if (reactionForce_ == null)
			reac = new DVec(6);

		// reaction force available
		else
			reac = new DVec(reactionForce_);

		// set to node
		reactionForce_ = reac.add(force).get1DArray();
	}

	/**
	 * Changes the value of contiunation index depending on the constraint
	 * applied and returns the continuation index.
	 * 
	 * @param start
	 *            The continuation index of nodal dofs.
	 * @return The continuation index.
	 */
	public int enumerateDofs(int start) {

		// initialize dof numbers array
		dofNumbers_ = new int[6];

		// no constraints applied or constraint is not a boundary case
		if (getConstraint() == null) {

			// loop over degrees of freedom
			for (int i = 0; i < dofNumbers_.length; i++) {

				// dof is not available
				if (availableDofs_[i] == -1)
					dofNumbers_[i] = availableDofs_[i];

				// dof is available
				else {

					// give start to dof numbers and increase it by 1
					dofNumbers_[i] = start;
					start++;
				}
			}
		}

		// constraint applied
		else {

			// loop over degrees of freedom
			for (int i = 0; i < dofNumbers_.length; i++) {

				// dof is not available
				if (availableDofs_[i] == -1)
					dofNumbers_[i] = availableDofs_[i];

				// dof is available
				else {

					// free
					if (constraint_.getConstraints()[i]) {

						// give start to dof numbers and increase it by 1
						dofNumbers_[i] = start;
						start++;
					}

					// constrained
					else {

						// give -1 to dof
						dofNumbers_[i] = -1;
					}
				}
			}
		}

		// return continuation index
		return start;
	}

	/**
	 * Returns the local axis system object of node.
	 * 
	 * @return The local axis system object of node.
	 */
	public LocalAxis getLocalAxis() {
		return localAxis_;
	}

	/**
	 * Returns the position vector of node.
	 * 
	 * @return The position vector of node.
	 */
	public DVec getPosition() {
		return new DVec(position_);
	}

	/**
	 * Returns applied constraint of node.
	 * 
	 * @return The applied constraint of node.
	 */
	public Constraint getAppliedConstraint() {
		return constraint_;
	}

	/**
	 * Returns constraint of node.
	 * 
	 * @return The constraint of node.
	 */
	public Constraint getConstraint() {

		// constraint is a boundary case
		if (bCases_ != null && constraint_ != null)
			if (bCases_.contains(constraint_.getBoundaryCase()))
				return constraint_;

		// constraint is not a boundary case
		return null;
	}

	/**
	 * Returns the unknown vector of node (displacements and rotations).
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of the node.
	 * 
	 * @return The unknown vector of node.
	 */
	public DVec getUnknown(int coordinateSystem) {

		// check coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for nodal unknowns!");

		// unknowns in local coordinates demanded
		if (coordinateSystem == Node.local_) {

			// no unknowns available
			if (unknown_ == null)
				return new DVec(6);

			// unknowns available
			else
				return new DVec(unknown_);
		}

		// unknowns in global coordinates demanded
		else {

			// default transformation
			if (trans_ == null) {

				// no unknowns available
				if (unknown_ == null)
					return new DVec(6);

				// unknowns available
				else
					return new DVec(unknown_);
			}

			// customized transformation
			else {

				// no unknowns available
				if (unknown_ == null)
					return new DVec(6);

				// unknowns available
				else
					return new DVec(unknown_).transform(new DMat(trans_),
							DMat.toGlobal_);
			}
		}
	}

	/**
	 * Returns the reaction force vector of node.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of the node.
	 * 
	 * @return The reaction force vector of node.
	 */
	public DVec getReactionForce(int coordinateSystem) {

		// check coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for nodal reaction force!");

		// reaction forces in local coordinates demanded
		if (coordinateSystem == Node.local_) {

			// no reaction force available
			if (reactionForce_ == null)
				return new DVec(6);

			// reaction force available
			else
				return new DVec(reactionForce_);
		}

		// reaction forces in global coordinates demanded
		else {

			// default transformation
			if (trans_ == null) {

				// no reaction force available
				if (reactionForce_ == null)
					return new DVec(6);

				// reaction force available
				else
					return new DVec(reactionForce_);
			}

			// customized transformation
			else {

				// no reaction force available
				if (reactionForce_ == null)
					return new DVec(6);

				// reaction force available
				else
					return new DVec(reactionForce_).transform(new DMat(trans_),
							DMat.toGlobal_);
			}
		}
	}

	/**
	 * Returns the transformation matrix of node.
	 * 
	 * @return The transformation matrix of node.
	 */
	public DMat getTransformation() {

		// default transformation
		if (trans_ == null)
			return new DMat(6);

		// customized transformation
		else
			return new DMat(trans_);
	}

	/**
	 * Returns the springs of node.
	 * 
	 * @return The springs of node.
	 */
	public Vector<NodalSpring> getSprings() {
		return springs_;
	}

	/**
	 * Returns all applied mechanical loads.
	 * 
	 * @return The nodal mechanical loads.
	 */
	public Vector<NodalMechLoad> getAllMechLoads() {
		return mechLoads_;
	}

	/**
	 * Returns the mechanical loads of node for the demanded boundary cases.
	 * 
	 * @return The mechanical loads of node.
	 */
	public Vector<NodalMechLoad> getMechLoads() {

		// create mechanical load vector
		Vector<NodalMechLoad> ml = new Vector<NodalMechLoad>();

		// check if any mechanical load available
		if (mechLoads_ != null) {

			// loop over element mechanical loads
			for (int i = 0; i < mechLoads_.size(); i++) {

				// get mechanical load
				NodalMechLoad load = mechLoads_.get(i);

				// get boundary case of mechanical load
				BoundaryCase bc = load.getBoundaryCase();

				// boundary cases contains mechanical load
				if (bCases_ != null) {
					if (bCases_.contains(bc)) {

						// get scaling factor of boundary case and scale load
						double factor = bScales_[bCases_.indexOf(bc)];
						load.setLoadingScale(factor);

						// add
						ml.add(load);
					}
				}
			}
		}
		return ml;
	}

	/**
	 * Returns all applied displacement loads.
	 * 
	 * @return The nodal displacement loads.
	 */
	public Vector<DispLoad> getAllDispLoads() {
		return dispLoads_;
	}

	/**
	 * Returns the displacement loads of node for the demanded boundary cases.
	 * 
	 * @return The displacement loads of node.
	 */
	public Vector<DispLoad> getDispLoads() {

		// create displacement load vector
		Vector<DispLoad> ml = new Vector<DispLoad>();

		// check if any disp load available
		if (dispLoads_ != null) {

			// loop over node displacement loads
			for (int i = 0; i < dispLoads_.size(); i++) {

				// get displacement load
				DispLoad load = dispLoads_.get(i);

				// get boundary case of displacement load
				BoundaryCase bc = load.getBoundaryCase();

				// boundary cases contains displacement load
				if (bCases_ != null) {
					if (bCases_.contains(bc)) {

						// get scaling factor of boundary case and scale load
						double factor = bScales_[bCases_.indexOf(bc)];
						load.setLoadingScale(factor);

						// add
						ml.add(load);
					}
				}
			}
		}
		return ml;
	}

	/**
	 * Returns all applied initial displacements.
	 * 
	 * @return The nodal initial displacements.
	 */
	public Vector<InitialDisp> getAllInitialDisp() {
		return initialDisp_;
	}

	/**
	 * Returns the initial displacements of node for the demanded boundary
	 * cases.
	 * 
	 * @return The initial displacements of node.
	 */
	public Vector<InitialDisp> getInitialDisp() {

		// create initial displacement vector
		Vector<InitialDisp> ml = new Vector<InitialDisp>();

		// check if any initial disp available
		if (initialDisp_ != null) {

			// loop over node initial displacements
			for (int i = 0; i < initialDisp_.size(); i++) {

				// get initial displacements
				InitialDisp load = initialDisp_.get(i);

				// get boundary case of initial displacements
				BoundaryCase bc = load.getBoundaryCase();

				// boundary cases contains initial displacements
				if (bCases_ != null) {
					if (bCases_.contains(bc)) {

						// get scaling factor of boundary case and scale
						// displacements
						double factor = bScales_[bCases_.indexOf(bc)];
						load.setLoadingScale(factor);

						// add
						ml.add(load);
					}
				}
			}
		}
		return ml;
	}

	/**
	 * Returns all applied initial velocities.
	 * 
	 * @return The nodal initial velocities.
	 */
	public Vector<InitialVelo> getAllInitialVelo() {
		return initialVelo_;
	}

	/**
	 * Returns the initial velocities of node for the demanded boundary cases.
	 * 
	 * @return The initial velocities of node.
	 */
	public Vector<InitialVelo> getInitialVelo() {

		// create initial velocity vector
		Vector<InitialVelo> ml = new Vector<InitialVelo>();

		// check if any initial velo available
		if (initialVelo_ != null) {

			// loop over node initial velocities
			for (int i = 0; i < initialVelo_.size(); i++) {

				// get initial velocities
				InitialVelo load = initialVelo_.get(i);

				// get boundary case of initial velocities
				BoundaryCase bc = load.getBoundaryCase();

				// boundary cases contains initial velocities
				if (bCases_ != null) {
					if (bCases_.contains(bc)) {

						// get scaling factor of boundary case and scale
						// displacements
						double factor = bScales_[bCases_.indexOf(bc)];
						load.setLoadingScale(factor);

						// add
						ml.add(load);
					}
				}
			}
		}
		return ml;
	}

	/**
	 * Returns the masses of node.
	 * 
	 * @return The masses of node.
	 */
	public Vector<NodalMass> getMasses() {
		return masses_;
	}

	/**
	 * Returns the mechanical load vector of node in local coordinate system.
	 * 
	 * @return The mechanical load vector of node.
	 */
	public DVec getMechLoadVector() {

		// setup nodal mechanical load vector
		DVec vec = new DVec(6);

		// check if any mechanical load available
		if (mechLoads_ != null) {

			// loop over mechanical loads
			for (int i = 0; i < mechLoads_.size(); i++) {

				// get mechanical load
				NodalMechLoad l = mechLoads_.get(i);

				// get mechanical load vector
				DVec ml = l.getComponents();

				// transform if mechanical load is in global coordinates
				if (l.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					ml = ml.transform(tr, DMat.toLocal_);
				}

				// add to nodal mechanical load vector
				vec = vec.add(ml);
			}
		}

		// return nodal mechanical load vector
		return vec;
	}

	/**
	 * Returns the displacement load vector of node in local coordinates.
	 * 
	 * @return The displacement load vector of node.
	 */
	public DVec getDispLoadVector() {

		// setup nodal displacement load vector
		DVec vec = new DVec(6);

		// check if any disp load available
		if (dispLoads_ != null) {

			// loop over displacement loads
			for (int i = 0; i < dispLoads_.size(); i++) {

				// get displacement load
				DispLoad l = dispLoads_.get(i);

				// get displacement load vector and scale with factor
				DVec dl = l.getComponents();

				// transform if displacement load is in global coordinates
				if (l.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					dl = dl.transform(tr, DMat.toLocal_);
				}

				// add to nodal displacement load vector
				vec = vec.add(dl);
			}
		}

		// return nodal displacement load vector
		return vec;
	}

	/**
	 * Returns the initial displacement vector of node in local coordinates.
	 * 
	 * @return The initial displacement vector of node.
	 */
	public DVec getInitialDispVector() {

		// setup nodal initial displacement vector
		DVec vec = new DVec(6);

		// check if any initial disp available
		if (initialDisp_ != null) {

			// loop over initial displacements
			for (int i = 0; i < initialDisp_.size(); i++) {

				// get initial displacement
				InitialDisp l = initialDisp_.get(i);

				// get initial displacement vector and scale with factor
				DVec dl = l.getComponents();

				// transform if initial displacement is in global coordinates
				if (l.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					dl = dl.transform(tr, DMat.toLocal_);
				}

				// add to nodal initial displacement vector
				vec = vec.add(dl);
			}
		}

		// return nodal initial displacement vector
		return vec;
	}

	/**
	 * Returns the initial velocity vector of node in local coordinates.
	 * 
	 * @return The initial velocity vector of node.
	 */
	public DVec getInitialVeloVector() {

		// setup nodal initial velocity vector
		DVec vec = new DVec(6);

		// check if any initial velo available
		if (initialVelo_ != null) {

			// loop over initial velocities
			for (int i = 0; i < initialVelo_.size(); i++) {

				// get initial velocity
				InitialVelo l = initialVelo_.get(i);

				// get initial velocity vector and scale with factor
				DVec dl = l.getComponents();

				// transform if initial velocity is in global coordinates
				if (l.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					dl = dl.transform(tr, DMat.toLocal_);
				}

				// add to nodal initial velocity vector
				vec = vec.add(dl);
			}
		}

		// return nodal initial velocity vector
		return vec;
	}

	/**
	 * Returns the stiffness matrix of node in local coordinates.
	 * 
	 * @return The stiffness matrix of node.
	 */
	public DMat getStiffnessMatrix() {

		// setup stiffness matrix
		DMat stiffness = new DMat(6, 6);

		// check if any spring available
		if (springs_ != null) {

			// loop over nodal springs
			for (int i = 0; i < springs_.size(); i++) {

				// get spring
				NodalSpring s = springs_.get(i);

				// get spring stiffness
				DMat ks = s.getStiffness();

				// transform if spring is in global coordinates
				if (s.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					ks = ks.transform(tr, DMat.toLocal_);
				}

				// add to nodal stiffness
				stiffness = stiffness.add(ks);
			}
		}

		// return nodal stiffness
		return stiffness;
	}

	/**
	 * Returns the mass matrix of node in local coordinates.
	 * 
	 * @return The mass matrix of node.
	 */
	public DMat getMassMatrix() {

		// setup mass matrix
		DMat mass = new DMat(6, 6);

		// check if any mass available
		if (masses_ != null) {

			// loop over nodal masses
			for (int i = 0; i < masses_.size(); i++) {

				// get mass
				NodalMass m = masses_.get(i);

				// get mass matrix
				DMat nm = m.getMass();

				// transform if mass is in global coordinates
				if (m.getCoordinateSystem() == Node.global_) {
					DMat tr = getTransformation();
					nm = nm.transform(tr, DMat.toLocal_);
				}

				// add to nodal mass
				mass = mass.add(nm);
			}
		}

		// return lumped nodal mass
		return mass.lump();
	}

	/**
	 * Returns dof numbers array.
	 * 
	 * @return The dof numbers array.
	 */
	public int[] getDofNumbers() {
		return dofNumbers_;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
