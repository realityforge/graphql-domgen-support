package org.realityforge.graphql.domgen;

import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class Scalars
{
  public static GraphQLScalarType DATE_TIME =
    new GraphQLScalarType( "DateTime", "DataTime scalar", new DateTimeCoercing() );

  private static class DateTimeCoercing
    implements Coercing<Date, String>
  {
    @Override
    public String serialize( final Object input )
    {
      final Date date = (Date) input;
      return date.toInstant().atZone( ZoneId.systemDefault() ).format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
    }

    @Override
    public Date parseValue( final Object input )
    {
      return parseLiteral( input );
    }

    @Override
    public Date parseLiteral( final Object input )
    {
      if ( input instanceof StringValue )
      {
        return Date.from( ZonedDateTime.parse( ( (StringValue) input ).getValue() ).toInstant() );
      }
      else if ( input instanceof IntValue )
      {
        final long epochMilli = ( (IntValue) input ).getValue().longValue();
        return Date.from( Instant.ofEpochMilli( epochMilli ) );
      }
      else
      {
        return null;
      }
    }
  }
}
