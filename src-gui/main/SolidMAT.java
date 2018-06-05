package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import inf.v3d.obj.Sphere;
import inf.v3d.view.Viewer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data.InputData;
import dialogs.file.FileHandler1;

import analysis.Structure;

import visualize.ContourScalor;
import visualize.PostVisualizer;
import visualize.PreVisualizer;

/**
 * Class for main frame.
 * 
 * @author Murat
 * 
 */
public class SolidMAT implements WindowListener, MouseListener {

	/** The viewer object of main frame. */
	public Viewer viewer_ = new Viewer();

	/** Used for storing and managing input data objects. */
	public InputData inputData_ = new InputData();

	/** The structure object of main frame. */
	public Structure structure_ = new Structure();

	/** The pre-visualizer of the main frame. */
	public PreVisualizer preVis_ = new PreVisualizer();

	/** The post-visualizer of the main frame. */
	public PostVisualizer postVis_ = new PostVisualizer();

	/** The contour scalor of main frame. */
	public ContourScalor scalor_;

	/** The action handler of menu bar and toolbar items. */
	public ActionHandler action_ = new ActionHandler(this);

	/** The toolbars of main frame. */
	public Toolbars toolbars_;

	/** The statusbar of main frame. */
	public Statusbar statusbar_;

	/** The formatter of main frame. */
	public Formatter formatter_ = new Formatter();

	/** The path for the current file. */
	public String path_ = null;

	/**
	 * Sets structure to the main frame.
	 * 
	 * @param s
	 *            The structure to be set.
	 */
	public void setStructure(Structure s) {
		structure_ = s;
	}

	/**
	 * Sets input data to the main frame.
	 * 
	 * @param data
	 *            Input data to be set.
	 */
	public void setInputData(InputData data) {
		inputData_ = data;
	}

	/**
	 * Draws structure for pre-visualizer.
	 * 
	 */
	public void drawPre() {

		// close contour scalor
		setContourScalor(false, null, null, null);

		// disable canvas
		viewer_.getCanvas().setEnabled(false);

		// clear viewer
		viewer_.clear();

		// get # of nodes and elements
		int nn = structure_.getNumberOfNodes();
		int ne = structure_.getNumberOfElements();

		// get visibility of nodes and elements
		boolean vn = preVis_.areNodesVisible();
		boolean ve = preVis_.areElementsVisible();

		// no nodes and elements
		if (nn == 0 && ne == 0) {

			// create sphere
			Sphere sphere = new Sphere();
			sphere.setRadius(0);
		}

		// no elements but nodes available
		else if (nn != 0 && ne == 0) {

			// nodes are visible
			if (vn) {

				// set structure to pre-visualizer
				preVis_.setStructure(structure_);

				// draw
				preVis_.draw(this);
			}

			// nodes are not visible
			else {

				// create sphere
				Sphere sphere = new Sphere();
				sphere.setRadius(0);
			}
		}

		// no nodes but elements available
		else if (nn == 0 && ne != 0) {

			// elements are visible
			if (ve) {

				// set structure to pre-visualizer
				preVis_.setStructure(structure_);

				// draw
				preVis_.draw(this);
			}

			// elements are not visible
			else {

				// create sphere
				Sphere sphere = new Sphere();
				sphere.setRadius(0);
			}
		}

		// nodes and elements are available
		else {

			// any visibility is open
			if (vn || ve) {

				// set structure to pre-visualizer
				preVis_.setStructure(structure_);

				// draw
				preVis_.draw(this);
			}

			// no visibility is open
			else {

				// create sphere
				Sphere sphere = new Sphere();
				sphere.setRadius(0);
			}
		}

		// enable canvas
		viewer_.getCanvas().setEnabled(true);
	}

	/**
	 * Draws structure for post-visualizer.
	 * 
	 * @param isDeformed
	 *            Parameter for drawing deformed or undeformed shape.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 * @param step
	 *            The step number for drawing deformed shape.
	 */
	public void drawPost(boolean isDeformed, int option, int[] comp, int step) {

		// close contour scalor
		setContourScalor(false, null, null, null);

		// disable canvas
		viewer_.getCanvas().setEnabled(false);

		// clear viewer
		viewer_.clear();

		// get structure properties
		int nn = structure_.getNumberOfNodes();
		int ne = structure_.getNumberOfElements();

		// no nodes or elements
		if (nn == 0 || ne == 0) {

			// create sphere
			Sphere sphere = new Sphere();
			sphere.setRadius(0);
		}

		// nodes and elements avaible
		else {

			// set structure to post-visualizer
			postVis_.setStructure(structure_);

			// undeformed shape demanded
			if (isDeformed == false)
				postVis_.drawUndeformedShape(this, option, comp);

			// deformed shape demanded
			else
				postVis_.drawDeformedShape(this, option, comp, true, step);
		}

		// enable canvas
		viewer_.getCanvas().setEnabled(true);
	}

	/**
	 * Sets contour scalor visible or unvisible depending on the set parameter.
	 * If set is False, other parameters are not referenced.
	 * 
	 * @param set
	 *            True if contour scalor is wanted to be opened, False vice
	 *            versa.
	 * @param name
	 *            The name of values to be displayed.
	 * @param type
	 *            The type of contour scalor.
	 * @param objects
	 *            Array storing the objects to be displayed.
	 */
	public void setContourScalor(boolean set, String name, Integer type,
			Object[] objects) {

		// open contour scalor
		if (set) {

			// create and set visible
			scalor_ = new ContourScalor(this, viewer_, name, type, objects);
			scalor_.setVisible(set);
		}

		// close contour scalor
		else {

			// check if null
			if (scalor_ != null)
				scalor_.setVisible(set);
		}
	}

	/**
	 * Sets title for the main frame.
	 * 
	 */
	public void setName() {
		statusbar_.setText(path_);
	}

	/**
	 * Initializes viewer.
	 * 
	 */
	public void start() {

		// shut down warning display
		viewer_.getCanvas().GetRenderer().GetVTKWindow()
				.GlobalWarningDisplayOff();

		// set icon
		ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		viewer_.setIconImage(image.getImage());

		// set default background color as white
		viewer_.getCanvas().GetRenderer().SetBackground(1.0, 1.0, 1.0);

		// set menu bar
		MenuBar menuBar = new MenuBar(this);
		viewer_.setJMenuBar(menuBar);

		// set toolbars
		toolbars_ = new Toolbars(this);

		// set panels
		Panels.buildPanels(this);

		// set title
		viewer_.setTitle("SolidMAT");

		// draw
		drawPre();

		// unable window closing
		viewer_.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// add window listener
		viewer_.addWindowListener(this);

		// add mouse listeners
		viewer_.getCanvas().addMouseListener(this);

		// set visible
		viewer_.setVisible(true);
	}

	/**
	 * Handles mouse button release events. Displays related popup menu when the
	 * user releases mouse buttons.
	 */
	public void mouseReleased(MouseEvent e) {

		// for canvas
		if (e.getSource().equals(viewer_.getCanvas())) {

			// check if left mouse button clicked
			if (SwingUtilities.isLeftMouseButton(e)) {

				// check if its a double-click
				if (e.getClickCount() == 2) {

					// create popup menu
					PopupMenu1 popup = new PopupMenu1(this);

					// show
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}

		// for panels
		else {

			// check if right mouse button clicked
			if (SwingUtilities.isRightMouseButton(e)) {

				// create popup menu
				PopupMenu2 popup = new PopupMenu2(this);

				// show
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * Handles window closing event. Displays message for saving the current
	 * model if necessary when the user clicks to close the main frame.
	 */
	public void windowClosing(WindowEvent arg0) {

		// structure, input data or path exists
		if (structure_.isEmpty() == false || inputData_.isEmpty() == false
				|| path_ != null) {

			// display confirmation message
			int confirm = JOptionPane.showConfirmDialog(viewer_,
					"Save changes?", "Data confirmation",
					JOptionPane.YES_NO_OPTION);

			// save as/save
			if (confirm == JOptionPane.YES_OPTION) {

				// there is no path
				if (path_ == null) {

					// save as
					FileHandler1.saveFileAs(this);

					// exit
					System.exit(0);
				}

				// path exists
				else {

					// save
					FileHandler1.saveFile(this);

					// exit
					System.exit(0);
				}
			}

			// exit
			else
				System.exit(0);
		}

		// exit
		else
			System.exit(0);
	}

	/**
	 * Unused method.
	 */
	public void windowActivated(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void windowDeactivated(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void windowDeiconified(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void windowIconified(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void windowOpened(WindowEvent arg0) {
	}

	/**
	 * Unused method.
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Unused method.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Unused method.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Unused method.
	 */
	public void mousePressed(MouseEvent e) {
	}
}
