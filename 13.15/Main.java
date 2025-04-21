import java.math.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter rational r1 with numerator and denominator seperated by a space: ");
        String n1 = input.next();
        String d1 = input.next();

        System.out.print("Enter rational r2 with numerator and denominator seperated by a space: ");
        String n2 = input.next();
        String d2 = input.next();

        RationalUsingBigInteger r1 = new RationalUsingBigInteger(
        new BigInteger(n1), new BigInteger(d1));
        RationalUsingBigInteger r2 = new RationalUsingBigInteger(
        new BigInteger(n2), new BigInteger(d2));

        System.out.println(r1 + " + " + r2 + " = " + r1.add(r2));
        System.out.println(r1 + " - " + r2 + " = " + r1.subtract(r2));
        System.out.println(r1 + " * " + r2 + " = " + r1.multiply(r2));
        System.out.println(r1 + " / " + r2 + " = " + r1.divide(r2));
        System.out.println(r2 + " is " + r2.doubleValue());
    }
}

class RationalUsingBigInteger extends Number implements Comparable<RationalUsingBigInteger> {
  private BigInteger numerator = BigInteger.ZERO;
  private BigInteger denominator = BigInteger.ONE;

  public RationalUsingBigInteger(BigInteger numerator, BigInteger denominator) {
    BigInteger gcd = numerator.gcd(denominator);
    this.numerator = numerator.divide(gcd);
    this.denominator = denominator.divide(gcd);
  }
  
  public BigInteger getNumerator() {
    return numerator;
  }

  public BigInteger getDenominator() {
    return denominator;
  }

  public RationalUsingBigInteger add(RationalUsingBigInteger secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator()).add(denominator.multiply(secondRational.getNumerator()));
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new RationalUsingBigInteger(n, d);
  }

  public RationalUsingBigInteger subtract(RationalUsingBigInteger secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator()).subtract(denominator.multiply(secondRational.getNumerator()));
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new RationalUsingBigInteger(n, d);
  }

  public RationalUsingBigInteger multiply(RationalUsingBigInteger secondRational) {
    BigInteger n = numerator.multiply(secondRational.getNumerator());
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new RationalUsingBigInteger(n, d);
  }

  public RationalUsingBigInteger divide(RationalUsingBigInteger secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator());
    BigInteger d = denominator.multiply(secondRational.getNumerator());
    return new RationalUsingBigInteger(n, d);
  }
  
  @Override
  public String toString() {
    if (denominator.equals(BigInteger.ONE)) {
      return numerator.toString();
    } else {
      return numerator.toString() + "/" + denominator.toString();
    }
  }

  @Override
  public boolean equals(Object other) {
    return (this.subtract((RationalUsingBigInteger)(other))).getNumerator().equals(BigInteger.ZERO);
  }

  @Override
  public int intValue() {
    return numerator.divide(denominator).intValue();
  }

  @Override
  public long longValue() {
    return numerator.divide(denominator).longValue();
  }

  @Override
  public float floatValue() {
    return numerator.divide(denominator).floatValue();
  }

  @Override
  public double doubleValue() {
    return numerator.divide(denominator).doubleValue();
  }

  @Override
  public int compareTo(RationalUsingBigInteger o) {
    if (this.subtract(o).getNumerator().compareTo(BigInteger.ZERO) > 0) {
      return 1;
    } else if (this.subtract(o).getNumerator().compareTo(BigInteger.ZERO) < 0) {
      return -1;
    }
    return 0;
  }
  
}