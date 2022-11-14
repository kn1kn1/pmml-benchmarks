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

package pmml4s.runtime.mining;

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
public class PMMLEvaluateRandomForestBenchmark extends AbstractBenchmark {

    public static final String MODEL_NAME = "RandomForest";
    public static final String FILE_NAME_NO_SUFFIX = "RandomForest";

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
        INPUT_DATA.put("Age", 40.83);
        INPUT_DATA.put("MonthlySalary", 3.5);
        INPUT_DATA.put("TotalAsset", 0.04);
        INPUT_DATA.put("TotalRequired", 10.04);
        INPUT_DATA.put("NumberInstallments", 93.2);
    }

    @Setup
    public void setupResource() {
    }

    @Benchmark
    public Series evaluatePrediction() {
        return PMMLUtil.evaluate(MODEL, STRUCT_TYPES, INPUT_DATA);
    }
}
