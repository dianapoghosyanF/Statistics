package com.xxx.statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xxx.model.dto.StatisticInfo;

@Component
public final class Statistics {
  private static final Logger logger = LogManager.getLogger( Statistics.class );

  private final int MONITORING_CYCLE;//= 60
  private final static int MSC = 1000;

  private long cycleLastMonitoredTime = 0;

  private StatisticsForOneSecond lastStatistics;
  private StatisticsForOneSecond[] statisticsData;

  private StatisticsForOneSecond DUMMY_OBJECT = new StatisticsForOneSecond( 0 );

  public Statistics ( @Value("${valid.transaction.period.seconds}") final int monitoringCycle ) {
    this.MONITORING_CYCLE = monitoringCycle / MSC;
    statisticsData = new StatisticsForOneSecond[MONITORING_CYCLE];
  }

  public synchronized void updateCurrentStatistics( long timestamp, double amount ) {
    logger.info( "Start updating statistics info by new data : { " + timestamp + ", " + amount +"}" );

    long currentTime = ZonedDateTime.now( ZoneOffset.UTC ).toInstant().toEpochMilli();
    long diff = currentTime - timestamp;

    if ( diff < 0 || diff >= MONITORING_CYCLE * MSC ) {
      throw new IllegalArgumentException( "Incorrect index value param" );
    }

    int index = (int)( ( diff / MSC ) % MONITORING_CYCLE );

    if ( cycleLastMonitoredTime == 0 ) {
      update( index, amount );
    } else {

      int passed = (int)( ( currentTime - cycleLastMonitoredTime ) / MSC );

      logger.info( "Statistics data was updated " + passed + " seconds ago" );

      int passedMonitoredCycleCount = passed / MONITORING_CYCLE;
      int currentCycleMilisc = passed % MONITORING_CYCLE;

      if ( passedMonitoredCycleCount < 1 ) {
        if ( passed != 0 ) {
          rotate( currentCycleMilisc );
        }
        update( index, amount );
      } else {
        clear();
        update( index, amount );
      }
    }
    cycleLastMonitoredTime = currentTime;

    logger.info( "End updating statistics info by new data : { " + timestamp + ", " + amount +"}" );
  }

  public synchronized StatisticInfo getCurrentStatistics() {
    logger.info( "Start retrieving statistics info for last " + MONITORING_CYCLE + " seconds" );

    StatisticsForOneSecond result = DUMMY_OBJECT;

    if ( cycleLastMonitoredTime != 0 ) {
      long currentTime = ZonedDateTime.now( ZoneOffset.UTC ).toInstant().toEpochMilli();

      int passed = (int)( ( currentTime - cycleLastMonitoredTime ) / MSC );
      int passedMonitoredCycleCount = passed / MONITORING_CYCLE;
      int currentCycleMilisc = passed % MONITORING_CYCLE;

      if ( passedMonitoredCycleCount < 1 ) {
        if ( passed != 0 ) {
          rotate( currentCycleMilisc );
        }
        result = calculateCurrentStatistics();
        cycleLastMonitoredTime = currentTime;
      } else {
        cycleLastMonitoredTime = 0;
        clear();
      }
    }

    logger.info( "End retrieving statistics info for last " + MONITORING_CYCLE + " seconds: " + result  );
    return new StatisticInfo ( result.sum, result.avg, result.max, result.min, result.count );
  }

  private StatisticsForOneSecond update( int index, double amount ) {
    logger.info( "Updating statistics data with index " + index );

    if ( index < 0 || index >= MONITORING_CYCLE ) {
      throw new IllegalArgumentException( "Incorrect index value param" );
    }

    if (statisticsData[index] == null ) {
      statisticsData[index] = new StatisticsForOneSecond();
    }
    statisticsData[index].update( amount );

    return calculateCurrentStatistics();
  }

  private StatisticsForOneSecond calculateCurrentStatistics() {

    StatisticsForOneSecond last = null;

    for( int i = 0; i < MONITORING_CYCLE; i++ ) {
      if ( statisticsData[i] != null ) {

        StatisticsForOneSecond prev = last;
        last = statisticsData[i];
        if ( prev == null ) {
          prev = new StatisticsForOneSecond();
          prev.currentStatistics = new StatisticsForOneSecond();
        }
        last.updateCurrent( prev.currentStatistics );
      }
    }

    lastStatistics = ( last != null ? last.currentStatistics : DUMMY_OBJECT );

    return lastStatistics;
  }

  private void rotate( int rotationCount ) {
    logger.info( "Rotating statistics data by " + rotationCount + " step" );

    for( int i = MONITORING_CYCLE - 1; i > rotationCount; i-- ) {
      statisticsData[i] = statisticsData[i - 1];
    }
    Arrays.fill( statisticsData, 0, rotationCount + 1, null );
  }

  private void clear() {
    Arrays.fill( statisticsData, null );
  }

  final class StatisticsForOneSecond {

    private BigDecimal sum = BigDecimal.ZERO;
    private double avg;
    private double max;
    private double min = Double.MAX_VALUE;
    private long count;

    private StatisticsForOneSecond currentStatistics;

    StatisticsForOneSecond (){}

    StatisticsForOneSecond ( double min ){
      this.min = min;
    }

    private void update( double amount ) {

      this.count++;
      this.sum = this.sum.add( BigDecimal.valueOf( amount ) );
      this.max = Math.max( this.max, amount );
      this.min = Math.min( this.min, amount );
      this.avg = this.sum.divide( BigDecimal.valueOf( this.count ), MathContext.DECIMAL64).doubleValue();

      if ( currentStatistics == null ) {
        currentStatistics = new StatisticsForOneSecond();
      }
    }

    private void updateCurrent( StatisticsForOneSecond prev ) {
      if ( prev == null ) return;

      this.currentStatistics.count = this.count + prev.count;
      this.currentStatistics.sum = this.sum.add( prev.sum );
      this.currentStatistics.max = Math.max( this.max, prev.max );
      this.currentStatistics.min = Math.min( this.min, prev.min );
      this.currentStatistics.avg = this.currentStatistics.sum.divide(
        BigDecimal.valueOf(this.currentStatistics.count ), MathContext.DECIMAL64).doubleValue();
    }

    @Override
    public String toString()
    {
      return "StatisticsForOneSecond{" +
        "sum=" + sum +
        ", avg=" + avg +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        '}';
    }
  }
}