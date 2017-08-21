require 'buildr_plus'
require 'buildr/git_auto_version'
require 'buildr/gpg'

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

  compile.with BuildrPlus::Libs.ee_provided, BuildrPlus::Libs.graphql_java_servlet, BuildrPlus::Libs.replicant_server

  package(:jar)
  package(:sources)
  package(:javadoc)
end
