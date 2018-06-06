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
 * Class for sector of a thin walled hollow circle cross section.
 * 
 * @author Murat
 * 
 */
public class TWHCSectorCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double r_, a_, t_;

	/**
	 * Creates sector of a thin walled hollow circle cross section.
	 * 
	 * @param name
	 *            Name of section.
	 * @param r
	 *            The radius of section.
	 * @param a
	 *            The segment angle of section (in degrees).
	 * @param t
	 *            The thickness of section.
	 */
	public TWHCSectorCS(String name, double r, double a, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		r_ = r;
		a_ = a;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.hCSector_;
	}

	@Override
	public double getArea(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return a * t_ * (2.0 * r_ - t_);
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
		return r_
				* r_
				* r_
				* t_
				* ((1.0 - 3.0 * t_ / (2.0 * r_) + t_ * t_ / (r_ * r_) - t_ * t_
						* t_ / (4.0 * r_ * r_ * r_))
						* (a + Math.sin(a) * Math.cos(a) - 2.0
								* Math.pow(Math.sin(a), 2.0) / a) + t_ * t_
						* Math.pow(Math.sin(a), 2.0)
						/ (3.0 * r_ * r_ * a * (2.0 - t_ / r_))
						* (1.0 - t_ / r_ + t_ * t_ / (6.0 * r_ * r_)));
	}

	@Override
	public double getInertiaX3(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return r_
				* r_
				* r_
				* t_
				* (1.0 - 3.0 * t_ / (2.0 * r_) + t_ * t_ / (r_ * r_) - t_ * t_
						* t_ / (4.0 * r_ * r_ * r_))
				* (a - Math.sin(a) * Math.cos(a));
	}

	@Override
	public double getTorsionalConstant(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return 2.0 / 3.0 * t_ * t_ * t_ * r_ * a;
	}

	@Override
	public double getWarpingConstant(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return 2.0
				* t_
				* Math.pow(r_, 5.0)
				/ 3.0
				* (a * a * a - 6.0
						* Math.pow(Math.sin(a) - a * Math.cos(a), 2.0)
						/ (a - Math.sin(a) * Math.cos(a)));
	}

	@Override
	public double[][] getOutline() {
		// TODO Computation codes...
		return null;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> r, 1 -> a, 2 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return r_;
		else if (i == 1)
			return a_;
		else
			return t_;
	}

	@Override
	public double getCentroid(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		if (i == 0)
			return r_ * Math.sin(a);
		else
			return r_
					* (1.0 - 2.0 * Math.sin(a) / (3.0 * a)
							* (1.0 - t_ / r_ + 1.0 / (2.0 - t_ / r_)));
	}

	@Override
	public double getShearCenter() {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		return 2.0 * r_ * (Math.sin(a) - a * Math.cos(a))
				/ (a - Math.sin(a) * Math.cos(a));
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (r_ <= 0.0 || t_ <= 0.0)
			exceptionHandler(message);
	}
}
