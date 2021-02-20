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
import java.util.Set;

import org.thinkit.zenna.catalog.ContentExtension;
import org.thinkit.zenna.catalog.ContentRoot;
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
 * <p>
 * The {@link #scan} method defined in this abstract class handles all the
 * linking between the content object inheriting from this abstract class and
 * the content file.
 *
 * <p>
 * This {@link #scan} method cannot be overridden by content objects that
 * inherit from this abstract class. In other words, after creating an instance
 * of a content object that inherits from this abstract class, simply calling
 * the {@link #scan} method defined in the inherited abstract class will perform
 * the mapping process, resulting in the acquisition result as the result type
 * defined in the content file The result will be returned as the result type
 * defined in the content file.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ContentMapper<R extends ContentEntity> implements Mapper<R> {

    /**
     * The format of content path
     */
    private static final String FORMAT_CONTENT_PATH = "%s%s.%s";

    /**
     * The cache of content
     */
    private Map<String, Object> cachedContent;

    @Override
    public final List<R> scan() {

        final ContentObject<R> contentObject = ContentObject.from(this);

        final Map<String, Object> rawContent = this.getRawContent(contentObject);
        final ResultType<R> resultType = this.getResultType(rawContent);

        if (!resultType.isExist()) {
            throw new IllegalStateException();
        }

        return resultType.createResultEntities(
                this.evaluateContent(rawContent, resultType.getAttributes(), contentObject.getConditions()));
    }

    private Map<String, Object> getRawContent(@NonNull final ContentObject<R> contentObject) {

        if (this.cachedContent != null) {
            return this.cachedContent;
        }

        final String contentName = contentObject.getContentName();

        final InputStream contentStream = contentObject.getClassLoader().getResourceAsStream(String.format(
                FORMAT_CONTENT_PATH, ContentRoot.DEFAULT.getTag(), contentName, ContentExtension.JSON.getTag()));

        if (contentStream == null) {
            throw new ContentNotFoundException(String.format("The content name '%s' was not found.", contentName));
        }

        return ContentLoader.from(contentStream).load();
    }

    private ResultType<R> getResultType(@NonNull final Map<String, Object> rawContent) {
        final Map<String, Object> metaMap = ContentNodeResolver.getNodeMap(rawContent, MetaNodeKey.META);
        return ResultType.from(ContentNodeResolver.getString(metaMap, MetaNodeKey.RESULT_TYPE));
    }

    private List<Map<String, String>> evaluateContent(@NonNull final Map<String, Object> rawContent,
            @NonNull final Set<String> attributes, @NonNull final Map<String, String> conditions) {
        return ContentEvaluator.builder().content(rawContent).attributes(attributes).conditions(conditions).build()
                .evaluate();
    }
}
