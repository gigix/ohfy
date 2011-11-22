#!/bin/sh
ant -f application/build.xml clean debug && \
ant -f application-test/build.xml test.run
