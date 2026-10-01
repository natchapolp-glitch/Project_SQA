```java
/*
 * Copyright 2008 The Closure Compiler Authors.
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

package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

/**
 * Deterministic unit and regression tests for {@link RemoveUnusedVars}.
 */
public class RemoveUnusedVarsRegressionTest extends TestCase {

  private String compile(
      String js,
      boolean removeGlobals,
      boolean preserveFunctionExpressionNames,
      boolean modifyCallSites) {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(js);

    RemoveUnusedVars pass = new Remove
```