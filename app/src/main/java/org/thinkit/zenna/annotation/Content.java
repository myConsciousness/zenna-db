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

package org.thinkit.zenna.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that specifies the mapping between the content class and the
 * content file.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Content {

    /**
     * Specify the name of the content file to be mapped. The file extension is not
     * required when specifying the content file name. The default value is empty
     * string.
     * <p>
     * When mapping content, inferences can be made from content class names and
     * content file names. Therefore if the class name of the content class to which
     * this annotation is assigned starts with the content file name, there is no
     * need to set this field.
     * <p>
     * Even if only a content name is specified instead of a path, it will search
     * under the resources folder for content files associated with the specified
     * content name. If there is a file associated with the specified path, the
     * specified content file will be used.
     *
     * @return The content name to be used
     */
    String value() default "";
}
