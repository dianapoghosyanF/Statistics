package com.xxx.model.mapper;

import org.springframework.stereotype.Component;

import com.xxx.entity.Transaction;
import com.xxx.model.dto.TransactionInfo;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component("transactionInfoMapper")
public class TransactionInfoMapper extends ConfigurableMapper
{

  protected void configure( MapperFactory factory )
  {
    factory.classMap( Transaction.class, TransactionInfo.class )
      .field( "amount", "amount" )
      .field( "timestamp", "timestamp" )
      .byDefault()
      .register();

    factory.classMap( TransactionInfo.class, Transaction.class )
      .field( "amount", "amount" )
      .field( "timestamp", "timestamp" )
      .byDefault()
      .register();
  }
}
