package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.transaction.TransactionSynchronizationRegistry;
import org.realityforge.replicant.server.EntityMessageEndpoint;
import org.realityforge.replicant.server.transport.ReplicantSessionManager;

public abstract class AbstractReplicantGraphQLSchemaService
  extends AbstractGraphQLSchemaService
{
  @Nonnull
  protected <T> DataFetcher<T> wrapInTransaction( @Nonnull final String key, @Nonnull final DataFetcher<T> fetcher )
  {
    final ReplicantEnabledDataFetcher replicantEnabledDataFetcher =
      new ReplicantEnabledDataFetcher( getReplicantSessionManager(),
                                       getEndpoint(),
                                       getEntityManager(),
                                       getRegistry(),
                                       key,
                                       fetcher );
    return super.wrapInTransaction( key, replicantEnabledDataFetcher );
  }

  @Nonnull
  protected abstract ReplicantSessionManager getReplicantSessionManager();

  @Nonnull
  protected abstract TransactionSynchronizationRegistry getRegistry();

  @Nonnull
  protected abstract EntityManager getEntityManager();

  @Nonnull
  protected abstract EntityMessageEndpoint getEndpoint();
}
