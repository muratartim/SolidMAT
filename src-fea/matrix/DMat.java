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

import math.MathUtil;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.EVD;
import no.uib.cipr.matrix.NotConvergedException;

/**
 * Class for general dense matrix. This class uses an external library called
 * "MTJ".
 * 
 * @author Murat Artim
 */
public class DMat extends Mat {

	/** The DenseMatrix object. */
	public DenseMatrix mat_;

	/** Row and column counts of matrix. */
	private int row_, col_;

	/**
	 * Static variable for the order of transformation in the creation of
	 * rotation matrix.
	 */
	public static final int zyx_ = 0, xyz_ = 1;

	/** Static variable for the coordinate system to be transformed. */
	public static final int toGlobal_ = 0, toLocal_ = 1;

	/**
	 * Creates matrix from a Matrix object.
	 * 
	 * @param mat
	 *            Matrix object.
	 */
	public DMat(Matrix mat) {
		mat_ = (DenseMatrix) mat;
		row_ = mat_.numRows();
		col_ = mat_.numColumns();
	}

	/**
	 * Creates an arg0 x arg1 matrix of zeros.
	 * 
	 * @param arg0
	 *            Number of rows.
	 * @param arg1
	 *            Number of columns.
	 */
	public DMat(int arg0, int arg1) {
		try {
			mat_ = new DenseMatrix(arg0, arg1);
			row_ = mat_.numRows();
			col_ = mat_.numColumns();
		} catch (Exception excep) {
			exceptionHandler("Matrix has illegal dimensions!");
		}
	}

	/**
	 * Creates matrix from two dimensional double array.
	 * 
	 * @param arg0
	 *            Two dimensional array.
	 */
	public DMat(double[][] arg0) {
		try {
			mat_ = new DenseMatrix(arg0);
			row_ = mat_.numRows();
			col_ = mat_.numColumns();
		} catch (Exception excep) {
			exceptionHandler("Matrix has illegal dimensions!");
		}
	}

	/**
	 * Creates identity matrix.
	 * 
	 * @param arg0
	 *            The dimension of identity matrix.
	 */
	public DMat(int arg0) {
		try {
			mat_ = Matrices.identity(arg0);
			row_ = mat_.numRows();
			col_ = mat_.numColumns();
		} catch (Exception excep) {
			exceptionHandler("Matrix has illegal dimensions!");
		}
	}

	/**
	 * Creates three dimensional transformation matrix from Euler angles. The
	 * transformation is in either X-Y-Z or Z-Y-X order depending on the order
	 * parameter.
	 * 
	 * @param r1
	 *            Rotation about x in degrees.
	 * @param r2
	 *            X-Y-Z order -> Rotation about y' in degrees, Z-Y-X order ->
	 *            Rotation about z' in degrees.
	 * @param r3
	 *            X-Y-Z order -> Rotation about z'' in degrees, Z-Y-X order ->
	 *            Rotation about y'' in degrees.
	 * @param order
	 *            The order of rotation, X-Y-Z or Z-Y-X.
	 */
	public DMat(double r1, double r2, double r3, int order) {

		// convert angles to radians
		r1 = Math.toRadians(r1);
		r2 = Math.toRadians(r2);
		r3 = Math.toRadians(r3);

		// X-Y-Z order
		if (order == DMat.xyz_) {

			// set rotation about x
			DMat rx = new DMat(3, 3);
			rx.set(0, 0, 1);
			rx.set(0, 1, 0);
			rx.set(0, 2, 0);
			rx.set(1, 0, 0);
			rx.set(1, 1, Math.cos(r1));
			rx.set(1, 2, Math.sin(r1));
			rx.set(2, 0, 0);
			rx.set(2, 1, -Math.sin(r1));
			rx.set(2, 2, Math.cos(r1));

			// set rotation about y'
			DMat ry = new DMat(3, 3);
			ry.set(0, 0, Math.cos(r2));
			ry.set(0, 1, 0);
			ry.set(0, 2, -Math.sin(r2));
			ry.set(1, 0, 0);
			ry.set(1, 1, 1);
			ry.set(1, 2, 0);
			ry.set(2, 0, Math.sin(r2));
			ry.set(2, 1, 0);
			ry.set(2, 2, Math.cos(r2));

			// set rotation about z''
			DMat rz = new DMat(3, 3);
			rz.set(0, 0, Math.cos(r3));
			rz.set(0, 1, Math.sin(r3));
			rz.set(0, 2, 0);
			rz.set(1, 0, -Math.sin(r3));
			rz.set(1, 1, Math.cos(r3));
			rz.set(1, 2, 0);
			rz.set(2, 0, 0);
			rz.set(2, 1, 0);
			rz.set(2, 2, 1);

			// set transformation matrix
			mat_ = rz.multiply(ry).multiply(rx).mat_;
			row_ = mat_.numRows();
			col_ = mat_.numColumns();
		}

		// Z-Y-X order
		else if (order == DMat.zyx_) {

			// set rotation about x
			DMat rx = new DMat(3, 3);
			rx.set(0, 0, 1);
			rx.set(0, 1, 0);
			rx.set(0, 2, 0);
			rx.set(1, 0, 0);
			rx.set(1, 1, Math.cos(r1));
			rx.set(1, 2, Math.sin(r1));
			rx.set(2, 0, 0);
			rx.set(2, 1, -Math.sin(r1));
			rx.set(2, 2, Math.cos(r1));

			// set rotation about z'
			DMat rz = new DMat(3, 3);
			rz.set(0, 0, Math.cos(r2));
			rz.set(0, 1, Math.sin(r2));
			rz.set(0, 2, 0);
			rz.set(1, 0, -Math.sin(r2));
			rz.set(1, 1, Math.cos(r2));
			rz.set(1, 2, 0);
			rz.set(2, 0, 0);
			rz.set(2, 1, 0);
			rz.set(2, 2, 1);

			// set rotation about y''
			DMat ry = new DMat(3, 3);
			ry.set(0, 0, Math.cos(-r3));
			ry.set(0, 1, 0);
			ry.set(0, 2, -Math.sin(-r3));
			ry.set(1, 0, 0);
			ry.set(1, 1, 1);
			ry.set(1, 2, 0);
			ry.set(2, 0, Math.sin(-r3));
			ry.set(2, 1, 0);
			ry.set(2, 2, Math.cos(-r3));

			// set transformation matrix
			mat_ = rx.multiply(ry).multiply(rz).mat_;
			row_ = mat_.numRows();
			col_ = mat_.numColumns();
		} else
			exceptionHandler("Illegal assignment for the order of transformation!");
	}

	/**
	 * Adds matrices (C = A + B).
	 * 
	 * @param arg0
	 *            The matrix to be added (B).
	 * @return The resulting matrix (C).
	 */
	public DMat add(DMat arg0) {

		// check for dimensions
		if (rowCount() != arg0.rowCount()
				|| columnCount() != arg0.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// add
		return new DMat(mat_.copy().add(arg0.mat_));
	}

	/**
	 * Adds element to specified position.
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

		// add elements and set
		mat_.add(arg0, arg1, arg2);
	}

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public int columnCount() {
		return col_;
	}

	/**
	 * Applies condensation to matrix in order to condense out force degrees of
	 * freedom. Note that kuu is assumed to be a zero matrix.
	 * 
	 * @param arg0
	 *            The dimension of condensed matrix.
	 * @return The condensed matrix.
	 */
	public DMat condense(int arg0) {

		// get row and column counts
		int rc = rowCount();
		int cc = columnCount();

		// check if its square matrix
		if (rc != cc)
			exceptionHandler("Matrix is not a square matrix!");

		// check for illegal dimension for condensed matrix
		if (arg0 <= 0 || arg0 >= rc)
			exceptionHandler("Illegal dimension for condensed matrix!");

		// create sub-matrices
		DMat krr = getSubMatrix(0, 0, rc - arg0 - 1, cc - arg0 - 1);
		DMat kru = getSubMatrix(0, cc - arg0, rc - arg0 - 1, cc - 1);

		// compute condensed matrix
		return kru.transpose().multiply(krr.invert()).multiply(kru).scale(-1.0);
	}

	/**
	 * Returns the determinant.
	 * 
	 * @return The determinant.
	 */
	public double determinant() {
		return MathUtil.determinant(get2DArray());
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
	 * Returns two dimensional array storing elements of matrix.
	 * 
	 * @return Two dimensional array.
	 */
	public double[][] get2DArray() {
		return Matrices.getArray(mat_.copy());
	}

	/**
	 * Returns the eigenvalues of a square matrix in an array. The eigenvalues
	 * are sorted in increasing order.
	 * 
	 * @return Array storing the eigenvalues.
	 */
	public double[] getEigenvalue() {

		// check if its square matrix
		if (rowCount() != columnCount())
			exceptionHandler("Matrix is not a square matrix!");

		// compute eigenvalues
		double[] values = null;
		try {
			values = EVD.factorize(mat_).getRealEigenvalues();
		} catch (NotConvergedException e) {
			exceptionHandler("Eigenvalues could not be found!");
		}

		// sort eigenvalues in increasing order
		MathUtil.sort(values, true);

		// return eigenvalues
		return values;
	}

	/**
	 * Returns sub-matrix in the given intervals.
	 * 
	 * @param arg0
	 *            Initial row index.
	 * @param arg1
	 *            Initial column index.
	 * @param arg2
	 *            Final row index.
	 * @param arg3
	 *            Final column index.
	 * @return The sub-matrix.
	 */
	public DMat getSubMatrix(int arg0, int arg1, int arg2, int arg3) {

		// check for illegal initial indices
		if (arg0 < 0 || arg1 < 0 || arg0 > rowCount() - 1
				|| arg1 > columnCount() - 1)
			exceptionHandler("Illegal indices!");

		// check for illegal final indices
		if (arg2 < 0 || arg3 < 0 || arg2 > rowCount() - 1
				|| arg3 > columnCount() - 1)
			exceptionHandler("Illegal indices!");

		// check for illegal mutual indices
		if (arg2 < arg0 || arg3 < arg1)
			exceptionHandler("Illegal indices!");

		// get sub-matrix
		DMat B = new DMat(arg2 - arg0 + 1, arg3 - arg1 + 1);
		for (int i = arg0; i <= arg2; i++)
			for (int j = arg1; j <= arg3; j++)
				B.set(i - arg0, j - arg1, get(i, j));

		// return sub-matrix
		return B;
	}

	/**
	 * Returns the inverse of this matrix (B = Inverse(A)).
	 * 
	 * @return The inversed matrix (B).
	 */
	public DMat invert() {
		return new DMat(MathUtil.inverse(get2DArray()));
	}

	/**
	 * Checks if the matrix is symmetric.
	 * 
	 * @return True if matrix is symmetric, false vice versa.
	 */
	public boolean isSymmetric() {

		// check if square matrix
		if (rowCount() != columnCount())
			exceptionHandler("Matrix is not a square matrix!");

		// loop over rows and columns
		for (int i = 0; i < rowCount(); i++) {
			for (int j = 0; j < columnCount(); j++) {
				if (i != j)
					if (get(i, j) != get(j, i))
						return false;
			}
		}
		return true;
	}

	/**
	 * Returns row-sum lumped matrix. The matrix should be square, symmetric and
	 * positive definite.
	 * 
	 * @return Row-sum lumped matrix.
	 */
	public DMat lump() {

		// check if it is square
		if (rowCount() != columnCount())
			exceptionHandler("Matrix is not a square matrix!");

		// row-summing operation
		double value = 0.0;
		double[][] B = new double[rowCount()][columnCount()];
		for (int i = 0; i < rowCount(); i++) {
			for (int j = 0; j < columnCount(); j++)
				value += get(i, j);
			B[i][i] = value;
			value = 0.0;
		}

		// return lumped matrix
		return new DMat(B);
	}

	/**
	 * Mirrors the upper triangular part of the matrix to lower triangular part.
	 * 
	 * @return The new matrix.
	 */
	public DMat mirror() {

		// check if it is square matrix
		if (rowCount() != columnCount())
			exceptionHandler("Matrix is not a square matrix!");

		// get array of matrix
		double[][] B = get2DArray();

		// perform operation
		for (int i = 0; i < rowCount(); i++)
			for (int j = i + 1; j < columnCount(); j++)
				B[j][i] = B[i][j];

		// return new matrix
		return new DMat(B);
	}

	/**
	 * Performs linear algebraic matrix multiplication (C = A * B).
	 * 
	 * @param arg0
	 *            The second matrix to be multiplied (B).
	 * @return The product matrix (C).
	 */
	public DMat multiply(DMat arg0) {

		// check for dimensions
		if (columnCount() != arg0.rowCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// multiply
		DMat C = new DMat(rowCount(), arg0.columnCount());
		mat_.mult(arg0.mat_, C.mat_);
		return C;
	}

	/**
	 * Performs linear algebraic matrix-vector multiplication (c = A * b).
	 * 
	 * @param arg0
	 *            The vector to be multiplied (b).
	 * @return The product vector (c).
	 */
	public DVec multiply(DVec arg0) {

		// check for dimensions
		if (columnCount() != arg0.rowCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// multiply
		DVec c = new DVec(rowCount());
		mat_.mult(arg0.vec_, c.vec_);
		return c;
	}

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public int rowCount() {
		return row_;
	}

	/**
	 * Returns scaled matrix (B = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return The scaled matrix (B).
	 */
	public DMat scale(double arg0) {
		return new DMat(mat_.copy().scale(arg0));
	}

	/**
	 * Sets the element to respective place.
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
		mat_.set(arg0, arg1, arg2);
	}

	/**
	 * Sets sub-matrix to respective place.
	 * 
	 * @param arg0
	 *            The sub-matrix.
	 * @param arg1
	 *            Initial row index.
	 * @param arg2
	 *            Initial column index.
	 * @return The new matrix.
	 */
	public DMat setSubMatrix(DMat arg0, int arg1, int arg2) {

		// check for illegal indices
		if (arg1 < 0 || arg2 < 0 || arg1 > rowCount() - 1
				|| arg2 > columnCount() - 1)
			exceptionHandler("Illegal indices!");

		// compute final row and column indices
		int ii = arg1 + arg0.rowCount() - 1;
		int jj = arg2 + arg0.columnCount() - 1;

		// check for illegal placement of submatrix
		if (ii > rowCount() - 1 || jj > columnCount() - 1)
			exceptionHandler("Illegal addition of sub-matrix!");

		// loop over rows of sub-matrix
		DMat C = new DMat(mat_.copy());
		for (int i = 0; i < arg0.rowCount(); i++)

			// loop over columns of sub-matrix
			for (int j = 0; j < arg0.columnCount(); j++)

				// add element to respective place
				C.set(arg1 + i, arg2 + j, arg0.get(i, j));

		// return resulting matrix
		return C;
	}

	/**
	 * Solves linear system of equations (Ax=b). It uses direct solver.
	 * 
	 * @param arg0
	 *            The right hand side vector (b).
	 * @param arg1
	 *            The solution vector (x).
	 */
	public void solve(DVec arg0, DVec arg1) {
		mat_.solve(arg0.vec_, arg1.vec_);
	}

	/**
	 * Subtracts matrices.
	 * 
	 * @param arg0
	 *            The second matrix.
	 * @return The result matrix.
	 */
	public DMat subtract(DMat arg0) {

		// check for dimensions
		if (rowCount() != arg0.rowCount()
				|| columnCount() != arg0.columnCount())
			exceptionHandler("Matrix dimensions don't agree!");

		// subtract
		return add(arg0.scale(-1.0));
	}

	/**
	 * Transforms the matrix to the demanded coordinate system.
	 * 
	 * @param tr
	 *            The transformation matrix.
	 * @param toSystem
	 *            The coordinate system that the matrix will be transformed.
	 * @return The transformed matrix.
	 */
	public DMat transform(DMat tr, int toSystem) {

		// check coordinate system
		if (toSystem < 0 || toSystem > 1)
			exceptionHandler("Illegal coordinate transformation!");

		// to global coordinate system
		if (toSystem == toGlobal_)
			return tr.transpose().multiply(multiply(tr));

		// to local coordinate system
		else
			return tr.multiply(multiply(tr.transpose()));
	}

	/**
	 * Returns the transpose of matrix (B = Transpose(A)).
	 * 
	 * @return The transpose (B).
	 */
	public DMat transpose() {
		return new DMat(MathUtil.transpose(get2DArray()));
	}

	/**
	 * Deletes internal storage and frees memory.
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
		mat_ = new DenseMatrix(row_, col_);
	}
}
