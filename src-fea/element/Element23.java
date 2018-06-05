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
 * Interpolation family: Serendipity, Number of nodes: 20, Geometry: Hexahedral,
 * Dofs (per node): ux, uy, uz (global), u1, u2, u3 (local), Mechanical loading:
 * fx, fy, fz (global), f1, f2, f3 (local), Temperature loading: yes,
 * Displacements: u1, u2, u3, Strains: e11, e22, e33, e12, e13, e23 Stresses:
 * s11, s22, s33, s12, s13, s23.
 * 
 * @author Murat
 * 
 */
public class Element23 extends Element3D {

	private static final long serialVersionUID = 1L;

	/** The nodes of element. */
	private Node[] nodes_ = new Node[20];

	/** The element global dof array (per node). */
	private int[] globalDofArray_ = { Element.ux_, Element.uy_, Element.uz_ };

	/** The element local dof array (per node). */
	private int[] localDofArray_ = { Element.u1_, Element.u2_, Element.u3_ };

	/**
	 * Creates Element23 element. Nodes should be given in counter-clockwise
	 * order starting from the top side.
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
	 * @param n11
	 *            ...
	 * @param n12
	 *            ...
	 * @param n13
	 *            ...
	 * @param n14
	 *            ...
	 * @param n15
	 *            ...
	 * @param n16
	 *            ...
	 * @param n17
	 *            ...
	 * @param n18
	 *            ...
	 * @param n19
	 *            ...
	 * @param n20
	 *            ...
	 */
	public Element23(Node n1, Node n2, Node n3, Node n4, Node n5, Node n6,
			Node n7, Node n8, Node n9, Node n10, Node n11, Node n12, Node n13,
			Node n14, Node n15, Node n16, Node n17, Node n18, Node n19, Node n20) {
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
		nodes_[10] = n11;
		nodes_[11] = n12;
		nodes_[12] = n13;
		nodes_[13] = n14;
		nodes_[14] = n15;
		nodes_[15] = n16;
		nodes_[16] = n17;
		nodes_[17] = n18;
		nodes_[18] = n19;
		nodes_[19] = n20;
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
		return ElementLibrary.element23_;
	}

	/**
	 * Returns the geometry of element.
	 */
	public int getGeometry() {
		return Element3D.hexahedral_;
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
		DVec uGlobal = new DVec(60);
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
		DVec u11 = getNodes()[10].getUnknown(Node.global_);
		DVec u12 = getNodes()[11].getUnknown(Node.global_);
		DVec u13 = getNodes()[12].getUnknown(Node.global_);
		DVec u14 = getNodes()[13].getUnknown(Node.global_);
		DVec u15 = getNodes()[14].getUnknown(Node.global_);
		DVec u16 = getNodes()[15].getUnknown(Node.global_);
		DVec u17 = getNodes()[16].getUnknown(Node.global_);
		DVec u18 = getNodes()[17].getUnknown(Node.global_);
		DVec u19 = getNodes()[18].getUnknown(Node.global_);
		DVec u20 = getNodes()[19].getUnknown(Node.global_);
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
			uGlobal.set(i + 30, u11.get(i));
			uGlobal.set(i + 33, u12.get(i));
			uGlobal.set(i + 36, u13.get(i));
			uGlobal.set(i + 39, u14.get(i));
			uGlobal.set(i + 42, u15.get(i));
			uGlobal.set(i + 45, u16.get(i));
			uGlobal.set(i + 48, u17.get(i));
			uGlobal.set(i + 51, u18.get(i));
			uGlobal.set(i + 54, u19.get(i));
			uGlobal.set(i + 57, u20.get(i));
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
		DVec uGlobal = new DVec(60);
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
		DVec u11 = getNodes()[10].getUnknown(Node.global_);
		DVec u12 = getNodes()[11].getUnknown(Node.global_);
		DVec u13 = getNodes()[12].getUnknown(Node.global_);
		DVec u14 = getNodes()[13].getUnknown(Node.global_);
		DVec u15 = getNodes()[14].getUnknown(Node.global_);
		DVec u16 = getNodes()[15].getUnknown(Node.global_);
		DVec u17 = getNodes()[16].getUnknown(Node.global_);
		DVec u18 = getNodes()[17].getUnknown(Node.global_);
		DVec u19 = getNodes()[18].getUnknown(Node.global_);
		DVec u20 = getNodes()[19].getUnknown(Node.global_);
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
			uGlobal.set(i + 30, u11.get(i));
			uGlobal.set(i + 33, u12.get(i));
			uGlobal.set(i + 36, u13.get(i));
			uGlobal.set(i + 39, u14.get(i));
			uGlobal.set(i + 42, u15.get(i));
			uGlobal.set(i + 45, u16.get(i));
			uGlobal.set(i + 48, u17.get(i));
			uGlobal.set(i + 51, u18.get(i));
			uGlobal.set(i + 54, u19.get(i));
			uGlobal.set(i + 57, u20.get(i));
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
		int geometry = Interpolation3D.hexahedral_;
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
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.cube_);

		// loop over Gauss points in first direction
		DMat kLocal = new DMat(60, 60);
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

					// get jacobian and compute determinant
					DMat jac = getJacobian(supp1, supp2, supp3);
					double jacDet = jac.determinant();

					// get B operator and its transpose
					DMat bop = computeBop(supp1, supp2, supp3, jac);
					DMat bopTr = bop.transpose();

					// compute function to be integrated
					DMat func = bopTr.multiply(c.multiply(bop));
					func = func.scale(alpha1 * alpha2 * alpha3 * jacDet);

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
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.cube_);

		// loop over Gauss points in first direction
		DMat mLocal = new DMat(60, 60);
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

					// get jacobian and compute determinant
					DMat jac = getJacobian(supp1, supp2, supp3);
					double jacDet = jac.determinant();

					// compute shape function matrix and its transpose
					DMat shapeF = computeShapeF(supp1, supp2, supp3);
					DMat shapeFTr = shapeF.transpose();

					// compute function to be integrated
					DMat func = shapeFTr.multiply(shapeF);
					func = func.scale(alpha1 * alpha2 * alpha3 * ro * jacDet);

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
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.cube_);

		// loop over Gauss points in first direction
		DMat gLocal = new DMat(60, 60);
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

					// get jacobian and compute determinant
					DMat jac = getJacobian(supp1, supp2, supp3);
					double jacDet = jac.determinant();

					// compute initial stress matrix
					DMat sm = computeInitialStress(supp1, supp2, supp3);

					// get G operator and its transpose
					DMat gop = computeGop(supp1, supp2, supp3, jac);
					DMat gopTr = gop.transpose();

					// compute function to be integrated
					DMat func = gopTr.multiply(sm.multiply(gop));
					func = func.scale(alpha1 * alpha2 * alpha3 * jacDet);

					// add to stability matrix
					gLocal = gLocal.add(func);
				}
			}
		}

		// return stability matrix
		return gLocal;
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
		int nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(GaussQuadrature.cube_);

		// loop over Gauss points in first direction
		DVec tLoad = new DVec(60);
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

					// get jacobian and compute determinant
					DMat jac = getJacobian(supp1, supp2, supp3);
					double jacDet = jac.determinant();

					// get B operator and its transpose
					DMat bop = computeBop(supp1, supp2, supp3, jac);
					DMat bopTr = bop.transpose();

					// compute function to be integrated
					DVec func = bopTr.multiply(c.multiply(alpha));
					func = func.scale(alpha1 * alpha2 * alpha3 * jacDet);

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
		DMat bop = new DMat(6, 60);

		// loop over nodes of element
		for (int i = 0; i < 20; i++) {

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
		DMat shape = new DMat(3, 60);

		// compute and return shape function matrix
		for (int i = 0; i < 20; i++) {
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
		DMat gop = new DMat(9, 60);

		// loop over nodes of element
		for (int i = 0; i < 20; i++) {

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
