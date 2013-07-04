package builder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import uk.ac.ebi.brain.core.Brain;
import uk.ac.ebi.brain.error.BadNameException;
import uk.ac.ebi.brain.error.BadPrefixException;
import uk.ac.ebi.brain.error.BrainException;
import uk.ac.ebi.brain.error.ClassExpressionException;
import uk.ac.ebi.brain.error.ExistingClassException;
import uk.ac.ebi.brain.error.ExistingObjectPropertyException;
import uk.ac.ebi.brain.error.NewOntologyException;
import uk.ac.ebi.brain.error.NonExistingClassException;
import uk.ac.ebi.brain.error.NonExistingEntityException;
import uk.ac.ebi.brain.error.ObjectPropertyExpressionException;
import uk.ac.ebi.brain.error.StorageException;

public class Launcher {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, BrainException, OWLOntologyStorageException, OWLOntologyCreationException {

		//		convertGo();

		createUniprot();

		//				parseTaxo();
		//		convertTaxoInOWL();
		//		convertUsingApi();
	}

	//		private static void parseTaxo() throws FileNotFoundException, IOException {
	//			TaxoParser parser = new TaxoParser("data/taxo.ser");
	//			parser.start();
	//			parser.save();
	//		}




	private static void createUniprot() throws BrainException {
		Brain brain = new Brain();

		Brain uniprot = new Brain("http://localhost/", "http://localhost/uniprot.owl");

		System.out.println("learning NCBI");
		brain.learn("data/NCBI-taxonomy-mammals.owl");
		System.out.println("Learning GO");
		brain.learn("data/gene_ontology.owl");

		uniprot.addObjectProperty("involved_in");
		uniprot.addObjectProperty("expressed_in");
		uniprot.addClass("Protein");

		Random generator = new Random();
		int id = 0;

		List<String> species = brain.getSubClasses("NCBI_40674", false);


		for (String goTerm : brain.getSubClasses("GO_0008150", false)) {
			int roll = generator.nextInt(4) + 1;

			String randSpecies = species.get(generator.nextInt(species.size()));

			if(roll == 2){
				uniprot.addClass("Prot_" + id);
				uniprot.subClassOf("Prot_" + id, "Protein");

				if(!uniprot.knowsClass(randSpecies)){
					uniprot.addClass(randSpecies);
				}

				if(!uniprot.knowsClass(goTerm)){
					uniprot.addClass(goTerm);
				}

				uniprot.subClassOf("Prot_" + id, "expressed_in some " + randSpecies);
				uniprot.subClassOf("Prot_" + id, "involved_in some " + goTerm);
				id++;
			}
		}

		System.out.println("saving...");
		uniprot.save("data/uniprot.owl");
		uniprot.sleep();
		brain.sleep();
		System.out.println("done");
	}

	//	private static void convertGo() throws FileNotFoundException, IOException, ClassNotFoundException, BrainException {
	//		GeneOntology go = new GeneOntology("/home/samuel/git/ftc/data/tmp/go.ser");
	//		Brain brain = new Brain("http://localhost/", "http://localhost/go.owl");
	//		brain.addObjectProperty("regulates");
	//		brain.addObjectProperty("negatively_regulates");
	//		brain.addObjectProperty("positively_regulates");
	//		brain.subPropertyOf("negatively_regulates", "regulates");
	//		brain.subPropertyOf("positively_regulates", "regulates");
	//		brain.addObjectProperty("part_of");
	//		brain.transitive("part_of");
	//		brain.chain("regulates o part_of", "regulates");
	//
	//		for (GoTerm bioprocess : go.getBioProcesses()) {
	//			System.out.println(bioprocess.getName());
	//			String bioprocessName = bioprocess.getId().replaceAll(":", "_");
	//			try{
	//				if(go.isTermABioProcess(bioprocess.getId())){
	//					brain.addClass(bioprocessName);
	//					brain.label(bioprocessName, bioprocess.getName());
	//				}
	//			}catch(ExistingClassException e){
	//				e.printStackTrace();
	//			}
	//
	//
	//			for (GoRelation relation : bioprocess.getRelations()) {
	//				System.out.println(bioprocess.getId() + " - " + relation.getType() + " --> " + relation.getTarget());
	//				String targetName = relation.getTarget().replaceAll(":", "_");
	//				try{
	//					brain.getOWLClass(targetName);
	//				} catch(NonExistingClassException e){
	//					if(go.isTermABioProcess(relation.getTarget())){
	//						brain.addClass(targetName);
	//						brain.label(targetName, go.getTerm(relation.getTarget()).getName());
	//					}
	//				}
	//
	//				if(relation.getType().equals("is_a")){
	//					brain.subClassOf(bioprocessName, targetName);
	//				}else{
	//					try{
	//						brain.subClassOf(bioprocessName, relation.getType() + " some " + targetName);
	//					} catch(ClassExpressionException e){
	//						e.printStackTrace();
	//					}
	//				}
	//			}
	//		}
	//		brain.save("data/go.owl");
	//
	//	}

	private static void convertUsingApi() throws OWLOntologyCreationException, IOException, ClassNotFoundException, OWLOntologyStorageException {
		System.out.println("Starting the convertion in OWL");
		NCBItaxo taxo = new NCBItaxo("data/taxo.ser");
		System.out.println("loaded");

		FileInputStream fstream = null;
		fstream = new FileInputStream("data/mammals.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;

		ArrayList<Integer> mammals = new ArrayList<Integer>();

		while ((line = br.readLine()) != null)   {
			String id = TaxoParser.getStringFromPattern(".*\t(.*)\t.*\t.*\t.*\t.*\t.*\t.*\t.*\t.*", line);
			mammals.add(Integer.parseInt(id));
		}

		br.close();

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		String base = "http://www.example.org/";
		OWLOntology ontology = manager.createOntology(IRI.create(base + "ontology.owl"));
		OWLDataFactory factory = manager.getOWLDataFactory();

		int counter = 0;
		int mams = 0;
		int total = taxo.getNodes().size();

		for (Node node : taxo.getNodes()) {

			if(mammals.contains(node.getId())){
				mams++;
				String currentClass = Integer.toString(node.getId());
				String parentClass = Integer.toString(node.getParentId());

				OWLClass nodeClass = factory.getOWLClass(IRI.create(base + currentClass));
				OWLClass nodeClassParent = factory.getOWLClass(IRI.create(base + parentClass));
				OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(nodeClass, nodeClassParent);
				AddAxiom addAxiom = new AddAxiom(ontology, axiom);
				manager.applyChange(addAxiom);
			}

			System.out.println(counter + "/" + total + "(" + mams+ ")");
			counter++;
		}
		System.out.println("saving");
		manager.saveOntology(ontology, IRI.create("file:/home/samuel/workspace/SWAT4LS/data/owlapi.owl"));
	}

	private static void convertTaxoInOWL() throws IOException, ClassNotFoundException, NewOntologyException, BadPrefixException, BadNameException, ClassExpressionException, StorageException, NonExistingEntityException, OWLOntologyStorageException, OWLOntologyCreationException, ExistingClassException {
		System.out.println("Starting the convertion in OWL");
		NCBItaxo taxo = new NCBItaxo("data/taxo.ser");
		System.out.println("loaded");

		FileInputStream fstream = null;
		fstream = new FileInputStream("data/mammals.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;

		ArrayList<Integer> mammals = new ArrayList<Integer>();

		while ((line = br.readLine()) != null)   {
			String id = TaxoParser.getStringFromPattern(".*\t(.*)\t.*\t.*\t.*\t.*\t.*\t.*\t.*\t.*", line);
			mammals.add(Integer.parseInt(id));
		}

		br.close();

		Brain brain = new Brain("http://localhost/", "http://localhost/ncbi-taxonomy.owl");
		int counter = 0;
		int mams = 0;
		int total = taxo.getNodes().size();
		ArrayList<Integer> toAvoid = new ArrayList<Integer>();
		toAvoid.add(365702);
		toAvoid.add(351500);


		HashSet<String> ranks = new HashSet<String>();

		for (Node node : taxo.getNodes()) {

			if(mammals.contains(node.getId())){
				mams++;
				String currentClass = Integer.toString(node.getId());
				String parentClass = Integer.toString(node.getParentId());
				ranks.add(node.getRank());

				try {
					brain.addClass("NCBI_" + currentClass);
					brain.label("NCBI_" + currentClass, node.getName());
				} catch (ExistingClassException e) {
					e.printStackTrace();
				}

				//TODO replace this with proper method
				//Super dirty avoid because of inconsistency of native file
				if(node.getId() != 1 && !toAvoid.contains(node.getId())){
					try {
						brain.getOWLClass("NCBI_" + parentClass);
					}catch(NonExistingClassException e){
						try {
							System.out.println("Getting parent: " + parentClass + " - node: " + node.getId());
							brain.addClass("NCBI_" + parentClass);
							brain.label("NCBI_" + parentClass, taxo.getParentNode(node).getName());

						} catch (ExistingClassException e1) {
							e1.printStackTrace();
						}

					}

					brain.subClassOf("NCBI_" + currentClass, "NCBI_" + parentClass);
				}

				System.out.println(counter + "/" + total + "(" + node.getId() +" - "+ node.getName() + ")");
				counter++;
			}
		}
		System.out.println("saving");

		for (String string : ranks) {
			System.out.println(string);
		}

		brain.save("data/taxo.owl");

	}


}
