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
 * Class for circular sector cross section.
 * 
 * @author Murat
 * 
 */
public class CSectorCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double r_, a_;

	/**
	 * Creates circular sector cross section.
	 * 
	 * @param r
	 *            The radius of section.
	 * @param a
	 *            The segment angle of section (in degrees).
	 */
	public CSectorCS(String name, double r, double a) {

		// set name
		name_ = name;

		// set geometrical properties
		r_ = r;
		a_ = a;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.cSector_;
	}

	@Override
	public double getArea(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return a * r_ * r_;
	}

	@Override
	public double getShearAreaX2(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getShearAreaX3(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getInertiaX2(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return Math.pow(r_, 4.0)
				/ 4.0
				* (a + Math.sin(a) * Math.cos(a) - 16.0
						* Math.pow(Math.sin(a), 2.0) / (9.0 * a));
	}

	@Override
	public double getInertiaX3(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return Math.pow(r_, 4.0) / 4.0 * (a - Math.sin(a) * Math.cos(a));
	}

	@Override
	public double getTorsionalConstant(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		double c = 0.0034 - 0.1394 * a / Math.PI + 2.33
				* Math.pow(a / Math.PI, 2.0) - 2.36
				* Math.pow(a / Math.PI, 3.0) + 1.3984
				* Math.pow(a / Math.PI, 4.0) - 0.3552
				* Math.pow(a / Math.PI, 5.0);
		return c * Math.pow(r_, 4.0);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> r, 1 -> a.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return r_;
		else
			return a_;
	}

	@Override
	public double[][] getOutline() {
		// TODO Computation codes...
		return null;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (r_ <= 0.0)
			exceptionHandler(message);

		// convert angle to radians
		double a = Math.toRadians(a_);

		// check
		if (a / Math.PI > 1.0 || a / Math.PI < 0.05)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		if (i == 0)
			return r_ * Math.sin(a);
		else
			return r_ * (1.0 - 2.0 * Math.sin(a) / (3.0 * a));
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
