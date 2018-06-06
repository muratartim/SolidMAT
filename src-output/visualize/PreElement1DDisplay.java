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
package visualize;

import java.util.Vector;
import java.util.Locale;

import java.awt.Color;

import data.Group;

import section.Section;

import node.Node;

import main.SolidMAT;
import matrix.DMat;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.ElementMechLoad;
import boundary.ElementTemp;

import analysis.Structure;

import inf.v3d.obj.Arrow;
import inf.v3d.obj.Cylinder;
import inf.v3d.obj.Extrusion;
import inf.v3d.obj.Text;
import inf.v3d.obj.Object3D;

import element.*;

/**
 * Class for drawing one dimensional elements and related objects for the
 * pre-visualizer.
 * 
 * @author Murat
 * 
 */
public class PreElement1DDisplay {

	/** Static variable for the number of arrows of distributed loads. */
	private static final int numberOfArrows_ = 20;

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PreVisualizer.global_;

	/** The structure to be drawn. */
	private Structure structure_;

	/** The scaled values of the drawing. */
	private double radius_, radius1_, textHeight_, textPosition_,
			arrowLength1_, arrowLength2_;

	/** The boundary cases to be displayed for boundaries. */
	private Vector<BoundaryCase> bCases_ = new Vector<BoundaryCase>();

	/** The scaling factor of boundaries. Always displayes as 1. */
	private double[] bScales_ = new double[0];

	/** Writing tolerance. */
	private double tol_;

	/**
	 * Creates Element1DDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 */
	public PreElement1DDisplay(Structure structure, double maxDistance,
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
	 * Draws one dimensional elements and related objects.
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

			// one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {

				// get one dimensional element
				Element1D e1D = (Element1D) e;

				// set boundary cases to element
				e1D.setBoundaryCases(bCases_, bScales_);

				// get nodes of element
				Node[] nodes = e1D.getNodes();

				// create array for storing nodal position vectors
				DVec[] pos = new DVec[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodes.length; j++)
					pos[j] = nodes[j].getPosition();

				// visibility-materials-sections-types-groups
				if (options[0] || options[6] || options[7] || options[10]
						|| options[11] || options[12] || options[13]
						|| options[14] || options[15])
					drawElement(owner, e1D, pos, vizOptions, options, assigns);

				// label
				if (options[1])
					drawLabel(Integer.toString(i), pos);

				// extrude
				if (options[2]) {
					if (e1D.getSection() != null)
						drawExtrude(owner, e1D, pos, vizOptions, options,
								assigns);
				}

				// mechanical loads
				if (options[3]) {
					if (e1D.getMechLoads().size() != 0)
						drawMechLoads(e1D, pos);
				}

				// temperature loads
				if (options[4]) {
					if (e1D.getTempLoads().size() != 0)
						drawTempLoads(e1D, pos);
				}

				// additional masses
				if (options[5]) {
					if (e1D.getAdditionalMasses() != null)
						if (e1D.getAdditionalMasses().size() != 0)
							drawMasses(e1D, pos);
				}

				// springs
				if (options[8]) {
					if (e1D.getSprings() != null)
						if (e1D.getSprings().size() != 0)
							drawSprings(e1D, pos);
				}

				// local axes
				if (options[9])
					drawLocalAxes(e1D, pos);
			}
		}
	}

	/**
	 * Draws one dimensional elements as lines.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e1D
	 *            One dimensional element to be drawn.
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
	private void drawElement(SolidMAT owner, Element1D e1D, DVec[] pos,
			double[] vizOptions, boolean[] options, Vector[] assigns) {

		// loop over nodes of element
		for (int i = 0; i < pos.length - 1; i++) {

			// get positions of recent node
			double x1 = pos[i].get(0);
			double y1 = pos[i].get(1);
			double z1 = pos[i].get(2);

			// get positions of next node
			double x2 = pos[i + 1].get(0);
			double y2 = pos[i + 1].get(1);
			double z2 = pos[i + 1].get(2);

			// create cylinder
			Cylinder cylinder = new Cylinder(x1, y1, z1, x2, y2, z2);

			// set radius
			cylinder.setRadius(radius_);

			// set color
			cylinder.setColor(vizOptions[0], vizOptions[1], vizOptions[2]);

			// colored assigns selected
			if (options[6] || options[7] || options[10])
				setColorForAssigns(e1D, options, assigns, cylinder);

			// groups assign selected
			else if (options[11])
				setColorForGroupAssigns(owner, e1D, assigns, cylinder);

			// check assign selected
			else if (options[12] || options[13] || options[14] || options[15])
				setColorForCheckAssigns(assigns, cylinder);

			// set opacity
			cylinder.setOpacity(vizOptions[3]);
		}
	}

	/**
	 * Sets color to element for check assigns.
	 * 
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 * @param object3D
	 *            Drawing object representing the element.
	 */
	private void setColorForCheckAssigns(Vector[] assigns, Object3D object3D) {

		// element has no assign
		Color c = (Color) assigns[1].lastElement();
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		object3D.setColor(r, g, b);
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
	 * @param object3D
	 *            Drawing object representing the element.
	 */
	private void setColorForGroupAssigns(SolidMAT owner, Element e,
			Vector[] assigns, Object3D object3D) {

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
				object3D.setColor(r, g, b);
				return;
			}
		}

		// element has no assign
		Color c = (Color) assigns[1].lastElement();
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		object3D.setColor(r, g, b);
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
	 * @param object3D
	 *            Drawing object representing the element.
	 */
	private void setColorForAssigns(Element e, boolean[] options,
			Vector[] assigns, Object3D object3D) {

		// get assign
		Object a = null;
		if (options[6])
			a = e.getMaterial();
		else if (options[7])
			a = e.getSection();
		else if (options[10])
			a = e.getType();

		// element has no assign
		if (a == null) {

			// get last element of colors vector and set
			Color c = (Color) assigns[1].lastElement();
			double r = c.getRed() / 255.0;
			double g = c.getGreen() / 255.0;
			double b = c.getBlue() / 255.0;
			object3D.setColor(r, g, b);
		}

		// element has assign
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
			object3D.setColor(r, g, b);
		}
	}

	/**
	 * Draws label of element.
	 * 
	 * @param label
	 *            The string to be written.
	 * @param pos
	 *            The position of text.
	 */
	private void drawLabel(String label, DVec[] pos) {

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// loop over components of nodal poasition vectors
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// compute mid point
			if (value1 <= value2)
				posL.set(i, value1 + 0.5 * Math.abs(value2 - value1));
			else
				posL.set(i, value1 - 0.5 * Math.abs(value2 - value1));
		}

		// write label
		drawText(label, posL);
	}

	/**
	 * Extrudes one dimensional elements.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param e1D
	 *            One dimensional element to be drawn.
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
	private void drawExtrude(SolidMAT owner, Element1D e1D, DVec[] pos,
			double[] vizOptions, boolean[] options, Vector[] assigns) {

		// loop over nodes of element
		for (int i = 0; i < pos.length - 1; i++) {

			// get positions of recent node
			double x1 = pos[i].get(0);
			double y1 = pos[i].get(1);
			double z1 = pos[i].get(2);

			// get positions of next node
			double x2 = pos[i + 1].get(0);
			double y2 = pos[i + 1].get(1);
			double z2 = pos[i + 1].get(2);

			// create extrusion
			Extrusion extrusion = new Extrusion();
			extrusion.setPoint1(x1, y1, z1);
			extrusion.setPoint2(x2, y2, z2);

			// set outline
			Section section = e1D.getSection();
			extrusion.setOutline(section.getOutline());

			// set color
			extrusion.setColor(vizOptions[0], vizOptions[1], vizOptions[2]);

			// colored assigns selected
			if (options[6] || options[7] || options[10])
				setColorForAssigns(e1D, options, assigns, extrusion);

			// groups assign selected
			else if (options[11])
				setColorForGroupAssigns(owner, e1D, assigns, extrusion);

			// check assign selected
			else if (options[12] || options[13] || options[14] || options[15])
				setColorForCheckAssigns(assigns, extrusion);

			// set opacity
			extrusion.setOpacity(vizOptions[3]);
		}
	}

	/**
	 * Draws mechanical loads of one dimensional elements.
	 * 
	 * @param e1D
	 *            One dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMechLoads(Element1D e1D, DVec[] pos) {

		// create array for storing position vectors of stations
		DVec[] posL = new DVec[numberOfArrows_];
		for (int i = 0; i < posL.length; i++)
			posL[i] = new DVec(3);

		// compute positions of stationary points
		int ns = posL.length;
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// loop over stationary points
			for (int j = 0; j < ns; j++) {

				// compute positions of points
				double value = Math.abs(value2 - value1) * j / (ns - 1);
				if (value1 <= value2)
					posL[j].set(i, value1 + value);
				else
					posL[j].set(i, value1 - value);
			}
		}

		// get three dimensional transformation matrix of element
		DMat tr3D = e1D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// create vectors for storing loading values of end points
		DVec ml1 = new DVec(6);
		DVec ml2 = new DVec(6);

		// loop over mechanical loads of element
		for (int i = 0; i < e1D.getMechLoads().size(); i++) {

			// get load
			ElementMechLoad l = e1D.getMechLoads().get(i);

			// create vectors from loading values of end points
			DVec lv1 = new DVec(6);
			DVec lv2 = new DVec(6);
			lv1.set(l.getComponent(), l.getLoadingValues().get(0));
			lv2.set(l.getComponent(), l.getLoadingValues().get(1));

			// transform loading vectors to demanded coordinates
			if (l.getCoordinateSystem() != coordinateSystem_) {
				lv1 = lv1.transform(tr, coordinateSystem_);
				lv2 = lv2.transform(tr, coordinateSystem_);
			}

			// add vectors to element's loading vector
			ml1 = ml1.add(lv1);
			ml2 = ml2.add(lv2);
		}

		// get length of element
		double length = e1D.getLength();

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreVisualizer.local_)
			coord = 0;

		// draw mechanical loads
		for (int i = 0; i < 6; i++) {

			// check for zero load
			if (ml1.get(i) != 0.0 || ml2.get(i) != 0) {

				// get values and compute maximum absolute value
				double y1 = ml1.get(i);
				double y2 = ml2.get(i);
				double max = Math.max(Math.abs(y1), Math.abs(y2));

				// loop over stations
				for (int j = 0; j < ns; j++) {

					// compute length scalor of arrow
					double x = j * length / (ns - 1);
					double y = (x * (y2 - y1) / length + y1) / max;

					// draw arrow
					int h = 1;
					if (i > 2)
						h = 2;
					DVec tp = drawArrow(h, i % 3, posL[j], coord, tr3D, y,
							"green");

					// draw text
					if (j == 1)
						drawText(formatter(y1), tp);
					if (j == ns - 2)
						drawText(formatter(y2), tp);
				}
			}
		}
	}

	/**
	 * Draws temperature loads of one dimensional elements.
	 * 
	 * @param e1D
	 *            One dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawTempLoads(Element1D e1D, DVec[] pos) {

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// loop over components of nodal position vectors
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// compute mid point
			if (value1 <= value2)
				posL.set(i, value1 + 0.5 * Math.abs(value2 - value1));
			else
				posL.set(i, value1 - 0.5 * Math.abs(value2 - value1));
		}

		// loop over temperature loads of element
		double theta = 0.0;
		Vector<ElementTemp> ml = e1D.getTempLoads();
		for (int i = 0; i < ml.size(); i++)
			theta += ml.get(i).getValue();

		// get temperature load
		String value = formatter(theta);

		// write value
		drawText(value, posL);
	}

	/**
	 * Draws additional masses of one dimensional elements.
	 * 
	 * @param e1D
	 *            One dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawMasses(Element1D e1D, DVec[] pos) {

		// create vector for storing position of arrows
		DVec posL = new DVec(3);

		// loop over components of nodal position vectors
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// compute mid point
			if (value1 <= value2)
				posL.set(i, value1 + 0.5 * Math.abs(value2 - value1));
			else
				posL.set(i, value1 - 0.5 * Math.abs(value2 - value1));
		}

		// create vector for storing element additional masses
		DVec mass = new DVec(6);

		// get three dimensional transformation matrix of element
		DMat tr3D = e1D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// loop over masses of element
		for (int i = 0; i < e1D.getAdditionalMasses().size(); i++) {

			// get additional mass
			ElementMass em = e1D.getAdditionalMasses().get(i);

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
			DVec tp = drawArrow(1, 0, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(0)), tp);
		}

		// u2 direction
		if (mass.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(1)), tp);
		}

		// u3 direction
		if (mass.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(2)), tp);
		}

		// r1 direction
		if (mass.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(3)), tp);
		}

		// r2 direction
		if (mass.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(4)), tp);
		}

		// r3 direction
		if (mass.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(mass.get(5)), tp);
		}
	}

	/**
	 * Draws springs of one dimensional elements.
	 * 
	 * @param e1D
	 *            One dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawSprings(Element1D e1D, DVec[] pos) {

		// create vector for storing position of arrows
		DVec posL = new DVec(3);

		// loop over components of nodal position vectors
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// compute mid point
			if (value1 <= value2)
				posL.set(i, value1 + 0.5 * Math.abs(value2 - value1));
			else
				posL.set(i, value1 - 0.5 * Math.abs(value2 - value1));
		}

		// create vector for storing element springs
		DVec spring = new DVec(6);

		// get three dimensional transformation matrix of element
		DMat tr3D = e1D.getTransformation();
		DMat tr = new DMat(6, 6);
		tr = tr.setSubMatrix(tr3D, 0, 0);
		tr = tr.setSubMatrix(tr3D, 2, 2);

		// loop over springs of element
		for (int i = 0; i < e1D.getSprings().size(); i++) {

			// get spring
			ElementSpring es = e1D.getSprings().get(i);

			// create vector for storing spring stiffnesses
			DVec vec = new DVec(6);
			int comp = es.getComponent();
			double value = es.getValue();
			vec.set(comp, value);

			// transform stiffness vector to demanded coordinates
			if (es.getCoordinateSystem() != coordinateSystem_)
				vec = vec.transform(tr, coordinateSystem_);

			// add to element spring vector
			spring = spring.add(vec);
		}

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreVisualizer.local_)
			coord = 0;

		// u1 direction
		if (spring.get(0) != 0) {
			DVec tp = drawArrow(1, 0, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(0)), tp);
		}

		// u2 direction
		if (spring.get(1) != 0) {
			DVec tp = drawArrow(1, 1, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(1)), tp);
		}

		// u3 direction
		if (spring.get(2) != 0) {
			DVec tp = drawArrow(1, 2, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(2)), tp);
		}

		// r1 direction
		if (spring.get(3) != 0) {
			DVec tp = drawArrow(2, 0, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(3)), tp);
		}

		// r2 direction
		if (spring.get(4) != 0) {
			DVec tp = drawArrow(2, 1, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(4)), tp);
		}

		// r3 direction
		if (spring.get(5) != 0) {
			DVec tp = drawArrow(2, 2, posL, coord, tr3D, 1.0, "green");
			drawText(formatter(spring.get(5)), tp);
		}
	}

	/**
	 * Draws local axes of one dimensional elements.
	 * 
	 */
	private void drawLocalAxes(Element1D e1D, DVec[] pos) {

		// get transformation matrix of element
		DMat tr = e1D.getTransformation();

		// create vector for storing position of label
		DVec posL = new DVec(3);

		// loop over components of nodal poasition vectors
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// compute mid point
			if (value1 <= value2)
				posL.set(i, value1 + 0.5 * Math.abs(value2 - value1));
			else
				posL.set(i, value1 - 0.5 * Math.abs(value2 - value1));
		}

		// x1 axis
		drawArrow(1, 0, posL, 0, tr, 1.0, "red");

		// x2 axis
		drawArrow(1, 1, posL, 0, tr, 1.0, "green");

		// x3 axis
		drawArrow(1, 2, posL, 0, tr, 1.0, "blue");
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
	 * @return Text position for numerical values.
	 */
	private DVec drawArrow(int head, int dir, DVec pos, int coord, DMat tr,
			double length, String color) {

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
			arrow.setRadius(radius1_);
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
			arrow1.setRadius(radius1_);
			arrow1.setColor(color);

			// translate first arrow for an additional distance
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
			arrow2.setRadius(radius1_);
			arrow2.setColor(color);

			// translate second arrow for an additional distance
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

		// compute radius of line
		radius_ = 0.25 * scale * factors[1];

		// compute radius of arrow
		radius1_ = 0.25 * scale * factors[2];

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
