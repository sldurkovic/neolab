package com.neolab.crm.client.app.widgets.hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.neolab.crm.client.app.widgets.hierarchy.SimpleMillerColumns.SearchResult;
import com.neolab.crm.shared.resources.Folder;

public class FolderMillerColumnModel implements SimpleMillerColumns.ToolTipAwareModel<Folder> {
	

    public static final Folder ROOT = new Folder("ROOT");

    @Override
    public String getToolTip(Folder folder) {
        if (folder != null) {
            return folder.getLabel();
        } else
            return "";
    }

    @Override
    public String getDisplayString(Folder folder) {
//    	log.fine("path " + item.getPath());
        if (folder == null) {
            return "";
        }
        return folder.getLabel();
    }

    @Override
    public boolean equalsTo(Folder folder1, Folder folder2) {
        return folder1 != null && folder2 != null && folder1.equals(folder2);
    }

    @Override
    public String getHeaderString(Folder folder) {
        if (folder != null) {
            return folder.getLabel();
        } else
            return "";
    }

    @Override
	public boolean isHeaderBold(Folder folder) {
		return false;
	}

	@Override
	public void search(Folder parent, int offset, int itemsPerPage,
			AsyncCallback<SearchResult<Folder>> callback) {
		List<Folder> list = new ArrayList<Folder>();
		list.add(new Folder("test"));
		list.add(new Folder("test"));
		list.add(new Folder("test"));
		list.add(new Folder("test"));
		callback.onSuccess(new SearchResult<Folder>(4, list));
	}

	@Override
	public String getIcon(Folder item) {
		return null;
	}

}
