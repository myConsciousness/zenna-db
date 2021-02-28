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

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class represents the package name of the content defined in the content
 * property file {@code "content.properties"} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
final class ContentPackage implements Property, Serializable {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = -7329725953984635596L;

    /**
     * The slash
     */
    private static final String SLASH = "/";

    /**
     * The raw package name defined in {@code "content.properties"} file
     */
    private String packageName;

    /**
     * {@inheritDoc}
     *
     * <p>
     * Parse the package name of the content defined in the content property file
     * {@code "content.properties"} and make the minimum necessary corrections. The
     * correction determines whether or not a slash exists before or after the
     * defined package name, and if no slash exists, a slash is added to either
     * before or after the package name where no slash exists before the package
     * name is returned.
     *
     * <p>
     * If the property file does not exist, or if the content package name is not
     * defined in the property file, an empty string is returned.
     *
     * @return The content package name
     */
    @Override
    public String getProperty() {

        if (StringUtils.isEmpty(this.packageName)) {
            return "";
        }

        final StringBuilder correctPackageName = new StringBuilder();

        if (!this.packageName.startsWith(SLASH)) {
            correctPackageName.append(SLASH);
        }

        correctPackageName.append(this.packageName);

        if (!this.packageName.endsWith(SLASH)) {
            correctPackageName.append(SLASH);
        }

        return correctPackageName.toString();
    }
}
