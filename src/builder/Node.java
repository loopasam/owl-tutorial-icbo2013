package builder;

import java.io.Serializable;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String rank;
	private String name;
	private int parentId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

}
