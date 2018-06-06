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

import math.GaussQuadrature;
import math.Interpolation2D;
import matrix.DMat;
import matrix.DVec;

import boundary.ElementMechLoad;

/**
 * Class for two dimensional elements.
 * 
 * @author Murat
 * 
 */
public abstract class Element2D extends Element {

	private static final long serialVersionUID = 1L;

	/** Internal forces in local directions. */
	public static final int P11_ = 0, N22_ = 1, Q12_ = 2, F13_ = 3, H23_ = 4,
			K22_ = 5, M11_ = 6, T12_ = 7;

	/** Static variable for the geometry of two dimensional element. */
	public static final int quadrangular_ = 0, triangular_ = 1;

	/**
	 * Returns area of element. Note: This method includes hard-codes for
	 * obtaining the number of Gauss points.
	 * 
	 * @return The area of element.
	 */
	public double getArea() {

		// get element properties
		Interpolation2D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (degree == Interpolation2D.bilinear_) {
			nog = 1;
		} else if (degree == Interpolation2D.biquadratic_) {
			if (geo == Interpolation2D.quadrangular_)
				nog = 2;
			else if (geo == Interpolation2D.triangular_)
				nog = 3;
		} else if (degree == Interpolation2D.bicubic_)
			nog = 3;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(geo);

		// compute integration for quadrangular geometry
		double area = 0.0;
		if (q.getGeometry() == GaussQuadrature.square_) {

			// loop over Gauss points in first direction
			for (int i = 0; i < nog; i++) {

				// get first weight factor and support
				double alpha1 = q.getWeight(i);
				double supp1 = q.getSupport1(i);

				// loop over Gauss points in second direction
				for (int j = 0; j < nog; j++) {

					// get second weight factor and value of function
					double alpha2 = q.getWeight(j);
					double supp2 = q.getSupport2(j);
					double value = getJacobian(supp1, supp2).determinant();

					// compute area
					area += alpha1 * alpha2 * value;
				}
			}
			return area;
		}

		// compute integration for triangular geometry
		else {

			// loop over Gauss points
			for (int i = 0; i < nog; i++) {

				// get weight factor and value of function
				double alpha = q.getWeight(i);
				double value = getJacobian(q.getSupport1(i), q.getSupport2(i))
						.determinant();

				// compute area
				area += 0.5 * alpha * value;
			}
			return area;
		}
	}

	/**
	 * Returns jacobian matrix.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @return The jacobian matrix.
	 */
	public DMat getJacobian(double eps1, double eps2) {

		// get geometry approximations
		double value1 = getGeoAppx(eps1, eps2, 0, 1);
		double value2 = getGeoAppx(eps1, eps2, 1, 2);
		double value3 = getGeoAppx(eps1, eps2, 1, 1);
		double value4 = getGeoAppx(eps1, eps2, 0, 2);

		// compute and return jacobian matrix
		DMat jac = new DMat(2, 2);
		jac.set(0, 0, value1);
		jac.set(0, 1, value3);
		jac.set(1, 0, value4);
		jac.set(1, 1, value2);
		return jac;
	}

	/** Returns the geometry of element. */
	public abstract int getGeometry();

	/**
	 * Returns the dimension of element.
	 */
	public int getDimension() {
		return ElementLibrary.twoDimensional_;
	}

	/**
	 * Returns the volume of element.
	 * 
	 * @return The volume of element.
	 */
	public double getVolume() {
		return getArea() * getSection().getDimension(Section.thickness_);
	}

	/**
	 * Returns the three dimensional transformation matrix. Local axis x3 is
	 * normal to the element plane.
	 * 
	 * @return The three dimensional transformation matrix for two dimensional
	 *         elements.
	 */
	public DMat getTransformation() {

		// get first three corner nodes of element
		Node[] nodes = getNodes();
		Node node1 = nodes[0]; // point i
		Node node2 = nodes[1]; // point j
		Node node3 = nodes[2]; // point m

		// get position vectors of nodes
		DVec vi = node1.getPosition();
		DVec vj = node2.getPosition();
		DVec vm = node3.getPosition();

		// compute direction cosines of i-j side
		DVec vij = vj.subtract(vi);
		DVec vx = vij.scale(1.0 / vij.l2Norm());

		// compute direction cosines of normal axis
		DVec vim = vm.subtract(vi);
		DVec vz = vij.cross(vim);
		vz = vz.scale(1.0 / vz.l2Norm());

		// compute direction cosines of y' axis
		DVec vy = vz.cross(vx);

		// compute and return transformation matrix
		DMat tr = new DMat(3, 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 0)
					tr.set(i, j, vx.get(j));
				else if (i == 1)
					tr.set(i, j, vy.get(j));
				else if (i == 2)
					tr.set(i, j, vz.get(j));
			}
		}
		return tr;
	}

	/**
	 * Computes element mechanical load vector.
	 * 
	 * @return Element mechanical load vector.
	 */
	protected DVec computeMechLoadVector() {

		// get element properties
		int nn = getNodes().length;
		Interpolation2D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation2D.quadrangular_) {
			if (degree == Interpolation2D.bilinear_)
				nog = 2;
			else if (degree == Interpolation2D.biquadratic_)
				nog = 4;
			else if (degree == Interpolation2D.bicubic_)
				nog = 5;
		} else if (geo == Interpolation2D.triangular_)
			nog = 4;

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(geo);

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
				selfWeight = -getWeight() / getArea();

			// create loading sub-vector
			double[] r = new double[nn];

			// loop over rows of sub-vector
			for (int j = 0; j < nn; j++) {

				// integration for square geometry
				if (q.getGeometry() == GaussQuadrature.square_) {

					// loop over Gauss points in first direction
					for (int k = 0; k < nog; k++) {

						// get first weight factor and support
						double alpha1 = q.getWeight(k);
						double supp1 = q.getSupport1(k);

						// loop over Gauss points in second direction
						for (int l = 0; l < nog; l++) {

							// get second weight factor and support
							double alpha2 = q.getWeight(l);
							double supp2 = q.getSupport2(l);

							// get jacobian and compute determinant
							DMat jac = getJacobian(supp1, supp2);
							double jacDet = jac.determinant();

							// get value of loading function
							double val1 = 0.0;

							// self weight loading
							if (load.isSelfWeight()) {
								DVec values = new DVec(3);
								values.set(0, selfWeight);
								values.set(1, selfWeight);
								values.set(2, selfWeight);
								load.setLoadingValues(values);
							}
							val1 = load.getFunction(geo, supp1, supp2, 0);

							// get value of interpolation function
							double val2 = intF.getFunction(supp1, supp2, j);

							// compute integration
							r[j] += alpha1 * alpha2 * val1 * val2 * jacDet;
						}
					}
				}

				// integration for triangular geometry
				else if (q.getGeometry() == GaussQuadrature.triangle_) {

					// loop over Gauss points
					for (int k = 0; k < nog; k++) {

						// get weight factor and support points
						double alpha = q.getWeight(k);
						double supp1 = q.getSupport1(k);
						double supp2 = q.getSupport2(k);

						// get jacobian and compute determinant
						DMat jac = getJacobian(supp1, supp2);
						double jacDet = jac.determinant();

						// get value of loading function
						double val1 = 0.0;

						// self weight loading
						if (load.isSelfWeight()) {
							DVec values = new DVec(3);
							values.set(0, selfWeight);
							values.set(1, selfWeight);
							values.set(2, selfWeight);
							load.setLoadingValues(values);
						}
						val1 = load.getFunction(geo, supp1, supp2, 0);

						// get value of interpolation function
						double val2 = intF.getFunction(supp1, supp2, j);

						// compute integration
						r[j] += 0.5 * alpha * val1 * val2 * jacDet;
					}
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
		Interpolation2D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation2D.quadrangular_) {
			if (degree == Interpolation2D.bilinear_)
				nog = 2;
			else if (degree == Interpolation2D.biquadratic_)
				nog = 4;
			else if (degree == Interpolation2D.bicubic_)
				nog = 6;
		} else if (geo == Interpolation2D.triangular_) {
			if (degree == Interpolation2D.bilinear_)
				nog = 3;
			else if (degree == Interpolation2D.biquadratic_)
				nog = 7;
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(geo);

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

						// integration for square geometry
						if (q.getGeometry() == GaussQuadrature.square_) {

							// loop over Gauss points in first direction
							for (int l = 0; l < nog; l++) {

								// get first weight factor and support
								double alpha1 = q.getWeight(l);
								double supp1 = q.getSupport1(l);

								// loop over Gauss points in second direction
								for (int m = 0; m < nog; m++) {

									// get second weight factor and support
									double alpha2 = q.getWeight(m);
									double supp2 = q.getSupport2(m);

									// get jacobian and compute determinant
									DMat jac = getJacobian(supp1, supp2);
									double jacDet = jac.determinant();

									// get values of functions
									double val1 = intF.getFunction(supp1,
											supp2, j);
									double val2 = intF.getFunction(supp1,
											supp2, k);

									// compute integration
									ks[j][k] += stiff * alpha1 * alpha2 * val1
											* val2 * jacDet;
								}
							}
						}

						// integration for triangular geometry
						else if (q.getGeometry() == GaussQuadrature.triangle_) {

							// loop over Gauss points
							for (int l = 0; l < nog; l++) {

								// get weight factor and supporting points
								double alpha = q.getWeight(l);
								double supp1 = q.getSupport1(l);
								double supp2 = q.getSupport2(l);

								// get jacobian and compute determinant
								DMat jac = getJacobian(supp1, supp2);
								double jacDet = jac.determinant();

								// get values of functions
								double val1 = intF.getFunction(supp1, supp2, j);
								double val2 = intF.getFunction(supp1, supp2, k);

								// compute integration
								ks[j][k] += 0.5 * stiff * alpha * val1 * val2
										* jacDet;
							}
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
		Interpolation2D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation2D.quadrangular_) {
			if (degree == Interpolation2D.bilinear_)
				nog = 2;
			else if (degree == Interpolation2D.biquadratic_)
				nog = 4;
			else if (degree == Interpolation2D.bicubic_)
				nog = 6;
		} else if (geo == Interpolation2D.triangular_) {
			if (degree == Interpolation2D.bilinear_)
				nog = 3;
			else if (degree == Interpolation2D.biquadratic_)
				nog = 7;
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.twoDimensional_);
		q.setGeometry(geo);

		// construct transformation matrix
		DMat tr3D = getTransformation();
		DMat tr = new DMat(6 * nn, 6 * nn);
		for (int i = 0; i < nn; i++) {
			tr = tr.setSubMatrix(tr3D, i * 6, i * 6);
			tr = tr.setSubMatrix(tr3D, i * 6 + 3, i * 6 + 3);
		}

		// setup element additional mass matrix
		DMat eMass = new DMat(6 * nn, 6 * nn);

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

						// integration for square geometry
						if (q.getGeometry() == GaussQuadrature.square_) {

							// loop over Gauss points in first direction
							for (int l = 0; l < nog; l++) {

								// get first weight factor and support
								double alpha1 = q.getWeight(l);
								double supp1 = q.getSupport1(l);

								// loop over Gauss points in second direction
								for (int m = 0; m < nog; m++) {

									// get second weight factor and support
									double alpha2 = q.getWeight(m);
									double supp2 = q.getSupport2(m);

									// get jacobian and compute determinant
									DMat jac = getJacobian(supp1, supp2);
									double jacDet = jac.determinant();

									// get values of functions
									double val1 = intF.getFunction(supp1,
											supp2, j);
									double val2 = intF.getFunction(supp1,
											supp2, k);

									// compute integration
									ms[j][k] += value * alpha1 * alpha2 * val1
											* val2 * jacDet;
								}
							}
						}

						// integration for triangular geometry
						else if (q.getGeometry() == GaussQuadrature.triangle_) {

							// loop over Gauss points
							for (int l = 0; l < nog; l++) {

								// get weight factor and supporting points
								double alpha = q.getWeight(l);
								double supp1 = q.getSupport1(l);
								double supp2 = q.getSupport2(l);

								// get jacobian and compute determinant
								DMat jac = getJacobian(supp1, supp2);
								double jacDet = jac.determinant();

								// get values of functions
								double val1 = intF.getFunction(supp1, supp2, j);
								double val2 = intF.getFunction(supp1, supp2, k);

								// compute integration
								ms[j][k] += 0.5 * value * alpha * val1 * val2
										* jacDet;
							}
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
				if (coord == ElementSpring.local_)
					am = am.transform(tr, DMat.toGlobal_);

				// add to element additional mass matrix
				eMass = eMass.add(am);
			}
		}

		// return
		return eMass;
	}

	/**
	 * Returns the approximation of geometry.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * 
	 * @param coord
	 *            0 if approximation of local coordinates-1 is demanded, 1 if
	 *            approximation of local coordinates-2 is demanded.
	 * @param der
	 *            0 -> no derivative, 1 -> derivative with respect to natural
	 *            coordinate-1, 2 -> derivative with respect to natural
	 *            coordinate-2.
	 * @return The approximated geometry.
	 */
	protected double getGeoAppx(double eps1, double eps2, int coord, int der) {

		// check parameters
		if (coord < 0 || coord > 1)
			exceptionHandler("Illegal coordinate system for geometry approximation!");
		if (der < 0 || der > 2)
			exceptionHandler("Illegal derivative option for geometry approximation!");

		// get nodes of element
		Node[] nodes = getNodes();

		// get 3d transformation matrix
		DMat tr = getTransformation();

		// get interpolation
		Interpolation2D intF = getInterpolation();

		// compute approximation of demanded local coordinate
		double approx = 0.0;
		for (int i = 0; i < nodes.length; i++) {

			// get position vector of node and transform to local coordinates
			DVec pos = nodes[i].getPosition().transform(tr, DMat.toLocal_);

			// function demanded
			if (der == 0)
				approx += pos.get(coord) * intF.getFunction(eps1, eps2, i);

			// derivative with respect to natural coordinate-1 demanded
			else if (der == 1)
				approx += pos.get(coord) * intF.getDer1Function(eps1, eps2, i);

			// derivative with respect to natural coordinate-2 demanded
			else if (der == 2)
				approx += pos.get(coord) * intF.getDer2Function(eps1, eps2, i);
		}
		return approx;
	}

	/**
	 * Computes and returns matrix containing initial membrane stresses. This
	 * quantity is used for computing element stability matrix.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @return Initial membrane stresses.
	 */
	protected DMat computeInitialStress(double eps1, double eps2) {

		// get element stress tensor
		DMat stress = getStress(eps1, eps2, 0.0);

		// get membrane stresses
		stress = stress.getSubMatrix(0, 0, 1, 1);
		return stress.scale(-1.0);
	}

	/** Returns the interpolation of element. */
	protected abstract Interpolation2D getInterpolation();
}
