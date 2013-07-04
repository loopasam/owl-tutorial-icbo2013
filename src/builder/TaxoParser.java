package builder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//import build.Parser;

//public class TaxoParser extends Parser {
public class TaxoParser {

	private NCBItaxo taxo;

	public NCBItaxo getTaxo() {
		return taxo;
	}

	public void setTaxo(NCBItaxo taxo) {
		this.taxo = taxo;
	}

//	public TaxoParser(String pathOut) {
//		super(pathOut);
//		this.setTaxo(new NCBItaxo());
//	}

	//	@Override
	//	public void start() throws FileNotFoundException, IOException {
	//		System.out.println("Downloading the taxo file...");
	//		File tempTaxoFile = new File("data/taxonomy.dat");
	//		System.out.println("Parsing the taxo...");
	//
	//		FileInputStream fstream = null;
	//		fstream = new FileInputStream(tempTaxoFile);
	//		DataInputStream in = new DataInputStream(fstream);
	//		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	//		String line = null;
	//
	//		while ((line = br.readLine()) != null)   {
	//
	//			String id = null;
	//			String parentId = null;
	//			String name = null;
	//			String rank = null;
	//
	//			while(line != null && line.length() > 0){
	//
	//				if(line.startsWith("ID")){
	//					id = getStringFromPattern("ID.*: (.*)", line);
	//				}
	//
	//				if(line.startsWith("PARENT ID")){
	//					parentId = getStringFromPattern("PARENT ID.*: (.*)", line);
	//				}
	//
	//				if(line.startsWith("RANK")){
	//					rank = getStringFromPattern("RANK.*: (.*)", line);
	//				}
	//
	//				if(line.startsWith("SCIENTIFIC NAME")){
	//					name = getStringFromPattern("SCIENTIFIC NAME.*: (.*)", line);
	//				}
	//
	//				if(line.equals("//")){
	//					break;
	//				}
	//
	//				line = br.readLine();
	//			}
	//
	//			Node node = new Node();
	//			if(id != null){
	//				node.setId(Integer.parseInt(id));
	//			}else{
	//				System.err.println("no id for this node: " + name);
	//			}
	//			node.setName(name);
	//			if(parentId != null){
	//				node.setParentId(Integer.parseInt(parentId));
	//			}
	//			node.setRank(rank);
	//			this.getTaxo().getNodes().add(node);
	//
	//		}
	//
	//		br.close();
	//	}

	//	@Override
	//	public Object save() throws FileNotFoundException, IOException {
	//		System.out.println("Saving the taxo serialized...");
	//		ObjectOutput out = null;
	//		out = new ObjectOutputStream(new FileOutputStream(this.getPathOut()));
	//		out.writeObject(this.getTaxo());
	//		out.close();
	//		return this.getTaxo();
	//	}

	/**
	 * @param string
	 * @param line
	 * @return
	 */
	public static String getStringFromPattern(String patternString, String line) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()){
			return matcher.group(1);
		}
		return null;
	}

}
