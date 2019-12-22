import {getPiStatuses, restartPitemp} from "../services/axios-service";
import * as debugConstants from "../constants/debug-constants";
import {isDataError, isDataLoading, isDownload} from "./data-actions";

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

export const SET_DEVICE_STATUSES = 'SET_DEVICE_STATUSES'
export const setDeviceStatuses = (status) => ({
    type: SET_DEVICE_STATUSES,
    status
});

export const getStatusesThunk = (user) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true));
        dispatch(isDeviceError(false, ""));
        const response = await getPiStatuses(user);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error getting device statuses...'));
        }
        dispatch(setDeviceStatuses(response.data.deviceStatuses))
        dispatch(isDownload(true));
        dispatch(isDataLoading(false));
        return
    } catch (error) {
        dispatch(isDataLoading(false));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDataError(true, 'Error getting device statuses...'));
    }
};

export const restartPiTempThunk = (user, pi) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true));
        dispatch(isDeviceError(false, ""));
        const response = await restartPitemp(user, pi);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error restarting pitemp...'));
        }
        dispatch(setDeviceStatuses(response.data.deviceStatuses))
        dispatch(isDownload(true));
        dispatch(isDataLoading(false));
        return
    } catch (error) {
        dispatch(isDataLoading(false));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDataError(true, 'Error restarting pitemp...'));
    }
};