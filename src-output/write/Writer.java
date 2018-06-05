package write;

import java.io.File;
import java.util.Locale;

import analysis.Structure;

/**
 * Class for writing output file.
 * 
 * @author Murat
 * 
 */
public abstract class Writer {

	/**
	 * The writing tolerance. Absolute values smaller than this value will be
	 * considered as zero.
	 */
	private static final double tolerance_ = 1.00E-10;

	/**
	 * Writes demanded information to output file.
	 * 
	 * @param structure
	 *            The structure to be printed.
	 * @param output
	 *            The output file.
	 */
	protected abstract void write(Structure structure, File output);

	/**
	 * Writes header.
	 * 
	 * @param header
	 *            The header to be written.
	 * @return The header.
	 */
	protected String header(String header) {
		return "Table: " + header;
	}

	/**
	 * Writes lines of tables.
	 * 
	 * @param values
	 *            The values array to be written.
	 * @return The line of table.
	 */
	protected String table(String[] values) {

		// check dimension of array
		if (values.length > 5)
			exceptionHandler("Illegal size for table!");

		// arrange length of values
		for (int i = 0; i < values.length; i++) {

			// get the remaining length of values
			int length = 8 - values[i].length();

			// add remaining space
			if (length >= 0) {
				for (int j = 0; j < length; j++)
					values[i] += " ";
			}

			// subtract over space
			else
				values[i] = values[i].substring(0, 8);
		}

		// construct line
		String line = "";
		for (int i = 0; i < values.length; i++)
			line += '\t' + values[i];

		// return
		return line;
	}

	/**
	 * Formats given real number to scientific form and returns string.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	protected String formatter(double number) {

		// check number
		if (Math.abs(number) < tolerance_)
			number = 0.0;

		// format number
		String value = String.format(Locale.US, "%." + 2 + "E", number);
		if (value.length() == 9)
			value = String.format(Locale.US, "%." + 1 + "E", number);
		else if (value.length() == 10)
			value = String.format(Locale.US, "%." + "E", number);

		// return formatted value
		return value;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	protected void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
