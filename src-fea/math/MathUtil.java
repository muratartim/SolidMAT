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

import Jama.Matrix;

/**
 * This class provides additional utility methods for general use. It uses an
 * external library, "JAMA".
 * 
 * @author Murat Artim
 * 
 */
public class MathUtil {

	/** Tolerance for searching activities. */
	private static final double tol_ = Math.pow(10, -8);

	/**
	 * Returns the determinant of the given matrix.
	 * 
	 * @param arg0
	 *            Two dimensional array representing the matrix.
	 * @return The determinant of the given matrix.
	 */
	public static double determinant(double[][] arg0) {
		Matrix mat = new Matrix(arg0);
		return mat.det();
	}

	/**
	 * Returns the transpose of the given matrix.
	 * 
	 * @param arg0
	 *            Two dimensional array representing the matrix.
	 * @return The transpose of the given matrix.
	 */
	public static double[][] transpose(double[][] arg0) {
		Matrix mat = new Matrix(arg0);
		return mat.transpose().getArray();
	}

	/**
	 * Returns the inverse of the given matrix.
	 * 
	 * @param arg0
	 *            Two dimensional array representing the matrix.
	 * @return The inverse of the given matrix.
	 */
	public static double[][] inverse(double[][] arg0) {
		Matrix mat = new Matrix(arg0);
		return mat.inverse().getArray();
	}

	/**
	 * Returns the minimum value of the given integer array.
	 * 
	 * @param arg0
	 *            The integer array.
	 * @return The minimum value.
	 */
	public static int minVal(int[] arg0) {

		// initialize min value
		int min = arg0[0];

		// loop over array
		for (int i = 0; i < arg0.length; i++)
			if (arg0[i] < min)
				min = arg0[i];

		// return min value
		return min;
	}

	/**
	 * Returns the minimum value of the given double array.
	 * 
	 * @param arg0
	 *            The double array.
	 * @return The minimum value.
	 */
	public static double minVal(double[] arg0) {

		// initialize min value
		double min = arg0[0];

		// loop over array
		for (int i = 0; i < arg0.length; i++)
			if (arg0[i] < min)
				min = arg0[i];

		// return min value
		return min;
	}

	/**
	 * Returns the maximum value of the given integer array.
	 * 
	 * @param arg0
	 *            The integer array.
	 * @return The maximum value.
	 */
	public static int maxVal(int[] arg0) {

		// initialize max value
		int max = arg0[0];

		// loop over array
		for (int i = 0; i < arg0.length; i++)
			if (arg0[i] > max)
				max = arg0[i];

		// return max value
		return max;
	}

	/**
	 * Returns the maximum value of the given double array.
	 * 
	 * @param arg0
	 *            The double array.
	 * @return The maximum value.
	 */
	public static double maxVal(double[] arg0) {

		// initialize max value
		double max = arg0[0];

		// loop over array
		for (int i = 0; i < arg0.length; i++)
			if (arg0[i] > max)
				max = arg0[i];

		// return max value
		return max;
	}

	/**
	 * Sorts given integer array in increasing or decreasing order, depending on
	 * the second parameter.
	 * 
	 * @param arg0
	 *            Integer array.
	 * @param arg1
	 *            True if increasing order, False if decreasing order.
	 */
	public static void sort(int[] arg0, boolean arg1) {

		// loop over array for pivot
		for (int i = 0; i < arg0.length - 1; i++) {

			// loop over remaining elements
			for (int j = i + 1; j < arg0.length; j++) {

				// increasing sorting
				if (arg1) {

					// compare pivot
					if (arg0[i] > arg0[j]) {

						// exchange positions
						int temp = arg0[i];
						arg0[i] = arg0[j];
						arg0[j] = temp;
					}
				}

				// decreasing sorting
				else {

					// compare pivot
					if (arg0[i] < arg0[j]) {

						// exchange positions
						int temp = arg0[i];
						arg0[i] = arg0[j];
						arg0[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * Sorts given double array in increasing or decreasing order, depending on
	 * the second parameter.
	 * 
	 * @param arg0
	 *            Double array.
	 * @param arg1
	 *            True if increasing order, False if decreasing order.
	 */
	public static void sort(double[] arg0, boolean arg1) {

		// loop over array for pivot
		for (int i = 0; i < arg0.length - 1; i++) {

			// loop over remaining elements
			for (int j = i + 1; j < arg0.length; j++) {

				// increasing sorting
				if (arg1) {

					// compare pivot
					if (arg0[i] > arg0[j]) {

						// exchange positions
						double temp = arg0[i];
						arg0[i] = arg0[j];
						arg0[j] = temp;
					}
				}

				// decreasing sorting
				else {

					// compare pivot
					if (arg0[i] < arg0[j]) {

						// exchange positions
						double temp = arg0[i];
						arg0[i] = arg0[j];
						arg0[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * Returns the index of given element. If not in the array returns -1.
	 * 
	 * @param arg0
	 *            Integer array.
	 * @param arg1
	 *            Integer value
	 * @return Index of the element, -1 if not contained.
	 */
	public static int indexOf(int[] arg0, int arg1) {

		// loop over array
		for (int i = 0; i < arg0.length; i++)

			// return index if contained
			if (arg0[i] == arg1)
				return i;

		// return -1 if not
		return -1;
	}

	/**
	 * Returns the index of given element. If not in the array returns -1.
	 * 
	 * @param arg0
	 *            Double array.
	 * @param arg1
	 *            Double value
	 * @return Index of the element, -1 if not contained.
	 */
	public static int indexOf(double[] arg0, double arg1) {

		// loop over array
		for (int i = 0; i < arg0.length; i++)

			// return index if contained
			if (arg0[i] - tol_ <= arg1 && arg1 <= arg0[i] + tol_)
				return i;

		// return -1 if not
		return -1;
	}
}
