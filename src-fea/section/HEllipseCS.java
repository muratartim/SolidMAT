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
 * Class for hollow ellipse cross section.
 * 
 * @author Murat
 * 
 */
public class HEllipseCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double a_, b_, ao_, bo_;

	/**
	 * Creates hollow ellipse cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param a
	 *            The long outside radius of section.
	 * @param ao
	 *            The long inside radius of section.
	 * @param b
	 *            The short outside radius of section.
	 * @param bo
	 *            The short inside radius of section.
	 */
	public HEllipseCS(String name, double a, double ao, double b, double bo) {

		// set name
		name_ = name;

		// set geometrical properties
		a_ = a;
		b_ = b;
		ao_ = ao;
		bo_ = bo;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.hEllipse_;
	}

	@Override
	public double getArea(int i) {
		return Math.PI * (a_ * b_ - ao_ * bo_);
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
		return Math.PI / 4.0 * (b_ * a_ * a_ * a_ - bo_ * ao_ * ao_ * ao_);
	}

	@Override
	public double getInertiaX3(int i) {
		return Math.PI / 4.0 * (a_ * b_ * b_ * b_ - ao_ * bo_ * bo_ * bo_);
	}

	@Override
	public double getTorsionalConstant(int i) {
		return Math.PI * a_ * a_ * a_ * b_ * b_ * b_ / (a_ * a_ + b_ * b_)
				* (1.0 - Math.pow(ao_ / a_, 4.0));
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0.0;
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> a, 1 -> ao, 2 -> b, 3 -> bo.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return a_;
		else if (i == 1)
			return ao_;
		else if (i == 2)
			return b_;
		else
			return bo_;
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
		if (a_ <= 0 || b_ <= 0 || ao_ <= 0 || bo_ <= 0)
			exceptionHandler(message);

		// check constraints
		if (ao_ >= a_ || bo_ >= b_ || b_ >= a_ || bo_ >= ao_)
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
