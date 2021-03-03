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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
}
