package com.neolab.crm.server.persistance;

import org.tmatesoft.svn.core.io.SVNRepository;

public interface DocumentsSVN {
	
	public void getAllDocuments();
	
	public void getAllRevisionsForADocument();
	
	public void getDocumentByRevision();
	
	public void putDocument();
	
	public void deleteDocument();
}
