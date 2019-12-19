package util;

public class ToubleConverter {
	public String getNameFromValue(String toConvert) {
		String replaced = toConvert.replaceAll("([()])", " ");
		return replaced.trim().split(",")[0];
	}
}
