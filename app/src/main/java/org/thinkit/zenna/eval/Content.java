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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.zenna.exception.IllegalContentStateException;
import org.thinkit.zenna.key.ConditionNodeKey;
import org.thinkit.zenna.key.SelectionNodeKey;
import org.thinkit.zenna.util.ContentNodeResolver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
final class Content implements Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 1426571977335812321L;

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
    private Map<String, String> conditions;

    protected List<Map<String, String>> filter() {

        final ContentCondition contentCondition = ContentCondition.from(conditions, this.getConditionNodes());

        final List<Map<String, Object>> selectionNodes = this.getSelectionNodes();
        Preconditions.requireNonEmpty(selectionNodes, new IllegalContentStateException(String.format(
                "Failed to detect the selection node from the content file. At least one set of selections must be defined. The content = %s",
                this.content)));

        final List<Map<String, String>> filtredContent = new ArrayList<>();
        final int attributeCount = attributes.size();

        selectionNodes.forEach(selectionNode -> {
            final Map<String, Object> nodeMap = ContentNodeResolver.getNodeMap(selectionNode, SelectionNodeKey.NODE);
            final String conditionId = ContentNodeResolver.getString(nodeMap, SelectionNodeKey.CONDITION_ID);

            if (contentCondition.isSelectable(conditionId)) {
                final Map<String, String> content = new HashMap<>(attributeCount);

                attributes.forEach(attribute -> {
                    content.put(attribute, ContentNodeResolver.getString(nodeMap, attribute));
                });

                filtredContent.add(content);
            }
        });

        return filtredContent;
    }

    private List<Map<String, Object>> getSelectionNodes() {
        return ContentNodeResolver.getNodeList(this.content, SelectionNodeKey.SELECTION_NODES);
    }

    private List<Map<String, Object>> getConditionNodes() {
        return ContentNodeResolver.getNodeList(this.content, ConditionNodeKey.CONDITION_NODES);
    }
}
