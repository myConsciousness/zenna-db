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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.zenna.exception.IllegalContentStateException;
import org.thinkit.zenna.key.SelectionNodeKey;
import org.thinkit.zenna.util.ContentNodeResolver;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentSelection implements Iterator<ContentSelection>, Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 3005351001132119055L;

    /**
     * The iterator of content selection nodes
     */
    private Iterator<Map<String, Object>> iterator;

    /**
     * The set of content attribute
     */
    private Set<String> attributes;

    /**
     * The count of attributes
     */
    private int attributeCount;

    /**
     * The selection node map
     */
    private Map<String, Object> selectionNodeMap;

    /**
     * The condition id of selection node
     */
    private String conditionId;

    private ContentSelection(@NonNull Map<String, Object> content, @NonNull Set<String> attributes) {

        final List<Map<String, Object>> selectionNodes = this.getSelectionNodes(content);
        Preconditions.requireNonEmpty(selectionNodes, new IllegalContentStateException(
                "Failed to detect the selection node from the content file. At least one set of selections must be defined."));

        this.attributes = attributes;
        this.attributeCount = attributes.size();
        this.iterator = selectionNodes.iterator();
    }

    protected static ContentSelection from(@NonNull Map<String, Object> content, @NonNull Set<String> attributes) {
        return new ContentSelection(content, attributes);
    }

    @Override
    public boolean hasNext() {

        if (!this.iterator.hasNext()) {
            return false;
        }

        final Map<String, Object> nextSelectionNode = this.iterator.next();
        this.selectionNodeMap = ContentNodeResolver.getNodeMap(nextSelectionNode, SelectionNodeKey.NODE);
        this.conditionId = ContentNodeResolver.getString(selectionNodeMap, SelectionNodeKey.CONDITION_ID);

        return true;
    }

    @Override
    @Deprecated
    public ContentSelection next() {
        return this;
    }

    protected boolean isSelectable(@NonNull ContentCondition contentCondition) {
        return StringUtils.isEmpty(this.conditionId) || contentCondition.isSatisfied(conditionId);
    }

    protected Map<String, String> getSelection() {

        final Map<String, String> selection = new HashMap<>(this.attributeCount);

        this.attributes.forEach(attribute -> {
            selection.put(attribute, ContentNodeResolver.getString(this.selectionNodeMap, attribute));
        });

        return selection;
    }

    private List<Map<String, Object>> getSelectionNodes(@NonNull Map<String, Object> content) {
        return ContentNodeResolver.getNodeList(content, SelectionNodeKey.SELECTION_NODES);
    }
}
