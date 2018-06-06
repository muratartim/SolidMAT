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
 * Class for circular segment cross section.
 * 
 * @author Murat
 * 
 */
public class CSegmentCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double r_, a_;

	/**
	 * Creates circular segment cross section.
	 * 
	 * @param r
	 *            The radius of section.
	 * @param a
	 *            The segment angle of section (in degrees).
	 */
	public CSegmentCS(String name, double r, double a) {

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
		return Section.cSegment_;
	}

	@Override
	public double getArea(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		if (a > Math.PI / 4)
			return r_ * r_ * (a - Math.sin(a) * Math.cos(a));
		else
			return 2.0 / 3.0 * r_ * r_ * a * a * a
					* (1.0 - 0.2 * a * a + 0.019 * Math.pow(a, 4.0));
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
		if (a > Math.PI / 4)
			return Math.pow(r_, 4.0)
					/ 4.0
					* (a - Math.sin(a) * Math.cos(a) + 2.0
							* Math.pow(Math.sin(a), 3.0) * Math.cos(a) - 16.0
							* Math.pow(Math.sin(a), 6.0)
							/ (9.0 * (a - Math.sin(a) * Math.cos(a))));
		else
			return 0.01143 * Math.pow(r_, 4.0) * Math.pow(a, 7.0)
					* (1.0 - 0.3491 * a * a + 0.0450 * Math.pow(a, 4.0));
	}

	@Override
	public double getInertiaX3(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		if (a > Math.PI / 4)
			return Math.pow(r_, 4.0)
					/ 12.0
					* (3.0 * a - 3.0 * Math.sin(a) * Math.cos(a) - 2.0
							* Math.pow(Math.sin(a), 3.0) * Math.cos(a));
		else
			return 0.1333 * Math.pow(r_, 4.0) * Math.pow(a, 5.0)
					* (1.0 - 0.4762 * a * a + 0.1111 * Math.pow(a, 4.0));
	}

	@Override
	public double getTorsionalConstant(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// compute
		double h = r_ * (1.0 - Math.cos(a));
		double c = 0.7854 - 0.0333 * h / r_ - 2.6183 * Math.pow(h / r_, 2.0)
				+ 4.1595 * Math.pow(h / r_, 3.0) - 3.0769
				* Math.pow(h / r_, 4.0) + 0.9299 * Math.pow(h / r_, 5.0);
		return 2.0 * c * Math.pow(r_, 4.0);
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

		// convert angle to radians
		double a = Math.toRadians(a_);

		double[][] outline = new double[33][2];
		for (int i = 0; i <= 31; i++) {
			outline[i][0] = r_ * Math.cos(Math.PI / 2 - a + i * 2.0 * a / 32.0);
			outline[i][1] = r_ * Math.sin(Math.PI / 2 - a + i * 2.0 * a / 32.0);
		}
		outline[32][0] = outline[0][0];
		outline[32][1] = outline[0][1];
		double d = r_ - 2.0 / 3.0 * (r_ - outline[0][1]);
		for (int i = 0; i < 33; i++)
			outline[i][1] = outline[i][1] - d;
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
		if (r_ <= 0.0)
			exceptionHandler(message);

		// convert angle to radians
		double a = Math.toRadians(a_);

		// check
		if (a > Math.PI / 2 || a < 0 || a == Math.PI / 4)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {

		// convert angle to radians
		double a = Math.toRadians(a_);

		// a > pi/4
		if (a > Math.PI / 4) {

			// x2 coordinate
			if (i == 0)
				return r_ * Math.sin(a);

			// x3 coordinate
			else
				return r_
						* (1.0 - 2.0 * Math.pow(Math.sin(a), 3.0)
								/ (3.0 * (a - Math.sin(a) * Math.cos(a))));
		}

		// a < pi/4
		else {

			// x2 coordinate
			if (i == 0)
				return r_ * a * (1.0 - 0.1667 * a * a + 0.0083 * a * a * a * a);

			// x3 coordinate
			else
				return 0.3 * r_ * a * a
						* (1.0 - 0.0976 * a * a + 0.0028 * a * a * a * a);
		}
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
