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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.zenna.key.ConditionNodeKey;
import org.thinkit.zenna.key.SelectionNodeKey;
import org.thinkit.zenna.util.ContentNodeResolver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that defines the process of evaluating the defined content based on
 * the given inputs.
 *
 * <p>
 * The builder pattern is used as the process when creating an instance of this
 * class. When creating an instance of this class, first call the
 * {@link #builder()} method. The {@link #builder()} method returns an instance
 * of itself, subsequent processing can be described in the form of a method
 * chain. And you can create a new instance of the {@link ContentEvaluator}
 * class by setting the required and optional fields after calling the
 * {@link #builder()} method, and then calling the
 * {@link ContentEvaluatorBuilder#build()} method.
 *
 * <p>
 * The required inputs for creating an instance of this class and performing
 * subsequent processing successfully are an object in the {@link Map} structure
 * that stores content information and an object in the {@link Set} structure
 * that stores the key names defined under the {@code "selectionNodes"} of the
 * content. The condition to get the content item can be specified by an object
 * of {@link Map} structure, but specifying this condition is optional and not
 * required. If no content acquisition condition is specified, all items under
 * {@code "selectionNodes"} in the content file whose {@code "conditionId"} is
 * empty will be acquired.
 *
 * <p>
 * To set the {@link Map} object representing the content, call the
 * {@link ContentEvaluatorBuilder#content(Map)} method, and to set the
 * {@link Set} object representing the key name of the item to be retrieved,
 * call the {@link ContentEvaluatorBuilder#attributes(Set)} method. To set a
 * {@link Map} object that represents the conditions of an arbitrary item, call
 * {@link ContentEvaluatorBuilder#conditions(Map)} to set it. After setting the
 * data required for the evaluation process, call the
 * {@link ContentEvaluatorBuilder#build()} method and execute the
 * {@link #evaluate()} method. The content data evaluated based on the specified
 * input will be returned as an object of {@link List} structure.
 *
 * <p>
 * The required field must be an object that is not {@code null} and is not
 * empty in the case of a collection. The initial value of the condition object,
 * which is an optional item, is an empty {@link Map} object, and {@code null}
 * is not allowed. If the above values are set at the time of instance creation,
 * no exception will be thrown when executing the
 * {@link ContentEvaluatorBuilder#build()} method, but an exception will always
 * be thrown when checking the preconditions when executing the
 * {@link #evaluate()} method.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public final class ContentEvaluator implements Evaluator {

    /**
     * The content map
     */
    private Map<String, Object> content;

    /**
     * The attributes of content
     */
    private Set<String> attributes;

    /**
     * The conditions of content
     */
    @Builder.Default
    private Map<String, String> conditions = new HashMap<>(0);

    @Override
    public List<Map<String, String>> evaluate() {
        Preconditions.requireNonEmpty(this.content);
        Preconditions.requireNonEmpty(this.attributes);
        Preconditions.requireNonNull(this.conditions);

        final List<Map<String, Object>> conditionNodes = ContentNodeResolver.getNodeList(this.content,
                ConditionNodeKey.CONDITION_NODES);

        final List<String> conditionIdList = conditionNodes.isEmpty() ? new ArrayList<>(0)
                : this.getConditionIdList(conditionNodes, conditions);

        return this.getContentList(this.content, conditionIdList);
    }

    /**
     * Returns the content list based on the information passed as arguments.
     *
     * <p>
     * A record with a condition ID in the content definition will be fetched only
     * if it matches the condition defined in the content. If there is no condition
     * ID in the content definition, the record will be fetched unconditionally.
     *
     * @param attributes      The list of keys associated to the values to be
     *                        fetched from the content
     * @param rawContent      The unprocessed content objects
     * @param conditionIdList A list containing the condition ID to be fetched
     * @return The content list
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private List<Map<String, String>> getContentList(@NonNull Map<String, Object> rawContent,
            @NonNull List<String> conditionIdList) {

        final List<Map<String, String>> contentList = new ArrayList<>(0);
        final List<Map<String, Object>> selectionNodes = ContentNodeResolver.getNodeList(rawContent,
                SelectionNodeKey.SELECTION_NODES);

        for (Map<String, Object> nodeList : selectionNodes) {
            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(nodeList, SelectionNodeKey.NODE);
            final String conditionId = ContentNodeResolver.getString(nodeMap, SelectionNodeKey.CONDITION_ID);

            if (!StringUtils.isEmpty(conditionId) && !conditionIdList.contains(conditionId)) {
                continue;
            }

            final Map<String, String> content = new HashMap<>(0);

            for (String attribute : attributes) {
                content.put(attribute, ContentNodeResolver.getString(nodeMap, attribute));
            }

            contentList.add(content);
        }

        return contentList;
    }

    /**
     * Gets the condition ID used to load the content and returns it as a list.
     *
     * @param conditionNodes The list containing the conditional nodes
     * @param conditions     The conditional map to use when matching conditions
     * @return The List of condition IDs obtained as a result of matching
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private List<String> getConditionIdList(@NonNull List<Map<String, Object>> conditionNodes,
            @NonNull Map<String, String> conditions) {

        final List<String> conditionIdList = new ArrayList<>(0);

        conditionNodes.forEach(nodeList -> {

            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(nodeList, ConditionNodeKey.NODE);
            final List<Map<String, Object>> conditionList = ContentNodeResolver.getNodeList(nodeMap,
                    ConditionNodeKey.CONDITIONS);

            if (this.all(conditionList, conditions)) {
                conditionIdList.add(ContentNodeResolver.getString(nodeMap, ConditionNodeKey.CONDITION_ID));
            }
        });

        return conditionIdList;

    }

    /**
     * Tests the conditions defined in the content against those passed to
     * {@link #load(InputStream, Set, List)} and determine if all conditions are
     * met.
     *
     * @param contentConditionList The List of conditions defined in the content
     * @param conditions           The conditional map to use when matching
     *                             conditions
     * @return If all conditions are met {@code true} , otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private boolean all(@NonNull List<Map<String, Object>> contentConditionList,
            @NonNull Map<String, String> conditions) {

        final Set<Entry<String, String>> entrySet = conditions.entrySet();

        for (final Map<String, Object> contentCondition : contentConditionList) {
            final String keyName = ContentNodeResolver.getString(contentCondition, ConditionNodeKey.KEY_NAME);
            final String value = ContentNodeResolver.getString(contentCondition, ConditionNodeKey.OPERAND);

            for (Entry<String, String> entry : entrySet) {
                if (Objects.equals(keyName, entry.getKey()) && !Objects.equals(value, entry.getValue())) {
                    return false;
                }
            }
        }

        return true;
    }
}
