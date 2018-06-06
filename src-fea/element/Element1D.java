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

import node.LocalAxis;
import node.Node;

import math.GaussQuadrature;
import math.Interpolation1D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementMechLoad;

/**
 * Class for one dimensional elements.
 * 
 * @author Murat
 * 
 */
public abstract class Element1D extends Element {

	private static final long serialVersionUID = 1L;

	/** Internal forces in local directions. */
	public static final int N1_ = 0, V2_ = 1, V3_ = 2, T1_ = 3, M2_ = 4,
			M3_ = 5;

	/** The axial rotation of element. */
	private double axialRotation_ = 0.0;

	/** The local axis system of element. */
	private LocalAxis localAxis_;

	/**
	 * Sets local axis (axial rotation) to element.
	 * 
	 * @param localAxis
	 *            The local axis system of element.
	 */
	public void setLocalAxis(LocalAxis localAxis) {

		// null assignments
		if (localAxis == null) {
			localAxis_ = localAxis;
			axialRotation_ = 0.0;
		}

		// other assignments
		else {

			// check type of local axis
			if (localAxis.getType() != LocalAxis.line_)
				exceptionHandler("Illegal local axis type for element!");

			// set local axis
			localAxis_ = localAxis;

			// set axial rotation
			axialRotation_ = localAxis_.getValues()[0];
		}
	}

	/**
	 * Retuns the local axis system of element.
	 * 
	 * @return The local axis system of element.
	 */
	public LocalAxis getLocalAxis() {
		return localAxis_;
	}

	/**
	 * Returns length of element.
	 * 
	 * @return The length of element.
	 */
	public double getLength() {

		// get end nodes of element
		Node[] nodes = getNodes();
		Node node1 = nodes[0];
		Node node2 = nodes[nodes.length - 1];

		// compute length
		DVec vec = node2.getPosition().subtract(node1.getPosition());

		// return length
		return vec.l2Norm();
	}

	/**
	 * Returns determinant of jacobian.
	 * 
	 * @return Determinant of jacobian.
	 */
	public double getDetJacobian() {
		return getLength() / 2.0;
	}

	/**
	 * Returns the dimension of element.
	 */
	public int getDimension() {
		return ElementLibrary.oneDimensional_;
	}

	/**
	 * Returns the volume of element.
	 * 
	 * @return The volume of element.
	 */
	public double getVolume() {
		return getLength() * getSection().getArea(0);
	}

	/**
	 * Returns the three dimensional transformation matrix. Local axis x1 is
	 * placed in the axis of element.
	 * 
	 * @return The three dimensional transformation matrix for one dimensional
	 *         elements.
	 */
	public DMat getTransformation() {

		// get end nodes of element
		Node[] nodes = getNodes();
		Node node1 = nodes[0];
		Node node2 = nodes[nodes.length - 1];

		// compute projected lengths
		DVec vec = node2.getPosition().subtract(node1.getPosition());
		double dx = vec.get(0);
		double dy = vec.get(1);
		double dz = vec.get(2);
		double lxy = Math.sqrt(dx * dx + dy * dy);

		// compute Euler angles
		double r1 = axialRotation_;
		double r2 = 0.0;
		if (lxy != 0) {
			if (dy >= 0)
				r2 = Math.toDegrees(Math.acos(dx / lxy));
			else
				r2 = -Math.toDegrees(Math.acos(dx / lxy));
		}
		double r3 = Math.toDegrees(Math.asin(dz / getLength()));

		// return transformation matrix
		return new DMat(r1, r2, r3, DMat.zyx_);
	}

	/**
	 * Computes element mechanical load vector.
	 * 
	 * @return Element mechanical load vector.
	 */
	protected DVec computeMechLoadVector() {

		// get element properties
		int nn = getNodes().length;
		double jac = getDetJacobian();
		Interpolation1D intF = getInterpolation();
		int degree = intF.getDegree();

		// construct transformation matrix
		DMat tr3D = getTransformation();
		DMat tr = new DMat(6 * nn, 6 * nn);
		for (int i = 0; i < nn; i++) {
			tr = tr.setSubMatrix(tr3D, i * 6, i * 6);
			tr = tr.setSubMatrix(tr3D, i * 6 + 3, i * 6 + 3);
		}

		// setup element mechanical load vector
		DVec eLoad = new DVec(6 * nn);

		// get mechanical loads
		Vector<ElementMechLoad> ml = getMechLoads();

		// loop over element mechanical loads
		for (int i = 0; i < ml.size(); i++) {

			// get mechanical load
			ElementMechLoad load = ml.get(i);

			// check if self-weight
			double selfWeight = 0.0;
			if (load.isSelfWeight())
				selfWeight = -getWeight() / getLength();

			// get degree of loading function
			int deg = load.getDegree();

			// set number of Gauss points
			Double n = (deg + degree + 1.0) / 2.0;
			float s = n.floatValue();
			int nog = Math.round(s);

			// create Quadrature
			GaussQuadrature q = new GaussQuadrature(nog,
					GaussQuadrature.oneDimensional_);

			// create loading sub-vector
			double[] r = new double[nn];

			// loop over rows of sub-vector
			for (int j = 0; j < nn; j++) {

				// loop over Gauss points
				for (int k = 0; k < nog; k++) {

					// get weight factor
					double alpha = q.getWeight(k);

					// get value of loading function
					double val1 = 0.0;

					// self weight loading
					if (load.isSelfWeight()) {
						DVec values = new DVec(2);
						values.set(0, selfWeight);
						values.set(1, selfWeight);
						load.setLoadingValues(values);
					}
					val1 = load.getFunction(0, q.getSupport1(k), 0, 0);

					// get value of interpolation function
					double val2 = intF.getFunction(q.getSupport1(k), j);

					// compute integration
					r[j] += alpha * val1 * val2 * jac;
				}
			}

			// get component of mechanical load
			int comp = load.getComponent();

			// create vector of mechanical load
			DVec vec = new DVec(6 * nn);
			for (int j = 0; j < nn; j++)
				vec.set(6 * j + comp, r[j]);

			// transform if load is in local coordinates
			int coord = load.getCoordinateSystem();
			if (coord == ElementMechLoad.local_)
				vec = vec.transform(tr, DMat.toGlobal_);

			// add to element mechanical load vector
			eLoad = eLoad.add(vec);
		}

		// return
		return eLoad;
	}

	/**
	 * Computes element spring stiffness matrix.
	 * 
	 * @return Element spring stiffness matrix.
	 */
	protected DMat computeSpringStiffnessMatrix() {

		// get element properties
		int nn = getNodes().length;
		double jac = getDetJacobian();
		Interpolation1D intF = getInterpolation();
		int degree = intF.getDegree();

		// set number of Gauss points
		Double n = (2 * degree + 1.0) / 2.0;
		float s = n.floatValue();
		int nog = Math.round(s);

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.oneDimensional_);

		// construct transformation matrix
		DMat tr3D = getTransformation();
		DMat tr = new DMat(6 * nn, 6 * nn);
		for (int i = 0; i < nn; i++) {
			tr = tr.setSubMatrix(tr3D, i * 6, i * 6);
			tr = tr.setSubMatrix(tr3D, i * 6 + 3, i * 6 + 3);
		}

		// setup element spring stiffness matrix
		DMat eSpring = new DMat(6 * nn, 6 * nn);

		// check if any springs available
		if (getSprings() != null) {

			// loop over element springs
			for (int i = 0; i < getSprings().size(); i++) {

				// get spring
				ElementSpring spring = getSprings().get(i);

				// get spring stiffness
				double stiff = spring.getValue();

				// create spring stiffness sub-matrix
				double[][] ks = new double[nn][nn];

				// loop over rows of sub-matrix
				for (int j = 0; j < nn; j++) {

					// loop over columns of sub-matrix
					for (int k = 0; k < nn; k++) {

						// loop over Gauss points
						for (int l = 0; l < nog; l++) {

							// get weight factor and values of functions
							double alpha = q.getWeight(l);
							double supp = q.getSupport1(l);
							double value1 = intF.getFunction(supp, j);
							double value2 = intF.getFunction(supp, k);

							// compute integration
							ks[j][k] += stiff * alpha * value1 * value2 * jac;
						}
					}
				}

				// get component of spring
				int comp = spring.getComponent();

				// create stiffness matrix of spring
				DMat ss = new DMat(6 * nn, 6 * nn);
				for (int j = 0; j < nn; j++) {
					for (int k = 0; k < nn; k++)
						ss.set(6 * j + comp, 6 * k + comp, ks[j][k]);
				}

				// transform if spring is in local coordinates
				int coord = spring.getCoordinateSystem();
				if (coord == ElementSpring.local_)
					ss = ss.transform(tr, DMat.toGlobal_);

				// add to element spring stiffness matrix
				eSpring = eSpring.add(ss);
			}
		}

		// return
		return eSpring;
	}

	/**
	 * Computes element additional mass matrix.
	 * 
	 * @return Element additional mass matrix.
	 */
	protected DMat computeAdditionalMassMatrix() {

		// get element properties
		int nn = getNodes().length;
		double jac = getDetJacobian();
		Interpolation1D intF = getInterpolation();
		int degree = intF.getDegree();

		// set number of Gauss points
		Double n = (2 * degree + 1.0) / 2.0;
		float s = n.floatValue();
		int nog = Math.round(s);

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.oneDimensional_);

		// construct transformation matrix
		DMat tr3D = getTransformation();
		DMat tr = new DMat(6 * nn, 6 * nn);
		for (int i = 0; i < nn; i++) {
			tr = tr.setSubMatrix(tr3D, i * 6, i * 6);
			tr = tr.setSubMatrix(tr3D, i * 6 + 3, i * 6 + 3);
		}

		// setup element additional mass matrix
		DMat eAMass = new DMat(6 * nn, 6 * nn);

		// check if any mass available
		if (getAdditionalMasses() != null) {

			// loop over element additional masses
			for (int i = 0; i < getAdditionalMasses().size(); i++) {

				// get mass
				ElementMass mass = getAdditionalMasses().get(i);

				// get additional mass
				double value = mass.getValue();

				// create additional mass sub-matrix
				double[][] ms = new double[nn][nn];

				// loop over rows of sub-matrix
				for (int j = 0; j < nn; j++) {

					// loop over columns of sub-matrix
					for (int k = 0; k < nn; k++) {

						// loop over Gauss points
						for (int l = 0; l < nog; l++) {

							// get weight factor and values of functions
							double alpha = q.getWeight(l);
							double value1 = intF.getFunction(q.getSupport1(l),
									j);
							double value2 = intF.getFunction(q.getSupport1(l),
									k);

							// compute integration
							ms[j][k] += value * alpha * value1 * value2 * jac;
						}
					}
				}

				// get component of additional mass
				int comp = mass.getComponent();

				// create mass matrix of additional mass
				DMat am = new DMat(6 * nn, 6 * nn);
				for (int j = 0; j < nn; j++) {
					for (int k = 0; k < nn; k++)
						am.set(6 * j + comp, 6 * k + comp, ms[j][k]);
				}

				// transform if additional mass is in local coordinates
				int coord = mass.getCoordinateSystem();
				if (coord == ElementMass.local_)
					am = am.transform(tr, DMat.toGlobal_);

				// add to element additional mass matrix
				eAMass = eAMass.add(am);
			}
		}

		// return
		return eAMass;
	}

	/**
	 * Computes and returns initial normal stress. This quantity is used for
	 * computing element stability matrix.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @return Initial normal stress.
	 */
	protected double computeInitialStress(double eps1) {

		// get element stress tensor
		DMat stress = getStress(eps1, 0.0, 0.0);

		// return normal stress
		return -stress.get(0, 0);
	}

	/** Returns the interpolation of element. */
	protected abstract Interpolation1D getInterpolation();
}
