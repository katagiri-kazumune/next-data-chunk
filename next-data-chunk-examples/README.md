# chunk サンプル

[Doma2](https://github.com/domaframework/doma) を使用したサンプルです。
用意するのは以下の通りです。

* Dao(FruitDao)
  * ChunkableSqlParameter を受け取って SQL を発行する
    * ChunkableSqlParameter の他にパラメータがあっても問題ないです
  * chunk のサンプルは FruitDao#chunk(ChunkableSqlParameter)
* Repository(FruitRepository)
  * Dao の呼び出しや結果を加工するのを責務とする
  * Service から呼ばれることを想定(Service は Dao を意識しない)
  * chunk のサンプルは FruitRepository#chunk

## SQL 説明

判定文を Java 側に押し込みました。

```sql
SELECT *
FROM fruits f

WHERE
    /*%if chunkable.idComparisonValue != null */
          AND f.fruit_id /*#chunkable.idComparisonOperator*/ /*chunkable.idComparisonValue*/'fruit_id_001'
    /*%end*/
ORDER BY f.fruit_id /*#chunkable.sortOrder*/

LIMIT /*chunkable.size*/10
```

SQL に埋め込む変数名 `chunkable` は、chunk 形式で取得する時の Dao のパラメータ(ChunkableSqlParameter)と名前をあわせてください。
