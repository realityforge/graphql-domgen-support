package org.realityforge.graphql.domgen;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractGraphQLError
  implements GraphQLError
{
  private final String _message;
  private final ErrorType _errorType;

  public AbstractGraphQLError( @Nonnull final String message, @Nonnull final ErrorType errorType )
  {
    _message = Objects.requireNonNull( message );
    _errorType = Objects.requireNonNull( errorType );
  }

  @Nonnull
  @Override
  public String getMessage()
  {
    return _message;
  }

  @Nullable
  @Override
  public List<SourceLocation> getLocations()
  {
    return null;
  }

  @Nonnull
  @Override
  public ErrorType getErrorType()
  {
    return _errorType;
  }

  @SuppressWarnings( "EqualsWhichDoesntCheckParameterClass" )
  @Override
  public boolean equals( Object o )
  {
    return Helper.equals( this, o );
  }

  @Override
  public int hashCode()
  {
    return Helper.hashCode( this );
  }
}
