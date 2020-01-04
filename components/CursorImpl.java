package oxi.components;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Extension of {@code Page} implementation to store before and after Ids.
 *
 * @param <T> the type of which the page consists.
 * @author Deven Bryant
 */
public class CursorImpl<T> extends PageImpl<T>{

	private String beforeId = null;
	private String afterId = null;

	public CursorImpl(List<T> content, Pageable pageable, long total){
		super(content, pageable, total);
	}

	public CursorImpl(List<T> content){
		super(content);
	}

	public String getBeforeId(){return this.beforeId;}
	public String getAfterId(){return this.afterId;}

	public void setBeforeId(String beforeId){this.beforeId = beforeId;}
	public void setAfterId(String afterId){this.afterId = afterId;}

}