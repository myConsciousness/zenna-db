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
 * The annotation that specifies mapping between a field and a condition defined
 * in a conten file.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

    /**
     * Specify the field in the content class that you want to tie to the condition
     * defined in the content file. The default value is empty string.
     * <p>
     * If a specific condition name is not specified when this annotation is given
     * to a field in the content class, the annotated field name is inferred as the
     * condition key defined in the content file. If the field name is different
     * from the key name defined in the content file with this annotation, specify
     * any key name in this field.
     *
     * @return The condition key
     */
    String value() default "";
}
