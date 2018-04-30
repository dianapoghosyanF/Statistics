package com.xxx.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class StatisticInfo implements Serializable
{
  protected BigDecimal sum = BigDecimal.ZERO;
  protected double avg;
  protected double max;
  protected double min;
  protected long count;

  public StatisticInfo()
  {
  }

  public StatisticInfo( BigDecimal sum, double avg, double max, double min, long count )
  {
    this.sum = sum;
    this.avg = avg;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  public BigDecimal getSum()
  {
    return sum;
  }

  public double getAvg()
  {
    return avg;
  }

  public double getMax()
  {
    return max;
  }

  public double getMin()
  {
    return min;
  }

  public long getCount()
  {
    return count;
  }

  @Override
  public boolean equals( Object o )
  {
    if ( o == null || getClass() != o.getClass() ){
      return false;
    }

    if ( this == o ) {
      return true;
    }

    StatisticInfo otherObj = (StatisticInfo)o;

    return new EqualsBuilder()
      .append( sum, otherObj.sum )
      .append( avg, otherObj.avg )
      .append( max, otherObj.max )
      .append( min, otherObj.min )
      .append( count, otherObj.count )
      .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
      .append( sum )
      .append( avg )
      .append( max )
      .append( min )
      .append( count )
      .toHashCode();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this )
      .append( sum )
      .append( avg )
      .append( max )
      .append( min )
      .append( count )
      .toString();
  }
}
