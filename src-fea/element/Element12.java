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

import section.Section;

import node.Node;

import material.Material;
import math.GaussQuadrature;
import math.Interpolation2D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementTemp;

/**
 * Class for Element12. Properties of element are; Mechanics: Plane (plane
 * strain), Geometric: Linear, Material: Orthotropic/Isotropic, Interpolation
 * degree: Biquadratic, Interpolation family: Lagrange, Number of nodes: 6,
 * Geometry: Triangular, Dofs (per node): ux, uy, uz (global), u1, u2 (local),
 * Mechanical loading: fx, fy, fz (global), f1, f2 (local), Temperature loading:
 * yes, Displacements: u1, u2, Strains: e11, e22, e12, Stresses: s11, s22, s33,
 * s12, Internal forces: P11, N22, Q12.
 * 
 * @author Murat
 * 
 */
public class Element12 extends Element2D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[6];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_, Element.u2_ };

	/**
	 * Creates Element12 element. Nodes should be given in counter-clockwise
	 * order.
	 * 
	 * @param n1
	 *            The first node of element (bottom-right).
	 * @param n2
	 *            The second node of element (top).
	 * @param n3
	 *            The third node of element (bottom-left).
	 * @param n4
	 *            The fourth node of element (between n1 and n2).
	 * @param n5
	 *            The fifth node of element (between n2 and n3).
	 * @param n6
	 *            The sixth node of element (between n3 and n1).
	 */
	public Element12(Node n1, Node n2, Node n3, Node n4, Node n5, Node n6) {
		nodes_[0] = n1;
		nodes_[1] = n2;
		nodes_[2] = n3;
		nodes_[3] = n4;
		nodes_[4] = n5;
		nodes_[5] = n6;

		// tie internal nodes
		tieInternalNodes();
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
		return ElementLibrary.element12_;
	}

	/**
	 * Returns the geometry of element.
	 */
	public int getGeometry() {
		return Element2D.triangular_;
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
	 * @return The local approximated displacement vector of element.
	 */
	public DVec getDisplacement(double eps1, double eps2, double eps3) {

		// get nodal unknowns
		DVec uGlobal = new DVec(18);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		DVec u5 = getNodes()[4].getUnknown(Node.global_);
		DVec u6 = getNodes()[5].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
			uGlobal.set(i + 9, u4.get(i));
			uGlobal.set(i + 12, u5.get(i));
			uGlobal.set(i + 15, u6.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get shape function
		Interpolation2D shapeF = getInterpolation();

		// compute local approximated displacements
		double[] disp = new double[6];
		for (int i = 0; i < nodes_.length; i++) {
			disp[0] += uLocal.get(2 * i) * shapeF.getFunction(eps1, eps2, i);
			disp[1] += uLocal.get(2 * i + 1)
					* shapeF.getFunction(eps1, eps2, i);
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
	 * @return The elastic strain tensor of element.
	 */
	public DMat getStrain(double eps1, double eps2, double eps3) {

		// get nodal unknowns
		DVec uGlobal = new DVec(18);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		DVec u5 = getNodes()[4].getUnknown(Node.global_);
		DVec u6 = getNodes()[5].getUnknown(Node.global_);
		for (int i = 0; i < 3; i++) {
			uGlobal.set(i, u1.get(i));
			uGlobal.set(i + 3, u2.get(i));
			uGlobal.set(i + 6, u3.get(i));
			uGlobal.set(i + 9, u4.get(i));
			uGlobal.set(i + 12, u5.get(i));
			uGlobal.set(i + 15, u6.get(i));
		}

		// transform into local coordinates
		DMat tr = computeTransformation();
		DVec uLocal = uGlobal.transform(tr, DMat.toLocal_);

		// get jacobian matrix
		DMat jac = getJacobian(eps1, eps2);

		// compute strain-11-22-12
		DVec pStrain = computeBop(eps1, eps2, jac).multiply(uLocal);

		// build and return strain tensor
		DMat strain = new DMat(3, 3);
		strain.set(0, 0, pStrain.get(0));
		strain.set(0, 1, pStrain.get(2) / 2.0);
		strain.set(1, 0, pStrain.get(2) / 2.0);
		strain.set(1, 1, pStrain.get(1));
		return strain;
	}

	/**
	 * Returns the Cauchy stress tensor of element.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @return The Cauchy stress tensor of element.
	 */
	public DMat getStress(double eps1, double eps2, double eps3) {

		// get material values
		Material m = getMaterial();
		DMat c = m.getC(Material.planeStrain_);
		DVec alpha = m.getAlpha(Material.planeStrain_);
		DMat c3D = m.getC(Material.threeD_);
		DMat c1 = new DMat(1, 3);
		c1.set(0, 0, c3D.get(2, 0));
		c1.set(0, 1, c3D.get(2, 1));
		c1.set(0, 2, c3D.get(2, 5));

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();
		alpha = alpha.scale(theta);

		// get strain tensor
		DMat strain = getStrain(eps1, eps2, 0.0);
		DVec strainVec = new DVec(3);
		strainVec.set(0, strain.get(0, 0));
		strainVec.set(1, strain.get(1, 1));
		strainVec.set(2, 2.0 * strain.get(0, 1));

		// compute stress vector
		DVec stressVec = c.multiply(strainVec.subtract(alpha));

		// compute s-33
		double s33 = c1.multiply(strainVec.subtract(alpha)).get(0);

		// build and return stress tensor
		DMat stress = new DMat(3, 3);
		stress.set(0, 0, stressVec.get(0));
		stress.set(0, 1, stressVec.get(2));
		stress.set(1, 0, stressVec.get(2));
		stress.set(1, 1, stressVec.get(1));
		stress.set(2, 2, s33);
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
	 * @return The internal force of element.
	 */
	public double getInternalForce(int type, double eps1, double eps2,
			double eps3) {

		// check type of demanded internal force
		if (type < 0 || type > 2)
			return 0.0;

		// get thickness
		double h = getSection().getDimension(Section.thickness_);

		// get stress tensor
		DMat stress = getStress(eps1, eps2, 0.0);

		// internal force P11 demanded
		if (type == Element2D.P11_)
			return stress.get(0, 0) * h;

		// internal force N22 demanded
		else if (type == Element2D.N22_)
			return stress.get(1, 1) * h;

		// internal force Q12 demanded
		else if (type == Element2D.Q12_)
			return stress.get(0, 1) * h;
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
	protected Interpolation2D getInterpolation() {

		// set geometry and degree
		int geometry = Interpolation2D.triangular_;
		int degree = Interpolation2D.biquadratic_;

		// return interpolation function
		return new Interpolation2D(degree, geometry);
	}

	/**
	 * Computes element stiffness matrix.
	 * 
	 * @return Element stiffness matrix.
	 */
	protected DMat computeStiffnessMatrix() {

		// get factors
		DMat c = getMaterial().getC(Material.planeStrain_);
		double h = getSection().getDimension(Section.thickness_);

		// set number of Gauss points
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(GaussQuadrature.triangle_);

		// loop over Gauss points
		DMat kLocal = new DMat(12, 12);
		for (int i = 0; i < nog; i++) {

			// get weight factor and supporting points
			double alpha = q.getWeight(i);
			double supp1 = q.getSupport1(i);
			double supp2 = q.getSupport2(i);

			// get jacobian and compute determinant
			DMat jac = getJacobian(supp1, supp2);
			double jacDet = jac.determinant();

			// get B operator and its transpose
			DMat bop = computeBop(supp1, supp2, jac);
			DMat bopTr = bop.transpose();

			// compute function to be integrated
			DMat func = bopTr.multiply(c.multiply(bop));
			func = func.scale(0.5 * alpha * h * jacDet);

			// add to stiffness matrix
			kLocal = kLocal.add(func);
		}

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
		double h = getSection().getDimension(Section.thickness_);

		// set number of Gauss points
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(GaussQuadrature.triangle_);

		// loop over Gauss points
		DMat mLocal = new DMat(12, 12);
		for (int i = 0; i < nog; i++) {

			// get weight factor and supporting points
			double alpha = q.getWeight(i);
			double supp1 = q.getSupport1(i);
			double supp2 = q.getSupport2(i);

			// get jacobian and compute determinant
			DMat jac = getJacobian(supp1, supp2);
			double jacDet = jac.determinant();

			// compute shape function matrix and its transpose
			DMat shapeF = computeShapeF(supp1, supp2);
			DMat shapeFTr = shapeF.transpose();

			// compute function to be integrated
			DMat func = shapeFTr.multiply(shapeF);
			func = func.scale(0.5 * alpha * ro * h * jacDet);

			// add to mass matrix
			mLocal = mLocal.add(func);
		}

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

		// get thickness
		double h = getSection().getDimension(Section.thickness_);

		// set number of Gauss points
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(GaussQuadrature.triangle_);

		// loop over Gauss points
		DMat gLocal = new DMat(12, 12);
		DMat s = new DMat(4, 4);
		for (int i = 0; i < nog; i++) {

			// get weight factor and supporting points
			double alpha = q.getWeight(i);
			double supp1 = q.getSupport1(i);
			double supp2 = q.getSupport2(i);

			// get jacobian and compute determinant
			DMat jac = getJacobian(supp1, supp2);
			double jacDet = jac.determinant();

			// compute initial stress matrix
			DMat sm = computeInitialStress(supp1, supp2);
			s = s.setSubMatrix(sm, 0, 0);
			s = s.setSubMatrix(sm, 2, 2);

			// get G operator and its transpose
			DMat gop = computeGop(supp1, supp2, jac);
			DMat gopTr = gop.transpose();

			// compute function to be integrated
			DMat func = gopTr.multiply(s.multiply(gop));
			func = func.scale(0.5 * alpha * h * jacDet);

			// add to stiffness matrix
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
		DMat c = m.getC(Material.planeStrain_);
		DVec alpha = m.getAlpha(Material.planeStrain_);
		double h = getSection().getDimension(Section.thickness_);

		// get thermal influences
		double theta = 0.0;
		Vector<ElementTemp> ml = getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();
		alpha = alpha.scale(theta);

		// set number of Gauss points
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(GaussQuadrature.triangle_);

		// loop over Gauss points
		DVec tLoad = new DVec(12);
		for (int i = 0; i < nog; i++) {

			// get weight factor and supporting points
			double alpha1 = q.getWeight(i);
			double supp1 = q.getSupport1(i);
			double supp2 = q.getSupport2(i);

			// get jacobian and compute determinant
			DMat jac = getJacobian(supp1, supp2);
			double jacDet = jac.determinant();

			// get B operator and its transpose
			DMat bop = computeBop(supp1, supp2, jac);
			DMat bopTr = bop.transpose();

			// compute function to be integrated
			DVec func = bopTr.multiply(c.multiply(alpha));
			func = func.scale(0.5 * alpha1 * h * jacDet);

			// add to temperature load vector
			tLoad = tLoad.add(func);
		}

		// compute global load vector
		DMat tr = computeTransformation();
		return tLoad.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Computes and returns element B operator.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param jac
	 *            The jacobian matrix.
	 * @return Element B operator.
	 */
	private DMat computeBop(double eps1, double eps2, DMat jac) {

		// compute determinant of jacobian
		double jacDet = jac.determinant();

		// get interpolation of element
		Interpolation2D intF = getInterpolation();

		// compute and return B operator matrix
		DMat bop = new DMat(3, 12);

		// loop over nodes of element
		for (int i = 0; i < 6; i++) {

			// compute nodal B operator
			DMat bopNode = new DMat(3, 2);
			double der1 = intF.getDer1Function(eps1, eps2, i);
			double der2 = intF.getDer2Function(eps1, eps2, i);
			double val1 = jac.get(1, 1) * der1 - jac.get(0, 1) * der2;
			double val2 = -jac.get(1, 0) * der1 + jac.get(0, 0) * der2;
			bopNode.set(0, 0, val1);
			bopNode.set(1, 1, val2);
			bopNode.set(2, 0, val2);
			bopNode.set(2, 1, val1);
			bopNode = bopNode.scale(1.0 / jacDet);

			// set nodal B operator to element B operator
			bop = bop.setSubMatrix(bopNode, 0, 2 * i);
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
	 * @return The shape function matrix of element.
	 */
	private DMat computeShapeF(double eps1, double eps2) {

		// get interpolation of element
		Interpolation2D intF = getInterpolation();

		// build shape function matrix
		DMat shape = new DMat(2, 12);

		// compute and return shape function matrix
		for (int i = 0; i < 6; i++) {
			double val = intF.getFunction(eps1, eps2, i);
			shape.set(0, 2 * i, val);
			shape.set(1, 2 * i + 1, val);
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
	 * @param jac
	 *            The jacobian matrix.
	 * @return Element G operator.
	 */
	private DMat computeGop(double eps1, double eps2, DMat jac) {

		// compute determinant of jacobian
		double jacDet = jac.determinant();

		// get interpolation of element
		Interpolation2D intF = getInterpolation();

		// compute and return G operator matrix
		DMat gop = new DMat(4, 12);

		// loop over nodes of element
		for (int i = 0; i < 6; i++) {

			// compute nodal G operator
			DMat gopNode = new DMat(4, 2);
			double der1 = intF.getDer1Function(eps1, eps2, i);
			double der2 = intF.getDer2Function(eps1, eps2, i);
			double val1 = jac.get(1, 1) * der1 - jac.get(0, 1) * der2;
			double val2 = -jac.get(1, 0) * der1 + jac.get(0, 0) * der2;
			gopNode.set(0, 0, val1);
			gopNode.set(1, 0, val2);
			gopNode.set(2, 1, val1);
			gopNode.set(3, 1, val2);
			gopNode = gopNode.scale(1.0 / jacDet);

			// set nodal G operator to element G operator
			gop = gop.setSubMatrix(gopNode, 0, 2 * i);
		}

		// return element G operator
		return gop;
	}

	/**
	 * Ties the internal nodes of the element.
	 * 
	 */
	private void tieInternalNodes() {

		// set 3D transformation matrix of element to internal nodes
		DMat tr = getTransformation();
		nodes_[3].setTransformation(tr);
		nodes_[4].setTransformation(tr);
		nodes_[5].setTransformation(tr);
	}
}
