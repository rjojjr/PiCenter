export const  addToLoading = (loadingList, loadingDevice) => {
    loadingList.filter(device=== loadingDevice);
    loadingList.push(loadingDevice);
    return loadingList;
}

export const removeFromLoading = (loadingList, loadingDevice) => {
    loadingList.filter(device === loadingDevice);
    return loadingList;
}

export const changeStatus = (deviceList, dvice) => {
    deviceList.filter(deviceList.name === device.name);
    deviceList.push(device);
    return deviceList;
}