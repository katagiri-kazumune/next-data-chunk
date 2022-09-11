package jp.classmethod.aws.metropolis.chunk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaginationTokenEncoder {

  private static final String FIRST_KEY = "first_key";

  private static final String LAST_KEY = "last_key";

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

  private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

  public String encode(String firstKey, String lastKey) {
    var map = new LinkedHashMap<String, String>(2);
    map.put(FIRST_KEY, firstKey);
    map.put(LAST_KEY, lastKey);

    try {
      var json = MAPPER.writeValueAsString(map);
      return ENCODER.encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (JsonProcessingException e) {
      throw new InvalidKeyExpressionException(e);
    }
  }

  public Optional<String> extractFirstKey(String paginationToken) {
    return extractKey(paginationToken, FIRST_KEY);
  }

  public Optional<String> extractLastKey(String paginationToken) {
    return extractKey(paginationToken, LAST_KEY);
  }

  private Optional<String> extractKey(String paginationToken, String key) {
    if (paginationToken == null) {
      return Optional.empty();
    }

    try {
      var json = DECODER.decode(paginationToken);
      var keyNode = MAPPER.readTree(json).path(key);
      var value = MAPPER.treeToValue(keyNode, String.class);
      return Optional.ofNullable(value);
    } catch (IOException | IllegalArgumentException e) {
      log.warn("Invalid pagination token: {}", paginationToken);
    }
    return Optional.empty();
  }
}
