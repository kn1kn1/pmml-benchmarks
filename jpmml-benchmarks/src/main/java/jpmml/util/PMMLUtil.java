/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package jpmml.util;

import jakarta.xml.bind.JAXBException;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public final class PMMLUtil {

    public static Evaluator compileModel(InputStream inputStream) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        // load the PMML model
        return new LoadingModelEvaluatorBuilder()
                .load(inputStream)
                .build();
    }

    public static Map<String, ?> evaluate(Evaluator evaluator, Map<String, Object> inputData) {
        return evaluator.evaluate(inputData);
    }

    private PMMLUtil() {
        // It is not allowed to instantiate util classes.
    }
}
