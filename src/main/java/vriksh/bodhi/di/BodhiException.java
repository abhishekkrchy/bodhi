package vriksh.bodhi.di;

/**
 * Exception class used in library.
 */
class BodhiException extends RuntimeException {

  BodhiException(String message) {
    super(message);
  }

  BodhiException(Throwable cause) {
    super(cause);
  }
}
