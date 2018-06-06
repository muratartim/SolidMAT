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

import java.awt.Color;
import java.util.Vector;
import java.util.Locale;

import data.Group;

import node.*;

import main.SolidMAT;
import matrix.DMat;
import matrix.DVec;

import boundary.BoundaryCase;

import analysis.Structure;

import inf.v3d.obj.Sphere;
import inf.v3d.obj.BoundingBox;
import inf.v3d.obj.Text;
import inf.v3d.obj.Arrow;

/**
 * Class for drawing nodes and related objects for the pre-visualizer.
 * 
 * @author Murat
 * 
 */
public class PreNodeDisplay {

	/** Static variable for drawing coordinate system. */
	private static final int global_ = 0, local_ = 1;

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PreNodeDisplay.global_;

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
	 * Creates PreNodeDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 */
	public PreNodeDisplay(Structure structure, double maxDistance,
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
	 * Draws nodes and related objects.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param options
	 *            The node display options.
	 * @param vizOptions
	 *            The node visualization options.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 */
	protected void draw(SolidMAT owner, boolean[] options, double[] vizOptions,
			Vector[] assigns) {

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// set boundary cases to node
			node.setBoundaryCases(bCases_, bScales_);

			// get position vector of node
			DVec pos = node.getPosition();

			// visibility-groups
			if (options[0] || options[10])
				drawNode(owner, node, pos, vizOptions, options, assigns);

			// label
			if (options[1])
				drawText(Integer.toString(i), pos);

			// constraints
			if (options[2]) {
				if (node.getConstraint() != null)
					drawConstraints(node, pos);
			}

			// springs
			if (options[3]) {
				if (node.getSprings() != null)
					if (node.getSprings().size() != 0)
						drawSprings(node, pos);
			}

			// mechanical loads
			if (options[4]) {
				if (node.getMechLoads().size() != 0)
					drawMechLoads(node, pos);
			}

			// displacement loads
			if (options[5]) {
				if (node.getDispLoads().size() != 0)
					drawDispLoads(node, pos);
			}

			// masses
			if (options[6]) {
				if (node.getMasses() != null)
					if (node.getMasses().size() != 0)
						drawMasses(node, pos);
			}

			// local axes
			if (options[7])
				drawLocalAxes(pos, node.getTransformation());

			// initial displacements
			if (options[8]) {
				if (node.getInitialDisp().size() != 0)
					drawInitialDisp(node, pos);
			}

			// initial velocities
			if (options[9]) {
				if (node.getInitialVelo().size() != 0)
					drawInitialVelo(node, pos);
			}
		}
	}

	/**
	 * Draws nodes as sphere.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 * @param vizOptions
	 *            The visualization options for node.
	 * @param options
	 *            The node display options.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of elements.
	 */
	private void drawNode(SolidMAT owner, Node node, DVec pos,
			double[] vizOptions, boolean[] options, Vector[] assigns) {

		// create sphere
		Sphere sphere = new Sphere();

		// set center
		double x = pos.get(0);
		double y = pos.get(1);
		double z = pos.get(2);
		sphere.setCenter(x, y, z);

		// set radius
		sphere.setRadius(radius_);

		// create bounding box
		BoundingBox box = new BoundingBox(sphere);

		// unregister sphere
		sphere.unregister();

		// set color
		box.setColor(vizOptions[0], vizOptions[1], vizOptions[2]);

		// groups assign selected
		if (options[10])
			setColorForGroupAssigns(owner, node, assigns, box);

		// set opacity
		box.setOpacity(vizOptions[3]);
	}

	/**
	 * Sets color to node for the group-assign.
	 * 
	 * @param owner
	 *            The owner frame of this drawing.
	 * @param node
	 *            The node to be colored.
	 * @param assigns
	 *            Vector storing the names and related colors of colored assigns
	 *            of nodes.
	 * @param box
	 *            Drawing object representing the node.
	 */
	private void setColorForGroupAssigns(SolidMAT owner, Node node,
			Vector[] assigns, BoundingBox box) {

		// get groups vector
		Vector<Group> groups = owner.inputData_.getGroup();

		// loop over groups
		for (int j = 0; j < groups.size(); j++) {

			// get group
			Group group = groups.get(j);

			// node is contained in the group
			if (group.containsNode(node)) {

				// get name of group
				String name = group.getName();

				// get related color and set
				Color c = (Color) assigns[1].get(assigns[0].indexOf(name));
				double r = c.getRed() / 255.0;
				double g = c.getGreen() / 255.0;
				double b = c.getBlue() / 255.0;
				box.setColor(r, g, b);
				return;
			}
		}

		// node has no assign
		Color c = (Color) assigns[1].lastElement();
		double r = c.getRed() / 255.0;
		double g = c.getGreen() / 255.0;
		double b = c.getBlue() / 255.0;
		box.setColor(r, g, b);
	}

	/**
	 * Draws constraints for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawConstraints(Node node, DVec pos) {

		// get constraints of node
		boolean[] cons = node.getConstraint().getConstraints();

		// get transformation matrix of node
		DMat tr = node.getTransformation().getSubMatrix(0, 0, 2, 2);

		// u1 constraint
		if (!cons[0])
			drawArrow(1, 0, pos, 0, tr, "green");

		// u2 constraint
		if (!cons[1])
			drawArrow(1, 1, pos, 0, tr, "green");

		// u3 constraint
		if (!cons[2])
			drawArrow(1, 2, pos, 0, tr, "green");

		// r1 constraint
		if (!cons[3])
			drawArrow(2, 0, pos, 0, tr, "green");

		// r2 constraint
		if (!cons[4])
			drawArrow(2, 1, pos, 0, tr, "green");

		// r3 constraint
		if (!cons[5])
			drawArrow(2, 2, pos, 0, tr, "green");
	}

	/**
	 * Draws springs for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawSprings(Node node, DVec pos) {

		// get stiffness matrix of node in local coordinates
		DMat k = node.getStiffnessMatrix();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			k = k.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (k.get(0, 0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(k.get(0, 0)), tp);
		}

		// u2 direction
		if (k.get(1, 1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(k.get(1, 1)), tp);
		}

		// u3 direction
		if (k.get(2, 2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(k.get(2, 2)), tp);
		}

		// r1 direction
		if (k.get(3, 3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(k.get(3, 3)), tp);
		}

		// r2 direction
		if (k.get(4, 4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(k.get(4, 4)), tp);
		}

		// r3 direction
		if (k.get(5, 5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(k.get(5, 5)), tp);
		}
	}

	/**
	 * Draws mechanical loads for nodes.
	 * 
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawMechLoads(Node node, DVec pos) {

		// get mechanical load vector of node in local coordinates
		DVec load = node.getMechLoadVector();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			load = load.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (load.get(0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(0)), tp);
		}

		// u2 direction
		if (load.get(1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(1)), tp);
		}

		// u3 direction
		if (load.get(2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(2)), tp);
		}

		// r1 direction
		if (load.get(3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(3)), tp);
		}

		// r2 direction
		if (load.get(4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(4)), tp);
		}

		// r3 direction
		if (load.get(5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(5)), tp);
		}
	}

	/**
	 * Draws displacement loads for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawDispLoads(Node node, DVec pos) {

		// get displacement load vector of node in local coordinates
		DVec load = node.getDispLoadVector();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			load = load.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (load.get(0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(0)), tp);
		}

		// u2 direction
		if (load.get(1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(1)), tp);
		}

		// u3 direction
		if (load.get(2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(2)), tp);
		}

		// r1 direction
		if (load.get(3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(3)), tp);
		}

		// r2 direction
		if (load.get(4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(4)), tp);
		}

		// r3 direction
		if (load.get(5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(5)), tp);
		}
	}

	/**
	 * Draws initial diplacements for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawInitialDisp(Node node, DVec pos) {

		// get initial diplacement vector of node in local coordinates
		DVec load = node.getInitialDispVector();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			load = load.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (load.get(0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(0)), tp);
		}

		// u2 direction
		if (load.get(1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(1)), tp);
		}

		// u3 direction
		if (load.get(2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(2)), tp);
		}

		// r1 direction
		if (load.get(3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(3)), tp);
		}

		// r2 direction
		if (load.get(4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(4)), tp);
		}

		// r3 direction
		if (load.get(5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(5)), tp);
		}
	}

	/**
	 * Draws initial velocities for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawInitialVelo(Node node, DVec pos) {

		// get initial velocity vector of node in local coordinates
		DVec load = node.getInitialVeloVector();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			load = load.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (load.get(0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(0)), tp);
		}

		// u2 direction
		if (load.get(1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(1)), tp);
		}

		// u3 direction
		if (load.get(2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(2)), tp);
		}

		// r1 direction
		if (load.get(3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(load.get(3)), tp);
		}

		// r2 direction
		if (load.get(4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(load.get(4)), tp);
		}

		// r3 direction
		if (load.get(5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(load.get(5)), tp);
		}
	}

	/**
	 * Draws masses for nodes.
	 * 
	 * @param node
	 *            The node to be drawn.
	 * @param pos
	 *            The position vector of node.
	 */
	private void drawMasses(Node node, DVec pos) {

		// get mass matrix of node in local coordinates
		DMat mass = node.getMassMatrix();

		// transform if drawing in global coordinates is demanded
		DMat tr = node.getTransformation();
		if (coordinateSystem_ == PreNodeDisplay.global_)
			mass = mass.transform(tr, DMat.toGlobal_);
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// get demanded coordinate system
		int coord = 2;
		if (coordinateSystem_ == PreNodeDisplay.local_)
			coord = 0;

		// u1 direction
		if (mass.get(0, 0) != 0) {
			DVec tp = drawArrow(1, 0, pos, coord, tr, "green");
			drawText(formatter(mass.get(0, 0)), tp);
		}

		// u2 direction
		if (mass.get(1, 1) != 0) {
			DVec tp = drawArrow(1, 1, pos, coord, tr, "green");
			drawText(formatter(mass.get(1, 1)), tp);
		}

		// u3 direction
		if (mass.get(2, 2) != 0) {
			DVec tp = drawArrow(1, 2, pos, coord, tr, "green");
			drawText(formatter(mass.get(2, 2)), tp);
		}

		// r1 direction
		if (mass.get(3, 3) != 0) {
			DVec tp = drawArrow(2, 0, pos, coord, tr, "green");
			drawText(formatter(mass.get(3, 3)), tp);
		}

		// r2 direction
		if (mass.get(4, 4) != 0) {
			DVec tp = drawArrow(2, 1, pos, coord, tr, "green");
			drawText(formatter(mass.get(4, 4)), tp);
		}

		// r3 direction
		if (mass.get(5, 5) != 0) {
			DVec tp = drawArrow(2, 2, pos, coord, tr, "green");
			drawText(formatter(mass.get(5, 5)), tp);
		}
	}

	/**
	 * Draws local axes of nodes.
	 * 
	 * @param pos
	 *            The position vector of node.
	 * @param tr
	 *            The transformation matrix of node.
	 */
	private void drawLocalAxes(DVec pos, DMat tr) {

		// get transformation matrix of node
		tr = tr.getSubMatrix(0, 0, 2, 2);

		// x1 axis
		drawArrow(1, 0, pos, 0, tr, "red");

		// x2 axis
		drawArrow(1, 1, pos, 0, tr, "green");

		// x3 axis
		drawArrow(1, 2, pos, 0, tr, "blue");
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
	 * @param color
	 *            Color.
	 * @return Text position for numerical values.
	 */
	private DVec drawArrow(int head, int dir, DVec pos, int coord, DMat tr,
			String color) {

		// one headed
		if (head == 1) {

			// create vector of arrow
			DVec pos1 = new DVec(3);
			pos1.set(dir, arrowLength1_);

			// transform into demanded coordinate system
			if (coord != 2)
				pos1 = pos1.transform(tr, coord);

			// add position vector of node
			DVec vec = pos.add(pos1);

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
			pos1.set(dir, arrowLength1_);

			// transform into demanded coordinate system
			if (coord != 2)
				pos1 = pos1.transform(tr, coord);

			// add position vector of node
			DVec vec1 = pos.add(pos1);

			// create first arrow
			Arrow arrow1 = new Arrow(pos.get1DArray(), vec1.get1DArray());
			arrow1.setRadius(radius1_);
			arrow1.setColor(color);

			// translate first arrow for an additional distance
			arrow1.translate(pos1.get(0), pos1.get(1), pos1.get(2));

			// create vector of second arrow
			DVec pos2 = new DVec(3);
			pos2.set(dir, arrowLength2_);

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

		// compute radius of node
		radius_ = scale * factors[0];

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
