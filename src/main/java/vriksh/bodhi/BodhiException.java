package vriksh.bodhi;

class BodhiException extends RuntimeException {

  BodhiException(String message) {
    super(message);
  }

  BodhiException(Throwable cause) {
    super(cause);
  }
}
