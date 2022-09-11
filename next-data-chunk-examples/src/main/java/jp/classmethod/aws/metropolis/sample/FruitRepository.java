package jp.classmethod.aws.metropolis.sample;

import java.util.List;
import jp.classmethod.aws.metropolis.chunk.Chunk;
import jp.classmethod.aws.metropolis.chunk.ChunkUtils;
import jp.classmethod.aws.metropolis.chunk.Chunkable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FruitRepository {

  private final FruitDao fruitDao;

  public List<Fruit> findAll() {
    return fruitDao.selectAll();
  }

  public Fruit create(Fruit fruit) {
    return fruitDao.insert(fruit).getEntity();
  }

  public Chunk<Fruit> chunk(Chunkable chunkable) {
    return ChunkUtils.buildChunk(chunkable, fruitDao::chunk);
  }
}
