package com.neolab.crm.shared.domain;

public enum TaskStatus {

	ACTIVE() {
		@Override
		public Boolean getBoolean() {
			return false;
		}
	},
	FINISHED() {
		@Override
		public Boolean getBoolean() {
			return true;
		}
	},
	EXPIRED() {
		@Override
		public Boolean getBoolean() {
			return null;
		}
	};
	
	public abstract Boolean getBoolean();
	public static TaskStatus getStatus(Boolean bool){
		if(bool)
			return TaskStatus.FINISHED;
		else
			return TaskStatus.ACTIVE;
	}
	
	@Override
	public String toString() {
		return super.toString().toUpperCase();
	}

}
