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
package boundary;

import java.io.Serializable;

import matrix.DVec;

/**
 * Class for load (Neumann boundaries).
 * 
 * @author Murat
 * 
 */
public class NodalMechLoad implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of load. */
	public final static int global_ = 0, local_ = 1;

	/** The name of nodal mechanical load. */
	private String name_;

	/** The component vector of load. */
	private double[] components_;

	/** The coordinate system of load. */
	private int coordinateSystem_ = NodalMechLoad.global_;

	/** The boundary case of displacement load. */
	private BoundaryCase boundaryCase_;

	/** The value for scaling loading values. */
	private double scale_ = 1.0;

	/**
	 * Creates load.
	 * 
	 * @param name
	 *            The name of nodal mechanical load.
	 * @param boundaryCase
	 *            The boundary case of nodal mechanical load.
	 * @param components
	 *            The component vector of load. It has 6 components, first three
	 *            are forces, last three are moments.
	 */
	public NodalMechLoad(String name, BoundaryCase boundaryCase, DVec components) {

		// set name
		name_ = name;

		// set boundary case
		boundaryCase_ = boundaryCase;

		// set components
		if (components.rowCount() == 6)
			components_ = components.get1DArray();
		else
			exceptionHandler("Illegal dimension of load vector!");
	}

	/**
	 * Sets boundary case to nodal mechanical load.
	 * 
	 * @param boundaryCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase boundaryCase) {
		boundaryCase_ = boundaryCase;
	}

	/**
	 * Sets coordinate system of nodal mechanical load.
	 * 
	 * @param coordinateSystem
	 *            The nodal coordinate system to be set.
	 */
	public void setCoordinateSystem(int coordinateSystem) {
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal assignment for coordinate system!");
		else
			coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Sets scalinf factor for loading values.
	 * 
	 * @param scale
	 *            The scaling factor for loading values.
	 */
	public void setLoadingScale(double scale) {
		scale_ = scale;
	}

	/**
	 * Returns the name of nodal mechanical load.
	 * 
	 * @return The name of nodal mechanical load.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the boundary case of nodal mechanical load.
	 * 
	 * @return The boundary case of nodal mechanical load.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns the components vector of load.
	 * 
	 * @return The components vector of load.
	 */
	public DVec getComponents() {
		return new DVec(components_).scale(scale_);
	}

	/**
	 * Returns the coordinate system of load.
	 * 
	 * @return The coordinate system of load.
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
