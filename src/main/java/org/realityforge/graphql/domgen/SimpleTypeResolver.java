package org.realityforge.graphql.domgen;

import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public class SimpleTypeResolver
  implements TypeResolver
{
  private final LinkedHashMap<Class, GraphQLObjectType> _typeMap;

  public SimpleTypeResolver( @Nonnull final LinkedHashMap<Class, GraphQLObjectType> typeMap )
  {
    _typeMap = Objects.requireNonNull( typeMap );
  }

  @Override
  public GraphQLObjectType getType( final TypeResolutionEnvironment env )
  {
    final Object object = env.getObject();
    for ( final Map.Entry<Class, GraphQLObjectType> entry : _typeMap.entrySet() )
    {
      if ( entry.getKey().isInstance( object ) )
      {
        return entry.getValue();
      }
    }
    throw new IllegalStateException( "Unable to locate object type for " + object );
  }

  public static class Builder
  {
    private final LinkedHashMap<Class, GraphQLObjectType> _typeMap = new LinkedHashMap<>();

    @Nonnull
    public Builder add( @Nonnull final Class type, @Nonnull final GraphQLObjectType objectType )
    {
      _typeMap.put( type, objectType );
      return this;
    }

    @Nonnull
    public TypeResolver build()
    {
      return new SimpleTypeResolver( _typeMap );
    }
  }
}
