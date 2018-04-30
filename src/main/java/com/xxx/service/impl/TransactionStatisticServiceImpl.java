package com.xxx.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.xxx.entity.Transaction;
import com.xxx.exception.NApplicationException;
import com.xxx.model.dto.StatisticInfo;
import com.xxx.model.dto.TransactionInfo;
import com.xxx.repository.TransactionRepository;
import com.xxx.service.TransactionStatisticService;
import com.xxx.statistics.Statistics;

import ma.glasnost.orika.MapperFacade;

@Service
public class TransactionStatisticServiceImpl implements TransactionStatisticService
{
  private static final Logger logger = LogManager.getLogger( TransactionStatisticServiceImpl.class );

  @Autowired
  private Statistics currentStatistics;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  @Qualifier("transactionInfoMapper")
  private MapperFacade transactionInfoMapper;

  @Override
  public TransactionInfo addTransactionInfo( TransactionInfo transactionInfo )
    throws NApplicationException {
      try {
        synchronized( currentStatistics ) {
          currentStatistics.updateCurrentStatistics( transactionInfo.getTimestamp(), transactionInfo.getAmount() );
        }

        Transaction transaction = transactionInfoMapper.map( transactionInfo, Transaction.class );
        transaction = transactionRepository.save( transaction );
        return transactionInfoMapper.map( transaction, TransactionInfo.class );
      } catch( Throwable e ) {
        throw new NApplicationException( "Service processing exception. " + e );
      }
  }

  @Override
  public StatisticInfo getStatisticInfo() throws NApplicationException {
    StatisticInfo sData = null;
    try {
      synchronized( currentStatistics ) {
        sData = currentStatistics.getCurrentStatistics();
      }
    } catch( Throwable e ) {
      throw new NApplicationException( "Service processing exception. " + e );
    }
    return sData;
  }
}
