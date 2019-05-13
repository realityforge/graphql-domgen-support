package org.realityforge.graphql.domgen;

import graphql.servlet.GraphQLSchemaProvider;
import javax.annotation.Nonnull;

public interface GraphQLSchemaService
{
  @Nonnull
  GraphQLSchemaProvider getProvider();
}
