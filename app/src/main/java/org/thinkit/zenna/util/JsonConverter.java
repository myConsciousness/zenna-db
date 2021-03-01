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

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.thinkit.zenna.exception.ContentParsingException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This class defines a generic conversion process for json using the Jackson
 * library.
 *
 * <p>
 * Use the methods defined in {@link JsonConverter} to perform the json
 * conversion process. Use {@link JsonConverter#toObject(String, TypeReference)}
 * to convert it to an object with generic information.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonConverter {

    /**
     * The mapper
     */
    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES).build();

    /**
     * Converts the JSON string passed as an argument to a {@link LinkedHashMap}
     * structure.
     *
     * @param jsonString The JSON string to be converted
     * @return The JSON map based on {@link LinkedHashMap}
     *
     * @exception NullPointerException    If {@code null} was passed as an argument
     * @exception ContentParsingException If there is a syntax error in the JSON
     *                                    string
     */
    public static Map<String, Object> toLinkedHashMap(@NonNull final String jsonString) {
        return toObject(jsonString, new TypeReference<LinkedHashMap<String, Object>>() {
        });
    }

    /**
     * Converts the object specified as an argument to an arbitrary structure.
     *
     * @param <T>          The type after conversion
     * @param jsonString   The JSON string
     * @param valueTypeRef The type reference object
     * @return The converted object
     *
     * @exception NullPointerException    If {@code null} was passed as an argument
     * @exception ContentParsingException If there is a syntax error in the JSON
     *                                    string
     */
    public static <T> T toObject(@NonNull final String jsonString, @NonNull final TypeReference<T> valueTypeRef) {
        try {
            return mapper.readValue(jsonString, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new ContentParsingException(e);
        }
    }
}
