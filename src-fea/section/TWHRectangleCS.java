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
 * Class for tube cross section.
 * 
 * @author Murat
 * 
 */
public class TWHRectangleCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, t_, t1_;

	/**
	 * Creates tube cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            Outside depth (height) of section.
	 * @param b
	 *            Outside width of section.
	 * @param t
	 *            Flange thickness of section.
	 * @param t1
	 *            Web thickness of section.
	 */
	public TWHRectangleCS(String name, double a, double b, double t, double t1) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		t_ = t;
		t1_ = t1;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.tWHRectangle_;
	}

	@Override
	public double getArea(int i) {
		return a_ * b_ - (a_ - 2.0 * t_) * (b_ - 2.0 * t1_);
	}

	@Override
	public double getShearAreaX2(int i) {
		return 2.0 * t_ * b_;
	}

	@Override
	public double getShearAreaX3(int i) {
		return 2.0 * t1_ * a_;
	}

	@Override
	public double getInertiaX2(int i) {
		return (b_ * a_ * a_ * a_ - (b_ - 2.0 * t1_)
				* Math.pow(a_ - 2.0 * t_, 3.0)) / 12.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return (a_ * b_ * b_ * b_ - (a_ - 2.0 * t_)
				* Math.pow(b_ - 2.0 * t1_, 3.0)) / 12.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return (2.0 * t_ * t1_ * Math.pow(a_ - t_, 2.0) * Math.pow(b_ - t1_,
				2.0))
				/ (a_ * t_ + b_ * t1_ - t_ * t_ - t1_ * t1_);
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b , 2 -> t, 3 -> t1.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return b_;
		else if (i == 2)
			return t_;
		else
			return t1_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[4][2];
		outline[0][0] = -b_ / 2.0;
		outline[0][1] = -a_ / 2.0;
		outline[1][0] = b_ / 2.0;
		outline[1][1] = -a_ / 2.0;
		outline[2][0] = b_ / 2.0;
		outline[2][1] = a_ / 2.0;
		outline[3][0] = -b_ / 2.0;
		outline[3][1] = a_ / 2.0;
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
		if (a_ <= 0 || b_ <= 0 || t_ <= 0 || t1_ <= 0)
			exceptionHandler(message);

		// check for other constraints
		if (t_ >= a_ / 2.0)
			exceptionHandler(message);
		if (t1_ >= b_ / 2.0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_ * 0.5;
		else
			return a_ * 0.5;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
