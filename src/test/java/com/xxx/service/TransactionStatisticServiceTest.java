package com.xxx.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xxx.StatisticsApplication;
import com.xxx.entity.Transaction;
import com.xxx.model.dto.StatisticInfo;
import com.xxx.model.dto.TransactionInfo;
import com.xxx.repository.TransactionRepository;
import com.xxx.service.impl.TransactionStatisticServiceImpl;
import com.xxx.statistics.Statistics;
import com.xxx.util.TestUtil;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import ma.glasnost.orika.MapperFacade;

public class TransactionStatisticServiceTest extends StatisticsApplication
{
  private TransactionStatisticService transactionStatisticService;

  @Mock
  private TransactionRepository mockTransactionRepository;

  @Mock
  private MapperFacade mockTransactionInfoMapper;

  private static final int N = 5;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    transactionStatisticService = new TransactionStatisticServiceImpl();
    try {
      TestUtil.setPrivateFieldValue( transactionStatisticService,
        "transactionInfoMapper", mockTransactionInfoMapper );

      TestUtil.setPrivateFieldValue( transactionStatisticService,
        "transactionRepository", mockTransactionRepository );

      TestUtil.setPrivateFieldValue( transactionStatisticService,
        "currentStatistics", new Statistics(60000 ) );
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  @UseDataProvider("inputTestData")
  @Test
  public void addTransactionInfoTest(
    List<TransactionInfo> transactionInfoList,
    StatisticInfo expectedStatsInfo )
  {
    transactionInfoList.forEach( t -> transactionStatisticService.addTransactionInfo( t ) );

    when(
      mockTransactionInfoMapper.map( any( TransactionInfo.class ), any( Class.class ) )
    ).thenReturn( new Transaction() );

    StatisticInfo actualStatisticsInfo = transactionStatisticService.getStatisticInfo();

    verify( mockTransactionRepository, times( transactionInfoList.size() ) ).save( any() );
    assertEquals( expectedStatsInfo, actualStatisticsInfo );
  }

  @DataProvider
  public static Object[][] inputTestData() {
    List<TransactionInfo> transactionInfoList = new ArrayList<>();

    Instant currentUTCTime = ZonedDateTime.now( ZoneOffset.UTC ).toInstant();
    final double baseAmount = 10.0;

    double[] amounts = new double[N];

    for( int i = 0; i < N; i++ ) {
      long timestamp = currentUTCTime.minusSeconds( i ).toEpochMilli();
      TransactionInfo transactionInfo = new TransactionInfo( timestamp, amounts[i] = baseAmount * i );
      transactionInfoList.add( transactionInfo );
    }

    DoubleSummaryStatistics sts = DoubleStream.of( amounts ).
      collect( DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept,
        DoubleSummaryStatistics::combine );

    StatisticInfo statsInfo = new StatisticInfo( BigDecimal.valueOf( sts.getSum() ),
      sts.getAverage(), sts.getMax(), sts.getMin(), sts.getCount() );

    return new Object[][] { { transactionInfoList, statsInfo } };
  }
}

