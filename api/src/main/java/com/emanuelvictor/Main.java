package com.emanuelvictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class Main {

    static final public String DEFAULT_TENANT_IDENTIFICATION = "public";

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}