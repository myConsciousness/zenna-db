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
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.api.catalog.BiCatalog;
import org.thinkit.api.catalog.Catalog;
import org.thinkit.zenna.annotation.Condition;
import org.thinkit.zenna.annotation.Content;
import org.thinkit.zenna.catalog.ContentExtension;
import org.thinkit.zenna.catalog.ContentPropertyKey;
import org.thinkit.zenna.catalog.ContentRoot;
import org.thinkit.zenna.catalog.MapperSuffix;
import org.thinkit.zenna.config.ContentProperty;
import org.thinkit.zenna.entity.ContentEntity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that represents an object mapped to a content.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ContentObject<T extends ContentEntity> implements Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = -4141481108885397138L;

    /**
     * The format of content path
     */
    private static final String FORMAT_CONTENT_PATH = "%s%s.%s";

    /**
     * The content object associated with a specific content file
     */
    private Class<?> contentObject;

    /**
     * The constructor
     *
     * @param contentMapper The content mapper object
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private ContentObject(@NonNull final Mapper<T> contentMapper) {
        this.contentObject = contentMapper.getClass();
    }

    /**
     * Returns the new instance of {@link ContentObject} based on the content mapper
     * object passed as an argument.
     *
     * @param <T>    The type of result type mapped to the content mapper
     * @param mapper The content mapper
     * @return The new instance of {@link ContentObject}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static <T extends ContentEntity> ContentObject<T> from(@NonNull final Mapper<T> contentMapper) {
        return new ContentObject<>(contentMapper);
    }

    /**
     * Returns the simple name of the underlying class as given in the source code.
     * Returns an empty string if the underlying class is anonymous.
     * <p>
     * The simple name of an array is the simple name of the component type with
     * "[]" appended. In particular the simple name of an array whose component type
     * is anonymous is "[]".
     *
     * @return The simple name of the underlying class
     */
    public String getSimpleName() {
        return this.contentObject.getSimpleName();
    }

    /**
     * Returns {@link Content} annotation object from the content object. If the
     * annotation is not set, {@code null} will be returned.
     *
     * @return {@link Content} annotation object if the annotation is set, otherwise
     *         {@code null}
     */
    public Content getContentAnnotation() {
        return this.contentObject.getAnnotation(Content.class);
    }

    /**
     * Returns an input stream for reading the specified resource.
     * <p>
     * The search order is described in the documentation for
     * {@link #getResource(String)} .
     * <p>
     * Resources in named modules are subject to the encapsulation rules specified
     * by {@link Module#getResourceAsStream Module.getResourceAsStream}.
     * Additionally, and except for the special case where the resource has a name
     * ending with "{@code .class}", this method will only find resources in
     * packages of named modules when the package is {@link Module#isOpen(String)
     * opened} unconditionally.
     * </p>
     *
     * @param name The resource name
     *
     * @return An input stream for reading the resource; {@code null} if the
     *         resource could not be found, the resource is in a package that is not
     *         opened unconditionally, or access to the resource is denied by the
     *         security manager.
     *
     * @exception NullPointerException If {@code name} is {@code null}
     */
    public InputStream getResourceAsStream(@NonNull final String name) {
        return this.contentObject.getClassLoader().getResourceAsStream(
                String.format(FORMAT_CONTENT_PATH, ContentRoot.DEFAULT.getTag(), name, ContentExtension.JSON.getTag()));
    }

    /**
     * Returns a list of {@code Field} objects reflecting all the fields declared by
     * the class or interface represented by this class object. This includes
     * public, protected, default (package) access, and private fields, but excludes
     * inherited fields.
     * <p>
     * If this class object represents a class or interface with no declared fields,
     * then this method returns an empty list.
     * <p>
     * If this class object represents an array type, a primitive type, or void,
     * then this method returns an empty list.
     * <p>
     * The elements in the returned array are not sorted and are not in any
     * particular order.
     *
     * @return The list of {@code Field} objects representing all the declared
     *         fields of this class
     */
    public List<Field> getDeclaredFields() {
        return Arrays.asList(this.contentObject.getDeclaredFields());
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
    public String getContentName() {

        final Content contentAnnotation = this.getContentAnnotation();

        if (contentAnnotation != null) {
            return this.getFullContentName(contentAnnotation.value());
        }

        final String className = this.getSimpleName();

        if (className.endsWith(MapperSuffix.MAPPER.getTag())) {
            return this.getFullContentName(className.substring(0, className.indexOf(MapperSuffix.MAPPER.getTag())));
        }

        return this.getFullContentName(className);
    }

    public Map<String, String> getConditions() {

        final Map<String, String> conditions = new HashMap<>(0);

        this.getDeclaredFields().forEach(field -> {
            field.setAccessible(true);

            try {
                conditions.put(this.getConditionKey(field), this.getConditionValue(field));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e) {
                throw new IllegalStateException();
            }
        });

        return conditions;
    }

    private String getFullContentName(@NonNull String contentName) {

        final ContentProperty contentProperty = ContentProperty.from(this.contentObject);
        final String contentPackageName = contentProperty.getProperty(ContentPropertyKey.CONTENT_PACKAGE);

        if (StringUtils.isEmpty(contentPackageName)) {
            return contentName;
        }

        return contentPackageName + contentName;
    }

    private String getConditionKey(@NonNull final Field field) {

        final Condition conditionAnnotation = field.getAnnotation(Condition.class);

        if (conditionAnnotation != null) {
            return conditionAnnotation.value();
        }

        return field.getName();
    }

    private String getConditionValue(@NonNull final Field field)
            throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException {

        final Class<?> fieldType = field.getType();

        if (fieldType.equals(Catalog.class)) {
            final Catalog<?> catalog = (Catalog<?>) field.get(this.contentObject);
            return String.valueOf(catalog.getCode());
        } else if (fieldType.equals(BiCatalog.class)) {
            final BiCatalog<?, ?> biCatalog = (BiCatalog<?, ?>) field.get(this.contentObject);
            return String.valueOf(biCatalog.getCode());
        }

        return String.valueOf(field.get(this.contentObject));
    }
}
