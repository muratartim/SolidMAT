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
package element;

import java.io.Serializable;

/**
 * Class for additional element mass.
 * 
 * @author Murat
 * 
 */
public class ElementMass implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the component of mass. */
	public static final int ux_ = 0, uy_ = 1, uz_ = 2, rx_ = 3, ry_ = 4,
			rz_ = 5;

	/** Static variable for the coordinate system of mass. */
	public static final int global_ = 0, local_ = 1;

	/** The component of mass. */
	private int component_;

	/** Coordinate system of mass. */
	private int coordinateSystem_;

	/** Value of mass. */
	private double value_;

	/** The name of element mass. */
	private String name_;

	/**
	 * Creates element mass.
	 * 
	 * @param name
	 *            Name of element mass.
	 * @param component
	 *            The component of element mass.
	 * @param coordinateSystem
	 *            The coordinate system of element mass.
	 * @param value
	 *            The stiffness value of element mass.
	 */
	public ElementMass(String name, int component, int coordinateSystem,
			double value) {

		// set name
		name_ = name;

		// set component
		if (component < 0 || component > 5)
			exceptionHandler("Illegal component for element mass!");
		else
			component_ = component;

		// set coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for element mass!");
		else
			coordinateSystem_ = coordinateSystem;

		// set value
		if (value < 0)
			exceptionHandler("Illegal value for element mass!");
		else
			value_ = value;
	}

	/**
	 * Returns name of the element mass.
	 * 
	 * @return Name of element mass.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the stiffness value of mass.
	 * 
	 * @return The stiffness value of mass.
	 */
	public double getValue() {
		return value_;
	}

	/**
	 * Returns the component of mass.
	 * 
	 * @return The component of mass.
	 */
	public int getComponent() {
		return component_;
	}

	/**
	 * Returns the coordinate system of mass.
	 * 
	 * @return The coordinate system of mass.
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
