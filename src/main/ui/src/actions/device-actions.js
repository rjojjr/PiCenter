import {getPiStatuses, restartDHT, restartPitemp, restartPi, getPiStatus} from "../services/axios-service";
import * as debugConstants from "../constants/debug-constants";
import {isDataError, isDataLoading, isDownload} from "./data-actions";

export const IS_DEVICES_LOADING = 'IS_DEVICES_LOADING'
export const isDevicesLoading = (loading) => ({
    type: IS_DEVICES_LOADING,
    loading
});

export const IS_DEVICE_LOADING = 'IS_DEVICE_LOADING'
export const isDeviceLoading = (loading, device) => ({
    type: IS_DEVICE_LOADING,
    loading,
    device
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

export const SET_DEVICE_STATUS = 'SET_DEVICE_STATUS'
export const setDeviceStatus = (status) => ({
    type: SET_DEVICE_STATUS,
    status
});

export const getStatusesThunk = (user) => async dispatch => {
    try {
        dispatch(isDevicesLoading(true));
        dispatch(isDeviceError(false, ""));
        const response = await getPiStatuses(user);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error getting device statuses...'));
        }
        dispatch(setDeviceStatuses(response.data.deviceStatuses));
        dispatch(isDevicesLoading(false));
    } catch (error) {
        dispatch(isDevicesLoading(false));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDeviceError(true, 'Error getting device statuses...'));
    }
};

export const getStatusThunk = (user, pi) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true, pi));
        dispatch(isDeviceError(false, ""));
        const response = await getPiStatus(user);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error getting device statuses...'));
        }
        dispatch(setDeviceStatus(response.data.deviceStatuses[0]));
        dispatch(isDeviceLoading(false, pi));
    } catch (error) {
        dispatch(isDeviceLoading(false, pi));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDeviceError(true, 'Error getting device statuses...'));
    }
};

export const restartPiTempThunk = (user, pi) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true, pi));
        dispatch(isDeviceError(false, ""));
        const response = await restartPitemp(user, pi);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error restarting pitemp...'));
        }
        dispatch(setDeviceStatus(response.data.deviceStatuses[0]));
        dispatch(isDeviceLoading(false, pi));
    } catch (error) {
        dispatch(isDeviceLoading(false, pi));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDeviceError(true, 'Error restarting pitemp...'));
    }
};

export const restartDHTThunk = (user, pi) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true, pi));
        dispatch(isDeviceError(false, ""));
        const response = await restartDHT(user, pi);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error restarting dht...'));
        }
        dispatch(setDeviceStatus(response.data.deviceStatuses[0]));
        dispatch(isDeviceLoading(false, pi));
    } catch (error) {
        dispatch(isDeviceLoading(false, pi));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDeviceError(true, 'Error restarting dht...'));
    }
};

export const restartPiThunk = (user, pi) => async dispatch => {
    try {
        dispatch(isDeviceLoading(true, pi));
        dispatch(isDeviceError(false, ""));
        const response = await restartPi(user, pi);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDeviceError(true, 'Error restarting pi...'));
        }
        dispatch(setDeviceStatus(response.data.deviceStatuses[0]));
        dispatch(isDeviceLoading(false, pi));
    } catch (error) {
        dispatch(isDeviceLoading(false, pi));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDeviceError(true, 'Error restarting pi...'));
    }
};