package org.realityforge.graphql.domgen;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.transaction.Status;
import javax.transaction.TransactionManager;

/**
 * This fetcher sets up transactional context.
 * It should be used for top-level fetchers where the nested fetcher is not called across a boundary
 * and thus will not have transaction context initialized.
 */
public class TransactionEnabledDataFetcher<T>
  implements DataFetcher<T>
{
  private final TransactionManager _transactionManager;
  private final DataFetcher<T> _fetcher;

  public TransactionEnabledDataFetcher( @Nonnull final TransactionManager transactionManager,
                                        @Nonnull final DataFetcher<T> fetcher )
  {
    _transactionManager = Objects.requireNonNull( transactionManager );
    _fetcher = Objects.requireNonNull( fetcher );
  }

  @Override
  public T get( @Nonnull final DataFetchingEnvironment environment )
    throws Exception
  {
    _transactionManager.begin();
    try
    {
      return _fetcher.get( environment );
    }
    catch ( final Exception e )
    {
      _transactionManager.setRollbackOnly();
      throw e;
    }
    finally
    {
      if ( Status.STATUS_ACTIVE == _transactionManager.getStatus() )
      {
        _transactionManager.commit();
      }
      else
      {
        _transactionManager.rollback();
      }
    }
  }
}
