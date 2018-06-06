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
 * Class for ellipse cross section.
 * 
 * @author Murat
 * 
 */
public class EllipseCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_;

	/**
	 * Creates circular cross section.
	 * 
	 * @param a
	 *            The long radius of section.
	 * @param b
	 *            The short radius of section.
	 */
	public EllipseCS(String name, double a, double b) {

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
		return Section.ellipse_;
	}

	@Override
	public double getArea(int i) {
		return Math.PI * a_ * b_;
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
		return b_ * Math.pow(a_, 3.0) / 4.0;
	}

	@Override
	public double getInertiaX3(int i) {
		return a_ * Math.pow(b_, 3.0) / 4.0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.PI * Math.pow(a_, 3.0) * Math.pow(b_, 3.0)
				/ (a_ * a_ + b_ * b_);
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
		if (a_ <= 0 || b_ <= 0)
			exceptionHandler(message);

		// check relative
		if (a_ <= b_)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_;
		else
			return a_;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
