package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExceptingDataFetcher<T>
  extends DataFetcher<T>
{
}
