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

package jpmml.runtime.regression;

import com.google.common.io.Resources;
import jakarta.xml.bind.JAXBException;
import jpmml.AbstractBenchmark;
import jpmml.util.PMMLUtil;
import org.jpmml.evaluator.Evaluator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@State(Scope.Benchmark)
@Warmup(iterations = 300)
@Measurement(iterations = 50)
public class PMMLEvaluateLinearRegressionSampleWithTransformationsBenchmark extends AbstractBenchmark {

    public static final String MODEL_NAME = "LinearRegressionSampleWithTransformations";
    public static final String FILE_NAME_NO_SUFFIX = "LinearRegressionSampleWithTransformations";
    public static final String FILE_NAME = FILE_NAME_NO_SUFFIX + ".pmml";
    public static final String FILE_PATH = "pmml/" + FILE_NAME;

    private static final Map<String, Object> INPUT_DATA;
    private static final Evaluator evaluator;

    static {
        // Retrieve pmmlFile
        URL pmmlFileUrl = Resources.getResource(FILE_PATH);
        // Instantiate Evaluator
        try {
            evaluator = PMMLUtil.compileModel(pmmlFileUrl.openStream());
        } catch (IOException | JAXBException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        // Set input data
        INPUT_DATA = new HashMap<>();
        INPUT_DATA.put("age", 27.0);
        INPUT_DATA.put("salary", 34000.0);
        INPUT_DATA.put("car_location", "street");
    }

    @Setup
    public void setupResource() {
    }

    @Benchmark
    public Map<String, ?> evaluatePrediction() {
        return PMMLUtil.evaluate(evaluator, INPUT_DATA);
    }
}
