package oxi.models;
import java.util.List;

public interface Relational
{
	public <T extends Relational> void internalRemoveChild(T child);
	public <T extends Relational> void internalAddChild(T child);
	public <T extends Relational> void internalAddChildByType(T targetChild, Class<T> childClass, List<Class<T>> childList);
	public <T extends Relational> void internalRemoveChildByType(T targetChild, Class<T> childClass, List<Class<T>> childList);
}