package dialogs.help;

import java.awt.BorderLayout;
import java.awt.Dimension;


// import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

// import main.ImageHandler;
import main.SolidMAT;
import main.Commons;

/**
 * Class for Key Assist Help menu.
 * 
 * @author Murat Artim
 * 
 */
public class KeyAssist1 extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public KeyAssist1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Key Assistance", true);

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panel
		JPanel panel1 = Commons.getPanel("", Commons.gridbag_);

		// create table
		JTable table = new JTable(new MyTableModel2());

		// set column widths
		setColumnWidths(table);

		// set table unselectable
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(table);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// add scrollpane to panel
		Commons.addComponent(panel1, scrollpane1, 0, 0, 1, 1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets column widths of table.
	 * 
	 * @param table
	 *            The table to be processed.
	 */
	private void setColumnWidths(JTable table) {

		// set dimension for all table
		Dimension d = new Dimension(200, 300);
		table.setPreferredScrollableViewportSize(d);

		// get column count
		int n = table.getColumnModel().getColumnCount();

		// loop over columns
		for (int i = 0; i < n; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(d.width / n);
	}

	/**
	 * Inner class for creating the data model for the table.
	 * 
	 * @author Murat Artim
	 * 
	 */
	class MyTableModel2 extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		/** Array containing the headers of table. */
		private String[] headers_ = getHeaders();

		/** Array containing the data of table. */
		private Object[][] data_ = getData();

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
		 * Creates and returns headers array for table.
		 * 
		 * @return Array storing the headers of table.
		 */
		private String[] getHeaders() {

			// create array
			String[] headers = { "Function", "Key" };

			// return
			return headers;
		}

		/**
		 * Creates and returns data array for the table.
		 * 
		 * @return Array storing the data for the table.
		 */
		private String[][] getData() {

			// create array
			String[][] data = new String[29][2];

			// Ctrl keys
			data[0][0] = "New";
			data[0][1] = "Ctrl+N";
			data[1][0] = "Open";
			data[1][1] = "Ctrl+O";
			data[2][0] = "Save";
			data[2][1] = "Ctrl+S";
			data[3][0] = "Save Image";
			data[3][1] = "Ctrl+I";
			data[4][0] = "Exit";
			data[4][1] = "Ctrl+X";
			data[5][0] = "Help";
			data[5][1] = "Ctrl+H";

			// Shift keys
			data[6][0] = "Redraw";
			data[6][1] = "Shift+R";
			data[7][0] = "Zoom In";
			data[7][1] = "Shift+I";
			data[8][0] = "Zoom Out";
			data[8][1] = "Shift+O";
			data[9][0] = "Zoom Extents";
			data[9][1] = "Shift+X";
			data[10][0] = "Top View";
			data[10][1] = "Shift+F1";
			data[11][0] = "Bottom View";
			data[11][1] = "Shift+F2";
			data[12][0] = "Left View";
			data[12][1] = "Shift+F3";
			data[13][0] = "Right View";
			data[13][1] = "Shift+F4";
			data[14][0] = "Front View";
			data[14][1] = "Shift+F5";
			data[15][0] = "Back View";
			data[15][1] = "Shift+F6";
			data[16][0] = "SW Isometric View";
			data[16][1] = "Shift+F7";
			data[17][0] = "SE Isometric View";
			data[17][1] = "Shift+F8";
			data[18][0] = "NE Isometric View";
			data[18][1] = "Shift+F9";
			data[19][0] = "NW Isometric View";
			data[19][1] = "Shift+F10";
			data[20][0] = "Define Groups";
			data[20][1] = "Shift+G";
			data[21][0] = "Add Node";
			data[21][1] = "Shift+N";
			data[22][0] = "Add Element";
			data[22][1] = "Shift+E";
			data[23][0] = "Check Model";
			data[23][1] = "Shift+F11";
			data[24][0] = "Run";
			data[24][1] = "Shift+F12";
			data[25][0] = "Undeformed Shape";
			data[25][1] = "Shift+U";
			data[26][0] = "Deformed Shape";
			data[26][1] = "Shift+D";
			data[27][0] = "Options";
			data[27][1] = "Shift+P";
			data[28][0] = "Key Assistance";
			data[28][1] = "Shift+K";

			// return
			return data;
		}
	}
}
