package math;

/**
 * Class for three dimensional interpolation functions.
 * 
 * @author Murat
 * 
 */
public class Interpolation3D {

	/** Static variable for the degree of interpolation function. */
	public final static int trilinear_ = 1, triquadratic_ = 2;

	/** Static variable for the geometry of interpolation function. */
	public final static int hexahedral_ = 0, tetrahedral_ = 1;

	/** The degree of interpolation function. */
	private int degree_;

	/** The degree of interpolation function. */
	private int geometry_;

	/**
	 * Creates three dimensional interpolation function.
	 * 
	 * @param degree
	 *            The degree of interpolation.
	 * @param geometry
	 *            The geometry of interpolation.
	 */
	public Interpolation3D(int degree, int geometry) {

		// set degree
		if (degree < 1 || degree > 2)
			exceptionHandler("Illegal assignment for the degree of interpolation!");
		else
			degree_ = degree;

		// set geometry
		if (geometry < 0 || geometry > 1)
			exceptionHandler("Illegal assignment for the geometry of interpolation!");
		else
			geometry_ = geometry;
	}

	/**
	 * Returns the degree of interpolation in all directions.
	 * 
	 * @return The degree of interpolation.
	 */
	public int getDegree() {
		return degree_;
	}

	/**
	 * Returns the geometry of interpolation.
	 * 
	 * @return The geometry of interpolation.
	 */
	public int getGeometry() {
		return geometry_;
	}

	/**
	 * Returns the value of interpolation function.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of interpolation function.
	 */
	public double getFunction(double eps1, double eps2, double eps3, int index) {

		// hexahedral geometry
		if (geometry_ == Interpolation3D.hexahedral_) {

			// trilinear (lagrange family)
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return 0.125 * (1 + eps1) * (1 + eps2) * (1 + eps3);
				else if (index == 1)
					return 0.125 * (1 - eps1) * (1 + eps2) * (1 + eps3);
				else if (index == 2)
					return 0.125 * (1 - eps1) * (1 - eps2) * (1 + eps3);
				else if (index == 3)
					return 0.125 * (1 + eps1) * (1 - eps2) * (1 + eps3);
				else if (index == 4)
					return 0.125 * (1 + eps1) * (1 + eps2) * (1 - eps3);
				else if (index == 5)
					return 0.125 * (1 - eps1) * (1 + eps2) * (1 - eps3);
				else if (index == 6)
					return 0.125 * (1 - eps1) * (1 - eps2) * (1 - eps3);
				else if (index == 7)
					return 0.125 * (1 + eps1) * (1 - eps2) * (1 - eps3);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic (serendipity family)
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return 0.125
							* (1 + eps1)
							* (1 + eps2)
							* (1 + eps3)
							- 0.5
							* (-0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									* (1. + eps3) - 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) - 0.25
									* (1. + eps1)
									* (1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 1)
					return -0.125
							* (-1. + eps1)
							* (1. + eps2)
							* (1. + eps3)
							- 0.5
							* (-0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									* (1. + eps3) + 0.25 * (-1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) + 0.25
									* (-1. + eps1)
									* (1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 2)
					return 0.125
							* (-1. + eps1)
							* (-1. + eps2)
							* (1. + eps3)
							- 0.5
							* (0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
									* (1. + eps3) + 0.25 * (-1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) - 0.25
									* (-1. + eps1)
									* (-1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 3)
					return -0.125
							* (1. + eps1)
							* (-1. + eps2)
							* (1. + eps3)
							- 0.5
							* (0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
									* (1. + eps3) - 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) + 0.25
									* (1. + eps1)
									* (-1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 4)
					return -0.125
							* (1. + eps1)
							* (1. + eps2)
							* (-1. + eps3)
							- 0.5
							* (0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									* (-1. + eps3) + 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (-1. + eps3) - 0.25
									* (1. + eps1)
									* (1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 5)
					return 0.125
							* (-1. + eps1)
							* (1. + eps2)
							* (-1. + eps3)
							- 0.5
							* (0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									* (-1. + eps3) - 0.25 * (-1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (-1. + eps3) + 0.25
									* (-1. + eps1)
									* (1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 6)
					return -0.125
							* (-1. + eps1)
							* (-1. + eps2)
							* (-1. + eps3)
							- 0.5
							* (-0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
									* (-1. + eps3) - 0.25 * (-1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (-1. + eps3) - 0.25
									* (-1. + eps1)
									* (-1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 7)
					return 0.125
							* (1. + eps1)
							* (-1. + eps2)
							* (-1. + eps3)
							- 0.5
							* (-0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
									* (-1. + eps3) + 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) * (-1. + eps3) + 0.25
									* (1. + eps1)
									* (-1. + eps2)
									* (-1. + Math.pow(eps3, 2)));
				else if (index == 8)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
							* (1. + eps3);
				else if (index == 9)
					return 0.25 * (-1. + eps1) * (-1. + Math.pow(eps2, 2))
							* (1. + eps3);
				else if (index == 10)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
							* (1. + eps3);
				else if (index == 11)
					return -0.25 * (1. + eps1) * (-1. + Math.pow(eps2, 2))
							* (1. + eps3);
				else if (index == 12)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
							* (-1. + eps3);
				else if (index == 13)
					return -0.25 * (-1. + eps1) * (-1. + Math.pow(eps2, 2))
							* (-1. + eps3);
				else if (index == 14)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2)
							* (-1. + eps3);
				else if (index == 15)
					return 0.25 * (1. + eps1) * (-1. + Math.pow(eps2, 2))
							* (-1. + eps3);
				else if (index == 16)
					return -0.25 * (1. + eps1) * (1. + eps2)
							* (-1. + Math.pow(eps3, 2));
				else if (index == 17)
					return 0.25 * (-1. + eps1) * (1. + eps2)
							* (-1. + Math.pow(eps3, 2));
				else if (index == 18)
					return -0.25 * (-1. + eps1) * (-1. + eps2)
							* (-1. + Math.pow(eps3, 2));
				else if (index == 19)
					return 0.25 * (1. + eps1) * (-1. + eps2)
							* (-1. + Math.pow(eps3, 2));
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// tetrahedral geometry
		else if (geometry_ == Interpolation3D.tetrahedral_) {

			// trilinear
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return 1 - eps1 - eps2 - eps3;
				else if (index == 1)
					return eps1;
				else if (index == 2)
					return eps2;
				else if (index == 3)
					return eps3;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return 1. + 2. * Math.pow(eps1, 2) + 2. * (-1.5 + eps2)
							* eps2 - 3. * eps3 + 4. * eps2 * eps3 + 2.
							* Math.pow(eps3, 2) + eps1
							* (-3. + 4. * eps2 + 4. * eps3);
				else if (index == 1)
					return -1. * eps1 + 2. * Math.pow(eps1, 2);
				else if (index == 2)
					return -1. * eps2 + 2. * Math.pow(eps2, 2);
				else if (index == 3)
					return -1. * eps3 + 2. * Math.pow(eps3, 2);
				else if (index == 4)
					return -4 * eps1 * (-1 + eps1 + eps2 + eps3);
				else if (index == 5)
					return 4 * eps1 * eps2;
				else if (index == 6)
					return -4 * eps2 * (-1 + eps1 + eps2 + eps3);
				else if (index == 7)
					return 4 * eps1 * eps3;
				else if (index == 8)
					return 4 * eps2 * eps3;
				else if (index == 9)
					return -4 * eps3 * (-1 + eps1 + eps2 + eps3);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}
		return 0.0;
	}

	/**
	 * Returns the value of derivative of interpolation function with respect to
	 * natural coordinate-1.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function with respect to
	 *         natural coordinate-1.
	 */
	public double getDer1Function(double eps1, double eps2, double eps3,
			int index) {

		// hexahedral geometry
		if (geometry_ == Interpolation3D.hexahedral_) {

			// trilinear (lagrange family)
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return 0.125 * (1 + eps2) * (1 + eps3);
				else if (index == 1)
					return -0.125 * (1 + eps2) * (1 + eps3);
				else if (index == 2)
					return 0.125 * (-1 + eps2) * (1 + eps3);
				else if (index == 3)
					return -0.125 * (-1 + eps2) * (1 + eps3);
				else if (index == 4)
					return -0.125 * (1 + eps2) * (-1 + eps3);
				else if (index == 5)
					return 0.125 * (1 + eps2) * (-1 + eps3);
				else if (index == 6)
					return -0.125 * (-1 + eps2) * (-1 + eps3);
				else if (index == 7)
					return 0.125 * (-1 + eps2) * (-1 + eps3);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic (serendipity family)
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return 0.125
							* (1 + eps2)
							* (1 + eps3)
							- 0.5
							* (-0.5 * eps1 * (1 + eps2) * (1 + eps3) - 0.25
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) - 0.25
									* (1. + eps2) * (-1. + Math.pow(eps3, 2)));
				else if (index == 1)
					return -0.125
							* (1 + eps2)
							* (1 + eps3)
							- 0.5
							* (-0.5 * eps1 * (1 + eps2) * (1 + eps3) + 0.25
									* (-1. + Math.pow(eps2, 2)) * (1. + eps3) + 0.25
									* (1. + eps2) * (-1. + Math.pow(eps3, 2)));
				else if (index == 2)
					return 0.125
							+ Math.pow(eps2, 2)
							* (-0.125 - 0.125 * eps3)
							+ eps1
							* (0.25 + eps2 * (-0.25 - 0.25 * eps3) + 0.25 * eps3)
							+ eps2 * (0.125 + 0.125 * eps3) * eps3 - 0.125
							* Math.pow(eps3, 2);
				else if (index == 3)
					return -0.125
							+ Math.pow(eps2, 2)
							* (0.125 + 0.125 * eps3)
							+ eps1
							* (0.25 + eps2 * (-0.25 - 0.25 * eps3) + 0.25 * eps3)
							+ eps2 * (-0.125 - 0.125 * eps3) * eps3 + 0.125
							* Math.pow(eps3, 2);
				else if (index == 4)
					return -0.125
							+ eps1
							* (0.25 + eps2 * (0.25 - 0.25 * eps3) - 0.25 * eps3)
							+ Math.pow(eps2, 2) * (0.125 - 0.125 * eps3) + eps2
							* (-0.125 + 0.125 * eps3) * eps3 + 0.125
							* Math.pow(eps3, 2);
				else if (index == 5)
					return 0.125
							+ eps1
							* (0.25 + eps2 * (0.25 - 0.25 * eps3) - 0.25 * eps3)
							+ Math.pow(eps2, 2) * (-0.125 + 0.125 * eps3)
							+ eps2 * (0.125 - 0.125 * eps3) * eps3 - 0.125
							* Math.pow(eps3, 2);
				else if (index == 6)
					return 0.125
							+ eps1
							* (0.25 + eps2 * (-0.25 + 0.25 * eps3) - 0.25 * eps3)
							+ Math.pow(eps2, 2) * (-0.125 + 0.125 * eps3)
							+ eps2 * (-0.125 + 0.125 * eps3) * eps3 - 0.125
							* Math.pow(eps3, 2);
				else if (index == 7)
					return -0.125
							+ eps1
							* (0.25 + eps2 * (-0.25 + 0.25 * eps3) - 0.25 * eps3)
							+ Math.pow(eps2, 2) * (0.125 - 0.125 * eps3) + eps2
							* (0.125 - 0.125 * eps3) * eps3 + 0.125
							* Math.pow(eps3, 2);
				else if (index == 8)
					return -0.5 * eps1 * (1 + eps2) * (1 + eps3);
				else if (index == 9)
					return 0.25 * (-1. + Math.pow(eps2, 2)) * (1. + eps3);
				else if (index == 10)
					return 0.5 * eps1 * (-1. + eps2) * (1. + eps3);
				else if (index == 11)
					return -0.25 * (-1. + Math.pow(eps2, 2)) * (1. + eps3);
				else if (index == 12)
					return 0.5 * eps1 * (1. + eps2) * (-1. + eps3);
				else if (index == 13)
					return -0.25 * (-1. + Math.pow(eps2, 2)) * (-1. + eps3);
				else if (index == 14)
					return -0.5 * eps1 * (-1. + eps2) * (-1. + eps3);
				else if (index == 15)
					return 0.25 * (-1. + Math.pow(eps2, 2)) * (-1. + eps3);
				else if (index == 16)
					return -0.25 * (1. + eps2) * (-1. + Math.pow(eps3, 2));
				else if (index == 17)
					return 0.25 * (1. + eps2) * (-1. + Math.pow(eps3, 2));
				else if (index == 18)
					return -0.25 * (-1. + eps2) * (-1. + Math.pow(eps3, 2));
				else if (index == 19)
					return 0.25 * (-1. + eps2) * (-1. + Math.pow(eps3, 2));
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// tetrahedral geometry
		else if (geometry_ == Interpolation3D.tetrahedral_) {

			// trilinear
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return -1.0;
				else if (index == 1)
					return 1.0;
				else if (index == 2)
					return 0.0;
				else if (index == 3)
					return 0.0;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return -3. + 4. * eps1 + 4. * eps2 + 4. * eps3;
				else if (index == 1)
					return -1. + 4. * eps1;
				else if (index == 2)
					return 0.0;
				else if (index == 3)
					return 0.0;
				else if (index == 4)
					return -4 * (-1 + 2 * eps1 + eps2 + eps3);
				else if (index == 5)
					return 4 * eps2;
				else if (index == 6)
					return -4 * eps2;
				else if (index == 7)
					return 4 * eps3;
				else if (index == 8)
					return 0.0;
				else if (index == 9)
					return -4 * eps3;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}
		return 0.0;
	}

	/**
	 * Returns the value of derivative of interpolation function with respect to
	 * natural coordinate-2.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function with respect to
	 *         natural coordinate-2.
	 */
	public double getDer2Function(double eps1, double eps2, double eps3,
			int index) {

		// hexahedral geometry
		if (geometry_ == Interpolation3D.hexahedral_) {

			// trilinear (lagrange family)
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return 0.125 * (1 + eps1) * (1 + eps3);
				else if (index == 1)
					return -0.125 * (-1 + eps1) * (1 + eps3);
				else if (index == 2)
					return 0.125 * (-1 + eps1) * (1 + eps3);
				else if (index == 3)
					return -0.125 * (1 + eps1) * (1 + eps3);
				else if (index == 4)
					return -0.125 * (1 + eps1) * (-1 + eps3);
				else if (index == 5)
					return 0.125 * (-1 + eps1) * (-1 + eps3);
				else if (index == 6)
					return -0.125 * (-1 + eps1) * (-1 + eps3);
				else if (index == 7)
					return 0.125 * (1 + eps1) * (-1 + eps3);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic (serendipity family)
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return 0.125
							* (1 + eps1)
							* (1 + eps3)
							- 0.5
							* (-0.5 * (1 + eps1) * eps2 * (1 + eps3) - 0.25
									* (-1. + Math.pow(eps1, 2)) * (1. + eps3) - 0.25
									* (1. + eps1) * (-1. + Math.pow(eps3, 2)));
				else if (index == 1)
					return -0.125
							+ Math.pow(eps1, 2)
							* (0.125 + 0.125 * eps3)
							+ eps2
							* (0.25 + 0.25 * eps3)
							+ 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (-0.25 - 0.25 * eps3) + (-0.125 - 0.125 * eps3)
									* eps3);
				else if (index == 2)
					return 0.125
							+ Math.pow(eps1, 2)
							* (-0.125 - 0.125 * eps3)
							+ eps2
							* (0.25 + 0.25 * eps3)
							- 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (-0.25 - 0.25 * eps3) + (0.125 + 0.125 * eps3)
									* eps3);
				else if (index == 3)
					return -0.125
							* (1 + eps1)
							* (1 + eps3)
							- 0.5
							* (-0.5 * (1 + eps1) * eps2 * (1 + eps3) + 0.25
									* (-1. + Math.pow(eps1, 2)) * (1. + eps3) + 0.25
									* (1. + eps1) * (-1. + Math.pow(eps3, 2)));
				else if (index == 4)
					return -0.125
							+ eps2
							* (0.25 - 0.25 * eps3)
							+ Math.pow(eps1, 2)
							* (0.125 - 0.125 * eps3)
							+ 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (0.25 - 0.25 * eps3) + (-0.125 + 0.125 * eps3)
									* eps3);
				else if (index == 5)
					return -0.125
							+ eps2
							* (0.25 - 0.25 * eps3)
							+ Math.pow(eps1, 2)
							* (0.125 - 0.125 * eps3)
							+ 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (-0.25 + 0.25 * eps3) + (0.125 - 0.125 * eps3)
									* eps3);
				else if (index == 6)
					return 0.125
							+ eps2
							* (0.25 - 0.25 * eps3)
							+ Math.pow(eps1, 2)
							* (-0.125 + 0.125 * eps3)
							- 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (-0.25 + 0.25 * eps3) + (-0.125 + 0.125 * eps3)
									* eps3);
				else if (index == 7)
					return 0.125
							+ eps2
							* (0.25 - 0.25 * eps3)
							+ Math.pow(eps1, 2)
							* (-0.125 + 0.125 * eps3)
							- 0.125
							* Math.pow(eps3, 2)
							+ eps1
							* (eps2 * (0.25 - 0.25 * eps3) + (0.125 - 0.125 * eps3)
									* eps3);
				else if (index == 8)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps3);
				else if (index == 9)
					return 0.5 * (-1. + eps1) * eps2 * (1. + eps3);
				else if (index == 10)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps3);
				else if (index == 11)
					return -0.5 * (1 + eps1) * eps2 * (1 + eps3);
				else if (index == 12)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps3);
				else if (index == 13)
					return -0.5 * (-1. + eps1) * eps2 * (-1. + eps3);
				else if (index == 14)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps3);
				else if (index == 15)
					return 0.5 * (1. + eps1) * eps2 * (-1. + eps3);
				else if (index == 16)
					return -0.25 * (1. + eps1) * (-1. + Math.pow(eps3, 2));
				else if (index == 17)
					return 0.25 * (-1. + eps1) * (-1. + Math.pow(eps3, 2));
				else if (index == 18)
					return -0.25 * (-1. + eps1) * (-1. + Math.pow(eps3, 2));
				else if (index == 19)
					return 0.25 * (1. + eps1) * (-1. + Math.pow(eps3, 2));
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// tetrahedral geometry
		else if (geometry_ == Interpolation3D.tetrahedral_) {

			// trilinear
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return -1.0;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return 1.0;
				else if (index == 3)
					return 0.0;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return -3. + 4. * eps1 + 4. * eps2 + 4. * eps3;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return -1. + 4. * eps2;
				else if (index == 3)
					return 0.0;
				else if (index == 4)
					return -4 * eps1;
				else if (index == 5)
					return 4 * eps1;
				else if (index == 6)
					return -4 * (-1 + eps1 + 2 * eps2 + eps3);
				else if (index == 7)
					return 0.0;
				else if (index == 8)
					return 4 * eps3;
				else if (index == 9)
					return -4 * eps3;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}
		return 0.0;
	}

	/**
	 * Returns the value of derivative of interpolation function with respect to
	 * natural coordinate-3.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param eps2
	 *            Natural coordinate-2.
	 * @param eps3
	 *            Natural coordinate-3.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function with respect to
	 *         natural coordinate-3.
	 */
	public double getDer3Function(double eps1, double eps2, double eps3,
			int index) {

		// hexahedral geometry
		if (geometry_ == Interpolation3D.hexahedral_) {

			// trilinear (lagrange family)
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return 0.125 * (1 + eps1) * (1 + eps2);
				else if (index == 1)
					return -0.125 * (-1 + eps1) * (1 + eps2);
				else if (index == 2)
					return 0.125 * (-1 + eps1) * (-1 + eps2);
				else if (index == 3)
					return -0.125 * (1 + eps1) * (-1 + eps2);
				else if (index == 4)
					return -0.125 * (1 + eps1) * (1 + eps2);
				else if (index == 5)
					return 0.125 * (-1 + eps1) * (1 + eps2);
				else if (index == 6)
					return -0.125 * (-1 + eps1) * (-1 + eps2);
				else if (index == 7)
					return 0.125 * (1 + eps1) * (-1 + eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic (serendipity family)
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return 0.125
							* (1 + eps1)
							* (1 + eps2)
							- 0.5
							* (-0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									- 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) - 0.5
									* (1 + eps1) * (1 + eps2) * eps3);
				else if (index == 1)
					return -0.125
							+ Math.pow(eps1, 2)
							* (0.125 + 0.125 * eps2)
							+ 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (-0.125 - 0.125 * eps2 - 0.25 * eps3) - 0.25 * eps3)
							+ 0.25 * eps3 + 0.25 * eps2 * eps3;
				else if (index == 2)
					return -0.125
							+ Math.pow(eps1, 2)
							* (0.125 - 0.125 * eps2)
							+ 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (0.125 - 0.125 * eps2 + 0.25 * eps3) - 0.25 * eps3)
							+ 0.25 * eps3 - 0.25 * eps2 * eps3;
				else if (index == 3)
					return -0.125
							+ Math.pow(eps1, 2)
							* (0.125 - 0.125 * eps2)
							+ 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (-0.125 + 0.125 * eps2 - 0.25 * eps3) + 0.25 * eps3)
							+ 0.25 * eps3 - 0.25 * eps2 * eps3;
				else if (index == 4)
					return -0.125
							* (1 + eps1)
							* (1 + eps2)
							- 0.5
							* (0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2)
									+ 0.25 * (1. + eps1)
									* (-1. + Math.pow(eps2, 2)) - 0.5
									* (1 + eps1) * (1 + eps2) * eps3);
				else if (index == 5)
					return 0.125
							+ Math.pow(eps1, 2)
							* (-0.125 - 0.125 * eps2)
							- 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (0.125 + 0.125 * eps2 - 0.25 * eps3) - 0.25 * eps3)
							+ 0.25 * eps3 + 0.25 * eps2 * eps3;
				else if (index == 6)
					return 0.125
							+ Math.pow(eps1, 2)
							* (-0.125 + 0.125 * eps2)
							- 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (-0.125 + 0.125 * eps2 + 0.25 * eps3) - 0.25 * eps3)
							+ 0.25 * eps3 - 0.25 * eps2 * eps3;
				else if (index == 7)
					return 0.125
							+ Math.pow(eps1, 2)
							* (-0.125 + 0.125 * eps2)
							- 0.125
							* Math.pow(eps2, 2)
							+ eps1
							* (eps2 * (0.125 - 0.125 * eps2 - 0.25 * eps3) + 0.25 * eps3)
							+ 0.25 * eps3 - 0.25 * eps2 * eps3;
				else if (index == 8)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2);
				else if (index == 9)
					return 0.25 * (-1. + eps1) * (-1. + Math.pow(eps2, 2));
				else if (index == 10)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2);
				else if (index == 11)
					return -0.25 * (1. + eps1) * (-1. + Math.pow(eps2, 2));
				else if (index == 12)
					return 0.25 * (-1. + Math.pow(eps1, 2)) * (1. + eps2);
				else if (index == 13)
					return -0.25 * (-1. + eps1) * (-1. + Math.pow(eps2, 2));
				else if (index == 14)
					return -0.25 * (-1. + Math.pow(eps1, 2)) * (-1. + eps2);
				else if (index == 15)
					return 0.25 * (1. + eps1) * (-1. + Math.pow(eps2, 2));
				else if (index == 16)
					return -0.5 * (1 + eps1) * (1 + eps2) * eps3;
				else if (index == 17)
					return 0.5 * (-1. + eps1) * (1. + eps2) * eps3;
				else if (index == 18)
					return -0.5 * (-1. + eps1) * (-1. + eps2) * eps3;
				else if (index == 19)
					return 0.5 * (1. + eps1) * (-1. + eps2) * eps3;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// tetrahedral geometry
		else if (geometry_ == Interpolation3D.tetrahedral_) {

			// trilinear
			if (degree_ == Interpolation3D.trilinear_) {
				if (index == 0)
					return -1.0;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return 0.0;
				else if (index == 3)
					return 1.0;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// triquadratic
			else if (degree_ == Interpolation3D.triquadratic_) {
				if (index == 0)
					return -3. + 4. * eps1 + 4. * eps2 + 4. * eps3;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return 0.0;
				else if (index == 3)
					return -1. + 4. * eps3;
				else if (index == 4)
					return -4 * eps1;
				else if (index == 5)
					return 0.0;
				else if (index == 6)
					return -4 * eps2;
				else if (index == 7)
					return 4 * eps1;
				else if (index == 8)
					return 4 * eps2;
				else if (index == 9)
					return -4 * (-1 + eps1 + eps2 + 2 * eps3);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}
		return 0.0;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
