package builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class NCBItaxo implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Node> nodes;

	public NCBItaxo() {
		this.setNodes(new ArrayList<Node>());
	}

	public NCBItaxo(String pathToSer) throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File(pathToSer);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		NCBItaxo taxo = (NCBItaxo) in.readObject();
		this.setNodes(taxo.getNodes());
		in.close();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public boolean isMammal(Node node) {

		if(node.getId() == 1){
			return false;
		}

		if(node.getId() == 40674){
			return true;
		}

		return isMammal(this.getParentNode(node));
	}

	public Node getParentNode(Node node) {

		int parentId = node.getParentId();

		for (Node nodeToTest : this.getNodes()) {
			if(nodeToTest.getId() == parentId){
				return nodeToTest;
			}
		}

		return null;
	}

}
