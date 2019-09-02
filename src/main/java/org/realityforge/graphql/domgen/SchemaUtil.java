package org.realityforge.graphql.domgen;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.validation.SchemaValidator;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public final class SchemaUtil
{
  private SchemaUtil()
  {
  }

  @Nonnull
  public static GraphQLSchema buildGraphQLSchema( @Nonnull final List<String> resources,
                                                  @Nonnull final Consumer<RuntimeWiring.Builder> action )
  {
    final SchemaParser schemaParser = new SchemaParser();
    final SchemaGenerator schemaGenerator = new SchemaGenerator();

    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

    // each schema is merged into a single type registry
    for ( final String resourceName : resources )
    {
      try ( final InputStream inputStream = SchemaUtil.class.getResourceAsStream( resourceName ) )
      {
        if ( null == inputStream )
        {
          throw new IllegalStateException( "Attempting to load resource named '" + resourceName +
                                           "' to populate the GraphQL type definition registry but " +
                                           "the resource does not exist on the classpath." );
        }
        typeRegistry.merge( schemaParser.parse( new InputStreamReader( inputStream ) ) );
      }
      catch ( final IOException ioe )
      {
        throw new IllegalStateException( "Attempting to load resource named '" + resourceName +
                                         "' to populate the GraphQL type definition registry but " +
                                         "there was an error loading the resource from the classpath: " + ioe,
                                         ioe );
      }
    }

    final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
    action.accept( builder );
    final GraphQLSchema schema = schemaGenerator.makeExecutableSchema( typeRegistry, builder.build() );
    new SchemaValidator().validateSchema( schema );
    return schema;
  }
}
