package net.chepa.home6;

public interface StringConsumer {
  public void consume(String str);

  void addConsumer(StringConsumer me);
}