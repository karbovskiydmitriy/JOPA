package types;

public class CustomType {

	public String name;
	public String template;

	public CustomType(String name) {
		this.name = name;
	}

	public String generateCode() {
		return template;
	}

}