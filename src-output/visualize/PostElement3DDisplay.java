package visualize;

import inf.v3d.obj.PolygonOutlines;
import inf.v3d.obj.Polygons;

import java.util.Vector;
import java.util.Locale;

import main.SolidMAT;
import math.MathUtil;
import matrix.DMat;
import matrix.DVec;
import node.Node;
import element.Element;
import element.Element3D;
import element.ElementLibrary;

import analysis.Structure;

/**
 * Class for drawing 3D element results for the post-visualizer.
 * 
 * @author Murat
 * 
 */
public class PostElement3DDisplay {

	/** Polygons used to draw elements. */
	private Polygons polys_ = new Polygons();

	/** The structure to be drawn. */
	private Structure structure_;

	/** Vector for storing demanded result values of all elements. */
	private Vector<double[]> values_;

	/** Maximum and minimum values for scaling graphics. */
	private double maxDisp_, maxVal_ = -Math.pow(10, 8), minVal_ = Math.pow(10,
			8), elMax_ = -Math.pow(10, 8), elMin_ = Math.pow(10, 8);

	/** The scaled values of the drawing. */
	private double dispScale_;

	/**
	 * Scaling factor used for scaling displacements when automatic scaling is
	 * not used.
	 */
	private Double scale_ = null;

	/**
	 * Creates PostElement3DDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 * @param maxDisplacement
	 *            The maximum displacement of a node computed after the
	 *            analysis.
	 */
	public PostElement3DDisplay(Structure structure, double maxDistance,
			double minDistance, double maxDisplacement) {

		// set structure
		structure_ = structure;

		// set maximum displacement
		maxDisp_ = maxDisplacement;

		// compute scaled values
		computeScaledValues(maxDistance, minDistance);
	}

	/**
	 * Sets factor for scaling nodal displacements.
	 * 
	 * @param scale
	 *            The scaling factor.
	 */
	protected void setScalingFactor(Double scale) {
		scale_ = scale;
	}

	/**
	 * Draws three dimensional elements and related results.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param shape
	 *            Parameter for drawing deformed or undeformed shape.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 * @param isScaled
	 *            True if displacements are scaled, false if not.
	 */
	protected void draw(SolidMAT owner, int shape, int option, int[] comp,
			boolean isScaled) {

		// get result option
		String opt = checkOption(option, comp);

		// compute demanded result values and extremes
		if (opt != null) {

			// compute nodal values
			computeValues(option, comp);

			// compute average values
			if (option != PostVisualizer.elementDisp3D_)
				computeAverage();
		}

		// loop over elements
		int k = 0;
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// three dimensional elements
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get three dimensional element
				Element3D e3D = (Element3D) e;

				// get nodes of element
				Node[] nodes = e3D.getNodes();

				// create array for storing nodal position vectors
				DVec[] pos = new DVec[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodes.length; j++)
					pos[j] = nodes[j].getPosition();

				// deformed shape demanded
				if (shape == PostVisualizer.deformed_) {

					// loop over nodes of element
					for (int j = 0; j < nodes.length; j++) {

						// get unknown vector in global coordinates
						DVec disp = nodes[j].getUnknown(Node.global_);

						// get displacements
						disp = disp.getSubVector(0, 2);

						// scale displacements if demanded
						if (isScaled)
							if (maxDisp_ != 0.0) {
								if (scale_ != null)
									disp = disp.scale(scale_);
								else
									disp = disp.scale(dispScale_ / maxDisp_);
							}

						// add displacements to position vector
						pos[j] = pos[j].add(disp);
					}
				}

				// draw results
				if (opt != null) {
					drawResults(e3D, k, pos);
					k++;
				}

				// draw element
				else
					drawElement(e3D, pos);
			}
		}

		// draw contour scalor
		if (opt != null)
			drawContourScalor(owner, opt);

		// create contour colours
		polys_.createColors();

		// delete values vector
		values_ = null;
	}

	/**
	 * Draws three dimensional elements as polygons.
	 * 
	 * @param e3D
	 *            Three dimensional element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawElement(Element3D e3D, DVec[] pos) {

		// create polygon
		Polygons poly = new Polygons();

		// set color to light grey
		poly.setColored(false);
		poly.setColor(0.752941, 0.752941, 0.752941);

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

		// set outline
		PolygonOutlines outlines = new PolygonOutlines();
		outlines.setPolygons(poly);
	}

	/**
	 * Draws result diagram.
	 * 
	 * @param e3D
	 *            Three dimensional element that the diagram will be drawn on.
	 * @param index
	 *            The index of element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 * 
	 */
	private void drawResults(Element3D e3D, int index, DVec[] pos) {

		// get the geometry of element
		int geo = e3D.getGeometry();

		// for hexahedral elements
		if (geo == Element3D.hexahedral_) {

			// eight noded
			if (pos.length == 8) {

				// face-1
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);

				// face-2
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);

				// face-3
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);

				// face-4
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);

				// face-5
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);

				// face-6
				polys_.insertNextCell(4);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
			}

			// twenty noded
			else if (pos.length == 20) {

				// face-1
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[16].get1DArray(),
						values_.get(index)[16]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[12].get1DArray(),
						values_.get(index)[12]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[17].get1DArray(),
						values_.get(index)[17]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[8].get1DArray(),
						values_.get(index)[8]);

				// face-2
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[17].get1DArray(),
						values_.get(index)[17]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[13].get1DArray(),
						values_.get(index)[13]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[18].get1DArray(),
						values_.get(index)[18]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[9].get1DArray(),
						values_.get(index)[9]);

				// face-3
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[18].get1DArray(),
						values_.get(index)[18]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[14].get1DArray(),
						values_.get(index)[14]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[19].get1DArray(),
						values_.get(index)[19]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[10].get1DArray(),
						values_.get(index)[10]);

				// face-4
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[19].get1DArray(),
						values_.get(index)[19]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[15].get1DArray(),
						values_.get(index)[15]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[16].get1DArray(),
						values_.get(index)[16]);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[11].get1DArray(),
						values_.get(index)[11]);

				// face-5
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[8].get1DArray(),
						values_.get(index)[8]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[9].get1DArray(),
						values_.get(index)[9]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[10].get1DArray(),
						values_.get(index)[10]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[11].get1DArray(),
						values_.get(index)[11]);

				// face-6
				polys_.insertNextCell(8);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[12].get1DArray(),
						values_.get(index)[12]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[13].get1DArray(),
						values_.get(index)[13]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[14].get1DArray(),
						values_.get(index)[14]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[15].get1DArray(),
						values_.get(index)[15]);
			}
		}

		// for tetrahedral elements
		else if (geo == Element3D.tetrahedral_) {

			// four noded
			if (pos.length == 4) {

				// face-1
				polys_.insertNextCell(3);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);

				// face-2
				polys_.insertNextCell(3);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);

				// face-3
				polys_.insertNextCell(3);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);

				// face-4
				polys_.insertNextCell(3);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
			}

			// ten noded
			else if (pos.length == 10) {

				// face-1
				polys_.insertNextCell(6);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[9].get1DArray(),
						values_.get(index)[9]);

				// face-2
				polys_.insertNextCell(6);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[8].get1DArray(),
						values_.get(index)[8]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[7].get1DArray(),
						values_.get(index)[7]);

				// face-3
				polys_.insertNextCell(6);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[9].get1DArray(),
						values_.get(index)[9]);
				polys_.insertCellPoint(pos[3].get1DArray(),
						values_.get(index)[3]);
				polys_.insertCellPoint(pos[8].get1DArray(),
						values_.get(index)[8]);

				// face-4
				polys_.insertNextCell(6);
				polys_.insertCellPoint(pos[0].get1DArray(),
						values_.get(index)[0]);
				polys_.insertCellPoint(pos[6].get1DArray(),
						values_.get(index)[6]);
				polys_.insertCellPoint(pos[2].get1DArray(),
						values_.get(index)[2]);
				polys_.insertCellPoint(pos[5].get1DArray(),
						values_.get(index)[5]);
				polys_.insertCellPoint(pos[1].get1DArray(),
						values_.get(index)[1]);
				polys_.insertCellPoint(pos[4].get1DArray(),
						values_.get(index)[4]);
			}
		}
	}

	/**
	 * Draws contour scalor for numerical results.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param option
	 *            The result option to be displayed.
	 * 
	 */
	private void drawContourScalor(SolidMAT owner, String option) {

		// set values array
		String[] values = { formatter(minVal_),
				formatter((maxVal_ + minVal_) / 2.0), formatter(maxVal_) };

		// set contour scalor
		owner.setContourScalor(true, option, ContourScalor.result_, values);
	}

	/**
	 * Formats given real number to scientific form and returns string.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	private String formatter(double number) {

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
	 * Computes the demanded result values and the extremes for the graphics.
	 * 
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 */
	private void computeValues(int option, int[] comp) {

		// initialize values vector
		values_ = new Vector<double[]>();
		DVec vec = new DVec(3);

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// three dimensional elements
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get three dimensional element
				Element3D e3D = (Element3D) e;

				// get geometry of element
				int geo = e3D.getGeometry();

				// get number of nodes
				int nn = e.getNodes().length;

				// set natural coordinates
				double[] elValues = new double[nn];
				double[] eps1 = new double[nn];
				double[] eps2 = new double[nn];
				double[] eps3 = new double[nn];

				// for hexahedral elements
				if (geo == Element3D.hexahedral_) {

					// eight noded
					if (nn == 8) {
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

					// twenty noded
					else if (nn == 20) {
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
						eps1[8] = 0.0;
						eps2[8] = 1.0;
						eps3[8] = 1.0;
						eps1[9] = -1.0;
						eps2[9] = 0.0;
						eps3[9] = 1.0;
						eps1[10] = 0.0;
						eps2[10] = -1.0;
						eps3[10] = 1.0;
						eps1[11] = 1.0;
						eps2[11] = 0.0;
						eps3[11] = 1.0;
						eps1[12] = 0.0;
						eps2[12] = 1.0;
						eps3[12] = -1.0;
						eps1[13] = -1.0;
						eps2[13] = 0.0;
						eps3[13] = -1.0;
						eps1[14] = 0.0;
						eps2[14] = -1.0;
						eps3[14] = -1.0;
						eps1[15] = 1.0;
						eps2[15] = 0.0;
						eps3[15] = -1.0;
						eps1[16] = 1.0;
						eps2[16] = 1.0;
						eps3[16] = 0.0;
						eps1[17] = -1.0;
						eps2[17] = 1.0;
						eps3[17] = 0.0;
						eps1[18] = -1.0;
						eps2[18] = -1.0;
						eps3[18] = 0.0;
						eps1[19] = 1.0;
						eps2[19] = -1.0;
						eps3[19] = 0.0;
					}
				}

				// for tetrahedral elements
				else if (geo == Element3D.tetrahedral_) {

					// four noded
					if (nn == 4) {
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

					// ten noded
					else if (nn == 10) {
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
						eps1[4] = 0.5;
						eps2[4] = 0.0;
						eps3[4] = 0.0;
						eps1[5] = 0.5;
						eps2[5] = 0.5;
						eps3[5] = 0.0;
						eps1[6] = 0.0;
						eps2[6] = 0.5;
						eps3[6] = 0.0;
						eps1[7] = 0.5;
						eps2[7] = 0.0;
						eps3[7] = 0.5;
						eps1[8] = 0.0;
						eps2[8] = 0.5;
						eps3[8] = 0.5;
						eps1[9] = 0.0;
						eps2[9] = 0.0;
						eps3[9] = 0.5;
					}
				}

				// loop over natural coordinates
				for (int j = 0; j < elValues.length; j++) {

					// element displacements
					if (option == PostVisualizer.elementDisp3D_) {

						// resultant displacement
						if (comp[0] == 6)
							elValues[j] = e3D.getDisplacement(eps1[j], eps2[j],
									eps3[j]).getSubVector(0, 2).l2Norm();

						// resultant rotation
						else if (comp[0] == 7)
							elValues[j] = e3D.getDisplacement(eps1[j], eps2[j],
									eps3[j]).getSubVector(3, 5).l2Norm();

						// component
						else
							elValues[j] = e3D.getDisplacement(eps1[j], eps2[j],
									eps3[j]).get(comp[0]);
					}

					// elastic strain
					else if (option == PostVisualizer.elasticStrains3D_) {

						// get strain tensor
						DMat strain = e3D.getStrain(eps1[j], eps2[j], eps3[j]);

						// normal strain
						if (comp[0] == 3) {
							vec.set(0, strain.get(0, 0));
							vec.set(1, strain.get(1, 1));
							vec.set(2, strain.get(2, 2));
							elValues[j] = vec.l2Norm();
						}

						// shear strain
						else if (comp[0] == 4) {
							vec.set(0, strain.get(0, 1));
							vec.set(1, strain.get(0, 2));
							vec.set(2, strain.get(1, 2));
							elValues[j] = vec.l2Norm();
						}

						// component
						else
							elValues[j] = strain.get(comp[0], comp[1]);
					}

					// stress
					else if (option == PostVisualizer.stresses3D_) {

						// get stress tensor
						DMat stress = e3D.getStress(eps1[j], eps2[j], eps3[j]);

						// normal stress
						if (comp[0] == 3) {
							vec.set(0, stress.get(0, 0));
							vec.set(1, stress.get(1, 1));
							vec.set(2, stress.get(2, 2));
							elValues[j] = vec.l2Norm();
						}

						// shear stress
						else if (comp[0] == 4) {
							vec.set(0, stress.get(0, 1));
							vec.set(1, stress.get(0, 2));
							vec.set(2, stress.get(1, 2));
							elValues[j] = vec.l2Norm();
						}

						// component
						else
							elValues[j] = stress.get(comp[0], comp[1]);
					}

					// principle strain
					else if (option == PostVisualizer.principalStrains3D_)
						elValues[j] = e3D.getPrincipalStrain(eps1[j], eps2[j],
								eps3[j], comp[0]);

					// principle stress
					else if (option == PostVisualizer.principalStresses3D_)
						elValues[j] = e3D.getPrincipalStress(eps1[j], eps2[j],
								eps3[j], comp[0]);

					// Mises stress
					else if (option == PostVisualizer.misesStress3D_)
						elValues[j] = e3D.getVonMisesStress(eps1[j], eps2[j],
								eps3[j]);

					// assign minimum value
					if (elValues[j] < elMin_)
						elMin_ = elValues[j];

					// assign maximum value
					if (elValues[j] > elMax_)
						elMax_ = elValues[j];
				}

				// set element values to values array
				values_.add(elValues);

				// assign minimum value
				if (elMin_ < minVal_)
					minVal_ = elMin_;

				// assign maximum value
				if (elMax_ > maxVal_)
					maxVal_ = elMax_;
			}
		}
	}

	/**
	 * Computes nodal average for the demanded result values.
	 */
	private void computeAverage() {

		// create vector for storing the node ids of elements
		Vector<int[]> ids = new Vector<int[]>();

		// store node id arrays of every element in a vector
		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// three dimensional elements
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get two dimensional element
				Element3D e3D = (Element3D) e;

				// get nodes of element
				Node[] nodes = e3D.getNodes();

				// initialize ids of nodes
				int[] nodeIds = new int[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodeIds.length; j++)
					nodeIds[j] = structure_.indexOfNode(nodes[j]);

				// set element node ids to ids vector
				ids.add(nodeIds);
			}
		}

		// compute and set mean values for each node
		// initialize node ids and node values vectors
		Vector<Integer> nodeIds = new Vector<Integer>();
		Vector<Double> nodeValues = new Vector<Double>();

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// loop over elements
			int k = 0;
			for (int j = 0; j < ids.size(); j++) {

				// get the index of node in the element
				int index = MathUtil.indexOf(ids.get(j), i);

				// set value if contains
				if (index != -1) {

					// increase capacity if necessary
					if (i >= nodeValues.size())
						nodeValues.setSize(i + 1);

					// set value
					Double val = nodeValues.get(i);
					if (val != null)
						nodeValues.set(i, val + values_.get(j)[index]);
					else
						nodeValues.set(i, values_.get(j)[index]);
					k++;
				}
			}

			// compute mean value
			if (k > 0) {
				nodeIds.add(i);
				nodeValues.set(i, nodeValues.get(i) / k);
			}
		}

		// set the mean values to elements
		// loop over node ids
		for (int i = 0; i < nodeIds.size(); i++) {

			// loop over element node ids
			for (int j = 0; j < ids.size(); j++) {

				// check if node is contained
				int index = MathUtil.indexOf(ids.get(j), nodeIds.get(i));
				if (index != -1)
					values_.get(j)[index] = nodeValues.get(nodeIds.get(i));
			}
		}
	}

	/**
	 * Computes scaled values of the drawing.
	 * 
	 * @param maxDistance
	 *            The maximum distnace between nodes.
	 * @param minDistance
	 *            The minimum distance between nodes.
	 */
	private void computeScaledValues(double maxDistance, double minDistance) {

		// compute radius of node
		double radius = minDistance / 70.0 + maxDistance / 1000.0;

		// compute displacement scale
		dispScale_ = 10.0 * radius;
	}

	/**
	 * Checks result option.
	 * 
	 * @param option
	 *            The result option to be drawn.
	 * @param comp
	 *            The component of the result to be displayed.
	 * @return Result option if available, null vice versa.
	 */
	private String checkOption(int option, int[] comp) {

		// initialize string to be returned
		String val = null;

		// set option
		switch (option) {

		// element displacements
		case PostVisualizer.elementDisp3D_: {

			// set option
			val = "3D element ";

			// set component
			switch (comp[0]) {
			case 0:
				val += "displacement 1";
				break;
			case 1:
				val += "displacement 2";
				break;
			case 2:
				val += "displacement 3";
				break;
			case 3:
				val += "rotation 1";
				break;
			case 4:
				val += "rotation 2";
				break;
			case 5:
				val += "rotation 3";
				break;
			case 6:
				val += "resultant displacement";
				break;
			case 7:
				val += "resultant rotation";
				break;
			}

			// return value
			return val;
		}

			// elastic strains
		case PostVisualizer.elasticStrains3D_: {

			// set option
			val = "3D element ";

			// normal strain
			if (comp[0] == 3)
				val += "resultant normal strain";

			// shear strain
			else if (comp[0] == 4)
				val += "resultant shear strain";

			// set normal component
			else if (comp[0] == comp[1])
				val += "normal strain " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// set shear component
			else if (comp[0] != comp[1] && comp[0] < 3)
				val += "shear strain " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// return value
			return val;
		}

			// stresses
		case PostVisualizer.stresses3D_: {

			// set option
			val = "3D element ";

			// normal strain
			if (comp[0] == 3)
				val += "resultant normal stress";

			// shear strain
			else if (comp[0] == 4)
				val += "resultant shear stress";

			// set normal component
			else if (comp[0] == comp[1])
				val += "normal stress " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// set shear component
			else if (comp[0] != comp[1] && comp[0] < 3)
				val += "shear stress " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// return value
			return val;
		}

			// principal strains
		case PostVisualizer.principalStrains3D_: {

			// set option
			val = "3D element ";

			// set component
			switch (comp[0]) {
			case Element.minPrincipal_:
				val += "min principal strain";
				break;
			case Element.midPrincipal_:
				val += "mid principal strain";
				break;
			case Element.maxPrincipal_:
				val += "max principal strain";
				break;
			}

			// return value
			return val;
		}

			// principal stresses
		case PostVisualizer.principalStresses3D_: {

			// set option
			val = "3D element ";

			// set component
			switch (comp[0]) {
			case Element.minPrincipal_:
				val += "min principal stress";
				break;
			case Element.midPrincipal_:
				val += "mid principal stress";
				break;
			case Element.maxPrincipal_:
				val += "max principal stress";
				break;
			}

			// return value
			return val;
		}

			// mises stress
		case PostVisualizer.misesStress3D_: {

			// set option
			val = "3D element mises stress";

			// return value
			return val;
		}

			// no option
		default:
			return val;
		}
	}
}
