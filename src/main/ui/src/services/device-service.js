export const addToLoading = (loadingList, loadingDevice) => {
    const list = loadingList.filter(device => (!device === loadingDevice));
    list.push(loadingDevice);
    return list;
};

export const removeFromLoading = (loadingList, loadingDevice) => {
    const list = loadingList.filter(device => !(device === loadingDevice));
    return list;
};

export const changeStatus = (deviceList, device) => {
    let index = -1;
    const list = deviceList;
    deviceList.forEach((dev,  i) => {
        if(dev.name === device.name){
            index = i;
        }
    })
    if (index > -1){
        list[index] = device;
    }
    return list;
};

export const isDeviceInLoading = (deviceList, device) => {
    let loading = false;
    deviceList.forEach((list, i) => {
        if (list === device) {
            loading = true;
        }
    });
    return loading;
};