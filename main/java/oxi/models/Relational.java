package oxi.models;
import java.io.Serializable;

public interface Relational
{
	public <T extends Relational> void internalRemoveChild(T child);
	public <T extends Relational> void internalAddChild(T child);
}