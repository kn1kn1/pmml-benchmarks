#!/bin/bash

echo "*********"
echo "Start Benchmarks"
echo "*********"
echo "----------"
echo "KIE"
cd drools-benchmarks
mvn clean package
java -jar target/drools-benchmarks.jar -jvmArgs "-Xms4g -Xmx4g" -foe true -rf csv -rff results_kie.csv "org.drools.benchmarks.pmml.*"
echo "----------"
echo "JPMML"
cd ../jpmml-benchmarks
mvn clean package
java -jar target/jpmml-benchmarks.jar -jvmArgs "-Xms4g -Xmx4g" -foe true -rf csv -rff results_jpmml.csv "jpmml.*"
echo "----------"
echo "PMML4S"
cd ../pmml4s-benchmarks
mvn clean package
java -jar target/pmml4s-benchmarks.jar -jvmArgs "-Xms4g -Xmx4g" -foe true -rf csv -rff results_pmml4s.csv "pmml4s.*"
echo "----------"
echo "*********"
echo "All done! Bye"
echo "*********"