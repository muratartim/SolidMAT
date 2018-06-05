package element;

import material.Isotropic;
import math.Interpolation1D;
import matrix.DMat;
import matrix.DVec;
import node.Node;

/**
 * Class for Element38. Properties of element are; Mechanics: Euler-Bernoulli
 * beam, Geometric: Linear, Material: Orthotropic/Isotropic, Interpolation
 * degree: Linear (for u1 and r1), Cubic (for u2, u3, r2, r3), Interpolation
 * family: Lagrange (for u1 and r1), Hermite (for u2, u3, r2, r3), Number of
 * nodes: 2, Dofs (per node): ux, uy, uz, rx, ry, rz (global), u1, u2, u3, r1,
 * r2, r3 (local), Mechanical loading: fx, fy, fz, mx, my, mz (global), f1, f2,
 * f3, m1, m2, m3 (local), Temperature loading: yes, Displacements: u1, u2, u3,
 * r1, r2, r3, Strains: e11, e12, e13, Stresses: s11, s12, s13, Internal forces:
 * N1, V2, V3, T1, M2, M3.
 * 
 * @author Murat
 * 
 */
public class Element38 extends Element1D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[2];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_,
			Element.rx_, Element.ry_, Element.rz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_, Element.u2_, Element.u3_,
			Element.r1_, Element.r2_, Element.r3_ };

	/**
	 * Creates Element38 element.
	 * 
	 * @param n1
	 *            The first node of element.
	 * @param n2
	 *            The second node of element.
	 */
	public Element38(Node n1, Node n2) {
		nodes_[0] = n1;
		nodes_[1] = n2;
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
		return ElementLibrary.element38_;
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
		int degree = Interpolation1D.linear_;

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
		Isotropic m = (Isotropic) getMaterial();
		double e = m.getElasticModulus();
		double g = m.getShearModulus();
		double a = getSection().getArea(0);
		double i2 = getSection().getInertiaX2(0);
		double i3 = getSection().getInertiaX3(0);
		double tc = getSection().getTorsionalConstant(0);
		double l = getLength();

		// compute factors
		double lambda = e * a;
		double beta = g * tc;
		double phi = e * i3;
		double theta = e * i2;

		// compute sub-matrix k1
		DMat k1 = new DMat(2, 2);
		k1.set(0, 0, 0.5);
		k1.set(0, 1, -0.5);
		k1.set(1, 0, -0.5);
		k1.set(1, 1, 0.5);
		k1 = k1.scale(2.0 / l);

		// compute sub-matrix k2
		DMat k2 = new DMat(2, 2);
		k2.set(0, 0, 12.0);
		k2.set(0, 1, -12.0);
		k2.set(1, 0, -12.0);
		k2.set(1, 1, 12.0);
		k2 = k2.scale(1.0 / (l * l * l));

		// compute sub-matrix k3
		DMat k3 = new DMat(2, 2);
		k3.set(0, 0, 4.0);
		k3.set(0, 1, 2.0);
		k3.set(1, 0, 2.0);
		k3.set(1, 1, 4.0);
		k3 = k3.scale(1.0 / l);

		// compute sub-matrix k4
		DMat k4 = new DMat(2, 2);
		k4.set(0, 0, 6.0);
		k4.set(0, 1, 6.0);
		k4.set(1, 0, -6.0);
		k4.set(1, 1, -6.0);
		k4 = k4.scale(1.0 / (l * l));

		// insert sub-matrices to stiffness matrix
		DMat kLocal = new DMat(12, 12);
		for (int i = 0; i < k1.rowCount(); i++) {
			for (int j = 0; j < k1.columnCount(); j++) {
				kLocal.set(i, j, lambda * k1.get(i, j));
				kLocal.set(i + 2, j + 2, phi * k2.get(i, j));
				kLocal.set(i + 2, j + 10, phi * k4.get(i, j));
				kLocal.set(i + 4, j + 4, theta * k2.get(i, j));
				kLocal.set(i + 4, j + 8, -theta * k4.get(i, j));
				kLocal.set(i + 6, j + 6, beta * k1.get(i, j));
				kLocal.set(i + 8, j + 8, theta * k3.get(i, j));
				kLocal.set(i + 10, j + 10, phi * k3.get(i, j));
			}
		}
		kLocal = kLocal.mirror();

		// node-wise sorting of element stiffness matrix
		// sort by columns
		DMat kLocal1 = new DMat(12, 12);
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < 2; k++)
					kLocal1.set(i, j + 6 * k, kLocal.get(i, 2 * j + k));

		// sort by rows
		DMat kLocal2 = new DMat(12, 12);
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 12; j++)
				for (int k = 0; k < 2; k++)
					kLocal2.set(i + 6 * k, j, kLocal1.get(2 * i + k, j));

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
		DMat k1 = new DMat(2, 2);
		k1.set(0, 0, 2.0 / 3.0);
		k1.set(0, 1, 1.0 / 3.0);
		k1.set(1, 0, 1.0 / 3.0);
		k1.set(1, 1, 2.0 / 3.0);
		k1 = k1.scale(jac);

		// compute local mass matrix
		DMat mLocal = new DMat(12, 12);
		for (int i = 0; i < k1.rowCount(); i++) {
			for (int j = 0; j < k1.columnCount(); j++) {
				mLocal.set(i, j, ro * a * k1.get(i, j));
				mLocal.set(i + 2, j + 2, ro * a * k1.get(i, j));
				mLocal.set(i + 4, j + 4, ro * a * k1.get(i, j));
				mLocal.set(i + 6, j + 6, ro * i1 * k1.get(i, j));
				mLocal.set(i + 8, j + 8, ro * i2 * k1.get(i, j));
				mLocal.set(i + 10, j + 10, ro * i3 * k1.get(i, j));
			}
		}

		// node-wise sorting of element mass matrix
		// sort by columns
		DMat mLocal1 = new DMat(12, 12);
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < 2; k++)
					mLocal1.set(i, j + 6 * k, mLocal.get(i, 2 * j + k));

		// sort by rows
		DMat mLocal2 = new DMat(12, 12);
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 12; j++)
				for (int k = 0; k < 2; k++)
					mLocal2.set(i + 6 * k, j, mLocal1.get(2 * i + k, j));

		// compute global mass matrix
		DMat tr = computeTransformation();
		return mLocal2.transform(tr, DMat.toGlobal_);
	}

	// protected DMat computeMassMatrix() {
	//
	// // get factors
	// double l = getLength();
	// double ro = getMaterial().getVolumeMass();
	// double a = getSection().getArea();
	// double i1 = getSection().getInertiaX1();
	//
	// // compute sub-matrix k1
	// DMat k1 = new DMat(2, 2);
	// k1.set(0, 0, 4.0 / 3.0);
	// k1.set(0, 1, 2.0 / 3.0);
	// k1.set(1, 0, 2.0 / 3.0);
	// k1.set(1, 1, 4.0 / 3.0);
	// k1 = k1.scale(1.0 / l);
	//
	// // compute sub-matrix k2
	// DMat k2 = new DMat(2, 2);
	// k2.set(0, 0, 13.0 / 135.0);
	// k2.set(0, 1, 9.0 / 70.0);
	// k2.set(1, 0, 9.0 / 70.0);
	// k2.set(1, 1, 13.0 / 135.0);
	// k2 = k2.scale(l);
	//
	// // compute sub-matrix k3
	// DMat k3 = new DMat(2, 2);
	// k3.set(0, 0, 1.0 / 105.0);
	// k3.set(0, 1, -1.0 / 140.0);
	// k3.set(1, 0, -1.0 / 140.0);
	// k3.set(1, 1, 1.0 / 105.0);
	// k3 = k3.scale(l * l * l);
	//
	// // compute sub-matrix k4
	// DMat k4 = new DMat(2, 2);
	// k4.set(0, 0, 11.0 / 210.0);
	// k4.set(0, 1, -13.0 / 420.0);
	// k4.set(1, 0, 13.0 / 420.0);
	// k4.set(1, 1, -11.0 / 210.0);
	// k4 = k4.scale(l * l);
	//
	// // compute local mass matrix
	// DMat mLocal = new DMat(12, 12);
	// for (int i = 0; i < k1.rowCount(); i++) {
	// for (int j = 0; j < k1.columnCount(); j++) {
	// mLocal.set(i, j, ro * a * k1.get(i, j));
	// mLocal.set(i + 2, j + 2, ro * a * k2.get(i, j));
	// mLocal.set(i + 2, j + 10, ro * a * k4.get(i, j));
	// mLocal.set(i + 4, j + 4, ro * a * k2.get(i, j));
	// mLocal.set(i + 4, j + 8, -ro * a * k4.get(i, j));
	// mLocal.set(i + 6, j + 6, ro * i1 * k1.get(i, j));
	// mLocal.set(i + 8, j + 8, ro * a * k3.get(i, j));
	// mLocal.set(i + 10, j + 10, ro * a * k3.get(i, j));
	// }
	// }
	// mLocal = mLocal.mirror();
	//
	// // node-wise sorting of element mass matrix
	// // sort by columns
	// DMat mLocal1 = new DMat(12, 12);
	// for (int i = 0; i < 12; i++)
	// for (int j = 0; j < 6; j++)
	// for (int k = 0; k < 2; k++)
	// mLocal1.set(i, j + 6 * k, mLocal.get(i, 2 * j + k));
	//
	// // sort by rows
	// DMat mLocal2 = new DMat(12, 12);
	// for (int i = 0; i < 6; i++)
	// for (int j = 0; j < 12; j++)
	// for (int k = 0; k < 2; k++)
	// mLocal2.set(i + 6 * k, j, mLocal1.get(2 * i + k, j));
	//
	// // compute global mass matrix
	// DMat tr = computeTransformation();
	// return mLocal2.transform(tr, DMat.toGlobal_);
	// }
	@Override
	protected DMat computeStabilityMatrix() {

		// get constants
		double a = getSection().getArea(0);
		double l = getLength();
		double s11 = 100.0 / a;

		// compute sub-matrix k1
		DMat k1 = new DMat(2, 2);
		k1.set(0, 0, 0.5);
		k1.set(0, 1, -0.5);
		k1.set(1, 0, -0.5);
		k1.set(1, 1, 0.5);
		k1 = k1.scale(s11 * 2.0 / l);

		// compute sub-matrix k5
		DMat k5 = new DMat(2, 2);
		k5.set(0, 0, 6.0 / 5.0);
		k5.set(0, 1, -6.0 / 5.0);
		k5.set(1, 0, -6.0 / 5.0);
		k5.set(1, 1, 6.0 / 5.0);
		k5 = k5.scale(s11 * 1.0 / l);

		// compute sub-matrix k6
		DMat k6 = new DMat(2, 2);
		k6.set(0, 0, 2.0 / 15.0);
		k6.set(0, 1, -1.0 / 30.0);
		k6.set(1, 0, -1.0 / 30.0);
		k6.set(1, 1, 2.0 / 15.0);
		k6 = k6.scale(s11 * l);

		// compute sub-matrix k7
		DMat k7 = new DMat(2, 2);
		k7.set(0, 0, 1.0 / 10.0);
		k7.set(0, 1, 1.0 / 10.0);
		k7.set(1, 0, -1.0 / 10.0);
		k7.set(1, 1, -1.0 / 10.0);
		k7 = k7.scale(s11);

		// insert sub-matrices to stiffness matrix
		DMat kLocal = new DMat(12, 12);
		for (int i = 0; i < k1.rowCount(); i++) {
			for (int j = 0; j < k1.columnCount(); j++) {
				kLocal.set(i, j, a * k1.get(i, j));
				kLocal.set(i + 2, j + 2, a * k5.get(i, j));
				kLocal.set(i + 2, j + 10, a * k7.get(i, j));
				kLocal.set(i + 4, j + 4, a * k5.get(i, j));
				kLocal.set(i + 4, j + 8, -a * k7.get(i, j));
				kLocal.set(i + 8, j + 8, a * k6.get(i, j));
				kLocal.set(i + 10, j + 10, a * k6.get(i, j));
			}
		}
		kLocal = kLocal.mirror();

		// node-wise sorting of element stiffness matrix
		// sort by columns
		DMat kLocal1 = new DMat(12, 12);
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < 2; k++)
					kLocal1.set(i, j + 6 * k, kLocal.get(i, 2 * j + k));

		// sort by rows
		DMat kLocal2 = new DMat(12, 12);
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 12; j++)
				for (int k = 0; k < 2; k++)
					kLocal2.set(i + 6 * k, j, kLocal1.get(2 * i + k, j));

		// compute global stiffness matrix
		DMat tr = computeTransformation();
		return kLocal2.transform(tr, DMat.toGlobal_);
	}

	@Override
	protected DVec computeTempLoadVector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DVec getDisplacement(double eps1, double eps2, double eps3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getInternalForce(int type, double eps1, double eps2,
			double eps3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DMat getStrain(double eps1, double eps2, double eps3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DMat getStress(double eps1, double eps2, double eps3) {
		// TODO Auto-generated method stub
		return null;
	}
}