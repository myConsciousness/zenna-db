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
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ContentMapper<R extends ContentEntity> implements Mapper<R> {

    /**
     * The format of content path
     */
    private static final String FORMAT_CONTENT_PATH = "%s%s.%s";

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

        final Map<String, Object> rawContent = this.getRawContent();
        final ResultType<R> resultType = this.getResultType(rawContent);

        if (!resultType.isExist()) {
            throw new IllegalStateException();
        }

        return resultType.createResultEntities(
                this.evaluateContent(rawContent, resultType.getAttributes(), this.contentObject.getConditions()));
    }

    private Map<String, Object> getRawContent() {

        if (this.cachedContent != null) {
            return this.cachedContent;
        }

        final InputStream contentStream = this.contentObject.getClassLoader()
                .getResourceAsStream(String.format(FORMAT_CONTENT_PATH, ContentRoot.VALUE.getTag(),
                        this.contentObject.getContentName(), ContentExtension.JSON.getTag()));

        if (contentStream == null) {
            throw new ContentNotFoundException();
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
