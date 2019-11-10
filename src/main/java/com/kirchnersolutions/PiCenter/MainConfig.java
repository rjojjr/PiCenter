package com.kirchnersolutions.PiCenter;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.kirchnersolutions.PiCenter.entites")
@EntityScan("com.kirchnersolutions.PiCenter.entites")
@ComponentScan({"com.kirchnersolutions.PiCenter.entites", "com.kirchnersolutions.PiCenter.Configuration", "com.kirchnersolutions.PiCenter.dev",
        "com.kirchnersolutions.PiCenter.servers.objects", "com.kirchnersolutions.PiCenter.servers",
        "com.kirchnersolutions.PiCenter.servers.rest",  "com.kirchnersolutions.PiCenter.servers.beans","com.kirchnersolutions.PiCenter.services", "com.kirchnersolutions.PiCenter.servers.socket"})
public class MainConfig {
/*
    @Autowired
    DataSource dataSource;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.acme.domain");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }


    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
/*
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jc.jdbc.Driver");
        dataSource.setUrl("mysql:jdbc://localhost:3306/shome");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
/*
    @Bean
    public SocketService socketService() throws Exception{
        return new SocketService();
    }


@Bean
public HTTPController myController2() {
    return new HTTPController();
}
/*
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public SocketService pref() throws Exception{
        return new SocketService();
    }

 */

}
