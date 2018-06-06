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
 * Class for L cross section.
 * 
 * @author Murat
 * 
 */
public class LCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, c_, r_;

	/**
	 * Creates L cross sections.
	 * 
	 * @param name
	 *            Name of section.
	 * @param a
	 *            Height of section.
	 * @param b
	 *            Thickness of flange.
	 * @param c
	 *            Length of flange.
	 * @param r
	 *            Radius of flange corner.
	 */
	public LCS(String name, double a, double b, double c, double r) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		c_ = c;
		r_ = r;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.l_;
	}

	@Override
	public double getArea(int i) {
		return b_ * (a_ + c_);
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
		return b_
				* (Math.pow(a_, 4.0) + 2.0 * a_
						* (2.0 * a_ * a_ - 3.0 * a_ * b_ + 2.0 * b_ * b_) * c_ + b_
						* b_ * c_ * c_) / (12.0 * (a_ + c_));
	}

	@Override
	public double getInertiaX3(int i) {
		return b_
				* (a_ * a_ * b_ * b_ + Math.pow(c_, 4.0) + 2.0 * a_ * c_
						* (2.0 * b_ * b_ + 3.0 * b_ * c_ + 2.0 * c_ * c_))
				/ (12.0 * (a_ + c_));
	}

	@Override
	public double getTorsionalConstant(int i) {
		return a_
				* b_
				* b_
				* b_
				* (1.0 / 3.0 - 0.21 * b_ / a_
						* (1.0 - 1.0 / 12.0 * Math.pow(b_ / a_, 4.0)))
				+ c_
				* b_
				* b_
				* b_
				* (1.0 / 3.0 - 0.105 * b_ / c_
						* (1.0 - 1.0 / 192.0 * Math.pow(b_ / c_, 4.0)))
				+ (0.07 + 0.076 * r_ / b_)
				* Math.pow(4.0 * b_ + 6.0 * r_ - 2.8284 * (2.0 * r_ + b_), 4.0);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b, 2 -> c, 3 -> r.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return c_;
		else
			return r_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[6][2];
		outline[0][0] = -b_ / 2.0;
		outline[0][1] = -a_ / 2.0;
		outline[1][0] = c_ + b_ / 2.0;
		outline[1][1] = -a_ / 2.0;
		outline[2][0] = c_ + b_ / 2.0;
		outline[2][1] = -a_ / 2.0 + b_;
		outline[3][0] = b_ / 2.0;
		outline[3][1] = -a_ / 2.0 + b_;
		outline[4][0] = b_ / 2.0;
		outline[4][1] = a_ / 2.0;
		outline[5][0] = -b_ / 2.0;
		outline[5][1] = a_ / 2.0;
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
		if (a_ <= 0 || b_ <= 0 || c_ <= 0 || r_ < 0)
			exceptionHandler(message);

		// check for other constraints
		if (b_ >= a_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return (c_ * c_ + b_ * (a_ + 2.0 * c_)) / (2.0 * (a_ + c_));
		else
			return (a_ * a_ + c_ * b_) / (2.0 * (a_ + c_));
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
