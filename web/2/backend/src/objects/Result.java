package objects;

import java.time.Instant;

public class Result {
  public int x;
  public double y;
  public double r;
  public boolean checkingResult;
  public Instant currentTime;
  public long executionTimeMs;

  public Result(int x, double y, double r, boolean checkingResult, Instant currentTime, long executionTimeMs) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.checkingResult = checkingResult;
    this.currentTime = currentTime;
    this.executionTimeMs = executionTimeMs;
  }
}