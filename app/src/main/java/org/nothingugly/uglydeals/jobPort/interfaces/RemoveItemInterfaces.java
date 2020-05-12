package org.nothingugly.uglydeals.jobPort.interfaces;

import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

public interface RemoveItemInterfaces {
    void removeItem(String userId);

    void remove(int pos);

    void addItem(String name);

    void itemClick(CommonJobsModel commonJobsModel);
}
