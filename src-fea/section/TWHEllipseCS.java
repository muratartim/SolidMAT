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
 * Class for thin walled hollow ellipse cross section.
 * 
 * @author Murat
 * 
 */
public class TWHEllipseCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, t_;

	/**
	 * Creates thin walled hollow ellipse cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            The long radius of section.
	 * @param b
	 *            The short radius of section.
	 * @param t
	 *            The thickness of section.
	 */
	public TWHEllipseCS(String name, double a, double b, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.tWHEllipse_;
	}

	@Override
	public double getArea(int i) {
		double k = 0.2464 + 0.002222 * (a_ / b_ + b_ / a_);
		return Math.PI * t_ * (a_ + b_)
				* (1.0 + k * Math.pow((a_ - b_) / (a_ + b_), 2.0));
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
		double k1 = 0.1349 + 0.1279 * a_ / b_ - 0.01284 * a_ * a_ / (b_ * b_);
		double k2 = 0.1349 + 0.1279 * b_ / a_ - 0.01284 * b_ * b_ / (a_ * a_);
		return Math.PI / 4.0 * t_ * a_ * a_ * (a_ + 3.0 * b_)
				* (1.0 + k1 * Math.pow((a_ - b_) / (a_ + b_), 2.0)) + Math.PI
				/ 16.0 * t_ * t_ * t_ * (3.0 * a_ + b_)
				* (1.0 + k2 * Math.pow((a_ - b_) / (a_ + b_), 2.0));
	}

	@Override
	public double getInertiaX3(int i) {
		double k1 = 0.1349 + 0.1279 * b_ / a_ - 0.01284 * b_ * b_ / (a_ * a_);
		double k2 = 0.1349 + 0.1279 * a_ / b_ - 0.01284 * a_ * a_ / (b_ * b_);
		return Math.PI / 4.0 * t_ * b_ * b_ * (b_ + 3.0 * a_)
				* (1.0 + k1 * Math.pow((b_ - a_) / (b_ + a_), 2.0)) + Math.PI
				/ 16.0 * t_ * t_ * t_ * (3.0 * b_ + a_)
				* (1.0 + k2 * Math.pow((b_ - a_) / (b_ + a_), 2.0));
	}

	@Override
	public double getTorsionalConstant(int i) {
		double u = Math.PI
				* (a_ + b_ - t_)
				* (1.0 + 0.258 * Math.pow(a_ - b_, 2.0)
						/ Math.pow(a_ + b_ - t_, 2.0));
		return 4.0 * Math.pow(Math.PI, 2.0) * t_
				* (Math.pow(b_ - 0.5 * t_, 2.0) * Math.pow(b_ - 0.5 * t_, 2.0))
				/ u;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> b, 2 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return b_;
		else
			return t_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[64][2];
		for (int i = 0; i <= 63; i++) {
			outline[i][0] = b_ * Math.cos(Math.PI + i * Math.PI / 32.0);
			outline[i][1] = a_ * Math.sin(Math.PI + i * Math.PI / 32.0);
		}
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
		if (a_ <= 0 || b_ <= 0 || t_ <= 0)
			exceptionHandler(message);

		// check constraints
		if (b_ >= a_ || t_ >= b_)
			exceptionHandler(message);
		if (0.2 >= a_ / b_ || a_ / b_ >= 5.0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_ + t_ * 0.5;
		else
			return a_ + 0.5 * t_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
