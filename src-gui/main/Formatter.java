package main;

import java.util.Locale;

/**
 * Class for formatting numeric values in GUIs.
 * 
 * @author Murat Artim
 * 
 */
public class Formatter {

	/** Format type of formatter. */
	private String format_ = "a";

	/** Number of digits after comma separator. */
	private int digits_ = 4;

	/**
	 * Sets format type (a -> automatic, f -> real number, E -> scientific
	 * number).
	 * 
	 * @param format
	 *            Format type (a -> automatic, f -> real number, E -> scientific
	 *            number).
	 */
	public void setFormat(String format) {
		format_ = format;
	}

	/**
	 * Sets number of digits after comma separator.
	 * 
	 * @param digits
	 *            Number of digits after comma separator.
	 */
	public void setDigits(int digits) {
		digits_ = digits;
	}

	/**
	 * Returns format type (a -> automatic, f -> real number, E -> scientific
	 * number).
	 * 
	 * @return Format type (a -> automatic, f -> real number, E -> scientific
	 *         number).
	 */
	public String getFormat() {
		return format_;
	}

	/**
	 * Returns number of digits after comma separator.
	 * 
	 * @return Number of digits after comma separator.
	 */
	public int getDigits() {
		return digits_;
	}

	/**
	 * Formats given double number. Automatic formating rule: If the absolute
	 * value of the number is >= 1.00E-03 and <= 1.00E+03, it is formatted as a
	 * decimal number with 4 digits after the decimal separator. Otherwise, it
	 * is formatted as a scientific number with 4 digits after the decimal
	 * seperator.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	public String format(double number) {

		// automatic formating
		if (format_.equalsIgnoreCase("a")) {
			if (Math.abs(number) >= 1.00E-03 && Math.abs(number) <= 1.00E+03)
				return String.format(Locale.US, "%.4f", number);
			else
				return String.format(Locale.US, "%.4E", number);
		}

		// user defined formatting
		else
			return String.format(Locale.US, "%." + digits_ + format_, number);
	}
}