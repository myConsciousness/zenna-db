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

package org.thinkit.zenna.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Provides operations on {@link Field} object.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldResolver {

    /**
     * Ckeck if the {@code field} passed as an argument is a static field.
     *
     * @param field The field
     * @return {@code true} if the field is static, otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static boolean isStatic(@NonNull Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
}
