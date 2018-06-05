package dialogs.assign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor; // import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import element.Element;
import element.Element1D;
import element.ElementLibrary;

import main.Commons;

// import main.ImageHandler;

/**
 * Class for Element Info Assign menu.
 * 
 * @author Murat
 * 
 */
public class AElementInfo2 extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Owner dialog of this dialog. */
	private AElementInfo1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public AElementInfo2(AElementInfo1 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Element Information", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panel
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		MyPanel1 panel2 = new MyPanel1();
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
		for (int i = 0; i < owner_.elementIds_.size(); i++) {

			// get index of node
			int index = owner_.elementIds_.get(i);

			// create table
			JTable tables = new JTable(new MyTableModel1(index));

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
		int height = 16 * owner_.elementIds_.size();
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
		JLabel[] labels = new JLabel[8];
		labels[0] = new JLabel("Element ID");
		labels[1] = new JLabel("Material");
		labels[2] = new JLabel("Sections");
		labels[3] = new JLabel("Local Axes");
		labels[4] = new JLabel("Masses");
		labels[5] = new JLabel("Springs");
		labels[6] = new JLabel("Mech. Loads");
		labels[7] = new JLabel("Temp. Loads");

		// add to panel
		for (int i = 0; i < labels.length; i++) {
			labels[i].setPreferredSize(new Dimension(100, 20));
			panel.add(labels[i]);
		}
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
		column = table.getColumnModel().getColumn(5);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for springs
		comboBox = new JComboBox();
		items = springInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(6);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for mechanical loads
		comboBox = new JComboBox();
		items = mechLoadInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(7);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);

		// set up combo for temperature loads
		comboBox = new JComboBox();
		items = tempLoadInfo(index);
		for (int i = 0; i < items.length; i++)
			comboBox.addItem(items[i]);
		column = table.getColumnModel().getColumn(8);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		column.setCellRenderer(renderer);
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
	 * Returns material name of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The name of material, if any.
	 */
	private String materialInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no material assigned
		if (e.getMaterial() == null)
			return "none";

		// material assigned
		else
			return e.getMaterial().getName();
	}

	/**
	 * Returns mechanical load names of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The name of section, if any.
	 */
	private String sectionInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no section assigned
		if (e.getSection() == null)
			return "none";

		// section assigned
		else
			return e.getSection().getName();
	}

	/**
	 * Returns local axes name of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The name of local axes, if any.
	 */
	private String localAxesInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// check if one dimensional
		if (e.getDimension() == ElementLibrary.oneDimensional_) {

			// get one dimensional element
			Element1D e1D = (Element1D) e;

			// no local axes assigned
			if (e1D.getLocalAxis() == null)
				return "none";

			// local axes assigned
			else
				return e1D.getLocalAxis().getName();
		}

		// not one dimensional
		else
			return "none";
	}

	/**
	 * Returns mass names of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The names of masses, if any.
	 */
	private String[] massInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no mass assigned
		if (e.getAdditionalMasses() == null) {
			String[] array = { "none" };
			return array;
		}

		// springs assigned
		else {

			// initialize array
			String[] array = new String[e.getAdditionalMasses().size()];

			// loop over masses
			for (int i = 0; i < array.length; i++)
				array[i] = e.getAdditionalMasses().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns spring names of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The names of springs, if any.
	 */
	private String[] springInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no section assigned
		if (e.getSprings() == null) {
			String[] array = { "none" };
			return array;
		}

		// springs assigned
		else {

			// initialize array
			String[] array = new String[e.getSprings().size()];

			// loop over springs
			for (int i = 0; i < array.length; i++)
				array[i] = e.getSprings().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns mechanical load names of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The names of mechanical loads, if any.
	 */
	private String[] mechLoadInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no section assigned
		if (e.getAllMechLoads() == null) {
			String[] array = { "none" };
			return array;
		}

		// mechanical loads assigned
		else {

			// initialize array
			String[] array = new String[e.getAllMechLoads().size()];

			// loop over mechanical loads
			for (int i = 0; i < array.length; i++)
				array[i] = e.getAllMechLoads().get(i).getName();

			// return
			return array;
		}
	}

	/**
	 * Returns temperature load names of the element at the given index.
	 * 
	 * @param index
	 *            The index of element to be processed.
	 * 
	 * @return The names of temperature loads, if any.
	 */
	private String[] tempLoadInfo(int index) {

		// get element
		Element e = owner_.owner_.structure_.getElement(index);

		// no section assigned
		if (e.getAllTempLoads() == null) {
			String[] array = { "none" };
			return array;
		}

		// mechanical loads assigned
		else {

			// initialize array
			String[] array = new String[e.getAllTempLoads().size()];

			// loop over temperature loads
			for (int i = 0; i < array.length; i++)
				array[i] = e.getAllTempLoads().get(i).getName();

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
	class MyTableModel1 extends AbstractTableModel {

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
		MyTableModel1(int index) {
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
			if (col > 4)
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
			String[] headers = new String[8];
			headers[0] = "Element ID";
			headers[1] = "Material";
			headers[2] = "Section";
			headers[3] = "Local Axes";
			headers[4] = "Masses";
			headers[5] = "Springs";
			headers[6] = "Mech. Loads";
			headers[7] = "Temp. Loads";

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
			Object[][] data = new Object[1][8];
			data[0][0] = new Integer(index);
			data[0][1] = materialInfo(index);
			data[0][2] = sectionInfo(index);
			data[0][3] = localAxesInfo(index);
			data[0][4] = massInfo(index)[0];
			data[0][5] = springInfo(index)[0];
			data[0][6] = mechLoadInfo(index)[0];
			data[0][7] = tempLoadInfo(index)[0];

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
	class MyPanel1 extends JPanel implements Scrollable {

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