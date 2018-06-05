package material;

import matrix.DMat;
import matrix.DVec;

public class Orthotropic extends Material {

	private static final long serialVersionUID = 1L;

	/** Young's modulus of material. */
	private double[] e_;

	/** Poisson's ratio of material. */
	private double[] nu_;

	/** Shear modulus of material. */
	private double[] g_;

	/** Coefficient of thermal expansion of material. */
	private double[] alpha_;

	/**
	 * Creates isotropic material.
	 * 
	 * @param name
	 *            The name of material.
	 * 
	 * @param e
	 *            Young's modulus in directions 1, 2 and 3, respectively.
	 * @param nu
	 *            Poisson's ratios in planes 1-2, 1-3 and 2-3, respectively.
	 * @param g
	 *            Shear modulii in planes 1-2, 1-3 and 2-3, respectively.
	 */
	public Orthotropic(String name, double[] e, double[] nu, double[] g) {

		// set name of material
		setName(name);

		// set material parameters
		e_ = e;
		nu_ = nu;
		g_ = g;

		// check
		checkValues();
	}

	/**
	 * Sets coefficients of thermal expansion to material.
	 * 
	 * @param alpha
	 *            Coefficients of thermal expansion in directions 1, 2 and 3,
	 *            respectively.
	 */
	public void setThermalExpansion(double[] alpha) {

		// check dimensions
		if (alpha.length != 3)
			exceptionHandler("Illegal parameter for orthotropic material!");

		// check for zero or negative values
		for (int i = 0; i < 3; i++) {
			if (alpha[i] <= 0)
				exceptionHandler("Illegal parameter for orthotropic material!");
		}

		// set
		alpha_ = alpha;
	}

	/**
	 * Returns the type of material.
	 * 
	 * @return The type of material.
	 */
	public int getType() {
		return orthotropic_;
	}

	/**
	 * Returns the stiffness matrix.
	 * 
	 * @param type
	 *            The state of stiffness.
	 */
	public DMat getC(int type) {

		// three dimensional
		if (type == Material.threeD_)
			return getS(type).invert();

		// plane stress
		else if (type == Material.planeStress_) {

			// setup C
			double nu12 = nu_[0];
			double nu21 = nu12 * e_[1] / e_[0];
			DMat c = new DMat(3, 3);
			c.set(0, 0, e_[0] / (1 - nu12 * nu21));
			c.set(0, 1, nu12 * e_[1] / (1 - nu12 * nu21));
			c.set(1, 1, e_[1] / (1 - nu12 * nu21));
			c.set(2, 2, g_[0]);
			c = c.mirror();

			// return
			return c;
		}

		// plane strain
		else if (type == Material.planeStrain_) {

			// setup C
			double nu12 = nu_[0];
			double nu21 = nu12 * e_[1] / e_[0];
			DMat c = new DMat(3, 3);
			c.set(0, 0, e_[0] * (1 - nu12) / (1 - nu12 - 2 * nu12 * nu21));
			c.set(0, 1, nu12 * e_[1] / (1 - nu12 - 2 * nu12 * nu21));
			c.set(1, 1, e_[1] * (1 - nu12 * nu21)
					/ ((1 + nu12) * (1 - nu12 - 2 * nu12 * nu21)));
			c.set(2, 2, g_[0]);
			c = c.mirror();

			// return
			return c;
		}

		// illegal state demanded
		else
			exceptionHandler("Illegal state of material stiffness matrix!");
		return null;
	}

	/**
	 * Returns the compliance matrix.
	 * 
	 * @param type
	 *            The state of compliance matrix.
	 */
	public DMat getS(int type) {

		// three dimensional
		if (type == Material.threeD_) {

			// setup S
			double nu21 = nu_[0] * e_[1] / e_[0];
			double nu31 = nu_[1] * e_[2] / e_[0];
			double nu32 = nu_[2] * e_[2] / e_[1];
			DMat s = new DMat(6, 6);
			s.set(0, 0, 1.0 / e_[0]);
			s.set(0, 1, -nu21 / e_[1]);
			s.set(0, 2, -nu31 / e_[2]);
			s.set(1, 1, 1.0 / e_[1]);
			s.set(1, 2, -nu32 / e_[2]);
			s.set(2, 2, 1.0 / e_[2]);
			s.set(3, 3, 1.0 / g_[2]);
			s.set(4, 4, 1.0 / g_[1]);
			s.set(5, 5, 1.0 / g_[0]);
			s = s.mirror();

			// return
			return s;
		}

		// plane stress
		else if (type == Material.planeStress_)
			return getC(type).invert();

		// plane strain
		else if (type == Material.planeStrain_)
			return getC(type).invert();

		// illegal state demanded
		else
			exceptionHandler("Illegal state of material compliance matrix!");
		return null;
	}

	/**
	 * Returns the thermal influence vector.
	 * 
	 * @param type
	 *            The state of thermal influence vector.
	 */
	public DVec getAlpha(int type) {

		// three dimensional
		if (type == Material.threeD_) {
			DVec phi = new DVec(6);
			if (alpha_ != null) {
				phi.set(0, alpha_[0]);
				phi.set(1, alpha_[1]);
				phi.set(2, alpha_[2]);
			}
			return phi;
		}

		// plane stress
		else if (type == Material.planeStress_) {
			DVec phi = new DVec(3);
			if (alpha_ != null) {
				phi.set(0, alpha_[0]);
				phi.set(1, alpha_[1]);
			}
			return phi;
		}

		// plane strain
		else if (type == Material.planeStrain_) {
			DVec phi = new DVec(3);
			if (alpha_ != null) {
				phi.set(0, alpha_[0]);
				phi.set(1, alpha_[1]);
			}
			return phi;
		}

		// illegal state demanded
		else
			exceptionHandler("Illegal state of thermal influence vector!");
		return null;
	}

	/**
	 * Returns Young's modulus in directions 1, 2 and 3, respectively.
	 * 
	 * @return Young's modulus in directions 1, 2 and 3, respectively.
	 */
	public double[] getElasticModulus() {
		return e_;
	}

	/**
	 * Returns Poisson's ratios in planes 1-2, 1-3 and 2-3, respectively.
	 * 
	 * @return Poisson's ratios in planes 1-2, 1-3 and 2-3, respectively.
	 */
	public double[] getPoisson() {
		return nu_;
	}

	/**
	 * Returns coefficients of thermal expansion in directions 1, 2 and 3,
	 * respectively.
	 * 
	 * @return Coefficients of thermal expansion in directions 1, 2 and 3,
	 *         respectively.
	 */
	public double[] getThermalExpansion() {
		if (alpha_ == null)
			return new double[3];
		else
			return alpha_;
	}

	/**
	 * Returns shear modulii in planes 1-2, 1-3 and 2-3, respectively.
	 * 
	 * @return Shear modulii in planes 1-2, 1-3 and 2-3, respectively.
	 */
	public double[] getShearModulus() {
		return g_;
	}

	/**
	 * Checks for illegal parameters.
	 */
	private void checkValues() {

		// set error message
		String message = "Illegal parameter for orthotropic material!";

		// check dimensions
		if (e_.length != 3 || nu_.length != 3 || g_.length != 3)
			exceptionHandler(message);

		// check for zero or negative values
		for (int i = 0; i < 3; i++) {
			if (e_[i] <= 0 || nu_[i] <= 0 || g_[i] <= 0)
				exceptionHandler(message);
		}

		// check Poisson's ratios
		double nu12 = nu_[0];
		double nu21 = nu12 * e_[1] / e_[0];
		double nu13 = nu_[1];
		double nu31 = nu13 * e_[2] / e_[0];
		double nu23 = nu_[2];
		double nu32 = nu23 * e_[2] / e_[1];
		if (1 - nu23 * nu32 == 0)
			exceptionHandler(message);
		if (1 - nu31 * nu13 == 0)
			exceptionHandler(message);
		if (1 - nu12 * nu21 == 0)
			exceptionHandler(message);
		if (nu21 + nu31 * nu23 == 0)
			exceptionHandler(message);
		if (nu31 + nu21 * nu32 == 0)
			exceptionHandler(message);
		if (nu32 + nu31 * nu12 == 0)
			exceptionHandler(message);
		if (1 - nu12 * nu21 - nu23 * nu32 - nu31 * nu13 - 2 * nu12 * nu23
				* nu31 == 0)
			exceptionHandler(message);
	}
}
