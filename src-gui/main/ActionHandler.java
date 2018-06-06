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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import dialogs.analysis.AnalysisOptions1;
import dialogs.analysis.CheckModel1;
import dialogs.analysis.RunAnalysis1;
import dialogs.assign.AAreaMass1;
import dialogs.assign.AAreaMaterial1;
import dialogs.assign.AAreaMechLoad1;
import dialogs.assign.AAreaSection1;
import dialogs.assign.AAreaSpring1;
import dialogs.assign.AAreaTempLoad1;
import dialogs.assign.AConstraint1;
import dialogs.assign.AElementInfo1;
import dialogs.assign.AInitialDisp1;
import dialogs.assign.AInitialVelo1;
import dialogs.assign.ALineLocalAxis1;
import dialogs.assign.ALineMass1;
import dialogs.assign.ALineMaterial1;
import dialogs.assign.ALineMechLoad1;
import dialogs.assign.ALineSection1;
import dialogs.assign.ALineSpring1;
import dialogs.assign.ALineTempLoad1;
import dialogs.assign.ANodalDispLoad1;
import dialogs.assign.ANodalLocalAxis1;
import dialogs.assign.ANodalMass1;
import dialogs.assign.ANodalMechLoad1;
import dialogs.assign.ANodalSpring1;
import dialogs.assign.ANodeInfo1;
import dialogs.assign.ASolidMass1;
import dialogs.assign.ASolidMaterial1;
import dialogs.assign.ASolidMechLoad1;
import dialogs.assign.ASolidSpring1;
import dialogs.assign.ASolidTempLoad1;
import dialogs.display.DisplayAreaPlot1;
import dialogs.display.DisplayDeformedShape1;
import dialogs.display.DisplayElementLoads1;
import dialogs.display.DisplayElementMisc1;
import dialogs.display.DisplayFunctionPlot1;
import dialogs.display.DisplayLinePlot1;
import dialogs.display.DisplayNodalLoads1;
import dialogs.display.DisplayNodeMisc1;
import dialogs.display.DisplayNodePlot1;
import dialogs.display.DisplayOptions1;
import dialogs.display.DisplaySolidPlot1;
import dialogs.display.DisplayTAreaResult1;
import dialogs.display.DisplayTLineResult1;
import dialogs.display.DisplayTNodeResult1;
import dialogs.display.DisplayTSolidResult1;
import dialogs.display.DisplayUndeformedShape1;
import dialogs.display.DisplayVAreaResult1;
import dialogs.display.DisplayVLineResult1;
import dialogs.display.DisplayVNodeResult1;
import dialogs.display.DisplayVSolidResult1;
import dialogs.display.DisplayPreferences1;
import dialogs.file.FileHandler1;
import dialogs.file.WriteOutput1;
import dialogs.library.AnalysisCase1;
import dialogs.library.BoundaryCase1;
import dialogs.library.Constraint2;
import dialogs.library.DispLoad1;
import dialogs.library.ElementLib1;
import dialogs.library.ElementMass1;
import dialogs.library.ElementMechLoad1;
import dialogs.library.ElementSpring1;
import dialogs.library.ElementTemp1;
import dialogs.library.Function1;
import dialogs.library.InitialDisp1;
import dialogs.library.InitialVelo1;
import dialogs.library.LocalAxis1;
import dialogs.library.Material1;
import dialogs.library.NodalMass1;
import dialogs.library.NodalMechLoad1;
import dialogs.library.NodalSpring1;
import dialogs.library.Section1;
import dialogs.library.SolverLib1;
import dialogs.model.AddElement1;
import dialogs.model.AddNode1;
import dialogs.model.Check1;
import dialogs.model.DivideLine1;
import dialogs.model.EditElement1;
import dialogs.model.EditNode1;
import dialogs.model.Group1;
import dialogs.model.ImportModel1;
import dialogs.model.MeshArea1;
import dialogs.model.MeshSolid1;
import dialogs.model.MirrorElement1;
import dialogs.model.MirrorNode1;
import dialogs.model.MoveElement1;
import dialogs.model.MoveNode1;
import dialogs.model.RemoveElement1;
import dialogs.model.RemoveNode1;
import dialogs.model.ReplicateElement1;
import dialogs.model.ReplicateNode1;
import dialogs.model.ShowElement1;
import dialogs.model.ShowNode1;
import dialogs.model.Sweep1;
import dialogs.view.ViewColor;
import dialogs.view.CustomizeToolbars1;
import dialogs.view.MenuFormat;
import dialogs.help.About;
import dialogs.help.KeyAssist1;

/**
 * Class for handling action events of menubar and toolbar buttons.
 * 
 * @author Murat Artim
 * 
 */
public class ActionHandler extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/** Owner frame of this action handler. */
	private SolidMAT owner_;

	/**
	 * Creates ActionHandler object for handling action events of menubar and
	 * toolbar buttons.
	 * 
	 * @param owner
	 *            Owner frame of this action handler.
	 */
	public ActionHandler(SolidMAT owner) {
		super();
		owner_ = owner;
	}

	public void actionPerformed(ActionEvent e) {

		// get action command
		String command = e.getActionCommand();

		// for file menu items
		if (command.contains("fileMenu"))
			fileMenuEvents(command);

		// for view menu items
		else if (command.contains("viewMenu"))
			viewMenuEvents(command, e);

		// for library menu items
		else if (command.contains("libraryMenu"))
			libraryMenuEvents(command);

		// for model menu items
		else if (command.contains("modelMenu"))
			modelMenuEvents(command);

		// for assign menu items
		else if (command.contains("assignMenu"))
			assignMenuEvents(command);

		// for analysis menu items
		else if (command.contains("analysisMenu"))
			analysisMenuEvents(command);

		// for display menu items
		else if (command.contains("displayMenu"))
			displayMenuEvents(command);

		// for help menu items
		else if (command.contains("helpMenu"))
			helpMenuEvents(command);
	}

	/**
	 * Handles file menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void fileMenuEvents(String command) {

		// new menu item clicked
		if (command.equalsIgnoreCase("fileMenuNew"))
			FileHandler1.createNewFile(owner_);

		// open menu item clicked
		else if (command.equalsIgnoreCase("fileMenuOpen"))
			FileHandler1.openFile(owner_);

		// save menu item clicked
		else if (command.equalsIgnoreCase("fileMenuSave"))
			FileHandler1.saveFile(owner_);

		// save as menu item clicked
		else if (command.equalsIgnoreCase("fileMenuSaveAs"))
			FileHandler1.saveFileAs(owner_);

		// write output tables menu item clicked
		else if (command.equalsIgnoreCase("fileMenuWrite")) {
			WriteOutput1 dialog = new WriteOutput1(owner_);
			dialog.setVisible(true);
		}

		// exit menu item clicked
		else if (command.equalsIgnoreCase("fileMenuExit")) {

			// structure, input data or path exists
			if (owner_.structure_.isEmpty() == false
					|| owner_.inputData_.isEmpty() == false
					|| owner_.path_ != null) {

				// display confirmation message
				int confirm = JOptionPane.showConfirmDialog(owner_.viewer_,
						"Save current model?", "Data confirmation",
						JOptionPane.YES_NO_OPTION);

				// save as/save
				if (confirm == JOptionPane.YES_OPTION) {

					// there is no path
					if (owner_.path_ == null) {

						// save as
						FileHandler1.saveFileAs(owner_);

						// exit
						System.exit(0);
					}

					// path exists
					else {

						// save
						FileHandler1.saveFile(owner_);

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
	}

	/**
	 * Handles view menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void viewMenuEvents(String command, ActionEvent e) {

		// 3d orbit menu item clicked
		if (command.equalsIgnoreCase("viewMenu3DOrbit")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view1_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(0);
			button.doClick();
		}

		// zoom window menu item clicked
		else if (command.equalsIgnoreCase("viewMenuZoomWindow")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view1_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(1);
			button.doClick();
		}

		// zoom in menu item clicked
		else if (command.equalsIgnoreCase("viewMenuZoomIn")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view1_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(3);
			button.doClick();
		}

		// zoom out menu item clicked
		else if (command.equalsIgnoreCase("viewMenuZoomOut")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view1_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(4);
			button.doClick();
		}

		// zoom extents menu item clicked
		else if (command.equalsIgnoreCase("viewMenuZoomExtents")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view1_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(5);
			button.doClick();
		}

		// top view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuTopView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(0);
			button.doClick();
		}

		// bottom view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuBottomView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(1);
			button.doClick();
		}

		// left view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuLeftView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(2);
			button.doClick();
		}

		// right view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuRightView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(3);
			button.doClick();
		}

		// front view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuFrontView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(4);
			button.doClick();
		}

		// back view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuBackView")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(5);
			button.doClick();
		}

		// SW view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuSW")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(7);
			button.doClick();
		}

		// SE view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuSE")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(8);
			button.doClick();
		}

		// NE view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuNE")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(9);
			button.doClick();
		}

		// NW view menu item clicked
		else if (command.equalsIgnoreCase("viewMenuNW")) {
			JToolBar toolbar = owner_.toolbars_.getToolbar(Toolbars.view2_);
			AbstractButton button = (AbstractButton) toolbar.getComponent(10);
			button.doClick();
		}

		// view background colors menu item clicked
		else if (command.equalsIgnoreCase("viewMenuBackground"))
			ViewColor.show(owner_);

		// show axes menu item clicked
		else if (command.equalsIgnoreCase("viewMenuAxes")) {
			AbstractButton item = (AbstractButton) e.getSource();
			if (item.isSelected() == false) {
				owner_.viewer_.setAxesVisible(false);
				item.setSelected(false);
			} else {
				owner_.viewer_.setAxesVisible(true);
				item.setSelected(true);
			}
		}

		// redraw menu item clicked
		else if (command.equalsIgnoreCase("viewMenuRedraw")) {
			DisplayUndeformedShape1 dialog = new DisplayUndeformedShape1(owner_);
			dialog.setVisible(true);
		}

		// toolbars menu item clicked
		else if (command.equalsIgnoreCase("viewMenuToolbars")) {
			CustomizeToolbars1 dialog = new CustomizeToolbars1(owner_);
			dialog.setVisible(true);
		}

		// format menu item clicked
		else if (command.equalsIgnoreCase("viewMenuFormat")) {
			MenuFormat dialog = new MenuFormat(owner_);
			dialog.setVisible(true);
		}
	}

	/**
	 * Handles library menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void libraryMenuEvents(String command) {

		// sections menu item clicked
		if (command.equalsIgnoreCase("libraryMenuSections")) {
			Section1 dialog = new Section1(owner_);
			dialog.setVisible(true);
		}

		// initial displacements menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuInitialDisp")) {
			InitialDisp1 dialog = new InitialDisp1(owner_);
			dialog.setVisible(true);
		}

		// functions menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuFunctions")) {
			Function1 dialog = new Function1(owner_);
			dialog.setVisible(true);
		}

		// solvers menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuSolvers")) {
			SolverLib1 dialog = new SolverLib1(owner_);
			dialog.setVisible(true);
		}

		// initial velocities menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuInitialVelo")) {
			InitialVelo1 dialog = new InitialVelo1(owner_);
			dialog.setVisible(true);
		}

		// elements menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuElements")) {
			ElementLib1 dialog = new ElementLib1(owner_);
			dialog.setVisible(true);
		}

		// nodal displacements menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuNodalDispLoads")) {
			DispLoad1 dialog = new DispLoad1(owner_);
			dialog.setVisible(true);
		}

		// nodal mechanical loads menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuNodalMechLoads")) {
			NodalMechLoad1 dialog = new NodalMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// constraints menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuConstraints")) {
			Constraint2 dialog = new Constraint2(owner_);
			dialog.setVisible(true);
		}

		// element mechanical loads menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuElementMechLoads")) {
			ElementMechLoad1 dialog = new ElementMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// element temperature loads menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuElementTempLoads")) {
			ElementTemp1 dialog = new ElementTemp1(owner_);
			dialog.setVisible(true);
		}

		// local axes menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuLocalAxes")) {
			LocalAxis1 dialog = new LocalAxis1(owner_);
			dialog.setVisible(true);
		}

		// boundary cases menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuBoundaryCases")) {
			BoundaryCase1 dialog = new BoundaryCase1(owner_);
			dialog.setVisible(true);
		}

		// analysis cases menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuAnalysisCases")) {
			AnalysisCase1 dialog = new AnalysisCase1(owner_);
			dialog.setVisible(true);
		}

		// materials menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuMaterials")) {
			Material1 dialog = new Material1(owner_);
			dialog.setVisible(true);
		}

		// nodal masses menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuNodalMasses")) {
			NodalMass1 dialog = new NodalMass1(owner_);
			dialog.setVisible(true);
		}

		// element masses menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuElementMasses")) {
			ElementMass1 dialog = new ElementMass1(owner_);
			dialog.setVisible(true);
		}

		// nodal springs menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuNodalSprings")) {
			NodalSpring1 dialog = new NodalSpring1(owner_);
			dialog.setVisible(true);
		}

		// element springs menu item clicked
		else if (command.equalsIgnoreCase("libraryMenuElementSprings")) {
			ElementSpring1 dialog = new ElementSpring1(owner_);
			dialog.setVisible(true);
		}
	}

	/**
	 * Handles model menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void modelMenuEvents(String command) {

		// Add Node menu item clicked
		if (command.equalsIgnoreCase("modelMenuAddNode")) {
			AddNode1 dialog = new AddNode1(owner_);
			dialog.setVisible(true);
		}

		// Add Element menu item clicked
		else if (command.equalsIgnoreCase("modelMenuAddElement")) {
			AddElement1 dialog = new AddElement1(owner_);
			dialog.setVisible(true);
		}

		// Remove Node menu item clicked
		else if (command.equalsIgnoreCase("modelMenuRemoveNodes")) {
			RemoveNode1 dialog = new RemoveNode1(owner_);
			dialog.setVisible(true);
		}

		// Remove Element menu item clicked
		else if (command.equalsIgnoreCase("modelMenuRemoveElements")) {
			RemoveElement1 dialog = new RemoveElement1(owner_);
			dialog.setVisible(true);
		}

		// Show Node menu item clicked
		else if (command.equalsIgnoreCase("modelMenuShowNode")) {
			ShowNode1 dialog = new ShowNode1(owner_);
			dialog.setVisible(true);
		}

		// Show Element menu item clicked
		else if (command.equalsIgnoreCase("modelMenuShowElement")) {
			ShowElement1 dialog = new ShowElement1(owner_);
			dialog.setVisible(true);
		}

		// Groups menu item clicked
		else if (command.equalsIgnoreCase("modelMenuGroups")) {
			Group1 dialog = new Group1(owner_);
			dialog.setVisible(true);
		}

		// Edit Node menu item clicked
		else if (command.equalsIgnoreCase("modelMenuEditNode")) {
			EditNode1 dialog = new EditNode1(owner_);
			dialog.setVisible(true);
		}

		// Edit Element menu item clicked
		else if (command.equalsIgnoreCase("modelMenuEditElement")) {
			EditElement1 dialog = new EditElement1(owner_);
			dialog.setVisible(true);
		}

		// Divivde Lines menu item clicked
		else if (command.equalsIgnoreCase("modelMenuDivide")) {
			DivideLine1 dialog = new DivideLine1(owner_);
			dialog.setVisible(true);
		}

		// Mesh Areas menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMeshAreas")) {
			MeshArea1 dialog = new MeshArea1(owner_);
			dialog.setVisible(true);
		}

		// Mesh Solids menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMeshSolids")) {
			MeshSolid1 dialog = new MeshSolid1(owner_);
			dialog.setVisible(true);
		}

		// Move Nodes menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMoveNodes")) {
			MoveNode1 dialog = new MoveNode1(owner_);
			dialog.setVisible(true);
		}

		// Move Elements menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMoveElements")) {
			MoveElement1 dialog = new MoveElement1(owner_);
			dialog.setVisible(true);
		}

		// Replicate Elements menu item clicked
		else if (command.equalsIgnoreCase("modelMenuReplicateElements")) {
			ReplicateElement1 dialog = new ReplicateElement1(owner_);
			dialog.setVisible(true);
		}

		// Replicate Nodes menu item clicked
		else if (command.equalsIgnoreCase("modelMenuReplicateNodes")) {
			ReplicateNode1 dialog = new ReplicateNode1(owner_);
			dialog.setVisible(true);
		}

		// Mirror Nodes menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMirrorNodes")) {
			MirrorNode1 dialog = new MirrorNode1(owner_);
			dialog.setVisible(true);
		}

		// Mirror Elements menu item clicked
		else if (command.equalsIgnoreCase("modelMenuMirrorElements")) {
			MirrorElement1 dialog = new MirrorElement1(owner_);
			dialog.setVisible(true);
		}

		// Sweep menu item clicked
		else if (command.equalsIgnoreCase("modelMenuSweep")) {
			Sweep1 dialog = new Sweep1(owner_);
			dialog.setVisible(true);
		}

		// Check Element menu item clicked
		else if (command.equalsIgnoreCase("modelMenuCheck")) {
			Check1 dialog = new Check1(owner_);
			dialog.setVisible(true);
		}

		// import model menu item clicked
		else if (command.equalsIgnoreCase("modelMenuImport")) {
			ImportModel1 dialog = new ImportModel1(owner_);
			dialog.setVisible(true);
		}
	}

	/**
	 * Handles assign menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void assignMenuEvents(String command) {

		// Assign Constraint menu item clicked
		if (command.equalsIgnoreCase("assignMenuConstraints")) {
			AConstraint1 dialog = new AConstraint1(owner_);
			dialog.setVisible(true);
		}

		// Assign Nodal Springs menu item clicked
		else if (command.equalsIgnoreCase("assignMenuNodalSprings")) {
			ANodalSpring1 dialog = new ANodalSpring1(owner_);
			dialog.setVisible(true);
		}

		// Assign Initial Displacements menu item clicked
		else if (command.equalsIgnoreCase("assignMenuInitialDisplacements")) {
			AInitialDisp1 dialog = new AInitialDisp1(owner_);
			dialog.setVisible(true);
		}

		// Assign Initial Velocities menu item clicked
		else if (command.equalsIgnoreCase("assignMenuInitialVelocities")) {
			AInitialVelo1 dialog = new AInitialVelo1(owner_);
			dialog.setVisible(true);
		}

		// Assign Nodal Masses menu item clicked
		else if (command.equalsIgnoreCase("assignMenuNodalMasses")) {
			ANodalMass1 dialog = new ANodalMass1(owner_);
			dialog.setVisible(true);
		}

		// Assign Nodal Local Axes menu item clicked
		else if (command.equalsIgnoreCase("assignMenuNodalAxes")) {
			ANodalLocalAxis1 dialog = new ANodalLocalAxis1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Materials menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineMaterials")) {
			ALineMaterial1 dialog = new ALineMaterial1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Sections menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineSections")) {
			ALineSection1 dialog = new ALineSection1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Springs menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineSprings")) {
			ALineSpring1 dialog = new ALineSpring1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Masses menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineMasses")) {
			ALineMass1 dialog = new ALineMass1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Local Axes menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineLocalAxes")) {
			ALineLocalAxis1 dialog = new ALineLocalAxis1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Material menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaMaterials")) {
			AAreaMaterial1 dialog = new AAreaMaterial1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Section menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaSections")) {
			AAreaSection1 dialog = new AAreaSection1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Springs menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaSprings")) {
			AAreaSpring1 dialog = new AAreaSpring1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Masses menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaMasses")) {
			AAreaMass1 dialog = new AAreaMass1(owner_);
			dialog.setVisible(true);
		}

		// Assign Solid Material menu item clicked
		else if (command.equalsIgnoreCase("assignMenuSolidMaterials")) {
			ASolidMaterial1 dialog = new ASolidMaterial1(owner_);
			dialog.setVisible(true);
		}

		// Assign Solid Springs menu item clicked
		else if (command.equalsIgnoreCase("assignMenuSolidSprings")) {
			ASolidSpring1 dialog = new ASolidSpring1(owner_);
			dialog.setVisible(true);
		}

		// Assign Solid Masses menu item clicked
		else if (command.equalsIgnoreCase("assignMenuSolidMasses")) {
			ASolidMass1 dialog = new ASolidMass1(owner_);
			dialog.setVisible(true);
		}

		// Assign Nodal Mechanical Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuNodalMechLoads")) {
			ANodalMechLoad1 dialog = new ANodalMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Nodal Displacement Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuDisplacements")) {
			ANodalDispLoad1 dialog = new ANodalDispLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Mechanical Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineMechLoads")) {
			ALineMechLoad1 dialog = new ALineMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Line Temperature Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuLineTempLoads")) {
			ALineTempLoad1 dialog = new ALineTempLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Mechanical Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaMechLoads")) {
			AAreaMechLoad1 dialog = new AAreaMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Area Temperature Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuAreaTempLoads")) {
			AAreaTempLoad1 dialog = new AAreaTempLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Solid Mechanical Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuSolidMechLoads")) {
			ASolidMechLoad1 dialog = new ASolidMechLoad1(owner_);
			dialog.setVisible(true);
		}

		// Assign Solid Temperature Loads menu item clicked
		else if (command.equalsIgnoreCase("assignMenuSolidTempLoads")) {
			ASolidTempLoad1 dialog = new ASolidTempLoad1(owner_);
			dialog.setVisible(true);
		}

		// Node Info menu item clicked
		else if (command.equalsIgnoreCase("assignMenuNodeInfo")) {
			ANodeInfo1 dialog = new ANodeInfo1(owner_);
			dialog.setVisible(true);
		}

		// Element Info menu item clicked
		else if (command.equalsIgnoreCase("assignMenuElementInfo")) {
			AElementInfo1 dialog = new AElementInfo1(owner_);
			dialog.setVisible(true);
		}
	}

	/**
	 * Handles analysis menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void analysisMenuEvents(String command) {

		// Options menu item clicked
		if (command.equalsIgnoreCase("analysisMenuOptions")) {
			AnalysisOptions1 dialog = new AnalysisOptions1(owner_);
			dialog.setVisible(true);
		}

		// Check Model menu item clicked
		else if (command.equalsIgnoreCase("analysisMenuCheck")) {
			CheckModel1 dialog = new CheckModel1(owner_);
			dialog.setVisible(true);
		}

		// Run menu item clicked
		else if (command.equalsIgnoreCase("analysisMenuRun")) {

			// path doesn't exist
			if (owner_.path_ == null) {
				if (FileHandler1.saveFileAs(owner_)) {

					// open dialog
					RunAnalysis1 dialog = new RunAnalysis1(owner_);
					dialog.setVisible(true);
				}
			}

			// path exists
			else {

				// open dialog
				RunAnalysis1 dialog = new RunAnalysis1(owner_);
				dialog.setVisible(true);
			}
		}
	}

	/**
	 * Handles display menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void displayMenuEvents(String command) {

		// Display Options menu item clicked
		if (command.equalsIgnoreCase("displayMenuOptions")) {
			DisplayOptions1 dialog = new DisplayOptions1(owner_);
			dialog.setVisible(true);
		}

		// Display Preferences menu item clicked
		else if (command.equalsIgnoreCase("displayMenuPreferences")) {
			DisplayPreferences1 dialog = new DisplayPreferences1(owner_);
			dialog.setVisible(true);
		}

		// Undeformed Shape menu item clicked
		else if (command.equalsIgnoreCase("displayMenuUndeformed")) {
			DisplayUndeformedShape1 dialog = new DisplayUndeformedShape1(owner_);
			dialog.setVisible(true);
		}

		// Display Nodal Loads menu item clicked
		else if (command.equalsIgnoreCase("displayMenuLoadAssignsNodes")) {
			DisplayNodalLoads1 dialog = new DisplayNodalLoads1(owner_);
			dialog.setVisible(true);
		}

		// Display Element Loads menu item clicked
		else if (command.equalsIgnoreCase("displayMenuLoadAssignsElements")) {
			DisplayElementLoads1 dialog = new DisplayElementLoads1(owner_);
			dialog.setVisible(true);
		}

		// Display Nodal Assignments menu item clicked
		else if (command.equalsIgnoreCase("displayMenuMiscAssignsNodes")) {
			DisplayNodeMisc1 dialog = new DisplayNodeMisc1(owner_);
			dialog.setVisible(true);
		}

		// Display Element Assignments menu item clicked
		else if (command.equalsIgnoreCase("displayMenuMiscAssignsElements")) {
			DisplayElementMisc1 dialog = new DisplayElementMisc1(owner_);
			dialog.setVisible(true);
		}

		// Display Deformed Shape menu item clicked
		else if (command.equalsIgnoreCase("displayMenuDeformed")) {
			DisplayDeformedShape1 dialog = new DisplayDeformedShape1(owner_);
			dialog.setVisible(true);
		}

		// Display Node Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuVResultsNodes")) {
			DisplayVNodeResult1 dialog = new DisplayVNodeResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Line Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuVResultsLines")) {
			DisplayVLineResult1 dialog = new DisplayVLineResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Area Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuVResultsAreas")) {
			DisplayVAreaResult1 dialog = new DisplayVAreaResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Solid Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuVResultsSolids")) {
			DisplayVSolidResult1 dialog = new DisplayVSolidResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Node Table Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuTResultsNodes")) {
			DisplayTNodeResult1 dialog = new DisplayTNodeResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Line Table Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuTResultsLines")) {
			DisplayTLineResult1 dialog = new DisplayTLineResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Area Table Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuTResultsAreas")) {
			DisplayTAreaResult1 dialog = new DisplayTAreaResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Solid Table Results menu item clicked
		else if (command.equalsIgnoreCase("displayMenuTResultsSolids")) {
			DisplayTSolidResult1 dialog = new DisplayTSolidResult1(owner_);
			dialog.setVisible(true);
		}

		// Display Node Plot menu item clicked
		else if (command.equalsIgnoreCase("displayMenuPlotNodes")) {
			DisplayNodePlot1 dialog = new DisplayNodePlot1(owner_);
			dialog.setVisible(true);
		}

		// Display Line Plot menu item clicked
		else if (command.equalsIgnoreCase("displayMenuPlotLines")) {
			DisplayLinePlot1 dialog = new DisplayLinePlot1(owner_);
			dialog.setVisible(true);
		}

		// Display Area Plot menu item clicked
		else if (command.equalsIgnoreCase("displayMenuPlotAreas")) {
			DisplayAreaPlot1 dialog = new DisplayAreaPlot1(owner_);
			dialog.setVisible(true);
		}

		// Display Solid Plot menu item clicked
		else if (command.equalsIgnoreCase("displayMenuPlotSolids")) {
			DisplaySolidPlot1 dialog = new DisplaySolidPlot1(owner_);
			dialog.setVisible(true);
		}

		// Display Function Plot menu item clicked
		else if (command.equalsIgnoreCase("displayMenuFunctionPlot")) {
			DisplayFunctionPlot1 dialog = new DisplayFunctionPlot1(owner_);
			dialog.setVisible(true);
		}
	}

	/**
	 * Handles help menu items' events.
	 * 
	 * @param command
	 *            Action command.
	 */
	private void helpMenuEvents(String command) {

		// Volume B menu item clicked
		if (command.equalsIgnoreCase("helpMenuVolB")) {
			int option = DocumentHandler.page_;
			DocumentHandler.openPDFDocument("VolumeB.pdf", option, "1");
		}

		// Introduction & Overview menu item clicked
		else if (command.equalsIgnoreCase("helpMenuIntroduction")) {
			int option = DocumentHandler.page_;
			DocumentHandler.openPDFDocument("Introduction.pdf", option, "1");
		}

		// Key Assist menu item clicked
		else if (command.equalsIgnoreCase("helpMenuKeyAssist")) {
			KeyAssist1 dialog = new KeyAssist1(owner_);
			dialog.setVisible(true);
		}

		// About menu item clicked
		else if (command.equalsIgnoreCase("helpMenuAbout")) {
			About dialog = new About(owner_);
			dialog.setVisible(true);
		}
	}
}
