/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.drools.benchmarks.session;

import java.util.concurrent.TimeUnit;

import org.drools.benchmarks.common.AbstractBenchmark;
import org.drools.benchmarks.common.DRLProvider;
import org.drools.benchmarks.common.providers.RulesWithJoinsProvider;
import org.drools.benchmarks.common.util.BuildtimeUtil;
import org.drools.benchmarks.common.util.RuntimeUtil;
import org.drools.benchmarks.model.A;
import org.drools.benchmarks.model.B;
import org.drools.benchmarks.model.C;
import org.drools.benchmarks.model.ConsequenceBlackhole;
import org.drools.benchmarks.model.D;
import org.drools.kiesession.session.StatefulKnowledgeSessionImpl;
import org.kie.api.conf.EventProcessingOption;
import org.kie.internal.conf.MultithreadEvaluationOption;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Inserts facts and fires at each insertion causing the activation of all rules.
 */
@Warmup(iterations = 2000)
@Measurement(iterations = 1000)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class InsertFireLoopWithJoinsBenchmark extends AbstractBenchmark {

    @Param({"16"})
    private int rulesNr;

    @Param({"5", "10"})
    private int factsNr;

    @Param({"true", "false"})
    private boolean multithread;

    @Param({"true", "false"})
    private boolean async;

    @Param({"true", "false"})
    private boolean cep;

    @Param({"2", "3"})
    private int joinsNr;

    @Param({"true", "false"})
    private boolean batchFire;

    @Param({"true", "false"})
    private boolean useCanonicalModel;

    @Param({"true", "false"})
    private boolean withConsequence;

    @Setup
    public void setupKieBase() {
        final DRLProvider drlProvider = new RulesWithJoinsProvider().withNumberOfJoins( joinsNr ).withGeneratedConsequence( withConsequence );
        kieBase = BuildtimeUtil.createKieBaseFromDrl(useCanonicalModel, drlProvider.getDrl(rulesNr),
                                                     multithread ? MultithreadEvaluationOption.YES : MultithreadEvaluationOption.NO,
                                                     cep ? EventProcessingOption.STREAM : EventProcessingOption.CLOUD );
    }

    @Setup(Level.Iteration)
    @Override
    public void setup() {
        kieSession = RuntimeUtil.createKieSession(kieBase);
    }

    @Benchmark
    public void test(final Blackhole eater) {
        ConsequenceBlackhole.eater = eater;

        StatefulKnowledgeSessionImpl session = (StatefulKnowledgeSessionImpl) kieSession;
        if (async) {
            session.insertAsync( new A( rulesNr + 1 ) );
        } else {
            eater.consume(session.insert( new A( rulesNr + 1 ) ));
        }
        for ( int i = 0; i < factsNr; i++ ) {
            if (async) {
                session.insertAsync( new B( rulesNr + i + 3 ) );
                if (joinsNr > 1) {
                    session.insertAsync( new C( rulesNr + factsNr + i + 3 ) );
                }
                if (joinsNr > 2) {
                    session.insertAsync( new D( rulesNr + factsNr * 2 + i + 3 ) );
                }
            } else {
                eater.consume(session.insert( new B( rulesNr + i + 3 ) ));
                if (joinsNr > 1) {
                    eater.consume(session.insert( new C( rulesNr + factsNr + i + 3 ) ));
                }
                if (joinsNr > 2) {
                    eater.consume(session.insert( new D( rulesNr + factsNr * 2 + i + 3 ) ));
                }
            }

            if (!batchFire) {
                eater.consume(kieSession.fireAllRules());
            }
        }

        if (batchFire) {
            eater.consume(kieSession.fireAllRules());
        }
    }
}
