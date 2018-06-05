package visualize;

import inf.v3d.obj.Arrow;
import inf.v3d.obj.Object3D;
import inf.v3d.obj.Polygons;
import inf.v3d.obj.PolygonOutlines;
import inf.v3d.obj.Text;

import java.awt.Color;
import java.util.Vector;
import java.util.Locale;

import data.Group;

import node.Node;

import main.SolidMAT;
import matrix.DMat;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.ElementMechLoad;
import boundary.ElementTemp;

import analysis.Structure;

import element.Element;
import element.Element2D;
import element.ElementMass;
import element.ElementSpring;
import element.ElementLibrary;

/**
 * Class for drawing two dimensional elements and related objects for the
 * pre-visualizer.
 * 
 * @author Murat
 * 
 */
public class PreElement2DDisplay {

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PreVisualizer.global_;

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
	 * Creates Element2DDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 */
	public PreElement2DDisplay(Structure structure, double maxDistance,
			double minDistance, double[] factors) {

		// set structure
		structure_ = structure;

		// compute scaled values
		computeScaledValues(maxDistance, minDistance, factors);
	}

	/**
	 * Sets coordinate system for drawing numerical values.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of drawing.
	 */
	protected void setCoordinateSystem(int coordinateSystem) {
		coordinateSystem_ = coordinateSystem;
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
	 * Draws two dimensional elements and related objects.
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

			// two dimensional elements
			if (e.getDimension() == ElementLibrary.twoDimensional_) {

				// get two dimensional element
				Element2D e2D = (Element2D) e;

				// set boundary cases to element
				e2D.setBoundaryCases(bCases_, bScales_);

				// get nodes of element
				Node[] nodes = e2D.getNodes();

				// create array for storing nodal position vectors
				DVec[] pos = new DVec[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodes.length; j++)
					pos[j] = nodes[j].getPosition();

				// visibility-materials-sections-types-groups
				if (options[0] || options[6] || options[7] || options[10]
						|| options[11] || options[12] || options[13]
						|| options[14] || options[15])
					drawElement(owner, e2D, pos, vizOptions, options, assigns);

				// label
				if (options[1])
					drawLabel(e2D, Integer.toString(i), pos);

				// mechanical loads
				if (options[3]) {
					if (e2D.getMechLoads().size() != 0)
						drawMechLoads(e2D, pos);
				}

				// temperature loads
				if (options[4]) {
					if (e2D.getTempLoads().size() != 0)
						drawTempLoads(e2D, pos);
				}

				// additional masses
				if (options[5]) {
					if (e2D.getAdditionalMasses() != null)
						if (e2D.getAdditionalMasses().size() != 0)
							drawMasses(e2D, pos);
				}

				// springs
				if (options[8]) {
					if (e2D.getSprings() != null)
						if (e2D.getSprings().size() != 0)
							drawSprings(e2D, pos);
				}

				// local axes
				if (options[9])
					drawLocalAxes(e2D, pos);
			}
		}
	}

	/**
	 * Draws two dimensional elements as polygons.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e2D
	 *            Two dimensional element.
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
	private void drawElement(SolidMAT owner, Element2D e2D, DVec[] pos,
			double[] vizOptions, boolean[] options, Vector[] assigns) {

		// create polygon
		Polygons poly = new Polygons();

		// set color
		poly.setColored(false);
		poly.setColor(vizOptions[0], vizOptions[1], vizOptions[2]);

		// colored assigns selected
		if (options[6] || options[7] || options[10])
			setColorForAssigns(e2D, options, assigns, poly);

		// groups assign selected
		else if (options[11])
			setColorForGroupAssigns(owner, e2D, assigns, poly);

		// upside down assign selected
		else if (options[12])
			setColorForUpsideDownAssigns(owner, e2D, assigns, poly);

		// inside out/zero volume assign selected
		else if (options[13] || options[14])
			setColorForNoAssigns(assigns, poly);

		// aspect ratio assign selected
		else if (options[15])
			setColorForAspectRatioAssigns(owner, e2D, assigns, poly);

		// set opacity
		poly.setOpacity(vizOptions[3]);

		// set outline
		PolygonOutlines outlines = new PolygonOutlines();
		outlines.setPolygons(poly);

		// get the geometry of element
		int geo = e2D.getGeometry();

		// insert cells
		poly.insertNextCell(pos.length);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// four noded
			if (pos.length == 4) {
				for (int i = 0; i < 4; i++)
					poly.insertCellPoint(pos[i].get1DArray(), 0.0);
			}

			// eight noded
			else if (pos.length == 8) {
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
			}

			// twelve noded
			else if (pos.length == 12) {
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[6].get1DArray(), 0.0);
				poly.insertCellPoint(pos[7].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[8].get1DArray(), 0.0);
				poly.insertCellPoint(pos[9].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[10].get1DArray(), 0.0);
				poly.insertCellPoint(pos[11].get1DArray(), 0.0);
			}
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// three noded
			if (pos.length == 3) {
				for (int i = 0; i < 3; i++)
					poly.insertCellPoint(pos[i].get1DArray(), 0.0);
			}

			// six noded
			else if (pos.length == 6) {
				poly.insertCellPoint(pos[0].get1DArray(), 0.0);
				poly.insertCellPoint(pos[3].get1DArray(), 0.0);
				poly.insertCellPoint(pos[1].get1DArray(), 0.0);
				poly.insertCellPoint(pos[4].get1DArray(), 0.0);
				poly.insertCellPoint(pos[2].get1DArray(), 0.0);
				poly.insertCellPoint(pos[5].get1DArray(), 0.0);
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
	private void setColorForAspectRatioAssigns(SolidMAT owner, Element2D e2D,
			Vector[] assigns, Polygons poly) {

		// initialize color to be set
		Color c = null;

		// get area of element
		double a = e2D.getArea();

		// get nodes of element
		Node[] nodes = e2D.getNodes();

		// quad element
		if (e2D.getGeometry() == Element2D.quadrangular_) {

			// compute edge lengths
			double d1 = nodes[1].getPosition().subtract(nodes[0].getPosition())
					.l2Norm();
			double d2 = nodes[2].getPosition().subtract(nodes[1].getPosition())
					.l2Norm();
			double d3 = nodes[3].getPosition().subtract(nodes[2].getPosition())
					.l2Norm();
			double d4 = nodes[0].getPosition().subtract(nodes[3].getPosition())
					.l2Norm();

			// compute perimeter
			double s = d1 + d2 + d3 + d4;

			// compute aspect ratio of element
			double arEl = s / a;

			// get maximum edge length
			double max = Math.max(Math.max(d1, d2), Math.max(d3, d4));

			// compute aspect ratio of perfect quad
			double arPer = 4.0 / max;

			// normalize element's aspect ratio
			arEl = arEl / arPer;

			// bad aspect ratio
			if (arEl > treshold_ + 1.0)
				c = (Color) assigns[1].get(0);

			// acceptable aspect ratio
			else
				c = (Color) assigns[1].lastElement();
		}

		// tria element
		else if (e2D.getGeometry() == Element2D.triangular_) {

			// compute edge lengths
			double d1 = nodes[1].getPosition().subtract(nodes[0].getPosition())
					.l2Norm();
			double d2 = nodes[2].getPosition().subtract(nodes[1].getPosition())
					.l2Norm();
			double d3 = nodes[2].getPosition().subtract(nodes[0].getPosition())
					.l2Norm();

			// compute perimeter
			double s = d1 + d2 + d3;

			// compute aspect ratio of element
			double arEl = s / a;

			// get maximum edge length
			double max = Math.max(Math.max(d1, d2), d3);

			// compute aspect ratio of perfect tria
			double arPer = 12.0 / (max * Math.sqrt(3));

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
	 * Sets color to element for the upside down assign.
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
	private void setColorForUpsideDownAssigns(SolidMAT owner, Element2D e2D,
			Vector[] assigns, Polygons poly) {

		// initialize and compute natural coordinates
		double[] eps1 = null;
		double[] eps2 = null;

		// quad geometry
		if (e2D.getGeometry() == Element2D.quadrangular_) {
			eps1 = new double[4];
			eps2 = new double[4];
			eps1[0] = -1.0;
			eps1[1] = 1.0;
			eps1[2] = 1.0;
			eps1[3] = -1.0;
			eps2[0] = -1.0;
			eps2[1] = -1.0;
			eps2[2] = 1.0;
			eps2[3] = 1.0;
		}

		// triangular geometry
		else if (e2D.getGeometry() == Element2D.triangular_) {
			eps1 = new double[3];
			eps2 = new double[3];
			eps1[0] = 1.0;
			eps1[1] = 0.0;
			eps1[2] = 0.0;
			eps2[0] = 0.0;
			eps2[1] = 1.0;
			eps2[2] = 0.0;
		}

		// loop over corner points of element
		int m = 0;
		for (int j = 0; j < eps1.length; j++) {

			// compute the jacobian and determinant
			double jacDet = e2D.getJacobian(eps1[j], eps2[j]).determinant();

			// check if it is negative
			if (jacDet < 0.0)
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
	 *            Two dimensional element.
	 * @param label
	 *            The string to be written.
	 * @param pos
	 *            The position of text.
	 */
	private void drawLabel(Element2D e, String label, DVec[] pos) {

		// get the geometry of element
		int geo = e.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// write label
		drawText(label, posL);
	}

	/**
	 * Draws mechanical loads of two dimensional elements.
	 * 
	 * @param e2D
	 *            Two dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMechLoads(Element2D e2D, DVec[] pos) {

		// get the geometry of element
		int geo = e2D.getGeometry();

		// create vector for storing position of text
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// get three dimensional transformation matrix of element
		DMat tr3D = e2D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// create vectors for storing loading values of corner points
		DVec ml1 = new DVec(6);
		DVec ml2 = new DVec(6);
		DVec ml3 = new DVec(6);

		// loop over mechanical loads of element
		for (int i = 0; i < e2D.getMechLoads().size(); i++) {

			// get load
			ElementMechLoad l = e2D.getMechLoads().get(i);

			// create vectors from loading values of corner points
			DVec lv1 = new DVec(6);
			DVec lv2 = new DVec(6);
			DVec lv3 = new DVec(6);
			lv1.set(l.getComponent(), l.getLoadingValues().get(0));
			lv2.set(l.getComponent(), l.getLoadingValues().get(1));
			lv3.set(l.getComponent(), l.getLoadingValues().get(2));

			// transform loading vectors to demanded coordinates
			if (l.getCoordinateSystem() != coordinateSystem_) {
				lv1 = lv1.transform(tr, coordinateSystem_);
				lv2 = lv2.transform(tr, coordinateSystem_);
				lv3 = lv3.transform(tr, coordinateSystem_);
			}

			// add vectors to element's loading vector
			ml1 = ml1.add(lv1);
			ml2 = ml2.add(lv2);
			ml3 = ml3.add(lv3);
		}

		// set text to be written
		String val1 = formatter(ml1.get(component_));
		String val2 = formatter(ml2.get(component_));
		String val3 = formatter(ml3.get(component_));
		String text = val1 + ", " + val2 + ", " + val3;

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreVisualizer.local_)
			coord = 0;

		// draw arrow
		int h = 1;
		if (component_ > 2)
			h = 2;
		DVec tp = drawArrow(h, component_ % 3, posL, coord, tr3D, 1.0, "green",
				false);

		// draw text
		drawText(text, tp);
	}

	/**
	 * Draws temperature loads of two dimensional elements.
	 * 
	 * @param e2D
	 *            Two dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawTempLoads(Element2D e2D, DVec[] pos) {

		// get the geometry of element
		int geo = e2D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// loop over temperature loads of element
		double theta = 0.0;
		Vector<ElementTemp> ml = e2D.getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// get temperature load
		String value = formatter(theta);

		// write value
		drawText(value, posL);
	}

	/**
	 * Draws additional masses of two dimensional elements.
	 * 
	 * @param e2D
	 *            Two dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMasses(Element2D e2D, DVec[] pos) {

		// get the geometry of element
		int geo = e2D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// create vector for storing element additional masses
		DVec mass = new DVec(6);

		// get three dimensional transformation matrix of element
		DMat tr3D = e2D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// loop over masses of element
		for (int i = 0; i < e2D.getAdditionalMasses().size(); i++) {

			// get additional mass
			ElementMass em = e2D.getAdditionalMasses().get(i);

			// create vector for storing additional mass
			DVec vec = new DVec(6);
			int comp = em.getComponent();
			double value = em.getValue();
			vec.set(comp, value);

			// transform mass vector to demanded coordinates
			if (em.getCoordinateSystem() != coordinateSystem_)
				vec = vec.transform(tr, coordinateSystem_);

			// add to element additional mass vector
			mass = mass.add(vec);
		}

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreVisualizer.local_)
			coord = 0;

		// u1 direction
		if (mass.get(0) != 0) {
			DVec tp = drawArrow(1, 0, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(0)), tp);
		}

		// u2 direction
		if (mass.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(1)), tp);
		}

		// u3 direction
		if (mass.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(2)), tp);
		}

		// r1 direction
		if (mass.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(3)), tp);
		}

		// r2 direction
		if (mass.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(4)), tp);
		}

		// r3 direction
		if (mass.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(mass.get(5)), tp);
		}
	}

	/**
	 * Draws springs of two dimensional elements.
	 * 
	 * @param e2D
	 *            Two dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawSprings(Element2D e2D, DVec[] pos) {

		// get the geometry of element
		int geo = e2D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// create vector for storing element spring stiffnesses
		DVec spring = new DVec(6);

		// get three dimensional transformation matrix of element
		DMat tr3D = e2D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// loop over springs of element
		for (int i = 0; i < e2D.getSprings().size(); i++) {

			// get springs
			ElementSpring em = e2D.getSprings().get(i);

			// create vector for storing spring stiffness
			DVec vec = new DVec(6);
			int comp = em.getComponent();
			double value = em.getValue();
			vec.set(comp, value);

			// transform mass vector to demanded coordinates
			if (em.getCoordinateSystem() != coordinateSystem_)
				vec = vec.transform(tr, coordinateSystem_);

			// add to element spring stiffness vector
			spring = spring.add(vec);
		}

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreVisualizer.local_)
			coord = 0;

		// u1 direction
		if (spring.get(0) != 0) {
			DVec tp = drawArrow(1, 0, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(0)), tp);
		}

		// u2 direction
		if (spring.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(1)), tp);
		}

		// u3 direction
		if (spring.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(2)), tp);
		}

		// r1 direction
		if (spring.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(3)), tp);
		}

		// r2 direction
		if (spring.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(4)), tp);
		}

		// r3 direction
		if (spring.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, coord, tr3D, 1.0, "green", true);
			drawText(formatter(spring.get(5)), tp);
		}
	}

	/**
	 * Draws local axes of two dimensional elements.
	 * 
	 */
	private void drawLocalAxes(Element2D e2D, DVec[] pos) {

		// get transformation matrix of element
		DMat tr = e2D.getTransformation();

		// get the geometry of element
		int geo = e2D.getGeometry();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// for quadrilateral elements
		if (geo == Element2D.quadrangular_) {

			// compute intersection point of diagonals
			DVec x1 = pos[0].add(pos[1]).scale(0.5);
			DVec x2 = pos[3].add(pos[2]).scale(0.5);
			DVec x3 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[3]).scale(0.5);
			DVec a = x2.subtract(x1);
			DVec b = x4.subtract(x3);
			DVec c = x3.subtract(x1);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = x1.add(a.scale(s));
		}

		// for triangular elements
		else if (geo == Element2D.triangular_) {

			// compute intersection point of medians
			DVec x2 = pos[1].add(pos[2]).scale(0.5);
			DVec x4 = pos[0].add(pos[2]).scale(0.5);
			DVec a = x2.subtract(pos[0]);
			DVec b = x4.subtract(pos[1]);
			DVec c = pos[1].subtract(pos[0]);
			double s = (c.cross(b).dot(a.cross(b)))
					/ Math.pow(a.cross(b).l2Norm(), 2.0);
			posL = pos[0].add(a.scale(s));
		}

		// x1 axis
		drawArrow(1, 0, posL, 0, tr, 1.0, "red", false);

		// x2 axis
		drawArrow(1, 1, posL, 0, tr, 1.0, "green", false);

		// x3 axis
		drawArrow(1, 2, posL, 0, tr, 1.0, "blue", false);
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
	 * @param coord
	 *            Coordinate system.
	 * @param tr
	 *            Transformation matrix.
	 * @param length
	 *            The length scale.
	 * @param color
	 *            Color.
	 * @param doTranslate
	 *            True if two headed arrows should be translated, False vice
	 *            versa.
	 * @return Text position for numerical values.
	 */
	private DVec drawArrow(int head, int dir, DVec pos, int coord, DMat tr,
			double length, String color, boolean doTranslate) {

		// one headed
		if (head == 1) {

			// create vector of arrow
			DVec pos1 = new DVec(3);
			pos1.set(dir, arrowLength1_ * length);

			// transform into demanded coordinate system
			if (coord != 2)
				pos1 = pos1.transform(tr, coord);

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

			// transform into demanded coordinate system
			if (coord != 2)
				pos1 = pos1.transform(tr, coord);

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

			// transform into demanded coordinate system
			if (coord != 2)
				pos2 = pos2.transform(tr, coord);

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
