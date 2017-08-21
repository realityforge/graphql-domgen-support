package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.transaction.TransactionSynchronizationRegistry;
import org.realityforge.replicant.server.EntityMessageEndpoint;

public abstract class AbstractReplicantGraphQLSchemaProvider
  extends AbstractGraphQLSchemaProvider
{
  @Nonnull
  protected DataFetcher wrapInTransaction( @Nonnull final String key, @Nonnull final DataFetcher fetcher )
  {
    return super.wrapInTransaction( key,
                                    new ReplicantEnabledDataFetcher( getEndpoint(),
                                                                     getEntityManager(),
                                                                     getRegistry(),
                                                                     key,
                                                                     fetcher ) );
  }

  @Nonnull
  protected abstract TransactionSynchronizationRegistry getRegistry();

  @Nonnull
  protected abstract EntityManager getEntityManager();

  @Nonnull
  protected abstract EntityMessageEndpoint getEndpoint();
}
