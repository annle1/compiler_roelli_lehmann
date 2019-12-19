package util;

import as.AbsSynTree;
import vm.ICodeArray;
import vm.ICodeArray.CodeTooSmallError;

public class AStoCodeArrayHelper {
	public ICodeArray convert(AbsSynTree as) throws CodeTooSmallError {
		return as.getCodeArray();
	}
}
