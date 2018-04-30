package com.xxx.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "xxx_transaction")
public class Transaction implements Serializable
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private BigDecimal amount;
  private long timestamp;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount( BigDecimal amount )
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

    Transaction otherObj = (Transaction)o;

    return new EqualsBuilder()
      .append( id, otherObj.id )
      .append( amount, otherObj.amount )
      .append( timestamp, otherObj.timestamp )
      .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
      .append( id )
      .append( amount )
      .append( timestamp )
      .toHashCode();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this )
      .append( id )
      .append( amount )
      .append( timestamp )
      .toString();
  }
}
