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
 * Class for thickness section.
 * 
 * @author Murat
 * 
 */
public class Thickness extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double t_;

	/**
	 * Creates thickness section for two dimensional elements.
	 * 
	 * @param name
	 *            The name of section.
	 * @param t
	 *            The thickness of section.
	 */
	public Thickness(String name, double t) {

		// set values
		name_ = name;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.thickness_;
	}

	@Override
	public double getArea(int i) {
		return 0;
	}

	@Override
	public double getShearAreaX2(int i) {
		return 0;
	}

	@Override
	public double getShearAreaX3(int i) {
		return 0;
	}

	@Override
	public double getInertiaX2(int i) {
		return 0;
	}

	@Override
	public double getInertiaX3(int i) {
		return 0;
	}

	@Override
	public double getTorsionalConstant(int i) {
		return 0;
	}

	@Override
	public double getWarpingConstant(int i) {
		return 0;
	}

	/**
	 * Returns the demanded dimension of the section.
	 * 
	 * @param i
	 *            Not used here.
	 */
	public double getDimension(int i) {
		return t_;
	}

	@Override
	public double[][] getOutline() {
		return null;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (t_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		return 0.0;
	}

	@Override
	public double getShearCenter() {
		return 0.0;
	}
}
