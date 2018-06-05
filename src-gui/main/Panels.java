package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Class for creating panels for the main frame.
 * 
 * @author Murat Artim
 * 
 */
public class Panels {

	/**
	 * Builds panels for the main frame.
	 * 
	 * @param owner
	 *            The owner frame of panels.
	 */
	public static void buildPanels(SolidMAT owner) {

		// create color for background
		Color c = new Color(240, 240, 240);

		// create panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		owner.statusbar_ = new Statusbar();
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
		JPanel panel6 = new JPanel();
		panel1.setBackground(c);
		panel2.setBackground(c);
		panel4.setBackground(c);
		panel5.setBackground(c);
		panel6.setBackground(c);

		// set layout
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
		panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel6.setLayout(new BoxLayout(panel6, BoxLayout.Y_AXIS));

		// add components to sub-panels
		panel1.add(owner.toolbars_.getToolbar(Toolbars.file1_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.file2_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.view1_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.view2_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.view3_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.view4_));
		panel1.add(owner.toolbars_.getToolbar(Toolbars.help1_));
		panel2.add(owner.toolbars_.getToolbar(Toolbars.model1_));
		panel2.add(owner.toolbars_.getToolbar(Toolbars.model2_));
		panel2.add(owner.toolbars_.getToolbar(Toolbars.model3_));
		panel2.add(owner.toolbars_.getToolbar(Toolbars.model4_));
		panel2.add(owner.toolbars_.getToolbar(Toolbars.model5_));
		panel4.add(owner.toolbars_.getToolbar(Toolbars.library1_));
		panel4.add(owner.toolbars_.getToolbar(Toolbars.library2_));
		panel4.add(owner.toolbars_.getToolbar(Toolbars.library3_));
		panel4.add(owner.toolbars_.getToolbar(Toolbars.library4_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.assign1_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.assign2_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.analysis1_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.display1_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.display2_));
		panel5.add(owner.toolbars_.getToolbar(Toolbars.display3_));

		// add sub-panels to main panel
		panel6.add(panel1);
		panel6.add(panel5);

		// set listeners to main panels
		panel6.addMouseListener(owner);
		panel2.addMouseListener(owner);
		owner.statusbar_.addMouseListener(owner);
		panel4.addMouseListener(owner);

		// add main panels to main frame
		owner.viewer_.getContentPane().add(panel6, BorderLayout.NORTH);
		owner.viewer_.getContentPane().add(panel2, BorderLayout.WEST);
		owner.viewer_.getContentPane()
				.add(owner.statusbar_, BorderLayout.SOUTH);
		owner.viewer_.getContentPane().add(panel4, BorderLayout.EAST);
	}
}
