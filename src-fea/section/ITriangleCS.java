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
 * Class for isosceles triangle cross section.
 * 
 * @author Murat
 * 
 */
public class ITriangleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_;

	/**
	 * Creates isosceles triangle cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            The bottom side length of section.
	 * @param b
	 *            The height of section.
	 */
	public ITriangleCS(String name, double a, double b) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.iTriangle_;
	}

	@Override
	public double getArea(int i) {
		return 0.5 * a_ * b_;
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
		return a_ * Math.pow(b_, 3.0) / 36.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return b_ * Math.pow(a_, 3.0) / 48.0;
	}

	@Override
	public double getTorsionalConstant(int i) {

		// condition -1
		if (2.0 / 3.0 < a_ / b_ && a_ / b_ < Math.sqrt(3.0))
			return a_ * a_ * a_ * b_ * b_ * b_
					/ (15.0 * a_ * a_ + 20.0 * b_ * b_);

		// condition -2
		else
			return 0.0915 * Math.pow(b_, 4.0) * (a_ / b_ - 0.8592);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else
			return b_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[3][2];
		outline[0][0] = -a_ / 2.0;
		outline[0][1] = -b_ / 3.0;
		outline[1][0] = a_ / 2.0;
		outline[1][1] = -b_ / 3.0;
		outline[2][0] = 0.0;
		outline[2][1] = 2.0 * b_ / 3.0;
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
		if (a_ <= 0)
			exceptionHandler(message);
		if (b_ <= 0)
			exceptionHandler(message);

		// check constraints
		if (2.0 / 3.0 >= a_ / b_ || a_ / b_ >= 2.0 * Math.sqrt(3.0))
			exceptionHandler(message);
		if (Math.sqrt(3.0) == a_ / b_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return 0.5 * a_;
		else
			return 2.0 / 3.0 * b_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
