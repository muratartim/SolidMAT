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
package section;

/**
 * Class for variable cross section.
 * 
 * @author Murat Artim
 * 
 */
public class VariableCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The properties of section. */
	private double[] a_, a2_, a3_, i2_, i3_, j_, r_;

	/**
	 * Creates variable cross section.
	 * 
	 * @param name
	 *            The name of section.
	 */
	public VariableCS(String name) {

		// set name
		name_ = name;

		// create arrays
		a_ = new double[2];
		a2_ = new double[2];
		a3_ = new double[2];
		i2_ = new double[2];
		i3_ = new double[2];
		j_ = new double[2];
		r_ = new double[2];
	}

	/**
	 * Sets cross section areas.
	 * 
	 * @param val1
	 *            Area of end section I.
	 * @param val2
	 *            Area of end section J.
	 */
	public void setArea(double val1, double val2) {

		// set values
		a_[0] = val1;
		a_[1] = val2;

		// check value
		checkProperties(a_);
	}

	/**
	 * Sets shear areas in x2.
	 * 
	 * @param val1
	 *            Shear areas in x2 of end section I.
	 * @param val2
	 *            Shear areas in x2 of end section J.
	 */
	public void setShearX2(double val1, double val2) {

		// set values
		a2_[0] = val1;
		a2_[1] = val2;

		// check value
		checkProperties(a2_);
	}

	/**
	 * Sets shear areas in x3.
	 * 
	 * @param val1
	 *            Shear areas in x3 of end section I.
	 * @param val2
	 *            Shear areas in x3 of end section J.
	 */
	public void setShearX3(double val1, double val2) {

		// set values
		a3_[0] = val1;
		a3_[1] = val2;

		// check value
		checkProperties(a3_);
	}

	/**
	 * Sets moment of inertias about x2.
	 * 
	 * @param val1
	 *            Moment of inertia about x2 of end section I.
	 * @param val2
	 *            Moment of inertia about x2 of end section J.
	 */
	public void setInertiaX2(double val1, double val2) {

		// set values
		i2_[0] = val1;
		i2_[1] = val2;

		// check value
		checkProperties(i2_);
	}

	/**
	 * Sets moment of inertias about x3.
	 * 
	 * @param val1
	 *            Moment of inertia about x3 of end section I.
	 * @param val2
	 *            Moment of inertia about x3 of end section J.
	 */
	public void setInertiaX3(double val1, double val2) {

		// set values
		i3_[0] = val1;
		i3_[1] = val2;

		// check value
		checkProperties(i3_);
	}

	/**
	 * Sets torsional constants.
	 * 
	 * @param val1
	 *            Torsional constant of end section I.
	 * @param val2
	 *            Torsional constant of end section J.
	 */
	public void setTorsionalConstant(double val1, double val2) {

		// set values
		j_[0] = val1;
		j_[1] = val2;

		// check value
		checkProperties(j_);
	}

	/**
	 * Sets warping constants.
	 * 
	 * @param val1
	 *            Warping constant of end section I.
	 * @param val2
	 *            Warping constant of end section J.
	 */
	public void setWarpingConstant(double val1, double val2) {

		// set values
		r_[0] = val1;
		r_[1] = val2;

		// check value
		checkProperties(r_);
	}

	@Override
	public int getType() {
		return Section.variable_;
	}

	@Override
	public double getArea(int i) {
		return a_[i];
	}

	@Override
	public double getShearAreaX2(int i) {
		return a2_[i];
	}

	@Override
	public double getShearAreaX3(int i) {
		return a3_[i];
	}

	@Override
	public double getInertiaX2(int i) {
		return i2_[i];
	}

	@Override
	public double getInertiaX3(int i) {
		return i3_[i];
	}

	@Override
	public double getTorsionalConstant(int i) {
		return j_[i];
	}

	@Override
	public double getWarpingConstant(int i) {
		return r_[i];
	}

	@Override
	public double[][] getOutline() {
		double area = 0.0;
		for (int i = 0; i < a_.length; i++)
			area += a_[i] / a_.length;
		double radius = Math.sqrt(area / Math.PI);
		double[][] outline = new double[64][2];
		for (int i = 0; i <= 63; i++) {
			outline[i][0] = radius * Math.cos(Math.PI + i * Math.PI / 32.0);
			outline[i][1] = radius * Math.sin(Math.PI + i * Math.PI / 32.0);
		}
		return outline;
	}

	@Override
	public double getDimension(int i) {
		return 0;
	}

	/**
	 * Checks for illegal assignments to properties of section, if not throws
	 * exception.
	 */
	private void checkProperties(double[] value) {

		// message to be displayed
		String message = "Illegal value for section!";

		// check if positive
		for (int i = 0; i < value.length; i++)
			if (value[i] <= 0)
				exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		return 0.0;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
