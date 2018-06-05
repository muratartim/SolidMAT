package math;

import java.io.Serializable;

/**
 * Class for arbitrary functions.
 * 
 * @author Murat Artim
 * 
 */
public class Function implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of function. */
	public static int linear_ = 0, quadratic_ = 1, cubic_ = 2, sine_ = 3,
			cosine_ = 4, tangent_ = 5, exp_ = 6, log_ = 7, log10_ = 8,
			userDefined_ = 9;

	/** The name of function. */
	private String name_;

	/** Array storing the parameters of the function. */
	private double[] param_ = { 0.0, 1.0, 0.0, 0.0 };

	/**
	 * Array storing the x values of the function (for only user defined
	 * functions).
	 */
	private double[] xValues_;

	/**
	 * Array storing the y values of the function (for only user defined
	 * functions).
	 */
	private double[] yValues_;

	/** The type of function. */
	private int type_ = Function.linear_;

	/**
	 * Creates function.
	 * 
	 * @param name
	 *            The name of the function.
	 * @param type
	 *            The type of the function.
	 */
	public Function(String name, int type) {

		// set name
		name_ = name;

		// set type
		setType(type);
	}

	/**
	 * Sets name to the function.
	 * 
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets type to function.
	 * 
	 * @param type
	 *            The type to be set.
	 */
	public void setType(int type) {

		// check type
		if (type < 0 || type > 9)
			exceptionHandler("Illegal type for function!");

		// set type
		type_ = type;
	}

	/**
	 * Sets parameters of the function.
	 * 
	 * @param param
	 *            Array storing the parameters of the function.
	 */
	public void setParameters(double[] param) {

		// parameter array
		if (param.length != param_.length)
			exceptionHandler("Illegal parameters for function!");

		// set parameters
		param_ = param;
	}

	/**
	 * Sets values of the function. This method is used for user defined
	 * functions.
	 * 
	 * @param xValues
	 *            Array storing the x values of the function.
	 * @param yValues
	 *            Array storing the y values of the function.
	 */
	public void setValues(double[] xValues, double[] yValues) {

		// check arrays
		if (xValues.length != yValues.length)
			exceptionHandler("Illegal values for function!");

		// set values
		xValues_ = xValues;
		yValues_ = yValues;
	}

	/**
	 * Returns the name of the function.
	 * 
	 * @return The name of the function.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the type of function.
	 * 
	 * @return The type of function.
	 */
	public int getType() {
		return type_;
	}

	/**
	 * Returns the parameters of the function.
	 * 
	 * @return The parameters of the function.
	 */
	public double[] getParameters() {
		return param_;
	}

	/**
	 * Returns x values of the function.
	 * 
	 * @return The x values of the function.
	 */
	public double[] getXValues() {
		return xValues_;
	}

	/**
	 * Returns y values of the function.
	 * 
	 * @return The y values of the function.
	 */
	public double[] getYValues() {
		return yValues_;
	}

	/**
	 * Returns the value of the function for the given ordinate.
	 * 
	 * @param x
	 *            Ordinate.
	 * @return The value of the function.
	 */
	public double getValue(double x) {

		// initiaize value to be returned
		double val = 0.0;

		// linear function
		if (type_ == Function.linear_)
			val = param_[0] * x + param_[1];

		// quadratic function
		else if (type_ == Function.quadratic_)
			val = param_[0] * x * x + param_[1] * x + param_[2];

		// cubic function
		else if (type_ == Function.cubic_)
			val = param_[0] * x * x * x + param_[1] * x * x + param_[2] * x
					+ param_[3];

		// sine function
		else if (type_ == Function.sine_)
			val = param_[1] * Math.sin(2 * Math.PI * x / param_[0]);

		// cosine function
		else if (type_ == Function.cosine_)
			val = param_[1] * Math.cos(2 * Math.PI * x / param_[0]);

		// tangent function
		else if (type_ == Function.tangent_)
			val = param_[1] * Math.tan(2 * Math.PI * x / param_[0]);

		// exp function
		else if (type_ == Function.exp_)
			val = Math.exp(x);

		// log function
		else if (type_ == Function.log_)
			val = Math.log(x);

		// log10 function
		else if (type_ == Function.log10_)
			val = Math.log10(x);

		// user defined function
		else if (type_ == Function.userDefined_) {

			// check ordinate
			int i = MathUtil.indexOf(xValues_, x);
			if (i != -1)
				val = yValues_[i];
			else
				exceptionHandler("Value not contained in the function!");
		}

		// return the value
		return check(val);
	}

	/**
	 * Checks if the given value is infinite or not a number.
	 * 
	 * @param value
	 *            The value to be checked.
	 * @return The value if no exception occurres.
	 */
	private double check(double value) {

		// check if infinite
		if (Double.isInfinite(value))
			exceptionHandler("Function produced INFINITE value!");

		// check if not a number
		if (Double.isNaN(value))
			exceptionHandler("Function produced NaN value!");

		// return value
		return value;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
