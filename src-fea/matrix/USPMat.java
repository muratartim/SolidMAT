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
package matrix;

import no.uib.cipr.matrix.UpperSPDPackMatrix;

/**
 * Class for upper-symmetric-packed-square matrix (USP). Can be used for the
 * solution of generalized eigenvalue problems. This class uses an external
 * library called "MTJ".
 * 
 * @author Murat Artim
 */
public class USPMat extends SMat {

	/** The UpperSPDPackMatrix object. */
	public UpperSPDPackMatrix mat_;

	/**
	 * Creates a new arg0 x arg0 sparse matrix.
	 * 
	 * @param arg0
	 *            Size of the matrix.
	 */
	public USPMat(int arg0) {
		try {
			mat_ = new UpperSPDPackMatrix(arg0);
		} catch (Exception excep) {
			exceptionHandler("Matrix has illegal dimensions!");
		}
	}

	/**
	 * Creates sparse matrix from another sparse matrix.
	 * 
	 * @param arg0
	 *            Sparse matrix.
	 */
	public USPMat(USPMat arg0) {
		mat_ = (UpperSPDPackMatrix) arg0.mat_.copy();
	}

	/**
	 * Adds matrices (A = A + B).
	 * 
	 * @param arg0
	 *            The matrix to be added (B).
	 * @return This matrix (A).
	 */
	public SMat add(SMat arg0) {

		// cast given matrix
		USPMat arg = (USPMat) arg0;

		// check for dimensions
		if (rowCount() != arg.rowCount() || columnCount() != arg.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// add
		mat_.add(arg.mat_);
		return this;
	}

	/**
	 * Adds element to the specified position. Entries outside bandwiths will be
	 * ignored.
	 * 
	 * @param arg0
	 *            The row index.
	 * @param arg1
	 *            The column index.
	 * @param arg2
	 *            The value to be added.
	 */
	public void add(int arg0, int arg1, double arg2) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// add elements
		if (arg0 <= arg1)
			mat_.add(arg0, arg1, arg2);
	}

	/**
	 * Sets the element to respective place. Entries outside bandwiths will be
	 * ignored.
	 * 
	 * @param arg0
	 *            The row index to be set.
	 * @param arg1
	 *            The column index to be set.
	 * @param arg2
	 *            The element to be set.
	 */
	public void set(int arg0, int arg1, double arg2) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// set element
		if (arg0 <= arg1)
			mat_.set(arg0, arg1, arg2);
	}

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public int columnCount() {
		return mat_.numColumns();
	}

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public USPMat copy() {
		return new USPMat(this);
	}

	/**
	 * Performs linear algebraic matrix-vector multiplication (c = A * b).
	 * 
	 * @param arg0
	 *            The vector to be multiplied (b).
	 * @return The product vector (c).
	 */
	public DVec multiply(DVec arg0) {
		DVec vec = new DVec(arg0.rowCount());
		mat_.mult(arg0.vec_, vec.vec_);
		return vec;
	}

	/**
	 * Returns the demanded element.
	 * 
	 * @param arg0
	 *            The row index of demanded element.
	 * @param arg1
	 *            The column index of demanded element.
	 * @return The demanded element.
	 */
	public double get(int arg0, int arg1) {

		// check indices
		if (arg0 < 0 || arg0 >= rowCount())
			exceptionHandler("Illegal row index!");
		if (arg1 < 0 || arg1 >= columnCount())
			exceptionHandler("Illegal column index!");

		// return element
		return mat_.get(arg0, arg1);
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return mat_.numRows();
	}

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public USPMat scale(double arg0) {
		mat_.scale(arg0);
		return this;
	}
}
