package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.concurrent.ContextService;
import javax.transaction.TransactionManager;

public abstract class AbstractGraphQLSchemaService
  implements GraphQLSchemaService
{
  @Nullable
  private GraphQLSchemaProvider _schemaProvider;

  protected void initSchemaProvider()
  {
    _schemaProvider =
      new DefaultGraphQLSchemaProvider( SchemaUtil.buildGraphQLSchema( getSchemaResources(), this::wireSchema ) );
  }

  protected abstract void wireSchema( @Nonnull final RuntimeWiring.Builder builder );

  @Nonnull
  @Override
  public GraphQLSchemaProvider getProvider()
  {
    assert null != _schemaProvider;
    return _schemaProvider;
  }

  /**
   * Return an ordered set of java resources that contain .graphqls files to define the schema.
   */
  @Nonnull
  protected abstract List<String> getSchemaResources();

  @SuppressWarnings( "unchecked" )
  @Nonnull
  protected <T> DataFetcher<T> topLevelDataFetcher( @Nonnull final String key,
                                                    final boolean wrapInTransaction,
                                                    @Nonnull final DataFetcher<T> fetcher )
  {
    final DataFetcher dataFetcher = wrapInTransaction ? wrapInTransaction( key, fetcher ) : fetcher;
    return (DataFetcher<T>) getContextService().createContextualProxy( dataFetcher, DataFetcher.class );
  }

  @Nonnull
  protected <T> DataFetcher<T> wrapInTransaction( @Nonnull final String key, @Nonnull final DataFetcher<T> fetcher )
  {
    return new TransactionEnabledDataFetcher<>( getTransactionManager(), fetcher );
  }

  @Nonnull
  protected abstract TransactionManager getTransactionManager();

  @Nonnull
  protected abstract ContextService getContextService();
}
