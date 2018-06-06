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
 * Class for all sparse matrices.
 * 
 * @author Murat Artim
 */
public abstract class SMat extends Mat {

	/**
	 * Returns a deep copy of this matrix.
	 * 
	 * @return A deep copy of this matrix.
	 */
	public abstract SMat copy();

	/**
	 * Returns scaled matrix (A = s * A).
	 * 
	 * @param arg0
	 *            The scaling factor (s).
	 * @return This matrix (A).
	 */
	public abstract SMat scale(double arg0);

	/**
	 * Adds matrices (A = A + B).
	 * 
	 * @param arg0
	 *            The matrix to be added (B).
	 * @return This matrix (A).
	 */
	public abstract SMat add(SMat arg0);

	/**
	 * Performs linear algebraic matrix-vector multiplication (c = A * b).
	 * 
	 * @param arg0
	 *            The vector to be multiplied (b).
	 * @return The product vector (c).
	 */
	public abstract DVec multiply(DVec arg0);
}
