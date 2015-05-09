package com.example.rs;

import com.example.config.AppConfig;
import com.example.config.RestServiceDetails;
import com.example.model.Person;
import com.netflix.curator.x.discovery.ServiceDiscovery;
import com.netflix.curator.x.discovery.ServiceInstance;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collection;

@Path( PeopleRestService.PEOPLE_PATH ) 
public class PeopleRestService {
	public static final String PEOPLE_PATH = "/zookeeper";
	
	@Inject private JaxRsApiApplication application;
	@Inject private ServiceDiscovery< RestServiceDetails > discovery;	
	@Inject private Environment environment;
	
	@PostConstruct
	public void init() throws Exception {
		final ServiceInstance< RestServiceDetails > instance = 
			ServiceInstance.< RestServiceDetails >builder()
	            .name( "zookeeper" )
	            .payload( new RestServiceDetails( "1.0" ) )
	            .port( environment.getProperty( AppConfig.SERVER_PORT, Integer.class ) )
	            .uriSpec( application.getUriSpec( PEOPLE_PATH ) )
	            .build();
		
		discovery.registerService( instance );
	}
	
	@Produces( { MediaType.APPLICATION_JSON } )
	@GET
	public Collection< Person > getPeople( @QueryParam( "page") @DefaultValue( "1" ) final int page ) {
		return Arrays.asList(
			new Person( "nkukday@gmail.com","Neeraja", "Kukday" ),
			new Person( "cmpe@sjsu.edu","CMPE", "Department" )
		);
	}
}
