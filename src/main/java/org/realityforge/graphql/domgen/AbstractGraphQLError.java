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

  @Override
  public boolean equals( final Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( !( o instanceof AbstractGraphQLError ) )
    {
      return false;
    }
    else
    {
      final AbstractGraphQLError that = (AbstractGraphQLError) o;
      return Objects.equals( _message, that._message ) && _errorType == that._errorType;
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( _message, _errorType );
  }
}
