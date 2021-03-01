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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.thinkit.zenna.key.ConditionNodeKey;
import org.thinkit.zenna.util.ContentNodeResolver;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that represents a group of nodes that handle content conditions.
 *
 * <p>
 * This class evaluates the condition specified when the
 * {@link ContentCondition} class is instantiated and the condition defined in
 * the content file, and holds the {@code "conditionId"} that satisfies the
 * condition. Specify an empty collection even if the specified condition is
 * empty, i.e. when the condition does not exist. {@code null} is not allowed.
 *
 * <p>
 * To determine whether an item is selectable that satisfies a specific
 * condition, execute the {@link #isSatisfied(String)} method with the value
 * specified in {@code "conditionId"} of the selected item as an argument.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ContentCondition implements Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 8564840008830796135L;

    /**
     * The list of satisfied condition ids
     */
    private List<String> conditionIds;

    /**
     * The constructor.
     *
     * <p>
     * When this constructor process is executed, it evaluates the condition
     * specified as an argument and the condition defined in the content file, and
     * extracts the value of {@code "conditionId"} that satisfies the condition.
     *
     * @param content    The map containing the items defined in the content file
     * @param conditions The map containing condition data to be checked against the
     *                   conditions defined in the content file
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private ContentCondition(@NonNull Map<String, Object> content, @NonNull Map<String, String> conditions) {
        this.conditionIds = this.getConditionIds(content, conditions);
    }

    /**
     * Returns the new instance of {@link ContentCondition} based on the arguments.
     *
     * @param content    The map containing the items defined in the content file
     * @param conditions The map containing condition data to be checked against the
     *                   conditions defined in the content file
     * @return The new instance of {@link ContentCondition}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected static ContentCondition from(@NonNull Map<String, Object> content,
            @NonNull Map<String, String> conditions) {
        return new ContentCondition(content, conditions);
    }

    /**
     * Check if the condition associated with the value of {@code "conditionId"} set
     * in the selected item is satisfied.
     *
     * @param conditionId The condition id to be checked
     * @return {@code true} if the condition associated with the value of
     *         {@code "conditionId"} is met, otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected boolean isSatisfied(@NonNull final String conditionId) {
        return conditionIds.contains(conditionId);
    }

    /**
     * Checks the specified conditions against the conditions defined in the content
     * file and returns a list of condition IDs that satisfy the conditions. If the
     * specified condition is empty, or if the condition is not defined in the
     * content file, an empty list is returned.
     *
     * @param content    The map containing the items defined in the content file
     * @param conditions The map containing condition data to be checked against the
     *                   conditions defined in the content file
     * @return The list containing the condition IDs that satisfy the conditions
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private List<String> getConditionIds(@NonNull Map<String, Object> content,
            @NonNull Map<String, String> conditions) {

        final List<Map<String, Object>> conditionNodes = this.getConditionNodes(content);

        if (conditions.isEmpty() || conditionNodes.isEmpty()) {
            this.conditionIds = new ArrayList<>(0);
        }

        final List<String> conditionIds = new ArrayList<>();

        for (final Map<String, Object> conditionNode : conditionNodes) {
            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(conditionNode, ConditionNodeKey.NODE);
            final List<Map<String, Object>> conditionsNode = ContentNodeResolver.getNodeList(nodeMap,
                    ConditionNodeKey.CONDITIONS);

            if (this.isConditionSatisfied(conditionsNode, conditions)) {
                conditionIds.add(ContentNodeResolver.getString(nodeMap, ConditionNodeKey.CONDITION_ID));
            }
        }

        return conditionIds;
    }

    /**
     * Returns the condition node part from the content.
     *
     * @param content The map containing the items defined in the content file
     * @return The map of condition nodes
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private List<Map<String, Object>> getConditionNodes(@NonNull Map<String, Object> content) {
        return ContentNodeResolver.getNodeList(content, ConditionNodeKey.CONDITION_NODES);
    }

    /**
     * Checks the specified conditions against those defined in the content file. If
     * all the conditions are met, {@code true} will be returned; if any condition
     * is not met, {@code false} will be returned.
     *
     * @param conditionNodes The conditions defined in the content file
     * @param conditions     The map containing condition data to be checked against
     *                       the conditions defined in the content file
     * @return {@code true} if all the conditions are met, otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private boolean isConditionSatisfied(@NonNull List<Map<String, Object>> conditionNodes,
            @NonNull Map<String, String> conditions) {

        final Set<Entry<String, String>> entrySet = conditions.entrySet();

        for (final Map<String, Object> conditionNode : conditionNodes) {
            final String keyName = ContentNodeResolver.getString(conditionNode, ConditionNodeKey.KEY_NAME);
            final String value = ContentNodeResolver.getString(conditionNode, ConditionNodeKey.OPERAND);

            for (final Entry<String, String> entry : entrySet) {
                if (Objects.equals(keyName, entry.getKey()) && !Objects.equals(value, entry.getValue())) {
                    return false;
                }
            }
        }

        return true;
    }
}
