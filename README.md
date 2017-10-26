# alluxio-akka-http

Simple service to allow you to deposit a local file to Alluxio and to get it
back. This leverages the native java client provided by [Alluxio Java Client](http://www.alluxio.org/docs/1.6/en/Clients-Java-Native.html)

## Read Options

Alluxio provides the following options : `CACHE`, `NO_CACHE` and `CACHE_PROMOTE` which allows the developer to leverage the 3-tier storage levels supported in Alluxio

Read [Alluxio Java Client](http://www.alluxio.org/docs/1.6/en/Clients-Java-Native.html) for more information.

## Write Options

Alluxio provides the following options : `CACHE_THROUGH`, `MUST_CACHE`,
`THROUGH` and `ASYCN_THROUGH` which allows the developer to deposit the file at
different levels of the tiered-storage.

Read [Alluxio Java Client](http://www.alluxio.org/docs/1.6/en/Clients-Java-Native.html) for more information.

# Example of writing and read to/from Alluxio

Over here is an example of reading a file from local storage after writing to
it. Below is an example of the `curl` `POST` and `GET` requests.

## Write to Alluxio

Example of writing a local file into Alluxio 

```
curl -X POST -v 'http://localhost:8080/alluxio/store?\
sPath=/home/ubuntu/Songs/data/B/B/B/TRBBBKS12903CDFCF4.h5&\
dPath=/songdata/file1.h5&\
writetype=CACHE_THROUGH'
```

## Example read from Alluxio

```
curl -v 'http://localhost:8080/alluxio/load?sPath=/songdata/file1.h5&readtype=CACHE_PROMOTE'
```

