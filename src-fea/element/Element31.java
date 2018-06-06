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

import boundary.ElementTemp;
import material.Material;
import math.GaussQuadrature;
import math.Interpolation3D;
import matrix.DMat;
import matrix.DVec;
import node.Node;

/**
 * Class for Element23. Properties of element are; Mechanics: Solid, Geometric:
 * Linear, Material: Orthotropic/Isotropic, Interpolation degree: Triquadratic,
 * Interpolation family: Lagrange, Number of nodes: 10, Geometry: Tetrahedral,
 * Dofs (per node): ux, uy, uz (global), u1, u2, u3 (local), Mechanical loading:
 * fx, fy, fz (global), f1, f2, f3 (local), Temperature loading: yes,
 * Displacements: u1, u2, u3, Strains: e11, e22, e33, e12, e13, e23 Stresses:
 * s11, s22, s33, s12, s13, s23.
 * 
 * @author Murat
 * 
 */
public class Element31 extends Element3D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[10];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_, Element.u2_, Element.u3_ };

	/**
	 * Creates Element31 element. Nodes 1, 2 and 3 should be given in
	 * counter-clockwise order and node 4 is on the opposing vertex.
	 * 
	 * @param n1
	 *            The first node of element.
	 * @param n2
	 *            ...
	 * @param n3
	 *            ...
	 * @param n4
	 *            ...
	 * @param n5
	 *            ...
	 * @param n6
	 *            ...
	 * @param n7
	 *            ...
	 * @param n8
	 *            ...
	 * @param n9
	 *            ...
	 * @param n10
	 *            ...
	 */
	public Element31(Node n1, Node n2, Node n3, Node n4, Node n5, Node n6,
			Node n7, Node n8, Node n9, Node n10) {
		nodes_[0] = n1;
		nodes_[1] = n2;
		nodes_[2] = n3;
		nodes_[3] = n4;
		nodes_[4] = n5;
		nodes_[5] = n6;
		nodes_[6] = n7;
		nodes_[7] = n8;
		nodes_[8] = n9;
		nodes_[9] = n10;
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
		return ElementLibrary.element31_;
	}

	/**
	 * Returns the geometry of element.
	 */
	public int getGeometry() {
		return Element3D.tetrahedral_;
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
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The local approximated displacement vector of element.
	 */
	public DVec getDisplacement(double eps1, double eps2, double eps3) {

		// get nodal unknowns
		DVec uGlobal = new DVec(30);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		DVec u5 = getNodes()[4].getUnknown(Node.global_);
		DVec u6 = getNodes()[5].getUnknown(Node.global_);
		DVec u7 = getNodes()[6].getUnknown(Node.global_);
		DVec u8 = getNodes()[7].getUnknown(Node.global_);
		DVec u9 = getNodes()[8].getUnknown(Node.global_);
		DVec u10 = getNodes()[9].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
			uGlobal.set(i + 9, u4.get(i));
			uGlobal.set(i + 12, u5.get(i));
			uGlobal.set(i + 15, u6.get(i));
			uGlobal.set(i + 18, u7.get(i));
			uGlobal.set(i + 21, u8.get(i));
			uGlobal.set(i + 24, u9.get(i));
			uGlobal.set(i + 27, u10.get(i));
		}

		// get shape function
		Interpolation3D shapeF = getInterpolation();

		// compute local approximated displacements
		double[] disp = new double[6];
		for (int i = 0; i < nodes_.length; i++) {
			disp[0] += uGlobal.get(3 * i)
					* shapeF.getFunction(eps1, eps2, eps3, i);
			disp[1] += uGlobal.get(3 * i + 1)
					* shapeF.getFunction(eps1, eps2, eps3, i);
			disp[2] += uGlobal.get(3 * i + 2)
					* shapeF.getFunction(eps1, eps2, eps3, i);
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
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The elastic strain tensor of element.
	 */
	public DMat getStrain(double eps1, double eps2, double eps3) {

		// get nodal unknowns
		DVec uGlobal = new DVec(30);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		DVec u5 = getNodes()[4].getUnknown(Node.global_);
		DVec u6 = getNodes()[5].getUnknown(Node.global_);
		DVec u7 = getNodes()[6].getUnknown(Node.global_);
		DVec u8 = getNodes()[7].getUnknown(Node.global_);
		DVec u9 = getNodes()[8].getUnknown(Node.global_);
		DVec u10 = getNodes()[9].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
			uGlobal.set(i + 9, u4.get(i));
			uGlobal.set(i + 12, u5.get(i));
			uGlobal.set(i + 15, u6.get(i));
			uGlobal.set(i + 18, u7.get(i));
			uGlobal.set(i + 21, u8.get(i));
			uGlobal.set(i + 24, u9.get(i));
			uGlobal.set(i + 27, u10.get(i));
		}

		// get jacobian matrix
		DMat jac = getJacobian(eps1, eps2, eps3);

		// compute strain vector
		DVec strainV = computeBop(eps1, eps2, eps3, jac).multiply(uGlobal);

		// build and return strain tensor
		DMat strain = new DMat(3, 3);
		strain.set(0, 0, strainV.get(0));
		strain.set(1, 1, strainV.get(1));
		strain.set(2, 2, strainV.get(2));
		strain.set(0, 1, strainV.get(5) / 2.0);
		strain.set(0, 2, strainV.get(4) / 2.0);
		strain.set(1, 2, strainV.get(3) / 2.0);
		strain = strain.mirror();
		return strain;
	}

	/**
	 * Returns the Cauchy stress tensor of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The Cauchy stress tensor of element.
	 */
	public DMat getStress(double eps1, double eps2, double eps3) {

		// get material values
		Material m = getMaterial();
		DMat c = m.getC(Material.threeD_);
		DVec alpha = m.getAlpha(Material.threeD_);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();
		alpha = alpha.scale(theta);

		// get strain tensor
		DMat strain = getStrain(eps1, eps2, eps3);
		DVec strainVec = new DVec(6);
		strainVec.set(0, strain.get(0, 0));
		strainVec.set(1, strain.get(1, 1));
		strainVec.set(2, strain.get(2, 2));
		strainVec.set(3, 2.0 * strain.get(1, 2));
		strainVec.set(4, 2.0 * strain.get(0, 2));
		strainVec.set(5, 2.0 * strain.get(0, 1));

		// compute stress vector
		DVec stressVec = c.multiply(strainVec.subtract(alpha));

		// build and return stress tensor
		DMat stress = new DMat(3, 3);
		stress.set(0, 0, stressVec.get(0));
		stress.set(1, 1, stressVec.get(1));
		stress.set(2, 2, stressVec.get(2));
		stress.set(0, 1, stressVec.get(5));
		stress.set(0, 2, stressVec.get(4));
		stress.set(1, 2, stressVec.get(3));
		stress = stress.mirror();
		return stress;
	}

	/**
	 * Returns the internal force of element.
	 * 
	 * @param type
	 *            The type of internal force.
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The internal force of element.
	 */
	public double getInternalForce(int type, double eps1, double eps2,
			double eps3) {
		return 0.0;
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
	protected Interpolation3D getInterpolation() {

		// set geometry and degree
		int geometry = Interpolation3D.tetrahedral_;
		int degree = Interpolation3D.triquadratic_;

		// return interpolation function
		return new Interpolation3D(degree, geometry);
	}

	/**
	 * Computes element stiffness matrix.
	 * 
	 * @return Element stiffness matrix.
	 */
	protected DMat computeStiffnessMatrix() {

		// get material matrix
		DMat c = getMaterial().getC(Material.threeD_);

		// set number of Gauss points
		int nog = 4;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.tetrahedral_);

		// loop over Gauss points in first direction
		DMat kLocal = new DMat(30, 30);
		for (int i = 0; i < nog; i++) {

			// get first weight factor and support
			double alpha1 = q.getWeight(i);
			double supp1 = q.getSupport1(i);

			// loop over Gauss points in second direction
			for (int j = 0; j < nog; j++) {

				// get second weight factor and support
				double alpha2 = q.getWeight(j);
				double supp2 = q.getSupport2(j);

				// loop over Gauss points in third direction
				for (int k = 0; k < nog; k++) {

					// get third weight factor and support
					double alpha3 = q.getWeight(k);
					double supp3 = q.getSupport3(k);

					// compute cm, xm, ym and zm
					double cm = alpha1 * alpha2 * alpha3 / 64.0;
					cm *= Math.pow(1.0 - supp1, 2.0) * (1.0 - supp2);
					double xm = (1.0 + supp1) / 2.0;
					double ym = (1.0 - supp1) * (1.0 + supp2) / 4.0;
					double zm = (1.0 - supp1) * (1.0 - supp2) * (1 + supp3)
							/ 8.0;

					// get jacobian and compute determinant
					DMat jac = getJacobian(xm, ym, zm);
					double jacDet = jac.determinant();

					// get B operator and its transpose
					DMat bop = computeBop(xm, ym, zm, jac);
					DMat bopTr = bop.transpose();

					// compute function to be integrated
					DMat func = bopTr.multiply(c.multiply(bop));
					func = func.scale(cm * jacDet);

					// add to stiffness matrix
					kLocal = kLocal.add(func);
				}
			}
		}

		// return stiffness matrix
		return kLocal;
	}

	/**
	 * Retuns element mass matrix.
	 * 
	 * @return Element mass matrix.
	 */
	protected DMat computeMassMatrix() {

		// get volume mass
		double ro = getMaterial().getVolumeMass();

		// set number of Gauss points
		int nog = 4;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.tetrahedral_);

		// loop over Gauss points in first direction
		DMat mLocal = new DMat(30, 30);
		for (int i = 0; i < nog; i++) {

			// get first weight factor and support
			double alpha1 = q.getWeight(i);
			double supp1 = q.getSupport1(i);

			// loop over Gauss points in second direction
			for (int j = 0; j < nog; j++) {

				// get second weight factor and support
				double alpha2 = q.getWeight(j);
				double supp2 = q.getSupport2(j);

				// loop over Gauss points in second direction
				for (int k = 0; k < nog; k++) {

					// get third weight factor and support
					double alpha3 = q.getWeight(k);
					double supp3 = q.getSupport3(k);

					// compute cm, xm, ym and zm
					double cm = alpha1 * alpha2 * alpha3 / 64.0;
					cm *= Math.pow(1.0 - supp1, 2.0) * (1.0 - supp2);
					double xm = (1.0 + supp1) / 2.0;
					double ym = (1.0 - supp1) * (1.0 + supp2) / 4.0;
					double zm = (1.0 - supp1) * (1.0 - supp2) * (1 + supp3)
							/ 8.0;

					// get jacobian and compute determinant
					DMat jac = getJacobian(xm, ym, zm);
					double jacDet = jac.determinant();

					// compute shape function matrix and its transpose
					DMat shapeF = computeShapeF(xm, ym, zm);
					DMat shapeFTr = shapeF.transpose();

					// compute function to be integrated
					DMat func = shapeFTr.multiply(shapeF);
					func = func.scale(cm * ro * jacDet);

					// add to mass matrix
					mLocal = mLocal.add(func);
				}
			}
		}

		// return element mass matrix
		return mLocal;
	}

	/**
	 * Computes element stability matrix.
	 * 
	 * @return Element stability matrix.
	 */
	protected DMat computeStabilityMatrix() {

		// set number of Gauss points
		int nog = 4;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.tetrahedral_);

		// loop over Gauss points in first direction
		DMat kLocal = new DMat(30, 30);
		for (int i = 0; i < nog; i++) {

			// get first weight factor and support
			double alpha1 = q.getWeight(i);
			double supp1 = q.getSupport1(i);

			// loop over Gauss points in second direction
			for (int j = 0; j < nog; j++) {

				// get second weight factor and support
				double alpha2 = q.getWeight(j);
				double supp2 = q.getSupport2(j);

				// loop over Gauss points in third direction
				for (int k = 0; k < nog; k++) {

					// get third weight factor and support
					double alpha3 = q.getWeight(k);
					double supp3 = q.getSupport3(k);

					// compute cm, xm, ym and zm
					double cm = alpha1 * alpha2 * alpha3 / 64.0;
					cm *= Math.pow(1.0 - supp1, 2.0) * (1.0 - supp2);
					double xm = (1.0 + supp1) / 2.0;
					double ym = (1.0 - supp1) * (1.0 + supp2) / 4.0;
					double zm = (1.0 - supp1) * (1.0 - supp2) * (1 + supp3)
							/ 8.0;

					// get jacobian and compute determinant
					DMat jac = getJacobian(xm, ym, zm);
					double jacDet = jac.determinant();

					// compute initial stress matrix
					DMat sm = computeInitialStress(xm, ym, zm);

					// get G operator and its transpose
					DMat gop = computeGop(xm, ym, zm, jac);
					DMat gopTr = gop.transpose();

					// compute function to be integrated
					DMat func = gopTr.multiply(sm.multiply(gop));
					func = func.scale(cm * jacDet);

					// add to stability matrix
					kLocal = kLocal.add(func);
				}
			}
		}

		// return stability matrix
		return kLocal;
	}

	/**
	 * Returns element thermal load vector.
	 * 
	 * @return Element thermal load vector.
	 */
	protected DVec computeTempLoadVector() {

		// get material factors
		Material m = getMaterial();
		DMat c = m.getC(Material.threeD_);
		DVec alpha = m.getAlpha(Material.threeD_);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();
		alpha = alpha.scale(theta);

		// set number of Gauss points
		int nog = 4;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.tetrahedral_);

		// loop over Gauss points in first direction
		DVec tLoad = new DVec(30);
		for (int i = 0; i < nog; i++) {

			// get first weight factor and support
			double alpha1 = q.getWeight(i);
			double supp1 = q.getSupport1(i);

			// loop over Gauss points in second direction
			for (int j = 0; j < nog; j++) {

				// get second weight factor and support
				double alpha2 = q.getWeight(j);
				double supp2 = q.getSupport2(j);

				// loop over Gauss points in third direction
				for (int k = 0; k < nog; k++) {

					// get third weight factor and support
					double alpha3 = q.getWeight(k);
					double supp3 = q.getSupport3(k);

					// compute cm, xm, ym and zm
					double cm = alpha1 * alpha2 * alpha3 / 64.0;
					cm *= Math.pow(1.0 - supp1, 2.0) * (1.0 - supp2);
					double xm = (1.0 + supp1) / 2.0;
					double ym = (1.0 - supp1) * (1.0 + supp2) / 4.0;
					double zm = (1.0 - supp1) * (1.0 - supp2) * (1 + supp3)
							/ 8.0;

					// get jacobian and compute determinant
					DMat jac = getJacobian(xm, ym, zm);
					double jacDet = jac.determinant();

					// get B operator and its transpose
					DMat bop = computeBop(xm, ym, zm, jac);
					DMat bopTr = bop.transpose();

					// compute function to be integrated
					DVec func = bopTr.multiply(c.multiply(alpha));
					func = func.scale(cm * jacDet);

					// add to temperature load vector
					tLoad = tLoad.add(func);
				}
			}
		}

		// return element thermal load vector
		return tLoad;
	}

	/**
	 * Computes and returns element B operator.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param jac
	 *            The jacobian matrix.
	 * @return Element B operator.
	 */
	private DMat computeBop(double eps1, double eps2, double eps3, DMat jac) {

		// compute inverse of jacobian
		DMat jacInv = jac.invert();

		// get interpolation of element
		Interpolation3D intF = getInterpolation();

		// compute and return B operator matrix
		DMat bop = new DMat(6, 30);

		// loop over nodes of element
		for (int i = 0; i < 10; i++) {

			// compute nodal B operator
			DMat bopNode = new DMat(6, 3);
			double der1 = intF.getDer1Function(eps1, eps2, eps3, i);
			double der2 = intF.getDer2Function(eps1, eps2, eps3, i);
			double der3 = intF.getDer3Function(eps1, eps2, eps3, i);
			double val1 = jacInv.get(0, 0) * der1 + jacInv.get(0, 1) * der2
					+ jacInv.get(0, 2) * der3;
			double val2 = jacInv.get(1, 0) * der1 + jacInv.get(1, 1) * der2
					+ jacInv.get(1, 2) * der3;
			double val3 = jacInv.get(2, 0) * der1 + jacInv.get(2, 1) * der2
					+ jacInv.get(2, 2) * der3;
			bopNode.set(0, 0, val1);
			bopNode.set(4, 2, val1);
			bopNode.set(5, 1, val1);
			bopNode.set(1, 1, val2);
			bopNode.set(3, 2, val2);
			bopNode.set(5, 0, val2);
			bopNode.set(2, 2, val3);
			bopNode.set(3, 1, val3);
			bopNode.set(4, 0, val3);

			// set nodal B operator to element B operator
			bop = bop.setSubMatrix(bopNode, 0, 3 * i);
		}

		// return element B operator
		return bop;
	}

	/**
	 * Computes shape function matrix of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The shape function matrix of element.
	 */
	private DMat computeShapeF(double eps1, double eps2, double eps3) {

		// get interpolation of element
		Interpolation3D intF = getInterpolation();

		// build shape function matrix
		DMat shape = new DMat(3, 30);

		// compute and return shape function matrix
		for (int i = 0; i < 10; i++) {
			double val = intF.getFunction(eps1, eps2, eps3, i);
			shape.set(0, 3 * i, val);
			shape.set(1, 3 * i + 1, val);
			shape.set(2, 3 * i + 2, val);
		}
		return shape;
	}

	/**
	 * Computes and returns element G operator.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param jac
	 *            The jacobian matrix.
	 * @return Element G operator.
	 */
	private DMat computeGop(double eps1, double eps2, double eps3, DMat jac) {

		// compute inverse of jacobian
		DMat jacInv = jac.invert();

		// get interpolation of element
		Interpolation3D intF = getInterpolation();

		// compute and return G operator matrix
		DMat gop = new DMat(9, 30);

		// loop over nodes of element
		for (int i = 0; i < 10; i++) {

			// compute nodal G operator
			DMat gopNode = new DMat(9, 3);
			double der1 = intF.getDer1Function(eps1, eps2, eps3, i);
			double der2 = intF.getDer2Function(eps1, eps2, eps3, i);
			double der3 = intF.getDer3Function(eps1, eps2, eps3, i);
			double val1 = jacInv.get(0, 0) * der1 + jacInv.get(0, 1) * der2
					+ jacInv.get(0, 2) * der3;
			double val2 = jacInv.get(1, 0) * der1 + jacInv.get(1, 1) * der2
					+ jacInv.get(1, 2) * der3;
			double val3 = jacInv.get(2, 0) * der1 + jacInv.get(2, 1) * der2
					+ jacInv.get(2, 2) * der3;
			gopNode.set(0, 0, val1);
			gopNode.set(1, 1, val1);
			gopNode.set(2, 2, val1);
			gopNode.set(3, 0, val2);
			gopNode.set(4, 1, val2);
			gopNode.set(5, 2, val2);
			gopNode.set(6, 0, val3);
			gopNode.set(6, 1, val3);
			gopNode.set(6, 2, val3);

			// set nodal G operator to element G operator
			gop = gop.setSubMatrix(gopNode, 0, 3 * i);
		}

		// return element G operator
		return gop;
	}
}
