import {getReadingsCSV} from "../services/axios-service";
import {setUser} from "./universal-actions";
import * as debugConstants from "../constants/debug-constants";
import {isLoading, isNotLoading, loadingError} from "./loader-actions";

export const IS_DATA_LOADING = 'IS_DATA_LOADING'
export const isDataLoading = (loading) => ({
    type: IS_DATA_LOADING,
    loading
});

export const IS_DOWNLOAD = 'IS_DOWNLOAD'
export const isDownload = (loading) => ({
    type: IS_DOWNLOAD,
    loading
});

export const IS_DATA_ERROR = 'IS_DATA_ERROR'
export const isDataError = (error, msg) => {
    if (!error) {
        msg = '';
    }
    return ({
        type: IS_DATA_ERROR,
        isError: error,
        msg
    });
};

/**
 * Set the temp chart starting date.
 * @type {string}
 */
export const VISUAL_FROM_DATE = 'VISUAL_FROM_DATE'
export const visualFromDate = (date) => ({
    type: VISUAL_FROM_DATE,
    date
});

/**
 * Set the temp chart ending date.
 * @type {string}
 */
export const VISUAL_TO_DATE = 'VISUAL_TO_DATE'
export const visualToDate = (date) => ({
    type: VISUAL_TO_DATE,
    date
});

export const getReadingsCSVThunk = (user) => async dispatch => {
    try {
        dispatch(isDataLoading(true));
        dispatch(isDataError(false, ""));
        const response = await getReadingsCSV(user);
        if (!response.data.responseBody.includes('success')){
            dispatch(isDataError('Error getting CSV...'));
        }
        dispatch(isDownload(true));
        dispatch(isDataLoading(false));
        return
    } catch (error) {
        dispatch(isDataLoading(false));
        if (process.env.NODE_ENV === 'development' && debugConstants.ALERT_DEBUG_THUNKS) {
            alert(error);
        }
        dispatch(isDataError('Error getting CSV...'));
    }
};
