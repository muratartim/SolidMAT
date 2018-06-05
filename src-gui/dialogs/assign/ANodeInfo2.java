package dialogs.assign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor; // import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.Scrollable;
import java.awt.FlowLayout;
import javax.swing.JLabel;

import node.Node;

import main.Commons;

// import main.ImageHandler;

/**
 * Class for Node Info Assign menu.
 * 
 * @author Murat Artim
 * 
 */
public class ANodeInfo2 extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Owner dialog of this dialog. */
	private ANodeInfo1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public ANodeInfo2(ANodeInfo1 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Node Information", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panel
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		MyPanel panel2 = new MyPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		// create headers
		createHeaders(panel1);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(panel2);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// loop over node ids
		for (int i = 0; i < owner_.nodeIds_.size(); i++) {

			// get index of node
			int index = owner_.nodeIds_.get(i);

			// create table
			JTable tables = new JTable(new MyTableModel(index));

			// setup columns of table
			setUpColumns(tables, index);

			// set column widths
			setColumnWidths(tables);

			// set table unselectable
			tables.setRowSelectionAllowed(false);
			tables.setColumnSelectionAllowed(false);

			// add table to panel
			panel2.add(tables);
		}

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", scrollpane1);

		// visualize
		Commons.visualize(this);

		// check height of panel
		int height = 16 * owner_.nodeIds_.size();
		if (scrollpane1.getHeight() > height) {
			scrollpane1.setPreferredSize(new Dimension(900, height));
			pack();
		}
	}

	/**
	 * Creates headers for table.
	 * 
	 * @param panel
	 *            The panel for the headers to be placed.
	 */
	private void createHeaders(JPanel panel) {

		// create labels
		JLabel[] labels = new JLabel[9];
		labels[0] = new JLabel("Node ID");
		labels[1] = new JLabel("Local Axes");
		labels[2] = new JLabel("Constraint");
		labels[3] = new JLabel("Masses");
		labels[4] = new JLabel("Springs");
		labels[5] = new JLabel("Mech. Loads");
		labels[6] = new JLabel("Disp. Loads");
		labels[7] = new JLabel("Initial Disps.");
		labels[8] = new JLabel("Initial Velos.");

		// add to panel
		for (int i = 0; i < labels.length; i++) {
			labels[i].setPreferredSize(new Dimension(100, 20));
			panel.add(labels[i]);
		}
	}

	/**
	 * Sets column widths of table.
	 * 
	 * @param table
	 *            The table to be processed.
	 */
	private void setColumnWidths(JTable table) {

		// set dimension for all table
		Dimension d = new Dimension(900, 20);
		table.setPreferredScrollableViewportSize(d);

		// get column count
		int n = table.getColumnModel().getColumnCount();

		// loop over columns
		for (int i = 0; i < n; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(d.width / n);
	}

	/**
	 * Sets up column data and renderers of table columns.
	 * 
	 * @param table
	 *            The table to be processed.
	 * @param index
	 *            The index of node to be processed.
	 */
	private void setUpColumns(JTable table, int index) {

		// initialize variables
		JComboBox comboBox;
		TableColumn column;
		String[] items;
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to extract info...");

		// set up combo for masses
		comboBox = new JComboBox();
		items = massInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(3);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for springs
		comboBox = new JComboBox();
		items = springInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(4);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for mechanical loads
		comboBox = new JComboBox();
		items = mechLoadInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(5);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for displacement loads
		comboBox = new JComboBox();
		items = dispLoadInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(6);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for initial displacements
		comboBox = new JComboBox();
		items = initialDispInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(7);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for initial velocities
		comboBox = new JComboBox();
		items = initialVeloInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(8);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);
	}

	/**
	 * Returns local axes name of the node at the given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return The name of local axes, if any.
	 */
	private String localAxesInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no local axes assigned
		if (node.getLocalAxis() == null)
			return "default";

		// local axes assigned
		else
			return node.getLocalAxis().getName();
	}

	/**
	 * Returns constraint name of the node at the given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return The name of constraint, if any.
	 */
	private String constraintInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no constraint assigned
		if (node.getAppliedConstraint() == null)
			return "none";

		// constraint assigned
		else
			return node.getAppliedConstraint().getName();
	}

	/**
	 * Returns names of assigned masses (if any) to the node at the given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned masses, if any.
	 */
	private String[] massInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no masses applied
		if (node.getMasses() == null) {
			String[] array = { "none" };
			return array;
		}

		// masses assigned
		else {

			// initialize array
			String[] array = new String[node.getMasses().size()];

			// loop over masses
			for (int i = 0; i < array.length; i++)
				array[i] = node.getMasses().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns names of assigned springs (if any) to the node at the given
	 * index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned springs, if any.
	 */
	private String[] springInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no springs applied
		if (node.getSprings() == null) {
			String[] array = { "none" };
			return array;
		}

		// springs assigned
		else {

			// initialize array
			String[] array = new String[node.getSprings().size()];

			// loop over springs
			for (int i = 0; i < array.length; i++)
				array[i] = node.getSprings().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns names of assigned mechanical loads (if any) to the node at the
	 * given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned mechanical loads, if any.
	 */
	private String[] mechLoadInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no mechanical loads assigned
		if (node.getAllMechLoads() == null) {
			String[] array = { "none" };
			return array;
		}

		// mechanical loads assigned
		else {

			// initialize array
			String[] array = new String[node.getAllMechLoads().size()];

			// loop over mechanical loads
			for (int i = 0; i < array.length; i++)
				array[i] = node.getAllMechLoads().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns names of assigned displacement loads (if any) to the node at the
	 * given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned displacement loads, if
	 *         any.
	 */
	private String[] dispLoadInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no displacement loads assigned
		if (node.getAllDispLoads() == null) {
			String[] array = { "none" };
			return array;
		}

		// displacement loads assigned
		else {

			// initialize array
			String[] array = new String[node.getAllDispLoads().size()];

			// loop over displacement loads
			for (int i = 0; i < array.length; i++)
				array[i] = node.getAllDispLoads().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns names of assigned initial displacements (if any) to the node at
	 * the given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned initial displacements, if
	 *         any.
	 */
	private String[] initialDispInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no initial displacements assigned
		if (node.getAllInitialDisp() == null) {
			String[] array = { "none" };
			return array;
		}

		// initial displacements assigned
		else {

			// initialize array
			String[] array = new String[node.getAllInitialDisp().size()];

			// loop over initial displacements
			for (int i = 0; i < array.length; i++)
				array[i] = node.getAllInitialDisp().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns names of assigned initial velocities (if any) to the node at the
	 * given index.
	 * 
	 * @param index
	 *            The index of node to be processed.
	 * 
	 * @return Array containing the names of assigned initial velocities, if
	 *         any.
	 */
	private String[] initialVeloInfo(int index) {

		// get node
		Node node = owner_.owner_.structure_.getNode(index);

		// no initial velocities assigned
		if (node.getAllInitialVelo() == null) {
			String[] array = { "none" };
			return array;
		}

		// initial velocities assigned
		else {

			// initialize array
			String[] array = new String[node.getAllInitialDisp().size()];

			// loop over initial velocities
			for (int i = 0; i < array.length; i++)
				array[i] = node.getAllInitialVelo().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Inner class for creating the data model for the table.
	 * 
	 * @author Murat Artim
	 * 
	 */
	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		/** Array containing the headers of table. */
		private String[] headers_;

		/** Array containing the data of table. */
		private Object[][] data_;

		/**
		 * Creates data model for table.
		 * 
		 * @param index
		 *            Index of node for data generation.
		 */
		MyTableModel(int index) {
			headers_ = getHeaders(index);
			data_ = getData(index);
		}

		/**
		 * Returns column count of table.
		 */
		public int getColumnCount() {
			return headers_.length;
		}

		/**
		 * Returns row count of table.
		 */
		public int getRowCount() {
			return data_.length;
		}

		/**
		 * Returns header of column.
		 */
		public String getColumnName(int col) {
			return headers_[col];
		}

		/**
		 * Returns the data in the demanded cell.
		 */
		public Object getValueAt(int row, int col) {
			return data_[row][col];
		}

		/**
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/**
		 * Enables comboboxes to be visualized.
		 */
		public boolean isCellEditable(int row, int col) {
			if (col > 2)
				return true;
			return false;
		}

		/**
		 * Unused method.
		 */
		public void setValueAt(Object value, int row, int col) {
			data_[row][col] = value;
			fireTableCellUpdated(row, col);
		}

		/**
		 * Creates and returns array storing the headers of table.
		 * 
		 * @return Array storing the headers of table.
		 */
		private String[] getHeaders(int index) {

			// create array
			String[] headers = new String[9];
			headers[0] = "Node ID";
			headers[1] = "Local Axes";
			headers[2] = "Constraint";
			headers[3] = "Masses";
			headers[4] = "Springs";
			headers[5] = "Mech. Loads";
			headers[6] = "Disp. Loads";
			headers[7] = "Initial Disps.";
			headers[8] = "Initial Velos.";

			// return
			return headers;
		}

		/**
		 * Creates and returns array storing the data of table.
		 * 
		 * @return Array storing the data of table.
		 */
		private Object[][] getData(int index) {

			// create array
			Object[][] data = new Object[1][9];
			data[0][0] = new Integer(index);
			data[0][1] = localAxesInfo(index);
			data[0][2] = constraintInfo(index);
			data[0][3] = massInfo(index)[0];
			data[0][4] = springInfo(index)[0];
			data[0][5] = mechLoadInfo(index)[0];
			data[0][6] = dispLoadInfo(index)[0];
			data[0][7] = initialDispInfo(index)[0];
			data[0][8] = initialVeloInfo(index)[0];

			// return
			return data;
		}
	}

	/**
	 * Inner class for creating panel with scrollable property.
	 * 
	 * @author Murat Artim
	 * 
	 */
	class MyPanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = 1L;

		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(900, 400);
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 30;
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 10;
		}
	}
}
