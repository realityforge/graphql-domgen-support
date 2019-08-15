# Change Log

### Unreleased

* Upgrade the `graphql-java-servlet` artifact to version `8.0.0` which involved changing the artifact coordinates to `com.graphql-java-kickstart:graphql-java-servlet:jar:8.0.0`.
* Upgrade the `graphql-java` artifact to version `13.0`.

### [v1.7.0](https://github.com/realityforge/graphql-domgen-support/tree/v1.7.0) (2019-08-15)
[Full Changelog](https://github.com/realityforge/graphql-domgen-support/compare/v1.6.0...v1.7.0)

* Further improvement to the error handling when parsing and serializing dates and datetimes. Ensure that the correct exceptions are generated when parsing or serializing fails. Ensure that the date range is within those accepted by underlying tooling.

### [v1.6.0](https://github.com/realityforge/graphql-domgen-support/tree/v1.6.0) (2019-08-14)
[Full Changelog](https://github.com/realityforge/graphql-domgen-support/compare/v1.5.0...v1.6.0)

* Improve the error handling when parsing and serializing scalars.

### [v1.5.0](https://github.com/realityforge/graphql-domgen-support/tree/v1.5.0) (2019-05-13)
[Full Changelog](https://github.com/realityforge/graphql-domgen-support/compare/v1.4.0...v1.5.0)

* Incorporate generic typing of `DataFetcher` added in a later version of `graphql-java`.

### [v1.4.0](https://github.com/realityforge/graphql-domgen-support/tree/v1.4.0) (2019-05-13)
[Full Changelog](https://github.com/realityforge/graphql-domgen-support/compare/v1.3.0...v1.4.0)

* Remove `ExceptingDataFetcher` as the `DataFetcher` supplied by `graphql-java` now throws an exception.
* Update the `ReplicantEnabledDataFetcher` class and the `TransactionEnabledDataFetcher` class to throw
  exceptions directly rather than wrapping in a `WrapperRuntimeException` now that the underlying `DataFetcher`
  supplied by the `graphql-java` library throws an exception.
* Remove `WrapperRuntimeException` now that it is unused.
* Remove unused methods from `AbstractDomgenGraphQLEndpoint` which includes; `wrapDataFetcherError(...)`,
  `getMessage(...)`, `throwableToError(...)`, `unwrap(...)` and `isWrapperException(...)`.
* Remove unused class `ValidationError`.
* Remove unused class `DataFetchingError`.
* Remove unused class `AbstractGraphQLError`.
* Replace `AbstractGraphQLSchemaProvider` with `AbstractGraphQLSchemaService` that has a service method that
  returns the `GraphQLSchemaProvider` instance.
* Update `AbstractGraphQLSchemaService` to use graphqls to define schemas.

### [v1.3.0](https://github.com/realityforge/graphql-domgen-support/tree/v1.3.0) (2019-05-10)
[Full Changelog](https://github.com/realityforge/graphql-domgen-support/compare/v1.2.0...v1.3.0)

* Upgrade the `org.realityforge.replicant:replicant-server:jar` artifact to version `6.43`.
* Decouple from `commons-lang` that was used when defining `toString()` and `hashcode()`.
* Improve nullability annotations.
* Add `Date` scalar type.
* Upgrade the `com.graphql-java:graphql-java:jar` artifact to version `12.0`.

### [v1.2.0](https://github.com/replicant4j/replicant/tree/v1.2.0)
[Full Changelog](https://github.com/replicant4j/replicant/compare/v1.1.0...v1.2.0)

* Some basic work arounds to get some meaningful exceptions emitted.

### [v1.1.0](https://github.com/replicant4j/replicant/tree/v1.1.0)
[Full Changelog](https://github.com/replicant4j/replicant/compare/v1.0.0...v1.1.0)

* Initial integration with replicant.

### [v1.0.0](https://github.com/replicant4j/replicant/tree/v1.0.0)

* Initial release
