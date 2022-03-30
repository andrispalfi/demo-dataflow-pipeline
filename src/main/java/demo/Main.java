package demo;

import demo.options.DemoOptions;
import demo.pipeline.DemoPipeline;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class Main {
  public static void main(String[] args) {
    PipelineOptionsFactory.register(DemoOptions.class);
    final DemoOptions options = PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(DemoOptions.class);

    Pipeline demoPipeline = DemoPipeline.createPipeline(options);
    demoPipeline.run();
  }
}
