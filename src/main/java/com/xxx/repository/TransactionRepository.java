package com.xxx.repository;

import org.springframework.data.repository.CrudRepository;

import com.xxx.entity.Transaction;


public interface TransactionRepository extends CrudRepository<Transaction, Long>
{
}
