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
 * Class for i cross section.
 * 
 * @author Murat
 * 
 */
public class ICS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, c_, d_, r_;

	/**
	 * Creates i cross sections.
	 * 
	 * @param name
	 *            Name of section.
	 * @param a
	 *            Length of flange.
	 * @param b
	 *            Thickness of flange.
	 * @param c
	 *            Height of web.
	 * @param d
	 *            Thickness of web.
	 * @param r
	 *            Radius of flange corner.
	 */
	public ICS(String name, double a, double b, double c, double d, double r) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		c_ = c;
		d_ = d;
		r_ = r;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.i_;
	}

	@Override
	public double getArea(int i) {
		return 2.0 * a_ * b_ + d_ * c_;
	}

	@Override
	public double getShearAreaX2(int i) {
		return 5.0 / 3.0 * b_ * a_;
	}

	@Override
	public double getShearAreaX3(int i) {
		return d_ * (c_ + b_);
	}

	@Override
	public double getInertiaX2(int i) {
		return a_ * Math.pow(c_ + 2.0 * b_, 3.0) / 12.0 - (a_ - d_) * c_ * c_
				* c_ / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return a_ * a_ * a_ * b_ / 6.0 + d_ * d_ * d_ * c_ / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		double t = b_;
		double t1 = d_;
		if (b_ > d_) {
			t = d_;
			t1 = b_;
		}
		return 2.0
				* a_
				* b_
				* b_
				* b_
				* (1.0 / 3.0 - 0.21 * b_ / a_
						* (1.0 - 1.0 / 12.0 * Math.pow(b_ / a_, 4.0)))
				+ 1.0
				/ 3.0
				* c_
				* d_
				* d_
				* d_
				+ 2.0
				* t
				/ t1
				* (0.15 + 0.10 * r_ / b_)
				* Math.pow((Math.pow(b_ + r_, 2.0) + r_ * d_ + d_ * d_ / 4.0)
						/ (2.0 * r_ + b_), 4.0);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b, 2 -> c, 3 -> d, 4 -> r.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return c_;
		else if (i == 3)
			return d_;
		else
			return r_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[12][2];
		outline[0][0] = -a_ / 2.0;
		outline[0][1] = -(c_ + 2.0 * b_) / 2.0;
		outline[1][0] = a_ / 2.0;
		outline[1][1] = -(c_ + 2.0 * b_) / 2.0;
		outline[2][0] = a_ / 2.0;
		outline[2][1] = -(c_ + 2.0 * b_) / 2.0 + b_;
		outline[3][0] = d_ / 2.0;
		outline[3][1] = -c_ / 2.0;
		outline[4][0] = d_ / 2.0;
		outline[4][1] = c_ / 2.0;
		outline[5][0] = a_ / 2.0;
		outline[5][1] = c_ / 2.0;
		outline[6][0] = a_ / 2.0;
		outline[6][1] = c_ / 2.0 + b_;
		outline[7][0] = -a_ / 2.0;
		outline[7][1] = c_ / 2.0 + b_;
		outline[8][0] = -a_ / 2.0;
		outline[8][1] = c_ / 2.0;
		outline[9][0] = -d_ / 2.0;
		outline[9][1] = c_ / 2.0;
		outline[10][0] = -d_ / 2.0;
		outline[10][1] = -c_ / 2.0;
		outline[11][0] = -a_ / 2.0;
		outline[11][1] = -c_ / 2.0 + b_;
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
		if (a_ <= 0 || b_ <= 0 || c_ <= 0 || d_ <= 0 || r_ < 0)
			exceptionHandler(message);

		// check for other constraints
		if (d_ >= 2.0 * (b_ + r_) || d_ >= a_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return a_ / 2.0;
		else
			return c_ * 0.5 + b_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
