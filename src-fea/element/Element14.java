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
import math.GaussQuadrature;
import math.Interpolation1D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementTemp;

/**
 * Class for Element14. Properties of element are; Mechanics: Timoshenko beam,
 * Geometric: Linear, Material: Orthotropic/Isotropic, Interpolation degree:
 * Quadratic, Interpolation family: Lagrange, Number of nodes: 3, Dofs (per
 * node): ux, uy, uz, rx, ry, rz (global), u1, u2, u3, r1, r2, r3 (local),
 * Mechanical loading: fx, fy, fz, mx, my, mz (global), f1, f2, f3, m1, m2, m3
 * (local), Temperature loading: yes, Displacements: u1, u2, u3, r1, r2, r3,
 * Strains: e11, e12, e13, Stresses: s11, s12, s13, Internal forces: N1, V2, V3,
 * T1, M2, M3. Note: Transverse shear terms are under-integrated against shear
 * locking.
 * 
 * @author Murat
 * 
 */
public class Element14 extends Element1D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[3];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_,
			Element.rx_, Element.ry_, Element.rz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_, Element.u2_, Element.u3_,
			Element.r1_, Element.r2_, Element.r3_ };

	/**
	 * Creates Element14 element.
	 * 
	 * @param n1
	 *            The first (starting) node of element.
	 * @param n2
	 *            The second (middle) node of element.
	 * @param n3
	 *            The third (ending) node of element.
	 */
	public Element14(Node n1, Node n2, Node n3) {

		// set nodes
		nodes_[0] = n1;
		nodes_[1] = n2;
		nodes_[2] = n3;
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
		return ElementLibrary.element14_;
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
		DVec uGlobal = new DVec(18);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		for (int i = 0; i < 6; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 6, u2.get(i));
			uGlobal.set(i + 12, u3.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get shape function
		Interpolation1D shapeF = getInterpolation();

		// compute local approximated displacements
		double[] disp = new double[6];
		for (int i = 0; i < nodes_.length; i++) {
			disp[0] += uLocal.get(6 * i) * shapeF.getFunction(eps1, i);
			disp[1] += uLocal.get(6 * i + 1) * shapeF.getFunction(eps1, i);
			disp[2] += uLocal.get(6 * i + 2) * shapeF.getFunction(eps1, i);
			disp[3] += uLocal.get(6 * i + 3) * shapeF.getFunction(eps1, i);
			disp[4] += uLocal.get(6 * i + 4) * shapeF.getFunction(eps1, i);
			disp[5] += uLocal.get(6 * i + 5) * shapeF.getFunction(eps1, i);
		}

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

		// get nodal unknowns
		DVec uGlobal = new DVec(18);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		for (int i = 0; i < 6; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 6, u2.get(i));
			uGlobal.set(i + 12, u3.get(i));
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
			strain[0][0] += uLocal.get(6 * i) * shapeF.getDerFunction(eps1, i)
					/ jacDet;

			// compute strain-12
			strain[0][1] += uLocal.get(6 * i + 1)
					* shapeF.getDerFunction(eps1, i) / (2.0 * jacDet) - 0.5
					* uLocal.get(6 * i + 5) * shapeF.getFunction(eps1, i);

			// compute strain-13
			strain[0][2] += uLocal.get(6 * i + 2)
					* shapeF.getDerFunction(eps1, i) / (2.0 * jacDet) + 0.5
					* uLocal.get(6 * i + 4) * shapeF.getFunction(eps1, i);
		}
		strain[1][0] = strain[0][1];
		strain[2][0] = strain[0][2];

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

		// get material values
		Material m = getMaterial();
		DMat c = m.getC(Material.threeD_);
		DVec phi = m.getAlpha(Material.threeD_);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// get strain vector
		DVec strainV = new DVec(6);
		DMat strainM = getStrain(eps1, eps2, 0.0);
		strainV.set(0, strainM.get(0, 0));
		strainV.set(1, strainM.get(1, 1));
		strainV.set(2, strainM.get(2, 2));
		strainV.set(3, 2.0 * strainM.get(1, 2));
		strainV.set(4, 2.0 * strainM.get(0, 2));
		strainV.set(5, 2.0 * strainM.get(0, 1));

		// compute stress vector
		DVec stressV = c.multiply(strainV.subtract(phi.scale(theta)));

		// construct and return stress tensor
		DMat stressM = new DMat(3, 3);
		stressM.set(0, 0, stressV.get(0));
		stressM.set(1, 1, stressV.get(1));
		stressM.set(2, 2, stressV.get(2));
		stressM.set(1, 2, stressV.get(3));
		stressM.set(0, 2, stressV.get(4));
		stressM.set(0, 1, stressV.get(5));
		stressM = stressM.mirror();
		return stressM;
	}

	/**
	 * Returns the demanded internal force of element.
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

		// get factors
		Material m = getMaterial();
		DMat s = m.getS(Material.threeD_);
		double s11 = s.get(0, 0);
		double s55 = s.get(4, 4);
		double s66 = s.get(5, 5);
		double a = getSection().getArea(0);
		double a2 = getSection().getShearAreaX2(0);
		double a3 = getSection().getShearAreaX3(0);
		double i2 = getSection().getInertiaX2(0);
		double i3 = getSection().getInertiaX3(0);
		double tc = getSection().getTorsionalConstant(0);
		double phi1 = m.getAlpha(Material.threeD_).get(0);
		double jac = getDetJacobian();

		// get nodal unknowns
		DVec uGlobal = new DVec(18);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		for (int i = 0; i < 6; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 6, u2.get(i));
			uGlobal.set(i + 12, u3.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get shape function
		Interpolation1D shapeF = getInterpolation();

		// initialize internal force
		double force = 0.0;

		// normal force N1 demanded
		if (type == Element1D.N1_) {

			// get thermal influences
			double theta = 0.0;
			Vector<ElementTemp> ml = getTempLoads();
			for (int i = 0; i < ml.size(); i++)
				theta += ml.get(i).getValue();

			// compute internal force
			double scale = a / (s11 * jac);
			for (int i = 0; i < nodes_.length; i++)
				force += scale * uLocal.get(6 * i)
						* shapeF.getDerFunction(eps1, i);
			force = force - a * theta * phi1 / s11;
		}

		// shear force V2 demanded
		else if (type == Element1D.V2_) {

			// compute internal force
			double scale = a3 / s66;
			for (int i = 0; i < nodes_.length; i++)
				force += scale
						* (uLocal.get(6 * i + 1)
								* shapeF.getDerFunction(eps1, i) / jac - uLocal
								.get(6 * i + 5)
								* shapeF.getFunction(eps1, i));
			force = -force;
		}

		// shear force V3 demanded
		else if (type == Element1D.V3_) {

			// compute internal force
			double scale = a2 / s55;
			for (int i = 0; i < nodes_.length; i++)
				force += scale
						* (uLocal.get(6 * i + 2)
								* shapeF.getDerFunction(eps1, i) / jac + uLocal
								.get(6 * i + 4)
								* shapeF.getFunction(eps1, i));
		}

		// twisting moment T1 demanded
		else if (type == Element1D.T1_) {

			// compute internal force
			double scale = (2.0 * tc) / ((s55 + s66) * jac);
			for (int i = 0; i < nodes_.length; i++)
				force += scale
						* (uLocal.get(6 * i + 3) * shapeF.getDerFunction(eps1,
								i));
		}

		// bending moment M2 demanded
		else if (type == Element1D.M2_) {

			// compute internal force
			double scale = i2 / (s11 * jac);
			for (int i = 0; i < nodes_.length; i++)
				force += scale
						* (uLocal.get(6 * i + 4) * shapeF.getDerFunction(eps1,
								i));
			force = -force;
		}

		// bending moment M3 demanded
		else if (type == Element1D.M3_) {

			// compute internal force
			double scale = i3 / (s11 * jac);
			for (int i = 0; i < nodes_.length; i++)
				force += scale
						* (uLocal.get(6 * i + 5) * shapeF.getDerFunction(eps1,
								i));
			force = -force;
		}

		// return internal force
		return force;
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

		// get constants
		Material m = getMaterial();
		DMat s = m.getS(Material.threeD_);
		double s11 = s.get(0, 0);
		double s55 = s.get(4, 4);
		double s66 = s.get(5, 5);
		double a = getSection().getArea(0);
		double a2 = getSection().getShearAreaX2(0);
		double a3 = getSection().getShearAreaX3(0);
		double i2 = getSection().getInertiaX2(0);
		double i3 = getSection().getInertiaX3(0);
		double tc = getSection().getTorsionalConstant(0);
		double jac = getDetJacobian();

		// compute factors
		double lambda = -s66 / a3;
		double beta = -s55 / a2;
		double gamma = -s11 / a;
		double alpha = -s11 / i3;
		double phi = -s11 / i2;
		double theta = -(s55 + s66) / (2.0 * tc);

		// compute sub-matrix k1
		DMat k1 = new DMat(3, 3);
		k1.set(0, 0, 4.0 / 15.0);
		k1.set(0, 1, 2.0 / 15.0);
		k1.set(0, 2, -1.0 / 15.0);
		k1.set(1, 0, 2.0 / 15.0);
		k1.set(1, 1, 16.0 / 15.0);
		k1.set(1, 2, 2.0 / 15.0);
		k1.set(2, 0, -1.0 / 15.0);
		k1.set(2, 1, 2.0 / 15.0);
		k1.set(2, 2, 4.0 / 15.0);
		k1 = k1.scale(jac);

		// compute sub-matrix k2
		DMat k2 = new DMat(3, 3);
		k2.set(0, 0, -0.5);
		k2.set(0, 1, 2.0 / 3.0);
		k2.set(0, 2, -1.0 / 6.0);
		k2.set(1, 0, -2.0 / 3.0);
		k2.set(1, 2, 2.0 / 3.0);
		k2.set(2, 0, 1.0 / 6.0);
		k2.set(2, 1, -2.0 / 3.0);
		k2.set(2, 2, 0.5);

		// compute sub-matrix k3 (under-integrated)
		DMat k3 = new DMat(3, 3);
		k3.set(0, 0, 2.0 / 9.0);
		k3.set(0, 1, 2.0 / 9.0);
		k3.set(0, 2, -1.0 / 9.0);
		k3.set(1, 0, 2.0 / 9.0);
		k3.set(1, 1, 8.0 / 9.0);
		k3.set(1, 2, 2.0 / 9.0);
		k3.set(2, 0, -1.0 / 9.0);
		k3.set(2, 1, 2.0 / 9.0);
		k3.set(2, 2, 2.0 / 9.0);
		k3 = k3.scale(jac);

		// insert sub-matrices to stiffness matrix
		DMat kLocal = new DMat(36, 36);
		for (int i = 0; i < k1.rowCount(); i++) {
			for (int j = 0; j < k1.columnCount(); j++) {
				kLocal.set(i, j, lambda * k1.get(i, j));
				kLocal.set(i, j + 21, k2.get(i, j));
				kLocal.set(i, j + 33, -k3.get(i, j));
				kLocal.set(i + 3, j + 3, beta * k1.get(i, j));
				kLocal.set(i + 3, j + 24, k2.get(i, j));
				kLocal.set(i + 3, j + 30, k3.get(i, j));
				kLocal.set(i + 6, j + 6, gamma * k1.get(i, j));
				kLocal.set(i + 6, j + 18, k2.get(i, j));
				kLocal.set(i + 9, j + 9, alpha * k1.get(i, j));
				kLocal.set(i + 9, j + 33, k2.get(i, j));
				kLocal.set(i + 12, j + 12, phi * k1.get(i, j));
				kLocal.set(i + 12, j + 30, k2.get(i, j));
				kLocal.set(i + 15, j + 15, theta * k1.get(i, j));
				kLocal.set(i + 15, j + 27, k2.get(i, j));
			}
		}
		kLocal = kLocal.mirror();

		// apply static condensation
		kLocal = kLocal.condense(18);

		// node-wise sorting of element stiffness matrix
		// sort by columns
		DMat kLocal1 = new DMat(18, 18);
		for (int i = 0; i < 18; i++)
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < 3; k++)
					kLocal1.set(i, j + 6 * k, kLocal.get(i, 3 * j + k));

		// sort by rows
		DMat kLocal2 = new DMat(18, 18);
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 18; j++)
				for (int k = 0; k < 3; k++)
					kLocal2.set(i + 6 * k, j, kLocal1.get(3 * i + k, j));

		// compute global stiffness matrix
		DMat tr = computeTransformation();
		return kLocal2.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Retuns element mass matrix.
	 * 
	 * @return Element mass matrix.
	 */
	protected DMat computeMassMatrix() {

		// get factors
		double ro = getMaterial().getVolumeMass();
		double a = getSection().getArea(0);
		double i1 = getSection().getInertiaX1(0);
		double i2 = getSection().getInertiaX2(0);
		double i3 = getSection().getInertiaX3(0);
		double jac = getDetJacobian();

		// compute sub-matrix k1
		DMat k1 = new DMat(3, 3);
		k1.set(0, 0, 4.0 / 15.0);
		k1.set(0, 1, 2.0 / 15.0);
		k1.set(0, 2, -1.0 / 15.0);
		k1.set(1, 0, 2.0 / 15.0);
		k1.set(1, 1, 16.0 / 15.0);
		k1.set(1, 2, 2.0 / 15.0);
		k1.set(2, 0, -1.0 / 15.0);
		k1.set(2, 1, 2.0 / 15.0);
		k1.set(2, 2, 4.0 / 15.0);
		k1 = k1.scale(jac);

		// compute local mass matrix
		DMat mLocal = new DMat(18, 18);
		for (int i = 0; i < k1.rowCount(); i++) {
			for (int j = 0; j < k1.columnCount(); j++) {
				mLocal.set(i, j, ro * a * k1.get(i, j));
				mLocal.set(i + 3, j + 3, ro * a * k1.get(i, j));
				mLocal.set(i + 6, j + 6, ro * a * k1.get(i, j));
				mLocal.set(i + 9, j + 9, ro * i1 * k1.get(i, j));
				mLocal.set(i + 12, j + 12, ro * i2 * k1.get(i, j));
				mLocal.set(i + 15, j + 15, ro * i3 * k1.get(i, j));
			}
		}

		// node-wise sorting of element stiffness matrix
		// sort by columns
		DMat mLocal1 = new DMat(18, 18);
		for (int i = 0; i < 18; i++)
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < 3; k++)
					mLocal1.set(i, j + 6 * k, mLocal.get(i, 3 * j + k));

		// sort by rows
		DMat mLocal2 = new DMat(18, 18);
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 18; j++)
				for (int k = 0; k < 3; k++)
					mLocal2.set(i + 6 * k, j, mLocal1.get(3 * i + k, j));

		// compute global mass matrix
		DMat tr = computeTransformation();
		return mLocal2.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Computes element stability matrix.
	 * 
	 * @return Element stability matrix.
	 */
	protected DMat computeStabilityMatrix() {

		// get factors
		double a = getSection().getArea(0);
		double jac = getDetJacobian();

		// set number of Gauss points
		int nog = 2;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.oneDimensional_);

		// loop over Gauss points
		DMat gLocal = new DMat(18, 18);
		DMat s = new DMat(6, 6);
		for (int i = 0; i < nog; i++) {

			// get weight factor and support
			double alpha = q.getWeight(i);
			double supp = q.getSupport1(i);

			// compute initial stress matrix
			double s11 = computeInitialStress(supp);
			for (int j = 0; j < 3; j++)
				s.set(j, j, s11);

			// get G operator and its transpose
			DMat gop = computeGop(supp, jac);
			DMat gopTr = gop.transpose();

			// compute function to be integrated
			DMat func = gopTr.multiply(s.multiply(gop));
			func = func.scale(alpha * a * jac);

			// add to stability matrix
			gLocal = gLocal.add(func);
		}

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
		DVec tLoad = new DVec(18);
		tLoad.set(0, -1.0);
		tLoad.set(12, 1.0);
		tLoad = tLoad.scale(a * theta * phi1 / s11);

		// compute global load vector
		DMat tr = computeTransformation();
		return tLoad.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Computes and returns element geometric differential operator.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param jac
	 *            Determinant of jacobian.
	 * @return Element geometric differential operator.
	 */
	private DMat computeGop(double eps1, double jac) {

		// get interpolation function
		Interpolation1D intF = getInterpolation();

		// initialize element G operator matrix
		DMat gop = new DMat(6, 18);

		// loop over nodes of element
		for (int i = 0; i < 3; i++) {

			// compute nodal G operator
			DMat gopNode = new DMat(6, 6);
			double val = intF.getDerFunction(eps1, i) / jac;
			gopNode.set(0, 0, val);
			gopNode.set(1, 1, val);
			gopNode.set(2, 2, val);

			// set nodal G operator to element G operator
			gop = gop.setSubMatrix(gopNode, 0, 6 * i);
		}

		// return element G operator
		return gop;
	}
}
