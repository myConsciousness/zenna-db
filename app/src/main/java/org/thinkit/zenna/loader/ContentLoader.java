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
import java.util.LinkedHashMap;
import java.util.Map;

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
 * The class that defines the content loading process.
 *
 * <p>
 * In order to create an instance of this {@link ContentLoader} class, use the
 * {@link #from(InputStream)} method and pass the input stream object of the
 * content resource as an initialization argument. Even if the input stream
 * object of the content for initialization is {@code null} , no exception will
 * be thrown when executing the constructor process, but a
 * {@link NullPointerException} will be thrown when executing the {@link #load}
 * method. For this reason, be sure to pass a content input stream object that
 * is not {@link null} in order for the content loading process to be
 * successful.
 *
 * <p>
 * After creating an instance of the {@link ContentLoader} class based on the
 * above points, simply call the {@link #load} method. This method parses the
 * content input stream object passed when creating an instance of the
 * {@link ContentLoader} class, converts the content string defined in JSON
 * format into the structure of {@link LinkedHashMap} , and returns it.
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
     * {@inheritDoc}
     *
     * <p>
     * Performs the content loading process based on the content input stream
     * specified when creating an instance of this class. The loaded content will be
     * formatted and returned in the format {@link LinkedHashMap} .
     *
     * @return The loaded content
     *
     * @exception NullPointerException If the input stream content is {@code null}
     */
    @Override
    public Map<String, Object> load() {
        Preconditions.requireNonNull(this.contentStream, "The content stream must not be null.");
        return this.getContent(this.contentStream);
    }

    /**
     * Parses the content input stream passed as an argument, and returns the
     * obtained JSON format content string in {@link LinkedHashMap} structure.
     *
     * @param contentStream The input stream of content
     * @return The content in {@link LinkedHashMap} structure
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private Map<String, Object> getContent(@NonNull final InputStream contentStream) {
        return JsonConverter.toLinkedHashMap(InputStreamResolver.toString(contentStream));
    }
}
