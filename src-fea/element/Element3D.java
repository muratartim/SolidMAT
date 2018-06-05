package element;

import java.util.Vector;

import boundary.ElementMechLoad;
import math.GaussQuadrature;
import math.Interpolation3D;
import matrix.DMat;
import matrix.DVec;
import node.Node;

/**
 * Class for three dimensional elements.
 * 
 * @author Murat
 * 
 */
public abstract class Element3D extends Element {

	private static final long serialVersionUID = 1L;

	/** Static variable for the geometry of three dimensional element. */
	public static final int hexahedral_ = 0, tetrahedral_ = 1;

	/**
	 * Returns jacobian matrix.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return The jacobian matrix.
	 */
	public DMat getJacobian(double eps1, double eps2, double eps3) {

		// get geometry approximations
		double val00 = getGeoAppx(eps1, eps2, eps3, 0, 1);
		double val01 = getGeoAppx(eps1, eps2, eps3, 1, 1);
		double val02 = getGeoAppx(eps1, eps2, eps3, 2, 1);
		double val10 = getGeoAppx(eps1, eps2, eps3, 0, 2);
		double val11 = getGeoAppx(eps1, eps2, eps3, 1, 2);
		double val12 = getGeoAppx(eps1, eps2, eps3, 2, 2);
		double val20 = getGeoAppx(eps1, eps2, eps3, 0, 3);
		double val21 = getGeoAppx(eps1, eps2, eps3, 1, 3);
		double val22 = getGeoAppx(eps1, eps2, eps3, 2, 3);

		// compute and return jacobian matrix
		DMat jac = new DMat(3, 3);
		jac.set(0, 0, val00);
		jac.set(0, 1, val01);
		jac.set(0, 2, val02);
		jac.set(1, 0, val10);
		jac.set(1, 1, val11);
		jac.set(1, 2, val12);
		jac.set(2, 0, val20);
		jac.set(2, 1, val21);
		jac.set(2, 2, val22);
		return jac;
	}

	/** Returns the geometry of element. */
	public abstract int getGeometry();

	/**
	 * Returns the dimension of element.
	 */
	public int getDimension() {
		return ElementLibrary.threeDimensional_;
	}

	/**
	 * Returns the volume of element. Note: This method includes hard-codes for
	 * obtaining the number of Gauss points.
	 * 
	 * @return The volume of element.
	 */
	public double getVolume() {

		// get element properties
		Interpolation3D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation3D.hexahedral_) {
			if (degree == Interpolation3D.trilinear_)
				nog = 2;
			else if (degree == Interpolation3D.triquadratic_)
				nog = 3;
		} else if (geo == Interpolation3D.tetrahedral_) {
			if (degree == Interpolation3D.trilinear_) {
				nog = 2;
			} else if (degree == Interpolation3D.triquadratic_) {
				nog = 3;
			}
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(geo);

		// compute integration for hexahedral geometry
		double volume = 0.0;
		if (q.getGeometry() == GaussQuadrature.cube_) {

			// loop over Gauss points in first direction
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

						// get third weight factor, support and value of
						// function
						double alpha3 = q.getWeight(k);
						double supp3 = q.getSupport3(k);
						double value = getJacobian(supp1, supp2, supp3)
								.determinant();

						// compute volume
						volume += alpha1 * alpha2 * alpha3 * value;
					}
				}
			}
			return volume;
		}

		// compute integration for tetrahedral geometry
		else {

			// loop over Gauss points in first direction
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

						// compute value of function
						double value = getJacobian(xm, ym, zm).determinant();

						// compute volume
						volume += cm * value;
					}
				}
			}
			return volume;
		}
	}

	/**
	 * Returns the three dimensional transformation matrix. It is a unity
	 * matrix.
	 * 
	 * @return The three dimensional transformation matrix for three dimensional
	 *         elements.
	 */
	public DMat getTransformation() {
		return new DMat(3);
	}

	/**
	 * Computes element mechanical load vector.
	 * 
	 * @return Element mechanical load vector.
	 */
	protected DVec computeMechLoadVector() {

		// get element properties
		int nn = getNodes().length;
		Interpolation3D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation3D.hexahedral_) {
			if (degree == Interpolation3D.trilinear_)
				nog = 2;
			else if (degree == Interpolation3D.triquadratic_)
				nog = 4;
		} else if (geo == Interpolation3D.tetrahedral_) {
			if (degree == Interpolation3D.trilinear_) {
				nog = 2;
			} else if (degree == Interpolation3D.triquadratic_) {
				nog = 4;
			}
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(geo);

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
				selfWeight = -getWeight() / getVolume();

			// create loading sub-vector
			double[] r = new double[nn];

			// loop over rows of sub-vector
			for (int j = 0; j < nn; j++) {

				// integration for cube geometry
				if (q.getGeometry() == GaussQuadrature.cube_) {

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

							// loop over Gauss points in third direction
							for (int m = 0; m < nog; m++) {

								// get third weight factor and support
								double alpha3 = q.getWeight(m);
								double supp3 = q.getSupport3(m);

								// get jacobian and compute determinant
								DMat jac = getJacobian(supp1, supp2, supp3);
								double jacDet = jac.determinant();

								// get value of loading function
								double val1 = 0.0;

								// self weight loading
								if (load.isSelfWeight()) {
									DVec values = new DVec(1);
									values.set(0, selfWeight);
									load.setLoadingValues(values);
								}
								val1 = load.getFunction(geo, supp1, supp2,
										supp3);

								// get value of interpolation function
								double val2 = intF.getFunction(supp1, supp2,
										supp3, j);

								// compute integration
								r[j] += alpha1 * alpha2 * alpha3 * val1 * val2
										* jacDet;
							}
						}
					}
				}

				// integration for tetrahedral geometry
				else if (q.getGeometry() == GaussQuadrature.tetrahedral_) {

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

							// loop over Gauss points in third direction
							for (int m = 0; m < nog; m++) {

								// get third weight factor and support
								double alpha3 = q.getWeight(m);
								double supp3 = q.getSupport3(m);

								// compute cm, xm, ym and zm
								double cm = alpha1 * alpha2 * alpha3 / 64.0;
								cm *= Math.pow(1.0 - supp1, 2.0)
										* (1.0 - supp2);
								double xm = (1.0 + supp1) / 2.0;
								double ym = (1.0 - supp1) * (1.0 + supp2) / 4.0;
								double zm = (1.0 - supp1) * (1.0 - supp2)
										* (1 + supp3) / 8.0;

								// get jacobian and compute determinant
								DMat jac = getJacobian(xm, ym, zm);
								double jacDet = jac.determinant();

								// get value of loading function
								double val1 = 0.0;

								// self weight loading
								if (load.isSelfWeight()) {
									DVec values = new DVec(1);
									values.set(0, selfWeight);
									load.setLoadingValues(values);
								}
								val1 = load.getFunction(geo, xm, ym, zm);

								// get value of interpolation function
								double val2 = intF.getFunction(xm, ym, zm, j);

								// compute integration
								r[j] += cm * val1 * val2 * jacDet;
							}
						}
					}
				}
			}

			// get component of mechanical load
			int comp = load.getComponent();

			// create vector of mechanical load
			DVec vec = new DVec(6 * nn);
			for (int j = 0; j < nn; j++)
				vec.set(6 * j + comp, r[j]);

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
		Interpolation3D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation3D.hexahedral_) {
			if (degree == Interpolation3D.trilinear_)
				nog = 3;
			else if (degree == Interpolation3D.triquadratic_)
				nog = 5;
		} else if (geo == Interpolation3D.tetrahedral_) {
			if (degree == Interpolation3D.trilinear_) {
				nog = 3;
			} else if (degree == Interpolation3D.triquadratic_) {
				nog = 5;
			}
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(geo);

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

						// integration for cube geometry
						if (q.getGeometry() == GaussQuadrature.cube_) {

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

									// loop over Gauss points in third direction
									for (int n = 0; n < nog; n++) {

										// get third weight factor and support
										double alpha3 = q.getWeight(n);
										double supp3 = q.getSupport3(n);

										// get jacobian and compute determinant
										DMat jac = getJacobian(supp1, supp2,
												supp3);
										double jacDet = jac.determinant();

										// get values of functions
										double val1 = intF.getFunction(supp1,
												supp2, supp3, j);
										double val2 = intF.getFunction(supp1,
												supp2, supp3, k);

										// compute integration
										ks[j][k] += stiff * alpha1 * alpha2
												* alpha3 * val1 * val2 * jacDet;
									}
								}
							}
						}

						// integration for tetrahedral geometry
						else if (q.getGeometry() == GaussQuadrature.tetrahedral_) {

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

									// loop over Gauss points in third direction
									for (int n = 0; n < nog; n++) {

										// get third weight factor and support
										double alpha3 = q.getWeight(n);
										double supp3 = q.getSupport3(n);

										// compute cm, xm, ym and zm
										double cm = alpha1 * alpha2 * alpha3
												/ 64.0;
										cm *= Math.pow(1.0 - supp1, 2.0)
												* (1.0 - supp2);
										double xm = (1.0 + supp1) / 2.0;
										double ym = (1.0 - supp1)
												* (1.0 + supp2) / 4.0;
										double zm = (1.0 - supp1)
												* (1.0 - supp2) * (1 + supp3)
												/ 8.0;

										// get jacobian and compute determinant
										DMat jac = getJacobian(xm, ym, zm);
										double jacDet = jac.determinant();

										// get values of functions
										double val1 = intF.getFunction(xm, ym,
												zm, j);
										double val2 = intF.getFunction(xm, ym,
												zm, k);

										// compute integration
										ks[j][k] += stiff * cm * val1 * val2
												* jacDet;
									}
								}
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
		Interpolation3D intF = getInterpolation();
		int degree = intF.getDegree();
		int geo = intF.getGeometry();

		// set number of Gauss points
		int nog = 0;
		if (geo == Interpolation3D.hexahedral_) {
			if (degree == Interpolation3D.trilinear_)
				nog = 3;
			else if (degree == Interpolation3D.triquadratic_)
				nog = 5;
		} else if (geo == Interpolation3D.tetrahedral_) {
			if (degree == Interpolation3D.trilinear_) {
				nog = 3;
			} else if (degree == Interpolation3D.triquadratic_) {
				nog = 5;
			}
		}

		// create Quadrature
		GaussQuadrature q = new GaussQuadrature(nog,
				GaussQuadrature.threeDimensional_);
		q.setGeometry(geo);

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

						// integration for cube geometry
						if (q.getGeometry() == GaussQuadrature.cube_) {

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

									// loop over Gauss points in third direction
									for (int n = 0; n < nog; n++) {

										// get third weight factor and support
										double alpha3 = q.getWeight(n);
										double supp3 = q.getSupport3(n);

										// get jacobian and compute determinant
										DMat jac = getJacobian(supp1, supp2,
												supp3);
										double jacDet = jac.determinant();

										// get values of functions
										double val1 = intF.getFunction(supp1,
												supp2, supp3, j);
										double val2 = intF.getFunction(supp1,
												supp2, supp3, k);

										// compute integration
										ms[j][k] += value * alpha1 * alpha2
												* alpha3 * val1 * val2 * jacDet;
									}
								}
							}
						}

						// integration for tetrahedral geometry
						else if (q.getGeometry() == GaussQuadrature.tetrahedral_) {

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

									// loop over Gauss points in third direction
									for (int n = 0; n < nog; n++) {

										// get third weight factor and support
										double alpha3 = q.getWeight(n);
										double supp3 = q.getSupport3(n);

										// compute cm, xm, ym and zm
										double cm = alpha1 * alpha2 * alpha3
												/ 64.0;
										cm *= Math.pow(1.0 - supp1, 2.0)
												* (1.0 - supp2);
										double xm = (1.0 + supp1) / 2.0;
										double ym = (1.0 - supp1)
												* (1.0 + supp2) / 4.0;
										double zm = (1.0 - supp1)
												* (1.0 - supp2) * (1 + supp3)
												/ 8.0;

										// get jacobian and compute determinant
										DMat jac = getJacobian(xm, ym, zm);
										double jacDet = jac.determinant();

										// get values of functions
										double val1 = intF.getFunction(xm, ym,
												zm, j);
										double val2 = intF.getFunction(xm, ym,
												zm, k);

										// compute integration
										ms[j][k] += value * cm * val1 * val2
												* jacDet;
									}
								}
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
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param coord
	 *            0 if approximation of local coordinates-1 is demanded, 1 if
	 *            approximation of local coordinates-2 is demanded, 2 if
	 *            approximation of local coordinates-3 is demanded.
	 * @param der
	 *            0 -> no derivative, 1 -> derivative with respect to natural
	 *            coordinate-1, 2 -> derivative with respect to natural
	 *            coordinate-2, 3 -> derivative with respect to natural
	 *            coordinate-3.
	 * @return The approximated geometry.
	 */
	protected double getGeoAppx(double eps1, double eps2, double eps3,
			int coord, int der) {

		// check parameters
		if (coord < 0 || coord > 2)
			exceptionHandler("Illegal coordinate system for geometry approximation!");
		if (der < 0 || der > 3)
			exceptionHandler("Illegal derivative option for geometry approximation!");

		// get nodes of element
		Node[] nodes = getNodes();

		// get interpolation
		Interpolation3D intF = getInterpolation();

		// compute approximation of demanded local coordinate
		double approx = 0.0;
		for (int i = 0; i < nodes.length; i++) {

			// get position vector of node
			DVec pos = nodes[i].getPosition();

			// function demanded
			if (der == 0)
				approx += pos.get(coord)
						* intF.getFunction(eps1, eps2, eps3, i);

			// derivative with respect to natural coordinate-1 demanded
			else if (der == 1)
				approx += pos.get(coord)
						* intF.getDer1Function(eps1, eps2, eps3, i);

			// derivative with respect to natural coordinate-2 demanded
			else if (der == 2)
				approx += pos.get(coord)
						* intF.getDer2Function(eps1, eps2, eps3, i);

			// derivative with respect to natural coordinate-3 demanded
			else if (der == 3)
				approx += pos.get(coord)
						* intF.getDer3Function(eps1, eps2, eps3, i);
		}
		return approx;
	}

	/**
	 * Computes and returns matrix containing initial stresses. This quantity is
	 * used for computing element stability matrix.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @return Initial stresses.
	 */
	protected DMat computeInitialStress(double eps1, double eps2, double eps3) {

		// get element stress tensor
		DMat stress = getStress(eps1, eps2, eps3);

		// form initial stress matrix
		DMat inStress = new DMat(9, 9);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					inStress.set(k + 3 * i, k + 3 * j, stress.get(i, j));

		// return
		return inStress.scale(-1.0);
	}

	/** Returns the interpolation of element. */
	protected abstract Interpolation3D getInterpolation();
}
