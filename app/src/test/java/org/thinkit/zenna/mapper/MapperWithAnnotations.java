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

import org.thinkit.zenna.annotation.Condition;
import org.thinkit.zenna.annotation.Content;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The concrete class with annotations that extends {@link ContentMapper} for
 * testing.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(staticName = "newInstance")
@Content("ConcreteContentForAnnotations")
public final class MapperWithAnnotations extends ContentMapper<ConcreteContentEntityWithAttribute> {

    /**
     * The condition item
     */
    @Setter
    @Condition("variableName")
    private String anotherName;
}