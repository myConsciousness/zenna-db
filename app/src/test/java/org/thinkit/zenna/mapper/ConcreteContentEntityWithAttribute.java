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

import org.thinkit.zenna.annotation.Attribute;
import org.thinkit.zenna.entity.ContentEntity;

import lombok.Data;

/**
 * The concrete entity class with attribute annotation that implements
 * {@link ContentEntity} for testing.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Data
public final class ConcreteContentEntityWithAttribute implements ContentEntity, Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 1934317616872417182L;

    /**
     * The test field 1.
     */
    @Attribute("test1")
    private String anotherName1;

    /**
     * The test field 2.
     */
    @Attribute("test2")
    private String anotherName2;
}
