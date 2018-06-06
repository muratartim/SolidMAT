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
package material;

import java.io.Serializable;

import matrix.DMat;
import matrix.DVec;

/**
 * Class for material.
 * 
 * @author Murat
 * 
 */
public abstract class Material implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of material. */
	public final static int isotropic_ = 0, orthotropic_ = 1;

	/** Static variable for the type of coefficient matrix. */
	public final static int threeD_ = 0, planeStress_ = 1, planeStrain_ = 2;

	/** Mass per unit volume of material. */
	private double d_ = 1.0;

	/** Weight per unit volume of material. */
	private double dg_ = 1.0;

	private String name_;

	/**
	 * Sets name to material.
	 * 
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets mass per unit volume to material.
	 * 
	 * @param d
	 *            Mass per unit volume.
	 */
	public void setVolumeMass(double d) {
		if (d >= 0)
			d_ = d;
		else
			exceptionHandler("Illegal assignment for mass per unit volume!");
	}

	/**
	 * Sets weight per unit volume to material.
	 * 
	 * @param dg
	 *            Weight per unit volume.
	 */
	public void setVolumeWeight(double dg) {
		if (dg >= 0)
			dg_ = dg;
		else
			exceptionHandler("Illegal assignment for weight per unit volume!");
	}

	/**
	 * Returns the name of material.
	 * 
	 * @return The name of material.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns mass per unit volume of material.
	 * 
	 * @return Mass per unit volume of material.
	 */
	public double getVolumeMass() {
		return d_;
	}

	/**
	 * Returns weight per unit volume of material.
	 * 
	 * @return Weight per unit volume of material.
	 */
	public double getVolumeWeight() {
		return dg_;
	}

	/** Returns the type of material. */
	public abstract int getType();

	/**
	 * Returns the stiffness matrix (C).
	 * 
	 * @param type
	 *            The state (type) of stiffness matrix.
	 * @return The stiffness matrix.
	 */
	public abstract DMat getC(int type);

	/**
	 * Returns the compliance matrix (S = C^-1).
	 * 
	 * @param type
	 *            The state (type) of compliance matrix.
	 * @return The compliance matrix.
	 */
	public abstract DMat getS(int type);

	/**
	 * Returns the thermal influence vector of material.
	 * 
	 * @param type
	 *            The state (type) of influence vector.
	 * @return The thermal influence vector of material.
	 */
	public abstract DVec getAlpha(int type);

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
