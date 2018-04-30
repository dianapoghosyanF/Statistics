package com.xxx.service;

import com.xxx.exception.NApplicationException;
import com.xxx.model.dto.StatisticInfo;
import com.xxx.model.dto.TransactionInfo;

public interface TransactionStatisticService
{
  TransactionInfo addTransactionInfo( TransactionInfo transactionInfo ) throws NApplicationException;
  StatisticInfo getStatisticInfo() throws NApplicationException;
}
