package math;

/**
 * Class for Gauss Quadrature that can be used in numerical integration.
 * 
 * @author Murat
 * 
 */
public class GaussQuadrature {

	/** Static variable for the geometry 2D of quadrature. */
	public static int square_ = 0, triangle_ = 1;

	/** Static variable for the geometry 3D of quadrature. */
	public static int cube_ = 0, tetrahedral_ = 1;

	/** Static variable for the dimension of integration. */
	public static int oneDimensional_ = 0, twoDimensional_ = 1,
			threeDimensional_ = 2;

	/** Number of Gauss points. */
	private int nOGP_;

	/** The dimension of integration. */
	private int dimension_;

	/** The type of geometry. */
	private int geometry_ = GaussQuadrature.square_;

	/**
	 * Creates Gauss Quadrature object.
	 * 
	 * @param nOGP
	 *            Number of Gauss points (in one direction).
	 * @param dimension
	 *            The dimension of quadrature.
	 */
	public GaussQuadrature(int nOGP, int dimension) {

		// set number of Gauss points
		if (nOGP < 1)
			exceptionHandler("Illegal assignment for number of Gauss points!");
		else
			nOGP_ = nOGP;

		// set dimension of quadrature
		if (dimension < 0 || dimension > 2)
			exceptionHandler("Illegal assignment for the dimension of quadrature!");
		else
			dimension_ = dimension;
	}

	/**
	 * Sets the geometry of quadrature (square/cube or triangle/tetrahedral).
	 * 
	 * @param geometry
	 *            The geometry of quadrature.
	 */
	public void setGeometry(int geometry) {
		if (geometry < 0 || geometry > 1)
			exceptionHandler("Illegal assignment for the geometry of quadrature!");
		else
			geometry_ = geometry;
	}

	/**
	 * Returns the geometry of quadrature.
	 * 
	 * @return The geometry of quadrature.
	 */
	public int getGeometry() {
		return geometry_;
	}

	/**
	 * Returns the weighting factor of quadrature.
	 * 
	 * @param index
	 *            The index of weighting factor.
	 * @return The weighting factor (alpha).
	 */
	public double getWeight(int index) {

		// check index
		if (index >= nOGP_)
			exceptionHandler("Illegal index for weighting factor!");

		// set error message
		String message = "Not enough Gauss points for integration!";

		// setup weighting array
		double[] w = new double[nOGP_];

		// one dimensional
		if (dimension_ == GaussQuadrature.oneDimensional_) {

			// one point
			if (nOGP_ == 1)
				w[0] = 2.0;

			// two points
			else if (nOGP_ == 2) {
				w[0] = 1.0;
				w[1] = 1.0;
			}

			// three points
			else if (nOGP_ == 3) {
				w[0] = 5.0 / 9.0;
				w[1] = 8.0 / 9.0;
				w[2] = 5.0 / 9.0;
			}

			// four points
			else if (nOGP_ == 4) {
				w[0] = 0.34785;
				w[1] = 0.65214;
				w[2] = 0.65214;
				w[3] = 0.34785;
			}

			// not enough Gauss points
			else
				exceptionHandler(message);
		}

		// two dimensional
		else if (dimension_ == GaussQuadrature.twoDimensional_) {

			// square geometry
			if (geometry_ == GaussQuadrature.square_) {

				// one point
				if (nOGP_ == 1)
					w[0] = 2.0;

				// two point
				else if (nOGP_ == 2) {
					w[0] = 1.0;
					w[1] = 1.0;
				}

				// three point
				else if (nOGP_ == 3) {
					w[0] = 5.0 / 9.0;
					w[1] = 8.0 / 9.0;
					w[2] = 5.0 / 9.0;
				}

				// four points
				else if (nOGP_ == 4) {
					w[0] = 0.34785;
					w[1] = 0.65214;
					w[2] = 0.65214;
					w[3] = 0.34785;
				}

				// five points
				else if (nOGP_ == 5) {
					w[0] = 0.23692;
					w[1] = 0.47862;
					w[2] = 0.56888;
					w[3] = 0.47862;
					w[4] = 0.23692;
				}

				// six points
				else if (nOGP_ == 6) {
					w[0] = 0.17132;
					w[1] = 0.36076;
					w[2] = 0.46791;
					w[3] = 0.46791;
					w[4] = 0.36076;
					w[5] = 0.17132;
				}

				// seven points
				else if (nOGP_ == 7) {
					w[0] = 0.12948;
					w[1] = 0.27970;
					w[2] = 0.38183;
					w[3] = 0.41795;
					w[4] = 0.38183;
					w[5] = 0.27970;
					w[6] = 0.12948;
				}

				// eight points
				else if (nOGP_ == 8) {
					w[0] = 0.10122;
					w[1] = 0.22238;
					w[2] = 0.31370;
					w[3] = 0.36268;
					w[4] = 0.36268;
					w[5] = 0.31370;
					w[6] = 0.22238;
					w[7] = 0.10122;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// triangle geometry
			else if (geometry_ == GaussQuadrature.triangle_) {

				// one point
				if (nOGP_ == 1)
					w[0] = 1.0;

				// three point
				else if (nOGP_ == 3) {
					w[0] = 1.0 / 3.0;
					w[1] = 1.0 / 3.0;
					w[2] = 1.0 / 3.0;
				}

				// four point
				else if (nOGP_ == 4) {
					w[0] = -27.0 / 48.0;
					w[1] = 25.0 / 48.0;
					w[2] = 25.0 / 48.0;
					w[3] = 25.0 / 48.0;
				}

				// seven point
				else if (nOGP_ == 7) {
					w[0] = 0.22500;
					w[1] = 0.12593;
					w[2] = 0.12593;
					w[3] = 0.12593;
					w[4] = 0.13239;
					w[5] = 0.13239;
					w[6] = 0.13239;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}

		// three dimensional
		else if (dimension_ == GaussQuadrature.threeDimensional_) {

			// cube geometry
			if (geometry_ == GaussQuadrature.cube_) {

				// one point
				if (nOGP_ == 1)
					w[0] = 2.0;

				// two point
				else if (nOGP_ == 2) {
					w[0] = 1.0;
					w[1] = 1.0;
				}

				// three point
				else if (nOGP_ == 3) {
					w[0] = 5.0 / 9.0;
					w[1] = 8.0 / 9.0;
					w[2] = 5.0 / 9.0;
				}

				// four points
				else if (nOGP_ == 4) {
					w[0] = 0.34785;
					w[1] = 0.65214;
					w[2] = 0.65214;
					w[3] = 0.34785;
				}

				// five points
				else if (nOGP_ == 5) {
					w[0] = 0.23692;
					w[1] = 0.47862;
					w[2] = 0.56888;
					w[3] = 0.47862;
					w[4] = 0.23692;
				}

				// six points
				else if (nOGP_ == 6) {
					w[0] = 0.17132;
					w[1] = 0.36076;
					w[2] = 0.46791;
					w[3] = 0.46791;
					w[4] = 0.36076;
					w[5] = 0.17132;
				}

				// seven points
				else if (nOGP_ == 7) {
					w[0] = 0.12948;
					w[1] = 0.27970;
					w[2] = 0.38183;
					w[3] = 0.41795;
					w[4] = 0.38183;
					w[5] = 0.27970;
					w[6] = 0.12948;
				}

				// eight points
				else if (nOGP_ == 8) {
					w[0] = 0.10122;
					w[1] = 0.22238;
					w[2] = 0.31370;
					w[3] = 0.36268;
					w[4] = 0.36268;
					w[5] = 0.31370;
					w[6] = 0.22238;
					w[7] = 0.10122;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// tetrahedral geometry
			else if (geometry_ == GaussQuadrature.tetrahedral_) {

				// one point
				if (nOGP_ == 1)
					w[0] = 2.0;

				// two point
				else if (nOGP_ == 2) {
					w[0] = 1.0;
					w[1] = 1.0;
				}

				// three point
				else if (nOGP_ == 3) {
					w[0] = 5.0 / 9.0;
					w[1] = 8.0 / 9.0;
					w[2] = 5.0 / 9.0;
				}

				// four points
				else if (nOGP_ == 4) {
					w[0] = 0.34785;
					w[1] = 0.65214;
					w[2] = 0.65214;
					w[3] = 0.34785;
				}

				// five points
				else if (nOGP_ == 5) {
					w[0] = 0.23692;
					w[1] = 0.47862;
					w[2] = 0.56888;
					w[3] = 0.47862;
					w[4] = 0.23692;
				}

				// six points
				else if (nOGP_ == 6) {
					w[0] = 0.17132;
					w[1] = 0.36076;
					w[2] = 0.46791;
					w[3] = 0.46791;
					w[4] = 0.36076;
					w[5] = 0.17132;
				}

				// seven points
				else if (nOGP_ == 7) {
					w[0] = 0.12948;
					w[1] = 0.27970;
					w[2] = 0.38183;
					w[3] = 0.41795;
					w[4] = 0.38183;
					w[5] = 0.27970;
					w[6] = 0.12948;
				}

				// eight points
				else if (nOGP_ == 8) {
					w[0] = 0.10122;
					w[1] = 0.22238;
					w[2] = 0.31370;
					w[3] = 0.36268;
					w[4] = 0.36268;
					w[5] = 0.31370;
					w[6] = 0.22238;
					w[7] = 0.10122;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}
		return w[index];
	}

	/**
	 * Returns the supporting point in the direction of natural coordinate-1.
	 * 
	 * @param index
	 *            The index of supporting point.
	 * @return The supporting point in the direction of natural coordinate-1.
	 */
	public double getSupport1(int index) {

		// check index
		if (index >= nOGP_)
			exceptionHandler("Illegal index for supporting point!");

		// set error message
		String message = "Not enough Gauss points for integration!";

		// setup supporting point array
		double[] eps1 = new double[nOGP_];

		// one dimensional
		if (dimension_ == GaussQuadrature.oneDimensional_) {

			// one point
			if (nOGP_ == 1)
				eps1[0] = 0.0;

			// two point
			else if (nOGP_ == 2) {
				eps1[0] = -1.0 / Math.sqrt(3.0);
				eps1[1] = 1.0 / Math.sqrt(3.0);
			}

			// three point
			else if (nOGP_ == 3) {
				eps1[0] = -Math.sqrt(3.0 / 5.0);
				eps1[1] = 0.0;
				eps1[2] = Math.sqrt(3.0 / 5.0);
			}

			// four points
			else if (nOGP_ == 4) {
				eps1[0] = -0.86114;
				eps1[1] = -0.33998;
				eps1[2] = 0.33998;
				eps1[3] = 0.86114;
			}

			// not enough Gauss points
			else
				exceptionHandler(message);
		}

		// two dimensional
		else if (dimension_ == GaussQuadrature.twoDimensional_) {

			// square geometry
			if (geometry_ == GaussQuadrature.square_) {

				// one point
				if (nOGP_ == 1)
					eps1[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps1[0] = -1.0 / Math.sqrt(3.0);
					eps1[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps1[0] = -Math.sqrt(3.0 / 5.0);
					eps1[1] = 0.0;
					eps1[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps1[0] = -0.86114;
					eps1[1] = -0.33998;
					eps1[2] = 0.33998;
					eps1[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps1[0] = -0.90617;
					eps1[1] = -0.53846;
					eps1[2] = 0.00000;
					eps1[3] = 0.53846;
					eps1[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps1[0] = -0.93246;
					eps1[1] = -0.66120;
					eps1[2] = -0.23861;
					eps1[3] = 0.23861;
					eps1[4] = 0.66120;
					eps1[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps1[0] = -0.94910;
					eps1[1] = -0.74153;
					eps1[2] = -0.40584;
					eps1[3] = 0.00000;
					eps1[4] = 0.40584;
					eps1[5] = 0.74153;
					eps1[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps1[0] = -0.96028;
					eps1[1] = -0.79666;
					eps1[2] = -0.52553;
					eps1[3] = -0.18343;
					eps1[4] = 0.18343;
					eps1[5] = 0.52553;
					eps1[6] = 0.79666;
					eps1[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// triangle geometry
			else if (geometry_ == GaussQuadrature.triangle_) {

				// one point
				if (nOGP_ == 1)
					eps1[0] = 1.0 / 3.0;

				// three point
				else if (nOGP_ == 3) {
					eps1[0] = 1.0 / 6.0;
					eps1[1] = 2.0 / 3.0;
					eps1[2] = 1.0 / 6.0;
				}

				// four point
				else if (nOGP_ == 4) {
					eps1[0] = 1.0 / 3.0;
					eps1[1] = 3.0 / 5.0;
					eps1[2] = 1.0 / 5.0;
					eps1[3] = 1.0 / 5.0;
				}

				// seven point
				else if (nOGP_ == 7) {
					eps1[0] = 0.33333;
					eps1[1] = 0.10128;
					eps1[2] = 0.79742;
					eps1[3] = 0.33333;
					eps1[4] = 0.47014;
					eps1[5] = 0.33333;
					eps1[6] = 0.05971;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}

		// three dimensional
		else if (dimension_ == GaussQuadrature.threeDimensional_) {

			// cube geometry
			if (geometry_ == GaussQuadrature.cube_) {

				// one point
				if (nOGP_ == 1)
					eps1[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps1[0] = -1.0 / Math.sqrt(3.0);
					eps1[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps1[0] = -Math.sqrt(3.0 / 5.0);
					eps1[1] = 0.0;
					eps1[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps1[0] = -0.86114;
					eps1[1] = -0.33998;
					eps1[2] = 0.33998;
					eps1[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps1[0] = -0.90617;
					eps1[1] = -0.53846;
					eps1[2] = 0.00000;
					eps1[3] = 0.53846;
					eps1[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps1[0] = -0.93246;
					eps1[1] = -0.66120;
					eps1[2] = -0.23861;
					eps1[3] = 0.23861;
					eps1[4] = 0.66120;
					eps1[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps1[0] = -0.94910;
					eps1[1] = -0.74153;
					eps1[2] = -0.40584;
					eps1[3] = 0.00000;
					eps1[4] = 0.40584;
					eps1[5] = 0.74153;
					eps1[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps1[0] = -0.96028;
					eps1[1] = -0.79666;
					eps1[2] = -0.52553;
					eps1[3] = -0.18343;
					eps1[4] = 0.18343;
					eps1[5] = 0.52553;
					eps1[6] = 0.79666;
					eps1[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// tetrahedral geometry
			else if (geometry_ == GaussQuadrature.tetrahedral_) {

				// one point
				if (nOGP_ == 1)
					eps1[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps1[0] = -1.0 / Math.sqrt(3.0);
					eps1[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps1[0] = -Math.sqrt(3.0 / 5.0);
					eps1[1] = 0.0;
					eps1[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps1[0] = -0.86114;
					eps1[1] = -0.33998;
					eps1[2] = 0.33998;
					eps1[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps1[0] = -0.90617;
					eps1[1] = -0.53846;
					eps1[2] = 0.00000;
					eps1[3] = 0.53846;
					eps1[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps1[0] = -0.93246;
					eps1[1] = -0.66120;
					eps1[2] = -0.23861;
					eps1[3] = 0.23861;
					eps1[4] = 0.66120;
					eps1[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps1[0] = -0.94910;
					eps1[1] = -0.74153;
					eps1[2] = -0.40584;
					eps1[3] = 0.00000;
					eps1[4] = 0.40584;
					eps1[5] = 0.74153;
					eps1[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps1[0] = -0.96028;
					eps1[1] = -0.79666;
					eps1[2] = -0.52553;
					eps1[3] = -0.18343;
					eps1[4] = 0.18343;
					eps1[5] = 0.52553;
					eps1[6] = 0.79666;
					eps1[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}
		return eps1[index];
	}

	/**
	 * Returns the supporting point in the direction of natural coordinate-2.
	 * 
	 * @param index
	 *            The index of supporting point.
	 * @return The supporting point in the direction of natural coordinate-2.
	 */
	public double getSupport2(int index) {

		// check index
		if (index >= nOGP_)
			exceptionHandler("Illegal index for supporting point!");

		// set error message
		String message = "Not enough Gauss points for integration!";

		// setup supporting point array
		double[] eps2 = new double[nOGP_];

		// two dimensional
		if (dimension_ == GaussQuadrature.twoDimensional_) {

			// square geometry
			if (geometry_ == GaussQuadrature.square_) {

				// one point
				if (nOGP_ == 1)
					eps2[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps2[0] = -1.0 / Math.sqrt(3.0);
					eps2[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps2[0] = -Math.sqrt(3.0 / 5.0);
					eps2[1] = 0.0;
					eps2[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps2[0] = -0.86114;
					eps2[1] = -0.33998;
					eps2[2] = 0.33998;
					eps2[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps2[0] = -0.90617;
					eps2[1] = -0.53846;
					eps2[2] = 0.00000;
					eps2[3] = 0.53846;
					eps2[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps2[0] = -0.93246;
					eps2[1] = -0.66120;
					eps2[2] = -0.23861;
					eps2[3] = 0.23861;
					eps2[4] = 0.66120;
					eps2[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps2[0] = -0.94910;
					eps2[1] = -0.74153;
					eps2[2] = -0.40584;
					eps2[3] = 0.00000;
					eps2[4] = 0.40584;
					eps2[5] = 0.74153;
					eps2[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps2[0] = -0.96028;
					eps2[1] = -0.79666;
					eps2[2] = -0.52553;
					eps2[3] = -0.18343;
					eps2[4] = 0.18343;
					eps2[5] = 0.52553;
					eps2[6] = 0.79666;
					eps2[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// triangle geometry
			else if (geometry_ == GaussQuadrature.triangle_) {

				// one point
				if (nOGP_ == 1)
					eps2[0] = 1.0 / 3.0;

				// three point
				else if (nOGP_ == 3) {
					eps2[0] = 1.0 / 6.0;
					eps2[1] = 1.0 / 6.0;
					eps2[2] = 2.0 / 3.0;
				}

				// four point
				else if (nOGP_ == 4) {
					eps2[0] = 1.0 / 3.0;
					eps2[1] = 1.0 / 5.0;
					eps2[2] = 3.0 / 5.0;
					eps2[3] = 1.0 / 5.0;
				}

				// seven point
				else if (nOGP_ == 7) {
					eps2[0] = 0.33333;
					eps2[1] = 0.10128;
					eps2[2] = 0.10128;
					eps2[3] = 0.79742;
					eps2[4] = 0.05971;
					eps2[5] = 0.47014;
					eps2[6] = 0.47014;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}

		// three dimensional
		else if (dimension_ == GaussQuadrature.threeDimensional_) {

			// cube geometry
			if (geometry_ == GaussQuadrature.cube_) {

				// one point
				if (nOGP_ == 1)
					eps2[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps2[0] = -1.0 / Math.sqrt(3.0);
					eps2[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps2[0] = -Math.sqrt(3.0 / 5.0);
					eps2[1] = 0.0;
					eps2[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps2[0] = -0.86114;
					eps2[1] = -0.33998;
					eps2[2] = 0.33998;
					eps2[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps2[0] = -0.90617;
					eps2[1] = -0.53846;
					eps2[2] = 0.00000;
					eps2[3] = 0.53846;
					eps2[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps2[0] = -0.93246;
					eps2[1] = -0.66120;
					eps2[2] = -0.23861;
					eps2[3] = 0.23861;
					eps2[4] = 0.66120;
					eps2[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps2[0] = -0.94910;
					eps2[1] = -0.74153;
					eps2[2] = -0.40584;
					eps2[3] = 0.00000;
					eps2[4] = 0.40584;
					eps2[5] = 0.74153;
					eps2[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps2[0] = -0.96028;
					eps2[1] = -0.79666;
					eps2[2] = -0.52553;
					eps2[3] = -0.18343;
					eps2[4] = 0.18343;
					eps2[5] = 0.52553;
					eps2[6] = 0.79666;
					eps2[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// tetrahedral geometry
			else if (geometry_ == GaussQuadrature.tetrahedral_) {

				// one point
				if (nOGP_ == 1)
					eps2[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps2[0] = -1.0 / Math.sqrt(3.0);
					eps2[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps2[0] = -Math.sqrt(3.0 / 5.0);
					eps2[1] = 0.0;
					eps2[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps2[0] = -0.86114;
					eps2[1] = -0.33998;
					eps2[2] = 0.33998;
					eps2[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps2[0] = -0.90617;
					eps2[1] = -0.53846;
					eps2[2] = 0.00000;
					eps2[3] = 0.53846;
					eps2[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps2[0] = -0.93246;
					eps2[1] = -0.66120;
					eps2[2] = -0.23861;
					eps2[3] = 0.23861;
					eps2[4] = 0.66120;
					eps2[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps2[0] = -0.94910;
					eps2[1] = -0.74153;
					eps2[2] = -0.40584;
					eps2[3] = 0.00000;
					eps2[4] = 0.40584;
					eps2[5] = 0.74153;
					eps2[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps2[0] = -0.96028;
					eps2[1] = -0.79666;
					eps2[2] = -0.52553;
					eps2[3] = -0.18343;
					eps2[4] = 0.18343;
					eps2[5] = 0.52553;
					eps2[6] = 0.79666;
					eps2[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}
		return eps2[index];
	}

	/**
	 * Returns the supporting point in the direction of natural coordinate-3.
	 * 
	 * @param index
	 *            The index of supporting point.
	 * @return The supporting point in the direction of natural coordinate-3.
	 */
	public double getSupport3(int index) {

		// check index
		if (index >= nOGP_)
			exceptionHandler("Illegal index for supporting point!");

		// set error message
		String message = "Not enough Gauss points for integration!";

		// setup supporting point array
		double[] eps3 = new double[nOGP_];

		// three dimensional
		if (dimension_ == GaussQuadrature.threeDimensional_) {

			// cube geometry
			if (geometry_ == GaussQuadrature.cube_) {

				// one point
				if (nOGP_ == 1)
					eps3[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps3[0] = -1.0 / Math.sqrt(3.0);
					eps3[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps3[0] = -Math.sqrt(3.0 / 5.0);
					eps3[1] = 0.0;
					eps3[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps3[0] = -0.86114;
					eps3[1] = -0.33998;
					eps3[2] = 0.33998;
					eps3[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps3[0] = -0.90617;
					eps3[1] = -0.53846;
					eps3[2] = 0.00000;
					eps3[3] = 0.53846;
					eps3[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps3[0] = -0.93246;
					eps3[1] = -0.66120;
					eps3[2] = -0.23861;
					eps3[3] = 0.23861;
					eps3[4] = 0.66120;
					eps3[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps3[0] = -0.94910;
					eps3[1] = -0.74153;
					eps3[2] = -0.40584;
					eps3[3] = 0.00000;
					eps3[4] = 0.40584;
					eps3[5] = 0.74153;
					eps3[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps3[0] = -0.96028;
					eps3[1] = -0.79666;
					eps3[2] = -0.52553;
					eps3[3] = -0.18343;
					eps3[4] = 0.18343;
					eps3[5] = 0.52553;
					eps3[6] = 0.79666;
					eps3[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}

			// tetrahedral geometry
			else if (geometry_ == GaussQuadrature.tetrahedral_) {

				// one point
				if (nOGP_ == 1)
					eps3[0] = 0.0;

				// two point
				else if (nOGP_ == 2) {
					eps3[0] = -1.0 / Math.sqrt(3.0);
					eps3[1] = 1.0 / Math.sqrt(3.0);
				}

				// three point
				else if (nOGP_ == 3) {
					eps3[0] = -Math.sqrt(3.0 / 5.0);
					eps3[1] = 0.0;
					eps3[2] = Math.sqrt(3.0 / 5.0);
				}

				// four points
				else if (nOGP_ == 4) {
					eps3[0] = -0.86114;
					eps3[1] = -0.33998;
					eps3[2] = 0.33998;
					eps3[3] = 0.86114;
				}

				// five points
				else if (nOGP_ == 5) {
					eps3[0] = -0.90617;
					eps3[1] = -0.53846;
					eps3[2] = 0.00000;
					eps3[3] = 0.53846;
					eps3[4] = 0.90617;
				}

				// six points
				else if (nOGP_ == 6) {
					eps3[0] = -0.93246;
					eps3[1] = -0.66120;
					eps3[2] = -0.23861;
					eps3[3] = 0.23861;
					eps3[4] = 0.66120;
					eps3[5] = 0.93246;
				}

				// seven points
				else if (nOGP_ == 7) {
					eps3[0] = -0.94910;
					eps3[1] = -0.74153;
					eps3[2] = -0.40584;
					eps3[3] = 0.00000;
					eps3[4] = 0.40584;
					eps3[5] = 0.74153;
					eps3[6] = 0.94910;
				}

				// eight points
				else if (nOGP_ == 8) {
					eps3[0] = -0.96028;
					eps3[1] = -0.79666;
					eps3[2] = -0.52553;
					eps3[3] = -0.18343;
					eps3[4] = 0.18343;
					eps3[5] = 0.52553;
					eps3[6] = 0.79666;
					eps3[7] = 0.96028;
				}

				// not enough Gauss points
				else
					exceptionHandler(message);
			}
		}
		return eps3[index];
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
