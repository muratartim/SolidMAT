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

/**
 * Class for Local axis systems of nodes and one dimensional elements.
 * 
 * @author Murat
 * 
 */
public class LocalAxis implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of local axis. */
	public final static int point_ = 0, line_ = 1;

	/** The name of local axis. */
	private String name_;

	/** The type of local axis. */
	private int type_;

	/** The values of local axis (point -> length = 3, line -> length = 1). */
	private double[] values_;

	/**
	 * Creates LocalAxis object.
	 * 
	 * @param name
	 *            Name of local axis.
	 * @param type
	 *            The type of local axis.
	 * @param values
	 *            Rotation angles of local axis (point -> length = 3, line ->
	 *            length = 1). For point type, the angle sequence is rotations
	 *            about x, y' and z'' in degrees, respectively. For line type,
	 *            the axial rotation in degrees.
	 */
	public LocalAxis(String name, int type, double[] values) {

		// set name
		name_ = name;

		// set type
		if (type < 0 || type > 1)
			exceptionHandler("Illegal type for local axis!");
		type_ = type;

		// set values
		if (type == LocalAxis.point_ && values.length != 3)
			exceptionHandler("Illegal dimension of local axis array!");
		else if (type == LocalAxis.line_ && values.length != 1)
			exceptionHandler("Illegal dimension of local axis array!");
		values_ = values;
	}

	/**
	 * Returns name of the local axis.
	 * 
	 * @return Name of local axis.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns type of the local axis.
	 * 
	 * @return Type of local axis.
	 */
	public int getType() {
		return type_;
	}

	/**
	 * Returns rotation angles of the local axis.
	 * 
	 * @return Rotation angles of the local axis (point -> length = 3, line ->
	 *         length = 1).
	 */
	public double[] getValues() {
		return values_;
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
