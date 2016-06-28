package com.tomtop.valueobjects.order;


import java.io.Serializable;

public class OrderCount implements Serializable {
	private Integer all;
	private Integer pending;
	private Integer confirmed;
	private Integer onHold;
	private Integer processing;
	private Integer dispatched;
	private Integer cancelled;
	private Integer refunded;
	private Integer recycle;

	public Integer getAll() {
		return this.all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getPending() {
		return this.pending;
	}

	public void setPending(Integer pending) {
		this.pending = pending;
	}

	public Integer getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}

	public Integer getOnHold() {
		return this.onHold;
	}

	public void setOnHold(Integer onHold) {
		this.onHold = onHold;
	}

	public Integer getDispatched() {
		return this.dispatched;
	}

	public void setDispatched(Integer dispatched) {
		this.dispatched = dispatched;
	}

	public Integer getCancelled() {
		return this.cancelled;
	}

	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}

	public Integer getRefunded() {
		return this.refunded;
	}

	public void setRefunded(Integer refunded) {
		this.refunded = refunded;
	}

	public Integer getProcessing() {
		return this.processing;
	}

	public void setProcessing(Integer processing) {
		this.processing = processing;
	}

	public Integer getRecycle() {
		return this.recycle;
	}

	public void setRecycle(Integer recycle) {
		this.recycle = recycle;
	}
}
