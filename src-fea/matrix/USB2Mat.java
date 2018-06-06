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

/**
 * Class for upper-symmetric-banded matrix stored in 2D array (USB2).
 * 
 * @author Murat Artim
 */
public class USB2Mat extends SMat {

	/** The dimension and the half-bandwidth. */
	private int n_, hbw_;

	/** Internal storage array. */
	private double[][] mat_;

	/**
	 * Creates arg0 by arg0 upper-symmetric-banded-square matrix.
	 * 
	 * @param arg0
	 *            Dimension.
	 * @param arg1
	 *            Half-bandwidth.
	 */
	public USB2Mat(int arg0, int arg1) {
		mat_ = new double[arg0][arg1 + 1];
		n_ = arg0;
		hbw_ = arg1;
	}

	/**
	 * Creates sparse matrix from another sparse matrix.
	 * 
	 * @param arg0
	 *            Sparse matrix.
	 */
	public USB2Mat(USB2Mat arg0) {

		// copy internal storage
		mat_ = new double[arg0.rowCount()][arg0.getHalfBandwidth() + 1];
		for (int i = 0; i < arg0.rowCount(); i++)
			for (int j = 0; j < arg0.getHalfBandwidth() + 1; j++)
				mat_[i][j] = arg0.getData()[i][j];

		// copy size and half-bandwidth
		n_ = arg0.rowCount();
		hbw_ = arg0.getHalfBandwidth();
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
		USB2Mat arg = (USB2Mat) arg0;

		// check for dimensions
		if (rowCount() != arg.rowCount() || columnCount() != arg.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// check half bandwidths
		if (getHalfBandwidth() < arg.getHalfBandwidth())
			exceptionHandler("Matrix bandwidths don't agree!");

		// add
		for (int i = 0; i < rowCount(); i++)
			for (int j = 0; j < columnCount(); j++)
				add(i, j, arg.get(i, j));
		return this;
	}

	/**
	 * Adds element to the specified position. Entries outside bandwidth will be
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
		if (Math.max(0, arg1 - hbw_) <= arg0 && arg0 <= arg1)
			mat_[arg0][arg1 - arg0] += arg2;
	}

	/**
	 * Sets the element to respective place. Entries outside bandwidth will be
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
		if (Math.max(0, arg1 - hbw_) <= arg0 && arg0 <= arg1)
			mat_[arg0][arg1 - arg0] = arg2;
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return n_;
	}

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public int columnCount() {
		return n_;
	}

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public USB2Mat copy() {
		return new USB2Mat(this);
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
		for (int i = 0; i < rowCount(); i++)
			for (int j = 0; j < columnCount(); j++)
				vec.set(i, get(i, j) * arg0.get(i));
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
		if (arg0 < arg1 + hbw_ + 1 && arg1 < arg0 + hbw_ + 1) {
			if (arg0 <= arg1)
				return mat_[arg0][arg1 - arg0];
			else
				return mat_[arg1][arg0 - arg1];
		}
		return 0.0;
	}

	/**
	 * Returns the internal storage array.
	 * 
	 * @return The internal storage array.
	 */
	public double[][] getData() {
		return mat_;
	}

	/**
	 * Returns the number of super diagonals.
	 * 
	 * @return The number of super diagonals.
	 */
	public int getHalfBandwidth() {
		return hbw_;
	}

	/**
	 * Deletes internal storage arrays and frees memory.
	 * 
	 */
	public void delete() {
		mat_ = null;
	}

	/**
	 * Reallocates internal storage arrays.
	 * 
	 */
	public void reAllocate() {
		mat_ = new double[n_][hbw_ + 1];
	}

	/**
	 * Sets the element to internal storage array.
	 * 
	 * @param arg0
	 *            Row index of element.
	 * @param arg1
	 *            Column index of element.
	 * @param arg2
	 *            Element to be set.
	 */
	public void setToInt(int arg0, int arg1, double arg2) {
		mat_[arg0][arg1] = arg2;
	}

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public USB2Mat scale(double arg0) {
		for (int i = 0; i < n_; i++)
			for (int j = 0; j < hbw_ + 1; j++)
				mat_[i][j] *= arg0;
		return this;
	}
}
