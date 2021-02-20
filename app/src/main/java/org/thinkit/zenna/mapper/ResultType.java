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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.thinkit.zenna.entity.ContentEntity;
import org.thinkit.zenna.exception.ResultTypeNotFoundException;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that represents the type of the result of content retrieval.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ResultType<T extends ContentEntity> implements Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -3664581429594247412L;

    /**
     * The result type
     */
    private Class<?> resultType;

    /**
     * The cache of attibutes
     */
    private Set<String> cachedAttirbutes;

    /**
     * The constructor
     *
     * @param className The fully qualified name of the desired class
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private ResultType(@NonNull String className) {
        try {
            this.resultType = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ResultTypeNotFoundException(e);
        }
    }

    /**
     * Returns the new instance of {@code ResultType} based on {@code className}
     * passed as an argument.
     *
     * @param className The fully qualified name of the desired class
     * @return The new instance of {@code ResultType}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static <T extends ContentEntity> ResultType<T> from(@NonNull String className) {
        return new ResultType<>(className);
    }

    /**
     * Test for the existence of a result type.
     *
     * @return {@code true} if the result type exists, otherwise {@code false}
     */
    public boolean isExist() {
        return this.resultType != null;
    }

    /**
     * Returns a set of attribute names for the content.
     * <p>
     * On the second and subsequent method calls, the cached attribute set will be
     * returned.
     *
     * @return The set of attribute names
     */
    public Set<String> getAttributes() {

        if (this.cachedAttirbutes != null) {
            return this.cachedAttirbutes;
        }

        final Set<String> attributes = new HashSet<>();

        Arrays.asList(this.resultType.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            if (!Modifier.isStatic(field.getModifiers())) {
                attributes.add(field.getName());
            }
        });

        this.cachedAttirbutes = attributes;

        return attributes;
    }

    public List<T> createResultEntities(@NonNull final List<Map<String, String>> contents) {

        final List<T> resultEntities = new ArrayList<>();

        try {
            for (final Map<String, String> content : contents) {
                final T resultEntity = this.getResultEntity();

                for (final Entry<String, String> entry : content.entrySet()) {
                    final Field field = resultEntity.getClass().getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(resultEntity, entry.getValue());
                }

                resultEntities.add(resultEntity);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException | NoSuchFieldException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        return resultEntities;
    }

    @SuppressWarnings("unchecked")
    private T getResultEntity() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final Constructor<?> constructor = this.resultType.getConstructor();
        return (T) constructor.newInstance();
    }
}
