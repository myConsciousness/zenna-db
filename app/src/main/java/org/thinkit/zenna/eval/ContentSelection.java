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
 * The class that represents a group of nodes that handle content selections.
 *
 * <p>
 * This class is designed to be an iterable object that implements the
 * {@link Iterator} interface. The {@link #hasNext()} method is provided to
 * define the necessary processing for iterating over the selected items in the
 * content file, and the {@link #next()} method does not need to be used. The
 * {@link #next()} method is implemented to only return an instance of itself,
 * and is deprecated.
 *
 * <p>
 * By executing the {@code hasNext()} method, the value of {@code "conditionId"}
 * of the currently focused selection will be cached. To check whether the
 * currently focused selection item meets the condition and can be selected,
 * execute {@link #isSelectable(ContentCondition)} with the object of
 * {@link ContentCondition} representing the content condition as an argument.
 * To get the currently focused selection group, execute the
 * {@link #getSelection()} method.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ContentSelection implements Iterator<ContentSelection>, Serializable {

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

    /**
     * The constructor.
     *
     * @param content    The map containing the items defined in the content file
     * @param attributes The set containing the attribute names defined in the
     *                   selected nodes of the content file
     *
     * @exception NullPointerException         If {@code null} is passed as an
     *                                         argument
     * @exception IllegalContentStateException If the selection is not defined in
     *                                         the content file
     */
    private ContentSelection(@NonNull Map<String, Object> content, @NonNull Set<String> attributes) {

        final List<Map<String, Object>> selectionNodes = this.getSelectionNodes(content);
        Preconditions.requireNonEmpty(selectionNodes, new IllegalContentStateException(
                "Failed to detect the selection node from the content file. At least one set of selections must be defined."));

        this.attributes = attributes;
        this.attributeCount = attributes.size();
        this.iterator = selectionNodes.iterator();
    }

    /**
     * Returns the new instance of {@link ContentSelection} based on the arguments.
     *
     * @param content    The map containing the items defined in the content file
     * @param attributes The set containing the attribute names defined in the
     *                   selected nodes of the content file
     * @return The new instance of {@link ContentSelection}
     *
     * @exception NullPointerException         If {@code null} is passed as an
     *                                         argument
     * @exception IllegalContentStateException If the selection is not defined in
     *                                         the content file
     */
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

    /**
     * This method just returns an instance of itself. It is deprecated and should
     * not be used.
     *
     * @deprecated
     */
    @Override
    @Deprecated
    public ContentSelection next() {
        return this;
    }

    /**
     * Check if the currently focused item group satisfies the condition and can be
     * selected.
     *
     * @param contentCondition The object that represents a content condition
     * @return {@code true} if the currently focused selection group is selectable,
     *         otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected boolean isSelectable(@NonNull ContentCondition contentCondition) {
        return StringUtils.isEmpty(this.conditionId) || contentCondition.isSatisfied(conditionId);
    }

    /**
     * Returns the currently focused set of selected items in the Map structure.
     *
     * @return The currently focused set of selected items
     */
    protected Map<String, String> getSelection() {

        final Map<String, String> selection = new HashMap<>(this.attributeCount);

        this.attributes.forEach(attribute -> {
            selection.put(attribute, ContentNodeResolver.getString(this.selectionNodeMap, attribute));
        });

        return selection;
    }

    /**
     * Returns the selection node part from the content.
     *
     * @param content The map containing the items defined in the content file
     * @return The map of selection nodes
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private List<Map<String, Object>> getSelectionNodes(@NonNull Map<String, Object> content) {
        return ContentNodeResolver.getNodeList(content, SelectionNodeKey.SELECTION_NODES);
    }
}
