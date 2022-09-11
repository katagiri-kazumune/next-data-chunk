package jp.classmethod.aws.metropolis.chunk;

public class InvalidKeyExpressionException extends RuntimeException {

  public InvalidKeyExpressionException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}
