package jp.classmethod.aws.metropolis.chunk;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Chunkable {

  @Getter private final String paginationToken;

  @Getter private final PaginationRelation paginationRelation;

  @Getter private final int maxPageSize;

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
