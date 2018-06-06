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
package node;

import java.io.Serializable;

import matrix.DMat;

/**
 * Class for nodal spring.
 * 
 * @author Murat
 * 
 */
public class NodalSpring implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of spring. */
	public final static int global_ = 0, local_ = 1;

	/** The stiffness matrix of spring. */
	private double[][] stiffness_;

	/** The coordinate system of spring. */
	private int coordinateSystem_;

	/** The name of nodal spring. */
	private String name_;

	/**
	 * Creates nodal spring.
	 * 
	 * @param name
	 *            Name of nodal spring.
	 * @param coordinateSystem
	 *            The coordinate system of the spring.
	 * @param stiffness
	 *            The stiffness matrix of the spring. The spring stiffness
	 *            matrix should be positive definite and symmetric.
	 */
	public NodalSpring(String name, int coordinateSystem, DMat stiffness) {

		// set name
		name_ = name;

		// set coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal assignment for coordinate system!");
		else
			coordinateSystem_ = coordinateSystem;

		// check stiffness matrix
		if (stiffness.rowCount() != 6 || stiffness.columnCount() != 6)
			exceptionHandler("Illegal stiffness matrix for spring!");
		if (stiffness.determinant() < 0)
			exceptionHandler("Illegal stiffness matrix for spring!");
		if (!stiffness.isSymmetric())
			exceptionHandler("Illegal stiffness matrix for spring!");

		// set stiffness
		stiffness_ = stiffness.get2DArray();
	}

	/**
	 * Returns name of the nodal spring.
	 * 
	 * @return Name of nodal spring.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the stiffness matrix of spring.
	 * 
	 * @return The stiffness matrix of spring.
	 */
	public DMat getStiffness() {
		return new DMat(stiffness_);
	}

	/**
	 * Returns the coordinate system of spring.
	 * 
	 * @return The coordinate system of spring.
	 */
	public int getCoordinateSystem() {
		return coordinateSystem_;
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
