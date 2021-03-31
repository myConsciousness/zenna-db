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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * The class that manages test cases for {@link ContentMapper} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class ContentMapperTest {

    @Test
    void testWhenContentHasOneSelectionNode() {

        final List<ConcreteContentEntity> results = ConcreteContentMapper.newInstance().scan();

        assertNotNull(results);
        assertTrue(results.size() == 1);

        final ConcreteContentEntity result = results.get(0);

        assertEquals("success1", result.getTest1());
        assertEquals("success2", result.getTest2());
    }

    @Test
    void testWhenContentHasSelectionWithCondition() {

        final List<List<String>> expectedResults = new ArrayList<>(2);
        expectedResults.add(List.of("success1", "success2"));
        expectedResults.add(List.of("success3", "success4"));

        final ConcreteContentWithConditionsMapper sut = ConcreteContentWithConditionsMapper.newInstance();

        for (int i = 0; i < 2; i++) {

            sut.setVariableName(String.valueOf(i));

            final List<ConcreteContentEntity> results = sut.scan();

            assertNotNull(results);
            assertTrue(results.size() == 1);

            final ConcreteContentEntity result = results.get(0);
            final List<String> expectedResult = expectedResults.get(i);

            assertEquals(expectedResult.get(0), result.getTest1());
            assertEquals(expectedResult.get(1), result.getTest2());
        }
    }

    @Test
    void testWhenContentHasAnnotations() {

        final List<List<String>> expectedResults = new ArrayList<>(2);
        expectedResults.add(List.of("success1", "success2"));
        expectedResults.add(List.of("success3", "success4"));

        final MapperWithAnnotations sut = MapperWithAnnotations.newInstance();

        for (int i = 0; i < 2; i++) {

            sut.setAnotherName(String.valueOf(i));

            final List<ConcreteContentEntityWithAttribute> results = sut.scan();

            assertNotNull(results);
            assertTrue(results.size() == 1);

            final ConcreteContentEntityWithAttribute result = results.get(0);
            final List<String> expectedResult = expectedResults.get(i);

            assertEquals(expectedResult.get(0), result.getAnotherName1());
            assertEquals(expectedResult.get(1), result.getAnotherName2());
        }
    }

    @Test
    void testWhenContentAndFieldHasVariousDataTypes() {

        final List<ConcreteContentEntityWithPrimitives> results = ConcreteContentEntityWithPrimitivesMapper
                .newInstance().scan();

        assertNotNull(results);
        assertTrue(results.size() == 1);

        final ConcreteContentEntityWithPrimitives result = results.get(0);

        assertEquals(1, result.getTestCount());
        assertEquals(false, result.isTestFlag());
        assertEquals(1.0, result.getTestDoubleCount());
        assertEquals("test", result.getTestString());
    }
}
