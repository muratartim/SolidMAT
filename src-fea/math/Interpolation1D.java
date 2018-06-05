package math;

/**
 * Class for one dimensional interpolation functions.
 * 
 * @author Murat
 * 
 */
public class Interpolation1D {

	/** Static variable for the family of interpolation function. */
	public final static int lagrange_ = 0;

	/** Static variable for the degree of interpolation function. */
	public final static int linear_ = 1, quadratic_ = 2, cubic_ = 3;

	/** The family of interpolation function. */
	private int family_;

	/** The degree of interpolation function. */
	private int degree_;

	/**
	 * Cretaes one dimensional interpolation function.
	 * 
	 * @param family
	 *            The family of interpolation.
	 * @param degree
	 *            The degree of interpolation.
	 */
	public Interpolation1D(int family, int degree) {

		// set family
		if (family < 0 || family > 0)
			exceptionHandler("Illegal assignment for the family of interpolation!");
		else
			family_ = family;

		// set degree
		if (degree < 1 || degree > 3)
			exceptionHandler("Illegal assignment for the degree of interpolation!");
		else
			degree_ = degree;
	}

	/**
	 * Returns the degree of interpolation.
	 * 
	 * @return The degree of interpolation.
	 */
	public int getDegree() {
		return degree_;
	}

	/**
	 * Returns the family of interpolation.
	 * 
	 * @return The family of interpolation.
	 */
	public int getFamily() {
		return family_;
	}

	/**
	 * Returns the value of interpolation function.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of interpolation function.
	 */
	public double getFunction(double eps1, int index) {

		// lagrange family
		if (family_ == Interpolation1D.lagrange_) {

			// linear
			if (degree_ == Interpolation1D.linear_) {
				if (index == 0)
					return 0.5 * (1 - eps1);
				else if (index == 1)
					return 0.5 * (1 + eps1);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// quadratic
			else if (degree_ == Interpolation1D.quadratic_) {
				if (index == 0)
					return 0.5 * (eps1 - 1) * eps1;
				else if (index == 1)
					return 1 - eps1 * eps1;
				else if (index == 2)
					return 0.5 * (eps1 + 1) * eps1;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// cubic
			else if (degree_ == Interpolation1D.cubic_) {
				if (index == 0)
					return 9.0 / 16.0 * (1 - eps1) * (eps1 * eps1 - 1.0 / 9.0);
				else if (index == 1)
					return 27.0 / 16.0 * (eps1 * eps1 - 1) * (eps1 - 1.0 / 3.0);
				else if (index == 2)
					return 27.0 / 16.0 * (1 - eps1 * eps1) * (eps1 + 1.0 / 3.0);
				else if (index == 3)
					return 9.0 / 16.0 * (1 + eps1) * (eps1 * eps1 - 1.0 / 9.0);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}
		return 0.0;
	}

	/**
	 * Returns the value of derivative of interpolation function.
	 * 
	 * @param eps1
	 *            Natural coordinate-1.
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function.
	 */
	public double getDerFunction(double eps1, int index) {

		// lagrange family
		if (family_ == Interpolation1D.lagrange_) {

			// linear
			if (degree_ == Interpolation1D.linear_) {
				if (index == 0)
					return -0.5;
				else if (index == 1)
					return 0.5;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// quadratic
			else if (degree_ == Interpolation1D.quadratic_) {
				if (index == 0)
					return -0.5 + eps1;
				else if (index == 1)
					return -2.0 * eps1;
				else if (index == 2)
					return 0.5 + eps1;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// cubic
			else if (degree_ == Interpolation1D.cubic_) {
				if (index == 0)
					return -1.6875 * (-0.718234 + eps1) * (0.0515668 + eps1);
				else if (index == 1)
					return 5.0625 * (-0.699056 + eps1) * (0.476834 + eps1);
				else if (index == 2)
					return -5.0625 * (-0.476834 + eps1) * (0.699056 + eps1);
				else if (index == 3)
					return 1.6875 * (-0.0515668 + eps1) * (0.718234 + eps1);
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