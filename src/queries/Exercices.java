package queries;

import uk.ac.ebi.brain.core.Brain;
import uk.ac.ebi.brain.error.BrainException;

public class Exercices {

	public static void main(String[] args) throws BrainException {
		exercice2();
		exercice4();
		exercice5();
		exercice8();
	}


	private static void exercice8() throws BrainException {
		Brain brain = new Brain();
		brain.learn("data/gene_ontology.owl");
		brain.learn("data/NCBI-taxonomy-mammals.owl");
		brain.learn("data/uniprot.owl");
		//involved_in some (regulates some blood coagulation) and expressed_in some Homo sapiens
		System.out.println(brain.getSubClasses("involved_in some (regulates some GO_0007596) and expressed_in some NCBI_9606", false).size());
		brain.sleep();
	}


	private static void exercice5() throws BrainException {
		Brain brain = new Brain();
		brain.learn("data/gene_ontology.owl");
		//biological_process and regulates some 'mitotic cell cycle'
		System.out.println(brain.getSubClasses("GO_0008150 and regulates some GO_0000278", false).size());
		brain.sleep();
	}


	private static void exercice4() throws BrainException {
		Brain brain = new Brain();
		brain.learn("data/gene_ontology.owl");
		//biological_process and part_of some 'wound healing'
		System.out.println(brain.getSubClasses("GO_0008150 and part_of some GO_0042060", false).size());
		brain.sleep();
	}


	private static void exercice2() throws BrainException {
		Brain brain = new Brain();
		brain.learn("data/NCBI-taxonomy-mammals.owl");
		//Abrothrix
		System.out.println(brain.getSubClasses("NCBI_156196", false).size());
		brain.sleep();
	}

}
