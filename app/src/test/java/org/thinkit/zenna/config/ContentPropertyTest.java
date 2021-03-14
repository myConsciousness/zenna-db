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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.thinkit.test.util.ReflectionTestHelper;
import org.thinkit.zenna.catalog.ContentPropertyKey;
import org.thinkit.zenna.catalog.ContentRoot;
import org.thinkit.zenna.catalog.PropertyFileName;
import org.thinkit.zenna.mapper.ConcreteContentMapper;

/**
 * The class that manages test case of {@link ContentProperty} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class ContentPropertyTest {

    /**
     * The property file name with empty attributes
     */
    private static final String EMPTY_ATTRIBUTE_PROPERTY_NAME = "emptyAttributeProperty.properties";

    /**
     * The empty property file name
     */
    private static final String EMPTY_PROPERTY_NAME = "emptyProperty.properties";

    /**
     * The nested class for {@link ContentProperty#getContentPackage()} method.
     */
    @Nested
    class TestGetContentPackage {

        @Test
        void testWhenAttributeHasContentPackage() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    PropertyFileName.DEFAULT.getTag());
            final String contentPackage = contentProperty.getContentPackage();

            assertNotNull(contentPackage);
            assertEquals("/org/thinkit/zenna/", contentPackage);
        }

        @Test
        void testWhenAttributeHasNotContentPackage() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    EMPTY_ATTRIBUTE_PROPERTY_NAME);
            final String contentPackage = contentProperty.getContentPackage();

            assertNotNull(contentPackage);
            assertEquals("", contentPackage);
        }

        @Test
        void testWhenPropertyHasNotContentPackageAttribute() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    EMPTY_PROPERTY_NAME);
            final String contentPackage = contentProperty.getContentPackage();

            assertNotNull(contentPackage);
            assertEquals("", contentPackage);
        }
    }

    /**
     * The nested class for {@link ContentProperty#getMapperSuffix()} method.
     */
    @Nested
    class TestGetMapperSuffix {

        @Test
        void testWhenAttributeHasContentPackage() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    PropertyFileName.DEFAULT.getTag());
            final String mapperSuffix = contentProperty.getMapperSuffix();

            assertNotNull(mapperSuffix);
            assertEquals("TestSuffix", mapperSuffix);
        }

        @Test
        void testWhenAttributeHasNotMapperSuffix() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    EMPTY_ATTRIBUTE_PROPERTY_NAME);
            final String mapperSuffix = contentProperty.getMapperSuffix();

            assertNotNull(mapperSuffix);
            assertEquals("", mapperSuffix);
        }

        @Test
        void testWhenPropertyHasNotMapperSuffixAttribute() {

            final ContentProperty contentProperty = ContentProperty.from(ConcreteContentMapper.newInstance().getClass(),
                    EMPTY_PROPERTY_NAME);
            final String mapperSuffix = contentProperty.getMapperSuffix();

            assertNotNull(mapperSuffix);
            assertEquals("", mapperSuffix);
        }
    }

    /**
     * The nested class for {@link ContentProperty#getProperty(ContentPropertyKey)}
     * method.
     */
    @Nested
    class TestGetProperty {

        /**
         * The field name of property
         */
        private static final String PROPERTY_FIELD_NAME = "properties";

        /**
         * The method name
         */
        private static final String METHOD_NAME = "getProperty";

        @Test
        void testWhenPropertyIsNotExist() {

            final ReflectionTestHelper<ContentProperty, String> sut = ReflectionTestHelper.from(ContentProperty.class);
            sut.setFieldValue(PROPERTY_FIELD_NAME, this.getProperties("emptyProperty.properties"));
            sut.addArgument(ContentPropertyKey.class, ContentPropertyKey.CONTENT_PACKAGE);

            final String expected = "";
            final String actual = sut.invokeMethod(METHOD_NAME);

            assertNotNull(actual);
            assertEquals(expected, actual);
        }

        @Test
        void testWhenPropertyIsNotSet() {

            final ReflectionTestHelper<ContentProperty, String> sut = ReflectionTestHelper.from(ContentProperty.class);
            sut.setFieldValue(PROPERTY_FIELD_NAME, this.getProperties("emptyAttributeProperty.properties"));
            sut.addArgument(ContentPropertyKey.class, ContentPropertyKey.CONTENT_PACKAGE);

            final String expected = "";
            final String actual = sut.invokeMethod(METHOD_NAME);

            assertNotNull(actual);
            assertEquals(expected, actual);
        }

        @Test
        void testWhenPropertyIsSet() {

            final ReflectionTestHelper<ContentProperty, String> sut = ReflectionTestHelper.from(ContentProperty.class);
            sut.setFieldValue(PROPERTY_FIELD_NAME, this.getProperties("content.properties"));
            sut.addArgument(ContentPropertyKey.class, ContentPropertyKey.CONTENT_PACKAGE);

            final String expected = "org/thinkit/zenna/";
            final String actual = sut.invokeMethod(METHOD_NAME);

            assertNotNull(actual);
            assertEquals(expected, actual);
        }

        /**
         * Return the property object.
         *
         * @param propertyFileName The file name of property
         * @return The property object
         */
        private Properties getProperties(final String propertyFileName) {
            try (final InputStream stream = this.getClass().getClassLoader()
                    .getResourceAsStream(ContentRoot.DEFAULT.getTag() + propertyFileName)) {
                final Properties properties = new Properties();
                properties.load(stream);
                return properties;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
