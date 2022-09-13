package jp.classmethod.aws.metropolis.chunk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Chunk.
 *
 * @param <T> Chunk の要素
 */
@EqualsAndHashCode(exclude = "chunkable")
public class Chunk<T extends ChunkElement> {

  public static final PaginationTokenEncoder ENCODER = new PaginationTokenEncoder();

  /** Chunk の要素. */
  @JsonProperty private final List<T> content = new ArrayList<>();

  /**
   * トークン.
   *
   * <p>この Chunk の次 or 前の Chunk を取得する時に使用。 Chunk の要素が空の時、null を設定します。
   */
  @JsonProperty @Getter private final String paginationToken;

  /** Chunk 要素取得時のパラメータ. */
  @JsonIgnore @Getter private final Chunkable chunkable;

  public Chunk(List<T> content, Chunkable chunkable) {
    this.content.addAll(content);
    this.paginationToken = buildPaginationToken(content);
    this.chunkable = chunkable;
  }

  private String buildPaginationToken(List<T> content) {
    if (content.isEmpty()) {
      return null;
    }
    return ENCODER.encode(
        content.get(0).getIdValue(), content.get(content.size() - 1).getIdValue());
  }

  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  public Stream<T> stream() {
    return content.stream();
  }

  public boolean hasContent() {
    return content.isEmpty() == false;
  }

  public boolean hasNext() {
    return getLastKey() != null && isLast() == false;
  }

  public boolean hasPrev() {
    return getFirstKey() != null && isFirst() == false;
  }

  public boolean isLast() {
    Integer maxPageSize = chunkable.getMaxPageSize();
    if (maxPageSize == null) {
      return false;
    }
    return content.size() < maxPageSize;
  }

  public boolean isFirst() {
    return getFirstKey() == null;
  }

  public Chunkable nextChunkable() {
    if (hasNext() == false) {
      return null;
    }
    return Chunkable.of(
        paginationToken,
        PaginationRelation.NEXT,
        chunkable.getMaxPageSize(),
        chunkable.getDirection());
  }

  public Chunkable prevChunkable() {
    if (hasPrev() == false) {
      return null;
    }
    return Chunkable.of(
        paginationToken,
        PaginationRelation.PREV,
        chunkable.getMaxPageSize(),
        chunkable.getDirection());
  }

  private String getLastKey() {
    return ENCODER.extractLastKey(paginationToken).orElse(null);
  }

  private String getFirstKey() {
    return ENCODER.extractFirstKey(paginationToken).orElse(null);
  }

  public int size() {
    return content.size();
  }

  public boolean isEmpty() {
    return content.isEmpty();
  }
}
