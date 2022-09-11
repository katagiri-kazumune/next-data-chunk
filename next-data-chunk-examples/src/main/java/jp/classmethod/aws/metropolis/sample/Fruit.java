package jp.classmethod.aws.metropolis.sample;

import java.util.UUID;
import jp.classmethod.aws.metropolis.chunk.ChunkElement;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity(immutable = true)
@Table(name = "fruits")
@Value
@RequiredArgsConstructor
public class Fruit implements ChunkElement {
  @Id
  @Column(name = "fruit_id")
  String fruitId;

  @Column(name = "fruit_name")
  String fruitName;

  @Column(name = "weight")
  long weight;

  public Fruit(String name, long weight) {
    this.fruitId = UUID.randomUUID().toString();
    this.fruitName = name;
    this.weight = weight;
  }

  @Override
  public String getIdValue() {
    return this.fruitId;
  }
}
