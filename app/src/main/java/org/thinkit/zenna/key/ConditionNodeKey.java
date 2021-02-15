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

package org.thinkit.zenna.key;

import lombok.RequiredArgsConstructor;

/**
 * The enum that manages condition node key.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum ConditionNodeKey implements Key {

    /**
     * {@code "conditionNodes"}
     */
    CONDITION_NODES(KeyName.conditionNodes),

    /**
     * {@code "node"}
     */
    NODE(KeyName.node),

    /**
     * {@code "conditionId"}
     */
    CONDITION_ID(KeyName.conditionId),

    /**
     * {@code "conditions"}
     */
    CONDITIONS(KeyName.conditions),

    /**
     * {@code "keyName"}
     */
    KEY_NAME(KeyName.keyName),

    /**
     * {@code "operator"}
     */
    OPERATOR(KeyName.operator),

    /**
     * {@code "operand"}
     */
    OPERAND(KeyName.operand);

    /**
     * The key name
     */
    private final KeyName keyName;

    /**
     * The inner enum that manages key name
     */
    private enum KeyName {
        conditionNodes, node, conditionId, conditions, keyName, operator, operand;
    }

    @Override
    public String getKeyName() {
        return this.keyName.name();
    }
}
