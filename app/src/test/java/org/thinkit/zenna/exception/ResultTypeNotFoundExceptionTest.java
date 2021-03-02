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

package org.thinkit.zenna.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * The class that manages test case of {@link ResultTypeNotFoundException} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public final class ResultTypeNotFoundExceptionTest {

    @Test
    public void testDefaultConstructor() {
        assertNotNull(new ResultTypeNotFoundException());
    }

    @Test
    public void testConstructorWithMessage() {
        final String message = "This is a test message.";
        final ResultTypeNotFoundException exception = new ResultTypeNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndException() {
        final String message = "This is a test message.";
        ResultTypeNotFoundException exception = null;

        try {
            new ArrayList<>(0).get(1);
        } catch (IndexOutOfBoundsException e) {
            exception = new ResultTypeNotFoundException(message, e);
        }

        assertNotNull(exception);
        assertNotNull(exception.getCause());
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithException() {

        ResultTypeNotFoundException exception = null;

        try {
            new ArrayList<>(0).get(1);
        } catch (IndexOutOfBoundsException e) {
            exception = new ResultTypeNotFoundException(e);
        }

        assertNotNull(exception);
        assertNotNull(exception.getCause());
    }
}
