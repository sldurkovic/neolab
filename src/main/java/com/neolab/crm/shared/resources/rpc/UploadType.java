package com.neolab.crm.shared.resources.rpc;

public enum UploadType {
	
	PROFILE_IMAGE(){
		@Override
		public String toString() {
			return "profile_image";
		}
	},
	
	PROJECT_DOCUMENTS(){
		@Override
		public String toString() {
			return "project_documents";
		}
	}

}
