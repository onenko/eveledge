package net.nenko.run.eveledge;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.nenko.eveledge.Engine;
import net.nenko.eveledge.Persister;
import net.nenko.eveledge.PersisterInMemory;
import net.nenko.eveledge.PropStringifier;
import net.nenko.eveledge.PropStringifierUnknown;

@Configuration
public class AppConfig {
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
    DataSource dataSource() {
//    DataSource realDataSource() {
        DataSource dataSource = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword())
                .build();
        return dataSource;
    }

    @Bean
    public Engine engine(Persister persisterInMemory) {
    	return new Engine(persisterInMemory);
    }

	@Bean
	public Persister persisterInMemory(PropStringifier propStringifier) {
		return new PersisterInMemory(propStringifier);
	}

	@Bean
	public PropStringifier propStringifier() {
		return new PropStringifier(new PropStringifierUnknown());
	}
	
}