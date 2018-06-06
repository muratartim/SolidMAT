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
 * Class for element spring.
 * 
 * @author Murat
 * 
 */
public class ElementSpring implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the component of spring. */
	public static final int ux_ = 0, uy_ = 1, uz_ = 2, rx_ = 3, ry_ = 4,
			rz_ = 5;

	/** Static variable for the coordinate system of spring. */
	public static final int global_ = 0, local_ = 1;

	/** The component of spring. */
	private int component_;

	/** Coordinate system of spring. */
	private int coordinateSystem_;

	/** Value of spring. */
	private double value_;

	/** The name of element spring. */
	private String name_;

	/**
	 * Creates element spring.
	 * 
	 * @param name
	 *            Name of element spring.
	 * @param component
	 *            The component of element spring.
	 * @param coordinateSystem
	 *            The coordinate system of element spring.
	 * @param value
	 *            The stiffness value of element spring.
	 */
	public ElementSpring(String name, int component, int coordinateSystem,
			double value) {

		// set name
		name_ = name;

		// set component
		if (component < 0 || component > 5)
			exceptionHandler("Illegal component for element spring!");
		else
			component_ = component;

		// set coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for element spring!");
		else
			coordinateSystem_ = coordinateSystem;

		// set value
		if (value < 0)
			exceptionHandler("Illegal stiffness for element spring!");
		else
			value_ = value;
	}

	/**
	 * Returns name of the element spring.
	 * 
	 * @return Name of element spring.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the stiffness value of spring.
	 * 
	 * @return The stiffness value of spring.
	 */
	public double getValue() {
		return value_;
	}

	/**
	 * Returns the component of spring.
	 * 
	 * @return The component of spring.
	 */
	public int getComponent() {
		return component_;
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
