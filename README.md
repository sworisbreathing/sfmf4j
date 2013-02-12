SFMF4J
====================

SFMF4J is a simple file monitoring facade for Java.  It attempts to do for
file monitoring what SLF4J does for logging, which is to abstract away the
API from the interface, such that *library* developers can write code
against a file monitoring solution without forcing an *application*
developer to choose a particular file monitoring implementation.

Developing Libraries
-----------------

To add file monitoring support to your library, simply declare a dependency on
sfmf4j-api.  In Maven:

    <dependency>
        <groupId>sfmf4j</groupId>
        <artifactId>sfmf4j-api</artifactId>
        <version>${sfmf4j.version}</version>
    </dependency>

Then you need to write a class that implements `DirectoryListener`, in order to
take a particular action when a folder's contents have changed.  For
convenience, you may extend `DirectoryListenerAdapter` and override its no-op
methods instead.


Developing Applications
-----------------------

In addition to sfmf4j-api, an application will also require a dependency on one
of the SFMF4J implementations in order to support file monitoring.  SFMF4J offers
several out-of-the-box implementations:

* Commons IO, which uses a dedicated file system polling thread on a fixed interval.
* NIO2, the new file system API introduced in Java 7 (requires Java 7 or later).
* jpathwatch, essentially a backport of NIO2 to earlier versions of Java
(uses native operating system features with a fallback to simple polling).


SPI
---

All of SFMF4J's provided implementations support the `java.util.ServiceLoader`
mechanism.  This means that applications can obtain `FileMonitorServiceFactory`
instances without dodgy classpath hacks.  The following code snippet
demonstrates how to obtain a reference to a `FileMonitorServiceFactory` using
the SPI approach:

    public static FileMonitorServiceFactory loadFromSpi() {
        FileMonitorServiceFactory factory = null;
        ServiceLoader<FileMonitorServiceFactory> serviceLoader = ServiceLoader.load(FileMonitorServiceFactory.class);
        Iterator<FileMonitorServiceFactory> implementations = serviceLoader.iterator();
        if (iterator.hasNext()) {
            factory = iterator.next();
        }
        return factory;
    }

OSGi
----

SFMF4J is designed to be OSGi-friendly.  The sfmf4j-api module exports the
package `sfmf4j.api`, which is imported by the implementation modules (your own
bundle must also import this package).  The provided implementations expose
default instances of the `sfmf4j.api.FileMonitorServiceFactory` interface using
Blueprint.
