package main;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * Class for popup menu for the canvas of the main frame.
 * 
 * @author Murat
 * 
 */
public class PopupMenu1 extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates popup menu.
	 * 
	 * @param owner
	 *            The owner frame of this popup menu.
	 */
	public PopupMenu1(SolidMAT owner) {

		// build menu bar
		super();

		// build the Redraw menu item
		JMenuItem menuItem = new JMenuItem(owner.action_);
		ImageIcon image = ImageHandler.createImageIcon("redraw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Redraw");
		menuItem.setActionCommand("viewMenu" + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.SHIFT_MASK));
		add(menuItem);

		// build the 3D Orbit menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("orbit.jpg");
		menuItem.setIcon(image);
		menuItem.setText("3D Orbit");
		menuItem.setActionCommand("viewMenu" + "3DOrbit");
		add(menuItem);

		// build the Zoom Window menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("zoomWindow.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Window");
		menuItem.setActionCommand("viewMenu" + "ZoomWindow");
		add(menuItem);

		// build the Zoom sub menu
		JMenu subMenu1 = new JMenu("Zoom...");
		add(subMenu1);

		// build the Zoom In menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("zoomIn.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom In");
		menuItem.setActionCommand("viewMenu" + "ZoomIn");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Zoom Out menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("zoomOut.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Out");
		menuItem.setActionCommand("viewMenu" + "ZoomOut");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Zoom Extends menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("zoomExtends.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Extents");
		menuItem.setActionCommand("viewMenu" + "ZoomExtents");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the 2D Views sub menu
		subMenu1 = new JMenu("2D Views");
		add(subMenu1);

		// build the Top View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("top.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Top View");
		menuItem.setActionCommand("viewMenu" + "TopView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Bottom View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("bottom.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Bottom View");
		menuItem.setActionCommand("viewMenu" + "BottomView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Left View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("left.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Left View");
		menuItem.setActionCommand("viewMenu" + "LeftView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Right View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("right.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Right View");
		menuItem.setActionCommand("viewMenu" + "RightView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Front View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("front.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Front View");
		menuItem.setActionCommand("viewMenu" + "FrontView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Back View menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("back.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Back View");
		menuItem.setActionCommand("viewMenu" + "BackView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Isometric Views sub menu
		subMenu1 = new JMenu("Isometric Views");
		add(subMenu1);

		// build the SW Isometric menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("sw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("SW Isometric View");
		menuItem.setActionCommand("viewMenu" + "SW");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the SE Isometric menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("se.jpg");
		menuItem.setIcon(image);
		menuItem.setText("SE Isometric View");
		menuItem.setActionCommand("viewMenu" + "SE");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the NE Isometric menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("ne.jpg");
		menuItem.setIcon(image);
		menuItem.setText("NE Isometric View");
		menuItem.setActionCommand("viewMenu" + "NE");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the NW Isometric menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("nw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("NW Isometric View");
		menuItem.setActionCommand("viewMenu" + "NW");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// add seperator
		addSeparator();

		// build the Undeformed Shape menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("undeformed.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Undeformed Shape");
		menuItem.setActionCommand("displayMenu" + "Undeformed");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.SHIFT_MASK));
		add(menuItem);

		// build the Load Assigns sub menu
		subMenu1 = new JMenu("Load Assigns");
		add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand("displayMenu" + "LoadAssignsNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Elements");
		menuItem.setActionCommand("displayMenu" + "LoadAssignsElements");
		subMenu1.add(menuItem);

		// build the Misc Assigns sub menu
		subMenu1 = new JMenu("Misc Assigns");
		add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand("displayMenu" + "MiscAssignsNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Elements");
		menuItem.setActionCommand("displayMenu" + "MiscAssignsElements");
		subMenu1.add(menuItem);

		// add seperator
		addSeparator();

		// build the Deformed Shape menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("deformed.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Deformed Shape...");
		menuItem.setActionCommand("displayMenu" + "Deformed");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				ActionEvent.SHIFT_MASK));
		add(menuItem);

		// build the Visual Results sub menu
		subMenu1 = new JMenu("Visual Results");
		image = ImageHandler.createImageIcon("visual.jpg");
		subMenu1.setIcon(image);
		add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand("displayMenu" + "VResultsNodes");
		subMenu1.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand("displayMenu" + "VResultsLines");
		subMenu1.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand("displayMenu" + "VResultsAreas");
		subMenu1.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand("displayMenu" + "VResultsSolids");
		subMenu1.add(menuItem);

		// build the History Plot sub menu
		subMenu1 = new JMenu("History Plot");
		image = ImageHandler.createImageIcon("historyPlot.jpg");
		subMenu1.setIcon(image);
		add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand("displayMenu" + "PlotNodes");
		subMenu1.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand("displayMenu" + "PlotLines");
		subMenu1.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand("displayMenu" + "PlotAreas");
		subMenu1.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand("displayMenu" + "PlotSolids");
		subMenu1.add(menuItem);

		// add seperator
		addSeparator();

		// build the Options menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("vizOptions.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Options...");
		menuItem.setActionCommand("displayMenu" + "Options");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.SHIFT_MASK));
		add(menuItem);

		// build the Preferences menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("preferences.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Preferences...");
		menuItem.setActionCommand("displayMenu" + "Preferences");
		add(menuItem);

		// add seperator
		addSeparator();

		// build the Help menu item
		menuItem = new JMenuItem(owner.action_);
		image = ImageHandler.createImageIcon("help.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Help");
		menuItem.setActionCommand("helpMenu" + "SolidMATHelp");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		add(menuItem);
	}
}
