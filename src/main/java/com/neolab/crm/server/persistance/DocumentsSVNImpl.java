package com.neolab.crm.server.persistance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public abstract class DocumentsSVNImpl implements DocumentsSVN{

	private static final String url = "file://c:/repos/root/path/";
	private static final String path = "c:/repos/root/path/";
	
	private SVNRepository repository;

	public DocumentsSVNImpl() {
		FSRepositoryFactory.setup();
	}
	
	public void getRootFolders(){
	}
	
	public void getChildFolders(){
		
	}
	
	public void getFolderFiles(){
		
	}
	
	
	private SVNRepository createRepository() {
		return repository;
	}
	
	private SVNURL getSVNUrl() throws SVNException{
		return SVNURL.parseURIDecoded(url);
	}
	
	private ISVNEditor getEditor(String message) throws SVNException{
		return repository.getCommitEditor(message, null);
	}
	
	private void setAuthentication(String username, String password){
		repository.setAuthenticationManager(
				SVNWCUtil.createDefaultAuthenticationManager(username, password));
	}
	
	private byte[] getBytesFromFile(File file) {
		try {
			InputStream is = new FileInputStream(file);
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				// File is too large
			}
			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}

			// Close the input stream and return bytes
			is.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[10];
	}
	
	private static SVNCommitInfo addDir(ISVNEditor editor, String dirPath,
			String filePath, byte[] data) throws SVNException {
		editor.openRoot(-1);

		editor.addDir(dirPath, null, -1);

		editor.addFile(filePath, null, -1);

		editor.applyTextDelta(filePath, null);

		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(filePath,
				new ByteArrayInputStream(data), editor, true);

		editor.closeFile(filePath, checksum);

		// Closes dirPath.
		editor.closeDir();

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}
	
	public byte[] getFile(SVNRepository repository, String filename) throws SVNException {
	    SVNProperties fileProperties = new SVNProperties();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    repository.getFile(filename, -1, fileProperties, baos);
	    return baos.toByteArray();
	}
	
	public void writeToFile(String path, byte[] content){
		try{
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(content);
			fos.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
}
