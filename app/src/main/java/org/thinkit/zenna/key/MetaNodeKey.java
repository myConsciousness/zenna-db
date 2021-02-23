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

package org.thinkit.zenna.key;

import lombok.RequiredArgsConstructor;

/**
 * The enum class that manages the key names supported when defining metas in
 * content files. The content key name of each element can be retrieved by
 * calling the {@link #getName} method.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum MetaNodeKey implements ContentKey {

    /**
     * {@code "meta"}
     */
    META(KeyName.meta),

    /**
     * {@code "resultType"}
     */
    RESULT_TYPE(KeyName.resultType);

    /**
     * The key name
     */
    private final KeyName keyName;

    /**
     * The inner enum that manages key name
     */
    private enum KeyName {
        meta, resultType;
    }

    @Override
    public String getName() {
        return this.keyName.name();
    }
}
