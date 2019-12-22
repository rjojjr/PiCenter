export const IS_DEVICE_LOADING = 'IS_DEVICE_LOADING'
export const isDeviceLoading = (loading) => ({
    type: IS_DEVICE_LOADING,
    loading
});

export const IS_DEVICE_ERROR = 'IS_DEVICE_ERROR'
export const isDeviceError = (error, msg) => {
    if (!error) {
        msg = '';
    }
    return ({
        type: IS_DEVICE_ERROR,
        isError: error,
        msg
    });
};
