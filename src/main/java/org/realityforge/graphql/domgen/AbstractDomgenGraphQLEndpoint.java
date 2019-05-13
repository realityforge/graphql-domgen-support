package org.realityforge.graphql.domgen;

import graphql.servlet.AbstractGraphQLHttpServlet;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.GraphQLSchemaProvider;
import javax.annotation.Nonnull;

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
}
