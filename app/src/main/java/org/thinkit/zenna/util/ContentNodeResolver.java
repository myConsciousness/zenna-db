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

package org.thinkit.zenna.util;

import java.util.List;
import java.util.Map;

import org.thinkit.zenna.key.ContentKey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Provides operations on content node.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentNodeResolver {

    /**
     * Returns the list of nodes associated with the specified {@link ContentKey}
     * from the content map.
     * <p>
     * Because it's impossible to avoid warnings when casting with generics, we
     * specify {@link SuppressWarnings} with {@code "unchecked"} to this
     * {@link #getNodeList(Map, ContentKey)} method.
     *
     * @param content    The content map
     * @param contentKey The content key
     * @return A list of nodes associated with {@link ContentKey}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getNodeList(@NonNull Map<String, Object> content,
            @NonNull ContentKey contentKey) {
        return (List<Map<String, Object>>) content.get(contentKey.getName());
    }

    /**
     * Returns the node map associated with the specified {@link ContentKey} from
     * the content map.
     * <p>
     * Because it's impossible to avoid warnings when casting with generics, we
     * specify {@link SuppressWarnings} with {@code "unchecked"} to this
     * {@link #getNodeMap(Map, ContentKey)} method.
     *
     * @param content    The content map
     * @param contentKey The content key
     * @return A list of nodes associated with {@link ContentKey}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNodeMap(@NonNull Map<String, Object> content, @NonNull ContentKey contentKey) {
        return (Map<String, Object>) content.get(contentKey.getName());
    }

    /**
     * Returns a value of type string based on the content key specified as an
     * argument from the node map.
     *
     * @param nodeMap    The node map
     * @param contentKey The content key
     * @return A String value tied to a content key stored in the node map
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static String getString(@NonNull Map<String, Object> nodeMap, @NonNull ContentKey contentKey) {
        return getString(nodeMap, contentKey.getName());
    }

    /**
     * Returns a value of type string based on the content key specified as an
     * argument from the node map.
     *
     * @param nodeMap    The node map
     * @param contentKey The content key
     * @return A String value tied to a content key stored in the node map
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static String getString(@NonNull Map<String, Object> nodeMap, @NonNull String contentKey) {
        return (String) nodeMap.get(contentKey);
    }

    /**
     * Returns a value of type Object based on the content key specified as an
     * argument from the node map.
     *
     * @param nodeMap    The node map
     * @param contentKey The content key
     * @return A Object value tied to a content key stored in the node map
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static Object getObject(@NonNull Map<String, Object> nodeMap, @NonNull String contentKey) {
        return nodeMap.get(contentKey);
    }
}
