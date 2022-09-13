# chunk の実装

chunk は Utils 経由で実現します。

chunk で絞り込みやソート順に使う key は、
「unique な 単一カラム」です。

# class 概要

- Chunkable
  - chunk 取得時のパラメータ
  - MSC 側でインスタンス生成します
- ChunkableSqlParameter
  - SQL 発行時のパラメータ
  - こちらでインスタンス生成します
- Chunk
  - chunk のレスポンス
  - `paginationToken` プロパティを次(or 前)の chunk 取得時のパラメータとして使用します
  - こちらでインスタンス生成します
- ChunkUtils
  - MSC 側で使用する Utils
  - dao のレスポンスを元に Chunk を返します
