package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.transaction.TransactionSynchronizationRegistry;
import org.realityforge.replicant.server.EntityMessageEndpoint;
import org.realityforge.replicant.server.ee.ReplicantContextHolder;
import org.realityforge.replicant.server.ee.ReplicationRequestUtil;
import org.realityforge.replicant.shared.transport.ReplicantContext;

/**
 * This fetcher sets up replicant context.
 * It should be used for top-level fetchers where the nested fetcher is not called across a boundary
 * and thus will not have replicant context initialized. It should already have the transaction established.
 */
public class ReplicantEnabledDataFetcher
  implements DataFetcher
{
  private final EntityMessageEndpoint _endpoint;
  private final EntityManager _entityManager;
  private final TransactionSynchronizationRegistry _registry;
  private final String _name;
  private final DataFetcher _fetcher;

  public ReplicantEnabledDataFetcher( @Nonnull final EntityMessageEndpoint endpoint,
                                      @Nonnull final EntityManager entityManager,
                                      @Nonnull final TransactionSynchronizationRegistry registry,
                                      @Nonnull final String name,
                                      @Nonnull final DataFetcher fetcher )
  {
    _endpoint = Objects.requireNonNull( endpoint );
    _entityManager = Objects.requireNonNull( entityManager );
    _registry = Objects.requireNonNull( registry );
    _name = Objects.requireNonNull( name );
    _fetcher = Objects.requireNonNull( fetcher );
  }

  @Override
  public Object get( final DataFetchingEnvironment environment )
  {
    final String sessionID = (String) ReplicantContextHolder.remove( ReplicantContext.SESSION_ID_KEY );
    final String requestID = (String) ReplicantContextHolder.remove( ReplicantContext.REQUEST_ID_KEY );

    try
    {
      return ReplicationRequestUtil.runRequest( _registry,
                                                _entityManager,
                                                _endpoint,
                                                _name,
                                                sessionID,
                                                requestID,
                                                () -> _fetcher.get( environment ) );
    }
    catch ( final Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
