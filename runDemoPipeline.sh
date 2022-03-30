#!/bin/bash

mvn clean compile exec:java \
-Dexec.mainClass=demo.Main \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args=" \
--jobName=demo-dataflow-project \
--inputProjectId=sandbox-apalfi \
--inputSubscription=projects/sandbox-apalfi/subscriptions/demo-topic-sub \
--bigQueryProjectId=sandbox-apalfi \
--bigQueryDataset=demo_dataset \
--bigQueryTable=demo_table \
--gcpTempLocation=gs://sandbox_apalfi_bucket/dataflow/temp/demo-dataflow-project \
--runner=DataflowRunner" \
-Pdataflow-runner \
-Djava.util.logging.config.file=logging.properties
