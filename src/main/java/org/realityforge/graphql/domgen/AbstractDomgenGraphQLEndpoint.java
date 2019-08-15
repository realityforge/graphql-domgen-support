package org.realityforge.graphql.domgen;

import graphql.servlet.DefaultGraphQLServlet;
import graphql.servlet.config.GraphQLSchemaProvider;
import graphql.servlet.input.GraphQLInvocationInputFactory;
import javax.annotation.Nonnull;

public abstract class AbstractDomgenGraphQLEndpoint
  extends DefaultGraphQLServlet
{
  @Nonnull
  private final GraphQLInvocationInputFactory _invocationInputFactory =
    GraphQLInvocationInputFactory.newBuilder( this::getSchemaProvider ).build();

  @Nonnull
  protected abstract GraphQLSchemaProvider getSchemaProvider();

  @Nonnull
  @Override
  protected GraphQLInvocationInputFactory getInvocationInputFactory()
  {
    return _invocationInputFactory;
  }
}
