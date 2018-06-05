package element;

import java.util.Vector;

import node.Node;

import material.Material;
import math.Interpolation1D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementTemp;

/**
 * Class for Element34. Properties of element are; Mechanics: XZ Planar Truss,
 * Geometric: Linear, Material: Orthotropic/Isotropic, Interpolation degree:
 * Cubic, Interpolation family: Lagrange, Number of nodes: 4, Dofs (per node):
 * ux, uz (global), u1 (local), Mechanical loading: fx, fz (global), f1 (local),
 * Temperature loading: yes, Displacements: u1, Strains: e11, e22, e33,
 * Stresses: s11, Internal forces: N1. Note: Element must be used in XZ plane.
 * 
 * @author Murat
 * 
 */
public class Element34 extends Element1D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[4];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_ };

	/**
	 * Creates Element34 element.
	 * 
	 * @param n1
	 *            The first (starting) node of element.
	 * @param n2
	 *            The second (middle-1) node of element.
	 * @param n3
	 *            The third (middle-2) node of element.
	 * @param n4
	 *            The fourth (ending) node of element.
	 */
	public Element34(Node n1, Node n2, Node n3, Node n4) {

		// set nodes
		nodes_[0] = n1;
		nodes_[1] = n2;
		nodes_[2] = n3;
		nodes_[3] = n4;

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
		return ElementLibrary.element34_;
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
		DVec uGlobal = new DVec(8);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		for (int i = 0; i < 2; i++) {
			uGlobal.set(i, u1.get(2 * i));
			uGlobal.set(i + 2, u2.get(2 * i));
			uGlobal.set(i + 4, u3.get(2 * i));
			uGlobal.set(i + 6, u4.get(2 * i));
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
		DVec uGlobal = new DVec(8);
		DVec u1 = getNodes()[0].getUnknown(Node.global_);
		DVec u2 = getNodes()[1].getUnknown(Node.global_);
		DVec u3 = getNodes()[2].getUnknown(Node.global_);
		DVec u4 = getNodes()[3].getUnknown(Node.global_);
		for (int i = 0; i < 2; i++) {
			uGlobal.set(i, u1.get(2 * i));
			uGlobal.set(i + 2, u2.get(2 * i));
			uGlobal.set(i + 4, u3.get(2 * i));
			uGlobal.set(i + 6, u4.get(2 * i));
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
		int degree = Interpolation1D.cubic_;

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
		DMat kLocal = new DMat(4, 4);
		kLocal.set(0, 0, 148.0);
		kLocal.set(0, 1, -189.0);
		kLocal.set(0, 2, 54.0);
		kLocal.set(0, 3, -13.0);
		kLocal.set(1, 1, 432.0);
		kLocal.set(1, 2, -297.0);
		kLocal.set(1, 3, 54.0);
		kLocal.set(2, 2, 432.0);
		kLocal.set(2, 3, -189.0);
		kLocal.set(3, 3, 148.0);
		kLocal = kLocal.mirror();
		kLocal = kLocal.scale(a / (40.0 * s11 * l));

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
		DMat mLocal = new DMat(4, 4);
		mLocal.set(0, 0, 152.0);
		mLocal.set(0, 1, 118.0);
		mLocal.set(0, 2, -43.0);
		mLocal.set(0, 3, 23.0);
		mLocal.set(1, 1, 771.0);
		mLocal.set(1, 2, -96.0);
		mLocal.set(1, 3, -43.0);
		mLocal.set(2, 2, 771.0);
		mLocal.set(2, 3, -189.0);
		mLocal.set(3, 3, 152.0);
		mLocal = mLocal.mirror();
		mLocal = mLocal.scale(ro * a * l / 2000.0);

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

		// get length
		double l = getLength();
		double a = getSection().getArea(0);

		// get average normal stress
		double s11 = computeInitialStress(0.0);

		// compute local stability matrix
		DMat gLocal = new DMat(4, 4);
		gLocal.set(0, 0, 148.0);
		gLocal.set(0, 1, -189.0);
		gLocal.set(0, 2, 54.0);
		gLocal.set(0, 3, -13.0);
		gLocal.set(1, 1, 432.0);
		gLocal.set(1, 2, -297.0);
		gLocal.set(1, 3, 54.0);
		gLocal.set(2, 2, 432.0);
		gLocal.set(2, 3, -189.0);
		gLocal.set(3, 3, 148.0);
		gLocal = gLocal.mirror();
		gLocal = gLocal.scale(s11 * a / (40.0 * l));

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
		DVec tLoad = new DVec(4);
		tLoad.set(0, -1.0);
		tLoad.set(3, 1.0);
		tLoad = tLoad.scale(a * theta * phi1 / s11);

		// compute global load vector
		DMat tr = computeTransformation();
		return tLoad.transform(tr, DMat.toGlobal_);
	}

	/**
	 * Ties the internal nodes of the element.
	 * 
	 */
	private void tieInternalNodes() {

		// set 3D transformation matrix of element to internal nodes
		DMat tr = getTransformation();
		nodes_[1].setTransformation(tr);
		nodes_[2].setTransformation(tr);
	}
}