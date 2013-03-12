# webhdfs-java-client

Hadoop [WebHDFS][1] REST API's java client code with kerberos auth.

# Packaging

The tests need hadoop setup so they could not be made generic enough
to package:
    ````
    buildr TEST=no
    ````
## Basic Usage

    ````
	WebHDFSConnectionFactory connFactory = new WebHDFSConnectionFactory();
	WebHDFSConnection connection = connFactory.getConnection();
    ````

[1]: http://hortonworks.com/blog/webhdfs-%E2%80%93-http-rest-access-to-hdfs/
