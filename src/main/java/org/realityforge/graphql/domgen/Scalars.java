package org.realityforge.graphql.domgen;

import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.annotation.Nonnull;

public final class Scalars
{
  public static GraphQLScalarType DATE_TIME =
    GraphQLScalarType.newScalar()
      .name( "DateTime" )
      .description( "DataTime scalar" )
      .coercing( new DateTimeCoercing() )
      .build();
  public static GraphQLScalarType DATE =
    GraphQLScalarType.newScalar()
      .name( "Date" )
      .description( "Date scalar" )
      .coercing( new DateCoercing() )
      .build();

  private static class DateTimeCoercing
    implements Coercing<Date, String>
  {
    @Nonnull
    @Override
    public String serialize( @Nonnull final Object input )
    {
      try
      {
        final Date date = (Date) input;
        return date.toInstant().atZone( ZoneId.systemDefault() ).format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
      }
      catch ( Exception e )
      {
        throw new CoercingSerializeException( "Failed to serialize value " + input + " as a date-time" );
      }
    }

    @Nonnull
    @Override
    public Date parseValue( @Nonnull final Object input )
    {
      final Date date = doParseValue( input );
      if ( isInvalidYear( date ) )
      {
        final String message = "Date value " + input + " must be between January 1, 1753 and December 31, 9999.";
        throw new CoercingParseValueException( message );
      }

      return date;
    }

    @Nonnull
    private Date doParseValue( @Nonnull final Object input )
    {
      if ( input instanceof String )
      {
        try
        {
          return parseDate( (String) input );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing value '" + input + "'. Expected to be in the ISO date-time " +
                                 "format with an offset, such as '2011-12-03T10:15:30+01:00'";
          throw new CoercingParseValueException( message );
        }
      }
      else if ( input instanceof Number )
      {
        try
        {
          return parseDate( ( (Number) input ).longValue() );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + input + "'. Expected to be in milliseconds since Epoch";
          throw new CoercingParseValueException( message );
        }
      }
      else
      {
        final String message = "Error parsing value " + input + " as it is the incorrect type. " +
                               "Expected a string or an integer.";
        throw new CoercingParseValueException( message );
      }
    }

    @Nonnull
    @Override
    public Date parseLiteral( @Nonnull final Object input )
    {
      final Date date = doParseLiteral( input );
      if ( isInvalidYear( date ) )
      {
        final String message = "Date value " + input + " must be between January 1, 1753 and December 31, 9999.";
        throw new CoercingParseLiteralException( message );
      }

      return date;
    }

    @Nonnull
    private Date doParseLiteral( @Nonnull final Object input )
    {
      if ( input instanceof StringValue )
      {
        final String value = ( (StringValue) input ).getValue();
        try
        {
          return parseDate( value );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + value + "'. Expected to be in the ISO date-time " +
                                 "format with an offset, such as '2011-12-03T10:15:30+01:00'";
          throw new CoercingParseLiteralException( message );
        }
      }
      else if ( input instanceof IntValue )
      {
        final BigInteger value = ( (IntValue) input ).getValue();
        try
        {
          return parseDate( value );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + value + "'. Expected to be in milliseconds since Epoch";
          throw new CoercingParseLiteralException( message );
        }
      }
      else
      {
        final String message = "Error parsing literal " + input + " as it is the incorrect type. " +
                               "Expected a string or an integer.";
        throw new CoercingParseLiteralException( message );
      }
    }

    @Nonnull
    private Date parseDate( @Nonnull final BigInteger value )
    {
      return parseDate( value.longValue() );
    }

    @Nonnull
    private Date parseDate( final long epochMilli )
    {
      return Date.from( Instant.ofEpochMilli( epochMilli ) );
    }

    @Nonnull
    private Date parseDate( @Nonnull final String value )
    {
      return Date.from( ZonedDateTime.parse( value ).toInstant() );
    }
  }

  private static class DateCoercing
    implements Coercing<Date, String>
  {
    @Nonnull
    @Override
    public String serialize( @Nonnull final Object input )
    {
      try
      {
        final Date date = (Date) input;
        return date.toInstant().atZone( ZoneId.systemDefault() ).format( DateTimeFormatter.ISO_LOCAL_DATE );
      }
      catch ( Exception e )
      {
        throw new CoercingSerializeException( "Failed to serialize value " + input + " as a date." );
      }
    }

    @Nonnull
    @Override
    public Date parseValue( @Nonnull final Object input )
    {
      final Date date = doParseValue( input );
      if ( isInvalidYear( date ) )
      {
        final String message = "Date value " + input + " must be between January 1, 1753 and December 31, 9999.";
        throw new CoercingParseValueException( message );
      }

      return date;
    }

    @Nonnull
    private Date doParseValue( @Nonnull final Object input )
    {
      if ( input instanceof String )
      {
        try
        {
          return parseDate( (String) input );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + input + "'. Expected to be in the ISO local date " +
                                 "format, such as '2011-12-03'";
          throw new CoercingParseValueException( message );
        }
      }
      else if ( input instanceof Number )
      {
        try
        {
          return parseDate( ( (Number) input ).longValue() );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + input + "'. Expected to be in milliseconds since Epoch";
          throw new CoercingParseValueException( message );
        }
      }
      else
      {
        final String message = "Error parsing literal " + input + " as it is the incorrect type. " +
                               "Expected a string or an integer.";
        throw new CoercingParseValueException( message );
      }
    }

    @Nonnull
    @Override
    public Date parseLiteral( @Nonnull final Object input )
    {
      final Date date = doParseLiteral( input );
      if ( isInvalidYear( date ) )
      {
        final String message = "Date value " + input + " must be between January 1, 1753 and December 31, 9999.";
        throw new CoercingParseLiteralException( message );
      }

      return date;
    }

    @Nonnull
    private Date doParseLiteral( @Nonnull final Object input )
    {
      if ( input instanceof StringValue )
      {
        final String value = ( (StringValue) input ).getValue();
        try
        {
          return parseDate( value );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + value + "'. Expected to be in the ISO local date " +
                                 "format, such as '2011-12-03'";
          throw new CoercingParseLiteralException( message );
        }
      }
      else if ( input instanceof IntValue )
      {
        final BigInteger value = ( (IntValue) input ).getValue();
        try
        {
          return parseDate( value );
        }
        catch ( final Throwable t )
        {
          final String message = "Error parsing literal '" + value + "'. Expected to be in milliseconds since Epoch";
          throw new CoercingParseLiteralException( message );
        }
      }
      else
      {
        final String message = "Error parsing literal " + input + " as it is the incorrect type. " +
                               "Expected a string or an integer.";
        throw new CoercingParseLiteralException( message );
      }
    }

    @Nonnull
    private Date parseDate( @Nonnull final BigInteger value )
    {
      return parseDate( value.longValue() );
    }

    @Nonnull
    private Date parseDate( final long epochMilli )
    {
      return Date.from( Instant.ofEpochMilli( epochMilli ) );
    }

    @Nonnull
    private Date parseDate( @Nonnull final String value )
    {
      return Date.from( LocalDate.parse( value ).atStartOfDay( ZoneId.systemDefault() ).toInstant() );
    }
  }

  @SuppressWarnings( { "deprecation", "ConstantConditions" } )
  private static boolean isInvalidYear( @Nonnull final Date date )
  {
    final int year = date.getYear();
    return ( 9999 - 1753 ) <= year && year >= ( 9999 - 1900 );
  }
}
