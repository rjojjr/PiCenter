export const addToLoading = (loadingList, loadingDevice) => {
    loadingList.filter(device => device === loadingDevice);
    loadingList.push(loadingDevice);
    return loadingList;
}

export const removeFromLoading = (loadingList, loadingDevice) => {
    loadingList.filter(device => device === loadingDevice);
    return loadingList;
}

export const changeStatus = (deviceList, device) => {
    deviceList.filter(list => list.name === device.name);
    deviceList.push(device);
    return deviceList;
}

export const isDeviceInLoading = (deviceList, device) => {
    deviceList.map((list, i) => {
        if (list.name === device.name) {
            return true;
        }
    });
    return false;
}