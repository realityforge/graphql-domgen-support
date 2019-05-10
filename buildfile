require 'buildr/git_auto_version'
require 'buildr/gpg'

JACKSON_DEPS = %w(
  com.fasterxml.jackson.core:jackson-annotations:jar:2.8.11
  com.fasterxml.jackson.core:jackson-core:jar:2.8.11
  com.fasterxml.jackson.core:jackson-databind:jar:2.8.11
  com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.8.11
)

GRAPHQL_TOOLS_DEPS = %w(
  com.graphql-java:graphql-java:jar:12.0
  org.slf4j:slf4j-api:jar:1.7.25
  org.slf4j:slf4j-jdk14:jar:1.7.25
  org.antlr:antlr4-runtime:jar:4.7.2
  com.graphql-java:java-dataloader:jar:2.1.1
  org.reactivestreams:reactive-streams:jar:1.0.2
  com.graphql-java:graphql-java-servlet:jar:6.1.3
  commons-fileupload:commons-fileupload:jar:1.3.3
  commons-io:commons-io:jar:2.5
  com.google.guava:guava:jar:25.0-jre
)

REPLICANT_DEPS = %w(org.realityforge.replicant:replicant-server:jar:6.43)

desc 'GraphQL Domgen Support'
define 'graphql-domgen-support' do
  project.group = 'org.realityforge.keycloak.domgen'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/graphql-domgen-support')
  pom.add_developer('realityforge', 'Peter Donald', 'peter@realityforge.org', ['Developer'])

  compile.with :javaee_api,
               :jsr305,
               JACKSON_DEPS,
               GRAPHQL_TOOLS_DEPS,
               REPLICANT_DEPS

  package(:jar)
  package(:sources)
  package(:javadoc)
end
