package jp.classmethod.aws.metropolis.chunk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ChunkUtils {

  public static <T extends ChunkElement> Chunk<T> buildChunk(
      Chunkable chunkable, Function<ChunkableSqlParameter, List<T>> function) {
    var sqlParameter = chunkable.getSqlParameter();
    var result = function.apply(sqlParameter);

    if (sqlParameter.hasBefore()) {
      // before の場合、SQL の結果に対してソートを行う
      // Doma2 で SQL ステートメントをまたいだ if 文がかけないので、逆にソートする場合は Java 側で行う
      result = additionalSort(sqlParameter, result);
    }
    return new Chunk<>(result, chunkable);
  }

  private static <T extends ChunkElement> List<T> additionalSort(
      ChunkableSqlParameter sqlParameter, List<T> result) {
    var list = new ArrayList<>(result);
    if (sqlParameter.isAscSubOrder()) {
      // 昇順
      var idValueAscComparator = Comparator.comparing(ChunkElement::getIdValue);
      list.sort(idValueAscComparator);
    } else {
      // 降順
      var idValueDescComparator =
          new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
              return o2.getIdValue().compareTo(o1.getIdValue());
            }
          };
      list.sort(idValueDescComparator);
    }
    return list;
  }
}
