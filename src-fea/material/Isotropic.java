package material;

import matrix.DMat;
import matrix.DVec;

/**
 * Class for isotropic material.
 * 
 * @author Murat
 * 
 */
public class Isotropic extends Material {

	private static final long serialVersionUID = 1L;

	/** Young's modulus of material. */
	private double e_;

	/** Poisson's ratio of material. */
	private double nu_;

	/** Coefficient of thermal expansion of material. */
	private double alpha_ = 0.0;

	/**
	 * Creates isotropic material.
	 * 
	 * @param name
	 *            The name of material.
	 * 
	 * @param e
	 *            Young's modulus.
	 * @param nu
	 *            Poisson's ratio.
	 */
	public Isotropic(String name, double e, double nu) {

		// set name of material
		setName(name);

		// set elasticity modulus
		if (e >= 0)
			e_ = e;
		else
			exceptionHandler("Illegal assignment for Young's modulus!");

		// set Poisson's ratio
		if (nu >= 0 && nu < 0.5)
			nu_ = nu;
		else
			exceptionHandler("Illegal assignment for Poisson's ratio!");
	}

	/**
	 * Sets coefficient of thermal expansion to material.
	 * 
	 * @param alpha
	 *            Coefficient of thermal expansion.
	 */
	public void setThermalExpansion(double alpha) {
		if (alpha >= 0)
			alpha_ = alpha;
		else
			exceptionHandler("Illegal assignment for coefficient of thermal expansion!");
	}

	/**
	 * Returns the type of material.
	 * 
	 * @return The type of material.
	 */
	public int getType() {
		return isotropic_;
	}

	/**
	 * Returns the stiffness matrix.
	 * 
	 * @param type
	 *            The state of stiffness.
	 */
	public DMat getC(int type) {

		// three dimensional
		if (type == Material.threeD_) {

			// scale factor
			double value = e_ / ((1.0 + nu_) * (1.0 - 2.0 * nu_));

			// setup C
			DMat c = new DMat(6, 6);
			c.set(0, 0, 1.0 - nu_);
			c.set(0, 1, nu_);
			c.set(0, 2, nu_);
			c.set(1, 0, nu_);
			c.set(1, 1, 1.0 - nu_);
			c.set(1, 2, nu_);
			c.set(2, 0, nu_);
			c.set(2, 1, nu_);
			c.set(2, 2, 1 - nu_);
			c.set(3, 3, 0.5 * (1.0 - 2.0 * nu_));
			c.set(4, 4, 0.5 * (1.0 - 2.0 * nu_));
			c.set(5, 5, 0.5 * (1.0 - 2.0 * nu_));
			c = c.scale(value);

			// return
			return c;
		}

		// plane stress
		else if (type == Material.planeStress_) {

			// scale factor
			double value = e_ / (1 - nu_ * nu_);

			// setup C
			DMat c = new DMat(3, 3);
			c.set(0, 0, 1.0);
			c.set(0, 1, nu_);
			c.set(1, 0, nu_);
			c.set(1, 1, 1.0);
			c.set(2, 2, (1 - nu_) * 0.5);
			c = c.scale(value);

			// return
			return c;
		}

		// plane strain
		else if (type == Material.planeStrain_) {

			// scale factor
			double value = e_ / ((1 + nu_) * (1 - 2 * nu_));

			// setup C
			DMat c = new DMat(3, 3);
			c.set(0, 0, 1 - nu_);
			c.set(0, 1, nu_);
			c.set(1, 0, nu_);
			c.set(1, 1, 1 - nu_);
			c.set(2, 2, (1 - 2 * nu_) * 0.5);
			c = c.scale(value);

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
		return getC(type).invert();
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
			phi.set(0, alpha_);
			phi.set(1, alpha_);
			phi.set(2, alpha_);
			return phi;
		}

		// plane stress
		else if (type == Material.planeStress_) {
			DVec phi = new DVec(3);
			phi.set(0, alpha_);
			phi.set(1, alpha_);
			return phi;
		}

		// plane strain
		else if (type == Material.planeStrain_) {
			DVec phi = new DVec(3);
			phi.set(0, alpha_);
			phi.set(1, alpha_);
			return phi;
		}

		// illegal state demanded
		else
			exceptionHandler("Illegal state of thermal influence vector!");
		return null;
	}

	/**
	 * Returns Young's modulus of material.
	 * 
	 * @return Young's modulus.
	 */
	public double getElasticModulus() {
		return e_;
	}

	/**
	 * Returns Poisson's ratio of material.
	 * 
	 * @return Poisson's ratio of material.
	 */
	public double getPoisson() {
		return nu_;
	}

	/**
	 * Returns coefficient of thermal expansion of material.
	 * 
	 * @return Coefficient of thermal expansion of material.
	 */
	public double getThermalExpansion() {
		return alpha_;
	}

	/**
	 * Returns shear modulus of material.
	 * 
	 * @return Shear modulus of material.
	 */
	public double getShearModulus() {
		return e_ / (2 * (nu_ + 1));
	}
}
