package com.xxx.util;

import java.lang.reflect.Field;

public class TestUtil
{
  public static <T> void setPrivateFieldValue( T obj, String fieldName, Object value )
    throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException
  {
    Field field = obj.getClass().getDeclaredField( fieldName );
    field.setAccessible( true );
    field.set( obj, value );
  }
}
