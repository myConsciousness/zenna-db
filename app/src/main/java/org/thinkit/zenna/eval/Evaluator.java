/*
 * Copyright 2021 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.zenna.eval;

import java.util.List;
import java.util.Map;

/**
 * The interface that abstracts the evaluator.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface Evaluator {

    /**
     * Evaluate and return the obtained object as a list structure.
     *
     * @return The object obtained by the evaluation process
     */
    public List<Map<String, String>> evaluate();
}
