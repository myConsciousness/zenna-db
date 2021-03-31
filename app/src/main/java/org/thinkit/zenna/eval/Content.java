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
import java.util.Set;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that represents the content to be evaluated.
 *
 * <p>
 * This class maps each item defined in the content file based on the attributes
 * and conditions specified when the {@link Content} class is instantiated, and
 * has a {@link #filter()} method to filter the items to be acquired.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Content implements Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 1426571977335812321L;

    /**
     * The content selection nodes
     */
    private ContentSelection contentSelection;

    /**
     * The content condition nodes
     */
    private ContentCondition contentCondition;

    /**
     * The constructor.
     *
     * @param content    The map containing the items defined in the content file
     * @param attributes The set containing the attribute names defined in the
     *                   selected nodes of the content file
     * @param conditions The map containing condition data to be checked against the
     *                   conditions defined in the content file
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private Content(@NonNull Map<String, Object> content, @NonNull Set<String> attributes,
            @NonNull Map<String, String> conditions) {
        this.contentSelection = ContentSelection.from(content, attributes);
        this.contentCondition = ContentCondition.from(content, conditions);
    }

    /**
     * Returns the new instance of {@link Content} based on the arguments.
     *
     * @param content    The map containing the items defined in the content file
     * @param attributes The set containing the attribute names defined in the
     *                   selected nodes of the content file
     * @param conditions The map containing condition data to be checked against the
     *                   conditions defined in the content file
     * @return The new instance of {@link Content}
     *
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected static Content from(@NonNull Map<String, Object> content, @NonNull Set<String> attributes,
            @NonNull Map<String, String> conditions) {
        return new Content(content, attributes, conditions);
    }

    /**
     * Filters content based on the specified conditions.
     *
     * @return The filtered content
     */
    protected List<Map<String, Object>> filter() {

        final List<Map<String, Object>> filtredContent = new ArrayList<>();

        while (this.contentSelection.hasNext()) {
            if (this.contentSelection.isSelectable(this.contentCondition)) {
                filtredContent.add(this.contentSelection.getSelection());
            }
        }

        return filtredContent;
    }
}
