repositories.remote << 'http://repo1.maven.org/maven2'
repositories.remote << 'https://repository.cloudera.com/artifactory/cloudera-repos'
repositories.remote << 'https://repository.cloudera.com/content/repositories/third-party'
repositories.remote << 'https://repository.cloudera.com/content/repositories/releases'
repositories.remote << 'http://www.ibiblio.org/maven2/'

HADOOP = transitive('org.apache.hadoop:hadoop-client:jar:2.0.0-cdh4.1.2', :scopes => ["compile"])
GSON = transitive('com.google.code.gson:gson:jar:2.2.1', :scopes => ["compile"])
UNIT = ['junit:junit:jar:4.8.2'].flatten

desc 'Kerberos httpfs authenticator'
define 'kerberos-httpfs' do
  manifest['Copyright'] = 'Collective Media, Inc (C) 2013'
  project.group = 'com.collective'
	project.version = '0.1.0'
  
  package(:jar)
  
	compile.with(HADOOP,GSON)
	test.using(:junit) 
	package :jar
end
