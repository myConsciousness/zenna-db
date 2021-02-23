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

/**
 * The interface that abstracts key names supported as a framework to be used
 * when defining content files. Be sure to implement this interface if you want
 * to add content keys supported by the framework.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface ContentKey {

    /**
     * Returns the key name.
     *
     * @return The key name
     */
    public String getName();
}
