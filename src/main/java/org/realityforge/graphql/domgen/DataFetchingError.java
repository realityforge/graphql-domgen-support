package org.realityforge.graphql.domgen;

import graphql.ErrorType;
import javax.annotation.Nonnull;

public class DataFetchingError
  extends AbstractGraphQLError
{
  public DataFetchingError( @Nonnull final String message )
  {
    super( message, ErrorType.DataFetchingException );
  }
}
