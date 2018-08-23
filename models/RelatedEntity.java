package oxi.models;

import java.util.List;
import java.io.Serializable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class RelatedEntity implements Relational 
{
	private static final Logger logger = LogManager.getLogger(RelatedEntity.class);
	
	/**
	 * Helper method for modifying an Entity property with a 
	 * ManyToOne relation.  Updates the state of the parent 
	 * Entity to reflect the new child object.
	 *
	 * @param  parent  			The new parent object with which to update OneToMany annotated property in the Child object.
	 * @Param  currentParent	This is the Parent object to which the Child Object currently references.
	 * @param  childEntity		The calling object containing the OneToMany mapped property referencing the parent object.
	 * @return      			Relaitonal
	*/
	public <T extends Relational, S extends Relational> T setManyToOneParent(T parent, T currentParent, S childEntity){
		//Check if there is already a Parent Object associated with this Child Object.
		//If so remove this Child object from the refereced Parent object's List<Child> property
		//and reference this Child object to the passed Parent Object.  If the passed Parent Object is not null,
		//add this Child object to the referenced Parent Object's List<Child>.
		if(currentParent != null){
			logger.debug("Removing Child object from List<Child> property of Parent object");
			currentParent.internalRemoveChild(childEntity);
		}
		logger.warn("Referencing Child.parent to Parent object");
		//currentParent = parent;
		if(parent != null){
			logger.debug("Parent reference (before adding child):  " + parent.toString());
			logger.debug("Adding Child to List<Child> property of Parent object");
			parent.internalAddChild(childEntity);
			logger.debug("Parent reference (after adding child):  " + parent.toString());
		}
		return parent;
	}

	public <T extends Relational, S extends Relational> T setManyToOneParentByType(T parent, T currentParent, S childEntity, List<S> childEntities){
		if(currentParent != null){
			logger.debug("Removing Child object from List<Child> property of Parent object");
			currentParent.internalRemoveChildByType(childEntity, T.class, childEntities);
		}
		logger.warn("Referencing Child.parent to Parent object");
		//currentParent = parent;
		if(parent != null){
			logger.debug("Parent reference (before adding child):  " + parent.toString());
			logger.debug("Adding Child to List<Child> property of Parent object");
			parent.internalAddChildByType(childEntity, T.class, childEntities);
			logger.debug("Parent reference (after adding child):  " + parent.toString());
		}
		return parent;
	}
	
	/**
	* Method for modifying the Entity property having a ManyToMany relaiton.
	* This method updates the state of the parent Entity to reflect the addition of the child object.
	*
	* @param  parents  			The List<Relational> of parent objects with which to update ManyToMany annotated property of the Child object.
	* @Param  currentParents	This List<Relational> of parent objects to which the Child Object currently references.
	* @param  childEntity		The calling object containing the ManyToMany mapped List<Relational> referencing the parent objects.
	* @return      				List<Relaitonal>
	**/
	public <T extends Relational, S extends Relational> List<T> setManyToManyParents(List<T> parents, List<T> currentParents, S childEntity){
		if(currentParents != null){
			for(T currentParent : currentParents){
				if(currentParent != null){
					logger.debug("Removing Child object from List<Child> property of Parent object");
					currentParent.internalRemoveChild(childEntity);
				}
				logger.debug("currentParent is NULL");
			}
		}
		logger.debug("currentParents List is NULL");
		logger.debug("Referencing Child.parent to Parent object");
		if(parents != null){
			for(T parent : parents){
				logger.debug("Adding childEntity to parents");
				//logger.debug("Parent reference (before adding child):  " + parent.toString());
				//logger.debug("Adding Child to List<Child> property of Parent object");
				if(parent != null){
					parent.internalAddChild(childEntity);
				}else{
					logger.debug("parent element in parents is null" );
				}
				//logger.debug("Parent reference (after adding child):  " + parent.toString());
			}
		}
		return parents;
	}
	
	@Override
	public <T extends Relational> void internalAddChild(T child/*, List<T> currentChilderen*/){
		/*if(currentChilderen == null){
			logger.debug("instantiating new List<T>");
			currentChilderen = new ArrayList<T>();
		}
		currentChilderen.add(child);*/		
	}
	
	@Override
	public <T extends Relational> void internalRemoveChild(T child/*, List<T> currentChilderen*/){
		/*currentChilderen.remove(child);*/
	}

	@Override
	public <T extends Relational> void internalAddChildByType(T targetChild, Class<T> childClass, List<Class<T>> childList){

	}

	@Override
	public <T extends Relational> void internalRemoveChildByType(T targetChild, Class<T> childClass, List<Class<T>> childList){

	}

}