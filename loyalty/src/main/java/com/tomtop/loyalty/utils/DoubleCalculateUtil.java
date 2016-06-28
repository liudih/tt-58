package com.tomtop.loyalty.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class DoubleCalculateUtil {
	private BigDecimal result;
	private BigDecimal iniValue;
	private MathContext mcontext;

	public DoubleCalculateUtil(double v1) {
		this.iniValue = new BigDecimal(v1);
		this.result = new BigDecimal(v1);
		this.mcontext = new MathContext(2, RoundingMode.HALF_UP);
	}

	public DoubleCalculateUtil(double v1, MathContext mcontext) {
		this.iniValue = new BigDecimal(v1);
		this.result = new BigDecimal(v1);
		this.mcontext = mcontext;
	}

	public DoubleCalculateUtil add(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.add(d1);
		DoubleCalculateUtil tmp = new DoubleCalculateUtil(resultValue(),
				this.mcontext);
		return tmp;
	}

	public DoubleCalculateUtil subtract(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.subtract(d1);
		return new DoubleCalculateUtil(resultValue(), this.mcontext);
	}

	public DoubleCalculateUtil divide(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.divide(d1);
		return new DoubleCalculateUtil(resultValue(), this.mcontext);
	}

	public DoubleCalculateUtil multiply(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.multiply(d1);
		return new DoubleCalculateUtil(resultValue(), this.mcontext);
	}

	public double doubleValue() {
		return this.iniValue.setScale(this.mcontext.getPrecision(),
				this.mcontext.getRoundingMode()).doubleValue();
	}

	private double resultValue() {
		return this.result.setScale(this.mcontext.getPrecision(),
				this.mcontext.getRoundingMode()).doubleValue();
	}
}
