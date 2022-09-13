package jp.classmethod.aws.metropolis.chunk;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChunkableSqlParameter {

  public static final PaginationTokenEncoder ENCODER = new PaginationTokenEncoder();

  /** 指定したソート順. */
  String direction;
  /** prev 時に使用する ID 項目の値. */
  String before;
  /** next 時に使用する ID 項目の値. */
  String after;
  /** chunk に含む最大要素数. */
  int size;
  /** 発行する SQL でのソート順. */
  String sortOrder;
  
  public static ChunkableSqlParameter of(Chunkable chunkable) {

    var ascending = chunkable.getDirection() == null || chunkable.getDirection() == Direction.ASC;
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

    String direction = null;
    if (ascending == false) {
      direction = chunkable.getDirection().name();
    }
    return new ChunkableSqlParameter(direction, before, after, chunkable.getMaxPageSize(), buildSortOrder(before, direction));
  }

  private static String buildSortOrder(String before, String direction) {
    if(direction == null || Objects.equals(direction, "ASC")) {
      // direction が ASC 相当
      return before == null ? "ASC" : "DESC";
    }
    
    // direction が DESC 相当
    return before != null ? "ASC" : "DESC";
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
    return Objects.equals(direction, "DESC") == false;
  }
}
