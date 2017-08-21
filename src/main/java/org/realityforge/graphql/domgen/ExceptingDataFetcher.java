package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExceptingDataFetcher<T>
{
  T get( @Nonnull DataFetchingEnvironment environment )
    throws Exception;

  default DataFetcher toDataFetcher()
  {
    return environment ->
    {
      try
      {
        return get( environment );
      }
      catch ( final Exception e )
      {
        throw new RuntimeException( e );
      }
    };
  }
}
