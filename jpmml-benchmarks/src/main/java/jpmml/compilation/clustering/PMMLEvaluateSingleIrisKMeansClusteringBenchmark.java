/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package jpmml.compilation.clustering;

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

@State(Scope.Benchmark)
@Warmup(iterations = 300)
@Measurement(iterations = 50)
public class PMMLEvaluateSingleIrisKMeansClusteringBenchmark extends AbstractBenchmark {

    public static final String FILE_NAME_NO_SUFFIX = "SingleIrisKMeansClustering";

    public static final String FILE_NAME = FILE_NAME_NO_SUFFIX + ".pmml";
    public static final String FILE_PATH = "pmml/" + FILE_NAME;

    private static final URL pmmlFileUrl;

    static {
        // Retrieve pmmlFile
        pmmlFileUrl = Resources.getResource(FILE_PATH);
    }

    @Setup
    public void setupResource() {
        // noop
    }

    @Benchmark
    public Evaluator evaluateCompilation() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        return PMMLUtil.compileModel(pmmlFileUrl.openStream());
    }
}
