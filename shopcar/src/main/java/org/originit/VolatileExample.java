package org.originit;

class VolatileExample {

  int x = 0;

  volatile boolean v = false;

  public void writer() {
    x = 42;
    v = true;
  }

  public void reader() {

    final Thread thread = Thread.currentThread();
    while (true){
      if (thread.isInterrupted()) {
        break;
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        thread.interrupt();
      }
    }
    System.out.println(x);
  }

  public static void main(String[] args) {
    final VolatileExample volatileExample = new VolatileExample();
    final Thread reader = new Thread(() -> {
      volatileExample.reader();
    });
    final Thread a = new Thread(() -> {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      volatileExample.writer();
      reader.interrupt();
    });

    a.start();
    reader.start();
  }

}