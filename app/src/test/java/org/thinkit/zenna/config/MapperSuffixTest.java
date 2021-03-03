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

/**
 * The class that manages test case of {@link MapperSuffix} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class MapperSuffixTest {

    /**
     * The nested class for {@link MapperSuffix#getProperty()} method.
     */
    @Nested
    class TestGetProperty {

        private static final String EXPECTED_PROPERTY = "TestSuffix";

        @Test
        void testWhenMapperSuffixIsNotEmpty() {

            final MapperSuffix mapperSuffix = MapperSuffix.from("TestSuffix");
            final String suffix = mapperSuffix.getProperty();

            assertNotNull(suffix);
            assertEquals(EXPECTED_PROPERTY, suffix);
        }

        @Test
        void testWhenMapperSuffixIsEmpty() {

            final MapperSuffix mapperSuffix = MapperSuffix.from("");
            final String suffix = mapperSuffix.getProperty();

            assertNotNull(suffix);
            assertEquals("", suffix);
        }
    }
}
