package org.realityforge.graphql.domgen;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.NoOpInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.servlet.DefaultExecutionStrategyProvider;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLServlet;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractDomgenGraphQLEndpoint
  extends GraphQLServlet
{
  private final DefaultExecutionStrategyProvider _executionStrategyProvider = new DefaultExecutionStrategyProvider();

  @Override
  protected GraphQLContext createContext( final Optional<HttpServletRequest> request,
                                          final Optional<HttpServletResponse> response )
  {
    return new GraphQLContext( request, response );
  }

  @Override
  protected ExecutionStrategyProvider getExecutionStrategyProvider()
  {
    return _executionStrategyProvider;
  }

  @Override
  protected Instrumentation getInstrumentation()
  {
    return NoOpInstrumentation.INSTANCE;
  }

  @Override
  protected Map<String, Object> transformVariables( final GraphQLSchema schema,
                                                    final String query,
                                                    final Map<String, Object> variables )
  {
    return variables;
  }
}
