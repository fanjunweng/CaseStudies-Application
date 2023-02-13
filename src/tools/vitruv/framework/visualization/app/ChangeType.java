package tools.vitruv.framework.visualization.app;

enum ChangeType {
	CREATED("Created"), UPDATED("Updated"), DELETED("Deleted");
	
	private String changeTypeString;
	
	private ChangeType(String  changeTypeString) {
		this.changeTypeString = changeTypeString;
	}
	
	@Override
	public String toString() {
		return this.changeTypeString;
	}
}
