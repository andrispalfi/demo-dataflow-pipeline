package demo.transformations;

import com.google.api.services.bigquery.model.TableRow;
import demo.model.UserEvent;
import demo.model.UserRow;
import org.apache.beam.sdk.transforms.Combine;

public class AggregateTimeSpentValuesFn extends Combine.CombineFn<UserEvent, AggregateTimeSpentValuesAccum, TableRow> {

    @Override
    public AggregateTimeSpentValuesAccum createAccumulator() {
        return new AggregateTimeSpentValuesAccum();
    }

    @Override
    public AggregateTimeSpentValuesAccum addInput(AggregateTimeSpentValuesAccum accum, UserEvent event) {
        accum.add(event);
        return accum;
    }

    @Override
    public AggregateTimeSpentValuesAccum mergeAccumulators(Iterable<AggregateTimeSpentValuesAccum> accumulators) {
        AggregateTimeSpentValuesAccum mergedAccum = createAccumulator();
        for(AggregateTimeSpentValuesAccum accum : accumulators){
            mergedAccum.mergeAccums(accum);
        }
        return mergedAccum;
    }

    @Override
    public TableRow extractOutput(AggregateTimeSpentValuesAccum accumulator) {
        UserRow row = accumulator.getUserRow();
        return new TableRow()
                .set("user_id", row.getUserId())
                .set("user_name", row.getUserName())
                .set("user_age", row.getUserAge())
                .set("user_gender", row.getUserGender())
                .set("feed_category", row.getFeedCategory())
                .set("time_spent_in_sec", row.getTimeSpentInSec());
    }
}
