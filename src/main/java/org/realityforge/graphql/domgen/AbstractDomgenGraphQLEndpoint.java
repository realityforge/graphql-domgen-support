package org.realityforge.graphql.domgen;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.execution.NonNullableFieldWasNullException;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.NoOpInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.servlet.DefaultExecutionStrategyProvider;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLServlet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractDomgenGraphQLEndpoint
  extends GraphQLServlet
{
  private final DefaultExecutionStrategyProvider _executionStrategyProvider = new DefaultExecutionStrategyProvider();

  @Override
  protected GraphQLContext createContext( final Optional<HttpServletRequest> request,
                                          final Optional<HttpServletResponse> response )
  {
    return new GraphQLContext( request, response );
  }

  @Override
  protected ExecutionStrategyProvider getExecutionStrategyProvider()
  {
    return _executionStrategyProvider;
  }

  @Override
  protected Instrumentation getInstrumentation()
  {
    return NoOpInstrumentation.INSTANCE;
  }

  @Override
  protected Map<String, Object> transformVariables( final GraphQLSchema schema,
                                                    final String query,
                                                    final Map<String, Object> variables )
  {
    return variables;
  }

  @Nonnull
  @Override
  protected List<GraphQLError> filterGraphQLErrors( @Nonnull final List<GraphQLError> errors )
  {
    return errors.stream().
      filter( e -> e instanceof ExceptionWhileDataFetching ||
                   e instanceof NonNullableFieldWasNullException ||
                   isClientError( e ) ).
      map( e -> e instanceof ExceptionWhileDataFetching ? wrapDataFetcherError( (ExceptionWhileDataFetching) e ) : e ).
      map( e -> e instanceof NonNullableFieldWasNullException ? new DataFetchingError( e.getMessage() ) : e ).
      collect( Collectors.toList() );
  }

  @Nonnull
  protected GraphQLError wrapDataFetcherError( @Nonnull final ExceptionWhileDataFetching e )
  {
    final Throwable t = unwrap( e.getException() );
    return throwableToError( t );
  }

  @Nonnull
  private GraphQLError throwableToError( @Nonnull final Throwable t )
  {
    if ( t instanceof NoResultException )
    {
      return new ValidationError( "Failed to load expected entity in database" );
    }
    else
    {
      return new DataFetchingError( getMessage( t ) );
    }
  }

  @Nonnull
  protected String getMessage( @Nonnull final Throwable t )
  {
    return "Exception while fetching data: " + t;
  }

  @Nonnull
  protected Throwable unwrap( @Nonnull final Throwable root )
  {
    Throwable t = root;
    while ( isWrapperException( t ) && null != t.getCause() )
    {
      t = t.getCause();
    }
    return t;
  }

  protected boolean isWrapperException( @Nonnull final Throwable t )
  {
    return t instanceof UndeclaredThrowableException ||
           t instanceof InvocationTargetException ||
           t instanceof WrapperRuntimeException;
  }
}
