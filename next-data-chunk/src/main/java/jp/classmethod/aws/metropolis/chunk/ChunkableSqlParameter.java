package jp.classmethod.aws.metropolis.chunk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChunkableSqlParameter {

  public static final PaginationTokenEncoder ENCODER = new PaginationTokenEncoder();

  /** 指定したソート順. */
  Direction direction;
  /** prev 時に使用する ID 項目の値. */
  String before;
  /** next 時に使用する ID 項目の値. */
  String after;
  /** chunk に含む最大要素数. */
  int size;
  /** 発行する SQL でのソート順. */
  String sortOrder;

  /** ID 項目の比較演算子. */
  String idComparisonOperator;

  /** ID 項目の比較値. */
  String idComparisonValue;

  public static ChunkableSqlParameter of(Chunkable chunkable) {

    var forward = isForward(chunkable.getPaginationRelation());

    var paginationToken = chunkable.getPaginationToken();
    String after = null;
    String before = null;
    if (paginationToken != null) {
      if (forward) {
        after = ENCODER.extractLastKey(paginationToken).orElse(null);
      } else {
        before = ENCODER.extractFirstKey(paginationToken).orElse(null);
      }
    }

    Direction direction = chunkable.getDirection();
    return new ChunkableSqlParameter(
        direction,
        before,
        after,
        chunkable.getMaxPageSize(),
        buildSortOrder(before, direction),
        buildIdComparisonOperator(before, after, direction),
        buildIdComparisonValue(before, after));
  }

  private static String buildSortOrder(String before, Direction direction) {
    if (direction == null || direction == Direction.ASC) {
      // direction が ASC 相当
      return before == null ? "ASC" : "DESC";
    }

    // direction が DESC 相当
    return before != null ? "ASC" : "DESC";
  }

  private static String buildIdComparisonOperator(
      String before, String after, Direction direction) {
    if (after != null) {
      return (direction == Direction.DESC) == false ? ">" : "<";
    }

    if (before != null) {
      return (direction == Direction.DESC) == false ? "<" : ">";
    }

    return null;
  }

  private static String buildIdComparisonValue(String before, String after) {
    if (after != null) {
      return after;
    }
    return before;
  }

  private static boolean isForward(PaginationRelation paginationRelation) {
    return paginationRelation == null || paginationRelation == PaginationRelation.NEXT;
  }

  public boolean hasBefore() {
    return before != null;
  }

  public boolean isAscSubOrder() {
    if (hasBefore() == false) {
      throw new IllegalArgumentException("invalid before value");
    }
    return (direction == Direction.DESC) == false;
  }
}
