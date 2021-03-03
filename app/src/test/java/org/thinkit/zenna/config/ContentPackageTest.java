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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The class that manages test case of {@link ContentPackage} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class ContentPackageTest {

    /**
     * The nested test class for {@link ContentPackage#getProperty()} method.
     */
    @Nested
    class TestGetProperty {

        /**
         * The expected property
         */
        private static final String EXPECTED_PROPERTY = "/org/thinkit/zenna/";

        @ParameterizedTest
        @ValueSource(strings = { "org/thinkit/zenna", "/org/thinkit/zenna", "org/thinkit/zenna/",
                "/org/thinkit/zenna/" })
        void testWhenPropertyDoesNotHaveSlashes(final String parameter) {

            final ContentPackage contentPackage = ContentPackage.from(parameter);
            final String packageName = contentPackage.getProperty();

            assertNotNull(packageName);
            assertEquals(EXPECTED_PROPERTY, packageName);
        }

        @Test
        void testWhenPackageNameIsEmpty() {

            final ContentPackage contentPackage = ContentPackage.from("");
            final String packageName = contentPackage.getProperty();

            assertNotNull(packageName);
            assertEquals("", packageName);
        }
    }
}
