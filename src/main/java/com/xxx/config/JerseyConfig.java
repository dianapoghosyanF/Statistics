package com.xxx.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.xxx.rest.TransactionStatisticsEndpoint;

@Component
public class JerseyConfig extends ResourceConfig
{
  public JerseyConfig()
  {
    registerEndpoints();
  }

  private void registerEndpoints()
  {
    register( TransactionStatisticsEndpoint.class );
  }
}