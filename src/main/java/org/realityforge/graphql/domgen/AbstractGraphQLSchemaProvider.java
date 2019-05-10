package org.realityforge.graphql.domgen;

import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import graphql.servlet.GraphQLSchemaProvider;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.concurrent.ContextService;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.TransactionManager;

public abstract class AbstractGraphQLSchemaProvider
  implements GraphQLSchemaProvider
{
  private final HashMap<String, GraphQLScalarType> _scalarTypes = new HashMap<>();
  private final HashMap<String, GraphQLEnumType> _enumTypes = new HashMap<>();
  private final HashMap<String, GraphQLInterfaceType> _interfaceTypes = new HashMap<>();
  private final HashMap<String, GraphQLInputType> _inputTypes = new HashMap<>();
  private final HashMap<String, GraphQLObjectType> _objectTypes = new HashMap<>();

  private GraphQLSchema _schema;

  protected void postConstruct()
  {
    _schema = buildGraphQLSchema();
  }

  @Override
  public GraphQLSchema getSchema( @Nonnull final HttpServletRequest request )
  {
    return _schema;
  }

  @Override
  public GraphQLSchema getSchema()
  {
    return _schema;
  }

  @Override
  public GraphQLSchema getReadOnlySchema( final HttpServletRequest request )
  {
    return _schema;
  }

  @Nonnull
  private GraphQLSchema buildGraphQLSchema()
  {
    populateGraphQLSchema();
    final GraphQLObjectType query = _objectTypes.get( "Query" );
    if ( null == query )
    {
      throw new IllegalStateException( "Unable to build schema as no Query object type has been defined" );
    }
    final GraphQLObjectType mutation = _objectTypes.get( "Mutation" );
    final GraphQLObjectType subscription = _objectTypes.get( "Subscription" );
    final HashSet<GraphQLType> dictionary = new HashSet<>();
    dictionary.addAll( _scalarTypes.values() );
    dictionary.addAll( _enumTypes.values() );
    dictionary.addAll( _interfaceTypes.values() );
    dictionary.addAll( _objectTypes.values() );

    return new GraphQLSchema.Builder().
      query( query ).
      mutation( mutation ).
      subscription( subscription ).
      build( dictionary );
  }

  protected abstract void populateGraphQLSchema();

  protected void registerStandardScalarTypes()
  {
    registerScalarTypeUnlessRegistered( Scalars.GraphQLString );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLBigDecimal );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLBigInteger );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLBoolean );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLByte );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLChar );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLFloat );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLID );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLInt );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLLong );
    registerScalarTypeUnlessRegistered( Scalars.GraphQLShort );
  }

  protected void addFieldUnlessNull( @Nonnull final GraphQLInputObjectType.Builder objectType,
                                     @Nullable final GraphQLInputObjectField field )
  {
    if ( null != field )
    {
      objectType.field( field );
    }
  }

  protected void addFieldUnlessNull( @Nonnull final GraphQLInterfaceType.Builder objectType,
                                     @Nullable final GraphQLFieldDefinition field )
  {
    if ( null != field )
    {
      objectType.field( field );
    }
  }

  protected void addFieldUnlessNull( @Nonnull final GraphQLObjectType.Builder objectType,
                                     @Nullable final GraphQLFieldDefinition field )
  {
    if ( null != field )
    {
      objectType.field( field );
    }
  }

  protected boolean isScalarTypeRegistered( @Nonnull final GraphQLScalarType scalarType )
  {
    return _scalarTypes.containsKey( scalarType.getName() );
  }

  private void registerScalarTypeUnlessRegistered( @Nonnull final GraphQLScalarType scalarType )
  {
    if ( !isScalarTypeRegistered( scalarType ) )
    {
      registerScalarType( scalarType );
    }
  }

  protected void registerScalarTypeUnlessNull( @Nullable final GraphQLScalarType scalarType )
  {
    if ( null != scalarType )
    {
      registerScalarType( scalarType );
    }
  }

  protected void registerScalarType( @Nonnull final GraphQLScalarType scalarType )
  {
    final String name = scalarType.getName();
    if ( _scalarTypes.containsKey( name ) )
    {
      final String message =
        String.format( "Attempting to register scalarType '%s' that duplicates existing scalarType", name );
      throw new IllegalStateException( message );
    }
    _scalarTypes.put( name, scalarType );
  }

  @Nonnull
  protected GraphQLScalarType scalarType( @Nonnull final String key )
  {
    final GraphQLScalarType scalarType = _scalarTypes.get( key );
    if ( null == scalarType )
    {
      final String message = String.format(
        "Unable to locate scalarType '%s'. Please ensure it is defined prior to invoking scalarType() method",
        key );
      throw new IllegalStateException( message );
    }
    return scalarType;
  }

  protected boolean isEnumTypeRegistered( @Nonnull final GraphQLEnumType enumType )
  {
    return _enumTypes.containsKey( enumType.getName() );
  }

  protected void registerEnumTypeUnlessNull( @Nullable final GraphQLEnumType enumType )
  {
    if ( null != enumType )
    {
      registerEnumType( enumType );
    }
  }

  protected void registerEnumType( @Nonnull final GraphQLEnumType enumType )
  {
    final String name = enumType.getName();
    if ( _enumTypes.containsKey( name ) )
    {
      final String message =
        String.format( "Attempting to register enumType '%s' that duplicates existing enumType", name );
      throw new IllegalStateException( message );
    }
    _enumTypes.put( name, enumType );
  }

  @Nonnull
  protected GraphQLEnumType enumType( @Nonnull final String key )
  {
    final GraphQLEnumType enumType = _enumTypes.get( key );
    if ( null == enumType )
    {
      final String message = String.format(
        "Unable to locate enumType '%s'. Please ensure it is defined prior to invoking enumType() method",
        key );
      throw new IllegalStateException( message );
    }
    return enumType;
  }

  protected boolean isInterfaceTypeRegistered( @Nonnull final GraphQLInterfaceType interfaceType )
  {
    return _interfaceTypes.containsKey( interfaceType.getName() );
  }

  protected void registerInterfaceTypeUnlessNull( @Nullable final GraphQLInterfaceType interfaceType )
  {
    if ( null != interfaceType )
    {
      registerInterfaceType( interfaceType );
    }
  }

  protected void registerInterfaceType( @Nonnull final GraphQLInterfaceType interfaceType )
  {
    final String name = interfaceType.getName();
    if ( _interfaceTypes.containsKey( name ) )
    {
      final String message =
        String.format( "Attempting to register interfaceType '%s' that duplicates existing interfaceType", name );
      throw new IllegalStateException( message );
    }
    _interfaceTypes.put( name, interfaceType );
  }

  @Nonnull
  protected GraphQLInterfaceType interfaceType( @Nonnull final String key )
  {
    final GraphQLInterfaceType interfaceType = _interfaceTypes.get( key );
    if ( null == interfaceType )
    {
      final String message = String.format(
        "Unable to locate interfaceType '%s'. Please ensure it is defined prior to invoking interfaceType() method",
        key );
      throw new IllegalStateException( message );
    }
    return interfaceType;
  }

  protected void registerInputTypeUnlessNull( @Nullable final GraphQLInputType inputType )
  {
    if ( null != inputType )
    {
      registerInputType( inputType );
    }
  }

  protected void registerInputType( @Nonnull final GraphQLInputType inputType )
  {
    final String name = inputType.getName();
    if ( _inputTypes.containsKey( name ) )
    {
      final String message =
        String.format( "Attempting to register inputType '%s' that duplicates existing inputType", name );
      throw new IllegalStateException( message );
    }
    _inputTypes.put( name, inputType );
  }

  @Nonnull
  protected GraphQLInputType inputType( @Nonnull final String key )
  {
    final GraphQLInputType inputType = _inputTypes.get( key );
    if ( null == inputType )
    {
      final String message = String.format(
        "Unable to locate inputType '%s'. Please ensure it is defined prior to invoking inputType() method",
        key );
      throw new IllegalStateException( message );
    }
    return inputType;
  }

  protected void registerObjectTypeUnlessNull( @Nullable final GraphQLObjectType objectType )
  {
    if ( null != objectType )
    {
      registerObjectType( objectType );
    }
  }

  protected void registerObjectType( @Nonnull final GraphQLObjectType objectType )
  {
    final String name = objectType.getName();
    if ( _objectTypes.containsKey( name ) )
    {
      final String message =
        String.format( "Attempting to register objectType '%s' that duplicates existing objectType", name );
      throw new IllegalStateException( message );
    }
    _objectTypes.put( name, objectType );
  }

  @Nonnull
  protected GraphQLObjectType objectType( @Nonnull final String key )
  {
    final GraphQLObjectType objectType = _objectTypes.get( key );
    if ( null == objectType )
    {
      final String message = String.format(
        "Unable to locate objectType '%s'. Please ensure it is defined prior to invoking objectType() method",
        key );
      throw new IllegalStateException( message );
    }
    return objectType;
  }

  @Nonnull
  protected DataFetcher wrapTopLevelDataFetcher( @Nonnull final String key,
                                                 final boolean wrapInTransaction,
                                                 @Nonnull final ExceptingDataFetcher base )
  {
    return wrapTopLevelRawDataFetcher( key, wrapInTransaction, base.toDataFetcher() );
  }

  @Nonnull
  protected DataFetcher wrapTopLevelRawDataFetcher( @Nonnull final String key,
                                                    final boolean wrapInTransaction,
                                                    @Nonnull final DataFetcher fetcher )
  {
    final DataFetcher wrapped = ( wrapInTransaction ) ? wrapInTransaction( key, fetcher ) : fetcher;
    return getContextService().createContextualProxy( wrapped, DataFetcher.class );
  }

  @Nonnull
  protected DataFetcher wrapInTransaction( @Nonnull final String key, @Nonnull final DataFetcher fetcher )
  {
    return new TransactionEnabledDataFetcher( getTransactionManager(), fetcher );
  }

  @Nonnull
  protected abstract TransactionManager getTransactionManager();

  @Nonnull
  protected abstract ContextService getContextService();
}
