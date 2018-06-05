package element;

import java.io.Serializable;

/**
 * Class for storing Element library information.
 * 
 * @author Murat
 * 
 */
public class ElementLibrary implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the dimension of element. */
	public final static int oneDimensional_ = 0, twoDimensional_ = 1,
			threeDimensional_ = 2;

	/** Static variable for the geometry of element. */
	public final static int line_ = 0, quad_ = 1, tria_ = 2, hexa_ = 3,
			tetra_ = 4;

	/** Static variable for the degree of interpolation function. */
	public final static int linear_ = 0, quadratic_ = 1, cubic_ = 2;

	/** Static variable for the mechanics of one dimensional elements. */
	public final static int truss_ = 0, thickBeam_ = 1, curvedThickBeam_ = 2,
			truss2D_ = 3, thickBeam2D_ = 4, thinBeam_ = 5;

	/** Static variable for the mechanics of two dimensional elements. */
	public final static int planeStress_ = 0, planeStrain_ = 1, plate_ = 2,
			shell_ = 3, curvedShell_ = 4;

	/** Static variable for the mechanics of three dimensional elements. */
	public final static int solid_ = 0;

	/** Static variable for the type of element. */
	public final static int element0_ = 0, element1_ = 1, element2_ = 2,
			element3_ = 3, element4_ = 4, element5_ = 5, element6_ = 6,
			element7_ = 7, element8_ = 8, element9_ = 9, element10_ = 10,
			element11_ = 11, element12_ = 12, element13_ = 13, element14_ = 14,
			element15_ = 15, element16_ = 16, element17_ = 17, element18_ = 18,
			element19_ = 19, element20_ = 20, element21_ = 21, element22_ = 22,
			element23_ = 23, element24_ = 24, element25_ = 25, element26_ = 26,
			element27_ = 27, element28_ = 28, element29_ = 29, element30_ = 30,
			element31_ = 31, element32_ = 32, element33_ = 33, element34_ = 34,
			element35_ = 35, element36_ = 36, element37_ = 37, element38_ = 38;

	/** The name of library. */
	private String name_;

	/** The properties of element library. */
	private int geometry_, mechanics_, interpolation_;

	/**
	 * Creates ElementLibrary object.
	 * 
	 * @param name
	 *            The name of library.
	 * @param geo
	 *            The geometry of element.
	 * @param mech
	 *            The mchanics property.
	 * @param interpol
	 *            The interpolation type of element.
	 */
	public ElementLibrary(String name, int geo, int mech, int interpol) {

		// set name
		name_ = name;

		// set element properties and check type
		geometry_ = geo;
		mechanics_ = mech;
		interpolation_ = interpol;

		// check type
		if (getType() == null)
			exceptionHandler("Invalid element type!");
	}

	/**
	 * Sets name to element library.
	 * 
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets geometry property to element library.
	 * 
	 * @param geo
	 *            The geometry type to be set.
	 */
	public void setGeometry(int geo) {
		if (geo < 0 || geo > 4)
			exceptionHandler("Illegal assignment for Element Library!");
		geometry_ = geo;
	}

	/**
	 * Sets mechanics property to element library.
	 * 
	 * @param mech
	 *            The mechanics type to be set.
	 */
	public void setMechanics(int mech) {
		if (mech < 0 || mech > 4)
			exceptionHandler("Illegal assignment for Element Library!");
		mechanics_ = mech;
	}

	/**
	 * Sets interpolation property to element library.
	 * 
	 * @param interpol
	 *            The interpolation type to be set.
	 */
	public void setInterpolation(int interpol) {
		if (interpol < 0 || interpol > 2)
			exceptionHandler("Illegal assignment for Element Library!");
		interpolation_ = interpol;
	}

	/**
	 * Returns name of element library.
	 * 
	 * @return The name of library.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns geometry property of element library.
	 * 
	 * @return The geometry property of library.
	 */
	public int getGeometry() {
		return geometry_;
	}

	/**
	 * Returns mechanics property of element library.
	 * 
	 * @return The mechanics property of library.
	 */
	public int getMechanics() {
		return mechanics_;
	}

	/**
	 * Returns interpolation property of element library.
	 * 
	 * @return The interpolation property of library.
	 */
	public int getInterpolation() {
		return interpolation_;
	}

	/**
	 * Returns type property of element library.
	 * 
	 * @return The type property of library.
	 */
	public Integer getType() {

		// line elements
		if (geometry_ == ElementLibrary.line_) {

			// truss
			if (mechanics_ == ElementLibrary.truss_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element0_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element1_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element2_;
			}

			// thick beam
			else if (mechanics_ == ElementLibrary.thickBeam_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element13_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element14_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element15_;
			}

			// thin beam
			else if (mechanics_ == ElementLibrary.thinBeam_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element38_;
			}

			// curved beam
			else if (mechanics_ == ElementLibrary.curvedThickBeam_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element27_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element28_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element29_;
			}

			// planar truss
			else if (mechanics_ == ElementLibrary.truss2D_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element32_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element33_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element34_;
			}

			// planar beam
			else if (mechanics_ == ElementLibrary.thickBeam2D_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element35_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element36_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element37_;
			}
		}

		// quad elements
		else if (geometry_ == ElementLibrary.quad_) {

			// plane stress
			if (mechanics_ == ElementLibrary.planeStress_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element3_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element4_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element7_;
			}

			// plane strain
			else if (mechanics_ == ElementLibrary.planeStrain_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element8_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element9_;

				// cubic
				else if (interpolation_ == ElementLibrary.cubic_)
					return ElementLibrary.element10_;
			}

			// plate
			else if (mechanics_ == ElementLibrary.plate_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element24_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element25_;
			}

			// shell
			else if (mechanics_ == ElementLibrary.shell_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element16_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element17_;
			}

			// doubly-curved shell
			else if (mechanics_ == ElementLibrary.curvedShell_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element19_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element20_;
			}
		}

		// tria elements
		else if (geometry_ == ElementLibrary.tria_) {

			// plane stress
			if (mechanics_ == ElementLibrary.planeStress_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element5_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element6_;
			}

			// plane strain
			else if (mechanics_ == ElementLibrary.planeStrain_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element11_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element12_;
			}

			// plate
			else if (mechanics_ == ElementLibrary.plate_) {

				// quadratic
				if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element26_;
			}

			// shell
			else if (mechanics_ == ElementLibrary.shell_) {

				// quadratic
				if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element18_;
			}

			// doubly-curved shell
			else if (mechanics_ == ElementLibrary.curvedShell_) {

				// quadratic
				if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element21_;
			}
		}

		// hexa elements
		else if (geometry_ == ElementLibrary.hexa_) {

			// solid
			if (mechanics_ == ElementLibrary.solid_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element22_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element23_;
			}
		}

		// tetra elements
		else if (geometry_ == ElementLibrary.tetra_) {

			// solid
			if (mechanics_ == ElementLibrary.solid_) {

				// linear
				if (interpolation_ == ElementLibrary.linear_)
					return ElementLibrary.element30_;

				// quadratic
				else if (interpolation_ == ElementLibrary.quadratic_)
					return ElementLibrary.element31_;
			}
		}

		// illegal element type selected
		return null;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}