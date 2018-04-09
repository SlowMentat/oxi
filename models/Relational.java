package oxi.models;
import java.io.Serializable;

public interface Relational extends Serializable
{
	public <T extends Relational> void internalRemoveChild(T child);
	public <T extends Relational> void internalAddChild(T child);
}