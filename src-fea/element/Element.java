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
package element;

import java.io.Serializable;
import java.util.Vector;

import section.Section;

import node.Node;

import material.Material;
import matrix.DMat;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.ElementMechLoad;
import boundary.ElementTemp;

/**
 * Class for element.
 * 
 * @author Murat
 * 
 */
public abstract class Element implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of element. */
	public final static int global_ = 0, local_ = 1;

	/** Static variable for the principal stress/strain order. */
	public final static int minPrincipal_ = 0, midPrincipal_ = 1,
			maxPrincipal_ = 2;

	/** Static variable for the global dofs of element. */
	protected final static int ux_ = 0, uy_ = 1, uz_ = 2, rx_ = 3, ry_ = 4,
			rz_ = 5;

	/** Static variable for the local dofs of element. */
	protected final static int u1_ = 0, u2_ = 1, u3_ = 2, r1_ = 3, r2_ = 4,
			r3_ = 5;

	/** The material of element. */
	private Material material_;

	/** Vector storing the sections of element. */
	private Section section_;

	/** Vector for storing mechanical loads of element. */
	private Vector<ElementMechLoad> mechLoads_;

	/** Vector for storing temperature loads of element. */
	private Vector<ElementTemp> tempLoads_;

	/** Vector for storing springs of element. */
	private Vector<ElementSpring> springs_;

	/** Vector for storing additional masses of element. */
	private Vector<ElementMass> masses_;

	/** Additional parameters for the element such as radii of curvatures. */
	private double[] param_;

	/** The array for storing dof numbers of element. */
	private int[] dofNumbers_;

	/** The scaling factors of boundary cases. */
	private double[] bScales_;

	/** Vector for storing the boundary cases to compute element quantities. */
	private Vector<BoundaryCase> bCases_;

	/** Sets new nodes to element. */
	public abstract void setNodes(Node[] nodes);

	/**
	 * Sets material to element.
	 * 
	 * @param material
	 *            The material to be set.
	 */
	public void setMaterial(Material material) {
		material_ = material;
	}

	/**
	 * Sets section to element.
	 * 
	 * @param section
	 *            The section to be set.
	 */
	public void setSection(Section section) {

		// set error message
		String message = "Illegal section for element type: " + getType();

		// get type of section
		int type = section.getType();

		// get dimension of element
		int dim = getDimension();

		// check compatibility
		if (dim == ElementLibrary.threeDimensional_)
			exceptionHandler(message);
		if (type == Section.thickness_ || type == Section.varThickness_)
			if (dim == ElementLibrary.oneDimensional_)
				exceptionHandler(message);

		// set section
		section_ = section;
	}

	/**
	 * Sets boundary cases and their scaling factors to element for computing
	 * element quantities.
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
	 * Sets temperature loads to element.
	 * 
	 * @param temp
	 *            Temperature loads to be set.
	 */
	public void setTempLoads(Vector<ElementTemp> temp) {
		tempLoads_ = temp;
	}

	/**
	 * Sets mechanical loads to element.
	 * 
	 * @param mechLoads
	 *            Vector storing the element mechanical loads.
	 */
	public void setMechLoads(Vector<ElementMechLoad> mechLoads) {

		// set error message
		String message = "Illegal loading for element type: " + getType();

		// check components of load
		int m = 0;
		for (int i = 0; i < mechLoads.size(); i++) {

			// get mechanical load
			ElementMechLoad ml = mechLoads.get(i);

			// check type of mechanical load
			if (getDimension() != ml.getType()) {
				exceptionHandler(message);
				m++;
				break;
			}

			// check components
			int comp = ml.getComponent();
			int[] dof = getDofArray(ml.getCoordinateSystem());
			if (comp >= dof.length) {
				exceptionHandler(message);
				m++;
				break;
			}
		}

		// set loads
		if (m == 0)
			mechLoads_ = mechLoads;
	}

	/**
	 * Sets springs to element.
	 * 
	 * @param springs
	 *            Vector storing the element springs.
	 */
	public void setSprings(Vector<ElementSpring> springs) {

		// check components of spring
		int m = 0;
		for (int i = 0; i < springs.size(); i++) {
			ElementSpring s = springs.get(i);
			int comp = s.getComponent();
			int[] dof = getDofArray(s.getCoordinateSystem());
			if (comp >= dof.length) {
				exceptionHandler("Illegal spring for element type: "
						+ getType());
				m++;
				break;
			}
		}

		// set springs
		if (m == 0)
			springs_ = springs;
	}

	/**
	 * Sets masses to element.
	 * 
	 * @param masses
	 *            Vector storing the element masses.
	 */
	public void setMasses(Vector<ElementMass> masses) {

		// check components of mass
		int m = 0;
		for (int i = 0; i < masses.size(); i++) {
			ElementMass s = masses.get(i);
			int comp = s.getComponent();
			int[] dof = getDofArray(s.getCoordinateSystem());
			if (comp >= dof.length) {
				exceptionHandler("Illegal mass for element type: " + getType());
				m++;
				break;
			}
		}

		// set masses
		if (m == 0)
			masses_ = masses;
	}

	/**
	 * Sets additional parameters for element such as radii of curvatures.
	 * 
	 * @param param
	 *            Array storing the additional parameters.
	 */
	public void setParameters(double[] param) {
		param_ = param;
	}

	/**
	 * Enumerates element degrees of freedom.
	 */
	public void enumerateDofs() {

		// get nodes of element
		Node[] nodes = getNodes();

		// set dofNumbers array
		dofNumbers_ = new int[6 * nodes.length];

		// loop over nodes
		for (int i = 0; i < nodes.length; i++) {

			// get node
			Node node = nodes[i];

			// loop over degrees of freedom
			for (int j = 0; j < 6; j++)
				dofNumbers_[6 * i + j] = node.getDofNumbers()[j];
		}
	}

	/**
	 * Returns the material of element.
	 * 
	 * @return The material of element.
	 */
	public Material getMaterial() {
		return material_;
	}

	/**
	 * Returns section of element.
	 * 
	 * @return The section of element.
	 */
	public Section getSection() {
		return section_;
	}

	/**
	 * Returns all element mechanical loads without checking boundary cases.
	 * This is used for copying/editing elements.
	 * 
	 * @return All element mechanical loads.
	 */
	public Vector<ElementMechLoad> getAllMechLoads() {
		return mechLoads_;
	}

	/**
	 * Returns all element temperature loads without checking boundary cases.
	 * This is used for copying/editing elements.
	 * 
	 * @return All element temperature loads.
	 */
	public Vector<ElementTemp> getAllTempLoads() {
		return tempLoads_;
	}

	/**
	 * Returns the mechanical loads of element for the demanded boundary cases.
	 * 
	 * @return The mechanical loads of element.
	 */
	public Vector<ElementMechLoad> getMechLoads() {

		// create mechanical load vector
		Vector<ElementMechLoad> ml = new Vector<ElementMechLoad>();

		// check if mechanical loads available
		if (mechLoads_ != null) {

			// loop over element mechanical loads
			for (int i = 0; i < mechLoads_.size(); i++) {

				// get mechanical load
				ElementMechLoad load = mechLoads_.get(i);

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
	 * Returns the element temperature loads for the demanded boundary cases.
	 * 
	 * @return The element temperature loads.
	 */
	public Vector<ElementTemp> getTempLoads() {

		// create temperature load vector
		Vector<ElementTemp> tl = new Vector<ElementTemp>();

		// check if any temp loads available
		if (tempLoads_ != null) {

			// loop over element temperature loads
			for (int i = 0; i < tempLoads_.size(); i++) {

				// get temperature load
				ElementTemp load = tempLoads_.get(i);

				// get boundary case of temperature load
				BoundaryCase bc = load.getBoundaryCase();

				// boundary cases contains temperature load
				if (bCases_ != null) {
					if (bCases_.contains(bc)) {

						// get scaling factor of boundary case and scale load
						double factor = bScales_[bCases_.indexOf(bc)];
						load.setLoadingScale(factor);

						// add
						tl.add(load);
					}
				}
			}
		}
		return tl;
	}

	/**
	 * Returns the springs of element.
	 * 
	 * @return The springs of element.
	 */
	public Vector<ElementSpring> getSprings() {
		return springs_;
	}

	/**
	 * Returns the additional masses of element.
	 * 
	 * @return The additional masses of element.
	 */
	public Vector<ElementMass> getAdditionalMasses() {
		return masses_;
	}

	/**
	 * Returns additional parameters assigned to element such as radii of
	 * curvatures.
	 * 
	 * @return Array storing the additional parameters.
	 */
	public double[] getParameters() {
		return param_;
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
	 * Returns element stiffness matrix.
	 * 
	 * @param coord
	 *            Demanded coordinate system denoting either global or node
	 *            local.
	 * @return Element stiffness matrix.
	 */
	public DMat getStiffnessMatrix(int coord) {

		// get the node number of element
		Node[] nodes = getNodes();
		int nn = nodes.length;

		// create element dof array
		int[] edof = getDofArray(Element.global_);
		int[] dof = new int[nn * edof.length];
		int k = 0;
		for (int i = 0; i < nn; i++) {
			for (int j = 0; j < edof.length; j++) {
				dof[k] = edof[j] + 6 * i;
				k++;
			}
		}

		// create general element stiffness matrix
		DMat kg = new DMat(6 * nn, 6 * nn);

		// compute element stiffness matrix
		DMat ke = computeStiffnessMatrix();

		// store components into general stiffness matrix
		for (int i = 0; i < ke.rowCount(); i++) {
			for (int j = 0; j < ke.columnCount(); j++)
				kg.set(dof[i], dof[j], ke.get(i, j));
		}

		// add spring stiffness matrix if there is spring
		if (springs_ != null)
			if (springs_.size() != 0)
				kg = kg.add(computeSpringStiffnessMatrix());

		// return if in global coordinates is demanded
		if (coord == Element.global_)
			return kg;

		// transform general stiffness matrix to nodal local system
		DMat tr = setupNodalTransformation();
		return kg.transform(tr, DMat.toLocal_);
	}

	/**
	 * Returns lumped element mass matrix.
	 * 
	 * @param coord
	 *            Demanded coordinate system denoting either global or node
	 *            local.
	 * @return Element mass matrix.
	 */
	public DMat getMassMatrix(int coord) {

		// get the node number of element
		int nn = getNodes().length;

		// create element dof array
		int[] edof = getDofArray(Element.global_);
		int[] dof = new int[nn * edof.length];
		int k = 0;
		for (int i = 0; i < nn; i++) {
			for (int j = 0; j < edof.length; j++) {
				dof[k] = edof[j] + 6 * i;
				k++;
			}
		}

		// create general element mass matrix
		DMat mg = new DMat(6 * nn, 6 * nn);

		// compute element mass matrix
		DMat me = computeMassMatrix();

		// store components into general mass matrix
		for (int i = 0; i < me.rowCount(); i++) {
			for (int j = 0; j < me.columnCount(); j++)
				mg.set(dof[i], dof[j], me.get(i, j));
		}

		// add additional mass matrix if there is additional mass
		if (masses_ != null)
			if (masses_.size() != 0)
				mg = mg.add(computeAdditionalMassMatrix());

		// return if in global coordinates is demanded
		if (coord == Element.global_)
			return mg.lump();

		// transform general mass matrix to nodal local system
		DMat tr = setupNodalTransformation();
		mg = mg.transform(tr, DMat.toLocal_);

		// return lumped mass matrix
		return mg.lump();
	}

	/**
	 * Returns element stability matrix.
	 * 
	 * @param coord
	 *            Demanded coordinate system denoting either global or node
	 *            local.
	 * @return Element stability matrix.
	 */
	public DMat getStabilityMatrix(int coord) {

		// get the node number of element
		Node[] nodes = getNodes();
		int nn = nodes.length;

		// create element dof array
		int[] edof = getDofArray(Element.global_);
		int[] dof = new int[nn * edof.length];
		int k = 0;
		for (int i = 0; i < nn; i++) {
			for (int j = 0; j < edof.length; j++) {
				dof[k] = edof[j] + 6 * i;
				k++;
			}
		}

		// create general element stability matrix
		DMat gg = new DMat(6 * nn, 6 * nn);

		// compute element stability matrix
		DMat ge = computeStabilityMatrix();

		// store components into general stability matrix
		for (int i = 0; i < ge.rowCount(); i++) {
			for (int j = 0; j < ge.columnCount(); j++)
				gg.set(dof[i], dof[j], ge.get(i, j));
		}

		// return if in global coordinates is demanded
		if (coord == Element.global_)
			return gg;

		// transform general stability matrix to nodal local system
		DMat tr = setupNodalTransformation();
		return gg.transform(tr, DMat.toLocal_);
	}

	/**
	 * Returns element mechanical load vector.
	 * 
	 * @param coord
	 *            Demanded coordinate system denoting either global or node
	 *            local.
	 * @return Element mechanical load vector.
	 */
	public DVec getMechLoadVector(int coord) {

		// compute element mechanical load vector
		DVec ml = computeMechLoadVector();

		// return if in global coordinates is demanded
		if (coord == Element.global_)
			return ml;

		// transform load vector to nodal local system
		DMat tr = setupNodalTransformation();
		return ml.transform(tr, DMat.toLocal_);
	}

	/**
	 * Returns element temperature load vector.
	 * 
	 * @param coord
	 *            Demanded coordinate system denoting either global or node
	 *            local.
	 * @return Element temperature load vector.
	 */
	public DVec getTempLoadVector(int coord) {

		// get the node number of element
		int nn = getNodes().length;

		// create element dof array
		int[] edof = getDofArray(Element.global_);
		int[] dof = new int[nn * edof.length];
		int k = 0;
		for (int i = 0; i < nn; i++) {
			for (int j = 0; j < edof.length; j++) {
				dof[k] = edof[j] + 6 * i;
				k++;
			}
		}

		// create general element load vector
		DVec lg = new DVec(6 * nn);

		// compute element thermal load vector
		DVec le = computeTempLoadVector();

		// store components into general load vector
		for (int i = 0; i < le.rowCount(); i++)
			lg.set(dof[i], le.get(i));

		// return if in global coordinates is demanded
		if (coord == Element.global_)
			return lg;

		// transform general thermal load vector to nodal local system
		DMat tr = setupNodalTransformation();
		return lg.transform(tr, DMat.toLocal_);
	}

	/**
	 * Returns element boundary load vector in nodal local coordinates.
	 * 
	 * @return Element boundary load vector.
	 */
	public DVec getBoundLoadVector() {

		// get nodes of element
		Node[] nodes = getNodes();

		// create element unknown vector
		DVec u = new DVec(6 * nodes.length);

		// loop over nodes of element
		for (int i = 0; i < nodes.length; i++) {

			// get the unknown vector of node
			DVec vec = nodes[i].getUnknown(Node.local_);

			// loop over dofs of node
			for (int j = 0; j < 6; j++)
				u.set(6 * i + j, vec.get(j));
		}

		// get element stiffness matrix in nodal local coordinates
		DMat ke = getStiffnessMatrix(Node.local_);

		// get element mechanical load vector
		DVec re = new DVec(ke.rowCount());
		if (getMechLoads().size() != 0)
			re = getMechLoadVector(Node.local_);

		// get element thermal load vector
		DVec te = new DVec(ke.rowCount());
		if (getTempLoads().size() != 0)
			te = getTempLoadVector(Node.local_);

		// compute and return element boundary load vector
		return ke.multiply(u).subtract(re).subtract(te);
	}

	/**
	 * Returns the demanded principal elastic strain.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param order
	 *            The order of the demanded principal elastic strain
	 *            (max/mid/min).
	 * @return The demanded principal elastic strain.
	 */
	public double getPrincipalStrain(double eps1, double eps2, double eps3,
			int order) {

		// check order
		if (order < 0 || order > 2)
			exceptionHandler("Illegal principal elastic strain demanded!");

		// get elastic strain tensor
		DMat strain = getStrain(eps1, eps2, eps3);

		// return demanded principal elastic strain
		return strain.getEigenvalue()[order];
	}

	/**
	 * Returns the demanded principal stress.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param order
	 *            The order of the demanded principal stress (max/mid/min).
	 * @return The demanded principal stress.
	 */
	public double getPrincipalStress(double eps1, double eps2, double eps3,
			int order) {

		// check order
		if (order < 0 || order > 2)
			exceptionHandler("Illegal principal stress demanded!");

		// get cauchy stress tensor
		DMat stress = getStress(eps1, eps2, eps3);

		// return demanded principal stress
		return stress.getEigenvalue()[order];
	}

	/**
	 * Returns the von Mises stress.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The Von Mises stress value.
	 */
	public double getVonMisesStress(double eps1, double eps2, double eps3) {

		// get principal stresses
		double s1 = getPrincipalStress(eps1, eps2, eps3, Element.minPrincipal_);
		double s2 = getPrincipalStress(eps1, eps2, eps3, Element.midPrincipal_);
		double s3 = getPrincipalStress(eps1, eps2, eps3, Element.maxPrincipal_);

		// compute Von Mises stress
		double vm = Math.sqrt(0.5 * ((s1 - s2) * (s1 - s2) + (s1 - s3)
				* (s1 - s3) + (s2 - s3) * (s2 - s3)));

		// return
		return vm;
	}

	/**
	 * Returns the mass of element.
	 * 
	 * @return The mass of element.
	 */
	public double getMass() {
		return getVolume() * getMaterial().getVolumeMass();
	}

	/**
	 * Returns the weight of element.
	 * 
	 * @return The weight of element.
	 */
	public double getWeight() {
		return getVolume() * getMaterial().getVolumeWeight();
	}

	/** Returns the volume of element. */
	public abstract double getVolume();

	/** Returns the type of element. */
	public abstract int getType();

	/** Returns the dimension of element. */
	public abstract int getDimension();

	/** Returns nodes of element. */
	public abstract Node[] getNodes();

	/** Returns local approximated displacements and rotations of element. */
	public abstract DVec getDisplacement(double eps1, double eps2, double eps3);

	/** Returns the strain tensor of element. */
	public abstract DMat getStrain(double eps1, double eps2, double eps3);

	/** Returns the stress tensor of element. */
	public abstract DMat getStress(double eps1, double eps2, double eps3);

	/** Returns the internal force of element. */
	public abstract double getInternalForce(int type, double eps1, double eps2,
			double eps3);

	/** Returns three dimensional transformation matrix. */
	public abstract DMat getTransformation();

	/**
	 * Returns element's dof array (per node).
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of element.
	 * @return Element's dof array.
	 */
	public abstract int[] getDofArray(int coordinateSystem);

	/** Computes element stiffness matrix. */
	protected abstract DMat computeStiffnessMatrix();

	/** Computes element mass matrix. */
	protected abstract DMat computeMassMatrix();

	/** Computes element stability matrix. */
	protected abstract DMat computeStabilityMatrix();

	/** Computes element mechanical load vector. */
	protected abstract DVec computeMechLoadVector();

	/** Computes element temperature load vector. */
	protected abstract DVec computeTempLoadVector();

	/** Computes element spring stiffness matrix. */
	protected abstract DMat computeSpringStiffnessMatrix();

	/** Computes element additional mass matrix. */
	protected abstract DMat computeAdditionalMassMatrix();

	/**
	 * Computes element transformation matrix.
	 * 
	 * @return Element transformation matrix.
	 */
	protected DMat computeTransformation() {

		// compute nodal transformation matrix
		DMat tr3D = getTransformation();
		DMat trNodal = new DMat(6, 6);
		trNodal = trNodal.setSubMatrix(tr3D, 0, 0);
		trNodal = trNodal.setSubMatrix(tr3D, 3, 3);

		// get local and global dof arrays of element
		int[] local = getDofArray(Element.local_);
		int[] global = getDofArray(Element.global_);

		// get number of local and global dofs of element
		int m = local.length;
		int n = global.length;

		// get number of nodes of element
		int nn = getNodes().length;

		// create element transformation matrix
		DMat tr = new DMat(nn * m, nn * n);

		// loop over nodes of element
		for (int i = 0; i < nn; i++)

			// loop over number of local dofs
			for (int j = 0; j < m; j++)

				// loop over number of global dofs
				for (int k = 0; k < n; k++)
					tr.set(m * i + j, n * i + k, trNodal.get(local[j],
							global[k]));
		return tr;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}

	/**
	 * Sets up and returns nodal transformation matrix.
	 * 
	 * @return The nodal transformation matrix.
	 */
	private DMat setupNodalTransformation() {

		// get number of nodes of element
		int nn = getNodes().length;

		// create transformation matrix
		DMat tr = new DMat(6 * nn, 6 * nn);

		// loop over nodes of element
		for (int i = 0; i < nn; i++) {

			// get transformation matrix of node
			DMat trn = getNodes()[i].getTransformation();

			// loop over rows of node's transformation matrix
			for (int j = 0; j < trn.rowCount(); j++) {

				// loop over columns of node's transformation matrix
				for (int k = 0; k < trn.columnCount(); k++) {

					// set values to element's nodal transformation matrix
					tr.set(6 * i + j, 6 * i + k, trn.get(j, k));
				}
			}
		}
		return tr;
	}
}
