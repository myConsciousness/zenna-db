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
import java.util.Set;

import org.thinkit.zenna.annotation.Attribute;
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
     * The serial version UID
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
     * Returns a set of attribute names for the content.
     *
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
                if (field.isAnnotationPresent(Attribute.class)) {
                    attributes.add(field.getAnnotation(Attribute.class).value());
                } else {
                    attributes.add(field.getName());
                }
            }
        });

        this.cachedAttirbutes = attributes;

        return attributes;
    }

    /**
     * Returns the list of content entity object dynamically generated from the data
     * defined in the content file.
     *
     * @param contents The content object
     * @return The list of content entity object
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public List<T> createResultEntities(@NonNull final List<Map<String, String>> contents) {

        final List<T> resultEntities = new ArrayList<>();

        try {
            for (final Map<String, String> content : contents) {
                final T resultEntity = this.getResultEntity();
                final List<Field> fields = Arrays.asList(resultEntity.getClass().getDeclaredFields());

                for (final Field field : fields) {
                    field.setAccessible(true);

                    if (!Modifier.isStatic(field.getModifiers())) {
                        if (field.isAnnotationPresent(Attribute.class)) {
                            field.set(resultEntity, content.get(field.getAnnotation(Attribute.class).value()));
                        } else {
                            field.set(resultEntity, content.get(field.getName()));
                        }
                    }
                }

                resultEntities.add(resultEntity);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        return resultEntities;
    }

    /**
     * Generates and returns a new instance of the class object generated from the
     * package name defined in {@code "resultType"} of the content file.
     *
     * @return The new instance of the class object generated from the package name
     *         defined in {@code "resultType"} of the content file
     *
     * @throws InstantiationException    If the class that declares the underlying
     *                                   constructor represents an abstract class
     * @throws IllegalAccessException    If {@code Constructor} object is enforcing
     *                                   Java language access control and the
     *                                   underlying constructor is inaccessible
     * @throws IllegalArgumentException  If the number of actual and formal
     *                                   parameters differ; if an unwrapping
     *                                   conversion for primitive arguments fails;
     *                                   or if, after possible unwrapping, a
     *                                   parameter value cannot be converted to the
     *                                   corresponding formal parameter type by a
     *                                   method invocation conversion; if this
     *                                   constructor pertains to an enum type
     * @throws InvocationTargetException If the underlying constructor throws an
     *                                   exception
     * @throws NoSuchMethodException     If a matching method is not found
     * @throws SecurityException         If a security manager is present and the
     *                                   caller's class loader is not the same as or
     *                                   an ancestor of the class loader for the
     *                                   current class and invocation of
     *                                   {@link SecurityManager#checkPackageAccess
     *                                   s.checkPackageAccess()} denies access to
     *                                   the package of this class
     */
    @SuppressWarnings("unchecked")
    private T getResultEntity() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final Constructor<?> constructor = this.resultType.getConstructor();
        return (T) constructor.newInstance();
    }
}
