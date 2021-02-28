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

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class represents the suffix name of mapper object defined in the content
 * property file {@code "content.properties"} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
final class MapperSuffix implements Property, Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = -7831278671904511922L;

    /**
     * The suffix
     */
    private String suffix;

    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns the Mapper suffix defined as {@code "mapperSuffix"} in the property
     * file {@code "content.properties"} .
     *
     * @return The mapper suffix
     */
    @Override
    public String getProperty() {
        return this.suffix;
    }
}
