package token;

public class Literal implements IToken {
	private Boolean boolValue = null;
	private Integer intValue = null;
	private String stringValue = "";

	@Override
	public String toString() {
		if (boolValue != null) {
			return "(LITERAL, BoolVal " + boolValue + ")";
		} else if (intValue != null) {
			return "(LITERAL, Int64Val " + intValue + ")";
		} else {
			return "";
		}
	}

	public void setBoolValue(String boolValue) {
		this.boolValue = Boolean.valueOf(boolValue);
		this.intValue = null;
		this.stringValue = "";
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
		this.boolValue = null;
		this.stringValue = "";
	}

	public void setStringValue(String string) {
		this.stringValue = string;
		this.intValue = null;
		this.boolValue = null;
	}
	
	public Type getType() {
		if(boolValue != null) {
			return Type.BOOL;
		} else if (intValue != null) {
			return Type.INT64;
		} else {
			throw new RuntimeException("Other type than BOOL or INT64 found in Literal");
			// Should not happen :)
		}
	}

	public Boolean getBoolValue() {
		return boolValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public String getStringValue() {
		return stringValue;
	}
	
	
}
