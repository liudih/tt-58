package com.tomtop.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class DoubleCalculateUtils {
	private BigDecimal result;
	private BigDecimal iniValue;
	private MathContext mcontext;

	public DoubleCalculateUtils(double v1) {
		this.iniValue = new BigDecimal(v1);
		this.result = new BigDecimal(v1);
		this.mcontext = new MathContext(2, RoundingMode.HALF_UP);
	}

	public DoubleCalculateUtils(double v1, MathContext mcontext) {
		this.iniValue = new BigDecimal(v1);
		this.result = new BigDecimal(v1);
		this.mcontext = mcontext;
	}

	public DoubleCalculateUtils add(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.add(d1);
		DoubleCalculateUtils tmp = new DoubleCalculateUtils(resultValue(),
				this.mcontext);
		return tmp;
	}

	public DoubleCalculateUtils subtract(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.subtract(d1);
		return new DoubleCalculateUtils(resultValue(), this.mcontext);
	}

	public DoubleCalculateUtils divide(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.divide(d1);
		return new DoubleCalculateUtils(resultValue(), this.mcontext);
	}

	public DoubleCalculateUtils multiply(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		this.result = this.iniValue.multiply(d1);
		return new DoubleCalculateUtils(resultValue(), this.mcontext);
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
