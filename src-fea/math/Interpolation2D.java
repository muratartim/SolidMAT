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
package math;

/**
 * Class for two dimensional interpolation functions.
 * 
 * @author Murat
 * 
 */
public class Interpolation2D {

	/** Static variable for the degree of interpolation function. */
	public final static int bilinear_ = 1, biquadratic_ = 2, bicubic_ = 3;

	/** Static variable for the geometry of interpolation function. */
	public final static int quadrangular_ = 0, triangular_ = 1;

	/** The degree of interpolation function. */
	private int degree_;

	/** The degree of interpolation function. */
	private int geometry_;

	/**
	 * Creates two dimensional interpolation function.
	 * 
	 * @param degree
	 *            The degree of interpolation.
	 * @param geometry
	 *            The geometry of interpolation.
	 */
	public Interpolation2D(int degree, int geometry) {

		// set degree
		if (degree < 1 || degree > 3)
			exceptionHandler("Illegal assignment for the degree of interpolation!");
		else
			degree_ = degree;

		// set geometry
		if (geometry < 0 || geometry > 1)
			exceptionHandler("Illegal assignment for the geometry of interpolation!");
		else if (geometry == 1 && degree == 3)
			exceptionHandler("Illegal assignment for the geometry of interpolation!");
		else
			geometry_ = geometry;
	}

	/**
	 * Returns the degree of interpolation in both directions.
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
	 * @param index
	 *            The index of demanded function.
	 * @return The value of interpolation function.
	 */
	public double getFunction(double eps1, double eps2, int index) {

		// quadrangular geometry
		if (geometry_ == Interpolation2D.quadrangular_) {

			// bilinear (lagrange family)
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return 0.25 * (1 - eps1) * (1 - eps2);
				else if (index == 1)
					return 0.25 * (1 + eps1) * (1 - eps2);
				else if (index == 2)
					return 0.25 * (1 + eps1) * (1 + eps2);
				else if (index == 3)
					return 0.25 * (1 - eps1) * (1 + eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic (serendipity family)
			else if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return -0.25 * (1 - eps1) * (1 - eps2) * (1 + eps1 + eps2);
				else if (index == 1)
					return -0.25 * (1 + eps1) * (1 - eps2) * (1 - eps1 + eps2);
				else if (index == 2)
					return -0.25 * (1 + eps1) * (1 + eps2) * (1 - eps1 - eps2);
				else if (index == 3)
					return -0.25 * (1 - eps1) * (1 + eps2) * (1 + eps1 - eps2);
				else if (index == 4)
					return 0.5 * (1 - eps1 * eps1) * (1 - eps2);
				else if (index == 5)
					return 0.5 * (1 + eps1) * (1 - eps2 * eps2);
				else if (index == 6)
					return 0.5 * (1 - eps1 * eps1) * (1 + eps2);
				else if (index == 7)
					return 0.5 * (1 - eps1) * (1 - eps2 * eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// bicubic (serendipity family)
			else if (degree_ == Interpolation2D.bicubic_) {
				if (index == 0)
					return 1.0 / 32.0 * (1 - eps1) * (1 - eps2)
							* (-10 + 9.0 * (eps1 * eps1 + eps2 * eps2));
				else if (index == 1)
					return 1.0 / 32.0 * (1 + eps1) * (1 - eps2)
							* (-10 + 9.0 * (eps1 * eps1 + eps2 * eps2));
				else if (index == 2)
					return 1.0 / 32.0 * (1 + eps1) * (1 + eps2)
							* (-10 + 9.0 * (eps1 * eps1 + eps2 * eps2));
				else if (index == 3)
					return 1.0 / 32.0 * (1 - eps1) * (1 + eps2)
							* (-10 + 9.0 * (eps1 * eps1 + eps2 * eps2));
				else if (index == 4)
					return 9.0 / 32.0 * (1 - eps2) * (1 - eps1 * eps1)
							* (1 - 3.0 * eps1);
				else if (index == 5)
					return 9.0 / 32.0 * (1 - eps2) * (1 - eps1 * eps1)
							* (1 + 3.0 * eps1);
				else if (index == 6)
					return 9.0 / 32.0 * (1 + eps1) * (1 - eps2 * eps2)
							* (1 - 3.0 * eps2);
				else if (index == 7)
					return 9.0 / 32.0 * (1 + eps1) * (1 - eps2 * eps2)
							* (1 + 3.0 * eps2);
				else if (index == 8)
					return 9.0 / 32.0 * (1 + eps2) * (1 - eps1 * eps1)
							* (1 + 3.0 * eps1);
				else if (index == 9)
					return 9.0 / 32.0 * (1 + eps2) * (1 - eps1 * eps1)
							* (1 - 3.0 * eps1);
				else if (index == 10)
					return 9.0 / 32.0 * (1 - eps1) * (1 - eps2 * eps2)
							* (1 + 3.0 * eps2);
				else if (index == 11)
					return 9.0 / 32.0 * (1 - eps1) * (1 - eps2 * eps2)
							* (1 - 3.0 * eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// triangular geometry
		else if (geometry_ == Interpolation2D.triangular_) {

			// bilinear
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return eps1;
				else if (index == 1)
					return eps2;
				else if (index == 2)
					return 1 - eps1 - eps2;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic
			if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return eps1 * (2.0 * eps1 - 1);
				else if (index == 1)
					return eps2 * (2.0 * eps2 - 1);
				else if (index == 2)
					return (1 - eps1 - eps2) * (1 - 2.0 * eps1 - 2.0 * eps2);
				else if (index == 3)
					return 4.0 * eps1 * eps2;
				else if (index == 4)
					return 4.0 * eps2 * (1 - eps1 - eps2);
				else if (index == 5)
					return 4.0 * eps1 * (1 - eps1 - eps2);
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
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function with respect to
	 *         natural coordinate-1.
	 */
	public double getDer1Function(double eps1, double eps2, int index) {

		// quadrangular geometry
		if (geometry_ == Interpolation2D.quadrangular_) {

			// bilinear
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return -0.25 + 0.25 * eps2;
				else if (index == 1)
					return 0.25 - 0.25 * eps2;
				else if (index == 2)
					return 0.25 * (1 + eps2);
				else if (index == 3)
					return -0.25 * (1 + eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic
			else if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return eps1 * (0.5 - 0.5 * eps2) + (0.25 - 0.25 * eps2)
							* eps2;
				else if (index == 1)
					return eps1 * (0.5 - 0.5 * eps2) + (-0.25 + 0.25 * eps2)
							* eps2;
				else if (index == 2)
					return eps1 * (0.5 + 0.5 * eps2) + (0.25 + 0.25 * eps2)
							* eps2;
				else if (index == 3)
					return eps1 * (0.5 + 0.5 * eps2) + (-0.25 - 0.25 * eps2)
							* eps2;
				else if (index == 4)
					return eps1 * (-1 + eps2);
				else if (index == 5)
					return 0.5 - 0.5 * eps2 * eps2;
				else if (index == 6)
					return -eps1 * (1 + eps2);
				else if (index == 7)
					return -0.5 + 0.5 * eps2 * eps2;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// bicubic
			else if (degree_ == Interpolation2D.bicubic_) {
				if (index == 0)
					return -(-1 + eps2)
							* (-0.84375 * (-1.02722 + eps1) * (0.360555 + eps1) - 0.28125
									* eps2 * eps2);
				else if (index == 1)
					return -(-1 + eps2)
							* (0.84375 * (-0.360555 + eps1) * (1.02722 + eps1) + 0.28125
									* eps2 * eps2);
				else if (index == 2)
					return (1 + eps2)
							* (0.84375 * (-0.360555 + eps1) * (1.02722 + eps1) + 0.28125
									* eps2 * eps2);
				else if (index == 3)
					return (1 + eps2)
							* (-0.84375 * (-1.02722 + eps1) * (0.360555 + eps1) - 0.28125
									* eps2 * eps2);
				else if (index == 4)
					return (-0.699056 + eps1) * (0.476834 + eps1)
							* (2.53125 - 2.53125 * eps2);
				else if (index == 5)
					return (-0.476834 + eps1) * (0.699056 + eps1)
							* (-2.53125 + 2.53125 * eps2);
				else if (index == 6)
					return -0.28125 * (1 - 3.0 * eps2) * (-1 + eps2 * eps2);
				else if (index == 7)
					return -0.28125 * (1 + 3.0 * eps2) * (-1 + eps2 * eps2);
				else if (index == 8)
					return -2.53125 * (-0.476834 + eps1) * (0.699056 + eps1)
							* (1 + eps2);
				else if (index == 9)
					return 2.53125 * (-0.699056 + eps1) * (0.476834 + eps1)
							* (1 + eps2);
				else if (index == 10)
					return 0.28125 * (1 + 3.0 * eps2) * (-1 + eps2 * eps2);
				else if (index == 11)
					return 0.28125 * (1 - 3.0 * eps2) * (-1 + eps2 * eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// triangular geometry
		else if (geometry_ == Interpolation2D.triangular_) {

			// bilinear
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return 1.0;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return -1.0;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic
			if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return -1 + 4.0 * eps1;
				else if (index == 1)
					return 0.0;
				else if (index == 2)
					return -3 + 4.0 * eps1 + 4.0 * eps2;
				else if (index == 3)
					return 4.0 * eps2;
				else if (index == 4)
					return -4.0 * eps2;
				else if (index == 5)
					return 4 - 8.0 * eps1 - 4.0 * eps2;
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
	 * @param index
	 *            The index of demanded function.
	 * @return The value of derivative of interpolation function with respect to
	 *         natural coordinate-2.
	 */
	public double getDer2Function(double eps1, double eps2, int index) {

		// quadrangular geometry
		if (geometry_ == Interpolation2D.quadrangular_) {

			// bilinear
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return -0.25 + 0.25 * eps1;
				else if (index == 1)
					return -0.25 * (1 + eps1);
				else if (index == 2)
					return 0.25 * (1 + eps1);
				else if (index == 3)
					return 0.25 - 0.25 * eps1;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic
			else if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return eps1 * (0.25 - 0.25 * eps1 - 0.5 * eps2) + 0.5
							* eps2;
				else if (index == 1)
					return eps1 * (-0.25 - 0.25 * eps1 + 0.5 * eps2) + 0.5
							* eps2;
				else if (index == 2)
					return eps1 * (0.25 + 0.25 * eps1 + 0.5 * eps2) + 0.5
							* eps2;
				else if (index == 3)
					return eps1 * (-0.25 + 0.25 * eps1 - 0.5 * eps2) + 0.5
							* eps2;
				else if (index == 4)
					return -0.5 + 0.5 * eps1 * eps1;
				else if (index == 5)
					return -(1 + eps1) * eps2;
				else if (index == 6)
					return 0.5 - 0.5 * eps1 * eps1;
				else if (index == 7)
					return (-1 + eps1) * eps2;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// bicubic
			else if (degree_ == Interpolation2D.bicubic_) {
				if (index == 0)
					return -(-1 + eps1)
							* (-0.28125 * eps1 * eps1 - 0.84375
									* (-1.02722 + eps2) * (0.360555 + eps2));
				else if (index == 1)
					return (1 + eps1)
							* (-0.28125 * eps1 * eps1 - 0.84375
									* (-1.02722 + eps2) * (0.360555 + eps2));
				else if (index == 2)
					return (1 + eps1)
							* (0.28125 * eps1 * eps1 + 0.84375
									* (-0.360555 + eps2) * (1.02722 + eps2));
				else if (index == 3)
					return -(-1 + eps1)
							* (0.28125 * eps1 * eps1 + 0.84375
									* (-0.360555 + eps2) * (1.02722 + eps2));
				else if (index == 4)
					return 0.28125 * (1 - 3.0 * eps1) * (-1 + eps1 * eps1);
				else if (index == 5)
					return 0.28125 * (1 + 3.0 * eps1) * (-1 + eps1 * eps1);
				else if (index == 6)
					return 2.53125 * (1 + eps1) * (-0.699056 + eps2)
							* (0.476834 + eps2);
				else if (index == 7)
					return -2.53125 * (1 + eps1) * (-0.476834 + eps2)
							* (0.699056 + eps2);
				else if (index == 8)
					return -0.28125 * (1 + 3.0 * eps1) * (-1 + eps1 * eps1);
				else if (index == 9)
					return -0.28125 * (1 - 3.0 * eps1) * (-1 + eps1 * eps1);
				else if (index == 10)
					return (-2.53125 + 2.53125 * eps1) * (-0.476834 + eps2)
							* (0.699056 + eps2);
				else if (index == 11)
					return (2.53125 - 2.53125 * eps1) * (-0.699056 + eps2)
							* (0.476834 + eps2);
				else
					exceptionHandler("Illegal index for interpolation function!");
			}
		}

		// triangular geometry
		else if (geometry_ == Interpolation2D.triangular_) {

			// bilinear
			if (degree_ == Interpolation2D.bilinear_) {
				if (index == 0)
					return 0.0;
				else if (index == 1)
					return 1.0;
				else if (index == 2)
					return -1.0;
				else
					exceptionHandler("Illegal index for interpolation function!");
			}

			// biquadratic
			if (degree_ == Interpolation2D.biquadratic_) {
				if (index == 0)
					return 0.0;
				else if (index == 1)
					return -1 + 4.0 * eps2;
				else if (index == 2)
					return -3 + 4.0 * eps1 + 4.0 * eps2;
				else if (index == 3)
					return 4.0 * eps1;
				else if (index == 4)
					return 4 - 4.0 * eps1 - 8.0 * eps2;
				else if (index == 5)
					return -4.0 * eps1;
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
