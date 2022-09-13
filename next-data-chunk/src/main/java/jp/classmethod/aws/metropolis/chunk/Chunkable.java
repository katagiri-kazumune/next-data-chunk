package jp.classmethod.aws.metropolis.chunk;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/** Chunk 取得時のパラメータ. */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Chunkable {

  /**
   * 前 or 次の Chunk を取得する時のトークン.
   *
   * <pre>
   * null の時は先頭の Chunk を取得します。
   * </pre>
   */
  @Getter private final String paginationToken;

  /**
   * 取得するのは前 or 次の Chunk か？
   *
   * <pre>
   * paginationToken が not null の時に有効です。
   * 本項目が null の時は 次の Chunk を取得するとみなします。
   * </pre>
   */
  @Getter private final PaginationRelation paginationRelation;

  /** chunk に含める最大要素数. */
  @Getter private final int maxPageSize;

  /**
   * Chunk を取得する際の全体のソート順.
   *
   * <pre>
   * null の時は ASC とみなします。
   * </pre>
   */
  @Getter private final Direction direction;

  public static Chunkable of(String paginationToken) {
    return new Chunkable(paginationToken, PaginationRelation.NEXT, 10, Direction.ASC);
  }

  public static Chunkable of(int maxPageSize) {
    return new Chunkable(null, PaginationRelation.NEXT, maxPageSize, Direction.ASC);
  }

  public static Chunkable of(String paginationToken, Direction parameterDirection) {
    return new Chunkable(
        paginationToken,
        PaginationRelation.NEXT,
        10,
        buildDirectionForParameter(parameterDirection));
  }

  public static Chunkable of(int maxPageSize, Direction parameterDirection) {
    return new Chunkable(
        null, PaginationRelation.NEXT, maxPageSize, buildDirectionForParameter(parameterDirection));
  }

  private static Direction buildDirectionForParameter(Direction parameterDirection) {
    if (parameterDirection == null) {
      return Direction.ASC;
    }
    return parameterDirection;
  }

  public static Chunkable of(
      String paginationToken,
      PaginationRelation paginationRelation,
      int maxPageSize,
      Direction direction) {
    return new Chunkable(paginationToken, paginationRelation, maxPageSize, direction);
  }

  public ChunkableSqlParameter getSqlParameter() {
    return ChunkableSqlParameter.of(this);
  }
}
