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

package org.thinkit.zenna.mapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.thinkit.zenna.annotation.Content;
import org.thinkit.zenna.catalog.ContentExtension;
import org.thinkit.zenna.catalog.MapperSuffix;
import org.thinkit.zenna.entity.ContentEntity;
import org.thinkit.zenna.eval.ContentEvaluator;
import org.thinkit.zenna.exception.ContentNotFoundException;
import org.thinkit.zenna.key.MetaNodeKey;
import org.thinkit.zenna.loader.ContentLoader;
import org.thinkit.zenna.util.ContentNodeResolver;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * A class that abstracts the process of mapping content classes to content
 * files. The content class associated with a specific content file should
 * inherit from this abstract class.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ContentMapper<T extends ContentMapper<T, R>, R extends ContentEntity> implements Mapper<R> {

    /**
     * The content object associated with a specific content file
     */
    private ContentObject<R> contentObject;

    /**
     * The cache of content
     */
    private Map<String, Object> cachedContent;

    /**
     * The constructor.
     *
     * @param contentMapper The mapper object for content file
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected ContentMapper(@NonNull final Mapper<R> contentMapper) {
        this.contentObject = ContentObject.from(contentMapper);
    }

    protected List<R> loadContent() {

        final Map<String, Object> rawContent = this.getContent();
        final Map<String, Object> metaMap = ContentNodeResolver.getNodeMap(rawContent, MetaNodeKey.META);

        final ResultType resultType = ResultType.from(ContentNodeResolver.getString(metaMap, MetaNodeKey.RESULT_TYPE));

        if (!resultType.isExist()) {
            throw new IllegalStateException();
        }

        final List<Map<String, String>> contents = ContentEvaluator.builder().content(rawContent)
                .attributes(resultType.getAttributes()).conditions(this.contentObject.getConditions()).build()
                .evaluate();

        return List.of();
    }

    /**
     * Returns the content file name.
     * <p>
     * If an alias name is set in the {@link Content} annotation given to the
     * content class, the specified alias name is used, and if no alias name is
     * specified in the {@link Content} annotation, the name of the content class is
     * inferred as the content file name.
     * <p>
     * If no alias name is specified in the annotation, the class name up to
     * {@code "Mapper"} of the annotated content object will be retrieved as the
     * content file name.
     *
     * @return The content file name
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private String getContentName() {

        final Content contentAnnotation = this.contentObject.getContentAnnotation();

        if (contentAnnotation != null) {
            return contentAnnotation.value();
        }

        final String className = this.contentObject.getSimpleName();

        return className.substring(0, className.indexOf(MapperSuffix.VALUE.getTag()));
    }

    private Map<String, Object> getContent() {

        if (this.cachedContent != null) {
            return this.cachedContent;
        }

        final InputStream contentStream = this.contentObject.getClassLoader()
                .getResourceAsStream(String.format("%s.%s", this.getContentName(), ContentExtension.JSON.getTag()));

        if (contentStream == null) {
            throw new ContentNotFoundException();
        }

        return ContentLoader.from(contentStream).load();
    }
}
