package util;

import java.util.HashMap;
import java.util.Map;

import as.TypedIdent;

public class DeepCopyHelper {
	
	public DeepCopyHelper() {
		
	}
	
	public static HashMap<String, TypedIdent> deepCopy(HashMap<String, TypedIdent> map) {
		HashMap<String, TypedIdent> tmp = new HashMap<>();
		for(Map.Entry<String, TypedIdent> entry : map.entrySet()){
			try {
				tmp.put(entry.getKey(), (TypedIdent) entry.getValue().clone());
			} catch (CloneNotSupportedException e) {
				System.out.println("Clone error");
				e.printStackTrace();
			}
		}
		return tmp;
	}

}
