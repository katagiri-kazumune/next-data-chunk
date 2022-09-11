package jp.classmethod.aws.metropolis.sample;

import java.util.List;
import jp.classmethod.aws.metropolis.chunk.ChunkableSqlParameter;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.Result;

@Dao
public interface FruitDao {

  @Select
  @Sql("SELECT * FROM known_fruits")
  List<Fruit> selectAll();

  @Insert
  Result<Fruit> insert(Fruit fruit);

  @Select
  List<Fruit> chunk(ChunkableSqlParameter chunkable);
}
