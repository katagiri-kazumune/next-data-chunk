package jp.classmethod.aws.metropolis.sample;

public class FruitFixtures {
  public static Fruit create(String id, long weight) {
    return new Fruit(id, "name-" + id, weight);
  }
}
