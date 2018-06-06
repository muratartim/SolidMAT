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
package matrix;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class for all types of matrices.
 * 
 * @author Murat Artim
 * 
 */
public abstract class Mat {

	/**
	 * Adds element to specified position.
	 * 
	 * @param arg0
	 *            The row index.
	 * @param arg1
	 *            The column index.
	 * @param arg2
	 *            The value to be added.
	 */
	public abstract void add(int arg0, int arg1, double arg2);

	/**
	 * Returns the column count.
	 * 
	 * @return The column count.
	 */
	public abstract int columnCount();

	/**
	 * Returns the demanded element.
	 * 
	 * @param arg0
	 *            The row index of demanded element.
	 * @param arg1
	 *            The column index of demanded element.
	 * @return The demanded element.
	 */
	public abstract double get(int arg0, int arg1);

	/**
	 * Returns the row count.
	 * 
	 * @return The row count.
	 */
	public abstract int rowCount();

	/**
	 * Sets the element to respective place.
	 * 
	 * @param arg0
	 *            The row index to be set.
	 * @param arg1
	 *            The column index to be set.
	 * @param arg2
	 *            The element to be set.
	 */
	public abstract void set(int arg0, int arg1, double arg2);

	/**
	 * Prints matrix in decimal format.
	 * 
	 * @param arg0
	 *            The column width.
	 * @param arg1
	 *            The number of digits after the decimal seperator.
	 */
	public void printInDecimal(int arg0, int arg1) {
		PrintWriter pw = new PrintWriter(System.out, true);
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(arg1);
		format.setMinimumFractionDigits(arg1);
		format.setGroupingUsed(false);
		pw.println();
		for (int i = 0; i < rowCount(); i++) {
			for (int j = 0; j < columnCount(); j++) {
				String s = format.format(get(i, j));
				int padding = Math.max(1, arg0 + 2 - s.length());
				for (int k = 0; k < padding; k++)
					pw.print(' ');
				pw.print(s);
			}
			pw.println();
		}
		pw.println();
	}

	/**
	 * Prints matrix in scientific format.
	 * 
	 * @param arg0
	 *            The number of digits after the decimal seperator.
	 */
	public void printInScientific(int arg0) {

		for (int i = 0; i < rowCount(); i++) {
			for (int j = 0; j < columnCount(); j++) {

				// get element
				double value = get(i, j);

				// format element
				String s = String.format("%." + arg0 + "E", value);

				// print space
				if (value < 0)
					System.out.print(" ");
				else
					System.out.print("  ");

				// print element
				System.out.print(s);
			}

			// pass to next line
			System.out.println();
		}
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
