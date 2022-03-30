package demo.options;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.StreamingOptions;

public interface DemoOptions extends StreamingOptions {

    @Description("ID of the GCP project the input subscription is in.")
    String getInputProjectId();

    void setInputProjectId(String value);

    @Description("Pub/Sub subscription to read from.")
    String getInputSubscription();

    void setInputSubscription(String value);

    @Description("Project id of the BigQuery table.")
    String getBigQueryProjectId();

    void setBigQueryProjectId(String value);

    @Description("BigQuery dataset id.")
    String getBigQueryDataset();

    void setBigQueryDataset(String value);

    @Description("BigQuery table.")
    String getBigQueryTable();

    void setBigQueryTable(String value);

}
