#!/bin/sh

rm -rf ~/wildfly/modules/system/layers/base/dk/
mvn clean install

unzip target/logging-handler-1.0-SNAPSHOT-wildfly-module.zip -d ~/wildfly/modules/system/layers/base/
