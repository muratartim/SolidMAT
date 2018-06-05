package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import node.Node;

import analysis.Structure;

/**
 * Class for writing manager.
 * 
 * @author Murat
 * 
 */
public class WriteManager {

	/** Static variable for the writing option. */
	public final static int materialInfo_ = 0, sectionInfo_ = 1,
			constraintInfo_ = 2, nodalMechLoadInfo_ = 3, nodalPosInfo_ = 4,
			elementInfo_ = 5, structureInfo_ = 6, globalNodeDispInfo_ = 7,
			localNodeDispInfo_ = 8, elasticStrainInfo_ = 9, stressInfo_ = 10,
			principleStressInfo_ = 11, misesStressInfo_ = 12,
			internalForceInfo_ = 13, globalReactionForceInfo_ = 14,
			localReactionForceInfo_ = 15, dispLoadInfo_ = 16,
			nodalSpringInfo_ = 17, nodalMassInfo_ = 18,
			elementMechLoadInfo_ = 19, elementDispInfo_ = 20,
			elementSpringInfo_ = 21, elementAdditionalMassInfo_ = 22,
			principleStrainInfo_ = 23, nodalLocalAxisInfo_ = 24,
			initialDispInfo_ = 25, initialVeloInfo_ = 26, analysisInfo_ = 27,
			elementLocalAxisInfo_ = 28, tempLoadInfo_ = 29;

	/** The writer object of manager. */
	private Writer writer_;

	/** The structure to be written. */
	private Structure structure_;

	/** The output file of structure. */
	private File output_;

	/** The number of output stations used for one dimensional elements. */
	private int stations_ = 3;

	/**
	 * Creates writer object.
	 * 
	 * @param structure
	 *            The structure to be written.
	 * 
	 * @param path
	 *            The path of output file.
	 */
	public WriteManager(Structure structure, String path) {

		// set structure
		structure_ = structure;

		try {

			// create output file
			output_ = new File(path);
		}

		// exception occured during creation of file
		catch (Exception excep) {
			exceptionHandler("Exception occured during creation of output file!");
		}

		// initialize output file
		initialize();
	}

	/**
	 * Sets number of output stations for element results.
	 * 
	 * @param stations
	 *            The number of stations.
	 */
	public void setNumberOfOutputStations(int stations) {
		if (stations > 1)
			stations_ = stations;
		else
			exceptionHandler("Illegal number of output stations demanded!");
	}

	/**
	 * Writes demanded information to output file.
	 * 
	 * @param option
	 *            The writing option.
	 */
	public void write(int option) {

		// material information
		if (option == WriteManager.materialInfo_)
			writer_ = new MaterialInfo();

		// section information
		else if (option == WriteManager.sectionInfo_)
			writer_ = new SectionInfo();

		// constraint information
		else if (option == WriteManager.constraintInfo_)
			writer_ = new ConstraintInfo();

		// nodal mechanical load information
		else if (option == WriteManager.nodalMechLoadInfo_)
			writer_ = new NodalMechLoadInfo();

		// nodal position information
		else if (option == WriteManager.nodalPosInfo_)
			writer_ = new NodalPosInfo();

		// element information
		else if (option == WriteManager.elementInfo_)
			writer_ = new ElementInfo();

		// structure information
		else if (option == WriteManager.structureInfo_)
			writer_ = new StructureInfo();

		// global displacement information
		else if (option == WriteManager.globalNodeDispInfo_)
			writer_ = new NodalDispInfo(Node.global_);

		// local displacement information
		else if (option == WriteManager.localNodeDispInfo_)
			writer_ = new NodalDispInfo(Node.local_);

		// elastic strains information
		else if (option == WriteManager.elasticStrainInfo_)
			writer_ = new ElasticStrainInfo(stations_);

		// stress information
		else if (option == WriteManager.stressInfo_)
			writer_ = new StressInfo(stations_);

		// principle stress information
		else if (option == WriteManager.principleStressInfo_)
			writer_ = new PrincipleStressInfo(stations_);

		// Von Mises stress information
		else if (option == WriteManager.misesStressInfo_)
			writer_ = new MisesStressInfo(stations_);

		// internal force information
		else if (option == WriteManager.internalForceInfo_)
			writer_ = new InternalForceInfo(stations_);

		// global reaction force information
		else if (option == WriteManager.globalReactionForceInfo_)
			writer_ = new ReactionForceInfo(Node.global_);

		// local reaction force information
		else if (option == WriteManager.localReactionForceInfo_)
			writer_ = new ReactionForceInfo(Node.local_);

		// displacement load information
		else if (option == WriteManager.dispLoadInfo_)
			writer_ = new DispLoadInfo();

		// initial displacement information
		else if (option == WriteManager.initialDispInfo_)
			writer_ = new InitialDispInfo();

		// initial velocity information
		else if (option == WriteManager.initialVeloInfo_)
			writer_ = new InitialVeloInfo();

		// nodal spring information
		else if (option == WriteManager.nodalSpringInfo_)
			writer_ = new NodalSpringInfo();

		// nodal mass information
		else if (option == WriteManager.nodalMassInfo_)
			writer_ = new NodalMassInfo();

		// element mechanical load information
		else if (option == WriteManager.elementMechLoadInfo_)
			writer_ = new ElementMechLoadInfo();

		// element displacements information
		else if (option == WriteManager.elementDispInfo_)
			writer_ = new ElementDispInfo(stations_);

		// element spring information
		else if (option == WriteManager.elementSpringInfo_)
			writer_ = new ElementSpringInfo();

		// element additional mass information
		else if (option == WriteManager.elementAdditionalMassInfo_)
			writer_ = new ElementMassInfo();

		// principle strain information
		else if (option == WriteManager.principleStrainInfo_)
			writer_ = new PrincipleStrainInfo(stations_);

		// nodal local axis information
		else if (option == WriteManager.nodalLocalAxisInfo_)
			writer_ = new NodalLocalAxisInfo();

		// temperature information
		else if (option == WriteManager.tempLoadInfo_)
			writer_ = new TempLoadInfo();

		// element local axis information
		else if (option == WriteManager.elementLocalAxisInfo_)
			writer_ = new ElementLocalAxisInfo();

		// analysis information
		else if (option == WriteManager.analysisInfo_)
			writer_ = new AnalysisInfo();

		// illegal option
		else
			exceptionHandler("Illegal writing option!");

		// write demanded information
		writer_.write(structure_, output_);
	}

	/**
	 * Writes stepping information to output file.
	 * 
	 * @param step
	 *            The step number to be written.
	 */
	public void writeStepInfo(int step) {

		try {

			// create file writer
			FileWriter writer = new FileWriter(output_, true);

			// create buffered writer
			BufferedWriter bwriter = new BufferedWriter(writer);

			// pass to new line
			bwriter.newLine();

			// write step number to output
			bwriter.write("S  T  E  P  " + step);

			// pass to new line
			bwriter.newLine();

			// close writer
			bwriter.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Initializes output file.
	 * 
	 */
	private void initialize() {

		try {

			// create file writer
			FileWriter writer = new FileWriter(output_);

			// create buffered writer
			BufferedWriter bwriter = new BufferedWriter(writer);

			// get date
			Date date = new Date(System.currentTimeMillis());

			// set header
			String header = "SolidMAT 2.0 v1.0.0" + '\t' + date.toString();

			// write header to output
			bwriter.write(header);

			// pass to new line
			bwriter.newLine();

			// close writer
			bwriter.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during initializing output file!");
		}
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
