package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.test.context.ContextConfiguration;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

@ContextConfiguration(locations = { "applicationContext.xml" })
public class Test {

	@org.junit.Test
	public void main() {
		try {
			FSRepositoryFactory.setup();
			String url = "file://c:/repos/root/path/";
			String path = "c:/repos/root/path/";
			String userName = "foo";
			String userPassword = "bar";
			SVNURL svnUrl = SVNURL.parseURIDecoded(url);

			// to create new repository
			// SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(
			// new File(path), true, false);
			byte[] contents = "This is a new file".getBytes();
			byte[] modifiedContents = "This is the same file but modified a little."
					.getBytes();

			SVNRepository repository = SVNRepositoryFactory.create(svnUrl);
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(userName, userPassword);
			repository.setAuthenticationManager(authManager);

			 listEntries(repository, "");
			 
			 writeToFile("C://test.txt", getFile(repository, "dirA/file.txt"));
			 
			 System.exit(0);
			long latestRevision = repository.getLatestRevision();

			ISVNEditor editor = repository.getCommitEditor(
					"directory and file added", null);
			
			SVNCommitInfo commitInfo;
			// add dir
			try {
				commitInfo = addDir(editor, "test",
						"test/file.txt", contents);
				System.out.println("The directory was added: " + commitInfo);
			} catch (SVNException svne) {
				editor.abortEdit();
				throw svne;
			}

			// change contents
			editor = repository.getCommitEditor("file contents changed", null);
			try {
				commitInfo = modifyFile(editor, "test", "test/file.txt",
						contents, modifiedContents);
				System.out.println("The file was changed: " + commitInfo);
			} catch (SVNException svne) {
				editor.abortEdit();
				throw svne;
			}
	        System.out.println( "Repository latest revision (after committing): " + latestRevision );

//
//			editor.openRoot(-1);
//			// editor.openDir("dirA", -1);
//			// editor.addFile("dirA/file.txt", null, -1);
//			File file = new File("C://test.txt");
//			editor.closeEdit();
//			SVNNodeKind nodeKind = repository.checkPath("/", -1);
//			if (nodeKind == SVNNodeKind.NONE) {
//				System.err.println("There is no entry at '" + url + "'.");
//				// System.exit( 1 );
//			} else if (nodeKind == SVNNodeKind.FILE) {
//				System.err.println("The entry at '" + url
//						+ "' is a file while a directory was expected.");
//				// System.exit( 1 );
//			}
//
//			listEntries(repository, "");
//			// modifyFile(editor, "dirA", "dirA/file.txt",
//			// getBytesFromFile(file), getBytesFromFile(file));
//
//			// System.out.println( "Repository Root: " +
//			// repository.getRepositoryRoot( true ) );
//			// System.out.println( "Repository UUID: " +
//			// repository.getRepositoryUUID( true ) );
//			//
//			// SVNNodeKind nodeKind = repository.checkPath( "" , -1 );
//			// if ( nodeKind == SVNNodeKind.NONE ) {
//			// System.err.println( "There is no entry at '" + url + "'." );
//			// System.exit( 1 );
//			// } else if ( nodeKind == SVNNodeKind.FILE ) {
//			// System.err.println( "The entry at '" + url +
//			// "' is a file while a directory was expected." );
//			// System.exit( 1 );
//			// }
//			//
//			// listEntries(repository, "");

		} catch (SVNException e) {
			e.printStackTrace();
		}
	}

	public static void listEntries(SVNRepository repository, String path)
			throws SVNException {
		Collection entries = repository.getDir(path, -1, null,
				(Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/")
					+ entry.getName() + " ( author: '" + entry.getAuthor()
					+ "'; revision: " + entry.getRevision() + "; date: "
					+ entry.getDate() + ")");
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName());
			}
		}
	}

	private static SVNCommitInfo modifyFile(ISVNEditor editor, String dirPath,
			String filePath, byte[] oldData, byte[] newData)
			throws SVNException {
		editor.openRoot(-1);

		editor.openDir(dirPath, -1);

		editor.openFile(filePath, -1);

		editor.applyTextDelta(filePath, null);

		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(filePath,
				new ByteArrayInputStream(oldData), 0, new ByteArrayInputStream(
						newData), editor, true);

		// Closes filePath.
		editor.closeFile(filePath, checksum);

		// Closes dirPath.
		editor.closeDir();

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}

	public static byte[] getBytesFromFile(File file) {
		try {
			InputStream is = new FileInputStream(file);

			// Get the size of the file
			long length = file.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
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
