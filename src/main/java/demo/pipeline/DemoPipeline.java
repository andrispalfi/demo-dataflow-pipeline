package demo.pipeline;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.gson.Gson;
import demo.model.UserEvent;
import demo.model.UserEventGroupKey;
import demo.options.DemoOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.WithTimestamps;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoPipeline {

  private static final Logger LOG = LoggerFactory.getLogger(DemoPipeline.class);

  public static Pipeline createPipeline(DemoOptions options) {
    LOG.info("Reading messages from PubSub subscription {}", options.getInputSubscription());

    final Pipeline pipeline = Pipeline.create(options);
    pipeline
        .apply("Read messages from PubSub", source(options))
        .apply("Parse PubSub message to UserEvent object", ParDo.of(parseMessage()))
        .apply(
            "Group the messages into fixed-sized minute intervals",
            Window.into(FixedWindows.of(Duration.standardMinutes(1))))
        .apply("Log input messages", ParDo.of(logData()))

        // Pipeline without Combine
        .apply("Convert UserEvent objects to BigQuery TableRow", convertUserEventBigQueryTableRow())
        .apply(
            "Load data into a BigQuery table",
            loadDataIntoBigQuery(options, schemaWithEventDate()));

        // Pipeline with Combine
    //            .apply(
    //                "Group the UserEvents into fixed-sized 5 minutes intervals",
    //                Window.into(FixedWindows.of(Duration.standardMinutes(5))))
    //            .apply("Create group keys for UserEvents", userEventGroupKeys())
    //            .apply(
    //                "Aggregate user events of 5 mins window into user row",
    //                Combine.perKey(new AggregateTimeSpentValuesFn()))
    //            .apply("Pass BigQuery TableRow through", mapBigQueryTableRow())
    //            .apply("Load data into a BigQuery table", loadDataIntoBigQuery(options,
    // schema()));

    return pipeline;
  }

  private static PTransform<PBegin, PCollection<String>> source(DemoOptions options) {
    String inputSubscription = options.getInputSubscription();
    return PubsubIO.readStrings().fromSubscription(inputSubscription);
  }

  private static SerializableFunction<UserEvent, Instant> eventTimeFn() {
    return event -> {
      return Instant.parse((String) event.getEventDate());
    };
  }

  private static DoFn<UserEvent, UserEvent> logData() {
    return new DoFn<UserEvent, UserEvent>() {
      @ProcessElement
      public void processElement(ProcessContext context, BoundedWindow window) {
        UserEvent event = context.element();
        LOG.info(
            "Window max: {} EventTime: {} UserId: {} UserName: {}",
            window.maxTimestamp(),
            event.getEventDate(),
            event.getUserId(),
            event.getUserName());
        context.output(event);
      }
    };
  }

  private static DoFn<String, UserEvent> parseMessage() {
    return new DoFn<String, UserEvent>() {
      @ProcessElement
      public void processElement(ProcessContext context) {
        Gson gson = new Gson();
        UserEvent event = gson.fromJson(context.element(), UserEvent.class);
        context.output(event);
      }
    };
  }

  private static WithKeys<UserEventGroupKey, UserEvent> userEventGroupKeys() {
    return WithKeys.of(
        new SerializableFunction<UserEvent, UserEventGroupKey>() {
          @Override
          public UserEventGroupKey apply(UserEvent input) {
            return new UserEventGroupKey(input);
          }
        });
  }

  public static MapElements<KV<UserEventGroupKey, TableRow>, TableRow> mapBigQueryTableRow() {
    return MapElements.into(TypeDescriptor.of(TableRow.class))
        .via(
            (KV<UserEventGroupKey, TableRow> ev) -> {
              return ev.getValue();
            });
  }

  public static MapElements<UserEvent, TableRow> convertUserEventBigQueryTableRow() {
    return MapElements.into(TypeDescriptor.of(TableRow.class))
        .via(
            (UserEvent ev) -> {
              return new TableRow()
                  .set("event_date", ev.getEventDate())
                  .set("user_id", ev.getUserId())
                  .set("user_name", ev.getUserName())
                  .set("user_age", ev.getUserAge())
                  .set("user_gender", ev.getUserGender())
                  .set("feed_category", ev.getFeedCategory())
                  .set("time_spent_in_sec", ev.getTimeSpentInSec());
            });
  }

  private static BigQueryIO.Write<TableRow> loadDataIntoBigQuery(
      DemoOptions options, TableSchema schema) {
    TableReference outputTable =
        new TableReference()
            .setProjectId(options.getBigQueryProjectId())
            .setDatasetId(options.getBigQueryDataset())
            .setTableId(options.getBigQueryTable());

    return BigQueryIO.writeTableRows()
        .to(outputTable)
        .withSchema(schema)
        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND);
  }

  private static TableSchema schema() {
    return new TableSchema()
        .setFields(
            Arrays.asList(
                new TableFieldSchema().setName("user_id").setType("STRING").setMode("REQUIRED"),
                new TableFieldSchema().setName("user_name").setType("STRING").setMode("NULLABLE"),
                new TableFieldSchema().setName("user_age").setType("INT64").setMode("NULLABLE"),
                new TableFieldSchema().setName("user_gender").setType("STRING").setMode("NULLABLE"),
                new TableFieldSchema().setName("feed_category").setType("STRING"),
                new TableFieldSchema().setName("time_spent_in_sec").setType("INT64")));
  }

  private static TableSchema schemaWithEventDate() {
    List<TableFieldSchema> schemaFields = new ArrayList<>();
    TableSchema schema = schema();
    schemaFields.add(
        new TableFieldSchema().setName("event_date").setType("TIMESTAMP").setMode("REQUIRED"));
    schemaFields.addAll(schema.getFields());
    schema.setFields(schemaFields);
    return schema;
  }
}
