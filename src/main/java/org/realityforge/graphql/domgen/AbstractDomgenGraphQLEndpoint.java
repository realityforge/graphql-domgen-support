package org.realityforge.graphql.domgen;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.AbstractGraphQLHttpServlet;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.GraphQLSchemaProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import javax.annotation.Nonnull;
import javax.persistence.NoResultException;

public abstract class AbstractDomgenGraphQLEndpoint
  extends AbstractGraphQLHttpServlet
{
  @Nonnull
  private final GraphQLQueryInvoker _invoker = GraphQLQueryInvoker.newBuilder().build();
  @Nonnull
  private final GraphQLInvocationInputFactory _invocationInputFactory =
    GraphQLInvocationInputFactory.newBuilder( this::getSchemaProvider ).build();
  @Nonnull
  private final GraphQLObjectMapper _mapper = GraphQLObjectMapper.newBuilder().build();

  @Nonnull
  protected abstract GraphQLSchemaProvider getSchemaProvider();

  @Nonnull
  @Override
  protected GraphQLQueryInvoker getQueryInvoker()
  {
    return _invoker;
  }

  @Nonnull
  @Override
  protected GraphQLInvocationInputFactory getInvocationInputFactory()
  {
    return _invocationInputFactory;
  }

  @Override
  protected GraphQLObjectMapper getGraphQLObjectMapper()
  {
    return _mapper;
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
    return t instanceof UndeclaredThrowableException || t instanceof InvocationTargetException;
  }
}
