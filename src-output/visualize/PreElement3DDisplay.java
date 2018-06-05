package visualize;

import inf.v3d.obj.Arrow;
import inf.v3d.obj.Object3D;
import inf.v3d.obj.PolygonOutlines;
import inf.v3d.obj.Polygons;
import inf.v3d.obj.Text;

import java.awt.Color;
import java.util.Vector;
import java.util.Locale;

import data.Group;

import main.SolidMAT;
import matrix.DVec;
import node.Node;

import element.Element;
import element.Element3;
import element.Element3D;
import element.Element5;
import element.ElementMass;
import element.ElementSpring;

import analysis.Structure;
import boundary.BoundaryCase;
import boundary.ElementMechLoad;
import boundary.ElementTemp;
import element.ElementLibrary;

/**
 * Class for drawing three dimensional elements and related objects for the
 * pre-visualizer.
 * 
 * @author Murat
 * 
 */
public class PreElement3DDisplay {

	/** The component to be displayed fot drawing mechanical loads. */
	private int component_ = PreVisualizer.fx_;

	/** The structure to be drawn. */
	private Structure structure_;

	/** The boundary cases to be displayed for boundaries. */
	private Vector<BoundaryCase> bCases_ = new Vector<BoundaryCase>();

	/** The scaling factor of boundaries. Always displayes as 1. */
	private double[] bScales_ = new double[0];

	/** The scaled values of the drawing. */
	private double radius_, textHeight_, textPosition_, arrowLength1_,
			arrowLength2_;

	/** Treshold value for displaying bad aspect ratio elements. */
	private double treshold_;

	/** Writing tolerance. */
	private double tol_;

	/**
	 * Creates Element3DDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 */
	public PreElement3DDisplay(Structure structure, double maxDistance,
			double minDistance, double[] factors) {

		// set structure
		structure_ = structure;

		// compute scaled values
		computeScaledValues(maxDistance, minDistance, factors);
	}

	/**
	 * Sets component to be displayed for drawing mechanical loads.
	 * 
	 * @param comp
	 *            The component to be displayed.
	 */
	protected void setComponent(int comp) {
		component_ = comp;
	}

	/**
	 * Sets boundary cases for drawing boundaries.
	 * 
	 * @param bCases
	 *            The boundary cases to be displayed for boundaries.
	 */
	protected void setBoundaryCases(Vector<BoundaryCase> bCases) {

		// set boundary cases
		bCases_ = bCases;

		// set scaling factors
		bScales_ = new double[bCases.size()];
		for (int i = 0; i < bScales_.length; i++)
			bScales_[i] = 1.0;
	}

	/**
	 * Sets treshold value for displaying bad aspect ratio elements.
	 * 
	 * @param treshold
	 *            Treshold value to be set.
	 */
	protected void setTreshold(double treshold) {
		treshold_ = treshold;
	}

	/**
	 * Draws three dimensional elements and related objects.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param options
	 *            The element display options.
	 * @param vizOptions
	 *            The element visualization options.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 */
	protected void draw(SolidMAT owner, boolean[] options, double[] vizOptions,
			Vector[] assigns) {

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// three dimensional elements
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get three dimensional element
				Element3D e3D = (Element3D) e;

				// set boundary cases to element
				e3D.setBoundaryCases(bCases_, bScales_);

				// get nodes of element
				Node[] nodes = e3D.getNodes();

				// create array for storing nodal position vectors
				DVec[] pos = new DVec[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodes.length; j++)
					pos[j] = nodes[j].getPosition();

				// visibility-materials-sections-fatigue curves
				if (options[0] || options[6] || options[7] || options[10]
						|| options[11] || options[12] || options[13]
						|| options[14] || options[15])
					drawElement(owner, e3D, pos, vizOptions, options, assigns);

				// label
				if (options[1])
					drawLabel(e3D, Integer.toString(i), pos);

				// mechanical loads
				if (options[3]) {
					if (e3D.getMechLoads().size() != 0)
						drawMechLoads(e3D, pos);
				}

				// temperature loads
				if (options[4]) {
					if (e3D.getTempLoads().size() != 0)
						drawTempLoads(e3D, pos);
				}

				// additional masses
				if (options[5]) {
					if (e3D.getAdditionalMasses() != null)
						if (e3D.getAdditionalMasses().size() != 0)
							drawMasses(e3D, pos);
				}

				// springs
				if (options[8]) {
					if (e3D.getSprings() != null)
						if (e3D.getSprings().size() != 0)
							drawSprings(e3D, pos);
				}

				// local axes
				if (options[9])
					drawLocalAxes(e3D, pos);
			}
		}
	}

	/**
	 * Draws three dimensional elements.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 * @param vizOptions
	 *            The visualization options for element.
	 * @param options
	 *            The element display options.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 */
	private void drawElement(SolidMAT owner, Element3D e3D, DVec[] pos,
			double[] vizOptions, boolean[] options, Vector[] assigns) {

		// create polygon
		Polygons poly = new Polygons();

		// set color
		poly.setColored(false);
		poly.setColor(vizOptions[0], vizOptions[1], vizOptions[2]);

		// colored assigns selected
		if (options[6] || options[7] || options[10])
			setColorForAssigns(e3D, options, assigns, poly);

		// groups assign selected
		else if (options[11])
			setColorForGroupAssigns(owner, e3D, assigns, poly);

		// upside down assign selected
		else if (options[12])
			setColorForNoAssigns(assigns, poly);

		// inside out assign selected
		else if (options[13])
			setColorForInsideOutAssigns(owner, e3D, assigns, poly);

		// zero volume assign selected
		else if (options[14])
			setColorForZeroVolumeAssigns(owner, e3D, assigns, poly);

		// aspect ratio assign selected
		else if (options[15])
			setColorForAspectRatioAssigns(owner, e3D, assigns, poly);

		// set opacity
		poly.setOpacity(vizOptions[3]);

		// set outline
		PolygonOutlines outlines = new PolygonOutlines();
		outlines.setPolygons(poly);

		// get the geometry of element
		int geo = e3D.getGeometry();

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// eight noded
			if (pos.length == 8) {

				// face-1
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);

				// face-2
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);

				// face-3
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);

				// face-4
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);

				// face-5
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);

				// face-6
				poly.insertNextCell(4);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
			}

			// twenty noded
			else if (pos.length == 20) {

				// face-1
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[16].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[12].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[17].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[8].get1DArray(), 0.0);

				// face-2
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[17].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[13].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[18].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[9].get1DArray(), 0.0);

				// face-3
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[18].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[14].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[19].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[10].get1DArray(), 0.0);

				// face-4
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[19].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[15].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[16].get1DArray(), 0.0);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[11].get1DArray(), 0.0);

				// face-5
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[8].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[9].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[10].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[11].get1DArray(), 0.0);

				// face-6
				poly.insertNextCell(8);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[12].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[13].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[14].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[15].get1DArray(), 0.0);
			}
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// four noded
			if (pos.length == 4) {

				// face-1
				poly.insertNextCell(3);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);

				// face-2
				poly.insertNextCell(3);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);

				// face-3
				poly.insertNextCell(3);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);

				// face-4
				poly.insertNextCell(3);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
			}

			// ten noded
			else if (pos.length == 10) {

				// face-1
				poly.insertNextCell(6);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[9].get1DArray(), 0.0);

				// face-2
				poly.insertNextCell(6);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[8].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);

				// face-3
				poly.insertNextCell(6);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[9].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[8].get1DArray(), 0.0);

				// face-4
				poly.insertNextCell(6);
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
			}
		}
	}

	/**
	 * Sets color to element for no assigns.
	 * 
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param object3D
	 *            Drawing object representing the element.
	 */
	private void setColorForNoAssigns(Vector[] assigns, Object3D object3D) {

		// element has no assign
		Color c = (Color) assigns[1].lastElement();
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		object3D.setColor(r, g, b);
	}

	/**
	 * Sets color to element for the aspect ratio assign.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e
	 *            The element to be colored.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param poly
	 *            Drawing object representing the element.
	 */
	private void setColorForAspectRatioAssigns(SolidMAT owner, Element3D e3D,
			Vector[] assigns, Polygons poly) {

		// initialize color to be set
		Color c = null;

		// get volume of element
		double v = e3D.getVolume();

		// get nodes of element
		Node[] nodes = e3D.getNodes();

		// hexa element
		if (e3D.getGeometry() == Element3D.hexahedral_) {

			// compute surface areas of all faces
			Element3 eQuad = new Element3(nodes[0], nodes[4], nodes[5],
					nodes[1]);
			double a1 = eQuad.getArea();
			eQuad = new Element3(nodes[1], nodes[5], nodes[6], nodes[2]);
			double a2 = eQuad.getArea();
			eQuad = new Element3(nodes[2], nodes[6], nodes[7], nodes[3]);
			double a3 = eQuad.getArea();
			eQuad = new Element3(nodes[3], nodes[7], nodes[4], nodes[0]);
			double a4 = eQuad.getArea();
			eQuad = new Element3(nodes[0], nodes[1], nodes[2], nodes[3]);
			double a5 = eQuad.getArea();
			eQuad = new Element3(nodes[4], nodes[5], nodes[6], nodes[7]);
			double a6 = eQuad.getArea();

			// compute total surface area
			double s = a1 + a2 + a3 + a4 + a5 + a6;

			// compute aspect ratio of element
			double arEl = s / v;

			// get maximum surface area
			double max = Math.max(Math.max(a1, a2), Math.max(a3, a4));
			max = Math.max(max, Math.max(a5, a6));

			// compute aspect ratio of perfect hexa
			double arPer = 6.0 / max;

			// normalize element's aspect ratio
			arEl = arEl / arPer;

			// bad aspect ratio
			if (arEl > treshold_ + 1.0)
				c = (Color) assigns[1].get(0);

			// acceptable aspect ratio
			else
				c = (Color) assigns[1].lastElement();
		}

		// tetra element
		else if (e3D.getGeometry() == Element3D.tetrahedral_) {

			// compute surface areas of all faces
			Element5 eTria = new Element5(nodes[0], nodes[1], nodes[3]);
			double a1 = eTria.getArea();
			eTria = new Element5(nodes[1], nodes[2], nodes[3]);
			double a2 = eTria.getArea();
			eTria = new Element5(nodes[2], nodes[0], nodes[3]);
			double a3 = eTria.getArea();
			eTria = new Element5(nodes[0], nodes[2], nodes[1]);
			double a4 = eTria.getArea();

			// compute total surface area
			double s = a1 + a2 + a3 + a4;

			// compute aspect ratio of element
			double arEl = s / v;

			// get maximum surface area
			double max = Math.max(Math.max(a1, a2), Math.max(a3, a4));

			// compute aspect ratio of perfect hexa
			double arPer = 18.0 / max;

			// normalize element's aspect ratio
			arEl = arEl / arPer;

			// bad aspect ratio
			if (arEl > treshold_ + 1.0)
				c = (Color) assigns[1].get(0);

			// acceptable aspect ratio
			else
				c = (Color) assigns[1].lastElement();
		}

		// set color
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		poly.setColor(r, g, b);
	}

	/**
	 * Sets color to element for the zero volume assign.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e
	 *            The element to be colored.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param poly
	 *            Drawing object representing the element.
	 */
	private void setColorForZeroVolumeAssigns(SolidMAT owner, Element3D e3D,
			Vector[] assigns, Polygons poly) {

		// initialize color to be set
		Color c = null;

		// negative-zero volume
		if (e3D.getVolume() <= 0.0)
			c = (Color) assigns[1].get(0);

		// positive volume
		else
			c = (Color) assigns[1].lastElement();

		// set color
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		poly.setColor(r, g, b);
	}

	/**
	 * Sets color to element for the inside out assign.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e
	 *            The element to be colored.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param poly
	 *            Drawing object representing the element.
	 */
	private void setColorForInsideOutAssigns(SolidMAT owner, Element3D e3D,
			Vector[] assigns, Polygons poly) {

		// initialize and compute natural coordinates
		double[] eps1 = null;
		double[] eps2 = null;
		double[] eps3 = null;

		// hexa geometry
		if (e3D.getGeometry() == Element3D.hexahedral_) {
			eps1 = new double[8];
			eps2 = new double[8];
			eps3 = new double[8];
			eps1[0] = 1.0;
			eps2[0] = 1.0;
			eps3[0] = 1.0;
			eps1[1] = -1.0;
			eps2[1] = 1.0;
			eps3[1] = 1.0;
			eps1[2] = -1.0;
			eps2[2] = -1.0;
			eps3[2] = 1.0;
			eps1[3] = 1.0;
			eps2[3] = -1.0;
			eps3[3] = 1.0;
			eps1[4] = 1.0;
			eps2[4] = 1.0;
			eps3[4] = -1.0;
			eps1[5] = -1.0;
			eps2[5] = 1.0;
			eps3[5] = -1.0;
			eps1[6] = -1.0;
			eps2[6] = -1.0;
			eps3[6] = -1.0;
			eps1[7] = 1.0;
			eps2[7] = -1.0;
			eps3[7] = -1.0;
		}

		// tetra geometry
		else if (e3D.getGeometry() == Element3D.tetrahedral_) {
			eps1 = new double[4];
			eps2 = new double[4];
			eps3 = new double[4];
			eps1[0] = 0.0;
			eps2[0] = 0.0;
			eps3[0] = 0.0;
			eps1[1] = 1.0;
			eps2[1] = 0.0;
			eps3[1] = 0.0;
			eps1[2] = 0.0;
			eps2[2] = 1.0;
			eps3[2] = 0.0;
			eps1[3] = 0.0;
			eps2[3] = 0.0;
			eps3[3] = 1.0;
		}

		// loop over corner points of element
		int m = 0;
		for (int j = 0; j < eps1.length; j++) {

			// compute the jacobian and determinant
			double jacDet = e3D.getJacobian(eps1[j], eps2[j], eps3[j])
					.determinant();

			// check if it is negative or zero
			if (jacDet <= 0.0)
				m++;
		}

		// initialize color to be set
		Color c = null;

		// negative jacobian
		if (m > 0)
			c = (Color) assigns[1].get(0);

		// positive jacobian
		else
			c = (Color) assigns[1].lastElement();

		// set color
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		poly.setColor(r, g, b);
	}

	/**
	 * Sets color to element for the group-assign.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e
	 *            The element to be colored.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param poly
	 *            Drawing object representing the element.
	 */
	private void setColorForGroupAssigns(SolidMAT owner, Element e,
			Vector[] assigns, Polygons poly) {

		// get groups vector
		Vector<Group> groups = owner.inputData_.getGroup();

		// loop over groups
		for (int j = 0; j < groups.size(); j++) {

			// get group
			Group group = groups.get(j);

			// element is contained in the group
			if (group.containsElement(e)) {

				// get name of group
				String name = group.getName();

				// get related color and set
				Color c = (Color) assigns[1].get(assigns[0].indexOf(name));
				double r = c.getRed() / 255.0;
				double g = c.getGreen() / 255.0;
				double b = c.getBlue() / 255.0;
				poly.setColor(r, g, b);
				return;
			}
		}

		// element has no assign
		Color c = (Color) assigns[1].lastElement();
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		poly.setColor(r, g, b);
	}

	/**
	 * Sets color to element for the given colored-assign.
	 * 
	 * @param e
	 *            The element to be colored.
	 * @param options
	 *            The element display options.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param poly
	 *            Drawing object representing the element.
	 */
	private void setColorForAssigns(Element e, boolean[] options,
			Vector[] assigns, Polygons poly) {

		// get assign
		Object a = null;
		if (options[6])
			a = e.getMaterial();
		else if (options[7])
			a = e.getSection();
		else if (options[10])
			a = e.getType();

		// element has no material
		if (a == null) {

			// get last element of colors vector and set
			Color c = (Color) assigns[1].lastElement();
			double r = c.getRed() / 255.0;
			double g = c.getGreen() / 255.0;
			double b = c.getBlue() / 255.0;
			poly.setColor(r, g, b);
		}

		// element has material
		else {

			// get name of assign
			String name = null;
			if (options[6])
				name = e.getMaterial().getName();
			else if (options[7])
				name = e.getSection().getName();
			else if (options[10])
				name = "Type " + Integer.toString(e.getType());

			// get related color and set
			Color c = (Color) assigns[1].get(assigns[0].indexOf(name));
			double r = c.getRed() / 255.0;
			double g = c.getGreen() / 255.0;
			double b = c.getBlue() / 255.0;
			poly.setColor(r, g, b);
		}
	}

	/**
	 * Draws label of element.
	 * 
	 * @param e
	 *            Three dimensional element.
	 * @param label
	 *            The string to be written.
	 * @param pos
	 *            The position of text.
	 */
	private void drawLabel(Element3D e, String label, DVec[] pos) {

		// get the geometry of element
		int geo = e.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// write label
		drawText(label, posL);
	}

	/**
	 * Draws mechanical loads of three dimensional elements.
	 * 
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMechLoads(Element3D e3D, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// create vector for storing position of text
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// create vector for storing loading value
		DVec ml1 = new DVec(6);

		// loop over mechanical loads of element
		for (int i = 0; i < e3D.getMechLoads().size(); i++) {

			// get load
			ElementMechLoad l = e3D.getMechLoads().get(i);

			// create vector from loading value of element
			DVec lv1 = new DVec(6);
			lv1.set(l.getComponent(), l.getLoadingValues().get(0));

			// add vector to element's loading vector
			ml1 = ml1.add(lv1);
		}

		// set text to be written
		String text = formatter(ml1.get(component_));

		// draw arrow
		int h = 1;
		if (component_ > 2)
			h = 2;
		DVec tp = drawArrow(h, component_ % 3, posL, 1.0, "green", false);

		// draw text
		drawText(text, tp);
	}

	/**
	 * Draws temperature loads of three dimensional elements.
	 * 
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawTempLoads(Element3D e3D, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// loop over temperature loads of element
		double theta = 0.0;
		Vector<ElementTemp> ml = e3D.getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// get temperature load
		String value = formatter(theta);

		// write value
		drawText(value, posL);
	}

	/**
	 * Draws additional masses of three dimensional elements.
	 * 
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMasses(Element3D e3D, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// create vector for storing element additional masses
		DVec mass = new DVec(6);

		// loop over masses of element
		for (int i = 0; i < e3D.getAdditionalMasses().size(); i++) {

			// get additional mass
			ElementMass em = e3D.getAdditionalMasses().get(i);

			// create vector for storing additional mass
			DVec vec = new DVec(6);
			int comp = em.getComponent();
			double value = em.getValue();
			vec.set(comp, value);

			// add to element additional mass vector
			mass = mass.add(vec);
		}

		// u1 direction
		if (mass.get(0) != 0) {
			DVec tp = drawArrow(1, 0, posL, 1.0, "green", true);
			drawText(formatter(mass.get(0)), tp);
		}

		// u2 direction
		if (mass.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, 1.0, "green", true);
			drawText(formatter(mass.get(1)), tp);
		}

		// u3 direction
		if (mass.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, 1.0, "green", true);
			drawText(formatter(mass.get(2)), tp);
		}

		// r1 direction
		if (mass.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, 1.0, "green", true);
			drawText(formatter(mass.get(3)), tp);
		}

		// r2 direction
		if (mass.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, 1.0, "green", true);
			drawText(formatter(mass.get(4)), tp);
		}

		// r3 direction
		if (mass.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, 1.0, "green", true);
			drawText(formatter(mass.get(5)), tp);
		}
	}

	/**
	 * Draws springs of three dimensional elements.
	 * 
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawSprings(Element3D e3D, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// create vector for storing element spring stiffnesses
		DVec spring = new DVec(6);

		// loop over springs of element
		for (int i = 0; i < e3D.getSprings().size(); i++) {

			// get springs
			ElementSpring em = e3D.getSprings().get(i);

			// create vector for storing spring stiffness
			DVec vec = new DVec(6);
			int comp = em.getComponent();
			double value = em.getValue();
			vec.set(comp, value);

			// add to element spring stiffness vector
			spring = spring.add(vec);
		}

		// u1 direction
		if (spring.get(0) != 0) {
			DVec tp = drawArrow(1, 0, posL, 1.0, "green", true);
			drawText(formatter(spring.get(0)), tp);
		}

		// u2 direction
		if (spring.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, 1.0, "green", true);
			drawText(formatter(spring.get(1)), tp);
		}

		// u3 direction
		if (spring.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, 1.0, "green", true);
			drawText(formatter(spring.get(2)), tp);
		}

		// r1 direction
		if (spring.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, 1.0, "green", true);
			drawText(formatter(spring.get(3)), tp);
		}

		// r2 direction
		if (spring.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, 1.0, "green", true);
			drawText(formatter(spring.get(4)), tp);
		}

		// r3 direction
		if (spring.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, 1.0, "green", true);
			drawText(formatter(spring.get(5)), tp);
		}
	}

	/**
	 * Draws local axes of two dimensional elements.
	 * 
	 */
	private void drawLocalAxes(Element3D e3D, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// compute midpoint of 1-7 diagonal
			posL = pos[0].add(pos[6]).scale(0.5);
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// compute intersection point of medians
			DVec x2 = pos[2].add(pos[3]).scale(0.5);
			DVec x4 = pos[1].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(pos[1]);
			DVec b = x4.subtract(pos[2]);
			DVec c = pos[2].subtract(pos[1]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			DVec x3 = pos[1].add(a.scale(s));
			posL = pos[0].add(x3).scale(0.5);
		}

		// x1 axis
		drawArrow(1, 0, posL, 1.0, "red", false);

		// x2 axis
		drawArrow(1, 1, posL, 1.0, "green", false);

		// x3 axis
		drawArrow(1, 2, posL, 1.0, "blue", false);
	}

	/**
	 * Draws text.
	 * 
	 * @param text
	 *            The string to be written.
	 * @param pos
	 *            The position of text.
	 */
	private void drawText(String text, DVec pos) {

		// create text
		Text label = new Text(text);

		// set text height
		label.setHeight(textHeight_);

		// set position
		double x = pos.get(0) + textPosition_;
		double y = pos.get(1) + textPosition_;
		double z = pos.get(2) + textPosition_;
		label.setOrigin(x, y, z);

		// set color
		label.setColor("black");
	}

	/**
	 * Draws arrow.
	 * 
	 * @param head
	 *            Number of heads.
	 * @param dir
	 *            Direction.
	 * @param pos
	 *            Starting position.
	 * @param length
	 *            The length scale.
	 * @param color
	 *            Color.
	 * @param doTranslate
	 *            True if two headed arrows should be translated, False vice
	 *            versa.
	 * @return Text position for numerical values.
	 */
	private DVec drawArrow(int head, int dir, DVec pos, double length,
			String color, boolean doTranslate) {

		// one headed
		if (head == 1) {

			// create vector of arrow
			DVec pos1 = new DVec(3);
			pos1.set(dir, arrowLength1_ * length);

			// add position vector of node
			DVec vec = pos.add(pos1);

			// return text position if zero arrow length
			if (length == 0.0)
				return vec;

			// create arrow
			Arrow arrow = new Arrow(pos.get1DArray(), vec.get1DArray());
			arrow.setRadius(radius_);
			arrow.setColor(color);

			// return text position
			return vec;
		}

		// two headed
		else {

			// create vector of first arrow
			DVec pos1 = new DVec(3);
			pos1.set(dir, arrowLength1_ * length);

			// add position vector of node
			DVec vec1 = pos.add(pos1);

			// return text position if zero arrow length
			if (length == 0.0)
				return vec1.add(pos1);

			// create first arrow
			Arrow arrow1 = new Arrow(pos.get1DArray(), vec1.get1DArray());
			arrow1.setRadius(radius_);
			arrow1.setColor(color);

			// translate first arrow for an additional distance
			if (doTranslate)
				arrow1.translate(pos1.get(0), pos1.get(1), pos1.get(2));

			// create vector of second arrow
			DVec pos2 = new DVec(3);
			pos2.set(dir, arrowLength2_ * length);

			// add position vector of node
			DVec vec2 = pos.add(pos2);

			// create second arrow
			Arrow arrow2 = new Arrow(pos.get1DArray(), vec2.get1DArray());
			arrow2.setRadius(radius_);
			arrow2.setColor(color);

			// translate second arrow for an additional distance
			if (doTranslate)
				arrow2.translate(pos1.get(0), pos1.get(1), pos1.get(2));

			// return text position
			return vec1.add(pos1);
		}
	}

	/**
	 * Computes scaled values of the drawing.
	 * 
	 * @param minDistance
	 *            The minimum distance between nodes.
	 * @param maxDistance
	 *            The maximum distnace between nodes.
	 */
	private void computeScaledValues(double maxDistance, double minDistance,
			double[] factors) {

		// compute drawing dependent scale
		double scale = minDistance / 70.0 + maxDistance / 1000.0;

		// compute radius of arrows
		radius_ = 0.25 * scale * factors[2];

		// compute text height
		textHeight_ = 2.0 * scale * factors[4];

		// compute text position
		textPosition_ = textHeight_;

		// compute arrow lengths
		arrowLength1_ = 10.0 * scale * factors[3];
		arrowLength2_ = 7.5 * scale * factors[3];

		// set writing tolerance
		tol_ = factors[5];
	}

	/**
	 * Formats given real number to scientific form and returns string.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	private String formatter(double number) {

		// check number
		if (Math.abs(number) < tol_)
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
}
