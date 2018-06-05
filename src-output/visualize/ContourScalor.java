package visualize;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.Commons;
import main.SolidMAT;

/**
 * Class for displaying contour scalor.
 * 
 * @author Murat
 * 
 */
public class ContourScalor extends JDialog implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/** Static variable for the type of contour scalor. */
	public static final int result_ = 0, assignment_ = 1, deformed_ = 2;

	private JPanel panel2_, panel3_;

	private Point p_ = new Point();

	/** The owner of this dialog. */
	protected SolidMAT owner1_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner1
	 *            The owner of this dialog.
	 * @param owner2
	 *            Frame to be the owner of this dialog.
	 * @param name
	 *            The name of values to be displayed.
	 * @param type
	 *            The type of contour scalor.
	 * @param objects
	 *            Array storing the objects to be displayed.
	 */
	public ContourScalor(SolidMAT owner1, JFrame owner2, String name, int type,
			Object[] objects) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner2, name);
		setUndecorated(true);
		owner1_ = owner1;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panel
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		panel1.setBackground(Color.WHITE);

		// build sub-panels
		Object[] names = new Object[1];
		names[0] = name;
		panel3_ = new ContourPanel2(this, deformed_, names);
		if (type == ContourScalor.assignment_)
			panel2_ = new ContourPanel1(type, objects);
		else
			panel2_ = new ContourPanel2(this, type, objects);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3_, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel2_, 1, 0, 1, 1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);

		// set listeners to components
		panel2_.addMouseListener(this);
		panel2_.addMouseMotionListener(this);
		panel3_.addMouseListener(this);
		panel3_.addMouseMotionListener(this);

		// visualize
		visualize();
	}

	/**
	 * Sets visualization for frame.
	 */
	private void visualize() {

		// pack frame, do not allow resizing, set default close operation as
		// hide and set location
		pack();
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/**
	 * When left mouse button is pressed on panel2, this method computes the
	 * location of cursor relative to frame.
	 */
	public void mousePressed(MouseEvent arg0) {

		// for panel2-3
		if (arg0.getSource().equals(panel2_)
				|| arg0.getSource().equals(panel3_)) {

			// check if left mouse button clicked
			if (SwingUtilities.isLeftMouseButton(arg0)) {

				// check if its a single-click
				if (arg0.getClickCount() == 1) {

					// get location of frame
					Point p1 = getLocationOnScreen();

					// get location of cursor
					Point p2 = arg0.getPoint();

					// compute location of cursor relative to frame
					p_.setLocation(p1.x - p2.x, p1.y - p2.y);
				}
			}
		}

	}

	/**
	 * Renews the location of frame according to the new location of cursor.
	 */
	public void mouseDragged(MouseEvent e) {

		// for panel2
		if (e.getSource().equals(panel2_) || e.getSource().equals(panel3_)) {

			// check if left mouse button clicked
			if (SwingUtilities.isLeftMouseButton(e)) {

				// get location of cursor
				Point p2 = e.getPoint();

				// compute location of frame relative to cursor
				setLocation(p2.x + p_.x, p2.y + p_.y);
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
}