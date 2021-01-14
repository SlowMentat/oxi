package oxi;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import java.lang.Class;
import org.springframework.beans.BeansException;

@Service
public class BeanUtil implements ApplicationContextAware{
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass){
		return context.getBean(beanClass);
	}
}