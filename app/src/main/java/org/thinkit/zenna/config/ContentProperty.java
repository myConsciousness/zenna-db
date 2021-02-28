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

package org.thinkit.zenna.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.thinkit.zenna.catalog.ContentPropertyKey;
import org.thinkit.zenna.catalog.ContentRoot;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * A class that represents a content property file.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentProperty {

    /**
     * The name of property file
     */
    private static final String PROPERTY_FILE_NAME = ContentRoot.DEFAULT.getTag() + "/content.properties";

    /**
     * The properties
     */
    private Properties properties;

    /**
     * The constructor
     *
     * @param contentObject The content object
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private ContentProperty(@NonNull final Class<?> contentObject) {
        try (final InputStream stream = contentObject.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            if (stream != null) {
                final Properties properties = new Properties();
                properties.load(stream);

                this.properties = properties;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the new instance of {@link ContentProperty} based on an argument.
     *
     * @param contentObject The content object
     * @return The new isntance of {@link ContentProperty}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static ContentProperty from(@NonNull final Class<?> contentObject) {
        return new ContentProperty(contentObject);
    }

    /**
     * Test for the existence of content property files.
     *
     * @return {@code true} if the content property file exists, otherwise
     *         {@code false}
     */
    public boolean isExist() {
        return this.properties != null;
    }

    public String getContentPackage() {
        return ContentPackage.from(this.getProperty(ContentPropertyKey.CONTENT_PACKAGE)).getProperty();
    }

    public String getMapperSuffix() {
        return MapperSuffix.from(this.getProperty(ContentPropertyKey.MAPPER_SUFFIX)).getProperty();
    }

    /**
     * Searches for the property with the specified key in this property list. If
     * the key is not found in this property list, the default property list, and
     * its defaults, recursively, are then checked. The method returns empty string
     * {@code ""} if the property is not found.
     *
     * @param key The property key
     * @return The value in this property list with the specified key value
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private String getProperty(@NonNull final ContentPropertyKey key) {

        final String propertyValue = this.properties.getProperty(key.getTag());

        if (propertyValue == null) {
            return "";
        }

        return propertyValue;
    }
}
