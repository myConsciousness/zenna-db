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

package org.thinkit.zenna.loader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.zenna.util.InputStreamResolver;
import org.thinkit.zenna.util.JsonConverter;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * The class that defines the process of loading the content data based on the
 * specified content definition.
 * <p>
 * It provides a {@link #load(InputStream, Set)} method to load content data
 * without conditions, and a {@link #load(InputStream, Set, List)} method to
 * load content data with conditions.
 * <p>
 * If the value of {@code "conditionId"} defined in the content is an empty
 * string, the record will be loaded unconditionally. If you have defined a
 * value for the conditionId of the content, be sure to define a condition for
 * the content and call {@link #load(InputStream, Set, List)}.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "from")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public final class ContentLoader implements Loader {

    /**
     * The input stream of content
     */
    private final InputStream contentStream;

    /**
     * The cache of content
     */
    private Map<String, Object> contentCache;

    /**
     * Gets each element defined in the content file specified and return it as a
     * list.
     * <p>
     * Use this {@link ContentLoader#load(InputStream, Set, List)} method if there
     * are no acquisition conditions in the content definition.
     *
     * <pre>
     * Get the content data with conditions:
     * <code>List&lt;Map&lt;String, String&gt;&gt; contents = ContentLoader.load(contentStream, attributes, conditions);</code>
     * </pre>
     *
     * @param contentStream The stream of content file
     * @param attributes    The Attribute names to be acquired
     * @param conditions    The conditional list to use when getting data from the
     *                      content file
     * @return The List containing the elements retrieved from the content file
     *
     * @exception NullPointerException     If {@code null} is passed as an argument
     * @exception IllegalArgumentException If the attribute list is empty
     */
    public Map<String, Object> load() {
        Preconditions.requireNonNull(this.contentStream);

        if (this.contentCache == null) {
            this.contentCache = this.getContent(this.contentStream);
        }

        return this.contentCache;
    }

    /**
     * Returns content data from the input stream of content file.
     *
     * @param contentStream The stream of content file
     * @return The content data from the input stream of content
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private Map<String, Object> getContent(@NonNull final InputStream contentStream) {
        return JsonConverter.toLinkedHashMap(InputStreamResolver.toString(contentStream));
    }
}
