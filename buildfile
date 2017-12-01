require 'buildr_plus'
require 'buildr/git_auto_version'
require 'buildr/gpg'

GRAPHQL_TOOLS_DEPS = %w(
  com.graphql-java:graphql-java-servlet:jar:4.0.0
  commons-fileupload:commons-fileupload:jar:1.3.3
  commons-io:commons-io:jar:2.5

  com.esotericsoftware:reflectasm:jar:1.11.3
  com.fasterxml.jackson.core:jackson-annotations:jar:2.8.8
  com.fasterxml.jackson.core:jackson-core:jar:2.8.8
  com.fasterxml.jackson.core:jackson-databind:jar:2.8.8
  com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.8.8
  com.fasterxml.jackson.module:jackson-module-kotlin:jar:2.8.8
  com.google.guava:guava:jar:21.0
  com.graphql-java:graphql-java-tools:jar:3.2.1
  com.graphql-java:graphql-java:jar:3.0.0
  org.antlr:antlr4-runtime:jar:4.5.1
  org.jetbrains.kotlin:kotlin-reflect:jar:1.1.1
  org.jetbrains.kotlin:kotlin-stdlib:jar:1.1.1
  org.jetbrains.kotlin:kotlin-stdlib:jar:1.1.3-2
  org.jetbrains:annotations:jar:15.0
  org.ow2.asm:asm:jar:5.0.4
  org.slf4j:slf4j-api:jar:1.6.6
  org.slf4j:slf4j-jdk14:jar:1.6.6
  ru.vyarus:generics-resolver:jar:2.0.1
)

REPLICANT_DEPS = %w(
  com.fasterxml.jackson.core:jackson-annotations:jar:2.8.8
  com.fasterxml.jackson.core:jackson-core:jar:2.8.8
  com.fasterxml.jackson.core:jackson-databind:jar:2.8.8
  com.google.gwt:gwt-servlet:jar:2.8.2
  org.realityforge.gwt.datatypes:gwt-datatypes:jar:0.9
  org.realityforge.replicant:replicant-server:jar:0.06
  org.realityforge.replicant:replicant-shared-ee:jar:0.06
  org.realityforge.replicant:replicant-shared:jar:0.06
)

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
               GRAPHQL_TOOLS_DEPS,
               REPLICANT_DEPS

  package(:jar)
  package(:sources)
  package(:javadoc)
end
