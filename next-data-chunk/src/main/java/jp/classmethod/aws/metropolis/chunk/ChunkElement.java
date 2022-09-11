package jp.classmethod.aws.metropolis.chunk;

public interface ChunkElement {

  /**
   * 要素の識別子を返却する.
   *
   * <p>当該レコードの id 項目の値を返却します。 ユニークでかつ、単一の値でなければなりません。
   *
   * @return 要素の識別子
   */
  String getIdValue();
}
