package as;

public interface IExpr extends IAbsSynTreeNode {
	public token.Type getType();
	public token.LRVal getLRValue();
}
