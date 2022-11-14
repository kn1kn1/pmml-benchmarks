/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pmml4s.runtime.clustering;

import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.pmml4s.common.StructField;
import org.pmml4s.common.StructType;
import org.pmml4s.data.Series;
import org.pmml4s.model.Model;
import pmml4s.AbstractBenchmark;
import pmml4s.util.PMMLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@State(Scope.Benchmark)
@Warmup(iterations = 300)
@Measurement(iterations = 50)
public class PMMLEvaluateSingleIrisKMeansClusteringBenchmark extends AbstractBenchmark {

    public static final String MODEL_NAME = "SingleIrisKMeansClustering";
    public static final String FILE_NAME_NO_SUFFIX = "SingleIrisKMeansClustering";

    public static final String FILE_NAME = FILE_NAME_NO_SUFFIX + ".pmml";
    public static final String FILE_PATH = "pmml/" + FILE_NAME;

    private static final Map<String, Object> INPUT_DATA;
    private static final Model MODEL;
    private static final StructField[] STRUCT_TYPES;

    static {
        // Retrieve pmmlFile
        URL pmmlFileUrl = Resources.getResource(FILE_PATH);
        try {
            MODEL = Model.fromInputStream(pmmlFileUrl.openStream());
            StructType inputSchema = MODEL.inputSchema();
            int inputSchemaSize = inputSchema.size();
            STRUCT_TYPES = new StructField[inputSchemaSize];
            for (int i = 0; i < inputSchemaSize; i++) {
                STRUCT_TYPES[i] = inputSchema.apply(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set input data
        INPUT_DATA = new HashMap<>();
        INPUT_DATA.put("sepal_length", 4.4);
        INPUT_DATA.put("sepal_width", 3.0);
        INPUT_DATA.put("petal_length", 1.3);
        INPUT_DATA.put("petal_width", 0.2);
    }

    @Setup
    public void setupResource() {
    }

    @Benchmark
    public Series evaluatePrediction() {
        return PMMLUtil.evaluate(MODEL, STRUCT_TYPES, INPUT_DATA);
    }
}
