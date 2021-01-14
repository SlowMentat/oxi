package oxi.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
//mport org.springframework.context.annotation.Bean;
//mport org.springframework.context.annotation.Configuration;
//mport org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.test.context.*;


@Configuration
@EnableJpaRepositories(basePackages = {"oxi.repositories"})
@EntityScan(basePackages="oxi.models")
@ComponentScan(basePackages = {"oxi.configs", "oxi.components"})
//@ComponentScan
//@PropertySource("application.properties")
//@TestPropertySource(locations="/application-test.properties")
//@TestPropertySource(locations = {"application-test.properties"})
@EnableTransactionManagement
//@ComponentScan(basePackages = {"oxi.configs"})//removed oxi.security
@Profile("test")
public class JpaConfig {

	@Autowired
	private Environment env;
	
	//@Autowired
    //private PropertySourcesPropertyResolver propertySourceResolver;

    //@Bean
    //public static PropertyPlaceholderConfigurer properties(){
    //	PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
    //	Resource[] resources = new ClassPathResource[]{ new ClassPathResource( "/resources/application-test.properties" ) };
    //	ppc.setLocations( resources );
    //	ppc.setIgnoreUnresolvablePlaceholders( true );
    //	ppc.setOrder( Ordered.HIGHEST_PRECEDENCE );

    //	return ppc;
	//}

	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		//dataSource.setSchema(env.getProperty("spring.datasource.schema"));

        //dataSource.setDriverClassName("org.h2.Driver");
        //dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        //dataSource.setUsername("sa");
        //dataSource.setPassword("sa");

		return dataSource;
	}

	// configure entityManagerFactory
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] {"oxi.models", "oxi.repositories"});
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(additionalProperties());

		return em;
	}

	// configure transactionManager
	@Bean 
	JpaTransactionManager transactionManager(EntityManagerFactory emf){
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);

		return tm;
	}

	// configure additional Hibernate Properties

	final Properties additionalProperties(){

		final Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		//hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		//hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
		//hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));

		//hibernateProperties.setProperty("spring.jpa.hibernate.hbm2ddl.auto", "none");
		//hibernateProperties.setProperty("spring.jpa.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		//hibernateProperties.setProperty("spring.jpa.hibernate.show_sql", "true");

		return hibernateProperties;		
	}
}