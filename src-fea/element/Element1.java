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

import java.util.Vector;

import node.Node;

import material.Material;
import math.Interpolation1D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementTemp;

/**
 * Class for Element1. Properties of element are; Mechanics: Truss, Geometric:
 * Linear, Material: Orthotropic/Isotropic, Interpolation degree: Quadratic,
 * Interpolation family: Lagrange, Number of nodes: 3, Dofs (per node): ux, uy,
 * uz (global), u1 (local), Mechanical loading: fx, fy, fz (global), f1 (local),
 * Temperature loading: yes, Displacements: u1, Strains: e11, e22, e33,
 * Stresses: s11, Internal forces: N1.
 * 
 * @author Murat
 * 
 */
public class Element1 extends Element1D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[3];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_ };

	/**
	 * Creates Element1 element.
	 * 
	 * @param n1
	 *            The first (starting) node of element.
	 * @param n2
	 *            The second (middle) node of element.
	 * @param n3
	 *            The third (ending) node of element.
	 */
	public Element1(Node n1, Node n2, Node n3) {

		// set nodes
		nodes_[0] = n1;
		nodes_[1] = n2;
		nodes_[2] = n3;

		// tie internal node
		tieInternalNode();
	}

	/**
	 * Sets new nodes to element.
	 */
	public void setNodes(Node[] nodes) {
		nodes_ = nodes;
	}

	/**
	 * Returns the type of element.
	 * 
	 * @return The type of element.
	 */
	public int getType() {
		return ElementLibrary.element1_;
	}

	/**
	 * Returns the nodes of element.
	 * 
	 * @return The nodes of element.
	 */
	public Node[] getNodes() {
		return nodes_;
	}

	/**
	 * Returns local approximated displacements of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2 (not used here).
	 * @return The local approximated displacement vector of element.
	 */
	public DVec getDisplacement(double eps1, double eps2, double eps3) {

		// get nodal unknowns
		DVec uGlobal = new DVec(9);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get shape function
		Interpolation1D shapeF = getInterpolation();

		// compute local approximated displacements
		double[] disp = new double[6];
		for (int i = 0; i < nodes_.length; i++)
			disp[0] += uLocal.get(i) * shapeF.getFunction(eps1, i);

		// return
		return new DVec(disp);
	}

	/**
	 * Returns the elastic strain tensor of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2 (not used here).
	 * @return The elastic strain tensor of element.
	 */
	public DMat getStrain(double eps1, double eps2, double eps3) {

		// get material values
		Material m = getMaterial();
		double s11 = m.getS(Material.threeD_).get(0, 0);
		double s21 = m.getS(Material.threeD_).get(1, 0);
		double s31 = m.getS(Material.threeD_).get(2, 0);
		double phi1 = m.getAlpha(Material.threeD_).get(0);
		double phi2 = m.getAlpha(Material.threeD_).get(1);
		double phi3 = m.getAlpha(Material.threeD_).get(2);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// get nodal unknowns
		DVec uGlobal = new DVec(9);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get shape function and determinant of jacobian
		Interpolation1D shapeF = getInterpolation();
		double jacDet = getDetJacobian();

		// compute local approximated strains
		double[][] strain = new double[3][3];
		for (int i = 0; i < nodes_.length; i++) {

			// compute strain-11
			strain[0][0] += uLocal.get(i) * shapeF.getDerFunction(eps1, i)
					/ jacDet;
		}

		// compute strain-22
		strain[1][1] = s21 / s11 * (strain[0][0] - phi1 * theta) + phi2 * theta;

		// compute strain-33
		strain[2][2] = s31 / s11 * (strain[0][0] - phi1 * theta) + phi3 * theta;

		// return strain tensor
		return new DMat(strain);
	}

	/**
	 * Returns the Cauchy stress tensor of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2 (not used here).
	 * @return The Cauchy stress tensor of element.
	 */
	public DMat getStress(double eps1, double eps2, double eps3) {

		// get strain11
		double strain11 = getStrain(eps1, eps2, 0.0).get(0, 0);

		// get material values
		Material m = getMaterial();
		double s11 = m.getS(Material.threeD_).get(0, 0);
		double phi1 = m.getAlpha(Material.threeD_).get(0);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// compute stress tensor
		DMat sigma = new DMat(3, 3);
		double sigma11 = (strain11 - phi1 * theta) / s11;
		sigma.set(0, 0, sigma11);

		// return
		return sigma;
	}

	/**
	 * Returns the internal force of element.
	 * 
	 * @param type
	 *            The type of internal force.
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2 (not used here).
	 * @return The internal force of element.
	 */
	public double getInternalForce(int type, double eps1, double eps2,
			double eps3) {

		// check type of demanded internal force
		if (type != Element1D.N1_)
			return 0.0;

		// get stress tensor
		DMat stress = getStress(eps1, eps2, 0.0);

		// get cross-section area
		double a = getSection().getArea(0);

		// return internal force
		return stress.get(0, 0) * a;
	}

	/**
	 * Returns element dof array (per node).
	 */
	public int[] getDofArray(int coordinateSystem) {

		// check coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for dof array of element!");

		// return array
		if (coordinateSystem == Element.global_)
			return globalDofArray_;
		else
			return localDofArray_;
	}

	/**
	 * Returns interpolation function of element.
	 */
	protected Interpolation1D getInterpolation() {

		// set family and degree
		int family = Interpolation1D.lagrange_;
		int degree = Interpolation1D.quadratic_;

		// return interpolation function
		return new Interpolation1D(family, degree);
	}

	/**
	 * Computes element stiffness matrix.
	 * 
	 * @return Element stiffness matrix.
	 */
	protected DMat computeStiffnessMatrix() {

		// get factors
		Material m = getMaterial();
		double s11 = m.getS(Material.threeD_).get(0, 0);
		double a = getSection().getArea(0);
		double l = getLength();

		// compute local stiffness matrix
		DMat kLocal = new DMat(3, 3);
		kLocal.set(0, 0, 7.0);
		kLocal.set(0, 1, -8.0);
		kLocal.set(0, 2, 1.0);
		kLocal.set(1, 1, 16.0);
		kLocal.set(1, 2, -8.0);
		kLocal.set(2, 2, 7.0);
		kLocal = kLocal.mirror();
		kLocal = kLocal.scale(a / (3.0 * s11 * l));

		// compute global stiffness matrix
		DMat tr = computeTransformation();
		return kLocal.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Retuns element mass matrix.
	 * 
	 * @return Element mass matrix.
	 */
	protected DMat computeMassMatrix() {

		// get factors
		Material m = getMaterial();
		double ro = m.getVolumeMass();
		double a = getSection().getArea(0);
		double l = getLength();

		// compute local mass matrix
		DMat mLocal = new DMat(3, 3);
		mLocal.set(0, 0, 4.0);
		mLocal.set(0, 1, 2.0);
		mLocal.set(0, 2, -1.0);
		mLocal.set(1, 1, 16.0);
		mLocal.set(1, 2, 2.0);
		mLocal.set(2, 2, 4.0);
		mLocal = mLocal.mirror();
		mLocal = mLocal.scale(ro * a * l / 30.0);

		// compute global mass matrix
		DMat tr = computeTransformation();
		return mLocal.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Computes element stability matrix.
	 * 
	 * @return Element stability matrix.
	 */
	protected DMat computeStabilityMatrix() {

		// get factors
		double l = getLength();
		double a = getSection().getArea(0);

		// get average normal stress
		double s11 = computeInitialStress(0.0);

		// compute local stability matrix
		DMat gLocal = new DMat(3, 3);
		gLocal.set(0, 0, 7.0);
		gLocal.set(0, 1, -8.0);
		gLocal.set(0, 2, 1.0);
		gLocal.set(1, 1, 16.0);
		gLocal.set(1, 2, -8.0);
		gLocal.set(2, 2, 7.0);
		gLocal = gLocal.mirror();
		gLocal = gLocal.scale(s11 * a / (3.0 * l));

		// compute global stability matrix
		DMat tr = computeTransformation();
		return gLocal.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Returns element thermal load vector.
	 * 
	 * @return Element thermal load vector.
	 */
	protected DVec computeTempLoadVector() {

		// get factors
		Material m = getMaterial();
		double s11 = m.getS(Material.threeD_).get(0, 0);
		double phi1 = m.getAlpha(Material.threeD_).get(0);
		double a = getSection().getArea(0);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// compute local thermal load vector
		DVec tLoad = new DVec(3);
		tLoad.set(0, -1.0);
		tLoad.set(2, 1.0);
		tLoad = tLoad.scale(a * theta * phi1 / s11);

		// compute global load vector
		DMat tr = computeTransformation();
		return tLoad.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Ties the internal node of the element.
	 * 
	 */
	private void tieInternalNode() {

		// set 3D transformation matrix of element to internal node
		nodes_[1].setTransformation(getTransformation());
	}
}
