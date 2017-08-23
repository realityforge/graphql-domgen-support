package org.realityforge.graphql.domgen;

import graphql.ErrorType;
import javax.annotation.Nonnull;

public class ValidationError
  extends AbstractGraphQLError
{
  public ValidationError( @Nonnull final String message )
  {
    super( message, ErrorType.ValidationError );
  }
}
