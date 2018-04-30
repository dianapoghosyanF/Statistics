package com.xxx.model.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransactionInfo implements Serializable
{
  private double amount;
  private long timestamp;

  public TransactionInfo()
  {
  }

  public TransactionInfo( long timestamp, double amount )
  {
    super();
    this.timestamp = timestamp;
    this.amount = amount;
  }

  public double getAmount()
  {
    return amount;
  }

  public void setAmount( double amount )
  {
    this.amount = amount;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp( long timestamp )
  {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals( Object o )
  {
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    if ( this == o ) {
      return true;
    }

    TransactionInfo otherObj = (TransactionInfo)o;

    return new EqualsBuilder()
      .append( amount, otherObj.amount )
      .append( timestamp, otherObj.timestamp )
      .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
      .append( amount )
      .append( timestamp )
      .toHashCode();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this )
      .append( amount )
      .append( timestamp )
      .toString();
  }
}
