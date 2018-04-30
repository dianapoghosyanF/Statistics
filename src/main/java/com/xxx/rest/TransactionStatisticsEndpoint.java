package com.xxx.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.xxx.model.dto.StatisticInfo;
import com.xxx.model.dto.TransactionInfo;
import com.xxx.rest.util.Validator;
import com.xxx.service.TransactionStatisticService;

@Scope("request")
@Component
@Path("/")
public class TransactionStatisticsEndpoint
{
  private static final Logger logger = LogManager.getLogger( TransactionStatisticsEndpoint.class );

  @Autowired
  private TransactionStatisticService transactionStatisticService;

  @Autowired
  private Validator validator;

  @GET
  @Path("/statistics")
  @Produces(MediaType.APPLICATION_JSON)
  public Response retrieveStatisticInfo()
  {
    try {
      StatisticInfo statisticInfo = transactionStatisticService.getStatisticInfo();
      logger.info( "Retrieved object " + statisticInfo );
      return Response.status( Response.Status.OK ).entity( statisticInfo ).build();
    } catch( Exception e ) {
      return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( e.getMessage() ).build();
    }
  }

  @POST
  @Path("/transactions")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response populateTransactionInfo( TransactionInfo transactionInfo )
  {
    logger.info( "Creating object " + transactionInfo );
    try {
      if ( validator.isValidTimestamp( transactionInfo.getTimestamp() ) ) {
        transactionStatisticService.addTransactionInfo( transactionInfo );
        return Response.noContent().status( Response.Status.CREATED ).build();
      } else {
        return Response.noContent().status( Response.Status.NO_CONTENT ).build();
      }
    } catch( Exception e ) {
      return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( e.getMessage() ).build();
    }
  }

  @POST
  @Path("/transactionList")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response populateTransactionInfos( List<TransactionInfo> transactionInfos )
  {
    logger.info( "Creating objects " + transactionInfos );
    boolean areThereValid = true;
    try {
      for( TransactionInfo t : transactionInfos ) {
        if ( validator.isValidTimestamp( t.getTimestamp() ) ) {
          transactionStatisticService.addTransactionInfo( t );
        } else {
          areThereValid &= false;
        }
      }
    } catch( Exception e ) {
      return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( e.getMessage() ).build();
    }

    if ( areThereValid ) {
      return Response.noContent().status( Response.Status.CREATED ).build();
    } else {
      return Response.noContent().status( Response.Status.NO_CONTENT ).build();
    }
  }
}
