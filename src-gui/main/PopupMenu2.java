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
package main;


import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

/**
 * Class for popup menu for the panels of the main frame.
 * 
 * @author Murat
 * 
 */
public class PopupMenu2 extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Array storing the toolbars of main frame. */
	private JToolBar[] toolbars_;

	/**
	 * Creates popup menu.
	 * 
	 * @param owner
	 *            The owner frame of this popup menu.
	 */
	public PopupMenu2(SolidMAT owner) {

		// build menu bar
		super();

		// get toolbars of main frame
		toolbars_ = owner.toolbars_.getToolbars();

		// loop over toolbars
		for (int i = 0; i < toolbars_.length; i++) {

			// create menu item
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(toolbars_[i]
					.getName());

			// set selected if visible
			item.setSelected(toolbars_[i].isVisible());

			// add action listener and command
			item.addActionListener(this);
			item.setActionCommand(Integer.toString(i));

			// add to menu
			add(item);
		}

		// add seperator
		addSeparator();

		// build the Toolbars menu item
		JMenuItem menuItem = new JMenuItem(owner.action_);
		ImageIcon image = ImageHandler.createImageIcon("toolbars.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Toolbars...");
		menuItem.setActionCommand("viewMenu" + "Toolbars");
		add(menuItem);
	}

	/**
	 * Sets selected toolbar visible or not depending on its current state.
	 */
	public void actionPerformed(ActionEvent e) {

		// get action command
		int command = Integer.parseInt(e.getActionCommand());

		// set selected toolbar visible/unvisible
		toolbars_[command].setVisible(!toolbars_[command].isVisible());
	}
}
