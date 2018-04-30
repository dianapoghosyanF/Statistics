package com.xxx.rest.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Validator
{
  private static final Logger logger = LogManager.getLogger( Validator.class );

  @Value("${valid.transaction.period.seconds}")
  private int validTransactionPeriodInSeconds;

  public boolean isValidTimestamp( long timestamp )
  {
    long currentUTCTime = ZonedDateTime.now( ZoneOffset.UTC ).toInstant().toEpochMilli();
    long diff = currentUTCTime - timestamp;

    logger.info(timestamp + " > 'currentUTCTime' by " + diff + " msc" );
    return ( diff >= 0 && diff <= validTransactionPeriodInSeconds );
  }
}
