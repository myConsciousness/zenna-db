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

import java.util.List;

import org.thinkit.common.base.precondition.Preconditions;
import org.thinkit.zenna.annotation.Content;
import org.thinkit.zenna.entity.ContentEntity;

import lombok.EqualsAndHashCode;
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
public abstract class ContentMapper<T extends ContentMapper<T, R>, R extends ContentEntity> implements Mapper<R> {

    /**
     * The content object associated with a specific content file
     */
    private ContentMapper<T, R> contentObject;

    protected List<R> loadContent() {

        final Class<?> contentClass = contentObject.getClass();
        final Content mapping = contentClass.getAnnotation(Content.class);

        Preconditions.requireNonNull(mapping);

        return List.of();
    }
}
