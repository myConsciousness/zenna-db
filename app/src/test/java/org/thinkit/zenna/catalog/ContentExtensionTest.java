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

package org.thinkit.zenna.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * The class that manages test case of {@link ContentExtension} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class ContentExtensionTest {

    /**
     * The expected catalog definition
     */
    private static final Map<Integer, String> EXPECTED_DEFINITION = new HashMap<>() {

        /**
         * The serial version UID
         */
        private static final long serialVersionUID = -2448781453184569672L;

        {
            put(0, "json");
        }
    };

    @Test
    void testExpectedDefinition() {

        final List<ContentExtension> contentExtensions = Arrays.asList(ContentExtension.values());

        for (int i = 0, size = contentExtensions.size(); i < size; i++) {
            assertEquals(EXPECTED_DEFINITION.get(i), contentExtensions.get(i).getTag());
        }
    }
}
