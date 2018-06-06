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
 * Class for z cross section.
 * 
 * @author Murat
 * 
 */
public class ZCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, t_, r_;

	/**
	 * Creates z cross section.
	 * 
	 * @param name
	 *            Name of section.
	 * @param a
	 *            Length of flange.
	 * @param b
	 *            Height of section.
	 * @param t
	 *            Thickness.
	 * @param r
	 *            Radius of flange corner.
	 */
	public ZCS(String name, double a, double b, double t, double r) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		t_ = t;
		r_ = r;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.z_;
	}

	@Override
	public double getArea(int i) {
		return t_ * (b_ + 2.0 * a_);
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
		return (b_ * b_ * b_ * (a_ + t_) - a_ * Math.pow(b_ - 2.0 * t_, 3.0)) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return (b_ * Math.pow(2.0 * a_ + t_, 3.0) - 2.0 * a_ * a_ * a_
				* (b_ - t_) - 6.0 * a_ * Math.pow(a_ + t_, 2.0) * (b_ - t_)) / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return t_
				* t_
				* t_
				* (0.333 * b_ - 0.42 * t_ + 0.56 * Math.pow(t_, 5.0)
						/ Math.pow(b_, 4.0))
				+ a_
				* t_
				* t_
				* t_
				* (2.0 / 3.0 - 0.21 * t_ / a_
						* (1.0 - 1.0 / 192.0 * Math.pow(t_ / a_, 4.0)))
				+ (0.14 + 0.152 * r_ / t_)
				* Math.pow(4.0 * r_ + 6.0 * r_ - 2.8284 * (2.0 * r_ + t_), 4.0);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b, 2 -> t, 3 -> r.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return t_;
		else
			return r_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[8][2];
		outline[0][0] = -t_ / 2.0;
		outline[0][1] = -b_ / 2.0;
		outline[1][0] = a_ + t_ / 2.0;
		outline[1][1] = -b_ / 2.0;
		outline[2][0] = a_ + t_ / 2.0;
		outline[2][1] = -b_ / 2.0 + t_;
		outline[3][0] = t_ / 2.0;
		outline[3][1] = -b_ / 2.0 + t_;
		outline[4][0] = t_ / 2.0;
		outline[4][1] = b_ / 2.0;
		outline[5][0] = -a_ - t_ / 2.0;
		outline[5][1] = b_ / 2.0;
		outline[6][0] = -a_ - t_ / 2.0;
		outline[6][1] = b_ / 2.0 - t_;
		outline[7][0] = -t_ / 2.0;
		outline[7][1] = b_ / 2.0 - t_;
		return outline;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (a_ <= 0 || b_ <= 0 || t_ <= 0 || r_ < 0)
			exceptionHandler(message);

		// check for other constraints
		if (t_ >= b_ / 2.0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return 0.0;
		else
			return b_ * 0.5;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
