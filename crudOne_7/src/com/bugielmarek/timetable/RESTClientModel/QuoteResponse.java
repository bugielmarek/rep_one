package com.bugielmarek.timetable.RESTClientModel;

public class QuoteResponse {

	private Success success;
	
	private Contents contents;

	public Success getSuccess() {
		return success;
	}

	public void setSuccess(Success success) {
		this.success = success;
	}

	public Contents getContents() {
		return contents;
	}

	public void setContents(Contents contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "QuoteResponse [success=" + success + ", contents=" + contents + "]";
	}
	
	
}
