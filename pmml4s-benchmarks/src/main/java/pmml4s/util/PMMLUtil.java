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

package pmml4s.util;

import org.pmml4s.common.Predictable;
import org.pmml4s.common.StructField;
import org.pmml4s.data.Series;
import org.pmml4s.util.Utils;

import java.util.Map;

public final class PMMLUtil {

    public static Series evaluate(Predictable model, StructField[] structTypes, Map<String, Object> inputData) {
        int inputSchemaSize = structTypes.length;
        Object[] values = new Object[inputSchemaSize];
        for (int i = 0; i < inputSchemaSize; i++) {
            StructField sf = structTypes[i];
            values[i] = Utils.toVal(inputData.get(sf.name()), sf.dataType());
        }
        return model.predict(Series.fromArray(values));
    }


    private PMMLUtil() {
        // It is not allowed to instantiate util classes.
    }
}
