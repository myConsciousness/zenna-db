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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.common.base.precondition.exception.PreconditionFailedException;
import org.thinkit.zenna.exception.IllegalContentStateException;
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

    /**
     * {@inheritDoc}
     *
     * <p>
     * Evaluates and processes content based on the data specified when the
     * {@link ContentEvaluator} class is instantiated. In the content evaluation
     * process, the content data to be returned is filtered based on the input data
     * passed when the instance of this class is created. The filtered content data
     * will be returned as an object of {@link List} structure. {@code null} will
     * never be returned.
     *
     * <p>
     * If the content map specified when instantiating the {@link ContentEvaluator}
     * class is {@code null} or an empty map, or if the attribute set is
     * {@code null} or an empty set, or if the condition map is {@code null}, an
     * exception will always be raised at runtime and processing will fail.
     *
     * @return The filtered content list
     *
     * @exception PreconditionFailedException If {@code content} is {@code null} or
     *                                        empty, or if {@code attributes} is
     *                                        {@code null} or empty
     * @exception NullPointerException        If {@code conditions} is {@code null}
     */
    @Override
    public List<Map<String, String>> evaluate() {
        this.checkPreconditions();
        return this.filterContent(this.content, this.attributes, this.getConditionIds(this.conditions));
    }

    private void checkPreconditions() {
        Preconditions.requireNonEmpty(this.content,
                String.format("The content map must not be null or empty. The content map = %s", this.content));
        Preconditions.requireNonEmpty(this.attributes,
                String.format("The attribute set must not be null or empty. The attribute set = %s", this.attributes));
        Preconditions.requireNonNull(this.conditions, "The condition map must not be null.");
        Preconditions.requireNonEmpty(this.getSelectionNodes(), new IllegalContentStateException(String.format(
                "Failed to detect the selected node from the content file. At least one set of selections must be defined. The content = %s",
                this.content)));
    }

    private List<Map<String, Object>> getSelectionNodes() {
        return ContentNodeResolver.getNodeList(this.content, SelectionNodeKey.SELECTION_NODES);
    }

    private List<Map<String, Object>> getConditionNodes() {
        return ContentNodeResolver.getNodeList(this.content, ConditionNodeKey.CONDITION_NODES);
    }

    private List<String> getConditionIds(@NonNull Map<String, String> conditions) {

        final List<Map<String, Object>> conditionNodes = this.getConditionNodes();

        if (conditionNodes.isEmpty()) {
            return new ArrayList<>(0);
        }

        final List<String> conditionIds = new ArrayList<>();

        conditionNodes.forEach(nodeList -> {
            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(nodeList, ConditionNodeKey.NODE);
            final List<Map<String, Object>> conditionsNode = ContentNodeResolver.getNodeList(nodeMap,
                    ConditionNodeKey.CONDITIONS);

            if (this.all(conditionsNode, conditions)) {
                conditionIds.add(ContentNodeResolver.getString(nodeMap, ConditionNodeKey.CONDITION_ID));
            }
        });

        return conditionIds;
    }

    private List<Map<String, String>> filterContent(@NonNull Map<String, Object> rawContent,
            @NonNull Set<String> attributes, @NonNull List<String> conditionIds) {

        final List<Map<String, String>> filtredContent = new ArrayList<>();
        final int attributeCount = attributes.size();

        this.getSelectionNodes().forEach(selectionNode -> {
            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(selectionNode, SelectionNodeKey.NODE);
            final String conditionId = ContentNodeResolver.getString(nodeMap, SelectionNodeKey.CONDITION_ID);

            if (StringUtils.isEmpty(conditionId) || conditionIds.contains(conditionId)) {
                final Map<String, String> content = new HashMap<>(attributeCount);

                attributes.forEach(attribute -> {
                    content.put(attribute, ContentNodeResolver.getString(nodeMap, attribute));
                });

                filtredContent.add(content);
            }
        });

        return filtredContent;
    }

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
