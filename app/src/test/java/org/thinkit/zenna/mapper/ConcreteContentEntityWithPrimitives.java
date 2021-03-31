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

import java.io.Serializable;

import org.thinkit.zenna.entity.ContentEntity;

import lombok.Data;

/**
 * The concrete entity class that implements {@link ContentEntity} for testing.
 *
 * <p>
 * This data class manages patterns of the primitive data type including String.
 *
 * @author Kato Shinya
 * @since 1.0.2
 */
@Data
public final class ConcreteContentEntityWithPrimitives implements ContentEntity, Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = -6998959384300510445L;

    /**
     * The int field
     */
    private int testCount;

    /**
     * The boolean field
     */
    private boolean testFlag;

    /**
     * The long field
     */
    private double testDoubleCount;

    /**
     * The String field
     */
    private String testString;
}
