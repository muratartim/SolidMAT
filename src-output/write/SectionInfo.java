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
package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import section.Section;

import analysis.Structure;

import element.Element;
import element.Element1D;
import element.Element2D;
import element.ElementLibrary;

/**
 * Class for writing section information to output file.
 * 
 * @author Murat
 * 
 */
public class SectionInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/** Vector for storing one dimensional elements. */
	private Vector<Element1D> element1D_ = new Vector<Element1D>();

	/** Vector for storing two dimensional elements. */
	private Vector<Element2D> element2D_ = new Vector<Element2D>();

	/** Vector for storing ids of one dimensional elements. */
	private Vector<Integer> element1DId_ = new Vector<Integer>();

	/** Vector for storing ids of two dimensional elements. */
	private Vector<Integer> element2DId_ = new Vector<Integer>();

	/**
	 * Writes section information to output file.
	 * 
	 * @param structure
	 *            The structure to be printed.
	 * @param output
	 *            The output file.
	 */
	protected void write(Structure structure, File output) {

		// store elements
		for (int i = 0; i < structure.getNumberOfElements(); i++) {

			// get element
			Element e = structure.getElement(i);

			// store one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {
				element1D_.add((Element1D) e);
				element1DId_.add(i);
			}

			// store two dimensional elements
			else if (e.getDimension() == ElementLibrary.twoDimensional_) {
				element2D_.add((Element2D) e);
				element2DId_.add(i);
			}
		}

		try {

			// create file writer with appending option
			FileWriter writer = new FileWriter(output, true);

			// create buffered writer
			bwriter_ = new BufferedWriter(writer);

			// write tables for one dimensional elements
			if (element1D_.size() != 0) {
				writeTable1();
				writeTable2();
			}

			// write tables for two dimensional elements
			if (element2D_.size() != 0)
				writeTable3();

			// close writer
			bwriter_.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for one dimensional elements part 1.
	 * 
	 */
	private void writeTable1() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Sections (One dimensional elements), 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "A", "TC", "I2" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write section info
			for (int i = 0; i < element1D_.size(); i++) {

				// get section
				Section s = element1D_.get(i).getSection();

				// get general properties
				table[0] = Integer.toString(element1DId_.get(i));
				table[1] = s.getName();
				table[2] = formatter(s.getArea(0));
				table[3] = formatter(s.getTorsionalConstant(0));
				table[4] = formatter(s.getInertiaX2(0));

				// write
				bwriter_.write(table(table));
				bwriter_.newLine();
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for one dimensional elements part 2.
	 * 
	 */
	private void writeTable2() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Sections (One dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "I3", "SA2", "SA3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write material info
			for (int i = 0; i < element1D_.size(); i++) {

				// get section
				Section s = element1D_.get(i).getSection();

				// get general properties
				table[0] = Integer.toString(element1DId_.get(i));
				table[1] = formatter(s.getInertiaX3(0));
				table[2] = formatter(s.getShearAreaX2(0));
				table[3] = formatter(s.getShearAreaX3(0));

				// write
				bwriter_.write(table(table));
				bwriter_.newLine();
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for two dimensional elements.
	 * 
	 */
	private void writeTable3() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Sections (Two dimensional elements)"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "h" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write section info
			for (int i = 0; i < element2D_.size(); i++) {

				// get section
				Section s = element2D_.get(i).getSection();

				// get general properties
				table[0] = Integer.toString(element2DId_.get(i));
				table[1] = s.getName();
				table[2] = formatter(s.getDimension(0));

				// write
				bwriter_.write(table(table));
				bwriter_.newLine();
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}
